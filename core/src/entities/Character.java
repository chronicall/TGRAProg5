package entities;

import com.badlogic.gdx.graphics.Texture;

import graphics.ModelMatrix;
import graphics.Point3D;
import graphics.Vector3D;
import shaders.Shader;

public class Character {
	public ModelMatrix origin;
	public Shader shader;
	public Point3D position;
	
	private Vector3D materialAmbient;
	private Vector3D materialDiffuse;
	private Vector3D materialSpecular;
	private float materialShine;
	
	private float xRotation;
	private float yRotation;
	private float zRotation;
	
	
	public Character(Shader shader, Point3D position, Vector3D matAmbient, Vector3D matDiffuse, Vector3D matSpecular, float shine) {
		this.shader = shader;
		this.position = position;
		this.materialAmbient = matAmbient;
		this.materialDiffuse = matDiffuse;
		this.materialSpecular = matSpecular;
		this.materialShine = shine;
		
		this.xRotation = 0.0f;
		this.yRotation = 0.0f;
		this.zRotation = 0.0f;
		
		this.origin = new ModelMatrix();
		this.origin.loadIdentityMatrix();
		this.origin.addTranslation(position.x, position.y, position.z);
		this.origin.addScale(0.4f, 0.4f, 0.4f);
	}
	
	public void display() {
		// Character wide display setup
		// Material colours and shine values
		this.shader.setMaterialAmbient(this.materialAmbient.x, this.materialAmbient.y, this.materialAmbient.z, 1.0f);
		this.shader.setMaterialDiffuse(this.materialDiffuse.x, this.materialDiffuse.y, this.materialDiffuse.z, 1.0f);
		this.shader.setMaterialSpecular(this.materialSpecular.x, this.materialSpecular.y, this.materialSpecular.z, 1.0f);
		this.shader.setMaterialShiniess(this.materialShine);
		this.shader.setMaterialEmission(0.2f, 0.2f, 0.2f, 0.2f);
	}
	
	public void update(float deltaTime) {
		// Character wide updates
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
