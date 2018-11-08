package entities;

import com.badlogic.gdx.graphics.Texture;

import graphics.Material;
import graphics.ModelMatrix;
import graphics.Point3D;
import graphics.shapes.g3djmodel.MeshModel;
import shaders.Shader;

public class Character {
	public ModelMatrix origin;
	public Shader shader;
	public Point3D position;
	
	protected MeshModel model;
	protected Texture diffuseTexture;
	protected Texture specularTexture;
	protected Material material;
	
	protected float xRotation;
	protected float yRotation;
	protected float zRotation;
	
	public Character(
			Shader shader, MeshModel model, Texture diffuseTexture, Texture specularTexture,
			Material material, Point3D position
	) {
		this.model = model;
		this.diffuseTexture = diffuseTexture;
		this.specularTexture = specularTexture;
		this.shader = shader;
		this.position = position;
		this.material = material;
		
		this.xRotation = 0.0f;
		this.yRotation = 0.0f;
		this.zRotation = 0.0f;
		
		this.origin = new ModelMatrix();
		this.origin.loadIdentityMatrix();
		this.origin.addTranslation(position.x, position.y, position.z);
		//this.origin.addScale(0.4f, 0.4f, 0.4f);
	}
	
	public void display() {
		// Character wide display setup
		// Material colours and shine values
		if (this.material == null) {
			this.shader.setMaterial(new Material());
		} else {
			this.shader.setMaterial(this.material);
		}
	}
	
	public void update(float deltaTime) {
		// Character wide updates
		this.position = this.origin.getOrigin();
	}
	
	public float getXRotation() {
		return xRotation;
	}
	public float getYRotation() {
		return yRotation;
	}
	public float getZRotation() {
		return zRotation;
	}

	public void setXRotation(float xRotation) {
		this.xRotation = xRotation;
	}
	public void setYRotation(float yRotation) {
		this.yRotation = yRotation;
	}
	public void setZRotation(float zRotation) {
		this.zRotation = zRotation;
	}

}
