package tgra.prog5.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class Player extends Character {
	private boolean collision;
	
	public Player(Shader3D shader, int width, int height, Point3D position, Vector3D matAmbient, Vector3D matDiffuse, Vector3D matSpecular, float shine) {
		super(shader, width, height, position, matAmbient, matDiffuse, matSpecular, shine);
		this.collision = false;
	}
	
	public void display() {
		super.display();
	}
	
	public void update(float deltaTime) {
		// Boundary check
		super.update(deltaTime);
		Point3D originPoint = this.origin.getOrigin();
		if (originPoint.x >= this.width - 1) {
			this.origin.addTranslationBaseCoords(this.width - originPoint.x - 1.0f, 0.0f, 0.0f);
		} else if (originPoint.x <= 0) {
			this.origin.addTranslationBaseCoords(0 - originPoint.x, 0.0f, 0.0f);
		}
		if (originPoint.z >= this.height - 1) {
			this.origin.addTranslationBaseCoords(0.0f, 0.0f, this.height - originPoint.z - 1.0f);
		} else if (originPoint.z <= 0) {
			this.origin.addTranslationBaseCoords(0.0f, 0.0f, 0 - originPoint.z);
		}
		
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
			if (!this.collision) {
				this.origin.addTranslation(vecUpZ.x, vecUpZ.y, vecUpZ.z);
			} else {
				// TODO: determine which direction we're traveling to see which direction character should slide.
				this.origin.addTranslation(vecUpX.x - 2.0f * deltaTime, vecUpX.y, vecUpX.z);
			}
		}
		
		this.collision = false;
		if (Gdx.input.isKeyPressed(Keys.S)) {
			this.detectCollision(vecDownZ, originPoint);
			if (!this.collision) {
				this.origin.addTranslation(vecDownZ.x, vecDownZ.y, vecDownZ.z);
			} else {
				// TODO: determine which direction we're traveling to see which direction character should slide.
				this.origin.addTranslation(vecDownX.x + 2.0f * deltaTime, vecDownX.y, vecDownX.z);
			}
		}
		
		this.collision = false;
		if (Gdx.input.isKeyPressed(Keys.A)) {
			this.detectCollision(vecUpX, originPoint);
			if (!this.collision) {
				this.origin.addTranslation(vecUpX.x, vecUpX.y, vecUpX.z);
			} else {
				// TODO: determine which direction we're traveling to see which direction character should slide.
				this.origin.addTranslation(vecUpZ.x, vecUpZ.y, vecUpZ.z - 2.0f * deltaTime);
			}
		}
		
		this.collision = false;
		if (Gdx.input.isKeyPressed(Keys.D)) {
			this.detectCollision(vecDownX, originPoint);
			if (!this.collision) {
				this.origin.addTranslation(vecDownX.x, vecDownX.y, vecDownX.z);
			} else {
				// TODO: determine which direction we're traveling to see which direction character should slide.
				this.origin.addTranslation(vecDownZ.x, vecDownZ.y, vecDownZ.z + 2.0f * deltaTime);
			}
		}
	}
	
	public void detectCollision(Vector3D vec, Point3D originPoint) {
		Point3D movingTo = originPoint.add(vec);
		// Check whether we'd be outside the boundary of the maze, avoiding index out of bounds errors.
		if (((int)movingTo.x > 0 && (int)movingTo.x < this.width - 1) && ((int)movingTo.z > 0 && (int)movingTo.z < this.height - 1)) {
			Vector3D direction = movingTo.different(originPoint);
			
			boolean xUp = false;
			boolean xDown = false;
			boolean xUnchanged = false;
			boolean zUp = false;
			boolean zDown = false;
			boolean zUnchanged = false;
			
			if (direction.x > 0) {
				// moving along the x axis, increasing
				xUp = true;
			} else if (direction.x < 0) {
				xDown = true;
				// moving along the x axis, decreasing
			} else {
				xUnchanged = true;
			}
			if (direction.z > 0) {
				zUp = true;
				//moving along the z axis, increasing
			} else if (direction.z < 0) {
				zDown = true;
				//moving along the z axis, decreasing
			} else {
				zUnchanged = true;
			}
			
			float radiusSquared = (float)Math.pow(0.5 + 0.8, 2);
		}
	}
}
