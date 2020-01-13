package com.diamond.iain.javagame.entities;

import static com.diamond.iain.javagame.utils.GameConstants.getScreenDimension;

import java.awt.Point;

import com.diamond.iain.javagame.gfx.SpriteManager;
import com.diamond.iain.javagame.tiles.Tile;

public class InvaderMissile extends Missile implements Tile {

	public InvaderMissile(SpriteManager manager, Point p) {
		x = p.x;
		y = p.y;
		this.missile = manager.invaderMissile;
	}

	@Override
	public void tick() {
		if (y < getScreenDimension().height)
			y += SPEED;
	}
}