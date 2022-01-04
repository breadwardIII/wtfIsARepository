package managers;

import java.util.ArrayList;

import objects.Formation;
import units.Unit;

public class FormationManager {
	
	private ArrayList<Formation> formations;

	public FormationManager() {
		formations = new ArrayList<Formation>();
	}

	public void add(ArrayList<Unit> selectedUnits, int mapClickedX, int mapClickedY) {
		//for(Unit u : selectedUnits) {
		//	if(u.getFormation()!=null)
		//		u.getFormation().remove(u);
		//}
		for(Formation f : formations) {
			f.removeAll(selectedUnits);
		}
		
		formations.add(new Formation(selectedUnits, mapClickedX, mapClickedY, this));
	}
	
	public void update() {
		
		garbageCollection();
	}
	
	private void garbageCollection() {
		for(int i = 0; i < formations.size(); i++) {
			Formation f = formations.get(i);
			boolean alive = false;
			
			for(Unit u : f.getUnits()) {
				if(u.getFormation().equals(f)) {
					alive = true;
					break;
				}
			}
			
			if(!alive) {
				formations.remove(i--);
			}
		}
	}

	public void remove(Formation formation) {
		formations.remove(formation);
	}
	
}
