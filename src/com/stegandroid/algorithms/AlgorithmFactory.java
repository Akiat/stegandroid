package com.stegandroid.algorithms;

import android.util.Log;

public class AlgorithmFactory {

	public static IAlgorithm getInstanceFromName(String name) {
		IAlgorithm algorithm = null;
		Class<?> algorithmClass;
		
		try {
		    algorithmClass = Class.forName(name); 
		    algorithm = (IAlgorithm) algorithmClass.newInstance();
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
