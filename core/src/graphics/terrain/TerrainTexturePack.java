package graphics.terrain;

import com.badlogic.gdx.graphics.Texture;

public class TerrainTexturePack {
	Texture terrainTexture;
	Texture rTexture;
	Texture gTexture;
	Texture bTexture;
	
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
