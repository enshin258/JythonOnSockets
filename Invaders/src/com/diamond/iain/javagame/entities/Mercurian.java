package com.diamond.iain.javagame.entities;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.Timer;

import com.diamond.iain.javagame.gfx.SpriteManager;
import com.diamond.iain.javagame.tiles.Cloakable;
import com.diamond.iain.javagame.tiles.Tile;

public class Mercurian extends Invader implements Tile, Cloakable {

	private boolean timerRunning = false;
	private final int cloakCycle = 20000;
	private final int cloakDuration = 3000;
	private boolean cloakEngaged = false;
	private Random r = new Random();

	Timer cloak;

	public Mercurian(SpriteManager manager, Point p) {
		x = p.x;
		y = p.y;
		this.alien = manager.mercurian;

		score = 20;

		// This is a fixed timer so set it up once
		cloak = new Timer(cloakDuration, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				timerRunning = false;
				cloakEngaged = false;
			}
		});

		cloak.setRepeats(false);
	}

	@Override
	public void render(Graphics g) {
		if (!cloakEngaged) {
			g.drawImage(alien, x, y, width, height, null);
		}
	}

	/**
	 * Mercurian invaders can cloak
	 * 
	 * Uses a one-shot timer to cloak the invader at random intervals
	 * 
	 */
	@Override
	public void cloak() {

		if (!timerRunning) {
			timerRunning = true;

			Timer t = new Timer(r.nextInt(cloakCycle), new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// switch the cloak on
					cloakEngaged = true;
					cloak.start();
				}
			});

			t.setRepeats(false);
			t.start();
		}
	}
}