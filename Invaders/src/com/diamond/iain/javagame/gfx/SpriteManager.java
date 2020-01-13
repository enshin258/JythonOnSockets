package com.diamond.iain.javagame.gfx;

import java.awt.image.BufferedImage;

import static com.diamond.iain.javagame.utils.GameConstants.*;

/**
 * 
 * @author Iain Diamond
 *
 *         Splits the Sprite sheet into individual BufferedImages
 */

public class SpriteManager {

	public BufferedImage martian;
	public BufferedImage venusian;
	public BufferedImage plutonian;
	public BufferedImage mercurian;
	public BufferedImage player;
	public BufferedImage playerMissile;
	public BufferedImage invaderMissile;
	public BufferedImage destroyer;
	public BufferedImage mothership;
	public BufferedImage meteor;

	public SpriteManager(SpriteSheet ss) {

		// row 1
		martian = ss.crop(0, 0, TileWidth, TileHeight);
		plutonian = ss.crop(1, 0, TileWidth, TileHeight);
		player = ss.crop(2, 0, TileWidth, TileHeight);

		// row 2
		venusian = ss.crop(0, 1, TileWidth, TileHeight);
		mercurian = ss.crop(1, 1, TileWidth, TileHeight);
		playerMissile = ss.crop(2, 1, TileWidth, TileHeight);

		// row 3
		destroyer = ss.crop(0, 2, TileWidth * 2, TileHeight);
		invaderMissile = ss.crop(2, 2, TileWidth, TileHeight);

		// row 4
		mothership = ss.crop(0, 3, TileWidth * 2, TileHeight);
		meteor = ss.crop(2, 3, TileWidth, TileHeight);
	}
}
