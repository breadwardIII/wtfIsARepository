package managers;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import helper.LoadSave;
import objects.Projectile;
import scenes.Playing;
import units.Unit;

public class ProjectileManager {

	private Playing playing;
	private ArrayList<Projectile> projectiles;
	private BufferedImage img;
	
	public ProjectileManager(Playing playing) {
		this.playing = playing;
		projectiles = new ArrayList<Projectile>();
		img = LoadSave.getSpriteAtlas().getSubimage(2*32, 7*32, 32, 32);
	}
	
	public void addProjectile(Unit u) {
		projectiles.add(new Projectile((int) u.getX(), (int) u.getY(), u.getUnitType(), u.getTarget(), this, img));
	}
	
	public void update() {
		int size = projectiles.size();
		for(int i = 0; i < size; i++) {
			projectiles.get(i).update();
			if(size != projectiles.size()) {
				//i.e., if at least one was removed
				i -= size - projectiles.size();
				size = projectiles.size();
			}
		}
	}
	
	public void remove(Projectile p) {
		projectiles.remove(p);
	}
	
	public void draw(Graphics g) {
		//Graphics bg;
		//BufferedImage img =
		for(Projectile p : projectiles)
			p.draw(g, playing.getCamera().getX(), playing.getCamera().getY());
	}
	
}
