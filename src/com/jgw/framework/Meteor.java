package com.jgw.framework;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class Meteor {
	private int speedX, speedY;
	
	private Random rand;
	
	private int x, y;
	
	public Meteor() {
		Initialize();
		LoadContent();
		
		x = rand.nextInt(Framework.frameWidth);
		y = rand.nextInt(Framework.frameHeight);

		speedX = rand.nextInt(99) + 1;
		speedY = speedX;
	}
	
	private void Initialize() {
		rand = new Random();
	}
	
	private void LoadContent() {
		// TODO Auto-generated method stub

	}
	
	public void ResetMeteor() {
		x = rand.nextInt(Framework.frameWidth);
		y = rand.nextInt(Framework.frameHeight);
		speedX = rand.nextInt(9) + 1;
		speedY = speedX;
	}
	
	public void Update() {
		x += speedX;
		y += speedY;
	}
	
	public void Draw(Graphics2D g2d) {
		g2d.setColor(Color.WHITE);
		g2d.fillOval(x, y, 10, 10);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

}
