package main;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.joml.Vector2f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import rendering.Shader;
import rendering.Texture;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class MenuScene extends Scene {
	private int vaoID;
	private int vboID;
	private int eboID;
	
	private Shader defaultShader;
	
	private Texture testTex;
	
	private float[] vertexArray = {
			 100.5f,   0.5f, 0.0f,		1.0f, 0.0f, 0.0f, 1.0f,		1, 1,
			   0.5f, 100.5f, 0.0f,		0.0f, 1.0f, 0.0f, 1.0f,		0, 0,
			 100.5f, 100.5f, 0.0f,		0.0f, 0.0f, 1.0f, 1.0f,		1, 0,
			   0.5f,   0.5f, 0.0f,		1.0f, 1.0f, 0.0f, 1.0f,		0, 1
	};
	
	private int[] elementArray = {
			2, 1, 0,
			0, 1, 3
	};
	
	public MenuScene() {}
	
	public void init() {
		camera = new Camera(new Vector2f());
		
		defaultShader = new Shader("assets/shaders/default.glsl");
		defaultShader.compile();
		
		testTex = new Texture("assets/sonicsprites/idle0.png");
		
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
		vertexBuffer.put(vertexArray).flip();
		
		vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
		
		IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
		elementBuffer.put(elementArray).flip();
		
		eboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);
		
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
	
	public void update(float dt) {
		camera.position.x -= dt * 50.0f;
		
		defaultShader.use();
		
		defaultShader.uploadTexture("TEX_SAMPLER", 0);
		glActiveTexture(GL_TEXTURE0);
		testTex.bind();
		
		defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
		defaultShader.uploadMat4f("uView", camera.getViewMatrix());
		defaultShader.uploadFloat("uTime", Time.getTime());
		
		glBindVertexArray(vaoID);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		
		glBindVertexArray(0);
		
		defaultShader.detach();
	}
}