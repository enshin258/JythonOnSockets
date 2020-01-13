package com.diamond.iain.javagame.entities;

import static com.diamond.iain.javagame.utils.GameConstants.LeftWall;
import static com.diamond.iain.javagame.utils.GameConstants.RightWall;
import static com.diamond.iain.javagame.utils.GameConstants.TopWall;
import static com.diamond.iain.javagame.utils.GameConstants.getScreenDimension;
import static com.diamond.iain.javagame.utils.GameConstants.getSpacingDimension;
import static com.diamond.iain.javagame.utils.GameConstants.scaledWidth;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.Timer;

import com.diamond.iain.javagame.Game;
import com.diamond.iain.javagame.gfx.SpriteManager;
import com.diamond.iain.javagame.tiles.Tile;

/**
 * 
 * @author Iain Diamond
 * 
 *         This is the controller class for the Space Invaders game. It
 *         constructs the invader army, hold collections for Player Enemy
 *         missiles, plus meteors; moves / renders them while on-screen, and
 *         removes them if destroyed or go off-screen.
 *
 */

public class Aliens {

	// invader data
	private Point anchor = new Point(30, 50);
	private final int numOfInvadersPerRow = 11;
	private final SpriteManager manager;

	// display data
	Point openingMessagePosition = new Point(260, 260);
	Point startGamePosition = new Point(400, 400);
	Font basic = new Font("Dialog", Font.PLAIN, 32);
	Font grande = new Font("Dialog", Font.PLAIN, 100);

	// Ship data
	private final int ShipFreq = 20000;
	private final int ShipInitialDelay = 30000;
	private boolean destroyerTimerRunning = false;
	private boolean bossDefeated = false;
	Random r = new Random();
	Mothership mothership;
	Destroyer destroyer;

	// Meteor data
	private final int MeteorFreq = 8000;
	private final int MeteorInitialDelay = 8000;
	private boolean meteorTimerRunning = false;

	// http://www.javacodegeeks.com/2011/05/avoid-concurrentmodificationexception.html
	private CopyOnWriteArrayList<Invader> invaders = new CopyOnWriteArrayList<>();
	private CopyOnWriteArrayList<Missile> playerMissiles = new CopyOnWriteArrayList<>();
	private CopyOnWriteArrayList<Missile> enemyMissiles = new CopyOnWriteArrayList<>();
	private CopyOnWriteArrayList<Meteor> meteors = new CopyOnWriteArrayList<>();

	Player player = Game.getPlayer();

	enum GameState {
		Active, Over
	}

	private static GameState gameState = GameState.Over;

	enum InvaderType {
		Martian, Plutonian, Mercurian, Venusian
	}

	public Aliens(SpriteManager manager) {
		this.manager = manager;
		destroyer = new Destroyer(manager);
		mothership = new Mothership(manager);
	}

	public void tick() {

		double pos;

		if (isGameOver())
			return;

		// Check if player has lost all their lives
		// e.g. shot by invader missiles or hit
		// by meteors
		if (!Player.isAlive()) {
			gameState = GameState.Over;
			return;
		}

		// Check if the game is about to be over
		invaders.stream().forEach(invader -> {

			if (invader.reachedPlayer()) {
				invader.destroy();
				Player.losesOneLife();
				if (!Player.isAlive()) {
					gameState = GameState.Over;
					return;
				}
			}
		});

		// Mothership reaches player?
		if (mothership.isActive() && mothership.reachedPlayer()) {
			while (!bossDefeated) {
				mothership.destroy();
				Player.losesOneLife();

				// has mothership been completely destroyed?
				if (!mothership.isActive()) {
					mothership.reset();
					bossDefeated = true;
				}

				if (!Player.isAlive()) {
					gameState = GameState.Over;
					return;
				}
			}
		}

		// Is it Boss time?
		if (isArmyDefeated()) {
			if (!bossDefeated) {
				if (!mothership.isActive())
					mothership.setActive(true);
			} else {
				// Boss is Dead! Let's Level Up!
				bossDefeated = false;
				Invader.levelUp();
				mothership.levelUp();
				Player.levelUp();
				buildInvaderArmy();
			}
		}

		// These methods run on timers. A new item is added only when
		// its timer has expired
		addMeteor();
		addDestroyer();

		// check if any invader has hit the left or right wall
		for (Invader invader : invaders) {
			pos = invader.getPosition().getX();
			if (pos + invader.getWidth() > RightWall || pos < LeftWall) {
				invader.reverseDirection();
				invaders.stream().forEach(Invader::moveDown);
				break;
			}
		}

		// Everybody moves. Special invaders use special abilities
		invaders.stream().forEach(invader -> {
			if (invader instanceof Martian) {
				((Martian) invader).fire();
			} else if (invader instanceof Mercurian) {
				((Mercurian) invader).cloak();
			}
			invader.tick();
		});

		// Move each player missile if it is still on screen
		for (Missile m : playerMissiles) {
			if (m.getPosition().getY() > TopWall) {
				m.tick();
			} else {
				// otherwise mark for removal from list
				m.destroy();
			}
		}

		// Move each enemy missile if it is still on screen
		for (Missile m : enemyMissiles) {
			if (m.getPosition().getY() < getScreenDimension().height) {
				m.tick();
			} else {
				// otherwise mark for removal from list
				m.destroy();
			}
		}

		// check if mother-ship is alive and still on the screen
		if (mothership.isActive()) {
			pos = mothership.getPosition().getX();
			if (pos + mothership.getWidth() > RightWall || pos < LeftWall) {
				mothership.reverseDirection();
				mothership.moveDown();
			}
			mothership.fire();
			mothership.tick();
		}

		// check if destroyer is alive and still on the screen
		if (destroyer.isActive()) {
			if (destroyer.getPosition().getX() < RightWall) {
				destroyer.cloak();
				destroyer.fire();
				destroyer.tick();
			} else {
				// gone past wall
				destroyer.setActive(false);
			}
		}

		// Meteors move
		meteors.stream().forEach(Meteor::tick);
	}

