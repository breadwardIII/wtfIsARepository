package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import objects.Camera;
import scenes.Playing;
import testPackage.GameStates;

import static testPackage.GameStates.*;

public class KeyboardListener implements KeyListener{
	
	private boolean leftPressed, rightPressed, upPressed, downPressed;
	
	public KeyboardListener() {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT)
			leftPressed = true;
		
		else if(e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN)
			downPressed = true;
		else if(e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT)
			rightPressed = true;
		else if(e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP)
			upPressed = true;
			
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT)
			leftPressed = false;
		
		else if(e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN)
			downPressed = false;
		else if(e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT)
			rightPressed = false;
		else if(e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP)
			upPressed = false;
		
	}

	public boolean isLeftPressed() {
		return leftPressed;
	}
	public boolean isRightPressed() {
		return rightPressed;
	}

	public boolean isUpPressed() {
		return upPressed;
	}

	public boolean isDownPressed() {
		return downPressed;
	}
	
	

}
