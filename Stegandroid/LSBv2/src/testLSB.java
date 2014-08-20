import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class testLSB {

	static String string_to_hide = "coucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amissscoucou les amisss";

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			string_to_hide += string_to_hide;
			string_to_hide += string_to_hide;
			encode();
			decode();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void encode() throws IOException {
		FileInputStream f = new FileInputStream("aac-good.aac");
		FileOutputStream output = new FileOutputStream("aac-hide.aac");
		
		byte[] content = string_to_hide.getBytes();
		
		LSBEncode steg = new LSBEncode(content, 3);
		while (true)
		{
			String bitStr = "";
			int frameLength = 0;
			byte [] header = new byte[7];
			if (f.read(header) == -1)
				break;
			output.write(header);
			for (int i = 0; i < 13; i++){
				bitStr += Utils.getBitInByteArray(header, 30 + i);
			}
			frameLength = Integer.parseInt(bitStr, 2) - 7;
			byte[] frame = new byte[frameLength];
			f.read(frame);
			
			frame = steg.encodeNextFrame(frame);
			output.write(frame);
		}
		f.close();
		output.close();
	}
	
	public static void decode() throws IOException {
		LSBDecode dec = new LSBDecode();
		byte[] res = null;

		FileInputStream f = new FileInputStream("aac-hide.aac");
		
		while (true)
		{
			String bitStr = "";
			int frameLength = 0;
			byte [] header = new byte[7];
			if (f.read(header) == -1)
				break;

			for (int i = 0; i < 13; i++){
				bitStr += Utils.getBitInByteArray(header, 30 + i);
			}
			frameLength = Integer.parseInt(bitStr, 2) - 7;

			byte[] frame = new byte[frameLength];
			f.read(frame);
		
			res = dec.decodeFrame(frame);
			if (res != null)
				break;
		}
		f.close();
		
		byte[] content = string_to_hide.getBytes();
		Utils.compare(content, res, false);
	}
}

