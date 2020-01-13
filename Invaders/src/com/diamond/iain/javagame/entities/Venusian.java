package com.diamond.iain.javagame.entities;

import java.awt.Point;

import com.diamond.iain.javagame.gfx.SpriteManager;
import com.diamond.iain.javagame.tiles.Tile;

public class Venusian extends Invader implements Tile {

	public Venusian(SpriteManager manager, Point p) {
		x = p.x;
		y = p.y;
		this.alien = manager.venusian;

		score = 10;
	}
}