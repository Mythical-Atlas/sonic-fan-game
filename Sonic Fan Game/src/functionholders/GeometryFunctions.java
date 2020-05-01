package functionholders;

import static java.lang.Math.*;

import datatypes.Shape;
import datatypes.Vector;
import shapes.Rectangle;

public class GeometryFunctions {
	public static Shape rotateRectangle(Shape rect, Vector axis) {
		Vector pos0 = rect.points[0].getNew();
		Vector pos1 = rect.points[1].getNew();
		Vector pos2 = rect.points[2].getNew();
		Vector pos3 = rect.points[3].getNew();
		
		Vector tempGroundAxis = axis.getNew().normalize();
		tempGroundAxis.rotateAroundPointTo(new Vector(0, 0), -PI / 2);
		
		pos0.rotateAroundPointTo(rect.getCenter(), tempGroundAxis);
		pos1.rotateAroundPointTo(rect.getCenter(), tempGroundAxis);
		pos2.rotateAroundPointTo(rect.getCenter(), tempGroundAxis);
		pos3.rotateAroundPointTo(rect.getCenter(), tempGroundAxis);
		
		Shape output = new Shape(new Vector[]{pos0, pos1, pos2, pos3});
		
		return(output);
	}
}