	public void render(Graphics g) {

		if (isGameOver()) {
			displayStartGame(g);
			return;
		}

		performCollisionDetection();

		processElements(g, playerMissiles);
		processElements(g, enemyMissiles);
		processElements(g, meteors);
		processElements(g, invaders);

		if (mothership.isActive()) {
			mothership.render(g);
		}

		if (destroyer.isActive()) {
			destroyer.render(g);
		}
	}

	/**
	 * 
	 * Restarts a new game
	 * 
	 * The user can restart the game using a key press. All enemies and players
	 * are reset. Finally the new army is built and the game state changed.
	 * 
	 * @param restart
	 *            true to restart
	 */
	public void restartGame(boolean restart) {
		if (isGameOver() && restart == true) {
			bossDefeated = false;
			tearDownInvaders();
			playerMissiles.clear();
			enemyMissiles.clear();
			meteors.clear();
			Invader.restartGame();
			Player.restartGame();
			destroyer.reset();
			mothership.reset();
			buildInvaderArmy();
			/*
			 * Key presses are asynchronous (whereas tick() is synchronous) so
			 * make sure the invader army is finished construction before
			 * starting the game for real, comparing list + modifying list
			 * simultaneously = BAD!
			 */
			gameState = GameState.Active;
		}
	}

	/**
	 * One of the Enemy classes adds a missile to the collection
	 * 
	 * @param p
	 *            = firing position
	 * 
	 */
	public void addEnemyMissile(Point p) {
		enemyMissiles.add(new InvaderMissile(manager, new Point(p.x, p.y)));
	}

	/**
	 * Player class adds a missile to the collection
	 * 
	 * @param p
	 *            = firing position
	 */
	public void addPlayerMissile(Point p) {
		playerMissiles.add(new PlayerMissile(manager, new Point(p.x, p.y)));
	}

	/**
	 * A convenience function for improved readability
	 * 
	 * @return true = Game Over
	 */
	public static boolean isGameOver() {
		return gameState == GameState.Over;
	}

	/**
	 * Collection detection for player and (all) enemies missiles
	 * 
	 */
	private void performCollisionDetection() {

		// Player missiles collision detection
		playerMissiles.stream().forEach(
				missile -> {
					invaders.stream().forEach(invader -> {
						// a missile should kill one invader only
							if (missile.isActive()
									&& invader.getBounds().intersects(
											missile.getBounds())) {
								Player.addScore(invader.getScore());
								invader.destroy();
								missile.destroy();
							}
						});

					meteors.stream().forEach(
							meteor -> {
								if (meteor.isActive()
										&& meteor.getBounds().intersects(
												missile.getBounds())) {
									Player.addScore(meteor.getScore());
									meteor.destroy();
									missile.destroy();
								}
							});

					if (mothership.isActive()
							&& mothership.getBounds().intersects(
									missile.getBounds())) {
						mothership.destroy();
						missile.destroy();
						// has the mother-ship been killed?
						if (!mothership.isActive()) {
							Player.addScore(mothership.getScore());
							mothership.reset();
							bossDefeated = true;
						}
					}

					// hitting a destroyed ship should yield no further points
					if (destroyer.isActive()
							&& destroyer.getBounds().intersects(
									missile.getBounds())) {
						destroyer.destroy();
						missile.destroy();
						// has the destroyer been killed?
						if (!destroyer.isActive()) {
							Player.addScore(destroyer.getScore());
						}
					}
				});

		// Enemy Missile Collision detection
		enemyMissiles.stream().forEach(missile -> {
			if (player.getBounds().intersects(missile.getBounds())) {
				Player.losesOneLife();
				missile.destroy();
			}
		});
	}

