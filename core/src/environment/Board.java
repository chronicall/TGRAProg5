package environment;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import graphics.ModelMatrix;
import graphics.Point3D;
import graphics.Vector3D;
import graphics.shapes.BoxGraphic;
import graphics.shapes.g3djmodel.G3DJModelLoader;
import graphics.shapes.g3djmodel.MeshModel;
import graphics.shapes.g3djmodel.MeshModelNode;
import shaders.Shader;

public class Board {
	private static ArrayList<Platform> platforms;
	
	public static void create() {
		Board.platforms = new ArrayList<Platform>();
		Board.generate();
	}

	private static void generate() {
		MeshModel crate = G3DJModelLoader.loadG3DJFromFile("crate/crate.g3dj");

		Point3D position = new Point3D(202.0f, 1, 201.0f);
		Vector3D scale = new Vector3D(1.0f, 1.0f, 1.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		
		position = new Point3D(202.0f, 3, 201.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		
		position = new Point3D(202.0f, 1, 203.0f);
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
