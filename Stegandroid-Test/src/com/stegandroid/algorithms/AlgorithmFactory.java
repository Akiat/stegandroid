package com.stegandroid.algorithms;


public class AlgorithmFactory {

	public static ISteganographyContainer getSteganographyContainerInstanceFromName(String name) {
		ISteganographyContainer algorithm = null;
		Class<?> algorithmClass;
		
		try {
		    algorithmClass = Class.forName(name); 
		    algorithm = (ISteganographyContainer) algorithmClass.newInstance();
		} catch (ClassNotFoundException e) {
			System.err.println("[Algorithm Factory]: Unable to find class " + name);
		} catch (InstantiationException e) {
			System.err.println("[Algorithm Factory]: Failed to instantiate class " + name);
		} catch (IllegalAccessException e) {
			System.err.println("[Algorithm Factory]: Illegal access to " + name);
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
			System.err.println("[Algorithm Factory]: Unable to find class " + name);
		} catch (InstantiationException e) {
			System.err.println("[Algorithm Factory]: Failed to instantiate class " + name);
		} catch (IllegalAccessException e) {
			System.err.println("[Algorithm Factory]: Illegal access to " + name);
		}
		return (algorithm);
	}
}
