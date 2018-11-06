package entities;

import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.graphics.Texture;

import graphics.Material;
import graphics.ModelMatrix;
import graphics.Point3D;
import graphics.Vector3D;
import graphics.shapes.SphereGraphic;
import graphics.shapes.g3djmodel.MeshModel;
import shaders.Shader;

public class ChainChomp extends Enemy{
	private Point3D chainPosition;
	private float timeUntilMove;
	private float height;
	private float width;
	private int chainLength;

	public ChainChomp(
			Shader shader, MeshModel model, Texture diffuseTexture, Texture specularTexture,
			Material material, Point3D position, float height, float width
	) {
		super(shader, model, diffuseTexture, specularTexture, material, position);
		
		this.chainPosition = position;
		this.timeUntilMove = 2.0f;
		this.height = height;
		this.width = width;
		this.chainLength = 10;
	}

	public void display() {
		super.display();
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTransformation(this.origin.matrix);
		this.shader.setModelMatrix(ModelMatrix.main.getMatrix());
		if (this.model == null) {
			//BoxGraphic.drawSolidCube();
			SphereGraphic.drawSolidSphere(this.shader, this.diffuseTexture, this.specularTexture);
		} else {
			this.model.draw(this.shader);
		}
		ModelMatrix.main.popMatrix();
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		//System.out.println("time until move " + this.timeUntilMove + " delta " + deltaTime);
		
		if(this.timeUntilMove > 0) {
			this.timeUntilMove -= deltaTime;
			return;
		}
		
		this.timeUntilMove = 2.0f;
		Point3D originPoint = this.origin.getOrigin();
		Vector3D vecUpZ = new Vector3D(0.0f, 0.0f, 3.0f);
		Vector3D vecDownZ = new Vector3D(0.0f, 0.0f, -3.0f);
		Vector3D vecUpX = new Vector3D(3.0f, 0.0f, 0.0f);
		Vector3D vecDownX = new Vector3D(-3.0f, 0.0f, 0.0f);
		Vector3D vecUpY = new Vector3D(0.0f, 3.0f, 0.0f);
		Vector3D vecDownY = new Vector3D(0.0f, -3.0f, 0.0f);		
		
		System.out.println("move");
		
		Point3D movingTo = null;
		boolean moveFound = false;
		Vector3D move = null;
		
		//while(!moveFound) {
			int randomNum = ThreadLocalRandom.current().nextInt(1, 7); //number between 1 and 6
		
			if(randomNum == 1) {
				movingTo = originPoint.add(vecUpZ);
				move = vecUpZ;
			}
			else if(randomNum == 2) {
				movingTo = originPoint.add(vecDownZ);
				move = vecDownZ;
			}
			else if(randomNum == 3) {
				movingTo = originPoint.add(vecUpX);
				move = vecUpX;
			}
			else if(randomNum == 4) {
				movingTo = originPoint.add(vecDownX);
				move = vecDownX;
			}
			else if(randomNum == 5) {
				movingTo = originPoint.add(vecUpY);
				move = vecUpY;
			}
			else if(randomNum == 6) {
				movingTo = originPoint.add(vecDownY);
				move = vecDownY;
			}
				
			if(movingTo.y > this.chainLength || movingTo.y < 3.5 || Math.abs((movingTo.y - this.chainPosition.y)) > this.chainLength ||
				Math.abs((movingTo.z - this.chainPosition.z)) > this.chainLength || movingTo.z < 2 || movingTo.z > this.height - 1.0f ||
				Math.abs((movingTo.x - this.chainPosition.x)) > this.chainLength || movingTo.x < 2 || movingTo.x > this.width - 1.0f) {
				move = new Vector3D(0.0f, 0.0f, 0.0f);		
				System.out.println("move NOT found");
			}
			else {
				moveFound = true;
				System.out.println("move found");
			}
		//}
		
		this.origin.addTranslation(move.x, move.y, move.z);		
	}
}
