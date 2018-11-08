package shaders;

import java.nio.FloatBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import graphics.Material;
import graphics.terrain.TerrainTexturePack;
import textures.TextureData;
import utils.Utils;

public class Shader {
	private int renderingProgramID;
	private int vertexShaderID;
	private int fragmentShaderID;

	private int positionLoc;
	private int normalLoc;
	private int uvLoc;

	private int modelMatrixLoc;
	private int viewMatrixLoc;
	private int projectionMatrixLoc;
	
	private int textureTilingValueLoc;
	
	private boolean usesDiffuseTexture = false;
	private int usesDiffuseTextureLoc;
	private int diffuseTextureLoc;
	
	private boolean usesSpecularTexture = false;
	private int usesSpecularTextureLoc;
	private int specularTextureLoc;

	private int eyePositionLoc;
	
	private int skyColourLoc;
	private int fogDensityLoc;
	private int fogGradientLoc;
	
	private int isTerrainLoc;
	private int terrainTextureLoc;
	private int terrainRTextureLoc;
	private int terrainGTextureLoc;
	private int terrainBTextureLoc;
	private int terrainBlendMapLoc;
	
	private int materialAmbientLoc;
	private int materialDiffuseLoc;
	private int materialSpecularLoc;
	private int materialEmissionLoc;
	private int materialShininessLoc;
	
	private int globalAmbientLoc;
	private int lightColourLoc;
	private int lightPositionLoc;
	
	public Shader(String vertexShader, String fragmentShader) {
		String vertexShaderString;
		String fragmentShaderString;

		vertexShaderString = Gdx.files.internal(vertexShader).readString();
		fragmentShaderString =  Gdx.files.internal(fragmentShader).readString();
		
		this.vertexShaderID = Gdx.gl.glCreateShader(GL20.GL_VERTEX_SHADER);
		this.fragmentShaderID = Gdx.gl.glCreateShader(GL20.GL_FRAGMENT_SHADER);
	
		Gdx.gl.glShaderSource(this.vertexShaderID, vertexShaderString);
		Gdx.gl.glShaderSource(this.fragmentShaderID, fragmentShaderString);
	
		Gdx.gl.glCompileShader(this.vertexShaderID);
		Gdx.gl.glCompileShader(this.fragmentShaderID);
		
		System.out.println("Vertex shader compile messages:");
		System.out.println(Gdx.gl.glGetShaderInfoLog(this.vertexShaderID));
		System.out.println("Fragment shader compile messages:");
		System.out.println(Gdx.gl.glGetShaderInfoLog(this.fragmentShaderID));

		this.renderingProgramID = Gdx.gl.glCreateProgram();
	
		Gdx.gl.glAttachShader(this.renderingProgramID, this.vertexShaderID);
		Gdx.gl.glAttachShader(this.renderingProgramID, this.fragmentShaderID);
	
		Gdx.gl.glLinkProgram(this.renderingProgramID);

		this.positionLoc				= Gdx.gl.glGetAttribLocation(this.renderingProgramID, "a_position");
		Gdx.gl.glEnableVertexAttribArray(this.positionLoc);

		this.normalLoc					= Gdx.gl.glGetAttribLocation(this.renderingProgramID, "a_normal");
		Gdx.gl.glEnableVertexAttribArray(this.normalLoc);
		
		this.uvLoc						= Gdx.gl.glGetAttribLocation(this.renderingProgramID, "a_uv");
		Gdx.gl.glEnableVertexAttribArray(this.uvLoc);

		this.modelMatrixLoc				= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_modelMatrix");
		this.viewMatrixLoc				= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_viewMatrix");
		this.projectionMatrixLoc		= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_projectionMatrix");
		
		this.textureTilingValueLoc		= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_textureTilingValue");

		this.usesDiffuseTextureLoc		= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_usesDiffuseTexture");
		this.diffuseTextureLoc			= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_diffuseTexture");

		this.usesSpecularTextureLoc		= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_usesSpecularTexture");
		this.specularTextureLoc			= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_specularTexture");

		this.eyePositionLoc				= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_eyePosition");
		
		this.skyColourLoc				= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_skyColour");
		this.fogDensityLoc				= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_fogDensity");
		this.fogGradientLoc				= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_fogGradient");
		
		this.isTerrainLoc				= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_isTerrain");
		this.terrainTextureLoc			= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_terrain.terrainTexture");
		this.terrainRTextureLoc			= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_terrain.rTexture");
		this.terrainGTextureLoc			= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_terrain.gTexture");
		this.terrainBTextureLoc			= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_terrain.bTexture");
		this.terrainBlendMapLoc			= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_terrain.blendMap");
		
		this.materialAmbientLoc			= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_material.ambient");
		this.materialDiffuseLoc			= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_material.diffuse");
		this.materialSpecularLoc		= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_material.specular");
		this.materialEmissionLoc		= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_material.emission");
		this.materialShininessLoc		= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_material.shiniess");
		
		this.globalAmbientLoc			= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_globalAmbient");
		
		this.lightColourLoc				= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_lightColour");
		this.lightPositionLoc			= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_lightPosition");
		
		Gdx.gl.glUseProgram(this.renderingProgramID);
	}

