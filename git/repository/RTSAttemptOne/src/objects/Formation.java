package objects;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import helper.Constants;
import managers.FormationManager;
import units.Unit;

public class Formation {
	
	private ArrayList<Unit> units;
	private Unit leader;
	private FormationManager formationManager;
	private float speed;

	public Formation(ArrayList<Unit> units, int mapClickedX, int mapClickedY, FormationManager fm) {
		this.units = units;
		formationManager = fm;
		speed = (float) 0.8*Constants.Units.GetSpeed(Constants.Units.ARCHER);
		
		for(Unit u : units) {
			if(u.getUnitType()==Constants.Units.MAGE)
				speed = Constants.Units.GetSpeed(Constants.Units.MAGE);
			else if(u.getUnitType()==Constants.Units.SOLDIER) {
				speed = Constants.Units.GetSpeed(Constants.Units.SOLDIER);
				break;
			}
		}
		
		assignDestinations(mapClickedX, mapClickedY);
	}
	
	private void assignDestinations(int mapClickedX, int mapClickedY) {
		pickLeader();
		
		leader.setDestination(mapClickedX, mapClickedY);
		
		//ArrayList<Unit> soldiers = new ArrayList<Unit>();
		int numOfMelee = 0;
		for(; numOfMelee < units.size(); numOfMelee++) {
			if(units.get(numOfMelee).getUnitType() != Constants.Units.SOLDIER)
				break;
		}
		
		int numOfRanged = units.size() - numOfMelee;
		
		int numOfMeleeRows;
		if(numOfMelee == 0)
			numOfMeleeRows = 0;
		else {
			numOfMeleeRows = 1;
			for(; ; numOfMeleeRows++) {
				if((2*numOfMeleeRows + 1) * (numOfMeleeRows + 1) >= numOfMelee)
					break;
			}
		}
		
		int numOfRangedRows;
		if(numOfRanged == 0)
			numOfRangedRows = 0;
		else {
			numOfRangedRows = 1;
			for(; ; numOfRangedRows++) {
				if((2*numOfRangedRows + 1) * (numOfRangedRows + 1) >= numOfRanged)
					break;
			}
		}
		
		int numOfMeleeInFinalRow;
		int numOfMeleeInRemainingRows;
		
		if(numOfMeleeRows <= 1) {
			numOfMeleeInFinalRow = numOfMelee;
			numOfMeleeInRemainingRows = 0;
		} else {
			numOfMeleeInRemainingRows = (int) Math.ceil((double) numOfMelee/numOfMeleeRows);
			numOfMeleeInFinalRow = numOfMelee - (numOfMeleeInRemainingRows)*(numOfMeleeRows - 1);
		}
		
		int numOfRangedInFinalRow;
		int numOfRangedInRemainingRows;
		
		if(numOfRangedRows <= 1) {
			numOfRangedInFinalRow = numOfRanged;
			numOfRangedInRemainingRows = 0;
		} else {
			numOfRangedInRemainingRows = (int) Math.ceil((double) numOfRanged/numOfRangedRows);
			numOfRangedInFinalRow = numOfRanged - (numOfRangedInRemainingRows)*(numOfRangedRows - 1);
		}
		
		//go through the melee rows
		int index = 0;
		for(int i = 0; i < numOfMeleeRows; i++) {
			int rowAmount;
			if(i == numOfMeleeRows - 1)
				rowAmount = numOfMeleeInFinalRow;
			else
				rowAmount = numOfMeleeInRemainingRows;
			
			for(int j = 0; j < rowAmount; j++) {
				int deltaY = 32 * (j/2 - (j%2)*j);
				int deltaX = 32 * -i;
				
				if(i == numOfMeleeRows - 1 && numOfMeleeRows > 1) {
					if(numOfMeleeInRemainingRows % 2 == 1 && numOfMeleeInFinalRow % 2 == 0)
						deltaY += 16;
					else if(numOfMeleeInRemainingRows % 2 == 0 && numOfMeleeInFinalRow % 2 == 1)
						deltaY -=16;
				}
				
				double[] pt = {deltaX, deltaY};
				double angle = Math.atan((leader.getY()-leader.getDestY())/(leader.getX()-leader.getDestX()));
				//if moving left
				if(leader.getX() > leader.getDestX())
					angle += Math.PI;
				
				
				AffineTransform.getRotateInstance(angle, 0, 0).transform(pt, 0, pt, 0, 1);
				
				units.get(index++).enterFormation(this, leader, (int) pt[0], (int) pt[1]);
			}
		}
		//go through the ranged rows
		for(int i = numOfMeleeRows; i < numOfMeleeRows + numOfRangedRows; i++) {
			int rowAmount;
			if(i == numOfMeleeRows + numOfRangedRows - 1)
				rowAmount = numOfRangedInFinalRow;
			else
				rowAmount = numOfRangedInRemainingRows;
			
			for(int j = 0; j < rowAmount; j++) {
				int deltaY = 32 * (j/2 - (j%2)*j);
				int deltaX = 32 * -i;
				
				//remaining rows
				//if only one row, there are no "remaining" rows
				int n;
				if(numOfMeleeRows <= 1)
					n = numOfMeleeInFinalRow;
				else
					n = numOfMeleeInRemainingRows;
				

				if(n!=0) {
					if(i < numOfMeleeRows + numOfRangedRows - 1) { //not in final archer row
						if(n % 2 == 1 && numOfRangedInRemainingRows % 2 == 0)
							deltaY += 16;
						else if(n % 2 == 0 && numOfRangedInRemainingRows % 2 == 1)
							deltaY -=16;
					} else { //in final archer row
						if(n % 2 == 1 && numOfRangedInFinalRow % 2 == 0)
							deltaY += 16;
						else if(n % 2 == 0 && numOfRangedInFinalRow % 2 == 1)
							deltaY -=16;
					}
				} else {
					if(i == numOfRangedRows - 1 && numOfRangedRows > 1) {
						if(numOfRangedInRemainingRows % 2 == 1 && numOfRangedInFinalRow % 2 == 0)
							deltaY += 16;
						else if(numOfRangedInRemainingRows % 2 == 0 && numOfRangedInFinalRow % 2 == 1)
							deltaY -=16;
					}
				}
				
				double[] pt = {deltaX, deltaY};
				double angle = Math.atan((leader.getY()-leader.getDestY())/(leader.getX()-leader.getDestX()));
				//if moving left
				if(leader.getX() > leader.getDestX())
					angle += Math.PI;
				
				
				AffineTransform.getRotateInstance(angle, 0, 0).transform(pt, 0, pt, 0, 1);
				
				units.get(index++).enterFormation(this, leader, (int) pt[0], (int) pt[1]);
			}
		}
	}
	
	//public void adjustFormation() {
	//	if(System.currentTimeMillis() % 3000 < 60 && oldSize != units.size())
	//	//assignDestinations();
	//	//this is for adjusting formation when units die; don't do it constantly
	//}

	private void pickLeader() {
		leader = units.get(0); ////this should be a soldier!
	}
	
	public int size() {
		return units.size();
	}
	
	public ArrayList<Unit> getUnits(){
		return units;
	}

	public void removeAll(ArrayList<Unit> removable) {
		int mouseClickedX = (int) leader.getDestX();
		int mouseClickedY = (int) leader.getDestY();
		int oldSize = units.size();
		
		units.removeAll(removable);
		
		if(oldSize != units.size() && units.size()>0)
			assignDestinations(mouseClickedX, mouseClickedY);
	}
	
	public void endFormation() {
		for(Unit u : units) {
			u.setFormation(null);
			u.setLeader(null);
		}
		formationManager.remove(this);
	}
	
	public float getSpeed() {
		return speed;
	}
}
