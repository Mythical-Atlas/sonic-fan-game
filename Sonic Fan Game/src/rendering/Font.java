package rendering;

import java.nio.ByteBuffer;

import static functionholders.ListFunctions.*;

import static java.lang.Math.*;

public class Font {
	private Image image;
	
	private int[][] glyphSizes;
	private float[][] glyphUVs;
	
	private int width;
	private int height;
	
	private float[] color;
	
	public Font(ByteBuffer imageBuffer) {
		image = new Image(imageBuffer);
		
		glyphSizes = null;
		glyphUVs = null;
		
		color = new float[]{1, 1, 1, 1};
	}
	
	public void addGlyph(int x, int y, int w, int h, char index) {
		if(glyphSizes == null) {glyphSizes = new int[index + 1][];}
		if(glyphUVs == null) {glyphUVs = new float[index + 1][];}
		if(index >= glyphSizes.length) {glyphSizes = resize(glyphSizes, index + 1);}
		if(index >= glyphUVs.length) {glyphUVs = resize(glyphUVs, index + 1);}
		
		
		glyphSizes[index] = new int[]{w, h};
		glyphUVs[index] = new float[]{
			((x + w) * 1.0f) / (image.getWidth() * 1.0f), ((y + h) * 1.0f) / (image.getHeight() * 1.0f),
			( x      * 1.0f) / (image.getWidth() * 1.0f), ( y      * 1.0f) / (image.getHeight() * 1.0f),
			((x + w) * 1.0f) / (image.getWidth() * 1.0f), ( y      * 1.0f) / (image.getHeight() * 1.0f),
			( x      * 1.0f) / (image.getWidth() * 1.0f), ((y + h) * 1.0f) / (image.getHeight() * 1.0f)
		};
		
		height = max(h, height);
	}
	
	public void setSpaceWidth(int width) {this.width = width;}
	
	public void setColor(int r, int g, int b, int a) {color = new float[]{r * 1.0f / 255.0f, g * 1.0f / 255.0f, b * 1.0f / 255.0f, a * 1.0f / 255.0f};}
	
	public void draw(double x, double y, int scale, String s, Renderer r) {
		char[] c = s.toCharArray();
		
		int ox = 0;
		int oy = 0;
		
		for(int i = 0; i < c.length; i++) {
			if(c[i] == '\n') {
				ox = 0;
				oy += height * scale + scale;
			}
			else if(c[i] == ' ') {ox += width * scale;}
			else {
				if(glyphSizes[c[i]] != null && glyphUVs[c[i]] != null) {
					image.setPositionAndSize(x + ox, y + oy, glyphSizes[c[i]][0] * scale, glyphSizes[c[i]][1] * scale);
					image.setUVMap(glyphUVs[c[i]]);
					image.setColor(color);
					image.draw(r);
					
					ox += glyphSizes[c[i]][0] * scale + scale;
				}
			}
		}
	}
}
