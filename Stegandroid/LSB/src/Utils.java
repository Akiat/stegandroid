public class Utils {
	
	/**
	 * @param b The byte you want to change the LSB.
	 * @param bitValue The Less Significant Bit to modify b.
	 * @return Return b, with the LSB modified by lsbToSet.
	 */
	public static byte setLSB(byte b, int bitValue) {
		byte res = (byte) (b & ~1 | bitValue);
		
		/*
		System.out.println("setLSB - Avant: " + ByteToBinStr(b));
		System.out.println("setLSB - Après: " + ByteToBinStr(res) + " bitValue = " + bitValue);
		System.out.println("---------------");
		*/
		return res;
	}
	
	/**
	 * @param b The byte you want to change the bit.
	 * @param bitValue The Less Significant Bit to modify b.
	 * @param offset The index of the needed bit (e.g. 0b10000001 : index 7 for the MSB, index 0 for the LSB)
	 * @return Return b, with the byte number 'offset' modified by lsbToSet.
	 */
	public static byte setSpecificBit(byte b, int bitValue, int offset) {
		byte res;
		if (bitValue == 1)
			res = (byte) (b | (1 << offset));
		else
			res = (byte) (b & ~(1 << offset));
		
		/*
		System.out.println("setSpecificBit - Avant: " + ByteToBinStr(b) + " bitValue = " + bitValue);
		System.out.println("setSpecificBit - Après: " + ByteToBinStr(res) + " offset = " + offset);
		System.out.println("---------------");
		*/
		return res;
	}
	
	/**
	 * @param b The byte you want the string.
	 * @return The binary string representation of b.
	 */
	public static String byteToBinStr(byte b) {
		String s1 = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
		return s1;
	}
	
	/**
	 * @param b The byte which contain the needed bit.
	 * @param index The index of the needed bit (e.g. 0b10000001 : index 7 for the MSB, index 0 for the LSB)
	 * @return Return an int which contain the value of needed bit (so, 0 or 1).
	 */
	public static int getBit(byte b, int index) {
		return b >> index & 1;
	}
	
	/**
	 * @param b The byte which contain the needed bit.
	 * @return Return an int which contain the value of LSB bit (so, 0 or 1).
	 */
	public static int getLSB(byte b) {
		return b >> 0 & 1;
	}
	
	public static int getBitInByteArray(byte[] array, int bitOffset) {
		int byteNb = (int)bitOffset / 8;
		int[] invert = {7,6,5,4,3,2,1,0};
		int bitNb =  invert[bitOffset - (byteNb * 8)];
		
		return Utils.getBit(array[byteNb], bitNb);
	}
	
	public static byte[] setBitInByteArray(byte[] array, int bitValue, int bitOffset) {
		int byteNb = (int)bitOffset / 8;
		int[] invert = {7,6,5,4,3,2,1,0};
		int bitNb =  invert[bitOffset - (byteNb * 8)];

		array[byteNb] = Utils.setSpecificBit(array[byteNb], bitValue, bitNb);
		return array;
	}
}