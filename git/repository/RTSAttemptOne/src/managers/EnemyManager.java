package managers;

import java.util.ArrayList;

import helper.Constants;
import objects.Base;
import scenes.Playing;
import units.Unit;

public class EnemyManager implements Runnable{

	private UnitManager unitManager;
	private FormationManager formationManager;
	private ProjectileManager projectileManager;
	private BaseManager baseManager;
	
	int[][] pNumOfUnits = {{1, 0, 0}, {1, 0, 0}};
	int[] unitDeficit = new int[3];
	
	int personality = 1; //0 = very aggressive, 1 = aggressive, 2 = defensive, 3 = very defensive (passive-aggressive, harass?)
	int basePriorityAggressive = 3;
	int basePriorityDefensive = 5;
	
	boolean oldMode;
	
	private ArrayList<Base> bases;
	private ArrayList<Base> myBases;
	private ArrayList<Unit> myUnits;
	
	private Thread thinkingThread;
	
	public EnemyManager(Playing playing, ProjectileManager projectileManager, BaseManager baseManager) {
		//this.playing = playing;
		unitManager = playing.getUnitManager();
		formationManager = unitManager.getFormationManager();
		this.projectileManager = projectileManager;
		this.baseManager = baseManager;
		
		bases = baseManager.getBases();
		
		oldMode = true;
		
		myBases = new ArrayList<Base>();
		myUnits = new ArrayList<Unit>();
		
		thinkingThread = new Thread(this) {};
		thinkingThread.start();
		
	}
	
	@Override
	public void run() {
		
		while(true) {
			//The AI will figure shit out here
			//System.out.println(System.currentTimeMillis());
		}
		
	}
	
	public void init() {
		myBases.add(baseManager.getBases().get(4));
		myUnits.add(unitManager.getUnits().get(1));
		
		initialMove();
	}
	
	private void initialMove() {
		Base b = baseManager.getBases().get((int) (Math.random()*3 + 1));
		
		commandMove(myUnits, b.getX()+ 3*32 - 16, b.getY() + 3*32 - 16);
	}
	
	public void update() {
		
		//if()
		
	}
	
	private int determineUnitDeficits() {
		int lowestIndex = -1;
		int lowestVal = 0;
		for(int i = 0; i < 3; i++) {
			unitDeficit[i] = pNumOfUnits[0][i] - pNumOfUnits[1][i];
			if(unitDeficit[i] < lowestVal || lowestIndex == -1) {
				lowestIndex = i;
				lowestVal = unitDeficit[i];
			}
		}
		
		return lowestIndex;
	}
	
	private int numOfPlayerDefenders(Base b) {
		//for(Base b : bases)
		
		return 0;
	}
	
	////MACRO
	/////Temporary AI Plan:
	/////Produce at all bases the single unit type which AI is most lacking
	//call this method only every once in a while
	public void decideUnitCreation() {
		int preferredUnitType = determineUnitDeficits();
		
		for(Base b : myBases) {
			b.setUnitProduced(preferredUnitType);
		}
	}
	
	private double[] averagePOneCoords() {
		int num = 0;
		double totalX = 0; double totalY = 0;
		for(Unit u : unitManager.getUnits()) {
			if(u.getOwner()==1) {
				totalX += u.getX();
				totalY += u.getY();
				num++;
			}
		}
		
		double[] ans = {totalX/num, totalY/num};
		
		return ans;
	}
	
	public void decideBasePriorities() {
		//add another condition; only call this when opponent has 3
		//Retreat all units and defend one base.
		//If enemy captures that base, flip to aggressive -- capture whichever adjacent base of theirs is weakest
		
		//find avg of p1 unit coordinates (weighted by health?)
		//pick base furthest from that avg
		if(myBases.size() == 2) {
			if(getAvailableBases().size()!=0) {
				personality = 1;
				double[] avg = averagePOneCoords();
				
				
				if(Constants.Units.distance((float) myBases.get(0).getX() + 3*32, (float) (myBases.get(0).getY() + 3*32), (float) avg[0], (float) avg[1]) <
						Constants.Units.distance((float) myBases.get(1).getX() + 3*32, (float) myBases.get(1).getY() + 3*32, (float) avg[0], (float) avg[1]))
					basePriorityDefensive = myBases.get(1).getID();
				else
					basePriorityDefensive = myBases.get(0).getID();
			} else {
				//early game; consider sending a unit to capture it
				personality = 0;
				
			}
		} else if(myBases.size() == 1) {
			if(getAvailableBases().size()!=0) {
				personality = 0; //maybe have another personality mode. Or a status var that becomes: "Hail Mary"
				
			} else {
				personality = 1;
				
			}
		}
		
		
	}
	
	//need to calculate how many units to send to base for an aggressive attack?
	
	//calculate how many defenders at each base
	
	
	private ArrayList<Base> getAvailableBases() {
		ArrayList<Base> available = new ArrayList<Base>();
		for(Base b : baseManager.getBases()) {
			if(b.getOwner()==0)
				available.add(b);
		}
		
		return available;
	}
	
	////MICRO
	private void dodgeProjectiles() {
		
	}
	
	private void commandMove(ArrayList<Unit> select, int destX, int destY) {
		formationManager.add(select, destX, destY);
	}
	
	public void addBase(Base b) {
		myBases.add(b);
	}
	
	public void removeBase(Base b) {
		myBases.remove(b);
	}

	public void setRole(Unit unit) {
		//
		//if(oldMode) {
		//	int n = (int) (Math.random()*5);
		//	unit.setDestination(bases.get(n).getX() + 3*32 - 16, bases.get(n).getY() + 3*32 - 16);
		//}
	}
}
