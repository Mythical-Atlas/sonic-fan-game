package rendering;

import static java.lang.Math.*;
import static org.lwjgl.opengl.GL33.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import main.Window;

public class Image {
	private int vaoID;
	private int vboID;
	private int eboID;
	
	public Texture tex;
	
	public float[] vertexArray = {
		 0.0f, 0.0f, 0.0f,		1.0f, 1.0f, 1.0f, 1.0f,		1, 1,
		 0.0f, 0.0f, 0.0f,		1.0f, 1.0f, 1.0f, 1.0f,		0, 0,
		 0.0f, 0.0f, 0.0f,		1.0f, 1.0f, 1.0f, 1.0f,		1, 0,
		 0.0f, 0.0f, 0.0f,		1.0f, 1.0f, 1.0f, 1.0f,		0, 1
	};
	
	private int[] elementArray = {
		2, 1, 0,
		0, 1, 3
	};
	
	public Image(String filepath) {tex = new Texture(filepath);}
	public Image(ByteBuffer imageBuffer) {tex = new Texture(imageBuffer);}
	public Image(Texture tex) {this.tex = tex;}
	public Image(Texture tex, float[] vertices) {
		vertexArray = new float[36];
		for(int i = 0; i < vertices.length; i++) {vertexArray[i] = vertices[i];}
		this.tex = tex;
	}
	
	public void setVertices(float[] vertices) {for(int i = 0; i < vertices.length; i++) {vertexArray[i] = vertices[i];}}
	
	public void setPositions(double x, double y, double xScale, double yScale) {
		vertexArray[0]  = (float)x + (float)(tex.width * xScale);
		vertexArray[1]  = (float)y;
		
		vertexArray[9]  = (float)x;
		vertexArray[10] = (float)y + (float)(tex.height * -yScale);
		
		vertexArray[18] = (float)x + (float)(tex.width * xScale);
		vertexArray[19] = (float)y + (float)(tex.height * -yScale);
		
		vertexArray[27] = (float)x;
		vertexArray[28] = (float)y;
		
		vertexArray[1]  += (float)(tex.height * yScale);
		vertexArray[10] += (float)(tex.height * yScale);
		vertexArray[19] += (float)(tex.height * yScale);
		vertexArray[28] += (float)(tex.height * yScale);
		
		if(xScale < 0) {
			vertexArray[0]  -= (float)(tex.width * xScale);
			vertexArray[9]  -= (float)(tex.width * xScale);
			vertexArray[18] -= (float)(tex.width * xScale);
			vertexArray[27] -= (float)(tex.width * xScale);
		}
		if(yScale < 0) {
			vertexArray[1]  -= (float)(tex.height * yScale);
			vertexArray[10] -= (float)(tex.height * yScale);
			vertexArray[19] -= (float)(tex.height * yScale);
			vertexArray[28] -= (float)(tex.height * yScale);
		}
	}
	
	public void setPositionAndSize(double x, double y, double width, double height) {
		vertexArray[0]  = (float)x + (float)(width);
		vertexArray[1]  = (float)y;
		
		vertexArray[9]  = (float)x;
		vertexArray[10] = (float)y + (float)(-height);
		
		vertexArray[18] = (float)x + (float)(width);
		vertexArray[19] = (float)y + (float)(-height);
		
		vertexArray[27] = (float)x;
		vertexArray[28] = (float)y;
		
		vertexArray[1]  += (float)(height);
		vertexArray[10] += (float)(height);
		vertexArray[19] += (float)(height);
		vertexArray[28] += (float)(height);
		
		if(width < 0) {
			vertexArray[0]  -= (float)(width);
			vertexArray[9]  -= (float)(width);
			vertexArray[18] -= (float)(width);
			vertexArray[27] -= (float)(width);
		}
		if(height < 0) {
			vertexArray[1]  -= (float)(height);
			vertexArray[10] -= (float)(height);
			vertexArray[19] -= (float)(height);
			vertexArray[28] -= (float)(height);
		}
	}
	
