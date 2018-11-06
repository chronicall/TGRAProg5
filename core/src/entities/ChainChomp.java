package entities;

import java.util.Random;

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
	private int chainLength;
	private Vector3D direction;
	private Random random;

	public ChainChomp(
			Shader shader, MeshModel model, Texture diffuseTexture, Texture specularTexture,
			Material material, Point3D position
	) {
		super(shader, model, diffuseTexture, specularTexture, material, position);
		
		this.chainPosition = position;
		this.chainLength = 10;
		this.direction = new Vector3D(3, 0, 3);
		this.random = new Random();
	}

	public void display() {
		super.display();
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTransformation(this.origin.matrix);
		this.shader.setModelMatrix(ModelMatrix.main.getMatrix());
		if (this.model == null) {
			SphereGraphic.drawSolidSphere(this.shader, this.diffuseTexture, this.specularTexture);
		} else {
			this.model.draw(this.shader);
		}
		ModelMatrix.main.popMatrix();
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		if (this.position.equals(this.chainPosition) || (float) (
				Math.pow(this.position.x - this.chainPosition.x, 2) + Math.pow(this.position.z - this.chainPosition.z, 2)
			) > this.chainLength
		) {
			this.direction = this.direction.scale(-1);
			this.direction = this.direction.add(new Vector3D(this.random.nextFloat() * (-1 - 1) + 1, 0, this.random.nextFloat() * (-1 - 1) + 1));
		}

		this.origin.addTranslation(this.direction.x * deltaTime, this.direction.y, this.direction.z * deltaTime);
	}
}
