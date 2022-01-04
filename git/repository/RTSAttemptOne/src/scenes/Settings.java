package scenes;

import java.awt.Color;
import java.awt.Graphics;

import testPackage.Game;

public class Settings extends GameScene implements SceneMethods{

	public Settings(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		g.setColor(Color.blue);
		g.fillRect(0, 0, 640, 640);
	}

	@Override
	public void mouseClicked(int x, int y, int button) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(int x, int y, int button) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(int x, int y, int button) {
		// TODO Auto-generated method stub
		
	}
}
