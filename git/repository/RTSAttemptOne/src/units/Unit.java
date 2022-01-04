package units;

import java.awt.Rectangle;
import java.util.ArrayList;

import helper.Constants;
import managers.ProjectileManager;
import managers.UnitManager;
import objects.Formation;

public class Unit {

	private float x, y;
	private float destX, destY;
	private UnitManager unitManager;
	private ProjectileManager projectileManager;
	private Rectangle bounds;
	private int health, id, unitType, owner, range;
	private long lastAttackTime, attackTimerMax;
	private boolean selected;
	private Unit target, leader;
	private int deltaX, deltaY;
	private Formation formation;
	//private int stance; //0 = Aggressive Stance, 1 = Defensive, 2 = ? (make something in Constants.Units)
	
	public Unit(float x, float y, int id, int unitType, int owner, UnitManager unitManager, ProjectileManager projectileManager) {
		this.x = x;
		this.y = y;
		this.id = id;
		this.unitType = unitType;
		this.owner = owner;
		this.unitManager = unitManager;
		this.projectileManager = projectileManager;
		health = 100;
		target = null;
		attackTimerMax = 1500; //millis
		//attackTimer = 0;
		lastAttackTime = 0;
		
		if(unitType == Constants.Units.ARCHER)
			range = 7;
		else if(unitType == Constants.Units.MAGE)
			range = 5;
		else if(unitType == Constants.Units.SOLDIER)
			range = 1;
		
		bounds = new Rectangle((int)x, (int)y, 32, 32);
		destX = x;
		destY = y;
	}
	
	private void move() {
		float speed = Constants.Units.GetSpeed(unitType);
		
		if(leader != null && formation.getUnits().size() > 1) {
			if(!leader.equals(this)) {
				destX = leader.getX() + deltaX;
				destY = leader.getY() + deltaY;
			} else {
				speed = formation.getSpeed();
			}
		}
		
		double dist = Math.sqrt(Math.pow(x - destX, 2) + Math.pow(y - destY, 2));
		if(dist <= speed) {
			x = destX;
			y = destY;
			if(leader!=null && leader.equals(this))
				formation.endFormation();
		} else {
			x += (destX - x) * speed/dist;
			y += (destY - y) * speed/dist;
		}
		
		bounds = new Rectangle((int)x, (int)y, 32, 32);
	}
	
	public void enterFormation(Formation f, Unit leader, int deltaX, int deltaY) {
		formation = f;
		this.leader = leader;
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}
	
	////////////this code should be run not every update but every second, say
	/////////if unit A has null target, set target to B if: 1) B is attacking, 2) B is closest (maybe????)
	public void targetUnit() {
		Unit targetCandidate = target;
		
		double dOne = 32*9;
		if(target == null && destX == x && destY == y) {
			for(Unit u : unitManager.getUnits()) {
				double dTwo = Constants.Units.distance(u.getX(), u.getY(), x, y);
				if(owner != u.getOwner() && dTwo <= dOne) {
					dTwo = dOne;
					targetCandidate = u;
				}
			}
		}
		
		target = targetCandidate;
	}
	
	//for when player selects particular unit
	public void targetUnit(Unit u) {
		target = u;
	}
	
	private void pursueTarget() {
		if(target!=null) {
			double d = Constants.Units.distance(x, y, target.getX(), target.getY());
			if(d <= 32*14) {
				if(d <= 32*range) { ////they should stop not once they're within range, but a bit deeper
					destX = x;
					destY = y;
				} else {
					destX = target.getX();
					destY = target.getY();
				}
			} else {
				target = null;
				destX = x;
				destY = y;
			}
		}
	}
	
	public void dealDamage(int type) {
		//0 --> 1 --> 2 --> 0
		//x good versus (x+1)%3
		if(unitType == (type + 1)%3)
			health -= 25;
		else if(unitType == (type - 1)%3)
			health -=10;
		else
			health -= 15;
		
		if(health <= 0) {
			unitManager.remove(this);
			
			//if(formation!=null)
			//	formation.remove(this);
		}
	}
	
	private void attack() {
		if(target!=null) {
			if(System.currentTimeMillis()-lastAttackTime>=attackTimerMax) {
				if(Constants.Units.distance(x, y, target.getX(), target.getY()) <= range * 32) {
					if(unitType == Constants.Units.ARCHER || unitType == Constants.Units.MAGE)
						projectileManager.addProjectile(this);
					else
						target.dealDamage(unitType);
				
					lastAttackTime = System.currentTimeMillis();
				}
			}
		}
	}
	
	public void update() {
		if(System.currentTimeMillis() % 500 < 60) //0, 1, 2, ..., 59 : only happens once in the given interval, given 60 UPS
			targetUnit();
		pursueTarget(); ///////////either in here or after, produce projectile if ARCHER or MAGE
		move();
		attack();
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public int getId() {
		return id;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public int getHealth() {
		return health;
	}

	public int getUnitType() {
		return unitType;
	}

	public void setUnitType(int unitType) {
		this.unitType = unitType;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean s) {
		selected = s;
	}

	public int getOwner() {
		return owner;
	}

	public void setDestination(float f, float g) {
		destX = f;
		destY = g;
	}

	public Unit getTarget() {
		return target;
	}

	public void setTarget(Unit object) {
		target = object;
	}

	public float getDestX() {
		return destX;
	}
	
	public float getDestY() {
		return destY;
	}

	public Formation getFormation() {
		return formation;
	}

	public Unit getLeader() {
		return leader;
	}

	public void setFormation(Formation f) {
		formation = f;
	}

	public void setLeader(Unit u) {
		leader = u;
	}
}
