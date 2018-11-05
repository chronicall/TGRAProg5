package graphics.shapes.g3djmodel;

import java.nio.FloatBuffer;
import java.util.Vector;

public class Mesh {
	public Vector<String> attributes;
	public FloatBuffer vertices;
	public FloatBuffer normals;
	public FloatBuffer uv;

	public Mesh()
	{
		this.vertices = null;
		this.normals = null;
		this.uv = null;
	}
}
