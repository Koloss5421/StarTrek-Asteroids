package com.jphardin;


import java.util.Random;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Asteroid {
	
	int x, y;
	double xSpeed, ySpeed, heading;
	int width;
	static int count = 0;
	int direction;
	boolean firstX = true; //debug
	boolean firstY = true; //debug
	
	public Asteroid() {
		generate();
	}
	public void generate() {
		Random r = new Random();
		double speed = r.nextInt((5 - 1) + 1) + 1;
		int randSide = r.nextInt((4 - 1) + 1) + 1;
		width = r.nextInt((150 - 20) + 1) + 20 + 2;
		switch(randSide) {
			case 1:
				x = r.nextInt((840 - 800) + 1) + 800 + width;
				y = r.nextInt((600 - 0) + 1) + 0;
				direction = r.nextInt((225 - 135) + 1) + 90;

				xSpeed = speed * Math.cos( Math.toRadians( direction ));
				ySpeed = speed * Math.sin( Math.toRadians( direction ));
			break;
			case 2:
				x = r.nextInt((800 - 0) + 1) + 0;
				y = r.nextInt((640 - 600) + 1) + 600 + width;
				direction = r.nextInt((315 - 225) + 1) + 225;

				xSpeed = speed * Math.cos( Math.toRadians( direction ));
				ySpeed = speed * Math.sin( Math.toRadians( direction ));
			break;
			case 3:
				x = r.nextInt((40 - 0) + 1) + 0 + width;
				y = r.nextInt((600 - 0) + 1) + 0;

				x = x * -1;
				
				direction = r.nextInt((405 - 315) + 1) + 315;
				if (direction > 360) {
					int temp = direction - 360;
					direction = temp;
				}
				xSpeed = speed * Math.cos( Math.toRadians( direction ));
				ySpeed = speed * Math.sin( Math.toRadians( direction ));

			break;
			case 4:
				x = r.nextInt((800 - 0) + 1) + 0;
				y = r.nextInt((40 - 0) + 1) + 0 + width;

				y = y * -1;
				direction = r.nextInt((135 - 45) + 1) + 45;
				xSpeed = speed * Math.cos( Math.toRadians( direction ));
				ySpeed = speed * Math.sin( Math.toRadians( direction ));
		
			break;
				
		}
	}
	public void update() {
		
		if (x < -400 || y < -400 || x > 1200 || y > 1200) {
			this.generate();
		}
		if (xSpeed == 0.0 || ySpeed == 0.0) {
			this.generate();
		}
		x = (int) (x + Math.floor(xSpeed));
		y = (int) (y + Math.floor(ySpeed));
		
	}
	public static int getCount() {
		return count;
	}
	public int getWidth() {
		return width;
	}
	public int getX() {
		if (firstX) { // debug

			Double d = new Double(x);
			int i = d.intValue();
			
		
			
			return i;
		}
		Double d = new Double(x);
		int i = d.intValue();
		return i;
	}
	public int getY() {
		if (firstY) { //debug
	
			Double d = new Double(y);
			int i = d.intValue();
			
	
			
			return i;
		}
		Double d = new Double(y);
		int i = d.intValue();
		return i;
	}
	
	public boolean laserCollision(Laser curLaser) {
	
		if ( Math.pow(width, 2) > Math.pow( curLaser.getX() - x, 2 ) + Math.pow( curLaser.getY() - y, 2 ) ) {
			return true;
		}
		return false;
	}
	public boolean shipCollision(Ship ship){
		Rectangle ast = new Rectangle((int) x, (int) y, width, width);
		

		if (ast.intersects(ship.getRect())) {
			return true;
		}
		else {
			return false;
		}
	}

	public Rectangle2D getRect() {
		Rectangle rect = new Rectangle((int) this.x, (int) this.y, this.width, this.width);
		return rect;
	}
	

}
