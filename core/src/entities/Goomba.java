package entities;

import com.badlogic.gdx.graphics.Texture;

import graphics.Material;
import graphics.ModelMatrix;
import graphics.Point3D;
import graphics.Vector3D;
import graphics.shapes.SphereGraphic;
import graphics.shapes.g3djmodel.MeshModel;
import shaders.Shader;

public class Goomba extends Enemy {
	private boolean yMotion;
	private boolean up;
	private Point3D originalPosition;
	private float height;

	public Goomba(
			Shader shader, MeshModel model, Texture diffuseTexture, Texture specularTexture,
			Material material, Point3D position, boolean yMotion, float height
	) {
		super(shader, model, diffuseTexture, specularTexture, material, position);
		
		this.yMotion = yMotion;
		this.up = true;
		this.originalPosition = position;
		this.height = height;
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
		
		Point3D originPoint = this.origin.getOrigin();
		Vector3D move;
		
		if(yMotion) {
			if(up)
			{
				move = new Vector3D(0.0f, 4.0f * deltaTime, 0.0f);
			}
			else {
				move = new Vector3D(0.0f, -4.0f * deltaTime, 0.0f);				
			}
			
			Point3D movingTo = originPoint.add(move);
			
			
			if(movingTo.y > 12 || movingTo.y < 3.5) {
				up = !up;
			}
		}
		else {	
			if(up)
			{
				move = new Vector3D(0.0f, 0.0f, 4.0f * deltaTime);
			}
			else {
				move = new Vector3D(0.0f, 0.0f, -4.0f * deltaTime);				
			}
			
			Point3D movingTo = originPoint.add(move);
						
			if(Math.abs((movingTo.z - this.originalPosition.z)) > 5 || movingTo.z < 2 || movingTo.z > this.height - 1.0f) {
				up = !up;
			}	
			
		}
		
		this.origin.addTranslation(move.x, move.y, move.z);
	}

}
