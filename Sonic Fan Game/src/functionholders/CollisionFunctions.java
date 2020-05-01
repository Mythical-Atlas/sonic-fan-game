package functionholders;

import static java.lang.Math.*;
import static functionholders.MathFunctions.*;
import static functionholders.ListFunctions.*;

import datatypes.Shape;
import datatypes.Vector;
import shapes.Arc;

public class CollisionFunctions {
	public static Vector[] getAxisMovements(Shape a, Shape b, Vector axis) {
		Vector[] temp = a.getShadow(axis);
		
		Vector a1 = temp[0];
		Vector a2 = temp[1];
		temp = b.getShadow(axis);
		Vector b1 = temp[0];
		Vector b2 = temp[1];
		
		double a0 = getProjectedValue(a1, axis);
		double b0 = getProjectedValue(a2, axis);
		double c0 = getProjectedValue(b1, axis);
		double d0 = getProjectedValue(b2, axis);
		
		double dir0 = c0 - b0;
		double dir1 = d0 - a0;
		
		Vector temp0 = new Vector(axis.x * dir0, axis.y * dir0);
		Vector temp1 = new Vector(axis.x * dir1, axis.y * dir1);
		
		//Vector temp0 = b1.subtract(a2);
		//Vector temp1 = b2.subtract(a1);
		
		return(new Vector[]{temp0, temp1});
	}
	
	public static Vector clip(Shape a, Shape b) {
		if(!checkCollision(a, b)) {return(new Vector(0, 0));}
		
		Vector[] axis = combine(a.getAxis(b), b.getAxis(a));
		Vector[] movements = null;
		
		for(int i = 0; i < axis.length; i++) {
			Vector[] temp = getAxisMovements(a, b, axis[i]);
			
			movements = appendWithoutDupes(movements, temp[0]);
			movements = appendWithoutDupes(movements, temp[1]);
		}
		
		return(getSmallest(movements));
	}
	
	public static Vector clip(Shape a, Shape[] c) {
		Vector[] dirs = null;
		
		for(int x = 0; x < c.length; x++) {
			Shape b = c[x];
					
			if(checkCollision(a, b)) {
				Vector[] axis = combine(a.getAxis(b), b.getAxis(a));
				Vector[] movements = null;
				
				for(int i = 0; i < axis.length; i++) {
					Vector[] temp = getAxisMovements(a, b, axis[i]);
					
					movements = appendWithoutDupes(movements, temp[0]);
					movements = appendWithoutDupes(movements, temp[1]);
				}
				
				dirs = appendWithoutDupes(dirs, getSmallest(movements));
			}
		}
		
		if(dirs == null) {return(new Vector(0, 0));}
		
		return(getLargest(dirs));
	}
	
	public static boolean checkSegmentsOverlap(Vector a1, Vector a2, Vector b1, Vector b2, Vector axis) {
		double a = getProjectedValue(a1, axis);
		double b = getProjectedValue(a2, axis);
		double c = getProjectedValue(b1, axis);
		double d = getProjectedValue(b2, axis);
		
		if(d <= a || b <= c) {return(false);}
		else {return(true);}
	}
	
	public static boolean checkCollision(Shape a, Shape b) {
		Vector[] axis = removeDupes(combine(a.getAxis(b), b.getAxis(a)));
		
		for(int i = 0; i < axis.length; i++) {
			Vector[] temp = a.getShadow(axis[i]);
			Vector a1 = temp[0];
			Vector a2 = temp[1];
			temp = b.getShadow(axis[i]);
			Vector b1 = temp[0];
			Vector b2 = temp[1];
			
			if(!checkSegmentsOverlap(a1, a2, b1, b2, axis[i])) {return(false);}
		}
		
		return(true);
	}
}