	public int getVertexPointer() {
		return this.positionLoc;
	}
	
	public int getNormalPointer() {
		return this.normalLoc;
	}
	
	public int getUVPointer() {
		return this.uvLoc;
	}
	
	public void setTextureTilingValue(float tiling) {
		Gdx.gl.glUniform1f(this.textureTilingValueLoc, tiling);
	}
	
	public void setDiffuseTexture(Texture tex) {
		if(tex == null) {
			Gdx.gl.glUniform1f(this.usesDiffuseTextureLoc, 0.0f);
			this.usesDiffuseTexture = false;
		} else {
			tex.bind(0);
			Gdx.gl.glUniform1i(this.diffuseTextureLoc, 0);
			Gdx.gl.glUniform1f(this.usesDiffuseTextureLoc, 2.0f);
			this.usesDiffuseTexture = true;

			// Attempt at Mipmapping.. If I enable this it becomes incredibly laggy. :I
//			Gdx.gl.glGenerateMipmap(GL20.GL_TEXTURE_2D);
//			Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_MIN_FILTER, GL20.GL_LINEAR_MIPMAP_LINEAR);
			Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_S, GL20.GL_REPEAT);
			Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_T, GL20.GL_REPEAT);
		}
	}
	
	public void setSpecularTexture(Texture tex) {
		if(tex == null) {
			Gdx.gl.glUniform1f(this.usesSpecularTextureLoc, 0.0f);
			this.usesSpecularTexture = false;
		} else {
			tex.bind(0);
			Gdx.gl.glUniform1i(this.specularTextureLoc, 0);
			Gdx.gl.glUniform1f(this.usesSpecularTextureLoc, 2.0f);
			this.usesSpecularTexture = true;

			Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_S, GL20.GL_REPEAT);
			Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_T, GL20.GL_REPEAT);
		}
	}
	
	public void setSkyBoxTexture(String[] textureFiles) {
		int texID = Gdx.gl.glGenTexture();
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
		Gdx.gl.glBindTexture(GL20.GL_TEXTURE_CUBE_MAP, texID);
		for (int i = 0; i < textureFiles.length; i++) {
			TextureData data = Utils.decodeTextureFile(textureFiles[i]);
			Gdx.gl.glTexImage2D(
					GL20.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL20.GL_RGBA, data.getWidth(),
					data.getHeight(), 0, GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE, data.getBuffer()
			);
		}
		Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_CUBE_MAP, GL20.GL_TEXTURE_MAG_FILTER, GL20.GL_LINEAR);
		Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_CUBE_MAP, GL20.GL_TEXTURE_MIN_FILTER, GL20.GL_LINEAR);
	}
	
	public boolean usesTextures() {
		return (this.usesDiffuseTexture || this.usesSpecularTexture);
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
	
	public void setSkyColour(float r, float g, float b, float a) {
		Gdx.gl.glUniform4f(this.skyColourLoc, r, g, b, a);
	}
	public void setFogDensity(float density) {
		Gdx.gl.glUniform1f(this.fogDensityLoc, density);
	}
	public void setFogGradient(float gradient) {
		Gdx.gl.glUniform1f(this.fogGradientLoc, gradient);
	}
	
	public void setIsTerrain(float isTerrain) {
		Gdx.gl.glUniform1f(this.isTerrainLoc, isTerrain);
	}
	public void setTerrainTextures(TerrainTexturePack textures, Texture blendMap) {
		textures.getTerrainTexture().bind(0);
		Gdx.gl.glUniform1i(this.terrainTextureLoc, 0);
		Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_S, GL20.GL_REPEAT);
		Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_T, GL20.GL_REPEAT);
		
		textures.getrTexture().bind(1);
		Gdx.gl.glUniform1i(this.terrainRTextureLoc, 1);
		Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_S, GL20.GL_REPEAT);
		Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_T, GL20.GL_REPEAT);
		
		textures.getgTexture().bind(2);
		Gdx.gl.glUniform1i(this.terrainGTextureLoc, 2);
		Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_S, GL20.GL_REPEAT);
		Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_T, GL20.GL_REPEAT);
		
		textures.getbTexture().bind(3);
		Gdx.gl.glUniform1i(this.terrainBTextureLoc, 3);
		Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_S, GL20.GL_REPEAT);
		Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_T, GL20.GL_REPEAT);
		
		blendMap.bind(4);
		Gdx.gl.glUniform1i(this.terrainBlendMapLoc, 4);
		Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_S, GL20.GL_REPEAT);
		Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_T, GL20.GL_REPEAT);
	}
	
	public void setMaterial(Material material) {
		Gdx.gl.glUniform4f(this.materialAmbientLoc, material.ambient.r, material.ambient.g, material.ambient.b, material.ambient.a);
		Gdx.gl.glUniform4f(this.materialDiffuseLoc, material.diffuse.r, material.diffuse.g, material.diffuse.b, material.diffuse.a);
		Gdx.gl.glUniform4f(this.materialSpecularLoc, material.specular.r, material.specular.g, material.specular.b, material.specular.a);
		Gdx.gl.glUniform4f(this.materialEmissionLoc, material.emission.r, material.emission.g, material.emission.b, material.emission.a);
		Gdx.gl.glUniform1f(this.materialShininessLoc, material.shininess);
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
}
