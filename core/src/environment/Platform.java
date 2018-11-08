package environment;

import graphics.Point3D;
import graphics.Vector3D;
import graphics.shapes.g3djmodel.MeshModel;

public class Platform {
	private Point3D position;
	private Vector3D scale;
	private MeshModel model;

	public Platform(Point3D position, Vector3D scale, MeshModel model) {
		this.position = position;
		this.scale = scale;
		this.model = model;
	}

	public Point3D getPosition() {
		return this.position;
	}
	public Vector3D getScale() {
		return this.scale;
	}
	public MeshModel getModel() {
		return this.model;
	}
}
