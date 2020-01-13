package com.diamond.iain.javagame.gfx;

import java.awt.image.BufferedImage;
import static com.diamond.iain.javagame.utils.GameConstants.*;

/**
 * 
 * @author Iain Diamond
 * 
 *         Provides a crop methods to 'chop up' the Sprite sheet
 *
 */

public class SpriteSheet {
	private BufferedImage sheet;

	public SpriteSheet(BufferedImage sheet) {
		this.sheet = sheet;
	}

	public BufferedImage crop(int col, int row, int width, int height) {
		return sheet.getSubimage(col * TileWidth, row * TileHeight, width,
				height);
	}
}
