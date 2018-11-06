package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;

import graphics.Material;
import graphics.ModelMatrix;
import graphics.Point3D;
import graphics.Vector3D;
import graphics.shapes.BoxGraphic;
import graphics.shapes.SphereGraphic;
import graphics.shapes.g3djmodel.MeshModel;
import shaders.Shader;

public class Player extends Character {
	private static final float RUN_SPEED = 10;
	private static final float TURN_SPEED = 120;
	private static final float JUMP_POWER = 15;
	private static final float GRAVITY = -25;
	
	private static final float BASE_TERRAIN_HEIGHT = 1;
	
	private float upVelocity;
	private boolean isJumping;
	
	public Player(
			Shader shader, MeshModel model, Texture diffuseTexture, Texture specularTexture, 
			Material material, Point3D position
	) {
		super(shader, model, diffuseTexture, specularTexture, material, position);
		this.upVelocity = 0.0f;
		this.isJumping = false;
	}
	
	public void display() {
		super.display();
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTransformation(this.origin.matrix);
		ModelMatrix.main.addScale(13, 13, 13);
		this.shader.setModelMatrix(ModelMatrix.main.getMatrix());
		if (this.model == null) {
			BoxGraphic.drawSolidCube(this.shader, this.diffuseTexture, this.specularTexture);
			SphereGraphic.drawOutlineSphere(this.shader, this.diffuseTexture, this.specularTexture);
		} else {
			this.model.draw(this.shader);
		}
		ModelMatrix.main.popMatrix();
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		// Keeping the position updated makes for less calls to get the origin point
		//this.position = this.origin.getOrigin();
		float playAngle = TURN_SPEED * deltaTime;
		
		// Update the angle the player is rotated by
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			this.origin.addRoatationY(-playAngle);
			this.setYRotation(this.getYRotation() + playAngle);
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			this.origin.addRoatationY(playAngle);
			this.setYRotation(this.getYRotation() - playAngle);
		}
		
		// Movement vectors, we only move along the X and Z axes with WASD.
		Vector3D vecZ = new Vector3D(0.0f, 0.0f, RUN_SPEED * deltaTime);
		Vector3D vecX = new Vector3D(RUN_SPEED * deltaTime, 0.0f, 0.0f);
		if (Gdx.input.isKeyPressed(Keys.W)) {
			this.origin.addTranslation(vecZ.x, vecZ.y, vecZ.z);
		}
		if (Gdx.input.isKeyPressed(Keys.S)) {
			this.origin.addTranslation(-vecZ.x, vecZ.y, -vecZ.z);
		}
		if (Gdx.input.isKeyPressed(Keys.A)) {
			this.origin.addTranslation(vecX.x, vecX.y, vecX.z);
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			this.origin.addTranslation(-vecX.x, -vecX.y, -vecX.z);
		}
		
		// Force the player down on every frame.
		this.upVelocity += GRAVITY * deltaTime;
		this.origin.addTranslation(0, this.upVelocity * deltaTime, 0);
		
		// Check if falling through the terrain.
		// If so, reset upward velocity and allow jumping again.
		if (this.position.y < BASE_TERRAIN_HEIGHT) {
			this.upVelocity = 0.0f;
			this.isJumping = false;
			this.origin.addTranslation(0, BASE_TERRAIN_HEIGHT - this.position.y, 0);
		}
		
		/*
		 * TODO: Add variable jump, the jump height depends on how long the spacebar
		 * 		 is held pressed.
		 */
		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			if (!this.isJumping) {
				this.upVelocity = JUMP_POWER;
				this.isJumping = true;
			}
		}
	}
}
