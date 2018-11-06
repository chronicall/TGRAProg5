package environment;

import java.util.HashMap;

import graphics.ModelMatrix;
import graphics.Point3D;
import graphics.Vector3D;
import graphics.shapes.BoxGraphic;
import shaders.Shader;

public class Board {
	private static int width;
	private static int height;
	
	private static HashMap<Point3D, Vector3D> boxMap;
	
	public static void create(int width, int height) {
		Board.width = width;
		Board.height = height;
		Board.boxMap = new HashMap<Point3D, Vector3D>();
	}

	public static void newLevel() {
	}
	
	private static void generate() {
	}
	
	public static void drawBoard(Shader shader) {
		//Base wall - setja texture?
		shader.setMaterialDiffuse(0.5f, 0.5f, 0.5f, 1.0f);
		shader.setMaterialShiniess(10.0f);
		shader.setMaterialDiffuse(1.0f, 1.0f, 1.0f, 1.0f);
		shader.setMaterialShiniess(5.0f);
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(width/2, 0, width/2);
		ModelMatrix.main.addScale(width, 0.5f, height);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube(shader, null, null);
		ModelMatrix.main.popMatrix();
		
		Board.drawSurrounding(shader);
	}
	
	private static void drawSurrounding(Shader shader) { //Todo hafa þetta inni? Kannski draslið til að oppa á osfrv
		Point3D 	boxPoint = new Point3D(5.0f, 1.5f, 2.0f);
		Vector3D 	boxVector = new Vector3D(3.0f, 3.0f, 3.0f);
				
		shader.setMaterialDiffuse(0.2f, 0.2f, 0.2f, 1.0f);
		shader.setMaterialShiniess(5.0f);
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(boxPoint.x, boxPoint.y, boxPoint.z);
		ModelMatrix.main.addScale(boxVector.x, boxVector.y, boxVector.z);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube(shader, null, null);
		ModelMatrix.main.popMatrix();		
		boxMap.put(boxPoint, boxVector);
		
		boxPoint = new Point3D(10.0f, 2.5f, 2.0f);
		boxVector = new Vector3D(3.0f, 5.0f, 3.0f);
		
		shader.setMaterialDiffuse(0.2f, 0.2f, 0.2f, 1.0f);
		shader.setMaterialShiniess(5.0f);
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(boxPoint.x, boxPoint.y, boxPoint.z);
		ModelMatrix.main.addScale(boxVector.x, boxVector.y, boxVector.z);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube(shader, null, null);
		ModelMatrix.main.popMatrix();
		boxMap.put(boxPoint, boxVector);
		
		boxPoint = new Point3D(10.0f, 2f, 15.0f);
		boxVector = new Vector3D(3.0f, 4.0f, 2.0f);
		
		shader.setMaterialDiffuse(0.2f, 0.2f, 0.2f, 1.0f);
		shader.setMaterialShiniess(5.0f);
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(boxPoint.x, boxPoint.y, boxPoint.z);
		ModelMatrix.main.addScale(boxVector.x, boxVector.y, boxVector.z);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube(shader, null, null);
		ModelMatrix.main.popMatrix();
		boxMap.put(boxPoint, boxVector);
		
		
	}
}
