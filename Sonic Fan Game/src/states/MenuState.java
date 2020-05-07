package states;

import static java.lang.Math.*;

import static java.awt.event.KeyEvent.*;
import static functionholders.CollisionFunctions.*;
import static functionholders.ListFunctions.*;
import static functionholders.MathFunctions.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;

import datatypes.Shape;
import datatypes.State;
import datatypes.Vector;
import main.Loader;
import objects.Button;
import objects.Player;
import shapes.Arc;
import shapes.Circle;
import shapes.InverseArc;
import shapes.Rectangle;
import shapes.Triangle;

public class MenuState extends State {
	public int map;
	
	private Button[] buttons;
	
	public MenuState() {
		map = 0;
		
		buttons = new Button[]{
			new Button(20, 20 + 60 *  0, 160, 40, "Test Map 1"),
			new Button(20, 20 + 60 *  1, 160, 40, "Test Map 2"),
			new Button(20, 20 + 60 *  2, 160, 40, "Leaf Forest Act 1"),
			//new Button(20, 20 + 60 *  3, 160, 40, "Triangle")
		};
	}
	
	public void update() {
		if(buttons[0].checkPressed()) {map = 1;}
		if(buttons[1].checkPressed()) {map = 2;}
		if(buttons[2].checkPressed()) {map = 3;}
		//if(buttons[3].checkPressed()) {map = 3;}
		
		if(map == 1) {Loader.changeState = 1;}
		if(map == 2) {Loader.changeState = 2;}
		if(map == 3) {Loader.changeState = 3;}
	}
	
	public void draw(Graphics2D graphics) {
		graphics.setColor(Color.DARK_GRAY);
		graphics.fillRect(0, 0, Loader.graphicsWidth, Loader.graphicsHeight);
		
		for(int i = 0; i < buttons.length; i++) {buttons[i].draw(graphics);}
		
		int x = Loader.graphicsWidth - 400;
		int y = 20;
		
		drawString(x, y,  0, "Controls", graphics);
		drawString(x, y,  1, "---------------------------", graphics);
		drawString(x, y,  2, "Left + Right Arrows = Move Sonic", graphics);
		drawString(x, y,  4, "Space Bar = Jump", graphics);
		drawString(x, y,  6, "Down Arrow = Crouch", graphics);
		drawString(x, y,  8, "Space Bar While Crouching = Spindash", graphics);
		drawString(x, y,  9, "(space again to charge, release down arrow to activate)", graphics);
		drawString(x, y, 11, "Shift = Debug Boost", graphics);
		drawString(x, y, 12, "(will not be present in final game)", graphics);
		drawString(x, y, 14, "F12 = Fullscreen", graphics);
		drawString(x, y, 15, "(can cause lag)", graphics);
		drawString(x, y, 17, "Escape = Back", graphics);
		drawString(x, y, 19, "F1 = Show/Hide Player Collision Masks", graphics);
		drawString(x, y, 21, "F2 = Show/Hide Tile Collision Masks", graphics);
	}
	
	public void keyPressed(int key) {if(key == VK_ESCAPE) {System.exit(0);}}
	public void keyReleased(int key) {}

	public void mouseClicked(MouseEvent mouse) {}
	public void mouseEntered(MouseEvent mouse) {}
	public void mouseExited(MouseEvent mouse) {}
	public void mousePressed(MouseEvent mouse) {for(int i = 0; i < buttons.length; i++) {buttons[i].mousePressed(mouse);}}
	public void mouseReleased(MouseEvent mouse) {for(int i = 0; i < buttons.length; i++) {buttons[i].mouseReleased(mouse);}}
	
	private void drawString(int x, int y, int line, String text, Graphics2D graphics) {
		int sh = graphics.getFontMetrics(graphics.getFont()).getHeight();
		
		graphics.setColor(Color.WHITE);
		graphics.drawString(text, x, y + line * sh);
	}
}