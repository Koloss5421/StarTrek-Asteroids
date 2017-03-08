package com.jphardin;

import java.util.Random;

public class Laser {

	double x, y;
	double xSpeed;
	double ySpeed;
	double heading;
	
	public Laser(double currentHeading) {
		Ship ship = Main.ship;
		Random r = new Random();
		double spread = r.nextDouble();
		double shipCenterX = (int)ship.getX() + (ship.getShipHeight() / 2);
		double shipCenterY = (int)ship.getY() + (ship.getShipWidth() / 2);
		heading = currentHeading;
		x = shipCenterX - 5;
		y = shipCenterY - 5;
		
		xSpeed = 50 * Math.cos( Math.toRadians(currentHeading) ) + spread;
		ySpeed = 50 * Math.sin( Math.toRadians(currentHeading) ) + spread;

	}
	public void LaserUpdate() {
		
		x = x + xSpeed;
		y = y + ySpeed;

	
	}
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getHeading() {
		return heading;
	}
	public boolean isOOB() {
		
		if (x > 800 || x < 0 || y > 600 || y < 0) {
			return true;
		}
		else
		{
		return false;
		}
	}

}
