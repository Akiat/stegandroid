import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class testLSB {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			Path path = Paths.get("video.h264");
			String string_to_hide = "coucou les amisss";
			
			byte[] content = string_to_hide.getBytes();
			byte[] signal  = Files.readAllBytes(path);

			Steg steg = new Steg(signal, content, 2);

			byte[] encoded = steg.encode();
			byte[] decoded = steg.decode(2000);

			steg.compare(content, decoded);
			steg.createFile(encoded);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
