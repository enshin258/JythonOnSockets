package com.diamond.iain.javagame.entities;

import static com.diamond.iain.javagame.utils.GameConstants.scaledHeight;
import static com.diamond.iain.javagame.utils.GameConstants.scaledWidth;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.Timer;

import com.diamond.iain.javagame.Game;
import com.diamond.iain.javagame.gfx.SpriteManager;
import com.diamond.iain.javagame.tiles.CanFire;
import com.diamond.iain.javagame.tiles.Tile;

/**
 * 
 * @author Iain Diamond
 * 
 *         This the abstract base class for each Ship type: Destroyer
 *         and Mothership.
 *
 */

public abstract class Ship implements Tile, CanFire {

	protected static final int SPEED = 1;
	protected int direction = 1;
	protected int speed = SPEED;
	protected int x = 0, y = 0;
	protected boolean active = true;
	protected static final int width = (int) (scaledWidth * 2);
	protected static final int height = (int) (scaledHeight);

	protected Point p;

	protected SpriteManager manager;

	// firing data
	private boolean fireTimerRunning = false;
	private final int ShotRate = 8000;
	protected Random r = new Random();
	Timer t;

	protected BufferedImage ship;

	protected int startLives = 0;
	protected int lives = 0;
	protected int score = 0;

	@Override
	public void tick() {
		x += speed;
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(ship, x, y, width, height, null);
	}

	public boolean isActive() {
		return active;
	}

	/**
	 * Ships take a number of hits before being destroyed
	 */
	public void destroy() {
		lives -= 1;
		if (lives <= 0) {
			setActive(false);
		}
	}

	public Point getPosition() {
		return new Point(x, y);
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

	/**
	 * Generates missiles at random intervals defined by ShotRate
	 * 
	 */
	@Override
	public void fire() {
	
		if (!fireTimerRunning) {
			fireTimerRunning = true;
	
			t = new Timer(r.nextInt(ShotRate), new ActionListener() {
	
				@Override
				public void actionPerformed(ActionEvent arg0) {
					fireTimerRunning = false;
					Game.getAliens().addEnemyMissile(
							new Point(x + width / 2, y + height));
				}
			});
	
			t.setRepeats(false);
			t.start();
		}
	}

	public int getScore() {
		return score;
	}

	/**
	 * Sets the Ship's Active state. When the ship is set inactive the timer is
	 * stopped otherwise the timer is started
	 * 
	 * @param flag
	 *            true = start ship false = ship destroyed
	 */
	public void setActive(boolean flag) {
		active = flag;
		if (!active) {
			t.stop();
		} else {
			if (t != null && !t.isRunning()) {
				t.start();
			}
		}
	}

	public int getWidth() {
		return width;
	}

	/**
	 * Resets the ship's position, direction and number of lives
	 * 
	 */
	public void reset() {
		lives = startLives;
		active = false;

		// Always start moving left to right (i.e. +ive value)
		direction = 1;

		// Move to start position
		x = p.x;
		y = p.y;
	}
}
