package entities;

import com.badlogic.gdx.graphics.Texture;

import graphics.Material;
import graphics.Point3D;
import graphics.Vector3D;
import graphics.shapes.g3djmodel.MeshModel;
import shaders.Shader;

public class Enemy extends Character {
	
	public Enemy(
			Shader shader, MeshModel model, Texture diffuseTexture, Texture specularTexture,
			Material material, Point3D position
	) {
		super(shader, model, diffuseTexture, specularTexture, material, position);
	}
	
	public void display() {
		super.display();
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
	}
}
