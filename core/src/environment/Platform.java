package environment;

import graphics.Point3D;
import graphics.Vector3D;

public class Platform {
	private Point3D position;
	private Vector3D scale;

	public Platform(Point3D position, Vector3D scale) {
		this.position = position;
		this.scale = scale;
	}

	public Point3D getPosition() {
		return position;
	}
	public Vector3D getScale() {
		return scale;
	}
}
