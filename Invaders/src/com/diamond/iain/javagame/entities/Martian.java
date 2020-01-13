package com.diamond.iain.javagame.entities;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.Timer;

import com.diamond.iain.javagame.Game;
import com.diamond.iain.javagame.gfx.SpriteManager;
import com.diamond.iain.javagame.tiles.CanFire;
import com.diamond.iain.javagame.tiles.Tile;

public class Martian extends Invader implements Tile, CanFire {

	private SpriteManager manager;
	private boolean timerRunning = false;
	private final int ShotRate = 40000;
	Player player = Game.getPlayer();
	Random r = new Random();
	Timer t;

	public Martian(SpriteManager spriteManager, Point p) {
		x = p.x;
		y = p.y;
		this.manager = spriteManager;
		this.alien = manager.martian;

		score = 40;
	}

	@Override
	public void tick() {
		x += speed;
	}

	@Override
	public void destroy() {
		t.stop();
		active = false;
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(alien, x, y, width, height, null);
	}

	/**
	 * Generates missiles at random intervals defined by ShotRate
	 * 
	 */
	@Override
	public void fire() {

		if (!timerRunning) {
			timerRunning = true;

			t = new Timer(r.nextInt(ShotRate), new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					timerRunning = false;
					Game.getAliens().addEnemyMissile(new Point(x, y + height));
				}
			});

			t.setRepeats(false);
			t.start();
		}
	}
}
