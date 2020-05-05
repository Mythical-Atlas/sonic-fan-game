package functionholders;

import static java.lang.Math.*;
import static functionholders.MathFunctions.*;
import static functionholders.ListFunctions.*;
import static functionholders.DebugFunctions.*;

import datatypes.Shape;
import datatypes.Vector;
import shapes.Arc;
import shapes.InverseArc;

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
		if(b instanceof Arc || b instanceof InverseArc) {
			if(b.getInverseShadow(b.getAxis(a)[0]) == null) {return(false);}
			
			Vector[] temp = a.getShadow(b.getAxis(a)[0]);
			Vector a1 = temp[0];
			Vector a2 = temp[1];
			temp = b.getInverseShadow(b.getAxis(a)[0]);
			Vector c1 = temp[0];
			Vector c2 = temp[1];
			
			if(!checkSegmentsOverlap(a1, a2, c1, c2, b.getAxis(a)[0])) {return(false);}
		}
		
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
	
	public static Vector getLineLineIntersection(Vector point0, Vector dir0, Vector point1, Vector dir1) {
		Vector A = point0;
		Vector B = point0.add(dir0);
		Vector C = point1;
		Vector D = point1.add(dir1);
		
        // Line AB represented as a1x + b1y = c1 
        double a1 = B.y - A.y; 
        double b1 = B.x - A.x; 
        double c1 = a1*(A.x) + b1*(A.y); 
       
        // Line CD represented as a2x + b2y = c2 
        double a2 = D.y - C.y; 
        double b2 = D.x - C.x; 
        double c2 = a2*(C.x)+ b2*(C.y); 
       
        double determinant = a1*b2 - a2*b1; 
       
        if (determinant == 0) { 
            // The lines are parallel
            return(null); 
        } 
        else
        { 
            double x = (b2*c1 - b1*c2)/determinant; 
            double y = (a1*c2 - a2*c1)/determinant; 
            return new Vector(x, y); 
        } 
    }
}
