package main;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
//import static org.lwjgl.opengl.GL11.*;
//import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class MenuScene extends Scene {
	private String vertexShaderSrc = "#version 330 core\r\n" + 
			"\r\n" + 
			"layout (location=0) in vec3 aPos;\r\n" + 
			"layout (location=1) in vec4 aColor;\r\n" + 
			"\r\n" + 
			"out vec4 fColor;\r\n" + 
			"\r\n" + 
			"void main() {\r\n" + 
			"	fColor = aColor;\r\n" + 
			"	gl_Position = vec4(aPos, 1.0);\r\n" + 
			"}";
	private String fragmentShaderSrc = "#version 330 core\r\n" + 
			"\r\n" + 
			"in vec4 fColor;\r\n" + 
			"out vec4 color;\r\n" + 
			"\r\n" + 
			"void main() {color = fColor;}";
	
	private int vertexID;
	private int fragmentID;
	private int shaderProgram;
	
	private int vaoID;
	private int vboID;
	private int eboID;
	
	private float[] vertexArray = {
			 0.5f, -0.5f, 0.0f,		1.0f, 0.0f, 0.0f, 1.0f,
			-0.5f,  0.5f, 0.0f,		0.0f, 1.0f, 0.0f, 1.0f,
			 0.5f,  0.5f, 0.0f,		0.0f, 0.0f, 1.0f, 1.0f,
			-0.5f, -0.5f, 0.0f,		1.0f, 1.0f, 0.0f, 1.0f,
	};
	
	private int[] elementArray = {
			2, 1, 0,
			0, 1, 3
	};
	
	public MenuScene() {
		
	}
	
	public void init() {
		vertexID = glCreateShader(GL_VERTEX_SHADER);
		
		glShaderSource(vertexID, vertexShaderSrc);
		glCompileShader(vertexID);
		
		int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
		if(success == GL_FALSE) {
			int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
			System.out.println("ERROR: Vertex shader compilation failed.");
			System.out.println(glGetShaderInfoLog(vertexID, len));
			assert(false) : "";
		}
		
		fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
		
		glShaderSource(fragmentID, fragmentShaderSrc);
		glCompileShader(fragmentID);
		
		success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
		if(success == GL_FALSE) {
			int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
			System.out.println("ERROR: Fragment shader compilation failed.");
			System.out.println(glGetShaderInfoLog(fragmentID, len));
			assert(false) : "";
		}
		
		shaderProgram = glCreateProgram();
		glAttachShader(shaderProgram, vertexID);
		glAttachShader(shaderProgram, fragmentID);
		glLinkProgram(shaderProgram);
		
		success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
		if(success == GL_FALSE) {
			int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
			System.out.println("ERROR: Shader linking failed.");
			System.out.println(glGetProgramInfoLog(shaderProgram, len));
			assert(false) : "";
		}
		
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
		int floatSizeBytes = 4;
		int vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes;
		
		glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
		glEnableVertexAttribArray(0);
		
		glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * floatSizeBytes);
		glEnableVertexAttribArray(1);
	}
	
	public void update(float dt) {
		glUseProgram(shaderProgram);
		glBindVertexArray(vaoID);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		
		glBindVertexArray(0);
		glUseProgram(0);
	}
}