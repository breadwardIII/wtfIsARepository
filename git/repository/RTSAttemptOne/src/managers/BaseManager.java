package managers;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import objects.Base;
import scenes.Playing;
import units.Unit;

import static helper.Constants.Units.*;

public class BaseManager {

	private ArrayList<Base> bases;
	private Playing playing;
	private long unitTimer;
	private int convertDistance;
	
	public BaseManager(Playing playing) {
		this.playing = playing;
		bases = new ArrayList<Base>();
		convertDistance = 14*32;
		
		initBases();
	}
	
	private void initBases() {
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				//(0, 0)-->0 (0, 2) (2, 0) (2, 2)-->4 (1, 1)-->2
				if((i+j) % 2 == 0) {
					bases.add(new Base(224 + 960*i, 224 + 960*j, (int) ((double)3/8*Math.pow(i+j, 2) - (double)5/4*(i+j) + 1), playing, 3*i/2+j/2)); //adjust these
					//(0, 0)-->1 (2,2) -->2        (0,2) (1,1) (2, 0) <-- i+j = 2
					//i+j: 0-->1, 4-->2, 2-->0
					//16A + 4B + 1 = 2 ,  4A + 2B + 1 = 0
					//16A + 4B = 1, 4A + 2B = -1 --> 8A = 3 --> A=3/8, 3/2 + 2B = -1 --> -5/2 = 2B --> B = -5/4
				}
			}
		}
	}
	
	public void update() {
		unitTimer++;
		produceUnit();
		performConversion();
	}
	
	private void produceUnit() {
		for(Base b : bases) {
			if(unitTimer % (60 * 4) == 0 && b.getOwner() != 0) {
				//find unoccupied spot (x,y)
				int x = 0;
				int y = 0;
				
				for(int d = 0; d >= 0; d++) {
					for(int pos = 0; pos < 4*d || pos==0 ; pos++) {
						boolean failed = false;
						
						//deltaX + deltaY = d
						//deltaX: d --> -d --> d-1
						//deltaY: 0 --> -d --> d --> 0
						x = b.getX() + 3*32 + (Math.abs(2*d-pos) - d) * 32 - 16;
						y = b.getY() + 3*32 + (Math.abs(Math.abs(pos - 3*d) - 2*d) - d) * 32 - 16;
						
						//find if units are too close to this point
						for(Unit u : playing.getUnitManager().getUnits()) {
							if(u.getOwner() == b.getOwner() && u.getBounds().contains(x + 16, y + 16)) {
								failed = true;
								break;
							}
						}
						
						if(!failed) {
							//we have a solution. Escape loops
							pos = 5*d;
							d = -5;
						}
					}
				}
				
				//produce unit
				playing.getUnitManager().addUnit(x, y, b.getUnitProduced(), b.getOwner());
			}
		}
	}

	private void performConversion() {
		for(Base b : bases) {
			int pOne = 0;
			int pTwo = 0;
			
			for(Unit u : playing.getUnitManager().getUnits()) {
				if(distance(b.getX() + 3*32, b.getY() + 3*32, u.getX() + 16, u.getY() + 16) < convertDistance) {
					if(u.getOwner() == 1)
						pOne++;
					else if(u.getOwner() == 2)
						pTwo++;
				}
			}
			
			if(b.getOwner() == 1) {
				if(pOne >= pTwo) //consider multiplying defense by, like, 1.5
					b.changeHealth(1);
				else
					b.changeHealth(-1);
			} else if(b.getOwner() == 2) {
				if(pOne >= pTwo)
					b.changeHealth(-1);
				else
					b.changeHealth(1);
			} else {
				if(pOne - pTwo > 0) //give gaia half health from beginning!!!!!!
					b.changeHealth(-1);
				else if(pOne - pTwo < 0)
					b.changeHealth(1);
			}
			
			//center base could have gaia units!!!!!!
			
			if(b.getOwner() == 0) {
				if(b.getHealth() <= 0)
					b.convert(1);
				else if(b.getHealth() >= 10*60) {
					b.convert(2);
				}
			} else {
				if(b.getHealth() <= 0)
					b.convert();
			}
		}
	}
	
	public void mousePressed(int x, int y, int key) {
		if(key == MouseEvent.BUTTON1)
			for(Base b : bases) {
				if(b.getOwner()==1 && b.getBounds().contains(x + playing.getCamera().getX(), y + playing.getCamera().getY()))
					b.setSelected(true);
				else
					b.setSelected(false);
			}
		//if BUTTON3, set way-point
	}
	
	public void deselectAll() {
		for(Base b : bases)
			b.setSelected(false);
	}
	
	public boolean isBaseSelected() {
		return (getSelectedBase()!=null);
	}
	
	public Base getSelectedBase() {
		for(Base b : bases) {
			if(b.isSelected())
				return b;
		}
		
		return null;
	}
	
	public void draw(Graphics g) {
		for(Base b : bases) {
			b.draw(g);
		}
	}

	public ArrayList<Base> getBases() {
		return bases;
	}
	
}
