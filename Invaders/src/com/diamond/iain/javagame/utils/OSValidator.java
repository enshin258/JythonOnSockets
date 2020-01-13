package com.diamond.iain.javagame.utils;

/**
 * 
 * @author This class provides methods to detect the current OS
 *
 */
public class OSValidator {

	private static String OS = System.getProperty("os.name").toLowerCase();

	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	public static boolean isMac() {
		return (OS.indexOf("mac") >= 0);
	}
}
