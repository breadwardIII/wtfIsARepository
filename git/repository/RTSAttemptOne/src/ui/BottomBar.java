package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import objects.Base;
import objects.Tile;
import scenes.Playing;
import units.Unit;

import static testPackage.GameStates.*;

public class BottomBar {

	private int x, y, width, height;
	private Playing playing;
	private MyButton bMenu;
	private Rectangle minimap;
	
	///private Tile selectedTile;
	
	private ArrayList<MyButton> tileButtons = new ArrayList<>();
	
	public BottomBar(int x, int y, int width, int height, Playing playing) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.playing = playing;
		minimap = new Rectangle(1000 - 5 - 190, 540 + 5, 190, 190);
		
		initButtons();
	}
	
	private void initButtons() {
		bMenu = new MyButton("Menu", 2, 642, 100, 30);
		
		int w = 50;
		int h = 50;
		int xStart = 110;
		int yStart = 650;
		int xOffset = (int)(w*1.1f); //10% bigger than width
		
		int i = 0;
		for(Tile tile : playing.getTileManager().tiles) {
			tileButtons.add(new MyButton(tile.getName(), xStart + xOffset * i, yStart, w, h, i));
			System.out.println(i);
			i++;
		}
	}
	
	private void drawButtons(Graphics g, boolean baseSelected) {
		bMenu.draw(g);
		
		if(baseSelected)
			drawTileButtons(g);
		///drawSelectedTile(g);
	}
	
	///private void drawSelectedTile(Graphics g) {
		
	///	if(selectedTile != null) {
	///		g.drawImage(selectedTile.getSprite(), 550, 650, 50, 50, null);
	///		g.setColor(Color.black);
	///		g.drawRect(550, 650, 50, 50);
	///	}
		
	///}
	private void drawTileButtons(Graphics g) {

		for(MyButton b : tileButtons) {
			
			//Sprite
			g.drawImage(getButtonImg(b.getID()),b.x,b.y,b.width,b.height,null);
	
			//MouseOver
			if(b.isMouseOver())
				g.setColor(Color.white);
			else
				g.setColor(Color.black);
			
			//Border
			g.drawRect(b.x, b.y, b.width, b.height);
			
			//MousePressed
			if(b.isMousePressed()) {
				g.drawRect(b.x + 1, b.y + 1, b.width - 2, b.height - 2);
				g.drawRect(b.x + 2, b.y + 2, b.width - 4, b.height - 4);
			}
		}
		
	}

	private BufferedImage getButtonImg(int id) {
		return playing.getTileManager().getSprite(id);
	}

	public void draw(Graphics g, boolean baseSelected) {
		//background
		g.setColor(new Color(220, 123, 15));
		g.fillRect(x, y, width, height);
		
		//buttons
		drawButtons(g, baseSelected);
		drawMinimap(g);
	}
	
	private void drawMinimap(Graphics g) {
		int size = 190;
		g.setColor(Color.green.darker());
		g.fillRect(1000 - 5 - 190, 545, size, size);
		
		//bases
		for(Base b : playing.getBaseManager().getBases()) {
			
			if(b.isSelected())
				g.setColor(Color.white);
			else {
				if(b.getOwner() == 1)
					g.setColor(Color.blue);
				else if(b.getOwner() == 2)
					g.setColor(Color.red);
				else
					g.setColor(Color.gray);
			}

			g.fillRect(1000 - 5 - 190 + b.getX() * 190 / playing.getMapWidth(), 545 + b.getY() * 190 / playing.getMapHeight(), 12, 12);
		}
		
		//units
		for(Unit u : playing.getUnitManager().getUnits()) {
			
			if(u.isSelected())
				g.setColor(Color.white);
			else {
				if(u.getOwner() == 1)
					g.setColor(Color.blue);
				else if(u.getOwner() == 2)
					g.setColor(Color.red);
			}

			g.fillRect(1000 - 5 - 190 + (int) u.getX() * 190 / playing.getMapWidth(), 545 + (int) u.getY() * 190 / playing.getMapHeight(), 3, 3);
		}
		
		//screen
		g.setColor(Color.white);
		g.drawRect(1000 - 5 - 190 + playing.getCamera().getX() * 190 / playing.getMapWidth(),
				545 + playing.getCamera().getY() * 190 / playing.getMapHeight(),
				1000 *  190 / playing.getMapWidth(),
				540 * 190 / playing.getMapHeight());
				//size * playing.getCamera().getSize().getWidth() / playing.getMapWidth(),
				//size * playing.getCamera().getSize().getHeight() / playing.getMapHeight())
		
	}

	public void mouseClicked(int x, int y) {
		if(bMenu.getBounds().contains(x, y)) {
			SetGameState(MENU);
		}
		///else {
		///	for(MyButton b : tileButtons) {
		///		if(b.getBounds().contains(x, y)) {
		///			selectedTile = playing.getTileManager().getTile(b.getID());
		///			//selectedTile = b;
		///			playing.setSelectedTile(selectedTile);
		///			return;
		///		}
		///	}
		///}
	}

	public void mouseMoved(int x, int y) {
		bMenu.setMouseOver(false);
		for(MyButton b : tileButtons) {
			b.setMouseOver(false);
		}
		
		if(bMenu.getBounds().contains(x, y)) {
			bMenu.setMouseOver(true);
		}
		else {
			for(MyButton b : tileButtons) {
				if(b.getBounds().contains(x, y)) {
					b.setMouseOver(true);
					return;
				}
			}
		}
		
	}
	
	public void mousePressed(int x, int y, int button) {
		if(bMenu.getBounds().contains(x, y)) {
			bMenu.setMousePressed(true);
		}
		else if(minimap.getBounds().contains(x, y)) {
			if(button == MouseEvent.BUTTON1) {
				playing.getCamera().setX((x - (1000 - 5 - 190)) * playing.getMapWidth() / 190 - 1000 / 2);
				playing.getCamera().setY((y - (540 + 5)) * playing.getMapHeight() / 190 - 740 / 2);
			} else if(button == MouseEvent.BUTTON3) {
				playing.getUnitManager().mousePressed((x - (int) minimap.getBounds().getX()) * playing.getMapWidth() / 190, 
						(y - (int) minimap.getBounds().getY()) * playing.getMapHeight() / 190, button, true);
			}
		} else {
			if(button==MouseEvent.BUTTON1 && playing.getBaseManager().isBaseSelected())
				for(MyButton b : tileButtons) { //////////////tile buttons exist whether base selected or not
					if(b.getBounds().contains(x, y)) {
						b.setMousePressed(true);
						playing.getBaseManager().getSelectedBase().setUnitProduced(b.getID());
						return;
					}
				}
		}
	}

	public void mouseReleased(int x, int y) {
		resetButtons();
	}
	
	private void resetButtons() {
		for(MyButton b : tileButtons)
			b.resetBooleans();
	}
	
}
