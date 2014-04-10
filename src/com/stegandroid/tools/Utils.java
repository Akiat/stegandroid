package com.stegandroid.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import android.content.Context;
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
		    	 if (tmp.substring(0, tmp.lastIndexOf(".")).equals(packName)) {
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
}
