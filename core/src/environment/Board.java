package environment;

import java.util.ArrayList;

import graphics.Colour;
import graphics.ModelMatrix;
import graphics.Point3D;
import graphics.Vector3D;
import graphics.shapes.g3djmodel.G3DJModelLoader;
import graphics.shapes.g3djmodel.MeshMaterial;
import graphics.shapes.g3djmodel.MeshModel;
import shaders.Shader;

public class Board {
	private static ArrayList<Platform> platforms;
	
	public static void create() {
		Board.platforms = new ArrayList<Platform>();
		Board.generate();
	}

	private static void generate() {
		MeshModel crate = G3DJModelLoader.loadG3DJFromFile("crate/crate.g3dj");
		for (MeshMaterial mat : crate.materials) {
			mat.material.emission = new Colour(0,0,0,1);
		}
		Vector3D scale = new Vector3D(2.0f, 2.0f, 2.0f);
		
		// Platform needed to get onto the larger ones
		Point3D position = new Point3D(198.0f, 1, 216.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		
		// Big stack 1
		position = new Point3D(202.0f, 1, 220.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(202.0f, 3, 220.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		
		position = new Point3D(202.0f, 1, 222.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(202.0f, 3, 222.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		
		position = new Point3D(202.0f, 1, 224.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(202.0f, 3, 224.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(202.0f, 5, 224.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		
		// Big stack 2
		position = new Point3D(202.0f, 1, 228.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(202.0f, 3, 228.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(202.0f, 5, 228.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(202.0f, 7, 228.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		
		position = new Point3D(202.0f, 1, 230.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(202.0f, 3, 230.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(202.0f, 5, 230.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(202.0f, 7, 230.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(202.0f, 9, 230.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		
		// Big stack 3
		position = new Point3D(208.0f, 1, 234.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(208.0f, 3, 234.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(208.0f, 5, 234.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		
		position = new Point3D(210.0f, 1, 234.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(210.0f, 3, 234.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(210.0f, 5, 234.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		
		position = new Point3D(210.0f, 1, 232.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(210.0f, 3, 232.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(210.0f, 5, 232.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		
		// Big stack 4
		position = new Point3D(216.0f, 1, 230.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(216.0f, 3, 230.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(216.0f, 5, 230.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		
		position = new Point3D(218.0f, 1, 230.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(218.0f, 3, 230.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(218.0f, 5, 230.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(218.0f, 7, 230.0f);
		Board.platforms.add(new Platform(position, scale, crate));
	}
	
	public static void draw(Shader shader) {
		for (Platform platform : platforms) {
			Point3D pos = platform.getPosition();
			Vector3D scale = platform.getScale();
			ModelMatrix.main.pushMatrix();
			ModelMatrix.main.addTranslation(pos.x, pos.y, pos.z);
			ModelMatrix.main.addScale(scale.x, scale.y, scale.z);
			shader.setModelMatrix(ModelMatrix.main.getMatrix());
			platform.getModel().draw(shader);
			ModelMatrix.main.popMatrix();
		}
	}

	public static ArrayList<Platform> getPlatforms() {
		return platforms;
	}
}
