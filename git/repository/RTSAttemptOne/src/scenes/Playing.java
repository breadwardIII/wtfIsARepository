package scenes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import helper.LevelBuild;
import helper.LoadSave;
import managers.BaseManager;
import managers.EnemyManager;
import managers.ProjectileManager;
import managers.TileManager;
import managers.UnitManager;
import objects.Camera;
import objects.Tile;
import testPackage.Game;
import testPackage.GameScreen;
import ui.BottomBar;
import ui.MyButton;

public class Playing extends GameScene implements SceneMethods{

	private int[][] lvl;
	private TileManager tileManager; //this is gone in his
	private UnitManager unitManager;
	private BaseManager baseManager;
	private ProjectileManager projectileManager;
	private EnemyManager enemyManager;
	///private Tile selectedTile;
	private Camera camera;
	private BottomBar bottomBar;
	//mouseX within screen, mouseGridX within map
	private int mouseX, mouseY, dragX, dragY, mapWidth, mapHeight; //map = 100x100 tiles, 3200x3200 px
	private boolean dragging; //default false
	private GameScreen gameScreen;
	private Rectangle minimap;
	
	public BufferedImage atlas;
	private BufferedImage background;
	private Graphics bg;
	
	public Playing(Game game) { 
		super(game); 
		
		//initButtons(); 
		lvl = LevelBuild.getLevelData(); 
		tileManager = new TileManager(); //maybe remove
		baseManager = new BaseManager(this);
		projectileManager = new ProjectileManager(this);
		unitManager = new UnitManager(this, projectileManager);
		enemyManager = new EnemyManager(this, projectileManager, baseManager);
		unitManager.init();
		enemyManager.init();
		bottomBar = new BottomBar(0, 540, 1000, 200, this);
		gameScreen = game.getGameScreen();
		mapWidth = 80 * 32;
		mapHeight = 80 * 32;
		camera = new Camera(game.getScreenSize());
		background = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_BYTE_INDEXED);
		bg = background.getGraphics();
		minimap = new Rectangle(1000 - 5 - 190, 540 + 5, 190, 190);
		
		//the following initialization is to avoid bug
		//where mouse defaults to (0,0) & screen moves NW
		mouseX = (int) (game.getScreenSize().getWidth()/2);
		mouseY = (int) (game.getScreenSize().getHeight()/2);

		prerenderBackground();
		
	}

	private void prerenderBackground() {
		///I can get rid of tileManager here
		///use bg.drawImage(img, ...) where img is a subimage of atlas (get atlas in Playing)
		atlas = LoadSave.getSpriteAtlas();
		
		for(int y=0; y < lvl.length; y++) {
			for(int x=0; x < lvl[y].length; x++) {
				///int id = lvl[y][x];
				///bg.drawImage(tileManager.getSprite(id), x*32, y*32, null);
				bg.drawImage(atlas.getSubimage(8*32, 32, 32, 32), 32*x, 32*y, null);
			}
		}
	}

	public void update() {
		moveCamera(gameScreen.getKeyboardListener().isLeftPressed(), 
				gameScreen.getKeyboardListener().isRightPressed(),
				gameScreen.getKeyboardListener().isUpPressed(),
				gameScreen.getKeyboardListener().isDownPressed());
		//updateTick();
		//enemyManager.update();
		projectileManager.update();
		unitManager.update();
		baseManager.update();
		enemyManager.update();
	}
	
	@Override
	public void render(Graphics g) {
		
		g.setColor(Color.black);
		g.fillRect(0, 0, (int) camera.getSize().getWidth(), (int) camera.getSize().getHeight());
		
		g.drawImage(background, -camera.getX(), -camera.getY(), null);
		
		///drawSelectedTile(g);
		baseManager.draw(g);
		unitManager.draw(g);
		projectileManager.draw(g);
		if(dragging)
			drawDragBox(g);
		bottomBar.draw(g, baseManager.isBaseSelected());
	}
	
	private void drawDragBox(Graphics g) {
		g.setColor(Color.white);
		
		g.drawRect(Math.min(mouseX, dragX), Math.min(mouseY, dragY), Math.abs(dragX - mouseX), Math.abs(dragY - mouseY));
	}
	
	///private void drawSelectedTile(Graphics g) {
	///	if(selectedTile != null && drawSelect) {
	///		g.drawImage(selectedTile.getSprite(), mouseGridX - camera.getX(), mouseGridY - camera.getY(), 32, 32, null);
	///	}
	///}
	
	///public void setSelectedTile(Tile tile) {
	///	selectedTile = tile;
	///	drawSelect = true;
	///}

	public TileManager getTileManager() {
		return tileManager;
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	public int getMapWidth() {
		return mapWidth;
	}
	
	public int getMapHeight() {
		return mapHeight;
	}
	
	public UnitManager getUnitManager() {
		return unitManager;
	}
	
	public BaseManager getBaseManager() {
		return baseManager;
	}
	
	public void moveCamera(boolean l, boolean r, boolean u, boolean d) {
		if(!dragging)
			camera.move(l, r, u, d, mouseX, mouseY);
		else
			camera.move(l, r, u, d, 500, 500); //when the mouse gets near the border, don't move the camera if user is just dragging
	}
	
	public void mouseDragged(int x, int y) {
		dragX = x;
		
		if(mouseY < 540 && y < 540) {
			dragY = y;
			if(!dragging)
				baseManager.deselectAll();
			dragging = true;
		}
		else if(minimap.getBounds().contains(mouseX, mouseY)) {
			if(x < 1000 - 5 - 190)
				x = 1000 - 5 - 190;
			else if(x > 1000 - 5)
				x = 1000 - 5;
			
			if(y < 540 + 5)
				y = 540 + 5;
			else if(y > 740 - 5)
				y = 740 - 5;
			
			getCamera().setX((x - (1000 - 5 - 190)) * getMapWidth() / 190 - 1000 / 2);
			getCamera().setY((y - (540 + 5)) * getMapHeight() / 190 - 740 / 2);
		}
	}

	@Override
	public void mouseClicked(int x, int y, int button) {
		
		if(y > 540) {
			bottomBar.mouseClicked(x, y);
		///}else {
		///	changeTile(mouseGridX, mouseGridY);
		}
		
	}

	///private void changeTile(int x, int y) {
	///	int tileX = x / 32;
	///	int tileY = y / 32;
		
	///	if(selectedTile != null)
	///		lvl[tileY][tileX] = selectedTile.getID();
		
	///}

	@Override
	public void mouseMoved(int x, int y) {
		
		mouseX = x; //within screen
		mouseY = y;
		///mouseGridX = ((x + camera.getX()) / 32) * 32; //within map
		///mouseGridY = ((y + camera.getY()) / 32) * 32;
		
		if(y > 540) {
			bottomBar.mouseMoved(x, y);
			///drawSelect = false;
		}else {
			///drawSelect = true;
		}
		
	}
	
	@Override
	public void mousePressed(int x, int y, int button) {

		if(y > 540) {
			bottomBar.mousePressed(x, y, button);
		}else {
			if(!unitManager.mousePressed(x, y, button, false))
				baseManager.mousePressed(x, y, button);
			else
				baseManager.deselectAll();
		}
	}

	@Override
	public void mouseReleased(int x, int y, int button) {
		if(dragging)
			unitManager.dragSelect(mouseX, mouseY, dragX, dragY);
		
		dragging = false;
		bottomBar.mouseReleased(x, y);
		
	}

	public EnemyManager getEnemyManager() {
		return enemyManager;
	}

}
