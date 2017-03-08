package com.jphardin;


import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import java.io.IOException;
import javax.imageio.ImageIO;

public class Ship {
	double x, y;
	double maxSpeed = 10;
	double xVel, yVel = 0;
	double heading = 0;
	double thrustHeading = 0;

	int hull;
	int shipWidth = 40;
	int shipHeight = 50;
	BufferedImage img = null;
	
	
	public Ship(int width, int height) {
		x = (width / 2) - shipWidth / 2;
		y = (height / 2) - shipHeight / 2;
		hull = 100;

			try {
				img = ImageIO.read(getClass().getResource("resources/ship.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("COULD NOT LOAD IMAGE!" + img);
			}

			

	}
	public void update() {
			
		if (x > 800 + shipHeight) {
			x = 0 - shipHeight;
		}
		if (x < 0 - shipHeight) {
			x = 800 + shipHeight;
		}
		if (y > 600 + shipHeight) {
			y = 0 - shipHeight;
		}
		if (y < 0 - shipHeight) {
			y = 600 + shipHeight;
		}
		else
		{
			x = x + xVel;
			y = y + yVel;
		}
			
			
	}
	public double getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

	public double getYVel() {
		return yVel;
	}
	public void setXVel(double newVel) {
		
		if(newVel > maxSpeed) {
			xVel = maxSpeed;
		}
		else if (newVel < -maxSpeed) {
			xVel = -maxSpeed;
		}
		else
		{
			xVel = newVel;
		}
		
	}
	public void setYVel(double newVel) {
		if(newVel > maxSpeed) {
			yVel = maxSpeed;
		}
		else if (newVel < -maxSpeed) {
			yVel = -maxSpeed;
		}
		else
		{
			yVel = newVel;
		}
	}
	public double getXVel() {
		return xVel;
	}
	public double getHeading() {
		return heading;
	}
	public void setHeading(double heading) {
		this.heading = heading;
	}
	public void setThrustHeading(double newHeading) {
		thrustHeading = newHeading;
	}
	public double getThrustHeading() {
		return thrustHeading;
	}
	public int getHull() {
		return hull;
	}
	public void setHull(int hull) {
		this.hull = hull;
	}
	public int getShipWidth() {
		return shipWidth;
	}
	public int getShipHeight() {
		return shipHeight;
	}
	public BufferedImage getShipImg() {
		return img;
	}
	public Rectangle2D getRect() {
		Rectangle rect = new Rectangle((int) this.x, (int) this.y, this.getShipHeight(), this.getShipWidth());
		return rect;
	}
}
