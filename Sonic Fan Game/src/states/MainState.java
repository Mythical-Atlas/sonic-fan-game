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

public class MainState extends State {
	private final int SCALE = 2;
	
	private Player player;
	private Shape[] layer0;
	private Shape[] layer1;
	private Shape[] layer2;
	private Shape[] layer1Triggers;
	private Shape[] layer2Triggers;
	
	private double playerStartX;
	private double playerStartY;
	
	private boolean showTileMasks;
	
	private boolean toggle0 = true;
	private boolean toggle1 = true;
	
	public MainState() {
		interpretMap(Loader.leafForest1Map.json);
	}
		
	public void update() {
		player.update(layer0, layer1, layer2, layer1Triggers, layer2Triggers);
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
		
		Loader.leafForest1Map.draw(0, player.pos.add(-Loader.graphicsWidth / 2, -Loader.graphicsHeight / 2), SCALE, SCALE, graphics);
		Loader.leafForest1Map.draw(1, player.pos.add(-Loader.graphicsWidth / 2, -Loader.graphicsHeight / 2), SCALE, SCALE, graphics);
		
		if(showTileMasks) {
			for(int i = 0; i < layer0.length; i++) {layer0[i].draw(graphics, player.pos.add(-Loader.graphicsWidth / 2, -Loader.graphicsHeight / 2));}
			if(player.layer == 1 && layer1 != null) {for(int i = 0; i < layer1.length; i++) {layer1[i].draw(graphics, player.pos.add(-Loader.graphicsWidth / 2, -Loader.graphicsHeight / 2));}}
			if(player.layer == 2 && layer2 != null) {for(int i = 0; i < layer2.length; i++) {layer2[i].draw(graphics, player.pos.add(-Loader.graphicsWidth / 2, -Loader.graphicsHeight / 2));}}
		}
		
		player.draw(graphics);
		
		Loader.leafForest1Map.draw(2, player.pos.add(-Loader.graphicsWidth / 2, -Loader.graphicsHeight / 2), SCALE, SCALE, graphics);
	}
	
	public void keyPressed(int key) {
		if(key == VK_F1 && toggle0) {
			toggle0 = false;
			player.DRAW_MASKS = !player.DRAW_MASKS;
		}
		if(key == VK_F2 && toggle1) {
			toggle1 = false;
			showTileMasks = !showTileMasks;
		}
		
		if(key == VK_BACK_SPACE) {player = new Player(playerStartX, playerStartY);}
		player.keyPressed(key);
		if(key == VK_ESCAPE) {Loader.changeState = 0;}
	}
	public void keyReleased(int key) {
		if(key == VK_F1) {toggle0 = true;}
		if(key == VK_F2) {toggle1 = true;}
		
		player.keyReleased(key);
	}

	public void mouseClicked(MouseEvent mouse) {}
	public void mouseEntered(MouseEvent mouse) {}
	public void mouseExited(MouseEvent mouse) {}
	public void mousePressed(MouseEvent mouse) {}
	public void mouseReleased(MouseEvent mouse) {}
	
	private void interpretMap(TiledJSON json) {
		for(int tx = 0; tx < json.map[2].length; tx++) {
			for(int ty = 0; ty < json.map[2][tx].length; ty++) {
				int tile = json.map[2][tx][ty] - json.offsets[0];
				int w = json.tileWidth * SCALE;
				int h = json.tileHeight * SCALE;
				int w2 = w / 2;
				int h2 = h / 2;
				int x = tx * w;
				int y = ty * h;
				
				if(tile == 8) {player = new Player(x + w2, y + h2);}
			}
		}
		
		layer0 = getShapes(json, 3);
		layer1 = getShapes(json, 4);
		layer2 = getShapes(json, 5);
		layer1Triggers = getShapes(json, 6);
		layer2Triggers = getShapes(json, 7);
	}
	
