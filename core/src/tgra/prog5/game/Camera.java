package tgra.prog5.game;

import java.nio.FloatBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.BufferUtils;

import entities.Player;
import graphics.Point3D;
import graphics.Vector3D;

public class Camera {
	// Eye position
	public Point3D eye;
	// Camera specific vectors. TODO: descriptive names
	private Vector3D u;
	private Vector3D v;
	private Vector3D n;
	
	// Perspective projection variables
	private float left;
	private float right;
	private float top;
	private float bottom;
	private float near;
	private float far;
	
	// The pitch of the camera
	private float pitch;
	private float distanceToPlayer;
	
	// Player instance to get access to the position
	private Player player;
	
	public Camera(Player player) {
		this.player = player;
		this.eye = new Point3D();
		this.u = new Vector3D(1.0f, 0.0f, 0.0f);
		this.v = new Vector3D(0.0f, 1.0f, 0.0f);
		this.n = new Vector3D(0.0f, 0.0f, 1.0f);
		
		this.pitch = 30.0f;
		
		this.left = -1;
		this.right = 1;
		this.bottom = -1;
		this.top = 1;
		this.near = -1;
		this.far = 1;
		
		// Square root, not ideal, I know.. but only doing this once when the game starts, so..
		// I think we can get away with that!
		this.distanceToPlayer = (float) Math.sqrt(
			Math.pow(this.eye.x - this.player.position.x, 2) +
			Math.pow(this.eye.y - this.player.position.y, 2) +
			Math.pow(this.eye.z - this.player.position.z, 2)
		);
	}
	
	public void update(float deltaTime) {
		float angle = 90.0f * deltaTime;
		
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			if (this.pitch < 80) {
				this.pitch += angle;
			} else {
				this.pitch = 80;
			}
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			if (this.pitch > 0) {
				this.pitch -= angle;
			} else {
				this.pitch = 0;
			}
		}
		if (Gdx.input.isKeyPressed(Keys.Q)) {
			if (this.distanceToPlayer < 7) {
				this.distanceToPlayer += 3.0f * deltaTime;
			} else {
				this.distanceToPlayer = 7;
			}
		}
		if (Gdx.input.isKeyPressed(Keys.E)) {
			if (this.distanceToPlayer > 1) {
				this.distanceToPlayer -= 3.0f * deltaTime;
			} else {
				this.distanceToPlayer = 1;
			}
		}
	}

	public void look(Point3D eye, Point3D center, Vector3D up) {
		// Find the horizontal and vertical distance between the camera and the player
		float horizontalDistance = (float) (this.distanceToPlayer * Math.cos(Math.toRadians(this.pitch)));
		float verticalDistance = (float) (this.distanceToPlayer * Math.sin(Math.toRadians(this.pitch)));
		
		// The offset of the camera from the player.
		// The X and Z values are determined by the horizontal distance.
		// The Y value is simply the vertical distance.
		float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(this.player.getYRotation())));
		float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(this.player.getYRotation())));
		this.eye.set(
				this.player.position.x - offsetX,
				this.player.position.y + verticalDistance,
				this.player.position.z - offsetZ
		);
		
		// Update the camera vectors
		this.n = Vector3D.difference(eye, center);
		this.u = up.cross(this.n);
		this.n = this.n.normalize();
		this.u = this.u.normalize();
		this.v = this.n.cross(this.u);
	}
	
	public void setEye(float x, float y, float z) {
		this.eye.set(x, y, z);
	}
	public void setEye(Point3D position) {
		this.eye.set(position.x, position.y, position.z);
	}
	
	public float getPitch() {
		return this.pitch;
	}
	
	// Set the variables for a perspective projection
	public void perspectiveProjection(float fov, float ratio, float near, float far) {
		this.top = near * (float)Math.tan(((double)fov / 2.0) * Math.PI / 180.0);
		this.bottom = -this.top;
		this.right = ratio * this.top;
		this.left = -this.right;
		this.near = near;
		this.far = far;
	}
	
	// Construct and return the view matrix
	public FloatBuffer getViewMatrix() {
		FloatBuffer matrixBuffer = BufferUtils.newFloatBuffer(16);
		float[] pm = new float[16];
		
		Vector3D minusEye = new Vector3D(-this.eye.x, -this.eye.y, -this.eye.z);

		pm[0] = this.u.x; pm[4] = this.u.y; pm[8] = this.u.z;  pm[12] = minusEye.dot(this.u);
		pm[1] = this.v.x; pm[5] = this.v.y; pm[9] = this.v.z;  pm[13] = minusEye.dot(this.v);
		pm[2] = this.n.x; pm[6] = this.n.y; pm[10] = this.n.z; pm[14] = minusEye.dot(this.n);
		pm[3] = 0.0f; 	  pm[7] = 0.0f;  	pm[11] = 0.0f; 	   pm[15] = 1.0f;
		
		matrixBuffer.put(pm);
		matrixBuffer.rewind();
		
		return matrixBuffer;
	}
	
	// Construct and return the projection matrix
	public FloatBuffer getProjectionMatrix() {
		FloatBuffer matrixBuffer = BufferUtils.newFloatBuffer(16);
		float[] pm = new float[16];
		
		pm[0] = (2.0f * near) / (right - left); pm[4] = 0.0f; 							pm[8] = (right + left) / (right - left); pm[12] = 0.0f;
		pm[1] = 0.0f; 							pm[5] = (2.0f * near) / (top - bottom); pm[9] = (top + bottom) / (top - bottom); pm[13] = 0.0f;
		pm[2] = 0.0f;							pm[6] = 0.0f; 							pm[10] = -(far + near) / (far - near);   pm[14] = -(2.0f * far * near) / (far - near);
		pm[3] = 0.0f; 							pm[7] = 0.0f; 							pm[11] = -1.0f; 					  	 pm[15] = 0.0f;
		
		matrixBuffer.put(pm);
		matrixBuffer.rewind();
		return matrixBuffer;
	}
}
