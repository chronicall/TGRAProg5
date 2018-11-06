package tgra.prog5.game;

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
import graphics.Colour;
import graphics.Material;
import graphics.ModelMatrix;
import graphics.Point3D;
import graphics.Vector3D;
import graphics.shapes.BoxGraphic;
import graphics.shapes.SphereGraphic;
import graphics.shapes.g3djmodel.G3DJModelLoader;
import graphics.shapes.g3djmodel.MeshModel;
import shaders.Shader;

public class Prog5Game extends ApplicationAdapter implements InputProcessor {
	private Shader shader;
	
	private Camera camera;
	private float fov;
	private Point3D eye;
	private Vector3D up;
	
	private int width;
	private int height;
	
	private Player player;
	private MeshModel playerModel;
	private Texture playerTexture;
	
	private Goomba goomba1, goomba2;
	private MeshModel goombaModel;
	
	private ChainChomp chainChomp;
	private MeshModel chainChompModel;
	
	private Material sunMaterial;

	@Override
	public void create () {
		Gdx.gl.glClearColor(0.3f, 0.7f, 1.0f, 1.0f);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.input.setInputProcessor(this);
		
		this.height = 50;
		this.width = 50;
		this.fov = 90.0f;
		
		this.shader = new Shader();
		
		this.playerTexture = new Texture(Gdx.files.internal("textures/dice.png"));
		this.playerModel = G3DJModelLoader.loadG3DJFromFile("bowsette/Bowsette.g3dj");
		this.goombaModel = G3DJModelLoader.loadG3DJFromFile("goomba/goomba.g3dj");
		this.chainChompModel = G3DJModelLoader.loadG3DJFromFile("chainChomp/chomp.g3dj");
		
		ModelMatrix.main = new ModelMatrix();
		ModelMatrix.main.loadIdentityMatrix();
		shader.setModelMatrix(ModelMatrix.main.getMatrix());

		BoxGraphic.create();
		SphereGraphic.create();
		Board.create(this.width, this.height);
		
		this.sunMaterial = new Material();
		this.sunMaterial.emission = new Colour(1,1,1,1);
		
		this.setup();
	}
	
	private void setup() {
		// Set up the player
		Point3D playerPos = new Point3D(2.0f, 0.5f, 3.0f);
		Material playerMaterial = new Material();
		this.player = new Player(
				this.shader, this.playerModel, this.playerTexture, null,
				playerMaterial, playerPos
		);
		
		// Set up the enemies
		Point3D goomba1Pos = new Point3D(15.0f, 0.5f, 20.0f);
		Material goombaMaterial = new Material();
		this.goomba1 = new Goomba(
				this.shader, this.goombaModel, null, null,
				goombaMaterial, goomba1Pos, false, this.height
		);
		
		Point3D goomba2Pos = new Point3D(20.0f, 0.5f, 10.0f);
		this.goomba2 = new Goomba(
				this.shader, this.goombaModel, null, null, 
				goombaMaterial, goomba2Pos, true, this.height
		);
		
		Point3D chainChompPos = new Point3D(10.0f, 0.5f, 10.0f);
		Material chainChompMaterial = new Material();
		this.chainChomp = new ChainChomp(
				this.shader, this.chainChompModel, null, null, 
				chainChompMaterial, chainChompPos, this.height, this.width
		);
		
		// Set up the camera
		this.camera = new Camera(this.player);
		this.eye = new Point3D(this.player.position.x - 2, this.player.position.y + 3.0f, this.player.position.z - 2);
		this.up = new Vector3D(0.0f, 1.0f, 0.0f);
		this.camera.look(this.eye, this.player.position, this.up);
		
		// Set up the light, a single white light as a "sun"
		this.shader.setLightPosition(10, 15, 10, 1);
		this.shader.setLightColour(1, 1, 1, 1);
		// No global ambient lighting. May change.
		this.shader.setGlobalAmbient(0, 0, 0, 1);
	}

	private void update() {
		float deltaTime = Gdx.graphics.getDeltaTime();
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}
		
		// Update camera and entities
		this.camera.update(deltaTime);
		this.player.update(deltaTime);
		this.camera.look(this.camera.eye, this.player.position, this.up);
		
		this.goomba1.update(deltaTime);
		this.goomba2.update(deltaTime);
		this.chainChomp.update(deltaTime);
	}
	
	private void display() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		// Set the camera and camera related things in the shader on each frame
		this.camera.perspectiveProjection(this.fov, 1.0f, 0.1f, 1000.0f);
		this.shader.setProjectionMatrix(this.camera.getProjectionMatrix());
		this.shader.setViewMatrix(this.camera.getViewMatrix());
		this.shader.setEyePosition(this.camera.eye.x, this.camera.eye.y, this.camera.eye.z, 1.0f);
		
		ModelMatrix.main.loadIdentityMatrix();

		// The "sun", show it as a glowing white orb. Ideally..
		ModelMatrix.main.pushMatrix();
		this.shader.setMaterial(this.sunMaterial);
		ModelMatrix.main.addTranslation(10, 15, 10);
		ModelMatrix.main.addScale(0.5f, 0.5f, 0.5f);
		this.shader.setModelMatrix(ModelMatrix.main.getMatrix());
		SphereGraphic.drawSolidSphere(this.shader, null, null);
		ModelMatrix.main.popMatrix();
		
		// Set material emission to 0, usually we don't want things glowing
		// no matter what.
		this.shader.setMaterialEmission(0, 0, 0, 1);
		
		Board.drawBoard(this.shader);
		// Draw all the entities
		this.player.display();
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