package com.jgw.framework;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 * Actual Game
 */

public class Game {
	// Space rocket
	private PlayerRocket playerRocket;

	// Landing area
	private LandingArea landingArea;

	ArrayList<Meteor> meteors = new ArrayList<Meteor>();
	
	// Game background
	private BufferedImage backgroundImg;

	// Red boarder around frame (when the player crashes)
	private BufferedImage redBorderImg;

	// Temporary string (placeholder)
	private String tmpStr;

	private Random rand = new Random();

	public Game() {
		Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;

		Thread threadForInitGame = new Thread() {
			@Override
			public void run() {
				// Sets variables and objects for game
				Initialize();
				// Load game files (images, sounds, etc.)
				LoadContent();

				Framework.gameState = Framework.GameState.PLAYING;
			}
		};
		threadForInitGame.start();
	}

	// Sets variables and objects for game
	private void Initialize() {
		playerRocket = new PlayerRocket();
		landingArea = new LandingArea();
		for (int i = 0; i < 50; i++) {
			Meteor m = new Meteor();
			meteors.add(m);
		}
	}

	// Load game files (images, sounds, etc.)
	private void LoadContent() {
		try {
			URL backgroundImgUrl = this.getClass().getResource(
					"data/background.jpg");
			backgroundImg = ImageIO.read(backgroundImgUrl);

			URL redBorderImgUrl = this.getClass().getResource(
					"data/red_border.png");
			redBorderImg = ImageIO.read(redBorderImgUrl);
		} catch (IOException ex) {
			Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	// Restart (resets some variables)
	public void RestartGame() {
		playerRocket.ResetPlayer();
		for (int i = 0; i < meteors.size(); i++) {
			Meteor m = (Meteor) meteors.get(i);
			m.ResetMeteor();
		}
	}

	/**
	 * <p>
	 * Update game logic
	 * 
	 * @param gameTime
	 *            gameTime of the game
	 * @param mousePosition
	 *            current mouse position
	 */
	public void updateGame(long gameTime, Point mousePosition) {
		for (int i = 0; i < meteors.size(); i++) {
			Meteor m = (Meteor) meteors.get(i);
			m.Update();
			if (m.getX() > Framework.frameWidth || m.getY() > Framework.frameHeight) {
				meteors.remove(i);
				Meteor m2 = new Meteor();
				if (rand.nextInt(2) == 1) {
					m2.setX(rand.nextInt(Framework.frameWidth));
					m2.setY(-10);
				} else {
					m2.setX(-10);
					m2.setY(rand.nextInt(Framework.frameHeight));
				}
				meteors.add(m2);
			}
		}

		// Move rocket
		playerRocket.Update();

		// Check where the rocket is (in space, landed, or crashed)
		// First, check bottom y coord of rocket if it's near landing area
		if (playerRocket.y + playerRocket.rocketImgHeight - 10 > landingArea.y) {
			// Second, check if rocket is over the landing area
			if ((playerRocket.x > landingArea.x)
					&& (playerRocket.x < landingArea.x
							+ landingArea.landingAreaImgWidth
							- playerRocket.rocketImgWidth)) {
				// Third, check if rocket speed is too high
				if (playerRocket.speedY <= playerRocket.topLandingSpeed) {
					playerRocket.landed = true;
				} else {
					playerRocket.crashed = false;
				}
			} else {
				playerRocket.crashed = true;
			}

			Framework.gameState = Framework.GameState.GAMEOVER;
		}
	}

	/**
	 * <p>
	 * Draw game on screen
	 * 
	 * @param g2d
	 *            Graphics2D
	 * @param mousePosition
	 *            current mouse position
	 */
	public void Draw(Graphics2D g2d, Point mousePosition) {
		g2d.drawImage(backgroundImg, 0, 0, Framework.frameWidth,
				Framework.frameHeight, null);
		landingArea.Draw(g2d);
		playerRocket.Draw(g2d);
		for (int i = 0; i < meteors.size(); i++) {
			Meteor m = (Meteor) meteors.get(i);
			m.Draw(g2d);
		}
	}

	/**
	 * Draw Game Over screen
	 * 
	 * @param g2d
	 *            Graphics2D
	 * @param mousePosition
	 *            Current mouse position
	 * @param gameTime
	 *            Game time in nanosecs
	 */
	public void DrawGameOver(Graphics2D g2d, Point mousePosition, long gameTime) {
		Draw(g2d, mousePosition);

		tmpStr = "Press space or enter to restart.";
		g2d.drawString(tmpStr, (Framework.frameWidth / 2)
				- (g2d.getFontMetrics().stringWidth(tmpStr) / 2),
				Framework.frameHeight / 3 + 100);

		if (playerRocket.landed) {
			tmpStr = "You have successfully landed!";
			g2d.drawString(tmpStr, (Framework.frameWidth / 2)
					- (g2d.getFontMetrics().stringWidth(tmpStr) / 2),
					Framework.frameHeight / 3);

			tmpStr = "You have landed in " + gameTime / Framework.secInNanosec
					+ " seconds.";
			g2d.drawString(tmpStr, (Framework.frameWidth / 2)
					- (g2d.getFontMetrics().stringWidth(tmpStr) / 2),
					Framework.frameHeight / 3 + 40);
		} else {
			g2d.setColor(Color.RED);

			tmpStr = "You have crashed the rocket!";
			g2d.drawString(tmpStr, (Framework.frameWidth / 2)
					- (g2d.getFontMetrics().stringWidth(tmpStr) / 2),
					Framework.frameHeight / 3);
			g2d.drawImage(redBorderImg, 0, 0, Framework.frameWidth,
					Framework.frameHeight, null);
		}
	}
}