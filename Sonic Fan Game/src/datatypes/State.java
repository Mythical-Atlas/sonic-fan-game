package datatypes;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

public abstract class State {
	public abstract void update();
	public abstract void draw(Graphics2D graphics);
	
	public abstract void keyPressed(int key);
	public abstract void keyReleased(int key);
	
	public abstract void mouseClicked(MouseEvent mouse);
	public abstract void mouseEntered(MouseEvent mouse);
	public abstract void mouseExited(MouseEvent mouse);
	public abstract void mousePressed(MouseEvent mouse);
	public abstract void mouseReleased(MouseEvent mouse);
}