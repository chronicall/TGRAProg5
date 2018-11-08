package graphics.terrain;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.BufferUtils;

import graphics.Colour;
import graphics.Material;
import graphics.Point3D;
import graphics.Quaternion;
import graphics.Vector3D;
import graphics.shapes.g3djmodel.Mesh;
import graphics.shapes.g3djmodel.MeshMaterial;
import graphics.shapes.g3djmodel.MeshModel;
import graphics.shapes.g3djmodel.MeshModelNode;
import graphics.shapes.g3djmodel.MeshModelNodePart;
import graphics.shapes.g3djmodel.MeshPart;
import graphics.shapes.g3djmodel.MeshTexture;
import shaders.Shader;

public class Terrain {
	private static final float SIZE = 800;
	private static final int VERTEX_COUNT = 128;
	
	private float x;
	private float z;
	
	private MeshModel model;
	private TerrainTexturePack textures;
	private Texture blendMap;
	
	public Terrain(int gridX, int gridZ, TerrainTexturePack textures, Texture blendMap) {
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.textures = textures;
		this.blendMap = blendMap;
		this.model = new MeshModel(); 
		this.generateTerrain();
	}
	
	public void display(Shader shader) {
		shader.setTextureTilingValue(60.0f);
		shader.setIsTerrain(1.0f);
		shader.setTerrainTextures(textures, blendMap);
		this.model.draw(shader);
		shader.setTextureTilingValue(1.0f);
		shader.setIsTerrain(0.0f);
	}
	
	private void generateTerrain() {
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		short[] indices = new short[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
		int vertexPointer = 0;
		
		for (int i = 0; i < VERTEX_COUNT; i++) {
			for (int j = 0; j < VERTEX_COUNT; j++) {
				vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * SIZE;
				vertices[vertexPointer * 3 + 1] = 0;
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * SIZE;
				normals[vertexPointer * 3] = 0;
				normals[vertexPointer * 3 + 1] = 1;
				normals[vertexPointer * 3 + 2] = 0;
				textureCoords[vertexPointer * 2] = (float) j / ((float) VERTEX_COUNT - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);
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
		for (short gz = 0; gz < VERTEX_COUNT - 1; gz++) {
			for(short gx = 0; gx < VERTEX_COUNT - 1; gx++){
				short topLeft = (short) ((gz * VERTEX_COUNT) + gx);
				short topRight = (short) (topLeft + 1);
				short bottomLeft = (short) (((gz + 1) * VERTEX_COUNT) + gx);
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
		part.indices = BufferUtils.newShortBuffer(6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1));
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
