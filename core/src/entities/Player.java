package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;

import environment.Board;
import environment.Platform;
import graphics.Material;
import graphics.ModelMatrix;
import graphics.Point3D;
import graphics.Vector3D;
import graphics.shapes.BoxGraphic;
import graphics.shapes.SphereGraphic;
import graphics.shapes.g3djmodel.MeshModel;
import shaders.Shader;
import utils.Maths;

public class Player extends Character {
	private static final float RUN_SPEED = 10;
	private static final float TURN_SPEED = 120;
	private static final float JUMP_POWER = 15;
	private static final float GRAVITY = -30;
	
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
		ModelMatrix.main.addScale(5, 5, 5);
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
		// XXX: Quick and dirty way to offset the y position of the current model being used..
		this.position.set(this.position.x, this.position.y + 1, this.position.z);
		
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
		Point3D displacementHorizontal;
		if (Gdx.input.isKeyPressed(Keys.W)) {
			displacementHorizontal = this.detectCollissionHorizontal(this.position.add(vecZ), new Vector3D(0.4f, 0, 0.4f)); 
			if (displacementHorizontal.equals(new Point3D())) {
				this.origin.addTranslation(vecZ.x, vecZ.y, vecZ.z);
			}
		}
		if (Gdx.input.isKeyPressed(Keys.S)) {
			Vector3D down = vecZ.scale(-1);
			displacementHorizontal = this.detectCollissionHorizontal(this.position.add(down), new Vector3D(-0.4f, 0, -0.4f));
			if (displacementHorizontal.equals(new Point3D())) {
				this.origin.addTranslation(vecZ.x, vecZ.y, -vecZ.z);
			}
		}
		if (Gdx.input.isKeyPressed(Keys.A)) {
			displacementHorizontal = this.detectCollissionHorizontal(this.position.add(vecX), new Vector3D(0.4f, 0, 0.4f));
			if (displacementHorizontal.equals(new Point3D())) {
				this.origin.addTranslation(vecX.x, vecX.y, vecX.z);
			}
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			Vector3D right = vecX.scale(-1);
			displacementHorizontal = this.detectCollissionHorizontal(this.position.add(right), new Vector3D(-0.4f, 0, -0.4f)); 
			if (displacementHorizontal.equals(new Point3D())) {
				this.origin.addTranslation(-vecX.x, vecX.y, vecX.z);
			}
		}
		
		// Force the player down on every frame.
		this.upVelocity += GRAVITY * deltaTime;
		this.origin.addTranslation(0, this.upVelocity * deltaTime, 0);
		// Check if falling through the terrain.
		// If so, reset upward velocity and allow jumping again.
		//Point3D movingTo = new Point3D(0, this.position.y + this.upVelocity * deltaTime, 0);
		float displacementVertical = this.detectCollisionVertical(this.position);
		if (displacementVertical != 0.0f) {
			this.upVelocity = 0.0f;
			this.isJumping = false;
			this.origin.addTranslation(0, displacementVertical, 0);
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
	
	private Point3D detectCollissionHorizontal(Point3D movingTo, Vector3D offset) {
		Point3D pos;
		Point3D displacement = new Point3D();
		for (Platform platform : Board.getPlatforms()) {
			pos = platform.getPosition();
			if (Maths.isInside(movingTo.add(offset), pos)) {
				if (movingTo.y < pos.y + 2f) {
					displacement.set(pos.x - movingTo.x, 0, pos.z - movingTo.z);
					return displacement;
				}
			}
		}
		return displacement;
	}
	
	private float detectCollisionVertical(Point3D movingTo) {
		Point3D pos;
		for (Platform platform : Board.getPlatforms()) {
			pos = platform.getPosition();
			if (Maths.isInside(this.position, pos)) {
				if (movingTo.y < pos.y + 2.1f) {
					return (pos.y + 2.1f) - movingTo.y;
				}
			}
		}
		if (movingTo.y < BASE_TERRAIN_HEIGHT) {
			return BASE_TERRAIN_HEIGHT - movingTo.y;
		}
		return 0.0f;
	}
}
