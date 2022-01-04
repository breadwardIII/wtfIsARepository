package helper;

public class LevelBuild {
	
	
	public static int[][] getLevelData(){
		//create a 2D int array
		//where each val = tile in the level
		
		//5 screens long/wide
		int[][] lvl = new int[80][80];
		
		for(int i = 0; i < 80; i++) {
			for(int j = 0; j < 80; j++) {
				lvl[i][j] = 0;
			}
		}
		
		//0 --> 19, 20 --> 39, 40 --> 59
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 6; j++) {
				lvl[i+7][j+7] = 1; //7*32 = 160 + 64 = 224 = x
				lvl[i+7+60][j+7] = 1; //30*32 = 960 = delta
				lvl[i+7+60][j+7+60] = 1;
				lvl[i+7][j+7+60] = 1;
				lvl[i+7+30][j+7+30] = 1;
			}
		}
		
		return lvl;
		
	}
}
