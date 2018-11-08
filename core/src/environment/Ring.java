package environment;

import graphics.shapes.g3djmodel.MeshModel;
import utils.Point3D;
import utils.Vector3D;

public class Ring {
	private Point3D position;
	private MeshModel model;
	private Vector3D rotation;

	public Ring(Point3D position, Vector3D rotation, MeshModel model) {
		this.position = position;
		this.rotation = rotation;
		this.model = model;
	}

	public Point3D getPosition() {
		return this.position;
	}
	public Vector3D getRotation() {
		return this.rotation;
	}
	public MeshModel getModel() {
		return this.model;
	}
	
	public void setRotation(Vector3D rotation) {
		this.rotation = rotation;
	}
}
