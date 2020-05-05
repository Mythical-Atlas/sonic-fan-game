package shapes;

import static java.lang.Math.*;

import static functionholders.DebugFunctions.*;
import static functionholders.CollisionFunctions.*;
import static functionholders.MathFunctions.*;
import static functionholders.ListFunctions.*;

import java.awt.Color;
import java.awt.Graphics2D;

import datatypes.Shape;
import datatypes.Vector;

public class InverseArc extends Shape {
	private double radius;
	
	public InverseArc() {}
	public InverseArc(Vector a, Vector b, Vector c, Color color) {
		this.color = color;
		
		points = new Vector[]{a, b, c};
		
		radius = a.getDistance(b);
	}
	public InverseArc(Vector a, Vector b/*, double angle0*/, Color color) {
		this.color = color;
		double angle = PI/2; // TEMP
		
		Vector bot = a.subtract(b).normalize();
		double botAngle = acos(bot.x);
		if(bot.y > 0) {botAngle *= -1;}
		botAngle = limitAngle(botAngle);
		double topAngle = limitAngle(angle + botAngle);
		
		/*System.out.println(botAngle);
		System.out.println(angle);
		System.out.println(topAngle);*/
		
		points = new Vector[]{a, b, b.add(new Vector(cos(topAngle) * a.getDistance(b), -sin(topAngle) * a.getDistance(b)))};
		
		//System.out.println("c = (" + points[2].x + ", " + points[2].y + ")");
		
		radius = a.getDistance(b);
	}
	public InverseArc(Vector a, Vector b, Vector c, double radius, Color color) {
		this.color = color;
		this.radius = radius;
		
		points = new Vector[]{a.subtract(b).normalize().scale(radius).add(b), b, c.subtract(b).normalize().scale(radius).add(b)};
	}
	public InverseArc(Vector center, double radius, Vector point0, Vector point1, Color color) {
		this.color = color;
		this.radius = radius;
		
		points = new Vector[]{point0, center, point1};
	}
	public InverseArc(Vector corner, double angle0, double angle1, double distance, Color color) {
		this.color = color;
		
		Vector point0 = corner.add(new Vector(sin(angle0) * distance, cos(angle0) * distance));
		Vector point1 = corner.add(new Vector(-sin(angle1) * distance, -cos(angle1) * distance));
		Vector center = getLineLineIntersection(point0, new Vector(cos(angle0), sin(angle0)), point1, new Vector(cos(angle1), sin(angle1)));
		
		radius = point0.getDistance(center);
		points = new Vector[]{point0, center, point1};
	}
	public InverseArc(Vector corner, double angle0, double angle1, double distance, double angle2, double angle3, Color color) {
		this(corner, angle0, angle1, distance, color);
		
		points[0] = points[1].add(new Vector(cos(angle2) * radius, -sin(angle2) * radius));
		points[2] = points[1].add(new Vector(cos(angle3) * radius, -sin(angle3) * radius));
	}

	public Vector getCenter() {return(points[1]);}
	public double getRadius() {return(radius);}
	
	public Vector getCorner() {
		Vector bot = points[0].subtract(points[1]).normalize();
		Vector top = points[2].subtract(points[1]).normalize();
		
		double botAngle = acos(bot.x);
		double topAngle = acos(top.x);
		
		if(bot.y > 0) {botAngle *= -1;}
		if(top.y > 0) {topAngle *= -1;}
		
		botAngle = limitAngle(botAngle);
		topAngle = limitAngle(topAngle);
		
		if(checkAngleBetweenAnglesInclusive(botAngle, 0, PI / 2)       && checkAngleBetweenAnglesInclusive(topAngle, 0, PI / 2) ||
		   checkAngleBetweenAnglesInclusive(botAngle, PI, PI / 2 + PI) && checkAngleBetweenAnglesInclusive(topAngle, PI, PI / 2 + PI)) {return(new Vector(points[0].x, points[2].y));}
		else {return(new Vector(points[2].x, points[0].y));}
	}
	
	public Vector[] getAxis(Shape b) {
		Vector[] axis = new Vector[3];
		
		Vector tempCorner = getCorner();
		
		if(!(b instanceof Circle)) {
			int smallestIndex = -1;
			double smallest = 0;
			for(int i = 0; i < b.points.length; i++) {
				double temp = b.points[i].getDistance(tempCorner);
				
				if(smallestIndex == -1 || temp < smallest) {
					smallest = temp;
					smallestIndex = i;
				}
			}
			
			axis[0] = points[1].subtract(b.points[smallestIndex]).normalize();
		}
		else {
			axis[0] = points[1].subtract(b.points[1]).normalize();
		}
		
		/*Vector[] axis = new Vector[2 + 2];
		
		axis[0] = points[1].subtract(b.points[0]).normalize();
		axis[1] = points[1].subtract(b.points[2]).normalize();*/
		
		axis[1] = points[0].subtract(points[1]).normalize();
		axis[2] = points[2].subtract(points[1]).normalize();
		
		double tempX2 = axis[1].x;
		double tempY2 = axis[1].y;
		double tempX1 = axis[2].x;
		double tempY1 = axis[2].y;
		
		axis[1].x = tempY2;
		axis[1].y = -tempX2;
		axis[2].x = tempY1;
		axis[2].y = -tempX1;
		
		for(int i = 0; i < axis.length ; i++) {axis[i] = fixAxis(axis[i]);}
		
		return(removeDupes(axis));
	}
	
