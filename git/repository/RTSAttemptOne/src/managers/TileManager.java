package managers;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import helper.LoadSave;
import objects.Base;
import objects.Tile;

public class TileManager {

	public Tile GRASS, WATER, ROAD;
	public BufferedImage atlas;
	public ArrayList<Tile> tiles = new ArrayList<>();
	
	public TileManager() {
		
		loadAtlas();
		createTiles();
		
	}

	private void createTiles() {
		
		int id = 0;
		tiles.add(GRASS = new Tile(getSprite(8, 1), id++, "Grass")); //id = 0, 1, 2
		tiles.add(WATER = new Tile(getSprite(0, 6), id++, "Water"));
		tiles.add(ROAD  = new Tile(getSprite(9, 0), id++, "Road"));
	}

	private void loadAtlas() {
		
		atlas = LoadSave.getSpriteAtlas();
	}
	
	public Tile getTile(int id) {
		return tiles.get(id);
	}
	
	public BufferedImage getSprite(int id) {
		return tiles.get(id).getSprite();
	}
	
	private BufferedImage getSprite(int xcord, int ycord) {
		return atlas.getSubimage(xcord*32, ycord*32, 32, 32);
	}
	
}
