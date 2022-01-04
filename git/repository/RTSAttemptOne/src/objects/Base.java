package objects;

import static helper.Constants.Units.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import scenes.Playing;

public class Base {

	private int x, y, id, unitProduced, owner, health;
	private boolean selected;
	private Playing playing;
	private Rectangle bounds;
	private int convertDistance;
	
	//For now, I'm using ponds as the "bases"
	public Base(int x, int y, int owner, Playing playing, int id) {
		this.x = x; 
		this.y = y; 
		this.owner = owner; 
		this.playing = playing;
		this.id = id;
		unitProduced = ARCHER;
		convertDistance = 14*32;
		
		health = 10 * 60; //takes 10 seconds to convert
		if(owner == 0)
			health /= 2; //takes 5 seconds to convert gaia base
		
		bounds = new Rectangle(x, y, 6 * 32, 6 * 32);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public int getOwner() {
		return owner;
	}
	
	public int getUnitProduced() {
		return unitProduced;
	}
	
	public void setUnitProduced(int u) {
		unitProduced = u;
	}

	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean b) {
		selected = b;
	}
	
	public int getHealth() {
		return health;
	}
	
	public void changeHealth(int amount) {
		health += amount;
		
		if(health <= 0)
			health = 0;
		else if(health >= 10*60)
			health = 10*60;
	}
	
	public void draw(Graphics g) {
		if(owner==1)
			g.setColor(PLAYERONECOLOR);
		else if(owner==2)
			g.setColor(PLAYERTWOCOLOR);
		else
			g.setColor(Color.gray);
		
		g.fillRect(x - playing.getCamera().getX(), y - playing.getCamera().getY(), 6*32, 6*32);
		
		if(selected) {
			//draw rectangle around it
			g.setColor(Color.white);
			g.drawRect(x - playing.getCamera().getX(), y - playing.getCamera().getY(), 6 * 32, 6 * 32);
			g.drawRect(x - playing.getCamera().getX() + 1, y - playing.getCamera().getY() + 1, 6 * 32 - 2, 6 * 32 - 2);
			g.drawRect(x - playing.getCamera().getX() + 2, y - playing.getCamera().getY() + 2, 6 * 32 - 4, 6 * 32 - 4);
		} else {
			if(owner == 1)
				g.setColor(PLAYERONECOLOR);
			else if(owner == 2)
				g.setColor(PLAYERTWOCOLOR);
			else
				g.setColor(Color.black);
		}
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawOval(x + 3*32 - convertDistance - playing.getCamera().getX(), y + 3*32 - convertDistance - playing.getCamera().getY(), 2*convertDistance, 2*convertDistance);
	}
	
	public void convert(int owner) {
		this.owner = owner;
		health = 10*60;
	}
	
	public void convert() {
		owner = 3 - owner; //1 --> 2, 2 --> 1
		health = 10*60;
	}
	
	public int getID() {
		return id;
	}
	
}
