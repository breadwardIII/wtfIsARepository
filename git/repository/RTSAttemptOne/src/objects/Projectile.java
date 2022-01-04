package objects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import helper.Constants;
import helper.LoadSave;
import managers.ProjectileManager;
import units.Unit;

public class Projectile {

	private int x, y, speed, destX, destY, type, range;
	private Unit target;
	private BufferedImage img;
	private ProjectileManager projectileManager;
	
	public Projectile(int x, int y, int type, Unit target, ProjectileManager projectileManager, BufferedImage img) {
		this.x = x;
		this.y = y;
		this.type = type; //ARCHER, MAGE
		this.target = target;
		this.projectileManager = projectileManager;
		this.img = img;
		
		if(type == Constants.Units.ARCHER)
			range = 9;
		else if(type == Constants.Units.MAGE)
			range = 7;
		
		double dist = Constants.Units.distance(x, y, target.getX(), target.getY());
		destX = (int) (x - (x - target.getX()) * 32 * range / dist);
		destY = (int) (y - (y - target.getY()) * 32 * range / dist);
		
		speed = 8;
	}
	
	public void update() {
		if(Constants.Units.distance(x, y, target.getX(), target.getY()) <= 22) {
			target.dealDamage(type);
			projectileManager.remove(this);
		} else if(destX == x && destY == y) {
			projectileManager.remove(this);
		} else
			move();
	}
	
	private void move() {
		double dist = Math.sqrt(Math.pow(x - destX, 2) + Math.pow(y - destY, 2));
		if(dist <= speed) {
			x = destX;
			y = destY;
		} else {
			x += (destX - x) * speed/dist;
			y += (destY - y) * speed/dist;
		}
	}
	
	public void draw(Graphics g, int camX, int camY) {
		g.drawImage(img, x - camX, y - camY, null);
	}
}
