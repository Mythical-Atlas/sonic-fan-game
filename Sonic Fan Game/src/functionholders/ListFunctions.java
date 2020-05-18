package functionholders;

import badniks.Badnik;
import datatypes.Shape;
import datatypes.Vector;
import objects.AfterImage;
import objects.Item;
import objects.Ramp;
import objects.Ring;
import objects.Rotor;
import rendering.Image;
//import rendering.ShapeRendererBatch;
import rendering.SpriteRenderBatch;
import rendering.Texture;
import rendering.TileRenderBatch;

public class ListFunctions {
	public static int[][] append(int[][] existingPoints, int[] pointToCheck) {
		if(existingPoints == null) {
			if(pointToCheck != null) {return(new int[][]{pointToCheck});}
			else {return(null);}
		}
		
		if(pointToCheck != null) {
			int[][] newList = new int[existingPoints.length + 1][];
			
			for(int i = 0; i < existingPoints.length; i++) {newList[i] = existingPoints[i];}
			newList[existingPoints.length] = pointToCheck;
			
			return(newList);
		}
		else {return(existingPoints);}
	}
	public static Shape[] append(Shape[] existingPoints, Shape pointToCheck) {
		if(existingPoints == null) {return(new Shape[]{pointToCheck});}
		
		Shape[] newList = new Shape[existingPoints.length + 1];
		
		for(int i = 0; i < existingPoints.length; i++) {newList[i] = existingPoints[i];}
		newList[existingPoints.length] = pointToCheck;
		
		return(newList);
	}
	public static Texture[] append(Texture[] existingPoints, Texture pointToCheck) {
		if(existingPoints == null) {return(new Texture[]{pointToCheck});}
		
		Texture[] newList = new Texture[existingPoints.length + 1];
		
		for(int i = 0; i < existingPoints.length; i++) {newList[i] = existingPoints[i];}
		newList[existingPoints.length] = pointToCheck;
		
		return(newList);
	}
	public static Image[] append(Image[] existingPoints, Image pointToCheck) {
		if(existingPoints == null) {return(new Image[]{pointToCheck});}
		
		Image[] newList = new Image[existingPoints.length + 1];
		
		for(int i = 0; i < existingPoints.length; i++) {newList[i] = existingPoints[i];}
		newList[existingPoints.length] = pointToCheck;
		
		return(newList);
	}
	public static boolean[] append(boolean[] existingPoints, boolean pointToCheck) {
		if(existingPoints == null) {return(new boolean[]{pointToCheck});}
		
		boolean[] newList = new boolean[existingPoints.length + 1];
		
		for(int i = 0; i < existingPoints.length; i++) {newList[i] = existingPoints[i];}
		newList[existingPoints.length] = pointToCheck;
		
		return(newList);
	}
	public static Ramp[] append(Ramp[] existingPoints, Ramp pointToCheck) {
		if(existingPoints == null) {return(new Ramp[]{pointToCheck});}
		
		Ramp[] newList = new Ramp[existingPoints.length + 1];
		
		for(int i = 0; i < existingPoints.length; i++) {newList[i] = existingPoints[i];}
		newList[existingPoints.length] = pointToCheck;
		
		return(newList);
	}
	public static Rotor[] append(Rotor[] existingPoints, Rotor pointToCheck) {
		if(existingPoints == null) {return(new Rotor[]{pointToCheck});}
		
		Rotor[] newList = new Rotor[existingPoints.length + 1];
		
		for(int i = 0; i < existingPoints.length; i++) {newList[i] = existingPoints[i];}
		newList[existingPoints.length] = pointToCheck;
		
		return(newList);
	}
	public static TileRenderBatch[] append(TileRenderBatch[] existingPoints, TileRenderBatch pointToCheck) {
		if(existingPoints == null) {return(new TileRenderBatch[]{pointToCheck});}
		
		TileRenderBatch[] newList = new TileRenderBatch[existingPoints.length + 1];
		
		for(int i = 0; i < existingPoints.length; i++) {newList[i] = existingPoints[i];}
		newList[existingPoints.length] = pointToCheck;
		
		return(newList);
	}
	public static SpriteRenderBatch[] append(SpriteRenderBatch[] existingPoints, SpriteRenderBatch pointToCheck) {
		if(existingPoints == null) {return(new SpriteRenderBatch[]{pointToCheck});}
		
		SpriteRenderBatch[] newList = new SpriteRenderBatch[existingPoints.length + 1];
		
		for(int i = 0; i < existingPoints.length; i++) {newList[i] = existingPoints[i];}
		newList[existingPoints.length] = pointToCheck;
		
		return(newList);
	}
	/*public static ShapeRendererBatch[] append(ShapeRendererBatch[] existingPoints, ShapeRendererBatch pointToCheck) {
		if(existingPoints == null) {return(new ShapeRendererBatch[]{pointToCheck});}
		
		ShapeRendererBatch[] newList = new ShapeRendererBatch[existingPoints.length + 1];
		
		for(int i = 0; i < existingPoints.length; i++) {newList[i] = existingPoints[i];}
		newList[existingPoints.length] = pointToCheck;
		
		return(newList);
	}*/
	public static float[] append(float[] existingPoints, float pointToCheck) {
		if(existingPoints == null) {return(new float[]{pointToCheck});}
		
		float[] newList = new float[existingPoints.length + 1];
		
		for(int i = 0; i < existingPoints.length; i++) {newList[i] = existingPoints[i];}
		newList[existingPoints.length] = pointToCheck;
		
		return(newList);
	}
	public static int[] append(int[] existingPoints, int pointToCheck) {
		if(existingPoints == null) {return(new int[]{pointToCheck});}
		
		int[] newList = new int[existingPoints.length + 1];
		
		for(int i = 0; i < existingPoints.length; i++) {newList[i] = existingPoints[i];}
		newList[existingPoints.length] = pointToCheck;
		
		return(newList);
	}
	public static double[] append(double[] existingPoints, double pointToCheck) {
		if(existingPoints == null) {return(new double[]{pointToCheck});}
		
		double[] newList = new double[existingPoints.length + 1];
		
		for(int i = 0; i < existingPoints.length; i++) {newList[i] = existingPoints[i];}
		newList[existingPoints.length] = pointToCheck;
		
		return(newList);
	}
	public static Badnik[] append(Badnik[] existingPoints, Badnik pointToCheck) {
		if(existingPoints == null) {return(new Badnik[]{pointToCheck});}
		
		Badnik[] newList = new Badnik[existingPoints.length + 1];
		
		for(int i = 0; i < existingPoints.length; i++) {newList[i] = existingPoints[i];}
		newList[existingPoints.length] = pointToCheck;
		
		return(newList);
	}
	public static AfterImage[] append(AfterImage[] existingPoints, AfterImage pointToCheck) {
		if(existingPoints == null) {return(new AfterImage[]{pointToCheck});}
		
		AfterImage[] newList = new AfterImage[existingPoints.length + 1];
		
		for(int i = 0; i < existingPoints.length; i++) {newList[i] = existingPoints[i];}
		newList[existingPoints.length] = pointToCheck;
		
		return(newList);
	}
	public static Item[] append(Item[] existingPoints, Item pointToCheck) {
		if(existingPoints == null) {return(new Item[]{pointToCheck});}
		
		Item[] newList = new Item[existingPoints.length + 1];
		
		for(int i = 0; i < existingPoints.length; i++) {newList[i] = existingPoints[i];}
		newList[existingPoints.length] = pointToCheck;
		
		return(newList);
	}
	public static Vector[] append(Vector[] existingPoints, Vector pointToCheck) {
		if(existingPoints == null) {
			if(pointToCheck != null) {return(new Vector[]{pointToCheck});}
			else {return(null);}
		}
		
		if(pointToCheck != null) {
			Vector[] newList = new Vector[existingPoints.length + 1];
			
			for(int i = 0; i < existingPoints.length; i++) {newList[i] = existingPoints[i];}
			newList[existingPoints.length] = pointToCheck;
			
			return(newList);
		}
		else {return(existingPoints);}
	}
	public static Ring[] append(Ring[] existingPoints, Ring pointToCheck) {
		if(existingPoints == null) {
			if(pointToCheck != null) {return(new Ring[]{pointToCheck});}
			else {return(null);}
		}
		
		if(pointToCheck != null) {
			Ring[] newList = new Ring[existingPoints.length + 1];
			
			for(int i = 0; i < existingPoints.length; i++) {newList[i] = existingPoints[i];}
			newList[existingPoints.length] = pointToCheck;
			
			return(newList);
		}
		else {return(existingPoints);}
	}
	public static Vector[][] append(Vector[][] existingPoints, Vector[] pointToCheck) {
		if(existingPoints == null) {
			if(pointToCheck != null) {return(new Vector[][]{pointToCheck});}
			else {return(null);}
		}
		
		if(pointToCheck != null) {
			Vector[][] newList = new Vector[existingPoints.length + 1][];
			
			for(int i = 0; i < existingPoints.length; i++) {newList[i] = existingPoints[i];}
			newList[existingPoints.length] = pointToCheck;
			
			return(newList);
		}
		else {return(existingPoints);}
	}
	