	public Vector[] getInverseShadow(Vector axis) {
		Vector smallestPoint = null;
		Vector largestPoint = null;
		
		Vector bot = points[0].subtract(points[1]).normalize();
		Vector top = points[2].subtract(points[1]).normalize();
		
		double botAngle = acos(bot.x);
		double topAngle = acos(top.x);
		double axisAngle = acos(axis.x);
		
		if(bot.y > 0) {botAngle *= -1;}
		if(top.y > 0) {topAngle *= -1;}
		if(axis.y > 0) {axisAngle *= -1;}
		
		botAngle = limitAngle(botAngle);
		topAngle = limitAngle(topAngle);
		axisAngle = limitAngle(axisAngle);
		
		if(checkAngleBetweenAngles(axisAngle, botAngle, topAngle)) {largestPoint = getCenter().add(axis.scale(getRadius()));}
		if(checkAngleBetweenAngles(limitAngle(axisAngle + PI), botAngle, topAngle)) {smallestPoint = getCenter().subtract(axis.scale(getRadius()));}
		
		int smallestIndex = -1;
		int largestIndex = -1;
		double smallest = 0;
		double largest = 0;
		for(int i = 0; i < points.length; i++) {
			double temp = getProjectedValue(points[i], axis);
			
			if(smallestIndex == -1 || temp < smallest) {
				smallest = temp;
				smallestIndex = i;
			}
			if(largestIndex == -1 || temp > largest) {
				largest = temp;
				largestIndex = i;
			}
		}
		
		if(smallestPoint == null && largestPoint == null) {return(null);}
		if(smallestPoint == null) {smallestPoint = points[smallestIndex];}
		if(largestPoint == null) {largestPoint = points[largestIndex];}
		
		return(new Vector[]{smallestPoint, largestPoint});
	}
	
	public Vector[] getShadow(Vector axis1) { // VERY different from arc
		Vector axis = new Vector(-axis1.x, -axis1.y);
		
		Vector smallestPoint = null;
		Vector largestPoint = null;
		
		Vector bot = points[0].subtract(points[1]).normalize();
		Vector top = points[2].subtract(points[1]).normalize();
		
		double botAngle = acos(bot.x);
		double topAngle = acos(top.x);
		double axisAngle = acos(axis.x);
		
		if(bot.y > 0) {botAngle *= -1;}
		if(top.y > 0) {topAngle *= -1;}
		if(axis.y > 0) {axisAngle *= -1;}
		
		botAngle = limitAngle(botAngle);
		topAngle = limitAngle(topAngle);
		axisAngle = limitAngle(axisAngle);
		
		if(checkAngleBetweenAngles(axisAngle, botAngle, topAngle)) {largestPoint = getCenter().add(axis.scale(getRadius()));}
		if(checkAngleBetweenAngles(limitAngle(axisAngle + PI), botAngle, topAngle)) {smallestPoint = getCenter().subtract(axis.scale(getRadius()));}
		
		Vector[] tempPoints = null;
		
		for(int i = 0; i < points.length; i++) {tempPoints = append(tempPoints, points[i]);}
		
		tempPoints[0] = new Vector(points[2].x, points[2].y);
		tempPoints[1] = getCorner();
		tempPoints[2] = new Vector(points[0].x, points[0].y);
		
		int smallestIndex = -1;
		int largestIndex = -1;
		double smallest = 0;
		double largest = 0;
		for(int i = 0; i < tempPoints.length; i++) {
			double temp = getProjectedValue(tempPoints[i], axis);
			
			if(smallestIndex == -1 || temp < smallest) {
				smallest = temp;
				smallestIndex = i;
			}
			if(largestIndex == -1 || temp > largest) {
				largest = temp;
				largestIndex = i;
			}
		}
		
		//if(smallestPoint != null && largestPoint != null) {return(null);}
		if(smallestPoint == null) {smallestPoint = tempPoints[smallestIndex];}
		if(largestPoint == null) {largestPoint = tempPoints[largestIndex];}
		
		return(new Vector[]{smallestPoint, largestPoint});
	}
	
	public void draw(Graphics2D graphics, Vector offset) {
		Vector bot = points[0].subtract(points[1]).normalize();
		Vector top = points[2].subtract(points[1]).normalize();
		
		double botAngle = acos(bot.x);
		double topAngle = acos(top.x);
		
		if(bot.y > 0) {botAngle *= -1;}
		if(top.y > 0) {topAngle *= -1;}
		
		botAngle = limitAngle(botAngle);
		topAngle = limitAngle(topAngle);
		
		double delAngle = limitAngle(topAngle - botAngle);
		Vector corner = getCorner();
		
		graphics.setColor(color);
		graphics.drawArc((int)(points[1].x - radius - offset.x), (int)(points[1].y - radius - offset.y), (int)(radius * 2), (int)(radius * 2), (int)toDegrees(botAngle), (int)toDegrees(delAngle));
		//graphics.drawPolyline(new int[]{(int)(points[0].x - offset.x), (int)(corner.x - offset.x), (int)(points[2].x - offset.x)}, new int[]{(int)(points[0].y - offset.y), (int)(corner.y - offset.y), (int)(points[2].y - offset.y)}, 3);
	}
}
