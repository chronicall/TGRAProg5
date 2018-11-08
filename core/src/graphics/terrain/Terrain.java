package graphics.terrain;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.BufferUtils;

import graphics.Colour;
import graphics.Material;
import graphics.shapes.g3djmodel.Mesh;
import graphics.shapes.g3djmodel.MeshMaterial;
import graphics.shapes.g3djmodel.MeshModel;
import graphics.shapes.g3djmodel.MeshModelNode;
import graphics.shapes.g3djmodel.MeshModelNodePart;
import graphics.shapes.g3djmodel.MeshPart;
import graphics.shapes.g3djmodel.MeshTexture;
import shaders.Shader;
import tgra.prog5.game.Prog5Game;
import utils.Point3D;
import utils.Quaternion;
import utils.Utils;
import utils.Vector3D;

public class Terrain {
	private static final float SIZE = 800;
	private static final float MAX_HEIGHT = 40.0f;
	private static final float MAX_PIXEL_COLOUR = 256 * 256 * 256;
	
	private float x;
	private float z;
	
	private MeshModel model;
	private TerrainTexturePack textures;
	private Texture blendMap;
	
	private float[][] heightMap;
	
	public Terrain(int gridX, int gridZ, TerrainTexturePack textures, Texture blendMap, String heightMap) {
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.textures = textures;
		this.blendMap = blendMap;
		this.model = new MeshModel(); 
		this.generateTerrain(heightMap);
	}
	
	public void display(Shader shader) {
		shader.setTextureTilingValue(60.0f);
		shader.setIsTerrain(1.0f);
		shader.setTerrainTextures(textures, blendMap);
		this.model.draw(shader);
		shader.setTextureTilingValue(1.0f);
		shader.setIsTerrain(0.0f);
	}
	
	private void generateTerrain(String heightMap) {
		// Read in the height map
		BufferedImage image = null;
		try {
			image = ImageIO.read(Prog5Game.class.getResource("/textures/heightMap.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int vertexCount = image.getHeight();
		
		// Initialize the height map array
		this.heightMap = new float[vertexCount][vertexCount];
		
		int count = vertexCount * vertexCount;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		short[] indices = new short[6 * (vertexCount - 1) * (vertexCount - 1)];
		int vertexPointer = 0;
		
		for (int i = 0; i < vertexCount; i++) {
			for (int j = 0; j < vertexCount; j++) {
				float height = getHeight(j, i, image);
				this.heightMap[j][i] = height;
				vertices[vertexPointer * 3] = (float) j / ((float) vertexCount - 1) * SIZE;
				vertices[vertexPointer * 3 + 1] = height;
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) vertexCount - 1) * SIZE;
				Vector3D normal = calculateNormal(j, i, image);
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				textureCoords[vertexPointer * 2] = (float) j / ((float) vertexCount - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) vertexCount - 1);
				vertexPointer++;
			}
		}
		Mesh mesh = new Mesh();
		mesh.vertices = BufferUtils.newFloatBuffer(count * 3);
		mesh.vertices.put(vertices);
		mesh.vertices.rewind();
		mesh.normals = BufferUtils.newFloatBuffer(count * 3);
		mesh.normals.put(normals);
		mesh.normals.rewind();
		mesh.uv = BufferUtils.newFloatBuffer(count * 2);
		mesh.uv.put(textureCoords);
		mesh.uv.rewind();
		
		int pointer = 0;
		for (short gz = 0; gz < vertexCount - 1; gz++) {
			for(short gx = 0; gx < vertexCount - 1; gx++){
				short topLeft = (short) ((gz * vertexCount) + gx);
				short topRight = (short) (topLeft + 1);
				short bottomLeft = (short) (((gz + 1) * vertexCount) + gx);
				short bottomRight = (short) (bottomLeft + 1);
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		MeshPart part = new MeshPart();
		part.type = "TRIANGLES";
		part.indices = BufferUtils.newShortBuffer(6 * (vertexCount - 1) * (vertexCount - 1));
		BufferUtils.copy(indices, 0, part.indices, indices.length);
		part.indices.rewind();
		
		part.mesh = mesh;
		model.parts.add(part);
		model.meshes.add(mesh);
		
		MeshMaterial meshMaterial = new MeshMaterial();
		meshMaterial.material = new Material();
		meshMaterial.material.diffuse = new Colour(0.5f, 0.5f, 0.5f, 1.0f);
		meshMaterial.meshTexture = new MeshTexture();
		meshMaterial.meshTexture.texture = this.textures.terrainTexture;
		meshMaterial.meshTexture.type = "DIFFUSE";
		meshMaterial.meshTexture.id = "Terrain";
		model.materials.add(meshMaterial);
		
		MeshModelNode node = new MeshModelNode();
		node.rotation = new Quaternion(0, 0, 0, 0);
		node.scale = new Vector3D(1, 1, 1);
		node.translation = new Point3D(0, 0, 0);
		
		MeshModelNodePart nodePart = new MeshModelNodePart();
		nodePart.part = part;
		nodePart.meshMaterial = meshMaterial;
		
		node.parts.add(nodePart);
		this.model.nodes.add(node);
	}
	
	// Used with calculateNormal
	private float getHeight(int x, int z, BufferedImage image) {
		if (x < 0 || x >= image.getHeight() || z < 0 || z >= image.getHeight()) {
			return 0;
		}
		float height = image.getRGB(x, z);
		
		height += MAX_PIXEL_COLOUR / 2;
		height /= MAX_PIXEL_COLOUR / 2;
		height *= MAX_HEIGHT;
		
		return height;
	}
	
	// A somewhat (very) inefficient way to calculate a normal vector
	private Vector3D calculateNormal(int x, int z, BufferedImage image) {
		float heightL = this.getHeight(x - 1, z, image);
		float heightR = this.getHeight(x + 1, z, image);
		float heightU = this.getHeight(x, z + 1, image);
		float heightD = this.getHeight(x, z - 1, image);
		
		Vector3D normal = new Vector3D(heightL - heightR, 2.0f, heightD - heightU);
		
		return normal.normalize();
	}

	public float getTerrainHeight(float worldX, float worldZ) {
		// Setting up the variables for the barycentric calculation
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;
		float gridSquareSize = SIZE / ((float) this.heightMap.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
		if (gridX >= this.heightMap.length - 1 || gridX < 0 || gridZ >= this.heightMap.length - 1 || gridZ < 0) {
			return 0;
		}
		float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
		
		float quadTriangleHeight;
		// Find the height values of the exact location inside the triangle.
		if (xCoord <= 1 - zCoord) {
			quadTriangleHeight = Utils.baryCentric(new Point3D(0, this.heightMap[gridX][gridZ], 0),
												   new Point3D(1, this.heightMap[gridX + 1][gridZ], 0),
												   new Point3D(0, this.heightMap[gridX][gridZ + 1], 1),
												   new Point3D(xCoord, 0, zCoord));
		} else {
			quadTriangleHeight = Utils.baryCentric(new Point3D(1, this.heightMap[gridX + 1][gridZ], 0),
											       new Point3D(1, this.heightMap[gridX + 1][gridZ + 1], 1),
											       new Point3D(0, this.heightMap[gridX][gridZ + 1], 1),
											       new Point3D(xCoord, 0, zCoord));
		}
		return quadTriangleHeight;
	}
	
	public float getX() {
		return x;
	}
	public float getZ() {
		return z;
	}
	public MeshModel getModel() {
		return model;
	}
	public TerrainTexturePack getTexture() {
		return textures;
	}
}
