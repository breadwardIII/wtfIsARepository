package testPackage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Random;

import javax.swing.JPanel;

import inputs.KeyboardListener;
import inputs.MyMouseListener;
import objects.Camera;

public class GameScreen extends JPanel {

	private Game game;
	
	private MyMouseListener myMouseListener;
	private KeyboardListener keyboardListener;
	
	public GameScreen(Game game) {
		this.game = game;
		
		setPanelSize();
	}
	
	public void initInputs() {
		myMouseListener = new MyMouseListener(game);
		keyboardListener = new KeyboardListener();
		
		addMouseListener(myMouseListener);
		addMouseMotionListener(myMouseListener);
		addKeyListener(keyboardListener);
		
		requestFocus();
	}
	
	private void setPanelSize() {
		
		setMinimumSize(game.getScreenSize());
		setPreferredSize(game.getScreenSize());
		setMaximumSize(game.getScreenSize());
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		try {
			game.getRender().render(g);
		} catch(ConcurrentModificationException e) {
			System.out.println("drawing while updating.");
		}
	}
	
	public KeyboardListener getKeyboardListener() {
		return keyboardListener;
	}
	
}
