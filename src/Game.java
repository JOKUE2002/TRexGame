import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JFrame implements MouseListener, KeyListener, ActionListener {

	/**
	 * This is my attempt at programming the google TRex Game. 
	 * Total Coding-Time is at around 1h 45m.
	 * Sprites are from https://www.spriters-resource.com/pc_computer/trexgame/
	 * You can download the full code as well as the Runnable JAR File from my GitHub account
	 * @JOKUE2002. You can also find me on:
	 *  twitter (@JOKUE2002) 
	 *  and on Instagram (@JOKUE2002)
	 * Have fun playing. 
	 * My highscore is 1027.
	 * Feel free to beat it. if you do so, 
	 * please share your score on Github or on Twitter with a screenshot. and reffer to my account.
	 * Thanks!
	 * More functions will follow (onscreen menu, etc)
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Doublebuffering Graphics
	 */
	Graphics dbg;

	/**
	 * Doublebuffering Image
	 */
	Image img;

	/**
	 * Container auf dem alles gemalt wird.
	 */
	JPanel platte;

	/**
	 * Breite des Fensters
	 */
	int width = 800;

	/**
	 * Hoehe des Fensters
	 */
	int height = 200;

	/**
	 * Gibt die aktuelle Position des Dinos an
	 */
	int x = 20;

	/**
	 * Gibt die aktuelle Position des Dinos an
	 */
	double y = 0;

	/**
	 * Movement fürs Springen
	 */
	double vy = 0;
	double ay = -1.75;

	/**
	 * true wenn der Linke Fuss unten ist
	 */
	boolean leftDown = true;

	/**
	 * alle 4 züge wird der fuss bewegt
	 */
	int zug = 0;

	/**
	 * gibt die geschwindigkeit der hindernisse in pixeln pro tick an
	 */
	int hindernisSpeed = 9;

	/**
	 * x Wert um den boden zu bewegen
	 */
	int groundX = 0;

	/**
	 * Zug Wert, um den Boden verlangsamt zu bewegen
	 */

	int groundZug = 0;

	/**
	 * ArrayList mit Hindenissen
	 */
	ArrayList<Integer> hindernisse = new ArrayList<Integer>();
	ArrayList<Integer> hindernisImage = new ArrayList<Integer>();

	/**
	 * GameTimer
	 */
	Timer timer = new Timer(15, this);
	// SPRITES
	// Ground from 16,274 to 561,282 (545x8)
	// Dino 1 from 6,123 to 46,166 (40x43)
	// Dino 2 from 55,123 to 95,166 (40x43)
	// Hidnernis1 from 157,69 (49,33)
	// Hidnernis2 from 227,62 (23,46)
	// Hidnernis3 from 161,116 (32,33)
	// Hidnernis4 from 227,117 (15,33)

	BufferedImage ground;
	BufferedImage dino1;
	BufferedImage dino2;
	BufferedImage hindernis1, hindernis2, hindernis3, hindernis4;

	/**
	 * speichert den erreichten score
	 */
	int score = 0;

	/**
	 * Constructor
	 */
	public Game() {
		platte = new JPanel();
		platte.setBackground(Color.WHITE);
		setSize(width, height);
		setDefaultCloseOperation(3);
		setFocusable(true);
		addKeyListener(this);
		requestFocus();
		// setVisible(true);
		try {
			BufferedImage spriteSheet = ImageIO.read(new File(".//TRexGame.png"));
			ground = spriteSheet.getSubimage(16, 274, 545, 8);
			dino1 = spriteSheet.getSubimage(6, 123, 40, 43);
			dino2 = spriteSheet.getSubimage(55, 123, 40, 43);
			hindernis1 = spriteSheet.getSubimage(157, 69, 49, 33);
			hindernis2 = spriteSheet.getSubimage(227, 62, 23, 46);
			hindernis3 = spriteSheet.getSubimage(161, 116, 32, 33);
			hindernis4 = spriteSheet.getSubimage(227, 117, 15, 33);
			System.out.println("IMAGES CREATED");
		} catch (Exception e) {
			e.printStackTrace();
		}

		setContentPane(platte);
		// repaint();
		setVisible(true);

		timer.setInitialDelay(500);
		timer.start();
	}

	/**
	 * Main function to start the game
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new Game();
	}

	/**
	 * Paint function to paint the game
	 */
	@Override
	public void paint(Graphics g) {
		img = createImage(width, height);
		dbg = img.getGraphics();

		// HINTERGRUND
		dbg.setColor(Color.WHITE);
		dbg.fillRect(0, 0, width, height);

		// DINO
		if (!leftDown) {
			dbg.drawImage(dino1, x, height - 20 - (int) y - dino1.getHeight() - ground.getHeight(), null);
		} else {
			dbg.drawImage(dino2, x, height - 20 - (int) y - dino1.getHeight() - ground.getHeight(), null);
		}

		// HINDERNISSE
		for (int i = 0; i < hindernisse.size(); i++) {
			// dbg.fillRect(hindernisse.get(i), height-ground.getHeight()-20-50,
			// 20, 50);
			BufferedImage hindernis = getHindernisByIndex(i);
			dbg.drawImage(hindernis, hindernisse.get(i), height - ground.getHeight() - 20 - hindernis.getHeight(),
					null);
		}

		// BODEN
		dbg.drawImage(ground, groundX, height - ground.getHeight() - 20, null);
		dbg.drawImage(ground, groundX + ground.getWidth(), height - ground.getHeight() - 20, null);
		dbg.drawImage(ground, groundX + ground.getWidth() * 2, height - ground.getHeight() - 20, null);

		// SCORE
		dbg.setColor(Color.red);
		dbg.setFont(new Font("Comic Sans MS",Font.PLAIN, 12));
		dbg.drawString(Integer.toString(score), width - 40, 20);
		
		g.drawImage(img, 0, 20, null);
	}

	private BufferedImage getHindernisByIndex(int i) {
		switch (hindernisImage.get(i)) {
		case 1:
			return hindernis1;
		case 2:
			return hindernis2;
		case 3:
			return hindernis3;
		case 4:
			return hindernis4;
		}
		return hindernis1;
	}

	/**
	 * MouseClicked to catch the Mouseaction in the menus
	 */
	@Override
	public void mouseClicked(MouseEvent e) {

	}

	/**
	 * ActionListener for the gameTimer
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		move();
		hindernissMovement();
		repaint();

		score++;

		zug++;
		groundZug++;
		if (zug == 4) {
			zug = 0;
			leftDown = !leftDown;
		}
		groundX -= hindernisSpeed / 2;
		groundX %= ground.getWidth();
	}

	/**
	 * bewegt die hindernisse und macht die Collisiondetection
	 */
	private void hindernissMovement() {
		for (int i = 0; i < hindernisse.size(); i++) {
			hindernisse.set(i, hindernisse.get(i) - hindernisSpeed);
			if (hindernisse.get(i) < -getHindernisByIndex(i).getWidth()) {
				hindernisse.remove(i);
				hindernisImage.remove(i);
			}
		}

		if (hindernisse.size() <= 5) {
			if (hindernisse.size() != 0) {
				hindernisse.add((int) (Math.random() * (width / 2) + width / 4)
						+ (int) (hindernisse.get(hindernisse.size() - 1)));
			} else {
				hindernisse.add((int) (Math.random() * width + width / 2));
			}
			hindernisImage.add((int) (Math.random() * 4) + 1);
		}

		for (int i = 0; i < hindernisse.size(); i++) {
			if (x >= hindernisse.get(i) && x <= hindernisse.get(i) + getHindernisByIndex(i).getWidth()) {
				if (y >= getHindernisByIndex(i).getHeight()) {

				} else {
					endGame();
				}
			}
		}
	}

	private void endGame() {
		timer.stop();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		timer.setInitialDelay(1000);
		hindernisse.clear();
		y = 0;
		vy = 0;
		timer.restart();
		score = 0;
	}

	/**
	 * bewegt den spielen
	 */
	private void move() {
		vy += ay;
		y += vy;
		if (y < 0) {
			y = 0;
		}
		if (y > 0) {
			zug = 0;
		}
	}

	// UNNUSED

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// System.err.println("KEYEVENT");
		if (y == 0) {
			// System.err.println("SPACE");
			vy = 20;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
