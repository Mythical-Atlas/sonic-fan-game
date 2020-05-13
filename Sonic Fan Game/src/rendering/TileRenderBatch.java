package rendering;

import static functionholders.ListFunctions.*;

import static org.lwjgl.opengl.GL33.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class TileRenderBatch {
	private static final int POSITIONS_SIZE = 3;
	private static final int COLORS_SIZE = 4;
	private static final int UV_MAPS_SIZE = 2;
	private static final int VERTEX_ARRAY_SIZE = (POSITIONS_SIZE + COLORS_SIZE + UV_MAPS_SIZE) * Float.BYTES;
	
	public static final int MAX_BATCH_SIZE = 1048576 / Float.BYTES;
	
	public float[] positions;
	public float[] colors;
	public float[] uvMaps;
	
	public float[] vertexArray;
	public int[] elementArray;
	
	public int images;
	
	private int vaoID;
	private int vboID;
	private int eboID;
	
	public Texture tex;
	
	public TileRenderBatch(Texture tex) {
		this.tex = tex;
	
		positions = null;
		colors = null;
		uvMaps = null;
		
		images = 0;
	}
	
	public boolean add(float[] positions0, float[] colors0, float[] uvMaps0) {
		if(images < MAX_BATCH_SIZE) {
			for(int i = 0; i < positions0.length; i++) {positions = append(positions, positions0[i]);}
			for(int i = 0; i < colors0.length; i++) {colors = append(colors, colors0[i]);}
			for(int i = 0; i < uvMaps0.length; i++) {uvMaps = append(uvMaps, uvMaps0[i]);}
			
			images++;
			
			return(true);
		}
		else {return(false);}
	}
	
	public void load() {
		if(images == 0) {return;}
		
		vertexArray = null;
		elementArray = null;
		
		for(int i = 0; i < images * Float.BYTES; i++) {
			for(int p = 0; p < POSITIONS_SIZE; p++) {vertexArray = append(vertexArray, positions[i * POSITIONS_SIZE + p]);}
			for(int c = 0; c < COLORS_SIZE; c++) {vertexArray = append(vertexArray, colors[i * COLORS_SIZE + c]);}
			for(int u = 0; u < UV_MAPS_SIZE; u++) {vertexArray = append(vertexArray, uvMaps[i * UV_MAPS_SIZE + u]);}
			
			elementArray = append(elementArray, i * 4 + 2);
			elementArray = append(elementArray, i * 4 + 1);
			elementArray = append(elementArray, i * 4 + 0);
			elementArray = append(elementArray, i * 4 + 0);
			elementArray = append(elementArray, i * 4 + 1);
			elementArray = append(elementArray, i * 4 + 3);
		}
		
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		
		vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, vertexArray, GL_STATIC_DRAW);
		
		eboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementArray, GL_STATIC_DRAW);
		
		glVertexAttribPointer(0, POSITIONS_SIZE, GL_FLOAT, false, VERTEX_ARRAY_SIZE, 0);
		glEnableVertexAttribArray(0);
		
		glVertexAttribPointer(1, COLORS_SIZE, GL_FLOAT, false, VERTEX_ARRAY_SIZE, POSITIONS_SIZE * Float.BYTES);
		glEnableVertexAttribArray(1);
		
		glVertexAttribPointer(2, UV_MAPS_SIZE, GL_FLOAT, false, VERTEX_ARRAY_SIZE, (POSITIONS_SIZE + COLORS_SIZE) * Float.BYTES);
		glEnableVertexAttribArray(2);
	}
	
	public void draw(Shader shader, Camera camera) {
		if(images == 0) {return;}
		
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
		
		shader.detach();
	}
}
