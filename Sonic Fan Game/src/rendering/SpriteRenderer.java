package rendering;

import static functionholders.ListFunctions.*;

import main.Window;

public class SpriteRenderer {
	private static SpriteRenderer singleton = null;
	
	private SpriteRenderBatch[] batches;
	
	private SpriteRenderer() {batches = null;}
	
	public static SpriteRenderer get() {
		if(singleton == null) {singleton = new SpriteRenderer();}
		return(singleton);
	}
	
	public static void reset() {get().batches = null;}
	
	public static void add(Image image) {
		if(get().batches == null) {
			get().batches = append(get().batches, new SpriteRenderBatch());
			get().batches[get().batches.length - 1].load();
		}
		
		boolean success = get().batches[get().batches.length - 1].add(image);
		if(!success) {
			get().batches = append(get().batches, new SpriteRenderBatch());
			get().batches[get().batches.length - 1].load();
			get().batches[get().batches.length - 1].add(image);
		}
	}
	
	public static void draw(Shader shader, Camera camera) {if(get().batches != null) {for(int i = 0; i < get().batches.length; i++) {get().batches[i].draw(shader, camera);}}}
}
