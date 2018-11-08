package entities;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;

import graphics.Material;
import graphics.ModelMatrix;
import graphics.Point3D;
import graphics.Vector3D;
import graphics.shapes.SphereGraphic;
import graphics.shapes.g3djmodel.MeshModel;
import graphics.terrain.Terrain;
import shaders.Shader;

public class ChainChomp extends Enemy{
	private Vector3D direction;
	private Random random;
	private float DIRECTION_CHANGE_TIMER = 5.0f;
	private static final float GRAVITY = -30;

	public ChainChomp(
			Shader shader, MeshModel model, Texture diffuseTexture, Texture specularTexture,
			Material material, Point3D position
	) {
		super(shader, model, diffuseTexture, specularTexture, material, position);
		this.random = new Random();
		this.direction = new Vector3D((random.nextFloat() * (-4 - 4) + 4), 0.0f, (random.nextFloat() * (-4 - 4) + 4));
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
	
	public void update(float deltaTime, Terrain terrain) {
		super.update(deltaTime);
		
		this.DIRECTION_CHANGE_TIMER -= 5.0f * deltaTime;
		if (this.DIRECTION_CHANGE_TIMER <= 0) {
			this.DIRECTION_CHANGE_TIMER = 5.0f;
			this.direction.set((random.nextFloat() * (-4 - 4) + 4), 0.0f, (random.nextFloat() * (-4 - 4) + 4));
		}
		this.origin.addTranslation(this.direction.x * deltaTime, 0.0f, this.direction.z * deltaTime);
		this.origin.addTranslation(0, GRAVITY * deltaTime * deltaTime, 0);
		
		float terrainHeight = terrain.getTerrainHeight(this.position.x, this.position.z);
		if (this.position.y < terrainHeight + 2.0f) {
			this.origin.addTranslation(0, terrainHeight - this.position.y + 2.0f, 0);
		}
	}
}
