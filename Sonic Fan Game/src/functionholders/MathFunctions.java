package functionholders;

import static java.lang.Math.*;

import datatypes.Shape;
import datatypes.Vector;

import static functionholders.ListFunctions.*;
import static functionholders.MathFunctions.limitAngle;

public class MathFunctions {
	public static double getAngleOfVector(Vector vec) {
		Vector axis = vec.normalize();
		
		double axisAngle = acos(axis.x);
		if(axis.y > 0) {axisAngle *= -1;}
		axisAngle = limitAngle(axisAngle);
		
		return(axisAngle);
	}
	
	public static Vector fixAxis(Vector axis0) {
		Vector axis = new Vector(axis0.x, axis0.y);
		
		if(axis.y < 0) {axis = axis.scale(-1);}
		if(axis.x < 0) {axis = axis.scale(-1);}
		
		return(axis);
	}
	
	public static double getProjectedValue(Vector point, Vector axis) {
		Vector project = point.project(axis);
		double output = project.getLength();
		
		if(project.x < 0) {output *= -1;}
		
		return(output);
	}
	
	public static Vector getPerVector(Vector point) {return(new Vector(point.y, point.x));}
	
	public static double limitAngle(double angle) {
		double temp = angle;
		while(temp < 0) {temp += 2 * PI;}
		while(temp >= 2 * PI) {temp -= 2 * PI;}
		return(temp);
	}
	
	public static boolean checkAngleBetweenAngles(double a0, double a1, double a2) {
		double b0 = limitAngle(a0);
		double b1 = limitAngle(a1);
		double b2 = limitAngle(a2);
		
		b0 = limitAngle(b0 - b1);
		b2 = limitAngle(b2 - b1);
		
		/*System.out.println("a0 angle = " + limitAngle(a0));
		System.out.println("a1 angle = " + limitAngle(a1));
		System.out.println("a2 angle = " + limitAngle(a2));
		System.out.println("is a0 between a1 and a2? " + (b0 < b2) + "\n");*/
		
		return(b0 < b2 && b0 != 0);
	}
	
	public static boolean checkAngleBetweenAnglesInclusive(double a0, double a1, double a2) {
		double b0 = limitAngle(a0);
		double b1 = limitAngle(a1);
		double b2 = limitAngle(a2);
		
		b0 = limitAngle(b0 - b1);
		b2 = limitAngle(b2 - b1);
		
		/*System.out.println("a0 angle = " + limitAngle(a0));
		System.out.println("a1 angle = " + limitAngle(a1));
		System.out.println("a2 angle = " + limitAngle(a2));
		System.out.println("is a0 between a1 and a2? " + (b0 < b2) + "\n");*/
		
		return(b0 <= b2);
	}
}
