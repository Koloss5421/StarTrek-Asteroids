package com.jphardin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;



@SuppressWarnings("serial")
public class Main extends JPanel{
	
	public Main() {
		KeyListener listener = new KeyListener();
		addKeyListener(listener);
		setFocusable(true);
	}
	
	static String myTitle = "StarTrek Asteroids (Koloss)";
	static int windowWidth = 800;
	static int windowHeight = 600;
	static boolean running;
	static boolean debugMode = false;
	public static Ship ship;
	public static ArrayList<Laser> lasers;
	public static ArrayList<Asteroid> asteroids;
	
	//LEVEL BASED SETTINGS
	static int score = 0;
	static int level = 1;
	static int displayTimer = 0;
	static int displayTimerMax = 50;
	static int asteroidsRemaining = (int) Math.floor( 20 * ((level / 10) + 1) );
	static boolean displayNewLevel = true;
	

	public static void main(String[] args) throws InterruptedException {
		
		ship = new Ship(windowWidth, windowHeight);
		lasers = new ArrayList<Laser>();
		asteroids = new ArrayList<Asteroid>();
		
		final int FRAMES_PER_SEC = 25;
		final int SKIP_TICKS = 1000 / FRAMES_PER_SEC;
		long next_game_tick = System.currentTimeMillis();
		int sleep_time = 0;
		
		running = true;
		
		JFrame frame = new JFrame(myTitle);
		Main main = new Main();
		
		frame.setSize(windowWidth, windowHeight);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(main);
		frame.setVisible(true);
		
		while ( running ) {
			frame.repaint();
			KeyListener.updateInput();
			ship.update();
			
			
			next_game_tick += SKIP_TICKS;
			sleep_time = (int) (next_game_tick - System.currentTimeMillis());
			
			if ( sleep_time >= 0) {
				Thread.sleep(sleep_time);
			}
		}
	
	}
	
