package helper;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Constants {
	
	//arrange these to match the order in the sprite atlas
	public static class Units {
		public static final int ARCHER = 0;
		public static final int MAGE = 1;
		public static final int SOLDIER = 2;
		
		public static float distance(float x1, float y1, float x2, float y2) {
			return (float) Math.sqrt(Math.pow((double)(x1 - x2), 2) + Math.pow((double)(y1 - y2), 2));
		}
		
		public static float GetSpeed(int unitType) {
			switch(unitType) {
			case ARCHER:
				return 4.5f;
			case MAGE:
				return 4.0f;
			case SOLDIER:
				return 3.0f;
			}
			
			return 0;
		}
		
		public static final Color PLAYERONECOLOR = Color.blue;
		public static final Color PLAYERTWOCOLOR = Color.red;
		
		//public static final BufferedImage projectileImage = playing.atlas.getSubimage(7*32, 2*32, 32, 32);
	}

	public static class Direction {
		public static final int LEFT = 0;
		public static final int UP = 1;
		public static final int RIGHT = 2;
		public static final int DOWN = 3;
	}
	
	public static class Enemies {
		public static final int ORC = 0;
		public static final int BAT = 1;
		public static final int KNIGHT = 2;
		public static final int WOLF = 3;
	}
	
	public static class Tiles {
		public static final int WATER_TILE = 0;
		public static final int GRASS_TILE = 1;
		public static final int ROAD_TILE = 2;
	}
}
