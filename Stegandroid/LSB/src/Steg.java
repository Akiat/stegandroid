import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;


public class Steg {

	private static final int BYTE_SIZE 		= 8;
	private static final int INT_SIZE 		= BYTE_SIZE * 4;

	private int _begin	 		 		= 2000;
	private int _content_byte_length 	= 0;
	private int _content_bit_length 	= 0;
	private int _nbBitToHideInOneByte	= 1;

	private byte[] _signal				= null;
	private byte[] _content				= null;

	public byte[] encoded				= null;
	public byte[] decoded				= null;


	public Steg(byte[] signal, byte[] content, int nbBitToHideInOneByte) {

		_signal 				= signal;
		_content 				= content;

		_content_byte_length 	= content.length;

		_content_bit_length 	= _content_byte_length * BYTE_SIZE;

		_nbBitToHideInOneByte 	= nbBitToHideInOneByte;

		encoded 				= signal.clone();
		decoded 				= new byte[_content_byte_length];

	}

	public byte[] encode() {

		hideInteger(_content_byte_length); // LSB sur 32 octets pour push le nb d'octets cachés
		hideInteger(_nbBitToHideInOneByte); // LSB sur 32 octets pour push _nbBitToHideInOneByte

		int i = _begin;
		for (int bitCount = 0; bitCount < _content_bit_length;) {
			for (int j = 0; (j < _nbBitToHideInOneByte) && (bitCount < _content_bit_length); j++) {
				int bitValue = Utils.getBitInByteArray(_content, bitCount);
				encoded[i] = Utils.setSpecificBit(encoded[i], bitValue, j);
				bitCount++;
			}
			i++;
		}

		return encoded;
	}

	public byte[] decode(int begin) {
		int nbOfHiddenByte = getHiddenInteger(begin);
		int nbBitToDecodeInOneByte = getHiddenInteger(begin + INT_SIZE);

		int content_bit_length = nbOfHiddenByte * BYTE_SIZE;
		int j = begin + (2 * INT_SIZE);
		for (int i = 0; i < content_bit_length;) {
			for (int k = 0; (k < nbBitToDecodeInOneByte) && (i < content_bit_length) ; k++) {
				int bitValue = Utils.getBit(encoded[j], k);
				decoded = Utils.setBitInByteArray(decoded, bitValue, i);
				i++;
			}
			j++;

		}

		return decoded;
	}

	private int getHiddenInteger(int offset) {
		String bitStr = "";

		int max = offset + (BYTE_SIZE * 4);
		for (int i = offset; i < max; i++) {
			bitStr += Utils.getLSB(_signal[i]);
		}
		int hiddenInt = Integer.parseInt(bitStr, 2);

		return hiddenInt;
	}

	private void hideInteger(int toHide) {
		ByteBuffer b = ByteBuffer.allocate(4);
		//b.order(ByteOrder.BIG_ENDIAN);
		b.putInt(toHide);
		byte[] toHideByteArray = b.array();

		int end = _begin + INT_SIZE;
		for (int i = _begin; i < end; i++) {
			int bitValue = Utils.getBitInByteArray(toHideByteArray, i - _begin);
			_signal[i] = Utils.setLSB(_signal[i], bitValue);
		}

		_begin += INT_SIZE;
	}

	public void createFile(byte[] bytes){
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("output.h264");
			fos.write(bytes);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void compare(byte[] original, byte[] decoded){
		int max = original.length;
		for (int i = 0; i<max; i++){
			System.out.println(Utils.byteToBinStr(original[i]) + " | " + Utils.byteToBinStr(decoded[i]));
		}

		try {
			System.out.println(new String(original, "US-ASCII"));
			System.out.println(new String(decoded, "US-ASCII"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}	
}
