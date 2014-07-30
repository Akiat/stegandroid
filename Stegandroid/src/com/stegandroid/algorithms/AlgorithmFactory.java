package com.stegandroid.algorithms;

import android.util.Log;

public class AlgorithmFactory {

	public static ISteganographyAlgorithm getSteganographyAlgorithmInstanceFromName(String name) {
		ISteganographyAlgorithm algorithm = null;
		Class<?> algorithmClass;
		
		try {
		    algorithmClass = Class.forName(name); 
		    algorithm = (ISteganographyAlgorithm) algorithmClass.newInstance();
		} catch (ClassNotFoundException e) {
			Log.d("DEBUG", "AlgorithmFactory: Unable to find class " + name);
		} catch (InstantiationException e) {
			Log.d("DEBUG", "AlgorithmFactory: Failed to instantiate class " + name);
		} catch (IllegalAccessException e) {
			Log.d("DEBUG", "AlgorithmFactory: Illegal access to " + name);
		}
		return (algorithm);
	}
	
	public static ICryptographyAlgorithm getCryptographyAlgorithmInstanceFromName(String name) {
		ICryptographyAlgorithm algorithm = null;
		Class<?> algorithmClass;
		
		try {
		    algorithmClass = Class.forName(name); 
		    algorithm = (ICryptographyAlgorithm) algorithmClass.newInstance();
		} catch (ClassNotFoundException e) {
			Log.d("DEBUG", "AlgorithmFactory: Unable to find class " + name);
		} catch (InstantiationException e) {
			Log.d("DEBUG", "AlgorithmFactory: Failed to instantiate class " + name);
		} catch (IllegalAccessException e) {
			Log.d("DEBUG", "AlgorithmFactory: Illegal access to " + name);
		}
		return (algorithm);
	}
}
