package graphics.shapes.g3djmodel;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import graphics.ModelMatrix;
import shaders.Shader;

public class MeshModel {
	public Vector<Mesh> meshes;
	public Vector<MeshPart> parts;
	public Vector<MeshMaterial> materials;
	public Vector<MeshModelNode> nodes;

	public MeshModel()
	{
		meshes = new Vector<Mesh>();
		parts = new Vector<MeshPart>();
		materials = new Vector<MeshMaterial>();
		nodes = new Vector<MeshModelNode>();
	}

	public void draw(Shader shader) {

		for(MeshModelNode node : nodes)
		{
			ModelMatrix.main.pushMatrix();

			ModelMatrix.main.addTranslation(node.translation.x, node.translation.y, node.translation.z);
			ModelMatrix.main.addRotationQuaternion(node.rotation.x, node.rotation.y, node.rotation.z, node.rotation.w);
			ModelMatrix.main.addScale(node.scale.x, node.scale.y, node.scale.z);

			shader.setModelMatrix(ModelMatrix.main.getMatrix());
			for(MeshModelNodePart part : node.parts)
			{
				//TODO: send part.material.xxx into the shader
				shader.setMaterialAmbient(part.meshMaterial.material.ambient.r, part.meshMaterial.material.ambient.g, part.meshMaterial.material.ambient.b, 1.0f);
				shader.setMaterialDiffuse(part.meshMaterial.material.diffuse.r, part.meshMaterial.material.diffuse.g, part.meshMaterial.material.diffuse.b, 1.0f);
				shader.setMaterialSpecular(part.meshMaterial.material.specular.r, part.meshMaterial.material.specular.g, part.meshMaterial.material.specular.b, 1.0f);
				shader.setMaterialShiniess(part.meshMaterial.material.shininess);
				shader.setMaterialEmission(part.meshMaterial.material.emission.r, part.meshMaterial.material.emission.g, part.meshMaterial.material.emission.b, 1.0f);

				if (part.meshMaterial.meshTexture.texture != null) {
					System.out.println("Texture used. Texture ID: " + part.meshMaterial.meshTexture.id);
					System.out.println("Filename: " + part.meshMaterial.meshTexture.fileName);
					System.out.println("Type: " + part.meshMaterial.meshTexture.type);
					if (part.meshMaterial.meshTexture.type.equals("DIFFUSE")) {
						shader.setDiffuseTexture(part.meshMaterial.meshTexture.texture);
					}
					if (part.meshMaterial.meshTexture.type.equals("SPECULAR")) {
						shader.setSpecularTexture(part.meshMaterial.meshTexture.texture);
					}
				} else {
					shader.setDiffuseTexture(null);
					shader.setSpecularTexture(null);
				}
				
				//TODO: use glVertexAttribPointer to activate the vertex and normal lists in part.part.mesh
				//make sure you're reading these in 3 and 3 together, not 2 and 2 like the UV coordinates
				System.out.println("Vertex Pointer: " + shader.getVertexPointer());
				System.out.println("Normal Pointer: " + shader.getNormalPointer());
				Gdx.gl.glVertexAttribPointer(shader.getVertexPointer(), 3, GL20.GL_FLOAT, false, 0, part.part.mesh.vertices);
				Gdx.gl.glVertexAttribPointer(shader.getNormalPointer(), 3, GL20.GL_FLOAT, false, 0, part.part.mesh.normals);

				//if you've added textures to your shader but will not be using them here
				//you should set the UV vertex attribute pointer to something long enough,
				//just so it doesn't crash
				System.out.println("UV Pointer: " + shader.getUVPointer());
				Gdx.gl.glVertexAttribPointer(shader.getUVPointer(), 2, GL20.GL_FLOAT, false, 0, part.part.mesh.uv);

				if(part.part.type.equals("TRIANGLES"))
				{
					//here you actually draw, using the index list from part.part to decide in which order the polygons are rendered
					//TODO: uncomment the following line:
					Gdx.gl.glDrawElements(GL20.GL_TRIANGLES, part.part.indices.capacity(), GL20.GL_UNSIGNED_SHORT, part.part.indices);
				}
			}
			ModelMatrix.main.popMatrix();
		}
	}
}
