package rendering;

import static functionholders.ListFunctions.*;

import datatypes.Shape;
import main.Window;

public class Renderer {
	private RenderBatch[] batches;
	
	private int batchIndex;
	
	public Renderer() {
		batches = null;
		batchIndex = 0;
		
		for(int i = 0; i < 10; i++) {
			batches = append(batches, new RenderBatch());
			batches[batches.length - 1].load();
		}
	}
	
	public void reset() {if(batches != null) {
		for(int i = 0; i < batches.length; i++) {batches[i].reset();}}
		batchIndex = 0;
	}
	
	public void add(Image image) {
		if(batches == null) {
			batches = append(batches, new RenderBatch());
			batches[batchIndex].load();
		}
		else {
			boolean success = false;
			
			for(int i = 0; i <= batchIndex; i++) {
				success = batches[i].add(image);
				if(success) {break;}
			}
			
			if(!success) {
				if(++batchIndex == batches.length) {
					batches = append(batches, new RenderBatch());
					batches[batchIndex].load();
				}
			}
		}
		
		batches[batchIndex].add(image);
	}
	
	public void draw(Shader shader, Camera camera) {
		if(batches != null) {for(int i = 0; i < batches.length; i++) {batches[i].draw(shader, camera);}}
		//reset();
	}
}
