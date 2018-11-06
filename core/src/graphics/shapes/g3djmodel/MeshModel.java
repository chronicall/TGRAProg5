package graphics.shapes.g3djmodel;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

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
				shader.setMaterial(part.meshMaterial.material);

				// TODO: Allow multiple texture (diffues & specular)
				Texture diffuseTexture = null;
				Texture specularTexture = null;
				if (part.meshMaterial.meshTexture != null) {
					if (part.meshMaterial.meshTexture.type.equals("DIFFUSE")) {
						diffuseTexture = part.meshMaterial.meshTexture.texture;
					} else if (part.meshMaterial.meshTexture.type.equals("SPECULAR")) {
						specularTexture = part.meshMaterial.meshTexture.texture;
					}
				}
				shader.setDiffuseTexture(diffuseTexture);
				shader.setSpecularTexture(specularTexture);
				
				Gdx.gl.glVertexAttribPointer(shader.getVertexPointer(), 3, GL20.GL_FLOAT, false, 0, part.part.mesh.vertices);
				Gdx.gl.glVertexAttribPointer(shader.getNormalPointer(), 3, GL20.GL_FLOAT, false, 0, part.part.mesh.normals);
				if (part.part.mesh.uv != null) {
					Gdx.gl.glVertexAttribPointer(shader.getUVPointer(), 2, GL20.GL_FLOAT, false, 0, part.part.mesh.uv);
				}

				if(part.part.type.equals("TRIANGLES"))
				{
					Gdx.gl.glDrawElements(GL20.GL_TRIANGLES, part.part.indices.capacity(), GL20.GL_UNSIGNED_SHORT, part.part.indices);
				}
			}
			ModelMatrix.main.popMatrix();
		}
	}
}
