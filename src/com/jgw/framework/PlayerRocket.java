package com.jgw.framework;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class PlayerRocket {
	// Use this to randomize starting x-coord
	private Random rand;

	// Rocket coords
	public int x, y;

	// Rocket states (landed, crashed)
	public boolean landed, crashed;

	// Rocket's speed
	private int speedAccelerating;

	// Rocket's stopping/falling speed. (Falling because of gravity)
	private int speedStopping;

	// Rocket's max landing speed without crashing
	public int topLandingSpeed;

	// Speed
	public int speedX, speedY;

	// Rocket images
	private BufferedImage rocketImg, rocketLandedImg, rocketCrashedImg,
			rocketFireImg;

	// Rocket dimenstions
	public int rocketImgWidth, rocketImgHeight;

	public PlayerRocket() {
		Initialize();
		LoadContent();

		// Set a random starting x-coord
		x = rand.nextInt(Framework.frameWidth - rocketImgWidth);
	}

	private void Initialize() {
		rand = new Random();

		ResetPlayer();

		speedAccelerating = 2;
		speedStopping = 1;

		topLandingSpeed = 5;
	}

	private void LoadContent() {
		try {
			URL rocketImgUrl = this.getClass().getResource("data/rocket.png");
			rocketImg = ImageIO.read(rocketImgUrl);
			rocketImgWidth = rocketImg.getWidth();
			rocketImgHeight = rocketImg.getHeight();

			URL rocketLandedImgUrl = this.getClass().getResource(
					"data/rocket_landed.png");
			rocketLandedImg = ImageIO.read(rocketLandedImgUrl);

			URL rocketCrashedImgUrl = this.getClass().getResource(
					"data/rocket_crashed.png");
			rocketCrashedImg = ImageIO.read(rocketCrashedImgUrl);

			URL rocketFireImgUrl = this.getClass().getResource(
					"data/rocket_fire.png");
			rocketFireImg = ImageIO.read(rocketFireImgUrl);
		} catch (IOException ex) {
			Logger.getLogger(PlayerRocket.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	/**
	 * Setup new rocket
	 */
	public void ResetPlayer() {
		landed = false;
		crashed = false;

		x = rand.nextInt(Framework.frameWidth - rocketImgWidth);
		y = 10;

		speedX = 0;
		speedY = 0;
	}

	/**
	 * Move the rocket
	 */
	public void Update() {
		// Calc vertical speed
		if (Canvas.keyboardKeyState(KeyEvent.VK_W)) {
			speedY -= speedAccelerating;
		} else {
			speedY += speedStopping;
		}

		// Calc left speed
		if (Canvas.keyboardKeyState(KeyEvent.VK_A)) {
			speedX -= speedAccelerating;
		} else if (speedX < 0) {
			speedX += speedStopping;
		}

		// Calc right speed
		if (Canvas.keyboardKeyState(KeyEvent.VK_D)) {
			speedX += speedAccelerating;
		} else if (speedX > 0) {
			speedX -= speedStopping;
		}

		// Move rocket
		x += speedX;
		y += speedY;
	}

	public void Draw(Graphics2D g2d) {
		g2d.setColor(Color.WHITE);
		g2d.drawString("Rocket coordinates: " + x + " : " + y, 5, 15);

		// If landed
		if (landed) {
			g2d.drawImage(rocketLandedImg, x, y, null);
			// crashed
		} else if (crashed) {
			g2d.drawImage(rocketCrashedImg, x, y + rocketImgHeight
					- rocketCrashedImg.getHeight(), null);
			// Still in space
		} else {
			// If w is pressed, draw rocket fire
			if (Canvas.keyboardKeyState(KeyEvent.VK_W)) {
				g2d.drawImage(rocketFireImg, x + 12, y + 66, null);
			}
			g2d.drawImage(rocketImg, x, y, null);
		}
	}
}
