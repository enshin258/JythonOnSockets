package com.diamond.iain.javagame.entities;

import static com.diamond.iain.javagame.utils.GameConstants.LeftWall;
import static com.diamond.iain.javagame.utils.GameConstants.RightWall;
import static com.diamond.iain.javagame.utils.GameConstants.getScreenDimension;
import static com.diamond.iain.javagame.utils.GameConstants.missileYPos;
import static com.diamond.iain.javagame.utils.GameConstants.playerYPos;
import static com.diamond.iain.javagame.utils.GameConstants.scaledHeight;
import static com.diamond.iain.javagame.utils.GameConstants.scaledWidth;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.diamond.iain.javagame.Game;
import com.diamond.iain.javagame.gfx.SpriteManager;
import com.diamond.iain.javagame.tiles.Tile;
import com.diamond.iain.javagame.utils.FileIOManager;

public class Player implements Tile {

	private static final int SPEED = 6;
	private static final Point StartPosition = new Point(30, playerYPos);
	private int x = 0, y = 0;
	private boolean right = false, left = false;
	private BufferedImage player;
	private SpriteManager manager;

	private static int level = 1;
	private static int lives = 3;

	// Scoring data
	private static int totalScore = 0;
	private static int highScore = 0;

	// display data
	private Font f;
	private Point scorePosition = new Point(20, 20);
	private Point levelPosition = new Point(getScreenDimension().width / 2, 20);
	private Point livesPosition = new Point(20,
			getScreenDimension().height - 45);
	private Point highScorePosition = new Point(
			getScreenDimension().width - 200, 20);

	// firing data
	long lastPressed = System.currentTimeMillis();

	public Player(SpriteManager spriteManager) {
		this.x = StartPosition.x;
		this.y = StartPosition.y;
		this.player = spriteManager.player;
		manager = spriteManager;
		highScore = FileIOManager.readHighScoreFromFile();
		f = new Font("Dialog", Font.PLAIN, 18);

	}

	@Override
	public void tick() {

		// Move Player
		if (left && x >= LeftWall) {
			x -= SPEED;
		}

		// check player's next move doesn't go off screen
		if (right && x + SPEED <= RightWall - scaledWidth) {
			x += SPEED;
		}
	}

	@Override
	public void render(Graphics g) {

		updateScore(g);

		g.drawImage(player, x, y, scaledWidth, scaledHeight, null);
	}

	@Override
	public Point getPosition() {
		return new Point(x, y);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, scaledWidth, scaledHeight);
	}

	// stub: see isAlive()
	@Override
	public boolean isActive() {
		return false;
	}

	/**
	 * The Player Tile has been hit
	 */
	@Override
	public void destroy() {
		lives -= 1;
	}

	/**
	 * The player has started a new game. Saves the score (if a new high score)
	 * before resetting.
	 */
	public static void restartGame() {
		if (totalScore > highScore) {
			highScore = totalScore;
			FileIOManager.writeHighScoreToFile(highScore);
		}
		level = 1;
		lives = 3;
		totalScore = 0;
	}

	/**
	 * Adds points to the players current score
	 * 
	 * @param score
	 */
	public static void addScore(int score) {
		totalScore += score;
	}

	public static void levelUp() {
		level += 1;
	}

	public static int getLevel() {
		return level;
	}

	/**
	 * Updates the number of player lives, starts at 3. Note: the displayed
	 * value must never be negative.
	 */
	public static void losesOneLife() {
		lives = lives > 0 ? lives -= 1 : 0;
	}

	/**
	 * This method is used instead of Tile:isActive() so that other classes can
	 * call a static function.
	 * 
	 * @return true = Player alive
	 */
	public static boolean isAlive() {
		return lives > 0;
	}

	/**
	 * User presses right key
	 * 
	 * @param right true = pressed
	 * 
	 */
	public void setRight(boolean right) {
		this.right = right;
	}

	/**
	 * User presses left key
	 * 
	 * @param left true = pressed
	 * 
	 */
	public void setLeft(boolean left) {
		this.left = left;
	}

	/**
	 * Called when user presses the fire key (space bar) A minimum duration is
	 * calculated to limit the fire rate.
	 * 
	 * @param fire
	 *            true when fire pressed down
	 */
	public void firePressed(boolean fire) {

		long now = System.currentTimeMillis();
		long duration = now - lastPressed;

		// Avoid the machine gun effect
		if (fire && duration > 600) {
			// create new missile at player's x position
			Game.getAliens().addPlayerMissile(new Point(x, missileYPos));
			lastPressed = System.currentTimeMillis();
		}
	}

	/**
	 * Draws the game info: Score, lives, level, high score
	 * 
	 * @param g
	 */
	private void updateScore(Graphics g) {
		g.setFont(f);
		g.setColor(Color.white);
		g.drawString("Player Score: " + totalScore, scorePosition.x,
				scorePosition.y);
		g.drawString("Lives " + lives, livesPosition.x, livesPosition.y);
		g.drawString("Level " + level, levelPosition.x, levelPosition.y);
		g.drawString("High Score: " + highScore, highScorePosition.x,
				highScorePosition.y);
	}
}
