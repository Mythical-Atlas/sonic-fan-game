package states;

import static java.lang.Math.*;

import static java.awt.event.KeyEvent.*;
import static functionholders.CollisionFunctions.*;
import static functionholders.ListFunctions.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import datatypes.Shape;
import datatypes.State;
import datatypes.Vector;
import main.Loader;
import objects.Player;
import shapes.Arc;
import shapes.Circle;
import shapes.InverseArc;
import shapes.Rectangle;
import shapes.Triangle;

public class TestState extends State {
	private final int SCALE = 250;
	
	private Player player;
	private Shape[] shapes;
	
	private double playerStartX;
	private double playerStartY;
	
	public TestState(int[][] map) {interpretMap(map);}
	
	public void update() {player.update(shapes, null, null, null, null, null);}
	
	public void draw(Graphics2D graphics) {
		graphics.setColor(Color.DARK_GRAY);
		graphics.fillRect(0, 0, Loader.graphicsWidth, Loader.graphicsHeight);
		
		graphics.setColor(Color.BLACK);
		for(int x = 0; x < (int)(Loader.graphicsWidth / 64) + 2; x++) {
			for(int y = 0; y < (int)(Loader.graphicsHeight / 64) + 2; y++) {
				graphics.drawRect((int)(x * 64 - (((int)player.pos.x % 128) / 2)), (int)(y * 64 -  (((int)player.pos.y % 128) / 2)), 64, 64);
			}
		}
		
		for(int i = 0; i < shapes.length; i++) {shapes[i].draw(graphics, player.pos.add(-Loader.graphicsWidth / 2, -Loader.graphicsHeight / 2));}
		
		player.draw(graphics);
	}
	
	public void keyPressed(int key) {
		if(key == VK_BACK_SPACE) {player = new Player(playerStartX, playerStartY);}
		player.keyPressed(key);
		if(key == VK_ESCAPE) {Loader.changeState = 0;}
	}
	public void keyReleased(int key) {player.keyReleased(key);}

	public void mouseClicked(MouseEvent mouse) {}
	public void mouseEntered(MouseEvent mouse) {}
	public void mouseExited(MouseEvent mouse) {}
	public void mousePressed(MouseEvent mouse) {}
	public void mouseReleased(MouseEvent mouse) {}
	
	private void interpretMap(int[][] map) {
		player = null;
		shapes = null;
		
		for(int x = 0; x < map.length; x++) {
			for(int y = 0; y < map[x].length; y++) {
				int tile = map[x][y];
				
				if(tile == 1) {
					playerStartX = x * SCALE + SCALE / 2;
					playerStartY = y * SCALE + SCALE / 2;
					player = new Player(playerStartX, playerStartY);
				}
				if(tile == 2) {
					Vector pos = new Vector(x * SCALE, y * SCALE);
					Vector size = new Vector(SCALE, SCALE);
					Shape shape = new Rectangle(pos, size, Color.WHITE);
					shapes = append(shapes, shape);
				}
				if(tile == 3) {
					Vector pos0 = new Vector(x * SCALE, y * SCALE + SCALE);
					Vector pos1 = new Vector(x * SCALE + SCALE, y * SCALE);
					Vector pos2 = new Vector(x * SCALE + SCALE, y * SCALE + SCALE);
					Shape shape = new Triangle(pos0, pos1, pos2, Color.WHITE);
					shapes = append(shapes, shape);
				}
				if(tile == 4) {
					Vector pos0 = new Vector(x * SCALE + SCALE, y * SCALE + SCALE);
					Vector pos1 = new Vector(x * SCALE, y * SCALE);
					Vector pos2 = new Vector(x * SCALE + SCALE, y * SCALE);
					Shape shape = new Triangle(pos0, pos1, pos2, Color.WHITE);
					shapes = append(shapes, shape);
				}
				if(tile == 5) {
					Vector pos0 = new Vector(x * SCALE + SCALE, y * SCALE);
					Vector pos1 = new Vector(x * SCALE, y * SCALE + SCALE);
					Vector pos2 = new Vector(x * SCALE, y * SCALE);
					Shape shape = new Triangle(pos0, pos1, pos2, Color.WHITE);
					shapes = append(shapes, shape);
				}
				if(tile == 6) {
					Vector pos0 = new Vector(x * SCALE, y * SCALE);
					Vector pos1 = new Vector(x * SCALE + SCALE, y * SCALE + SCALE);
					Vector pos2 = new Vector(x * SCALE, y * SCALE + SCALE);
					Shape shape = new Triangle(pos0, pos1, pos2, Color.WHITE);
					shapes = append(shapes, shape);
				}
				if(tile == 7) {
					Vector pos0 = new Vector(x * SCALE + SCALE, y * SCALE);
					Vector pos1 = new Vector(x * SCALE + SCALE, y * SCALE + SCALE);
					Shape shape = new Arc(pos0, pos1, PI / 2, Color.WHITE);
					shapes = append(shapes, shape);
				}
				if(tile == 8) {
					Vector pos0 = new Vector(x * SCALE, y * SCALE);
					Vector pos1 = new Vector(x * SCALE + SCALE, y * SCALE);
					Shape shape = new Arc(pos0, pos1, PI / 2, Color.WHITE);
					shapes = append(shapes, shape);
				}
				if(tile == 9) {
					Vector pos0 = new Vector(x * SCALE, y * SCALE + SCALE);
					Vector pos1 = new Vector(x * SCALE, y * SCALE);
					Shape shape = new Arc(pos0, pos1, PI / 2, Color.WHITE);
					shapes = append(shapes, shape);
				}
				if(tile == 10) {
					Vector pos0 = new Vector(x * SCALE + SCALE, y * SCALE + SCALE);
					Vector pos1 = new Vector(x * SCALE, y * SCALE + SCALE);
					Shape shape = new Arc(pos0, pos1, PI / 2, Color.WHITE);
					shapes = append(shapes, shape);
				}
				if(tile == 11) {
					Vector pos0 = new Vector(x * SCALE, y * SCALE + SCALE);
					Vector pos1 = new Vector(x * SCALE, y * SCALE);
					Shape shape = new InverseArc(pos0, pos1, Color.WHITE);
					shapes = append(shapes, shape);
				}
				if(tile == 12) {
					Vector pos0 = new Vector(x * SCALE + SCALE, y * SCALE + SCALE);
					Vector pos1 = new Vector(x * SCALE, y * SCALE + SCALE);
					Shape shape = new InverseArc(pos0, pos1, Color.WHITE);
					shapes = append(shapes, shape);
				}
				if(tile == 13) {
					Vector pos0 = new Vector(x * SCALE + SCALE, y * SCALE);
					Vector pos1 = new Vector(x * SCALE + SCALE, y * SCALE + SCALE);
					Shape shape = new InverseArc(pos0, pos1, Color.WHITE);
					shapes = append(shapes, shape);
				}
				if(tile == 14) {
					Vector pos0 = new Vector(x * SCALE, y * SCALE);
					Vector pos1 = new Vector(x * SCALE + SCALE, y * SCALE);
					Shape shape = new InverseArc(pos0, pos1, Color.WHITE);
					shapes = append(shapes, shape);
				}
			}
		}
	}
}