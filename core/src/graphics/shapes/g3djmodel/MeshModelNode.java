package graphics.shapes.g3djmodel;

import java.util.Vector;

import graphics.Point3D;
import graphics.Quaternion;
import graphics.Vector3D;

public class MeshModelNode {
	public String id;
	public Quaternion rotation;
	public Vector3D scale;
	public Point3D translation;
	public Vector<MeshModelNodePart> parts;

	public MeshModelNode()
	{
		rotation = new Quaternion();
		scale = new Vector3D(1.0f, 1.0f, 1.0f);
		translation = new Point3D(0.0f, 0.0f, 0.0f);
		parts = new Vector<MeshModelNodePart>();
	}
}
