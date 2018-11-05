package entities;

import graphics.Point3D;
import graphics.Vector3D;
import shaders.Shader;

public class Enemy extends Character {
	
	public Enemy(Shader shader, Point3D position, Vector3D matAmbient, Vector3D matDiffuse, Vector3D matSpecular, float shine) {
		super(shader, position, matAmbient, matDiffuse, matSpecular, shine);
	}
	
	public void display() {
		super.display();
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
	}
}
