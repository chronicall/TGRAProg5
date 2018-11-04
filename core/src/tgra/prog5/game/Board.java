package tgra.prog5.game;

import java.util.HashMap;
import java.util.Map;

public class Board {
	private static Shader3D shader;
	private static int width;
	private static int height;
	
	private static HashMap<Point3D, Vector3D> boxMap;
	
	public static void create(Shader3D shader, int width, int height) {
		Board.shader = shader;
		Board.width = width;
		Board.height = height;
		Board.boxMap = new HashMap<Point3D, Vector3D>();
	}

	public static void newLevel() {
	}
	
	private static void generate() {
	}
	
	public static void drawBoard(float angle) {
		//Base wall - setja texture?
		Board.shader.setMaterialDiffuse(0.5f, 0.5f, 0.5f, 1.0f);
		Board.shader.setMaterialShiniess(10.0f);
		Board.shader.setMaterialDiffuse(1.0f, 1.0f, 1.0f, 1.0f);
		Board.shader.setMaterialShiniess(5.0f);
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(width/2, 2.0f, width/2);
		ModelMatrix.main.addScale(width, 0.5f, height);
		Board.shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawBaseWall();
		ModelMatrix.main.popMatrix();
		
		Board.drawSurrounding();
	}
	
	private static void drawSurrounding() { //Todo hafa þetta inni? Kannski draslið til að oppa á osfrv
		Point3D 	boxPoint = new Point3D(5.0f, 3.0f, 2.0f);
		Vector3D 	boxVector = new Vector3D(3.0f, 3.0f, 3.0f);
				
		Board.shader.setMaterialDiffuse(0.2f, 0.2f, 0.2f, 1.0f);
		Board.shader.setMaterialShiniess(5.0f);
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(boxPoint.x, boxPoint.y, boxPoint.z);
		ModelMatrix.main.addScale(boxVector.x, boxVector.y, boxVector.z);
		Board.shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube();
		ModelMatrix.main.popMatrix();		
		boxMap.put(boxPoint, boxVector);
		
		boxPoint = new Point3D(10.0f, 5.0f, 2.0f);
		boxVector = new Vector3D(3.0f, 5.0f, 3.0f);
		
		Board.shader.setMaterialDiffuse(0.2f, 0.2f, 0.2f, 1.0f);
		Board.shader.setMaterialShiniess(5.0f);
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(boxPoint.x, boxPoint.y, boxPoint.z);
		ModelMatrix.main.addScale(boxVector.x, boxVector.y, boxVector.z);
		Board.shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube();
		ModelMatrix.main.popMatrix();
		boxMap.put(boxPoint, boxVector);
		
		boxPoint = new Point3D(10.0f, 5.0f, 15.0f);
		boxVector = new Vector3D(3.0f, 4.0f, 2.0f);
		
		Board.shader.setMaterialDiffuse(0.2f, 0.2f, 0.2f, 1.0f);
		Board.shader.setMaterialShiniess(5.0f);
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(boxPoint.x, boxPoint.y, boxPoint.z);
		ModelMatrix.main.addScale(boxVector.x, boxVector.y, boxVector.z);
		Board.shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube();
		ModelMatrix.main.popMatrix();
		boxMap.put(boxPoint, boxVector);
		
		
	}
}
