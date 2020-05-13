package rendering;

import static functionholders.ListFunctions.*;

public class TileRenderer {
	private Texture tex;
	private TileRenderBatch[] batches;
	
	public TileRenderer(Texture tex) {this.tex = tex;}
	
	public void add(float[] positions, float[] colors, float[] uvMaps) {
		if(batches == null) {batches = append(batches, new TileRenderBatch(tex));}
		
		boolean success = batches[batches.length - 1].add(positions, colors, uvMaps);
		if(!success) {
			batches = append(batches, new TileRenderBatch(tex));
			batches[batches.length - 1].add(positions, colors, uvMaps);
		}
	}
	
	public void load() {for(int i = 0; i < batches.length; i++) {batches[i].load();}}
	
	public void draw(Shader shader, Camera camera) {for(int i = 0; i < batches.length; i++) {batches[i].draw(shader, camera);}}
}