	/**
	 * Process each item in a Tiled ArrayList. This typically contains:
	 * invaders, missiles or meteors. Active Tiles get rendered otherwise they
	 * are removed.
	 * 
	 * Note: Uses generics with type covariance
	 * 
	 * @param g
	 *            the shared Graphics variable
	 * @param list
	 *            An arrayList of Tile items
	 */
	private <T> void processElements(Graphics g,
			CopyOnWriteArrayList<? extends Tile> list) {

		list.stream().forEach(t -> {
			if (t.isActive()) {
				// render each Tile if it is still on the screen
				t.render(g);
			} else {
				// remove 'destroyed Tiles
				list.remove(t);
			}
		});
	}

	/**
	 * 
	 * @return false when all invaders are destroyed
	 */
	private boolean isArmyDefeated() {
		return invaders.size() == 0;
	}

	/**
	 * Displays Space Invaders Welcome Screen
	 * 
	 * @param g
	 * 
	 */
	private void displayStartGame(Graphics g) {
		g.setFont(grande);
		g.setColor(Color.cyan);
		g.drawString("Space Invaders", openingMessagePosition.x,
				openingMessagePosition.y);
		g.setFont(basic);
		g.setColor(Color.white);
		g.drawString("Press 's' to start a new game.", startGamePosition.x,
				startGamePosition.y);
	}

	/**
	 * This method runs through the list of active invaders, marks them as
	 * inactive, stops any timers and finally clears the collection
	 * 
	 */
	private void tearDownInvaders() {
		invaders.stream().forEach(Invader::destroy);
		invaders.clear();
	}

	/**
	 * Add a new destroyer
	 * 
	 * Uses a one-shot timer to trigger randomly appearing destroyer
	 * 
	 */
	private void addDestroyer() {

		if (!destroyerTimerRunning) {
			destroyerTimerRunning = true;

			Timer t = new Timer(r.nextInt(ShipFreq), new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					destroyerTimerRunning = false;
					destroyer.reset();
					destroyer.setActive(true);
				}
			});

			t.setInitialDelay(ShipInitialDelay);
			t.setRepeats(false);
			t.start();
		}
	}

	/**
	 * Add a new meteor
	 * 
	 * Uses a one-shot timer to trigger randomly appearing meteors
	 * 
	 */
	private void addMeteor() {

		if (!meteorTimerRunning) {
			meteorTimerRunning = true;

			Timer t = new Timer(r.nextInt(MeteorFreq), new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					meteorTimerRunning = false;
					int width = (int) (getScreenDimension().getWidth())
							- scaledWidth;
					meteors.add(new Meteor(manager, new Point(
							(r.nextInt(width)), 0)));
				}
			});

			t.setInitialDelay(MeteorInitialDelay);
			t.setRepeats(false);
			t.start();
		}
	}

	/**
	 * Builds a new invader army
	 * 
	 */
	private void buildInvaderArmy() {
		addRow(InvaderType.Martian, 0);
		addRow(InvaderType.Plutonian, 1);
		addRow(InvaderType.Mercurian, 2);
		addRow(InvaderType.Venusian, 3);
	}

	/**
	 * Adds a new row to the Invader army
	 * 
	 * @param invader
	 *            the invader type
	 * @param row
	 *            0..n the row number
	 */
	private void addRow(InvaderType invader, int row) {

		// set vertical gap between rows
		anchor.setLocation(new Point(30, 50 + getSpacingDimension().height
				* row));

		for (int i = 0; i < numOfInvadersPerRow; i++) {

			switch (invader) {
			case Martian:
				invaders.add(new Martian(manager, anchor));
				break;
			case Plutonian:
				invaders.add(new Plutonian(manager, anchor));
				break;
			case Mercurian:
				invaders.add(new Mercurian(manager, anchor));
				break;
			case Venusian:
				invaders.add(new Venusian(manager, anchor));
				break;
			}
			// set horizontal gap between invaders
			anchor.translate(getSpacingDimension().width, 0);
		}
	}
}
