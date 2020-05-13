package rendering;

import static functionholders.ListFunctions.*;

import static org.lwjgl.opengl.GL33.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class SpriteRenderBatch { 
	private static final int POSITIONS_SIZE = 3;
	private static final int COLORS_SIZE = 4;
	private static final int UV_MAPS_SIZE = 2;
	private static final int TEX_ID_SIZE = 1;
	private static final int VERTEX_SIZE = (POSITIONS_SIZE + COLORS_SIZE + UV_MAPS_SIZE + TEX_ID_SIZE);
	
	private static final int MAX_ARRAY_SIZE = 1048576 / Float.BYTES;
	private static final int MAX_TEXTURES = 32;
	
	private Texture[] textures;
	private float[] vertices;
	private int[] elements;
	private int[] texSlots;
	
	private int vaoID;
	private int vboID;
	private int eboID;
	
	private int textureIndex;
	private int spriteIndex;
	private int index;
	
	public SpriteRenderBatch() {
		textures = null;
		vertices = new float[MAX_ARRAY_SIZE];
		elements = new int[vertices.length / 4 * 6];
		
		for(int i = 0; i < elements.length; i += 6) {
			elements[i + 0] = i * 4 + 2;
			elements[i + 1] = i * 4 + 1;
			elements[i + 2] = i * 4 + 0;
			elements[i + 3] = i * 4 + 0;
			elements[i + 4] = i * 4 + 1;
			elements[i + 5] = i * 4 + 3;
		}
		for(int i = 0; i < MAX_TEXTURES; i++) {texSlots = append(texSlots, i);}
		
		textureIndex = 0;
		spriteIndex = 0;
		index = 0;
	}
	
	public boolean add(Image image) {
		if(spriteIndex < MAX_ARRAY_SIZE / VERTEX_SIZE && textureIndex < MAX_TEXTURES - 1) {
			spriteIndex++;
			
			float texID = 0;
			boolean texPresent = false;
			for(int i = 0; i < textureIndex; i++) {
				if(image.tex.equals(textures[i])) {
					texID = i + 1;
					texPresent = true;
					break;
				}
			}
			if(!texPresent) {
				texID = ++textureIndex;
				textures = append(textures, image.tex);
			}
			
			for(int i = 0; i < 6; i++) { // WHY?
				for(int v = 0; v < 4; v++) {
					loadVertices(image.getPositions2(), v * POSITIONS_SIZE, POSITIONS_SIZE);
					loadVertices(image.getColors(), v * COLORS_SIZE, COLORS_SIZE);
					loadVertices(image.getUVMaps(), v * UV_MAPS_SIZE, UV_MAPS_SIZE);
					vertices[index++] = texID;
				}
			}
			return(true);
		}
		else {return(false);}
	}
	
	private void loadVertices(float[] items, int start, int length) {for(int i = start; i < start + length; i++) {vertices[index++] = items[i];}}
	
	public void load() {
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		
		vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, MAX_ARRAY_SIZE * Float.BYTES, GL_DYNAMIC_DRAW);
		
		eboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, elements, GL_STATIC_DRAW);
		
		glVertexAttribPointer(0, POSITIONS_SIZE, GL_FLOAT, false, VERTEX_SIZE * Float.BYTES, 0);
		glEnableVertexAttribArray(0);
		
		glVertexAttribPointer(1, COLORS_SIZE, GL_FLOAT, false, VERTEX_SIZE * Float.BYTES, POSITIONS_SIZE * Float.BYTES);
		glEnableVertexAttribArray(1);
		
		glVertexAttribPointer(2, UV_MAPS_SIZE, GL_FLOAT, false, VERTEX_SIZE * Float.BYTES, (POSITIONS_SIZE + COLORS_SIZE) * Float.BYTES);
		glEnableVertexAttribArray(2);
		
		glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE * Float.BYTES, (POSITIONS_SIZE + COLORS_SIZE + UV_MAPS_SIZE) * Float.BYTES);
		glEnableVertexAttribArray(3);
	}
	
	public void draw(Shader shader, Camera camera) {
		if(spriteIndex == 0) {return;}
		
		System.out.println("num sprites = " + spriteIndex);
		
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
		
		shader.use();
		shader.uploadMat4f("uProjection", camera.getProjectionMatrix());
		shader.uploadMat4f("uView", camera.getViewMatrix());
		
		for(int i = 0; i < textures.length; i++) {
            glActiveTexture(GL_TEXTURE0 + i + 1);
            textures[i].bind();
        }
		
		shader.uploadIntArray("uTextures", texSlots);
		
		glBindVertexArray(vaoID);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glDrawElements(GL_TRIANGLES, spriteIndex * 6, GL_UNSIGNED_INT, 0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
		
		for(int i = 0; i < textures.length; i++) {textures[i].unbind();}
		
		shader.detach();
	}
}
