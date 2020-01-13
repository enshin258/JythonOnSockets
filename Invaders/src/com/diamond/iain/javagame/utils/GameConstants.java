package com.diamond.iain.javagame.utils;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * 
 * @author Iain Diamond
 * 
 *         This class contains the shared game constants
 *
 */

public class GameConstants {

	private static final int MacXSpacing = 55;
	private static final int MacYSpacing = 35;

	private static final int PCScreenWidth = 1200;
	private static final int PCScreenHeight = 1000;
	private static final int PCXSpacing = 45;
	private static final int PCYSpacing = 25;

	public static final int TileHeight = 40;
	public static final int TileWidth = 60;

	public static final int scaledHeight = (int) (TileHeight / getScaling());
	public static final int scaledWidth = (int) (TileWidth / getScaling());

	public static final int TopWall = scaledHeight;
	public static final int LeftWall = 0;
	public static final int RightWall = getScreenDimension().width;

	public static final int playerYPos = getScreenDimension().height - 80;
	public static final int missileYPos = playerYPos - scaledHeight;

	// These are convenience methods so that the game can be run on two
	// different screen sizes (PC and Mac)

	public static Dimension getScreenDimension() {
		if (OSValidator.isMac()) {
			return Toolkit.getDefaultToolkit().getScreenSize();
		}
		return new Dimension(PCScreenWidth, PCScreenHeight);
	}

	// XSpacing = width, YSpacing = height
	public static Dimension getSpacingDimension() {
		if (OSValidator.isMac()) {
			return new Dimension(MacXSpacing, MacYSpacing);
		}
		return new Dimension(PCXSpacing, PCYSpacing);
	}

	private static double getScaling() {
		if (OSValidator.isMac()) {
			return 1.7;
		}
		return 2;
	}

}