	// I know I probably shouldn't do all of this in paintComponent() But I did.
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		setBackground(Color.BLACK);
		/**
		 * If the ship is still alive draw things
		 */
		if (ship.getHull() > 0) {
			int shipCenterX = (int)ship.getX() + (ship.getShipHeight() / 2);
			int shipCenterY = (int)ship.getY() + (ship.getShipWidth() / 2);
			// Check if new Level
			if (asteroidsRemaining == 0) {
				level++;
				asteroidsRemaining = (int) Math.floor( 20 * (level * 1.05) );
				displayTimer = 0;
				displayNewLevel = true;
			}
			double LevelMultiplier = level * 1.5;
			if (asteroids.size() < (Math.floor(LevelMultiplier) * 10)) {
				asteroids.add(new Asteroid());
			}
			
			if (asteroids.size() > 0) {
				for (int j = 0; j < asteroids.size(); j++) {
					Asteroid current = asteroids.get(j);
					
					int curX = current.getX();
					int curY = current.getY();
					
					current.update();
					// Check if 'current' is colliding with ship - if so remove
					if ( current.shipCollision(ship) ) {
						ship.setHull((int) (ship.getHull() - Math.sqrt( (current.getWidth() / 2))) );
						asteroids.remove(current);
						
					}
					// DEBUG MODE ASTEROID BOX
					if (debugMode) {
						g2.setColor(Color.YELLOW);
						g2.draw(current.getRect());
					}
					if (lasers.size() > 0) {
						for (int k = 0; k < lasers.size(); k++) {
							Laser curLaser = lasers.get(k);
							if (debugMode) {
								int curLaserX = (int) curLaser.getX() + 5;
								int curLaserY = (int) curLaser.getY() + 5;
								int curAstX = (int) current.getX() + (current.getWidth() / 2);
								int curAstY = (int) current.getY() + (current.getWidth() / 2);
								
								g2.setColor(Color.RED);
								g2.drawLine(curLaserX, curLaserY, curAstX, curAstY);
							}
							if(current.laserCollision(curLaser)) {
								
								asteroids.remove(current);
								lasers.remove(curLaser);
								score = score + current.width;
								asteroidsRemaining--;
								
								
							}
						}
					}
					g2.setColor(Color.WHITE);
					g2.drawOval(curX, curY, current.getWidth(), current.getWidth());
					
				}
			}
			// FOR EVERY LASER - Check if Out of bounds/Update
			if (lasers.size() > 0) {
				for (int i = 0; i < lasers.size(); i++) {
					
						lasers.get(i).LaserUpdate();
						if (lasers.get(i).isOOB()) {
							lasers.remove(lasers.get(i));
						}
						else {
							g2.setColor(Color.BLUE);
							g2.fillOval((int) lasers.get(i).getX(), (int) lasers.get(i).getY(), 10, 10);
						}
						
				}
			}
			// DRAW SHIP
			
			AffineTransform old = g2.getTransform();//Get Current Transform Settings
			//Rotate ship based on Heading
			g2.rotate( Math.toRadians( ship.getHeading() ), shipCenterX, shipCenterY );
			g2.drawImage(ship.getShipImg(), (int) ship.getX(), (int) ship.getY(), ship.getShipHeight(), ship.getShipWidth(), this);
			if (debugMode) {
				g2.setColor(Color.YELLOW);
				g2.drawRect((int) ship.getX(), (int) ship.getY(), ship.getShipHeight(), ship.getShipWidth());
			}
			// Return to saved Transform so other things can be Drawn Properly
			// Otherwise everything will rotate with ship
			g2.setTransform(old);
			
			g2.setColor(Color.WHITE);
			g2.drawString("Info: Destroy the asteroids for points.", 3, 540);
			g2.drawString("Controls: W/Up, A/Left, S/Down, D/Right | Fire: Space or Right-CTRL", 3, 555);
			
			// Check if Debug Mode enable - IF so - Show Debug Info
			if (debugMode) {
				g2.setColor(Color.YELLOW);
				g2.setFont(new Font("Consolas", Font.BOLD, 16));
				g2.drawString("<DEBUG MODE>", 660, 20);
				g2.drawString("Asteroid #: " + asteroids.size(), 650, 40);
				
			}
			
			g2.setColor(Color.GREEN);
			g2.setFont(new Font("Arial", Font.BOLD, 16));
			g2.drawString("LEVEL " + level, 3, 20);
			g2.drawString("Score: " + score, 3, 40);
			g2.drawString("Remaining: " + asteroidsRemaining, 3, 60);
			g2.setColor(Color.RED);
			g2.drawString("Hull: " + ship.getHull(), 3, 80);
			
			
			if (displayNewLevel && displayTimer < displayTimerMax) {
				int fontSize = 20;
				String newLevel = "LEVEL: " + level + "!";
				int width = g2.getFontMetrics(new Font("Arial", Font.BOLD, fontSize)).stringWidth(newLevel);
				g2.setFont(new Font("Arial", Font.BOLD, fontSize));
				g2.setColor(Color.BLACK);
				g2.drawString("NEW LEVEL: " + level, windowWidth/2 - (width / 2) +1, windowHeight/2 - (fontSize / 2) +1);
				g2.setFont(new Font("Arial", Font.BOLD, fontSize));
				g2.setColor(Color.GREEN);
				g2.drawString("LEVEL: " + level, windowWidth/2 - (width / 2), windowHeight/2 - (fontSize / 2));
				displayTimer++;
			}
			else {
				displayNewLevel = false;
			}

			 
		}
		if (ship.getHull() <= 0) {
			int fontSize = 20;
			String gameLevel = "Level " + level;
			String gameOver = "GAME OVER";
			String gameScore = "Score: " + score;
			String gameRestart = "< Press 'R' to Restart> ";
			int gameLevelWidth = g2.getFontMetrics(new Font("Arial", Font.BOLD, fontSize)).stringWidth(gameLevel);
			int gameOverWidth = g2.getFontMetrics(new Font("Arial", Font.BOLD, fontSize)).stringWidth(gameOver);
			int scoreWidth = g2.getFontMetrics(new Font("Arial", Font.BOLD, fontSize)).stringWidth(gameScore);
			int restartWidth = g2.getFontMetrics(new Font("Consolas", Font.PLAIN, fontSize)).stringWidth(gameRestart);
			g2.setFont(new Font("Arial", Font.BOLD, fontSize));
			g2.setColor(Color.GREEN);
			g2.drawString(gameLevel, windowWidth/2 - (gameLevelWidth / 2) +1, windowHeight/2 - (fontSize / 2) - 30);
			g2.setColor(Color.RED);
			g2.drawString(gameOver, windowWidth/2 - (gameOverWidth / 2) +1, windowHeight/2 - (fontSize / 2));
			g2.setColor(Color.GREEN);
			g2.drawString(gameScore, windowWidth/2 - (scoreWidth / 2) +1, windowHeight/2 - (fontSize / 2) + 30);
			g2.setColor(Color.WHITE);
			g2.setFont(new Font("Consolas", Font.BOLD, fontSize));
			g2.drawString(gameRestart, windowWidth/2 - (restartWidth / 2) +1, windowHeight/2 - (fontSize / 2) + 75);
		}
	}
	
	public static void restartGame() {
		level = 1;
		score = 0;
		displayTimer = 0;
		displayTimerMax = 50;
		asteroidsRemaining = (int) Math.floor( 20 * ((level / 10) + 1) );
		displayNewLevel = true;
		ship = new Ship(windowWidth, windowHeight);
	}

}

