package scenes;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import testPackage.Game;
import ui.MyButton;
import static testPackage.GameStates.*;

public class Menu extends GameScene implements SceneMethods{

	private BufferedImage img;
	private ArrayList<BufferedImage> sprites = new ArrayList<>();
	//private Game game;
	
	private MyButton bPlaying, bSettings, bQuit;
	
	public Menu(Game game) {
		super(game);
		importImg();
		loadSprites();
		initButtons();
	}

	private void initButtons() {
		
		int w = 150;
		int h = w/3;
		int x = (int) game.getScreenSize().getWidth()/2 - w/2;
		int y = 150;
		int yOffset = 100;
		
		bPlaying = new MyButton("Play", x, y, w, h);
		bSettings = new MyButton("Settings", x, y + yOffset, w, h);
		bQuit = new MyButton("Quit", x, y + 2*yOffset, w, h);
	}

	@Override
	public void render(Graphics g) {
		drawButtons(g);
	}

	private void drawButtons(Graphics g) {
		// TODO Auto-generated method stub
		bPlaying.draw(g);
		bSettings.draw(g);
		bQuit.draw(g);
	}

	private void importImg() {

		InputStream is = getClass().getResourceAsStream("/spriteatlas.png");
		
		try {
			img = ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadSprites() {
		
		for(int y = 0; y < 10; y++) {
			for(int x = 0; x < 10; x++) {
				sprites.add(img.getSubimage(x*32, y*32, 32, 32));
			}
		}	
	}

	@Override
	public void mouseClicked(int x, int y, int button) {
		
		if(bPlaying.getBounds().contains(x, y)) {
			SetGameState(PLAYING);
		}
		if(bSettings.getBounds().contains(x, y)) {
			SetGameState(SETTINGS);
		}
		if(bQuit.getBounds().contains(x, y)) {
			System.exit(0);
		}
		
	}

	@Override
	public void mouseMoved(int x, int y) {
		bPlaying.setMouseOver(false);
		if(bPlaying.getBounds().contains(x, y)) {
			bPlaying.setMouseOver(true);
		}
		bSettings.setMouseOver(false);
		if(bSettings.getBounds().contains(x, y)) {
			bSettings.setMouseOver(true);
		}
		bQuit.setMouseOver(false);
		if(bQuit.getBounds().contains(x, y)) {
			bQuit.setMouseOver(true);
		}
	}

	@Override
	public void mousePressed(int x, int y, int button) {
		
		if(bPlaying.getBounds().contains(x, y)) {
			bPlaying.setMousePressed(true);
		}
		else if(bSettings.getBounds().contains(x, y)) {
			bSettings.setMousePressed(true);
		}
		else if(bQuit.getBounds().contains(x, y)) {
			bQuit.setMousePressed(true);
		}
		
	}

	@Override
	public void mouseReleased(int x, int y, int button) {
		resetButtons();
		
	}

	private void resetButtons() {
		bPlaying.resetBooleans();
		bSettings.resetBooleans();
		bQuit.resetBooleans();
	}
}
