package environment.terrain;

import com.badlogic.gdx.graphics.Texture;

/*
 * Used for blending multiple textures for the terrain
 */
public class TerrainTexturePack {
	Texture terrainTexture; // The base texture
	Texture rTexture;		// The texture used with the Red value of the blend map
	Texture gTexture;		// The texture used with the Green value of the blend map
	Texture bTexture;		// The texture used with the Blue value of the blend map
	
	public TerrainTexturePack(
			Texture terrainTexture, Texture rTexture, Texture gTexture, Texture bTexture
	) {
		this.terrainTexture = terrainTexture;
		this.rTexture = rTexture;
		this.gTexture = gTexture;
		this.bTexture = bTexture;
	}

	public Texture getTerrainTexture() {
		return terrainTexture;
	}
	public Texture getrTexture() {
		return rTexture;
	}
	public Texture getgTexture() {
		return gTexture;
	}
	public Texture getbTexture() {
		return bTexture;
	}
}
