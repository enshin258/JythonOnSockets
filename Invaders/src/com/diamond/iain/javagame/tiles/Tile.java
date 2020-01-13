package com.diamond.iain.javagame.tiles;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * 
 * @author Iain Diamond
 * 
 *         This interface defines the properties (methods) each Tile, or Sprite,
 *         must implement.
 *
 */

public interface Tile {

	// Move Tile
	public void tick();

	// Draw Tile
	public void render(Graphics g);

	// Tile has hit!
	public void destroy();

	// Is Tile Alive (or Dead)?
	public boolean isActive();

	// Where are we on the screen?
	public Point getPosition();

	// Get coordinates of Rectangle surrounding Tile,
	// Used for collision detection
	public Rectangle getBounds();
}
