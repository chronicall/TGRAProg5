package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import shaders.Shader3D;
import utils.Point3D;
import utils.Vector3D;

public class Player extends Character {
	private boolean jumping = false;
	
	public Player(Shader3D shader, Point3D position, Vector3D matAmbient, Vector3D matDiffuse, Vector3D matSpecular, float shine) {
		super(shader, position, matAmbient, matDiffuse, matSpecular, shine);
	}
	
	public void display() {
		super.display();
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		this.position = this.origin.getOrigin();
		float playAngle = 90 * deltaTime;
		
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			this.origin.addRoatationY(-playAngle);
			this.setYRotation(this.getYRotation() + playAngle);
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			this.origin.addRoatationY(playAngle);
			this.setYRotation(this.getYRotation() - playAngle);
		}
		
		Vector3D vecUpZ = new Vector3D(0.0f, 0.0f, 10.0f * deltaTime);
		Vector3D vecDownZ = new Vector3D(0.0f, 0.0f, -10.0f * deltaTime);
		Vector3D vecUpX = new Vector3D(10.0f * deltaTime, 0.0f, 0.0f);
		Vector3D vecDownX = new Vector3D(-10.0f * deltaTime, 0.0f, 0.0f);
		if (Gdx.input.isKeyPressed(Keys.W)) {
			this.origin.addTranslation(vecUpZ.x, vecUpZ.y, vecUpZ.z);
		}
		if (Gdx.input.isKeyPressed(Keys.S)) {
			this.origin.addTranslation(vecDownZ.x, vecDownZ.y, vecDownZ.z);
		}
		if (Gdx.input.isKeyPressed(Keys.A)) {
			this.origin.addTranslation(vecUpX.x, vecUpX.y, vecUpX.z);
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			this.origin.addTranslation(vecDownX.x, vecDownX.y, vecDownX.z);
		}
		if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			if (!this.jumping) {
				this.jumping = true;
			}
		}
		
		if (this.jumping && this.position.y < 5.0f) {
			this.origin.addTranslation(0, 20.0f * deltaTime, 0);
		} else if (this.jumping && this.position.y > 5.0f) {
			this.jumping = false;
		}
		
		if (!this.jumping && this.position.y > 0.5f) {
			this.origin.addTranslation(0, -20.0f * deltaTime, 0);
		}
	}
}
