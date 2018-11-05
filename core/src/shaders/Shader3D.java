package shaders;

import java.nio.FloatBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class Shader3D {
	private int renderingProgramID;
	
	private int vertexShaderID;
	private int fragmentShaderID;

	private int positionLoc;
	private int normalLoc;

	private int modelMatrixLoc;
	private int viewMatrixLoc;
	private int projectionMatrixLoc;

	private int eyePositionLoc;
	
	private int materialAmbientLoc;
	private int materialDiffuseLoc;
	private int materialSpecularLoc;
	private int materialEmissionLoc;
	private int materialShininessLoc;
	
	private int globalAmbientLoc;
	
	private int lightColourLoc;
	private int lightPositionLoc;
	
	private int sun1DirectionLoc;
	private int sun1AmbientLoc;
	private int sun1DiffuseLoc;
	private int sun1SpecularLoc;
	
	public Shader3D() {
		String vertexShaderString;
		String fragmentShaderString;

		vertexShaderString = Gdx.files.internal("shaders/mazeFragmentLighting3D.vert").readString();
		fragmentShaderString =  Gdx.files.internal("shaders/mazeFragmentLighting3D.frag").readString();
		
//		vertexShaderString = Gdx.files.internal("shaders/vertext3D.vert").readString();
//		fragmentShaderString =  Gdx.files.internal("shaders/fragment3D.frag").readString();

		this.vertexShaderID = Gdx.gl.glCreateShader(GL20.GL_VERTEX_SHADER);
		this.fragmentShaderID = Gdx.gl.glCreateShader(GL20.GL_FRAGMENT_SHADER);
	
		Gdx.gl.glShaderSource(this.vertexShaderID, vertexShaderString);
		Gdx.gl.glShaderSource(this.fragmentShaderID, fragmentShaderString);
	
		Gdx.gl.glCompileShader(this.vertexShaderID);
		System.out.println(Gdx.gl.glGetShaderInfoLog(this.vertexShaderID));
		Gdx.gl.glCompileShader(this.fragmentShaderID);
		System.out.println(Gdx.gl.glGetShaderInfoLog(this.fragmentShaderID));

		this.renderingProgramID = Gdx.gl.glCreateProgram();
	
		Gdx.gl.glAttachShader(this.renderingProgramID, this.vertexShaderID);
		Gdx.gl.glAttachShader(this.renderingProgramID, this.fragmentShaderID);
	
		Gdx.gl.glLinkProgram(this.renderingProgramID);

		this.positionLoc				= Gdx.gl.glGetAttribLocation(this.renderingProgramID, "a_position");
		Gdx.gl.glEnableVertexAttribArray(this.positionLoc);

		this.normalLoc					= Gdx.gl.glGetAttribLocation(this.renderingProgramID, "a_normal");
		Gdx.gl.glEnableVertexAttribArray(this.normalLoc);

		this.modelMatrixLoc				= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_modelMatrix");
		this.viewMatrixLoc				= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_viewMatrix");
		this.projectionMatrixLoc		= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_projectionMatrix");

		this.eyePositionLoc				= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_eyePosition");
		
		this.materialAmbientLoc			= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_material.ambient");
		this.materialDiffuseLoc			= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_material.diffuse");
		this.materialSpecularLoc		= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_material.specular");
		this.materialEmissionLoc		= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_material.emission");
		this.materialShininessLoc		= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_material.shiniess");
		
		this.globalAmbientLoc			= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_globalAmbient");
		
		this.lightColourLoc				= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_lightColour");
		this.lightPositionLoc			= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_lightPosition");
		
		this.sun1DirectionLoc			= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_sun1.direction");
		this.sun1AmbientLoc				= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_sun1.ambient");
		this.sun1DiffuseLoc				= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_sun1.diffuse");
		this.sun1SpecularLoc			= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_sun1.specular");
		
		Gdx.gl.glUseProgram(this.renderingProgramID);
	}

	public int getVertexPointer() {
		return this.positionLoc;
	}
	
	public int getNormalPointer() {
		return this.normalLoc;
	}
	
	public void setModelMatrix(FloatBuffer matrix) {
		Gdx.gl.glUniformMatrix4fv(this.modelMatrixLoc, 1, false, matrix);
	}
	public void setViewMatrix(FloatBuffer matrix) {
		Gdx.gl.glUniformMatrix4fv(this.viewMatrixLoc, 1, false, matrix);
	}
	public void setProjectionMatrix(FloatBuffer matrix) {
		Gdx.gl.glUniformMatrix4fv(this.projectionMatrixLoc, 1, false, matrix);
	}
	
	public void setEyePosition(float x, float y, float z, float w) {
		Gdx.gl.glUniform4f(this.eyePositionLoc, x, y, z, w);
	}
	
	public void setMaterialAmbient(float r, float g, float b, float a) {
		Gdx.gl.glUniform4f(this.materialAmbientLoc, r, g, b, a);
	}
	public void setMaterialDiffuse(float r, float g, float b, float a) {
		Gdx.gl.glUniform4f(this.materialDiffuseLoc, r, g, b, a);
	}
	public void setMaterialSpecular(float r, float g, float b, float a) {
		Gdx.gl.glUniform4f(this.materialSpecularLoc, r, g, b, a);
	}
	public void setMaterialEmission(float r, float g, float b, float a) {
		Gdx.gl.glUniform4f(this.materialEmissionLoc, r, g, b, a);
	}
	public void setMaterialShiniess(float shine) {
		Gdx.gl.glUniform1f(this.materialShininessLoc, shine);
	}
	
	public void setGlobalAmbient(float r, float g, float b, float a) {
		Gdx.gl.glUniform4f(this.globalAmbientLoc, r, g, b, a);
	}
	
	public void setLightColour(float r, float g, float b, float a) {
		Gdx.gl.glUniform4f(this.lightColourLoc, r, g, b, a);
	}
	public void setLightPosition(float x, float y, float z, float w) {
		Gdx.gl.glUniform4f(this.lightPositionLoc, x, y, z, w);
	}
	
	public void setSun1Direction(float x, float y, float z, float w) {
		Gdx.gl.glUniform4f(this.sun1DirectionLoc, x, y, z, w);
	}
	public void setSun1Ambient(float r, float g, float b, float a) {
		Gdx.gl.glUniform4f(this.sun1AmbientLoc, r, g, b, a);
	}
	public void setSun1Diffuse(float r, float g, float b, float a) {
		Gdx.gl.glUniform4f(this.sun1DiffuseLoc, r, g, b, a);
	}
	public void setSun1Specular(float r, float g, float b, float a) {
		Gdx.gl.glUniform4f(this.sun1SpecularLoc, r, g, b, a);
	}
}
