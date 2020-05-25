package rendering;

import static functionholders.ListFunctions.*;

import datatypes.Shape;
import main.Window;

public class SpriteRenderer {
	private static SpriteRenderer singleton = null;
	
	private SpriteRenderBatch[] batches;
	
	private int batchIndex;
	
	private SpriteRenderer() {
		batches = null;
		batchIndex = 0;
		
		for(int i = 0; i < 10; i++) {
			batches = append(batches, new SpriteRenderBatch());
			batches[batches.length - 1].load();
		}
	}
	
	public static SpriteRenderer get() {
		if(singleton == null) {singleton = new SpriteRenderer();}
		return(singleton);
	}
	
	public static void reset() {if(get().batches != null) {
		for(int i = 0; i < get().batches.length; i++) {get().batches[i].reset();}}
		get().batchIndex = 0;
	}
	
	public static void add(Image image) {
		if(get().batches == null) {
			get().batches = append(get().batches, new SpriteRenderBatch());
			get().batches[get().batchIndex].load();
		}
		else {
			boolean success = false;
			
			for(int i = 0; i <= get().batchIndex; i++) {
				success = get().batches[i].add(image);
				if(success) {break;}
			}
			
			if(!success) {
				if(++get().batchIndex == get().batches.length) {
					get().batches = append(get().batches, new SpriteRenderBatch());
					get().batches[get().batchIndex].load();
				}
			}
		}
		
		get().batches[get().batchIndex].add(image);
	}
	
	public static void draw(Shader shader, Camera camera) {if(get().batches != null) {for(int i = 0; i < get().batches.length; i++) {get().batches[i].draw(shader, camera);}}}
}
