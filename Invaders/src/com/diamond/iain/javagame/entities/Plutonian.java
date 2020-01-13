package com.diamond.iain.javagame.entities;

import java.awt.Point;

import com.diamond.iain.javagame.gfx.SpriteManager;
import com.diamond.iain.javagame.tiles.Tile;

public class Plutonian extends Invader implements Tile {

	public Plutonian(SpriteManager manager, Point p) {
		x = p.x;
		y = p.y;
		this.alien = manager.plutonian;

		score = 30;
	}
}