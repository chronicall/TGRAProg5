package tgra.prog5.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class Player extends Character {
	private boolean collision;
	
	public Player(Shader3D shader, Point3D position, Vector3D matAmbient, Vector3D matDiffuse, Vector3D matSpecular, float shine) {
		super(shader, position, matAmbient, matDiffuse, matSpecular, shine);
		this.collision = false;
	}
	
	public void display() {
		super.display();
	}
	
	public void update(float deltaTime) {
		// Boundary check
		super.update(deltaTime);
		Point3D originPoint = this.origin.getOrigin();
		//System.out.println("Player.x: " + originPoint.x + ", Player.y: " + originPoint.y + ", Player.z: " + originPoint.z);
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			this.origin.addRoatationY(-90.0f * deltaTime);
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			this.origin.addRoatationY(90.0f * deltaTime);
		}
		
		Vector3D vecUpZ = new Vector3D(0.0f, 0.0f, 4.0f * deltaTime);
		Vector3D vecDownZ = new Vector3D(0.0f, 0.0f, -4.0f * deltaTime);
		Vector3D vecUpX = new Vector3D(4.0f * deltaTime, 0.0f, 0.0f);
		Vector3D vecDownX = new Vector3D(-4.0f * deltaTime, 0.0f, 0.0f);
		this.collision = false;
		if (Gdx.input.isKeyPressed(Keys.W)) {
			this.detectCollision(vecUpZ, originPoint);
			this.origin.addTranslation(vecUpZ.x, vecUpZ.y, vecUpZ.z);
		}
		
		this.collision = false;
		if (Gdx.input.isKeyPressed(Keys.S)) {
			this.detectCollision(vecDownZ, originPoint);
			this.origin.addTranslation(vecDownZ.x, vecDownZ.y, vecDownZ.z);
		}
		
		this.collision = false;
		if (Gdx.input.isKeyPressed(Keys.A)) {
			this.detectCollision(vecUpX, originPoint);
			this.origin.addTranslation(vecUpX.x, vecUpX.y, vecUpX.z);
		}
		
		this.collision = false;
		if (Gdx.input.isKeyPressed(Keys.D)) {
			this.detectCollision(vecDownX, originPoint);
			this.origin.addTranslation(vecDownX.x, vecDownX.y, vecDownX.z);
		}
	}
	
	public void detectCollision(Vector3D vec, Point3D originPoint) {
	}
}
