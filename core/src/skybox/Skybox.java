package skybox;

import com.badlogic.gdx.utils.BufferUtils;

import graphics.Colour;
import graphics.Material;
import graphics.shapes.g3djmodel.Mesh;
import graphics.shapes.g3djmodel.MeshMaterial;
import graphics.shapes.g3djmodel.MeshModel;
import graphics.shapes.g3djmodel.MeshModelNode;
import graphics.shapes.g3djmodel.MeshModelNodePart;
import graphics.shapes.g3djmodel.MeshPart;
import shaders.Shader;
import tgra.prog5.game.Camera;
import utils.Point3D;
import utils.Quaternion;
import utils.Vector3D;

public class Skybox {
	private static final float SIZE = 500f;
	
	private static final float[] VERTICES = {        
	    -SIZE,  SIZE, -SIZE,
	    -SIZE, -SIZE, -SIZE,
	    SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	    -SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE
	};
	
	private static String[] TEXTURE_FILES = {
			"assets/textures/skybox/right.png",
			"assets/textures/skybox/left.png",
			"assets/textures/skybox/top.png",
			"assets/textures/skybox/bottom.png",
			"assets/textures/skybox/back.png",
			"assets/textures/skybox/front.png"
	};
	
	private MeshModel cube;
	private Shader shader;
	private Camera camera;
	
	public Skybox(Camera camera) {
		this.camera = camera;
		cube = new MeshModel();
		Mesh mesh = new Mesh();
		mesh.vertices = BufferUtils.newFloatBuffer(VERTICES.length);
		mesh.vertices.put(VERTICES);
		mesh.vertices.rewind();
		
		int pointer = 0;
		short[] indices = new short[6 * (VERTICES.length - 1) * (VERTICES.length - 1)];
		for (short gz = 0; gz < VERTICES.length - 1; gz++) {
			for(short gx = 0; gx < VERTICES.length - 1; gx++){
				short topLeft = (short) ((gz * VERTICES.length) + gx);
				short topRight = (short) (topLeft + 1);
				short bottomLeft = (short) (((gz + 1) * VERTICES.length) + gx);
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
		part.indices = BufferUtils.newShortBuffer(6 * (VERTICES.length - 1) * (VERTICES.length - 1));
		BufferUtils.copy(indices, 0, part.indices, indices.length);
		part.indices.rewind();
		part.mesh = mesh;
		this.cube.parts.add(part);
		this.cube.meshes.add(mesh);
		
		MeshMaterial meshMaterial = new MeshMaterial();
		meshMaterial.material = new Material();
		meshMaterial.material.diffuse = new Colour(0.5f, 0.5f, 0.5f, 1.0f);
		this.cube.materials.add(meshMaterial);
		
		MeshModelNode node = new MeshModelNode();
		node.rotation = new Quaternion(0, 0, 0, 0);
		node.scale = new Vector3D(1, 1, 1);
		node.translation = new Point3D(0, 0, 0);
		
		MeshModelNodePart nodePart = new MeshModelNodePart();
		nodePart.part = part;
		nodePart.meshMaterial = meshMaterial;
		
		node.parts.add(nodePart);
		this.cube.nodes.add(node);
		
		this.shader = new Shader("shaders/skyboxVertexShader.vert", "shaders/skyboxFragmentShader.frag");
		this.shader.setProjectionMatrix(this.camera.getProjectionMatrix());
		shader.setViewMatrix(this.camera.getViewMatrix());
		this.shader.setSkyBoxTexture(TEXTURE_FILES);
	}
	
	public void display() {
		this.shader.setProjectionMatrix(this.camera.getProjectionMatrix());
		this.shader.setViewMatrix(this.camera.getViewMatrix());
		//this.cube.draw(this.shader);
	}
	
	
	
	
	
	
	
	
	
	
}
