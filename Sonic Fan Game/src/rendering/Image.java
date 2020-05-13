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
	
	private float[] vertexArray = {
		 0.0f, 0.0f, 0.0f,		1.0f, 1.0f, 1.0f, 1.0f,		1, 1,
		 0.0f, 0.0f, 0.0f,		1.0f, 1.0f, 1.0f, 1.0f,		0, 0,
		 0.0f, 0.0f, 0.0f,		1.0f, 1.0f, 1.0f, 1.0f,		1, 0,
		 0.0f, 0.0f, 0.0f,		1.0f, 1.0f, 1.0f, 1.0f,		0, 1
	};
	
	private int[] elementArray = {
		2, 1, 0,
		0, 1, 3
	};
	
	public Image(String filepath) {
		tex = new Texture(filepath);
		//load();
	}
	public Image(ByteBuffer imageBuffer) {
		tex = new Texture(imageBuffer);
		//load();
	}
	
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
		
		//load();
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
		//load();
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
	
	private void load() {
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		
		vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, vertexArray, GL_STATIC_DRAW);
		
		eboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementArray, GL_STATIC_DRAW);
		
		int positionsSize = 3;
		int colorSize = 4;
		int uvSize = 2;
		int vertexSizeBytes = (positionsSize + colorSize + uvSize) * Float.BYTES;
		
		glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
		glEnableVertexAttribArray(0);
		
		glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * Float.BYTES);
		glEnableVertexAttribArray(1);
		
		glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize) * Float.BYTES);
		glEnableVertexAttribArray(2);
	}
	
	public void draw(Shader shader, Camera camera) {
		SpriteRenderer.add(this);
		
		/*shader.use();
		
		shader.uploadTexture("TEX_SAMPLER", 0);
		glActiveTexture(GL_TEXTURE0);
		tex.bind();
		
		shader.uploadMat4f("uProjection", camera.getProjectionMatrix());
		shader.uploadMat4f("uView", camera.getViewMatrix());
		
		glBindVertexArray(vaoID);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		
		glBindVertexArray(0);
		
		shader.detach();*/
	}
	
	public void draw(double x, double y, double xScale, double yScale, Shader shader, Camera camera) {
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
		
		SpriteRenderer.add(this);
		
		/*if(
			!checkOnScreen(new float[]{vertexArray[ 0], vertexArray[ 1]}, camera) &&
			!checkOnScreen(new float[]{vertexArray[ 9], vertexArray[10]}, camera) &&
			!checkOnScreen(new float[]{vertexArray[18], vertexArray[19]}, camera) &&
			!checkOnScreen(new float[]{vertexArray[27], vertexArray[28]}, camera)
		) {return;}*/
		
		/*load();
		
		shader.use();
		
		shader.uploadTexture("TEX_SAMPLER", 0);
		glActiveTexture(GL_TEXTURE0);
		tex.bind();
		
		shader.uploadMat4f("uProjection", camera.getProjectionMatrix());
		shader.uploadMat4f("uView", camera.getViewMatrix());
		
		glBindVertexArray(vaoID);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		
		glBindVertexArray(0);
		
		shader.detach();*/
	}
	
	public void draw(double x, double y, double ox, double oy, double angle, double xScale, double yScale, Shader shader, Camera camera) {
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
		
		/*if(
			!checkOnScreen(new float[]{vertexArray[ 0], vertexArray[ 1]}, camera) &&
			!checkOnScreen(new float[]{vertexArray[ 9], vertexArray[10]}, camera) &&
			!checkOnScreen(new float[]{vertexArray[18], vertexArray[19]}, camera) &&
			!checkOnScreen(new float[]{vertexArray[27], vertexArray[28]}, camera)
		) {return;}*/
		
		SpriteRenderer.add(this);
		
		/*load();
		
		shader.use();
		
		shader.uploadTexture("TEX_SAMPLER", 0);
		glActiveTexture(GL_TEXTURE0);
		tex.bind();
		
		shader.uploadMat4f("uProjection", camera.getProjectionMatrix());
		shader.uploadMat4f("uView", camera.getViewMatrix());
		
		glBindVertexArray(vaoID);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		
		glBindVertexArray(0);
		
		shader.detach();*/
	}
	
	public int getWidth() {return(tex.width);}
	public int getHeight() {return(tex.height);}
	
	private double[] rotate(double x, double y, double ox, double oy, double angle) {
		double tx = ox + (x - ox) * cos(angle) - (y - oy) * sin(angle);
		double ty = oy + (x - ox) * sin(angle) + (y - oy) * cos(angle);
		
		return(new double[]{tx, ty});
	}
	
	private boolean checkOnScreen(float[] point, Camera cam) {
		return(
			point[0] >= 0 && point[0] < cam.position.x + Window.getWidth() &&
			point[1] >= 0 && point[1] < cam.position.y + Window.getHeight()
		);
	}
}