	public static Vector[] appendWithoutDupes(Vector[] existingPoints, Vector pointToCheck) {
		if(existingPoints == null) {
			if(pointToCheck != null) {return(new Vector[]{pointToCheck});}
			else {return(null);}
		}
		
		if(pointToCheck != null) {
			boolean alreadyThere = false;
			for(int i = 0; i < existingPoints.length; i++) {if(pointToCheck.checkEqual(existingPoints[i])) {alreadyThere = true;}}
			
			if(!alreadyThere) {
				Vector[] newList = new Vector[existingPoints.length + 1];
				
				for(int i = 0; i < existingPoints.length; i++) {newList[i] = existingPoints[i];}
				newList[existingPoints.length] = pointToCheck;
				
				return(newList);
			}
			else {return(existingPoints);}
		}
		else {return(existingPoints);}
	}
	
	public static Vector[] removeDupes(Vector[] a) {
		Vector[] b = null;
		
		for(int i = 0; i < a.length; i++) {b = appendWithoutDupes(b, a[i]);}
		
		return(b);
	}
	
	public static Shape[] combine(Shape[] a, Shape[] b) {
		Shape[] c = null;
		
		if(a != null) {for(int i = 0; i < a.length; i++) {c = append(c, a[i]);}}
		if(b != null) {for(int i = 0; i < b.length; i++) {c = append(c, b[i]);}}
		
		return(c);
	}
	public static Vector[] combine(Vector[] a, Vector[] b) {
		Vector[] c = null;
		
		if(a != null) {for(int i = 0; i < a.length; i++) {c = append(c, a[i]);}}
		if(b != null) {for(int i = 0; i < b.length; i++) {c = append(c, b[i]);}}
		
		return(c);
	}
	public static Vector[] combine(Vector[][] a) {
		Vector[] c = null;
		
		for(int x = 0; x < a.length; x++) {for(int y = 0; y < a[x].length; y++) {c = append(c, a[x][y]);}}
		
		return(c);
	}
	public static Vector[] combine(Vector[] a, Vector[][] b) {
		Vector[] c = null;
		
		for(int i = 0; i < a.length; i++) {c = append(c, a[i]);}
		for(int x = 0; x < b.length; x++) {for(int y = 0; y < b[x].length; y++) {c = append(c, b[x][y]);}}
		
		return(c);
	}
	
