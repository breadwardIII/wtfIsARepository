package managers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import helper.Constants;
import helper.LoadSave;
import units.Unit;
import scenes.Playing;

import static helper.Constants.Units.*;

public class UnitManager {
	
	private Playing playing;
	private BufferedImage[] unitImgs;
	private ArrayList<Unit> units = new ArrayList<>();
	private ProjectileManager projectileManager;
	private FormationManager formationManager;
	private EnemyManager enemyManager;
	
	public UnitManager(Playing playing, ProjectileManager projectileManager) {
		this.playing = playing;
		this.projectileManager = projectileManager;
		formationManager = new FormationManager();
	}
	
	public void init() {
		enemyManager = playing.getEnemyManager();
		
		loadUnitImgs();
		initUnits();
	}

	//this method is a test; will be removed
	private void initUnits() {
		units.add(new Unit(224 + 3*32 - 16, 224 + 3*32 - 16, 0, ARCHER, 1, this, projectileManager));
		units.add(new Unit(224 + 960*2 + 3*32 - 16, 224 + 960*2 + 3*32 - 16, 1, ARCHER, 2, this, projectileManager));
	}

	private void loadUnitImgs() {
		//update the atlas
		//top row is the standard stuff
		//next two rows is new
		BufferedImage atlas = LoadSave.getSpriteAtlas();
		
		unitImgs = new BufferedImage[3];
		for(int i = 0; i<3; i++) {
			unitImgs[i] = atlas.getSubimage(9*32, (3-i)*32, 32, 32);
		}
		
	}
	
	public void update() {
		//formationManager.update();
		
		int size = units.size();
		for(int i = 0; i<size; i++) { ///////consider reversing order
			units.get(i).update();
			if(size != units.size()) {
				i -= size - units.size();
				size = units.size();
			}
		}
		
		formationManager.update();
	}
	
	public void addUnit(int x, int y, int unitType, int owner) {
		//Soldiers are always first in list so that they'll be leaders in formations
		if(unitType == Constants.Units.SOLDIER) {
			units.add(0, new Unit(x, y, units.size(), unitType, owner, this, projectileManager));
			
			if(owner == 2)
				enemyManager.setRole(units.get(0));
		} else {
			units.add(new Unit(x, y, units.size(), unitType, owner, this, projectileManager));
			
			if(owner == 2)
				enemyManager.setRole(units.get(units.size() - 1));
		}
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.white);
		for(Unit u : units) {
			g.drawImage(unitImgs[u.getUnitType()], (int) (u.getX()) - playing.getCamera().getX(), (int) (u.getY()) - playing.getCamera().getY(), null);
			
			if(u.isSelected()) {
				g.drawRect((int) (u.getX()) - playing.getCamera().getX(), (int) (u.getY()) - playing.getCamera().getY(), 32, 32);
			}
		}
		
		//health bar
		//I'm separating these to avoid any unit's health bar being obstructed by an above unit
		for(Unit u : units) {
			g.setColor(Color.red);
			g.fillRect((int) u.getX() - playing.getCamera().getX(), (int) u.getY() - playing.getCamera().getY() - 6, 32, 5);
			g.setColor(Color.green);
			g.fillRect((int) u.getX() - playing.getCamera().getX(), (int) u.getY() - playing.getCamera().getY() - 6, 32*u.getHealth()/100, 5);
		}
	}
	
	public boolean mousePressed(int x, int y, int button, boolean minimap) {
		boolean unitsSelected = false;
		
		if(button == MouseEvent.BUTTON1) { //left press
			for(Unit u : units) {
				if(u.getOwner() == 1 && u.getBounds().contains(x + playing.getCamera().getX(), y + playing.getCamera().getY())) {
					u.setSelected(true);
					unitsSelected = true;
				}
				else
					u.setSelected(false);
			}
		}
		else if(button == MouseEvent.BUTTON3) { //right click
			ArrayList<Unit> selectedUnits = new ArrayList<Unit>();
			for(Unit u : units) {
				if(u.isSelected()) {
					selectedUnits.add(u);
					
					//UNLESS I right-clicked an enemy unit; then set target to them
					u.setTarget(null);
				}
			}
			
			if(selectedUnits.size()!=0) {
				if(minimap)
					formationManager.add(selectedUnits, x - 16, y - 16);
				else
					formationManager.add(selectedUnits, x - 16 + playing.getCamera().getX(), y - 16 + playing.getCamera().getY());
			}
		}
		
		return unitsSelected;
	}
	
	public ArrayList<Unit> getUnits(){
		return units;
	}
	
	public void dragSelect(int x, int y, int dragX, int dragY) {
		Rectangle r = new Rectangle(Math.min(x, dragX), Math.min(y, dragY), Math.abs(dragX - x), Math.abs(dragY - y));
		for(Unit u : units) {
			u.setSelected(u.getOwner() == 1 && r.getBounds().contains(u.getX() + 16 - playing.getCamera().getX(), u.getY() + 16 - playing.getCamera().getY()));
		}
	}
	
	public void remove(Unit u) {
		for(Unit u2 : units) {
			if(u2.getTarget()!=null && u2.getTarget().equals(u))
				u2.setTarget(null);
		}
		
		units.remove(u);
	}

	public FormationManager getFormationManager() {
		return formationManager;
	}
}
