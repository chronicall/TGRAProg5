package utils;

import graphics.Point3D;

public class Maths {
	public static float baryCentric(Point3D p1, Point3D p2, Point3D p3, Point3D pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.z - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.z - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}
	
	public static boolean isInside(Point3D p1, Point3D p2) {
		return ((p1.x < p2.x + 1 && p1.x > p2.x - 1) &&
				(p1.z < p2.z + 1 && p1.z > p2.z - 1));
	}
}
