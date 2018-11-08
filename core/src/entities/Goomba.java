package entities;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;

import environment.terrain.Terrain;
import graphics.Material;
import graphics.ModelMatrix;
import graphics.Shader;
import graphics.shapes.g3djmodel.MeshModel;
import utils.BezierMotion;
import utils.Point3D;

public class Goomba extends Character {
	private Random random;
	
	private BezierMotion motion;
	private float currentTime;
	private float startTime = 1.0f;
	private float endTime = 12.0f;
	private boolean firstFrame = true;
	private Point3D currPoint;
	
	public Goomba(
			Shader shader, MeshModel model, Texture diffuseTexture, Texture specularTexture,
			Material material, Point3D position
	) {
		super(shader, model, diffuseTexture, specularTexture, material, position);
		this.random = new Random();
		
		float randomPoint = (random.nextFloat() * 15) ;
		this.motion = new BezierMotion(position, new Point3D(position.x, position.y, position.z + randomPoint), 
				new Point3D(position.x + randomPoint*2, position.y, position.z + randomPoint), 
				position,
				startTime, endTime);
		this.currPoint = new Point3D();
	}
	
	public void display() {
		super.display();
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTransformation(this.origin.matrix);
		ModelMatrix.main.addRoatationY(180);
		this.shader.setModelMatrix(ModelMatrix.main.getMatrix());
		this.model.draw(this.shader);
		ModelMatrix.main.popMatrix();
	}
	
	public void update(float deltaTime, Terrain terrain) {
		super.update(deltaTime);
		
		if(firstFrame) {
			currentTime = 0.0f;
			firstFrame = false;
		}
		else {
			currentTime += deltaTime;
		}
		
		if(currentTime < startTime) {
			this.origin.addTranslation(0.0f, 0.0f, 0.0f);
			return;
		}
		else if (currentTime > endTime) {
			currentTime = startTime;
		}
		
		motion.getCurrentPosition(currentTime, currPoint);		
		Point3D movingTo = currPoint.subtract(this.origin.getOrigin());		
		this.origin.addTranslation(movingTo.x, movingTo.y, movingTo.z);
	}
}
