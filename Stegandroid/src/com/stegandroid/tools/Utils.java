package com.stegandroid.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import dalvik.system.DexFile;

public class Utils {
	
	/**
	 * @param b The byte you want to change the LSB.
	 * @param lsbToSet The Less Significant Bit to modify b.
	 * @return Return b, with the LSB modified by lsbToSet.
	 */
	public static byte setLSB(byte b, int lsbToSet) {
		byte res = (byte) (b & ~1 | lsbToSet);
		return res;
	}
	
	/**
	 * @param b The byte you want the string.
	 * @return The binary string representation of b.
	 */
	public static String ByteToBinStr(byte b) {
		String s1 = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
		return s1;
	}
	
	/**
	 * @param b The byte which contain the needed bit.
	 * @param index The index of the needed bit (e.g. 0b10000001 : index 0 for the MSB '1', index 7 for the LSB '1')
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
		System.out.println("setSpecificBit - Apr�s: " + ByteToBinStr(res) + " offset = " + offset);
		System.out.println("---------------");
		*/
		return res;
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
	
	public static List<String> getClassesPathFromPackage(Context context, String packName) {
		DexFile dex;
		Enumeration<String> enumClasses;
		String tmp;
		List<String> classes = new ArrayList<String>();
		
		 try {
			 dex = new DexFile(context.getPackageCodePath());
		     enumClasses = dex.entries();
		     while (enumClasses.hasMoreElements()) {
		    	 tmp = enumClasses.nextElement();
		    	 if (tmp.substring(0, tmp.lastIndexOf(".")).equals(packName) && !tmp.contains("$")) {
		    		 classes.add(tmp);
		    	 }
		     }
		 } catch (IOException e) {
			 e.printStackTrace();
		 }
		 return (classes);
	}
	
	public static String convertClassNameToReadableName(String name) {
		StringBuffer res = new StringBuffer("");
		
		if (name.contains(".") && name.lastIndexOf(".") != name.length() - 1) {
			res.append(name.substring(name.lastIndexOf(".") + 1));
		} else {
			res.append(name);
		}
		
		if (!res.toString().toUpperCase().equals(res.toString())) {
			for (int i = 0; i < res.length(); ++i) {
				if ((res.charAt(i) >= 'A' && res.charAt(i) <= 'Z' || res.charAt(i) >= '0' && res.charAt(i) <= '9')
						&& i != 0 && res.charAt(i - 1) != ' ') {
					res.insert(i, ' ');
					++i;
				}
			}
		}
		return (res.toString());
	}

	public static String getBasenameFromPath(String path) {
		StringBuilder sb; 
		String [] chunks;
		
		sb = new StringBuilder();
		if (path == null) {
			return (sb.toString());
		}
		
		chunks = path.split("/");
		if (chunks == null || chunks.length == 0) {
			return (sb.toString());
		}
		
		for (int i = 0; i < chunks.length - 1; ++i) {
			if (chunks[i] != null && chunks[i].length() != 0) {
				sb.append('/').append(chunks[i]);
			}
		}
		return (sb.toString());
	}

	public static String getRealPathFromUri(Context context, Uri uri) {
		Cursor cursor;
		String [] projection = { MediaStore.Video.Media.DATA };
		int idx;
		String ret = "";
		
		cursor = context.getContentResolver().query(uri, projection, null, null, null);
		if (cursor == null) {
			return (uri.getPath());
		}
		idx = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
		if (idx != -1) {
			cursor.moveToFirst();
			ret = cursor.getString(idx);
		}
		cursor.close();
		return (ret);
	}
	
	public static String getFileNameFromPath(String path) {
		String ret = "";
		File file;
		
		if (path != null) {
			file = new File(path);
			ret = file.getName();
		}
		return (ret);
	}
	
	public static byte[] getContentOfFileAsByteArray(String path) {
		FileInputStream inputStream;
		File file;
		byte [] ret = null;
		
		file = new File(path);
		try {
			inputStream = new FileInputStream(new File(path));
			ret = new byte[(int) file.length()];
			inputStream.read(ret);
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return ret;
	}
	
}
