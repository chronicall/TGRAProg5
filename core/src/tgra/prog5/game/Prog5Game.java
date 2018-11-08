package tgra.prog5.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import entities.ChainChomp;
import entities.Goomba;
import entities.Player;
import environment.Board;
import environment.Platform;
import graphics.Material;
import graphics.ModelMatrix;
import graphics.Point3D;
import graphics.Vector3D;
import graphics.shapes.g3djmodel.G3DJModelLoader;
import graphics.shapes.g3djmodel.MeshModel;
import graphics.shapes.g3djmodel.MeshModelNode;
import graphics.terrain.Terrain;
import graphics.terrain.TerrainTexturePack;
import shaders.Shader;
import utils.Maths;

public class Prog5Game extends ApplicationAdapter implements InputProcessor {
	private Shader shader;

	private Camera camera;
	private float fov;
	private Point3D eye;
	private Vector3D up;
	
	private Random random;
	
	private Player player;
	private MeshModel playerModel;
	private Texture playerTexture;
	
	private Goomba goomba1, goomba2;
	private MeshModel goombaModel;
	
	private ChainChomp chainChomp;
	private MeshModel chainChompModel;
	
	private Terrain terrain1;
	
	private List<MeshModel> trees;

	@Override
	public void create () {
		Gdx.gl.glClearColor(0.3f, 0.7f, 1.0f, 1.0f);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		// Enable back face culling
		Gdx.gl.glEnable(GL20.GL_CULL_FACE);
		Gdx.gl.glCullFace(GL20.GL_BACK);
		Gdx.input.setInputProcessor(this);
		
		this.shader = new Shader();
		this.shader.setTextureTilingValue(1.0f);
		this.shader.setFogDensity(0.005f);
		this.shader.setFogGradient(1.0f);
		
		ModelMatrix.main = new ModelMatrix();
		ModelMatrix.main.loadIdentityMatrix();
		shader.setModelMatrix(ModelMatrix.main.getMatrix());

		this.fov = 90.0f;
		this.random = new Random();
		
		this.playerModel = G3DJModelLoader.loadG3DJFromFile("bowsette/Bowsette.g3dj");
		this.goombaModel = G3DJModelLoader.loadG3DJFromFile("goomba/goomba.g3dj");
		this.chainChompModel = G3DJModelLoader.loadG3DJFromFile("chainChomp/chomp.g3dj");
		
		TerrainTexturePack terrainTextures = new TerrainTexturePack(
				new Texture(Gdx.files.internal("textures/grass2.png")),
				new Texture(Gdx.files.internal("textures/dirt.png")),
				new Texture(Gdx.files.internal("textures/grassFlowers.png")),
				new Texture(Gdx.files.internal("textures/path.png"))
		);
		Texture blendMap = new Texture(Gdx.files.internal("textures/blendMap.png"));
		
		this.terrain1 = new Terrain(0, 0, terrainTextures, blendMap, "assets/textures/heightMap.png");
		Board.create(this.terrain1);
		
		// Set up the player
		Point3D playerPos = new Point3D(198.0f, 5.0f, 194.0f);
		Material playerMaterial = new Material();
		this.player = new Player(
				this.shader, this.playerModel, this.playerTexture, null,
				playerMaterial, playerPos
		);
		
		// Set up the enemies
		Point3D goomba1Pos = new Point3D(215.0f, this.terrain1.getTerrainHeight(215, 220), 220.0f);
		this.goomba1 = new Goomba(
				this.shader, this.goombaModel, null, null, null, goomba1Pos
		);
		
		Point3D goomba2Pos = new Point3D(220.0f, this.terrain1.getTerrainHeight(220, 210), 210.0f);
		this.goomba2 = new Goomba(
				this.shader, this.goombaModel, null, null, null, goomba2Pos
		);
		
		Point3D chainChompPos = new Point3D(210.0f, this.terrain1.getTerrainHeight(210, 210), 210.0f);
		this.chainChomp = new ChainChomp(
				this.shader, this.chainChompModel, null, null, 
				null, chainChompPos
		);
		
		// Set up the camera
		this.eye = new Point3D(playerPos.x - 2, playerPos.y + 3.0f, playerPos.z - 2);
		this.up = new Vector3D(0.0f, 1.0f, 0.0f);
		this.camera = new Camera(this.player, this.eye);
		this.camera.look(this.eye, playerPos, this.up);
		
		// Set up the light, a single white light as a "sun"
		this.shader.setLightPosition(210, 215, 210, 1);
		this.shader.setLightColour(1, 1, 1, 1);
		// No global ambient lighting. May change.
		this.shader.setGlobalAmbient(0, 0, 0, 1);
		
		this.trees = new ArrayList<MeshModel>();
		
		MeshModel tree;
		for (int i = 0; i < 50; i++) {
			tree = G3DJModelLoader.loadG3DJFromFile("tree/tree.g3dj");
			float x = 0, z = 0;
			while (true) {
				x = this.random.nextFloat() * (-400 - 800) + 800;
				z = this.random.nextFloat() * (-600 - 600) + 600;
				if (Maths.isInside(this.player.position, new Point3D(x, 0, z)) ||
					Maths.isInside(this.goomba1.position, new Point3D(x, 0, z)) ||
					Maths.isInside(this.goomba2.position, new Point3D(x, 0, z)) ||
					Maths.isInside(this.chainChomp.position, new Point3D(x, 0, z))
				) {
					continue;
				}
				boolean inPlatform = false;
				for (Platform platform : Board.getPlatforms()) {
					if (Maths.isInside(new Point3D(x, 0, z), platform.getPosition())) {
						inPlatform = true;
						break;
					}
				}
				if (!inPlatform) {
					break;
				}
			}
			float y = this.terrain1.getTerrainHeight(x, z);
			for (MeshModelNode node : tree.nodes) {
				node.translation = node.translation.add(new Vector3D(x, y, z));
			}
			this.trees.add(tree);
		}
	}

	private void update() {
		float deltaTime = Gdx.graphics.getDeltaTime();
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}
		
		// Update camera and entities
		this.camera.update(deltaTime);
		this.player.update(deltaTime, this.terrain1);
		this.camera.look(this.camera.eye, this.player.position, this.up);
		
		this.goomba1.update(deltaTime, this.terrain1);
		this.goomba2.update(deltaTime, this.terrain1);
		this.chainChomp.update(deltaTime, this.terrain1);
	}
	
	private void display() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		this.shader.setSkyColour(0.3f, 0.7f, 1.0f, 1.0f);
		
		// Set the camera and camera related things in the shader on each frame
		this.camera.perspectiveProjection(this.fov, 1.0f, 0.1f, 1000.0f);
		this.shader.setProjectionMatrix(this.camera.getProjectionMatrix());
		this.shader.setViewMatrix(this.camera.getViewMatrix());
		this.shader.setEyePosition(this.camera.eye.x, this.camera.eye.y, this.camera.eye.z, 1.0f);
		
		ModelMatrix.main.loadIdentityMatrix();

		// Draw all the terrain and entities
		this.terrain1.display(this.shader);
		for (MeshModel tree : trees) {
			tree.draw(this.shader);
		}
		Board.draw(this.shader);
		
		// Need to temporarily disable culling back faces or parts are missing from the crown.
		Gdx.gl.glDisable(GL20.GL_CULL_FACE);
		this.player.display();
		Gdx.gl.glEnable(GL20.GL_CULL_FACE);
		Gdx.gl.glCullFace(GL20.GL_BACK);

		this.goomba1.display();
		this.goomba2.display();
		this.chainChomp.display();
	}

	@Override
	public void render () {
		update();
		display();
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}