package entities;

import shaders.Shader3D;
import shapes.BoxGraphic;
import utils.ModelMatrix;
import utils.Point3D;
import utils.Vector3D;

public class Character {
	public ModelMatrix origin;
	public Shader3D shader;
	public Point3D position;
	
	private Vector3D materialAmbient;
	private Vector3D materialDiffuse;
	private Vector3D materialSpecular;
	private float materialShine;
	
	private float xRotation;
	private float yRotation;
	private float zRotation;
	
	
	public Character(Shader3D shader, Point3D position, Vector3D matAmbient, Vector3D matDiffuse, Vector3D matSpecular, float shine) {
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
		// Player setup
		this.shader.setMaterialAmbient(this.materialAmbient.x, this.materialAmbient.y, this.materialAmbient.z, 1.0f);
		this.shader.setMaterialDiffuse(this.materialDiffuse.x, this.materialDiffuse.y, this.materialDiffuse.z, 1.0f);
		this.shader.setMaterialSpecular(this.materialSpecular.x, this.materialSpecular.y, this.materialSpecular.z, 1.0f);
		this.shader.setMaterialShiniess(this.materialShine);
		this.shader.setMaterialEmission(0.2f, 0.2f, 0.2f, 0.2f);
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTransformation(this.origin.matrix);
		this.shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube();
//		SphereGraphic.drawSolidSphere();
		ModelMatrix.main.popMatrix();
	}
	
	public void update(float deltaTime) {
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
