package objects;

import java.awt.Dimension;

public class Camera {

	private int x, y, speed; //width/length?
	private Dimension size;
	
	public Camera(Dimension size) {
		x = 0;
		y = 0;
		speed = 8;
		this.size = size;
	}
	
	public void move(boolean leftPressed, boolean rightPressed, boolean upPressed, boolean downPressed, int mouseX, int mouseY) {
		if(leftPressed || mouseX < 20)
			x -= speed;
		else if(rightPressed || mouseX >= size.getWidth() - 20)
			x += speed;
		
		if(upPressed || mouseY < 20)
			y -= speed;
		else if(downPressed || mouseY >= size.getHeight() - 20)
			y += speed;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public Dimension getSize() {
		return size;
	}
}