	public void setRawPositions(float[] pos) {
		vertexArray[0]  = pos[ 0];
		vertexArray[1]  = pos[ 1];
		vertexArray[2]  = pos[ 2];
		
		vertexArray[9]  = pos[ 3];
		vertexArray[10] = pos[ 4];
		vertexArray[11] = pos[ 5];
		
		vertexArray[18] = pos[ 6];
		vertexArray[19] = pos[ 7];
		vertexArray[20] = pos[ 8];
		
		vertexArray[27] = pos[ 9];
		vertexArray[28] = pos[10];
		vertexArray[29] = pos[11];
	}
	
	public void setUVMap(float[] map) {
		vertexArray[7]  = map[0];
		vertexArray[8]  = map[1];
		vertexArray[16] = map[2];
		vertexArray[17] = map[3];
		vertexArray[25] = map[4];
		vertexArray[26] = map[5];
		vertexArray[34] = map[6];
		vertexArray[35] = map[7];
	}
	
	public void setColors(float[] colors) {
		vertexArray[ 3] = colors[ 0];
		vertexArray[ 4] = colors[ 1];
		vertexArray[ 5] = colors[ 2];
		vertexArray[ 6] = colors[ 3];
		vertexArray[12] = colors[ 4];
		vertexArray[13] = colors[ 5];
		vertexArray[14] = colors[ 6];
		vertexArray[15] = colors[ 7];
		vertexArray[21] = colors[ 8];
		vertexArray[22] = colors[ 9];
		vertexArray[23] = colors[10];
		vertexArray[24] = colors[11];
		vertexArray[30] = colors[12];
		vertexArray[31] = colors[13];
		vertexArray[32] = colors[14];
		vertexArray[33] = colors[15];
	}
	
	public void setColor(float r, float g, float b, float a) {
		vertexArray[ 3] = r;
		vertexArray[ 4] = g;
		vertexArray[ 5] = b;
		vertexArray[ 6] = a;
		vertexArray[12] = r;
		vertexArray[13] = g;
		vertexArray[14] = b;
		vertexArray[15] = a;
		vertexArray[21] = r;
		vertexArray[22] = g;
		vertexArray[23] = b;
		vertexArray[24] = a;
		vertexArray[30] = r;
		vertexArray[31] = g;
		vertexArray[32] = b;
		vertexArray[33] = a;
	}
	
	public float[] getPositions() {
		return(
			new float[]{
				vertexArray[ 0], vertexArray[ 1],
				vertexArray[ 9], vertexArray[10],
				vertexArray[18], vertexArray[19],
				vertexArray[27], vertexArray[28]
			}
		);
	}
	public float[] getPositions2() {
		return(
			new float[]{
				vertexArray[ 0], vertexArray[ 1], vertexArray[ 2],
				vertexArray[ 9], vertexArray[10], vertexArray[11],
				vertexArray[18], vertexArray[19], vertexArray[20],
				vertexArray[27], vertexArray[28], vertexArray[29]
			}
		);
	}
	
	public float[] getColors() {
		return(
			new float[]{
					vertexArray[ 3], vertexArray[ 4], vertexArray[ 5], vertexArray[ 6],
					vertexArray[12], vertexArray[13], vertexArray[14], vertexArray[15],
					vertexArray[21], vertexArray[22], vertexArray[23], vertexArray[24],
					vertexArray[30], vertexArray[31], vertexArray[32], vertexArray[33]
			}
		);
	}
	public float[] getUVMaps() {
		return(
			new float[]{
				vertexArray[ 7], vertexArray[ 8],
				vertexArray[16], vertexArray[17],
				vertexArray[25], vertexArray[26],
				vertexArray[34], vertexArray[35]
			}
		);
	}
	
	public void draw(Renderer r) {r.add(this);}
	
