package rendering;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.stb.STBImage.*;

import java.nio.IntBuffer;
import java.nio.ByteBuffer;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

public class Texture {
	private String filepath;
	private int texID;
	
	public Texture(String filepath) {
		this.filepath = filepath;
		
		texID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texID);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer channels = BufferUtils.createIntBuffer(1);
		ByteBuffer image = stbi_load(filepath, width, height, channels, 0);
		
		if(image != null) {
			if(channels.get(0) == 3) {glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);}
			else if(channels.get(0) == 4) {glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);}
			else {assert(false) : "Error: unknown number of channels '" + channels.get(0) + "' in '" + filepath + "'";}
		}
		else {assert(false) : "Error: Could not load image '" + filepath + "'";}
		
		stbi_image_free(image);
	}
	
	public void bind() {glBindTexture(GL_TEXTURE_2D, texID);}
	public void unbind() {glBindTexture(GL_TEXTURE_2D, 0);}
}
