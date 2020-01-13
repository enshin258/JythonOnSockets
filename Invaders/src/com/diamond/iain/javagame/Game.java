package com.diamond.iain.javagame;

/**
 * 
 * @author Iain Diamond
 * 
 * This is the framework class for the Space Invaders game.
 * It contains the outer shell for the game frame / panel, plus
 * generates regular ticks (moves) and renders (draws) for both
 * player and enemies.
 *
 */

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import com.diamond.iain.javagame.entities.Aliens;
import com.diamond.iain.javagame.entities.Player;
import com.diamond.iain.javagame.gfx.ImageLoader;
import com.diamond.iain.javagame.gfx.KeyManager;
import com.diamond.iain.javagame.gfx.SpriteManager;
import com.diamond.iain.javagame.gfx.SpriteSheet;
import com.diamond.iain.javagame.utils.GameConstants;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	public static boolean running = false;
	public Thread gameThread;
	private static JFrame frame;

	private BufferedImage spriteSheet;

	private static Player player;
	private static Aliens aliens;

	// Convenience variables to save typing
	private static int screenWidth = GameConstants.getScreenDimension().width;
	private static int screenHeight = GameConstants.getScreenDimension().height;

	/**
	 * Game Initialisation
	 * 
	 * Loads the spritesheet, creates the Spritesheet manager, player and aliens
	 * entities and sets up a keylistener class.
	 * 
	 */
	public void init() {
		ImageLoader loader = new ImageLoader();

		try {
			spriteSheet = loader.load("/spritesheet.png");
		} catch (IllegalArgumentException e) {
			System.out.println("SpriteSheet not found.");
			System.exit(-1);
		}

		SpriteSheet ss = new SpriteSheet(spriteSheet);
		SpriteManager manager = new SpriteManager(ss);

		player = new Player(manager);
		aliens = new Aliens(manager);

		this.addKeyListener(new KeyManager());
	}

	/**
	 * Starts the game loop
	 * 
	 */
	public synchronized void start() {
		if (running)
			return;
		running = true;
		gameThread = new Thread(this);
		gameThread.start();
	}

	/**
	 * Stops the game loop
	 * 
	 */
	public synchronized void stop() {
		if (!running)
			return;
		running = false;
		try {
			gameThread.join();
		} catch (InterruptedException e) {
			System.out.println("Bad Shutdown error.");
		}
	}

	/**
	 * The main game loop
	 * 
	 */
	@Override
	public void run() {

		init();
		// 18ms produces ~60 fps
		long sleepy = 18;

		while (running) {

			try {
				Thread.sleep(sleepy);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			tick(); // everybody move!
			render(); // everybody draw!
		}
		stop();
	}

	/**
	 * game ticks periodically
	 */
	private void tick() {
		player.tick();
		aliens.tick();
	}

	/**
	 * game renders periodically
	 */
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, screenWidth, screenHeight);

		// Let's draw the moving pieces
		player.render(g);
		aliens.render(g);

		g.dispose(); // tidy up when your finished
		bs.show();
	}

	/**
	 * Space Invaders main
	 * 
	 * Draws the game frame and starts the game loop running
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		Game game = new Game();
		game.setPreferredSize(new Dimension(screenWidth, screenHeight));

		frame = new JFrame("Space Invaders");
		frame.setSize(screenWidth, screenHeight);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(game);
		frame.setVisible(true);

		game.start();
	}

	// allows all classes to check game state
	public static boolean isGameOver() {
		return Aliens.isGameOver();
	}

	// allows KeyManager to update Player with key presses
	public static Player getPlayer() {
		return player;
	}

	// allows KeyManager to update Aliens with key presses
	public static Aliens getAliens() {
		return aliens;
	}
}
