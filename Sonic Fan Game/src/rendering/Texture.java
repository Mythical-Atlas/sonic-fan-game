package rendering;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.stb.STBImage.*;

import java.nio.IntBuffer;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

public class Texture {
	public int width;
	public int height;
	
	private int texID;
	
	public Texture(String filepath) {
		InputStream is = getClass().getResourceAsStream(filepath);
		byte[] bytes = null;
		
		try {bytes = is.readAllBytes();}
		catch (IOException e) {
			e.printStackTrace();
			assert(false) : "Error: Could not load image '" + filepath + "'";
		}
		
		ByteBuffer imageBuffer = BufferUtils.createByteBuffer(bytes.length);
		imageBuffer.put(bytes);
		imageBuffer.flip();
		
		load(imageBuffer);
	}
	public Texture(ByteBuffer imageBuffer) {load(imageBuffer);}
	
	private void load(ByteBuffer imageBuffer) {
		texID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texID);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer channels = BufferUtils.createIntBuffer(1);
		ByteBuffer image = stbi_load_from_memory(imageBuffer, width, height, channels, 0);
		
		this.width = width.get(0);
		this.height = height.get(0);
		
		if(image != null) {
			if(channels.get(0) == 3) {glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);}
			else if(channels.get(0) == 4) {glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);}
			else {assert(false) : "Error: unknown number of channels '" + channels.get(0);}
		}
		else {assert(false) : "Error: Could not load image";}
		
		stbi_image_free(image);
	}
	
	public void bind() {glBindTexture(GL_TEXTURE_2D, texID);}
	public void unbind() {glBindTexture(GL_TEXTURE_2D, 0);}
}
