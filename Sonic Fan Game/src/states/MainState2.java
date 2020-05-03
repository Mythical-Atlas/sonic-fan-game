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
import datatypes.TiledJSON;
import datatypes.Vector;
import main.Loader;
import objects.Player;
import shapes.Arc;
import shapes.Circle;
import shapes.InverseArc;
import shapes.Rectangle;
import shapes.Triangle;

public class MainState2 extends State {
	private final int SCALE = 2;
	
	private Player player;
	private Shape[] shapes;
	
	private double playerStartX;
	private double playerStartY;
	
	public MainState2() {
		interpretMap(Loader.leafForest1Map.json);
	}
		
	public void update() {
		player.update(shapes);
	}
	
	public void draw(Graphics2D graphics) {
		graphics.setColor(Color.DARK_GRAY);
		graphics.fillRect(0, 0, Loader.graphicsWidth, Loader.graphicsHeight);
		
		graphics.setColor(Color.BLACK);
		for(int x = 0; x < (int)(Loader.graphicsWidth / 64) + 2; x++) {
			for(int y = 0; y < (int)(Loader.graphicsHeight / 64) + 2; y++) {
				graphics.drawRect((int)(x * 64 - (((int)player.pos.x % 128) / 2)), (int)(y * 64 -  (((int)player.pos.y % 128) / 2)), 64, 64);
			}
		}
		
		Loader.leafForest1Map.draw(player.pos.add(-Loader.graphicsWidth / 2, -Loader.graphicsHeight / 2), SCALE, SCALE, graphics);
		
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
	
	private void interpretMap(TiledJSON json) {
		player = null;
		shapes = null;
		
		for(int tx = 0; tx < json.map[0].length; tx++) {
			for(int ty = 0; ty < json.map[0][tx].length; ty++) {
				int tile = json.map[0][tx][ty] - 1;
				int w = json.tileWidth * SCALE;
				int h = json.tileHeight * SCALE;
				int w2 = w / 2;
				int h2 = h / 2;
				int w3 = w / 3;
				int h3 = h / 3;
				int w6 = w / 6;
				int h6 = h / 6;
				int w12 = w / 12;
				int h12 = h / 12;
				int w24 = w / 24;
				int h24 = h / 24;
				int s0 = 0;
				int s1 = w / 12;
				int s2 = s1 * 2;
				int s3 = s1 * 3;
				int s4 = s1 * 4;
				int s5 = s1 * 5;
				int s6 = s1 * 6;
				int s7 = s1 * 7;
				int s8 = s1 * 8;
				int s9 = s1 * 9;
				int s10 = s1 * 10;
				int s11 = s1 * 11;
				int s12 = s1 * 12;
				
				int x = tx * w;
				int y = ty * h;
				
				if(tile == 9) {player = new Player(x + w2, y + h2);}
				if(tile == 11 ||
				   tile == 3 ||
				   tile == 116 ||
				   tile == 117 ||
				   tile == 66 ||
				   tile == 67 ||
				   tile == 140 ||
				   tile == 160 ||
				   tile == 230 ||
				   tile == 235 ||
				   tile == 34) {shapes = append(shapes, new Rectangle(new Vector(x, y), new Vector(w, h), Color.WHITE));}
				if(tile == 12) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y), new Vector(x + w, y + h6), new Vector(x + w, y + h2), new Vector(x, y + h2)}, Color.WHITE));}
				if(tile == 13) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + h6), new Vector(x + w, y + h3), new Vector(x + w, y + h2 + h6), new Vector(x, y + h2 + h6)}, Color.WHITE));}
				if(tile == 14) {shapes = append(shapes, new Rectangle(new Vector(x, y + h3), new Vector(w, h2 + s1), Color.WHITE));}
				if(tile == 15) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + h3), new Vector(x + w, y + h6), new Vector(x + w, y + h2 + h3), new Vector(x, y + h2 + h3)}, Color.WHITE));}
				if(tile == 16) {shapes = append(shapes, new Rectangle(new Vector(x, y + h6), new Vector(w, h2), Color.WHITE));}
				if(tile == 17) {
					shapes = append(shapes, new Rectangle(new Vector(x, y + h2), new Vector(w2, h2), Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{new Vector(x + w2, y + h2), new Vector(x + w, y + h2 + h24), new Vector(x + w, y + h), new Vector(x + w2, y + h)}, Color.WHITE));
				}
				if(tile == 18) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + h2 + h24), new Vector(x + w3, y + h3 + h3), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 35) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y), new Vector(x + w, y + h2 - h24), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 36 ||
				   tile == 204) {
					shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + h2 - h24), new Vector(x + w6, y + h2), new Vector(x + w6, y + h), new Vector(x, y + h)}, Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{new Vector(x + w6, y + h2), new Vector(x + w, y + h2), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));
				}
				if(tile == 37) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + h2), new Vector(x + w2, y + h2 + h6 - h24), new Vector(x + w - w12, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 69 ||
				   tile == 61) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y), new Vector(x + w - w12, y), new Vector(x + w, y + h12), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 70) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + s1), new Vector(x + s11, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 62) {
					shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + s1),
																   new Vector(x + s3, y + s4),
																   new Vector(x + s3, y + h),
																   new Vector(x, y + h)}, Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{new Vector(x + s3, y + s4),
							                                       new Vector(x + s7, y + s6),
							                                       new Vector(x + s7, y + h),
							                                       new Vector(x, y + h)}, Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{new Vector(x + s7, y + s6),
                                                                   new Vector(x + w, y + s6),
                                                                   new Vector(x + w, y + h),
                                                                   new Vector(x, y + h)}, Color.WHITE));
				}
				if(tile == 63 ||
				   tile == 206 ||
				   tile == 205 ||
				   tile == 42 ||
				   tile == 43 ||
				   tile == 44 ||
				   tile == 211 ||
				   tile == 41) {shapes = append(shapes, new Rectangle(new Vector(x, y + h2), new Vector(w, s4), Color.WHITE));}
				if(tile == 110) {shapes = append(shapes, new Rectangle(new Vector(x, y), new Vector(w, s4), Color.WHITE));}
				if(tile == 64) {shapes = append(shapes, new Rectangle(new Vector(x, y + h2), new Vector(w2, h2), Color.WHITE));}
				if(tile == 83) {
					shapes = append(shapes, new Rectangle(new Vector(x, y), new Vector(w2, h), Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{new Vector(x + s6, y + s1), new Vector(x + w, y + s3), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));
				}
				if(tile == 84) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + s3), new Vector(x + w, y + s6), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 85) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + s6), new Vector(x + w, y + s9), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 86) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + s9), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 88) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + h), new Vector(x + w, y + s9), new Vector(x + w, y + h)}, Color.WHITE));}
				if(tile == 89) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + s9), new Vector(x + w, y + s6), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 120 ||
				   tile == 250) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + h2),
						                                                        new Vector(x + s1, y + h2),
						                                                        new Vector(x + s5, y + h2 + s1),
						                                                        new Vector(x + s7, y + h2 + s2),
						                                                        new Vector(x + s9, y + h2 + s3),
						                                                        new Vector(x + w, y + h),
						                                                        new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 100) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 161) {
					shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y), new Vector(x + s11, y + s11), new Vector(x + s11, y + h), new Vector(x, y + h)}, Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{new Vector(x + s11, y + s11), new Vector(x + w, y + h - h24), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));
				}
				if(tile == 162) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + h - h24), new Vector(x + s2, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 154) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y), new Vector(x + w2, y + s1 + h24), new Vector(x + s9, y + s3), new Vector(x + w, y + h2), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 155) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + h2), new Vector(x + w2, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 175) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y), new Vector(x + w2, y), new Vector(x + w, y + h2), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 200) {
					shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + s6),
							                                       new Vector(x + s3, y + s8),
							                                       new Vector(x + s3, y + h),
							                                       new Vector(x, y + h)}, Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{new Vector(x + s3, y + s8),
									                               new Vector(x + s6, y + s8 + h24),
									                               new Vector(x + s6, y + h),
									                               new Vector(x + s3, y + h)}, Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{new Vector(x + s6, y + s8 + h24),
									                               new Vector(x + s9, y + s8),
									                               new Vector(x + s9, y + h),
									                               new Vector(x + s6, y + h)}, Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{new Vector(x + s9, y + s8),
									                               new Vector(x + w, y + s6),
									                               new Vector(x + w, y + h),
									                               new Vector(x + s9, y + h)}, Color.WHITE));
				}
				if(tile == 270) {
					shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y),
																   new Vector(x + s4, y + s4),
																   new Vector(x + s4, y + h),
																   new Vector(x, y + h)}, Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{new Vector(x + s4, y + s4),
							                                       new Vector(x + s8, y + s6),
							                                       new Vector(x + s8, y + h),
							                                       new Vector(x, y + h)}, Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{new Vector(x + s8, y + s6),
                                                                   new Vector(x + w, y + s6),
                                                                   new Vector(x + w, y + h),
                                                                   new Vector(x, y + h)}, Color.WHITE));
				}
				if(tile == 202) {
					shapes = append(shapes, new Rectangle(new Vector(x, y), new Vector(w2, h), Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{new Vector(x + w2, y), new Vector(x + w, y + h24), new Vector(x + w, y + h), new Vector(x + w2, y + h)}, Color.WHITE));
				}
				if(tile == 203) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + h24), new Vector(x + w, y + h2 - h24), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 173) {
					shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + h2),
							                                       new Vector(x + w2, y + h2),
							                                       new Vector(x + w2, y + h),
							                                       new Vector(x, y + h)}, Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{new Vector(x + s6, y + s6),
                                                                   new Vector(x + s9, y + s5),
									                               new Vector(x + s9, y + h),
									                               new Vector(x + s6, y + h)}, Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{new Vector(x + s9, y + s5),
									                               new Vector(x + s11, y + s3),
									                               new Vector(x + s11, y + h),
									                               new Vector(x + s9, y + h)}, Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{new Vector(x + s11, y + s3),
									                               new Vector(x + w, y),
									                               new Vector(x + w, y + h),
									                               new Vector(x + s11, y + h)}, Color.WHITE));
				}
				if(tile == 118) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y), new Vector(x + w, y), new Vector(x + w, y + s1), new Vector(x, y + s4)}, Color.WHITE));}
				if(tile == 119) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y), new Vector(x + s4, y), new Vector(x, y + s1)}, Color.WHITE));}
				if(tile == 39) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + h), new Vector(x + w, y + s9), new Vector(x + w, y + h)}, Color.WHITE));}
				if(tile == 40) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + s9), new Vector(x + w, y + s6), new Vector(x + w, y + s10), new Vector(x + s4, y + h), new Vector(x, y + h)}, Color.WHITE));}
			}
		}
	}
}