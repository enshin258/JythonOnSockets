package com.diamond.iain.javagame.entities;

import static com.diamond.iain.javagame.utils.GameConstants.getScreenDimension;
import static com.diamond.iain.javagame.utils.GameConstants.scaledHeight;
import static com.diamond.iain.javagame.utils.GameConstants.scaledWidth;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.diamond.iain.javagame.Game;
import com.diamond.iain.javagame.gfx.SpriteManager;
import com.diamond.iain.javagame.tiles.Tile;

public class Meteor implements Tile {

	protected final int SPEED = 1;
	protected int x = 0, y = 0;
	protected BufferedImage meteor;
	Player player = Game.getPlayer();

	private boolean active = true;

	private int score = 10;

	public Meteor(SpriteManager manager, Point p) {
		x = p.x;
		y = p.y;
		this.meteor = manager.meteor;
	}

	@Override
	public void tick() {
		if (y < getScreenDimension().height) {
			y += SPEED;
		} else {
			this.active = false;
		}
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(meteor, x, y, scaledWidth, scaledHeight, null);

		// Collision detection
		if (player.getBounds().intersects(this.getBounds())) {
			Player.losesOneLife();
			this.destroy();
		}
	}

	@Override
	public void destroy() {
		active = false;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public Point getPosition() {
		return new Point(x, y);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, scaledWidth, scaledHeight);
	}

	public int getScore() {
		return score;
	}

}