	private Shape[] getShapes(TiledJSON json, int layer) {
		Shape[] shapes = null;
		
		for(int tx = 0; tx < json.map[layer].length; tx++) {
			for(int ty = 0; ty < json.map[layer][tx].length; ty++) {
				int tile = json.map[layer][tx][ty] - json.offsets[1];
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
				int s00 = 0;
				int s01 = w / 12;
				int s02 = s1 * 2;
				int s03 = s1 * 3;
				int s04 = s1 * 4;
				int s05 = s1 * 5;
				int s06 = s1 * 6;
				int s07 = s1 * 7;
				int s08 = s1 * 8;
				int s09 = s1 * 9;
				int s10 = s1 * 10;
				int s11 = s1 * 11;
				int s12 = s1 * 12;
				
				int x = tx * w;
				int y = ty * h;
				
				if(tile == 0) {shapes = append(shapes, new Rectangle(new Vector(x, y), new Vector(w, h), Color.WHITE));}
				if(tile == 45) {
					shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y), new Vector(x + w, y + h6), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 46) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + h6), new Vector(x + w, y + h3), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 3) {shapes = append(shapes, new Rectangle(new Vector(x, y + s4), new Vector(w, s8), Color.WHITE));}
				if(tile == 66) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + h3), new Vector(x + w, y + h6), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 2) {shapes = append(shapes, new Rectangle(new Vector(x, y + s2), new Vector(w, s10), Color.WHITE));}
				/*if(tile == 108) {
					shapes = append(shapes, new Shape(new Vector[]{
							new Vector(x + s00, y + s06),
							new Vector(x + s06, y + s06),
							new Vector(x + s06, y + s12),
							new Vector(x + s00, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, new Arc(
						new Vector(x + s06, y + s06 + 200 * SCALE),
						200 * SCALE,
						new Vector(x + s12, y + s07),
						new Vector(x + s06, y + s06),
					Color.WHITE));
				}*/
				if(tile == 109) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s04, y + s08),
						new Vector(x + s12, y + s12),
						new Vector(x + s04, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, new Arc(
						new Vector(x - s06, y + s06 + 200 * SCALE),
						200 * SCALE,
						new Vector(x + s04, y + s08),
						new Vector(x + s00, y + s07),
					Color.WHITE));
				}
				/*if(tile == 246) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s00),
						new Vector(x + s10, y + s05),
						new Vector(x + s10, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s10, y + s06),
						new Vector(x + s12, y + s06),
						new Vector(x + s12, y + s12),
						new Vector(x + s10, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, new InverseArc(
						new Vector(x + s10, y + s05),
						PI + PI / 2 - PI / 8,
						new Vector(x + s10 + s05, y + s06),
						PI + PI / 2,
						new Vector(x + s10, y + s05),
						new Vector(x + s12, y + s06),
					Color.WHITE));
				}*/
				/*if(tile == 21) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s06),
						new Vector(x + s12, y + s06),
						new Vector(x + s12, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, new InverseArc(
						new Vector(x - s02, y + s05),
						PI + PI / 2 - PI / 8,
						new Vector(x + s03, y + s06),
						PI + PI / 2,
						new Vector(x + s00, y + s05),
						new Vector(x + s03, y + s06),
					Color.WHITE));
				}*/
				/*if(tile == 269) {
					shapes = append(shapes, new Arc(
						new Vector(x + s08, y + s09),
						PI / 4,
						new Vector(x + s00, y + s06),
						PI / 2,
					Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s08, y + s09),
						new Vector(x + s11, y + s12),
						new Vector(x + s08, y + s12),
					}, Color.WHITE));
				}*/
				if(tile == 180) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y), new Vector(x + w - w12, y), new Vector(x + w, y + h12), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 181) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + s1), new Vector(x + s11, y + h), new Vector(x, y + h)}, Color.WHITE));}
				/*if(tile == 172) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s06),
						new Vector(x + s12, y + s06),
						new Vector(x + s12, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s01),
						new Vector(x + s02, y + s03),
						new Vector(x + s02, y + s06),
						new Vector(x + s00, y + s06)
					}, Color.WHITE));
					shapes = append(shapes, new InverseArc(
						new Vector(x + s02, y + s03),
						PI + PI / 4,
						new Vector(x + s09, y + s06),
						PI + PI / 2,
					Color.WHITE));
				}*/
				if(tile == 4) {shapes = append(shapes, new Rectangle(new Vector(x, y + h2), new Vector(w, s6), Color.WHITE));}
				if(tile == 8) {shapes = append(shapes, new Rectangle(new Vector(x + w2, y + h2), new Vector(w2, h2), Color.WHITE));}
				if(tile == 9) {shapes = append(shapes, new Rectangle(new Vector(x, y + h2), new Vector(w2, h2), Color.WHITE));}
				if(tile == 10) {
					shapes = append(shapes, new Rectangle(new Vector(x, y), new Vector(w2, h), Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{new Vector(x + s6, y + s1), new Vector(x + w, y + s3), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));
				}
				if(tile == 11) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + s3), new Vector(x + w, y + s6), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 12) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + s6), new Vector(x + w, y + s9), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 13) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + s9), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 22) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + h), new Vector(x + w, y + s9), new Vector(x + w, y + h)}, Color.WHITE));}
				if(tile == 23) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + s9), new Vector(x + w, y + s6), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 249) {
					shapes = append(shapes, new Arc(
						new Vector(x + s00, y + s12 + s01),
						176 / 2 * SCALE,
						new Vector(x + s09, y + s09),
						new Vector(x + s00, y + s06),
					Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s09, y + s09),
						new Vector(x + s12, y + s12),
						new Vector(x + s09, y + s12),
					}, Color.WHITE));
				}
				if(tile == 40) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 43) {
					shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y), new Vector(x + s11, y + s11), new Vector(x + s11, y + h), new Vector(x, y + h)}, Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{new Vector(x + s11, y + s11), new Vector(x + w, y + h - h24), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));
				}
				if(tile == 44) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + h - h24), new Vector(x + s2, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 251) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y), new Vector(x + w2, y + s1 + h24), new Vector(x + s9, y + s3), new Vector(x + w, y + h2), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 127) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + h2), new Vector(x + w2, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 120) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y), new Vector(x + w2, y), new Vector(x + w, y + h2), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));}
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
				if(tile == 193) {
					shapes = append(shapes, new Rectangle(new Vector(x, y), new Vector(w2, h), Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{new Vector(x + w2, y), new Vector(x + w, y + h24), new Vector(x + w, y + h), new Vector(x + w2, y + h)}, Color.WHITE));
				}
				if(tile == 228) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + h24), new Vector(x + w, y + h2 - h24), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));}
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
				if(tile == 167) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s00),
						new Vector(x + s12, y + s03),
						new Vector(x + s12, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
				}
				if(tile == 168) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s03),
						new Vector(x + s12, y + s06),
						new Vector(x + s12, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
				}
				if(tile == 47) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s04),
						new Vector(x + s12, y + s06),
						new Vector(x + s12, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
				}
				if(tile == 48) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s06),
						new Vector(x + s12, y + s08),
						new Vector(x + s12, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
				}
				if(tile == 6) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s08),
						new Vector(x + s12, y + s08),
						new Vector(x + s12, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
				}
				if(tile == 64) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s08),
						new Vector(x + s12, y + s06),
						new Vector(x + s12, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
				}
				if(tile == 104) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s00),
						new Vector(x + s12, y + s00),
						new Vector(x + s12, y + s06),
						new Vector(x + s00, y + s06)
					}, Color.WHITE));
				}
				if(tile == 72) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s10, y + s00),
						new Vector(x + s12, y + s00),
						new Vector(x + s12, y + s12),
						new Vector(x + s10, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s06),
						new Vector(x + s10, y + s06),
						new Vector(x + s10, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, new InverseArc(
						new Vector(x + s00, y + s06),
						new Vector(x + s00, y + s06 - s10),
						new Vector(x + s09, y + s00),
						s10,
					Color.WHITE));
				}
				if(tile == 51) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s00),
						new Vector(x + s02, y + s00),
						new Vector(x + s02, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, new InverseArc(
						new Vector(x + s06, y + s00),
						new Vector(x + s12, y + s08),
						new Vector(x + s02, y + s08),
						s10,
					Color.WHITE));
				}
				if(tile == 52) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s10, y + s00),
						new Vector(x + s12, y + s00),
						new Vector(x + s12, y + s12),
						new Vector(x + s10, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, new InverseArc(
						new Vector(x + s10, y + s08),
						new Vector(x + s00, y + s08),
						new Vector(x + s06, y + s00),
						s10,
					Color.WHITE));
					shapes = append(shapes, new InverseArc(
						new Vector(x + s09, y + s12),
						new Vector(x + s00, y + s08),
						new Vector(x + s10, y + s08),
						s10,
					Color.WHITE));
				}
				if(tile == 71) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s00),
						new Vector(x + s02, y + s00),
						new Vector(x + s02, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
				}
				if(tile == 34) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s04),
						new Vector(x + s12, y + s04),
						new Vector(x + s12, y + s10),
						new Vector(x + s00, y + s10)
					}, Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s07, y + s10),
						new Vector(x + s12, y + s10),
						new Vector(x + s12, y + s12),
						new Vector(x + s07, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, new InverseArc(
						new Vector(x + s06, y + s12),
						new Vector(x + s00, y + s10 + s10),
						new Vector(x + s00, y + s10),
						s10,
					Color.WHITE));
				}
				if(tile == 33) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s04),
						new Vector(x + s12, y + s04),
						new Vector(x + s12, y + s10),
						new Vector(x + s00, y + s10)
					}, Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s10),
						new Vector(x + s05, y + s10),
						new Vector(x + s05, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, new InverseArc(
						new Vector(x + s12, y + s10),
						new Vector(x + s12, y + s10 + s10),
						new Vector(x + s06, y + s12),
						s10,
					Color.WHITE));
				}
				if(tile == 111) {
					shapes = append(shapes, new InverseArc(
						new Vector(x + s02, y + s02),
						new Vector(x + s12, y + s02),
						new Vector(x + s12, y + s12),
						s10,
					Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s00),
						new Vector(x + s02, y + s00),
						new Vector(x + s02, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
				}
			}
		}
		
		return(shapes);
	}
}