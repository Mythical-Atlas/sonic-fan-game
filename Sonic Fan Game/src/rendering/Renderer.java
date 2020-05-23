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
			boolean success = batches[batchIndex].add(image);
			if(!success) {
				if(++batchIndex == batches.length) {
					batches = append(batches, new RenderBatch());
					batches[batchIndex].load();
				}
			}
		}
		
		batches[batchIndex].add(image);
	}
	
	public void add(float[] positions, float[] colors, float[] uv) {
		if(batches == null) {
			batches = append(batches, new RenderBatch());
			batches[batchIndex].load();
		}
		else {
			boolean success = batches[batchIndex].add(positions, colors, uv);
			if(!success) {
				if(++batchIndex == batches.length) {
					batches = append(batches, new RenderBatch());
					batches[batchIndex].load();
				}
			}
		}
		
		batches[batchIndex].add(positions, colors, uv);
	}
	
	public void draw(Shader shader, Camera camera) {if(batches != null) {for(int i = 0; i < batches.length; i++) {batches[i].draw(shader, camera);}}}
}
