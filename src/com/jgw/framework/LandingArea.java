package com.jgw.framework;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class LandingArea {
	// Landing area coords and width
	public int x, y, landingAreaImgWidth;
	// Landing area pic
	private BufferedImage landingAreaImg;

	public LandingArea() {
		Initialize();
		LoadContent();
	}

	private void Initialize() {
		// x-coord is 46% of frame width
		x = (int) (Framework.frameWidth * 0.46);
		// y-coord is 86% of frame width
		y = (int) (Framework.frameHeight * 0.86);
	}

	private void LoadContent() {
		try {
			URL landingAreaImgUrl = this.getClass().getResource(
					"data/landing_area.png");
			landingAreaImg = ImageIO.read(landingAreaImgUrl);
			landingAreaImgWidth = landingAreaImg.getWidth();
		} catch (IOException ex) {
			Logger.getLogger(LandingArea.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	public void Draw(Graphics2D g2d) {
		g2d.drawImage(landingAreaImg, x, y, null);
	}
}
