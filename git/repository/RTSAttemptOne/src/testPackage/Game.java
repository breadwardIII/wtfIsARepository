package testPackage;

import static testPackage.GameStates.PLAYING;

import java.awt.Dimension;

import javax.swing.JFrame;

import inputs.KeyboardListener;
import inputs.MyMouseListener;
import scenes.Menu;
import scenes.Playing;
import scenes.Settings;

public class Game extends JFrame implements Runnable{
	
	private GameScreen gameScreen;
	private Thread gameThread;
	
	private final double FPS_SET = 60.0;
	private final double UPS_SET = 60.0;
	
	private final Dimension screenSize = new Dimension(1000, 740);//(640, 740);
	
	
	//classes
	private Render render;
	private Menu menu;
	private Playing playing;
	private Settings settings;
	
	public Game() {
		initClasses();
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		
		add(gameScreen); //like a sticky note added to a board
		pack();
		
		setVisible(true); //this needs to be at end
		setLocationRelativeTo(null);
	}
	
	private void initClasses() {
		render = new Render(this);
		gameScreen = new GameScreen(this);
		menu = new Menu(this);
		playing = new Playing(this);
		settings = new Settings(this);
		
	}

	private void start() {
		gameThread = new Thread(this) {}; //"this" already implements Runnable
		
		gameThread.start();
	}
	
	private void updateGame() {
		switch(GameStates.gameState){
		case MENU:
			break;
		case PLAYING:
			playing.update();
			break;
		case SETTINGS:
			break;
			
		}
	}
	
	public static void main(String[] args) {
		
		Game game = new Game();
		game.gameScreen.initInputs();
		game.start(); //instead of loopGame()
		
	}

	@Override
	public void run() {
		
		double timePerFrame = 1000000000.0 / FPS_SET; //billion nanoseconds (1s)
		double timePerUpdate = 1000000000.0 / UPS_SET;
		
		long lastFrame = System.nanoTime();
		long lastUpdate = System.nanoTime();
		long lastTimeCheck = System.currentTimeMillis();
		
		int frames = 0;
		int updates = 0;
		
		long now;
		
		while(true) { /////////////////////is it normal to put both draw and update in one thread? should there separate threads (and another for the AI)?
			
			now = System.nanoTime();
			
			//render
			if(now - lastFrame >= timePerFrame) {
				repaint(); //repaint for a JFrame also repaints the added JPanel
				lastFrame = now;
				frames++;
			}
			
			//update
			if(now - lastUpdate >= timePerUpdate) {
				updateGame();
				lastUpdate = now;
				updates++;
			}
			
			//check FPS, UPS
			if(System.currentTimeMillis() - lastTimeCheck >= 1000) {
				//System.out.println("FPS: " + frames + "| UPS: " + updates);
				frames = 0;
				updates = 0;
				lastTimeCheck = System.currentTimeMillis();
			}
		}
		
	}
	
	//getters and setters
	public Render getRender() {
		return render;
	}

	public Menu getMenu() {
		return menu;
	}

	public Playing getPlaying() {
		return playing;
	}

	public Settings getSettings() {
		return settings;
	}
	
	public Dimension getScreenSize() {
		return screenSize;
	}
	
	public GameScreen getGameScreen() {
		return gameScreen;
	}

	
	
}
