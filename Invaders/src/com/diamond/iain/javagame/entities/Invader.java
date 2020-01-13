package com.diamond.iain.javagame.entities;

import static com.diamond.iain.javagame.utils.GameConstants.playerYPos;
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
 *         This is the abstract base class for each Invader type: Martian,
 *         Mercurian, Plutonian and Venusian.
 *
 */

public abstract class Invader implements Tile {

	private static final int SPEED = 2; // the start speed
	protected static int speed = SPEED; // speed also controls direction
	protected int x = 0, y = 0;
	protected boolean active = true;
	protected static final int width = scaledWidth;
	protected static final int height = scaledHeight;

	protected BufferedImage alien;

	protected int score = 0;

	@Override
	public void tick() {
		x += speed;
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(alien, x, y, width, height, null);
	}

	/*
	 * returns the current invader's position
	 */
	public Point getPosition() {
		return new Point(x, y);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void destroy() {
		active = false;
	}

	// Let's begin at the start
	public static void restartGame() {
		speed = SPEED;
	}

	/**
	 * Make the invaders faster
	 */
	public static void levelUp() {
		// speed can be negative so be wary when adding
		speed = Math.abs(speed) + 1;
	}

	/**
	 * Modify the sign of speed to indicate rightwards (+ive) or leftwards (-ve)
	 * direction
	 */
	public void reverseDirection() {
		speed *= -1;
	}

	/**
	 * Invaders move down one row
	 */
	public void moveDown() {
		y += height;
	}

	public int getScore() {
		return score;
	}

	public int getWidth() {
		return width;
	}

	/**
	 * 
	 * @return true when the invader reaches the bottom of the screen
	 */
	public boolean reachedPlayer() {
		// true when bottom of invader is 'lower than' top of player,
		// as it appear on-screen
		return y + height >= playerYPos;
	}
}
