package utils;

import graphics.Point3D;

public class Maths {
	public static boolean isInside(Point3D p1, Point3D p2) {
		return ((p1.x < p2.x + 1 && p1.x > p2.x - 1) &&
				(p1.z < p2.z + 1 && p1.z > p2.z - 1));
	}
}
