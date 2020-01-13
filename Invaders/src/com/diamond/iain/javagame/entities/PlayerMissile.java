package com.diamond.iain.javagame.entities;

import java.awt.Point;

import com.diamond.iain.javagame.gfx.SpriteManager;
import com.diamond.iain.javagame.tiles.Tile;

public class PlayerMissile extends Missile implements Tile {

	public PlayerMissile(SpriteManager manager, Point p) {
		x = p.x;
		y = p.y;
		this.missile = manager.playerMissile;
	}
}
