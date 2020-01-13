package com.diamond.iain.javagame.entities;

import static com.diamond.iain.javagame.utils.GameConstants.scaledHeight;
import static com.diamond.iain.javagame.utils.GameConstants.scaledWidth;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.diamond.iain.javagame.tiles.Tile;

/**
 * 
 * @author Iain Diamond
 * 
 *         This is the abstract base class for each Missile type:
 *         PlayerMissile and InvaderMissile.
 *
 */

public abstract class Missile implements Tile {

	protected final int SPEED = 5;
	private final int xOffset = 15;
	private final int yOffset = 10;
	private final int missileWidth = 6;
	private final int missileHeight = 22;

	protected int x = 0, y = 0;
	protected BufferedImage missile;

	private boolean active = true;

	@Override
	public void tick() {
		if (y > 0)
			y -= SPEED;
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(missile, x, y, scaledWidth, scaledHeight, null);
	}

	@Override
	public Point getPosition() {
		return new Point(x, y);
	}

	@Override
	public Rectangle getBounds() {
		// A missile is quite a bit smaller than a Tile
		return new Rectangle(x + xOffset, y + yOffset, missileWidth,
				missileHeight);
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void destroy() {
		active = false;
	}
}
