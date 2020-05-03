package shapes;

import static java.lang.Math.*;

import static functionholders.CollisionFunctions.*;
import static functionholders.MathFunctions.*;
import static functionholders.ListFunctions.*;

import java.awt.Color;
import java.awt.Graphics2D;

import datatypes.Shape;
import datatypes.Vector;

public class InverseArc extends Shape {
	private double rx;
	private double ry;
	
	public InverseArc() {}
	public InverseArc(Vector a, Vector b, Vector c, Color color) {
		this.color = color;
		
		points = new Vector[]{a, b, c};
		
		rx = a.getDistance(b);
		ry = a.getDistance(b);
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
		
		rx = a.getDistance(b);
		ry = a.getDistance(b);
	}
	public InverseArc(Vector a, Vector b, Vector c, double radius, Color color) {
		this.color = color;
		
		points = new Vector[]{a.subtract(b).normalize().scale(radius).add(b), b, c.subtract(b).normalize().scale(radius).add(b)};
		
		rx = a.getDistance(b);
		ry = a.getDistance(b);
	}
	/*public InverseArc(Vector point0, Vector dir0, Vector point1, Vector dir1, Color color) {
		this.color = color;
		
		Vector norm0 = dir0.normalize().getPerpendicular();
		Vector norm1 = dir1.normalize().getPerpendicular();
		
		Vector center = getLineIntersection(point0, norm0, point1, norm1);
		
		points = new Vector[]{point0, center, point1};
	}*/
	public InverseArc(Vector point0, double angle0, Vector point1, double angle1, Color color) {
		this.color = color;
		
		Vector dir0 = new Vector(cos(angle0), sin(angle0));
		Vector dir1 = new Vector(cos(angle1), sin(angle1));
		
		Vector center = getLineIntersection(point0, dir0, point1, dir1);
		
		rx = abs(point0.subtract(center).x / cos(angle0));
		ry = abs(point1.subtract(center).y / sin(angle1));
		
		points = new Vector[]{point0, center, point1};
	}
	public InverseArc(Vector point0, double angle0, Vector point1, double angle1, Vector point2, Vector point3, Color color) {
		this.color = color;
		
		Vector dir0 = new Vector(cos(angle0), sin(angle0));
		Vector dir1 = new Vector(cos(angle1), sin(angle1));
		
		Vector center = getLineIntersection(point0, dir0, point1, dir1);
		
		rx = abs(point0.subtract(center).x / cos(angle0));
		ry = abs(point1.subtract(center).y / sin(angle1));
		
		Vector bot = point2.subtract(center).normalize();
		double botAngle = acos(bot.x);
		if(bot.y > 0) {botAngle *= -1;}
		botAngle = limitAngle(botAngle);
		
		Vector top = point3.subtract(center).normalize();
		double topAngle = acos(top.x);
		if(top.y > 0) {topAngle *= -1;}
		topAngle = limitAngle(topAngle);
		
		points = new Vector[]{center.add(new Vector(rx * cos(botAngle), ry * -sin(botAngle))), center, center.add(new Vector(rx * cos(topAngle), ry * -sin(topAngle)))};
	}

	public Vector getCenter() {return(points[1]);}
	public double getRadius(double angle) {return(new Vector(cos(angle) * rx, sin(angle) * ry).getLength());}
	
	public Vector getCorner() {
		Vector bot = points[0].subtract(points[1]).normalize();
		Vector top = points[2].subtract(points[1]).normalize();
		
		double botAngle = acos(bot.x);
		double topAngle = acos(top.x);
		
		if(bot.y > 0) {botAngle *= -1;}
		if(top.y > 0) {topAngle *= -1;}
		
		botAngle = limitAngle(botAngle);
		topAngle = limitAngle(topAngle);
		
		if(checkAngleBetweenAnglesInclusive(botAngle, 0, PI / 2)      && checkAngleBetweenAnglesInclusive(topAngle, 0, PI / 2) ||
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
	
	public Vector[] getShadow(Vector axis1) { // VERY different from arc
		Vector axis = new Vector(-axis1.x, -axis1.y);
		
		Vector smallestPoint = null;
		Vector largestPoint = null;
		
		/*Vector bot = points[1].subtract(points[0]).normalize();
		Vector top = points[1].subtract(points[2]).normalize();
		
		double botAngle = atan(bot.y / bot.x);
		double topAngle = atan(top.y / top.x);
		double axisAngle = -atan(axis.y / axis.x);*/
		
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
		
		/*System.out.println("axis   = (" + axis.x + ", " + axis.y + ")");
		System.out.println("axis   = " + axisAngle);
		System.out.println("is axis between angles?   " + checkAngleBetweenAngles(axisAngle, botAngle, topAngle));
		System.out.println("is axis 2 between angles? " + checkAngleBetweenAngles(limitAngle(axisAngle + PI), botAngle, topAngle));
		System.out.println();*/
		
		/*if(botAngle == PI / 2 && checkAngleBetweenAngles(axisAngle, botAngle, topAngle)) {
			System.out.println("axis   = (" + axis.x + ", " + axis.y + ")");
			System.out.println("axis   = " + axisAngle);
			System.out.println("axis 2 = " + limitAngle(axisAngle + PI));
			//System.out.println("1-0 = (" + bot.x + ", " + bot.y + ")");
			//System.out.println("1-2 = (" + top.x + ", " + top.y + ")");
			System.out.println("1-0 = " + botAngle);
			System.out.println("1-2 = " + topAngle);
			System.out.println("is axis between angles?   " + checkAngleBetweenAngles(axisAngle, botAngle, topAngle));
			System.out.println("is axis 2 between angles? " + checkAngleBetweenAngles(limitAngle(axisAngle + PI), botAngle, topAngle));
		}*/
		
		if(checkAngleBetweenAngles(axisAngle, botAngle, topAngle)) {largestPoint = getCenter().add(axis.scale(getRadius(axisAngle)));}
		if(checkAngleBetweenAngles(limitAngle(axisAngle + PI), botAngle, topAngle)) {smallestPoint = getCenter().subtract(axis.scale(getRadius(limitAngle(axisAngle + PI))));}
		
		/*if(largestPoint != null) {System.out.println("largestPoint = (" + (largestPoint.x - getCenter().x) + ", " + (largestPoint.y - getCenter().y) + ")\n");}
		else {System.out.println();}*/
		
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
		
		if(smallestPoint == null) {smallestPoint = tempPoints[smallestIndex];}
		if(largestPoint == null) {largestPoint = tempPoints[largestIndex];}
		
		//if(botAngle == PI / 2 && checkAngleBetweenAngles(axisAngle, botAngle, topAngle)) {System.out.println("smallestPoint = (" + (smallestPoint.x - getCenter().x) + ", " + (smallestPoint.y - getCenter().y) + ")\n");}
		
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
		graphics.drawArc((int)(points[1].x - rx - offset.x), (int)(points[1].y - ry - offset.y), (int)(rx * 2), (int)(ry * 2), (int)toDegrees(botAngle), (int)toDegrees(delAngle));
		graphics.drawPolyline(new int[]{(int)(points[0].x - offset.x), (int)(corner.x - offset.x), (int)(points[2].x - offset.x)}, new int[]{(int)(points[0].y - offset.y), (int)(corner.y - offset.y), (int)(points[2].y - offset.y)}, 3);
	}
}
