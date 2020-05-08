package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;

import datatypes.Vector;
import main.Loader;

public class Button {
	private int x;
	private int y;
	private int w;
	private int h;
	private String text;
	
	private boolean lmb;
	private boolean canPress;
	
	public Button(int x, int y, int w, int h, String text) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.text = text;
		
		canPress = true;
	}
	
	public void draw(Graphics2D graphics) {
		int sh = graphics.getFontMetrics(graphics.getFont()).getHeight();
		int sw = graphics.getFontMetrics(graphics.getFont()).stringWidth(text);
		
		if(checkMouseHovering()) {
			if(!lmb) {graphics.setColor(Color.WHITE);}
			else {graphics.setColor(Color.GRAY);}
		}
		else {graphics.setColor(Color.LIGHT_GRAY);}
		
		graphics.fillRect(x, y, w, h);
		
		graphics.setColor(Color.BLACK);
		graphics.drawString(text, x + w / 2 - sw / 2, y + h / 2 + sh / 4);
	}
	
	public boolean checkPressed() {
		if(checkMouseHovering() && lmb && canPress) {
			canPress = false;
			return(true);
		}
		else {if(lmb && canPress) {canPress = false;}}
		return(false);
	}
	
	public void mousePressed(MouseEvent mouse) {if(mouse.getButton() == MouseEvent.BUTTON1) {lmb = true;}}
	public void mouseReleased(MouseEvent mouse) {
		if(mouse.getButton() == MouseEvent.BUTTON1) {
			lmb = false;
			canPress = true;
		}
	}
	
	private boolean checkMouseHovering() {
		Vector mousePos = getMousePos();
		if(mousePos == null) {return(false);}
		
		return(mousePos.x >= x && mousePos.x < x + w && mousePos.y >= y && mousePos.y < y + h);
	}
	
	private Vector getMousePos() {
		Point windowPos = null;
		//try {windowPos = Loader.frame.getLocationOnScreen();}
		//catch(Exception e) {}
		if(windowPos == null) {return(null);}
		
		double mouseDeltaX = MouseInfo.getPointerInfo().getLocation().x - windowPos.x;
		double mouseDeltaY = MouseInfo.getPointerInfo().getLocation().y - windowPos.y;
		
		if(!Loader.fullscreen) {return(new Vector(mouseDeltaX - 8, mouseDeltaY - 32));}
		else {return(new Vector(mouseDeltaX, mouseDeltaY));}
	}
}