	public static Vector[] reduceDimensions(Vector[][] d) {
		Vector[] a = null;
		for(int x = 0; x < d.length; x++) {for(int y = 0; y < d[x].length; y++) {a = append(a, d[x][y]);}}
		
		return(a);
	}
	
	public static Vector[][] getVectorsFromShapes(Shape[] shapes) {
		Vector[][] a = new Vector[shapes.length][];
		for(int i = 0; i < shapes.length; i++) {a[i] = shapes[i].points;}
		
		return(a);
	}
	
	public static Vector[][] removeIndex(Vector[][] list, int index) {
		Vector[][] output = null;
		
		for(int i = 0; i < list.length; i++) {if(i != index) {output = append(output, list[i]);}}
		
		return(output);
	}
	public static Shape[] removeIndex(Shape[] list, int index) {
		Shape[] output = null;
		
		for(int i = 0; i < list.length; i++) {if(i != index) {output = append(output, list[i]);}}
		
		return(output);
	}
	public static Ring[] removeIndex(Ring[] list, int index) {
		Ring[] output = null;
		
		for(int i = 0; i < list.length; i++) {if(i != index) {output = append(output, list[i]);}}
		
		return(output);
	}
	public static Badnik[] removeIndex(Badnik[] list, int index) {
		Badnik[] output = null;
		
		for(int i = 0; i < list.length; i++) {if(i != index) {output = append(output, list[i]);}}
		
		return(output);
	}
	public static Item[] removeIndex(Item[] list, int index) {
		Item[] output = null;
		
		for(int i = 0; i < list.length; i++) {if(i != index) {output = append(output, list[i]);}}
		
		return(output);
	}
	public static AfterImage[] removeIndex(AfterImage[] list, int index) {
		AfterImage[] output = null;
		
		for(int i = 0; i < list.length; i++) {if(i != index) {output = append(output, list[i]);}}
		
		return(output);
	}
	
	public static Shape[] applyMask(Shape[] list, boolean[] masks) {
		Shape[] output = null;
		
		for(int i = 0; i < list.length; i++) {if(masks[i]) {output = append(output, list[i]);}}
		
		return(output);
	}
	
	public static Vector getSmallest(Vector[] list) {
		int smallestIndex = -1;
		double smallest = 0;
		for(int i = 0; i < list.length; i++) {
			double temp = list[i].getLength();
			if(smallestIndex == -1 || temp < smallest) {
				smallestIndex = i;
				smallest = temp;
			}
		}
		
		return(list[smallestIndex]);
	}
	
	public static Vector getLargest(Vector[] list) {
		int largestIndex = -1;
		double largest = 0;
		for(int i = 0; i < list.length; i++) {
			double temp = list[i].getLength();
			if(largestIndex == -1 || temp > largest) {
				largestIndex = i;
				largest = temp;
			}
		}
		
		return(list[largestIndex]);
	}
}