class KeyListener extends KeyAdapter {
	
	public static int debugKeyTimer = 0;
	public static int fireTimer = 0;
	public static int fireRate = 5;
	
	static boolean[] keys = new boolean[256]; 
	
	public static void updateInput() {
		Ship ship = Main.ship;

		if (keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP]) {
			ship.setThrustHeading( ship.getHeading() );
			
			ship.setXVel(ship.getXVel() + (1 * calcXChange( ship.getThrustHeading() ) ) );
			ship.setYVel(ship.getYVel() + (1 * calcYChange( ship.getThrustHeading() ) ) );
			
		}

		if (keys[KeyEvent.VK_D] || keys[KeyEvent.VK_RIGHT]) {
			if (ship.getHeading() > 360 || ship.getHeading() + 5 > 360) {
				ship.setHeading(5);
			}
			ship.setHeading(ship.getHeading() + 5);
		}
		if (keys[KeyEvent.VK_A] || keys[KeyEvent.VK_LEFT]) {
			if (ship.getHeading() < 0 || ship.getHeading() - 5 < 0) {
				ship.setHeading(355);
			}
			ship.setHeading(ship.getHeading() - 5);
		}
		if (keys[KeyEvent.VK_SPACE]|| keys[KeyEvent.VK_CONTROL]) {
			ArrayList<Laser> lasers = Main.lasers;
			
			if (fireTimer >= fireRate) {
				lasers.add(new Laser(ship.getHeading()));
				fireTimer = 0;
			}
			else
			{
				fireTimer++;
			}
			
			

		}
		if(keys[KeyEvent.VK_R]) {
			if (ship.getHull() <= 0) {
				Main.restartGame();
			}	
		}
		if (keys[KeyEvent.VK_P] && debugKeyTimer >= 10) {
			Main.debugMode = !Main.debugMode;
			 System.out.println("P Key Pressed!");
			 debugKeyTimer = 0;
		}
		else
		{
			debugKeyTimer++;
		}

			
		
	}
	public static double calcXChange(double heading) {
		return Math.cos( Math.toRadians( heading ) );
	}
	public static double calcYChange(double heading) {
		return Math.sin( Math.toRadians( heading ) );
	}
	
	public void keyPressed(KeyEvent event) {
		keys[event.getKeyCode()] = true;
	}
	public void keyReleased(KeyEvent event) {
		keys[event.getKeyCode()] = false;
	}
}
