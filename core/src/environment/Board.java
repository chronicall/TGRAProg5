package environment;

import java.util.ArrayList;

import graphics.Colour;
import graphics.ModelMatrix;
import graphics.Point3D;
import graphics.Vector3D;
import graphics.shapes.g3djmodel.G3DJModelLoader;
import graphics.shapes.g3djmodel.MeshMaterial;
import graphics.shapes.g3djmodel.MeshModel;
import graphics.terrain.Terrain;
import shaders.Shader;

public class Board {
	private static ArrayList<Platform> platforms;
	
	public static void create(Terrain terrain) {
		Board.platforms = new ArrayList<Platform>();
		Board.generate(terrain);
	}

	private static void generate(Terrain terrain) {
		MeshModel crate = G3DJModelLoader.loadG3DJFromFile("crate/crate.g3dj");
		for (MeshMaterial mat : crate.materials) {
			mat.material.emission = new Colour(0,0,0,1);
		}
		Vector3D scale = new Vector3D(2.0f, 2.0f, 2.0f);
		
		// Platform needed to get onto the larger ones
		Point3D position = new Point3D(198.0f, terrain.getTerrainHeight(198, 216) + 1, 216.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		
		// Big stack 1
		position = new Point3D(202.0f, terrain.getTerrainHeight(202, 220) + 1, 220.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(202.0f, terrain.getTerrainHeight(202, 220) + 3, 220.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(202.0f, terrain.getTerrainHeight(202, 220) + 5, 220.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		
		position = new Point3D(202.0f, terrain.getTerrainHeight(202, 222) + 1, 222.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(202.0f, terrain.getTerrainHeight(202, 222) + 3, 222.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(202.0f, terrain.getTerrainHeight(202, 222) + 5, 222.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		
		position = new Point3D(202.0f, terrain.getTerrainHeight(202, 224) + 1, 224.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(202.0f, terrain.getTerrainHeight(202, 224) + 3, 224.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(202.0f, terrain.getTerrainHeight(202, 224) + 5, 224.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(202.0f, terrain.getTerrainHeight(202, 224) + 7, 224.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		
		// Big stack 2
		position = new Point3D(202.0f, terrain.getTerrainHeight(202, 228) + 1, 228.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(202.0f, terrain.getTerrainHeight(202, 228) + 3, 228.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(202.0f, terrain.getTerrainHeight(202, 228) + 5, 228.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(202.0f, terrain.getTerrainHeight(202, 228) + 7, 228.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		
		position = new Point3D(202.0f, terrain.getTerrainHeight(202, 230) + 1, 230.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(202.0f, terrain.getTerrainHeight(202, 230) + 3, 230.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(202.0f, terrain.getTerrainHeight(202, 230) + 5, 230.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(202.0f, terrain.getTerrainHeight(202, 230) + 7, 230.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(202.0f, terrain.getTerrainHeight(202, 230) + 9, 230.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		
		// Big stack 3
		position = new Point3D(208.0f, terrain.getTerrainHeight(208, 234) + 1, 234.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(208.0f, terrain.getTerrainHeight(208, 234) + 3, 234.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(208.0f, terrain.getTerrainHeight(208, 234) + 5, 234.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		
		position = new Point3D(210.0f, terrain.getTerrainHeight(210, 234) + 1, 234.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(210.0f, terrain.getTerrainHeight(210, 234) + 3, 234.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(210.0f, terrain.getTerrainHeight(210, 234) + 5, 234.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		
		position = new Point3D(210.0f, terrain.getTerrainHeight(210, 232) + 1, 232.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(210.0f, terrain.getTerrainHeight(210, 232) + 3, 232.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(210.0f, terrain.getTerrainHeight(210, 232) + 5, 232.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		
		// Big stack 4
		position = new Point3D(218.0f, terrain.getTerrainHeight(218, 230) + 1, 230.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(218.0f, terrain.getTerrainHeight(218, 230) + 3, 230.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(218.0f, terrain.getTerrainHeight(218, 230) + 5, 230.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(218.0f, terrain.getTerrainHeight(218, 230) + 7, 230.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		
		position = new Point3D(222.0f, terrain.getTerrainHeight(222, 230) + 1, 230.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(222.0f, terrain.getTerrainHeight(222, 230) + 3, 230.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(222.0f, terrain.getTerrainHeight(222, 230) + 5, 230.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(222.0f, terrain.getTerrainHeight(222, 230) + 7, 230.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		position = new Point3D(222.0f, terrain.getTerrainHeight(222, 230) + 9, 230.0f);
		Board.platforms.add(new Platform(position, scale, crate));
		
		// TODO: Add something to "collect" on top of some of the boxes
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