	public void draw(double x, double y, double xScale, double yScale, Renderer r) {
		vertexArray[0]  = (float)x + (float)(tex.width * xScale);
		vertexArray[1]  = (float)y;
		
		vertexArray[9]  = (float)x;
		vertexArray[10] = (float)y + (float)(tex.height * -yScale);
		
		vertexArray[18] = (float)x + (float)(tex.width * xScale);
		vertexArray[19] = (float)y + (float)(tex.height * -yScale);
		
		vertexArray[27] = (float)x;
		vertexArray[28] = (float)y;
		
		vertexArray[1]  += (float)(tex.height * yScale);
		vertexArray[10] += (float)(tex.height * yScale);
		vertexArray[19] += (float)(tex.height * yScale);
		vertexArray[28] += (float)(tex.height * yScale);
		
		if(xScale < 0) {
			vertexArray[0]  -= (float)(tex.width * xScale);
			vertexArray[9]  -= (float)(tex.width * xScale);
			vertexArray[18] -= (float)(tex.width * xScale);
			vertexArray[27] -= (float)(tex.width * xScale);
		}
		if(yScale < 0) {
			vertexArray[1]  -= (float)(tex.height * yScale);
			vertexArray[10] -= (float)(tex.height * yScale);
			vertexArray[19] -= (float)(tex.height * yScale);
			vertexArray[28] -= (float)(tex.height * yScale);
		}
		
		r.add(this);
	}
	
	public void draw(double x, double y, double ox, double oy, double angle, double xScale, double yScale, Renderer r) {
		vertexArray[0]  = (float)x + (float)(tex.width * xScale);
		vertexArray[1]  = (float)y;
		
		vertexArray[9]  = (float)x;
		vertexArray[10] = (float)y + (float)(tex.height * -yScale);
		
		vertexArray[18] = (float)x + (float)(tex.width * xScale);
		vertexArray[19] = (float)y + (float)(tex.height * -yScale);
		
		vertexArray[27] = (float)x;
		vertexArray[28] = (float)y;
		
		vertexArray[1]  += tex.height * yScale;
		vertexArray[10] += tex.height * yScale;
		vertexArray[19] += tex.height * yScale;
		vertexArray[28] += tex.height * yScale;
		
		if(xScale < 0) {
			vertexArray[0]  -= tex.width * xScale;
			vertexArray[9]  -= tex.width * xScale;
			vertexArray[18] -= tex.width * xScale;
			vertexArray[27] -= tex.width * xScale;
		}
		if(yScale < 0) {
			vertexArray[1]  -= tex.height * yScale;
			vertexArray[10] -= tex.height * yScale;
			vertexArray[19] -= tex.height * yScale;
			vertexArray[28] -= tex.height * yScale;
		}
		
		double[] r0 = rotate(vertexArray[ 0], vertexArray[ 1], ox, oy, angle);
		double[] r1 = rotate(vertexArray[ 9], vertexArray[10], ox, oy, angle);
		double[] r2 = rotate(vertexArray[18], vertexArray[19], ox, oy, angle);
		double[] r3 = rotate(vertexArray[27], vertexArray[28], ox, oy, angle);
		
		vertexArray[0]  = (float)r0[0];
		vertexArray[1]  = (float)r0[1];
		vertexArray[9]  = (float)r1[0];
		vertexArray[10] = (float)r1[1];
		vertexArray[18] = (float)r2[0];
		vertexArray[19] = (float)r2[1];
		vertexArray[27] = (float)r3[0];
		vertexArray[28] = (float)r3[1];
		
		r.add(this);
	}
	
	public int getWidth() {return(tex.width);}
	public int getHeight() {return(tex.height);}
	
	private double[] rotate(double x, double y, double ox, double oy, double angle) {
		double tx = ox + (x - ox) * cos(angle) - (y - oy) * sin(angle);
		double ty = oy + (x - ox) * sin(angle) + (y - oy) * cos(angle);
		
		return(new double[]{tx, ty});
	}
}
