package tgra.prog5.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;

import entities.ChainChomp;
import entities.Goomba;
import entities.Player;
import environment.Board;
import shaders.Shader3D;
import shapes.BoxGraphic;
import shapes.SphereGraphic;
import utils.ModelMatrix;
import utils.Point3D;
import utils.Vector3D;


public class Prog5Game extends ApplicationAdapter implements InputProcessor {
	private Shader3D shader;
	
	private Camera camera;
	private float fov;
	private Point3D eye;
	private Vector3D up;
	private float distanceToPlayer;
	
	private int width;
	private int height;
	
	private Player player;
	private Goomba goomba1, goomba2;
	private ChainChomp chainChomp;

	@Override
	public void create () {
		Gdx.input.setInputProcessor(this);

		this.shader = new Shader3D();
		
		BoxGraphic.create(shader.getVertexPointer(), shader.getNormalPointer());
		SphereGraphic.create(shader.getVertexPointer(), shader.getNormalPointer());
		this.height = 50;
		this.width = 50;
		Board.create(shader, this.width, this.height);
		
		Gdx.gl.glClearColor(0.3f, 0.7f, 1.0f, 1.0f);

		ModelMatrix.main = new ModelMatrix();
		ModelMatrix.main.loadIdentityMatrix();
		shader.setModelMatrix(ModelMatrix.main.getMatrix());

		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		this.fov = 90.0f;
		this.setup();
	}
	
	private void setup() {
		Point3D playerPos = new Point3D(2.0f, 0.5f, 3.0f);
		Vector3D playerAmbient = new Vector3D();
		Vector3D playerDiffuse = new Vector3D();
		Vector3D playerSpecular = new Vector3D();
		float playerShine = 10.0f;
		this.player = new Player(
				this.shader, playerPos,
				playerAmbient, playerDiffuse, playerSpecular, playerShine
		);
		
		//enemies
		Point3D goomba1Pos = new Point3D(15.0f, 0.5f, 20.0f);
		Vector3D goombaAmbient = new Vector3D();
		Vector3D goombaDiffuse = new Vector3D();
		Vector3D goombaSpecular = new Vector3D();
		float goombaShine = 10.0f;
		this.goomba1 = new Goomba(
				this.shader, goomba1Pos,
				goombaAmbient, goombaDiffuse, goombaSpecular, goombaShine,
				false, this.height
		);
		
		Point3D goomba2Pos = new Point3D(20.0f, 0.5f, 10.0f);
		this.goomba2 = new Goomba(
				this.shader, goomba2Pos,
				goombaAmbient, goombaDiffuse, goombaSpecular, goombaShine,
				true, this.height
		);
		
		Point3D chainChompPos = new Point3D(10.0f, 0.5f, 10.0f);
		Vector3D chainChompAmbient = new Vector3D();
		Vector3D chainChompDiffuse = new Vector3D();
		Vector3D chainChompSpecular = new Vector3D();
		float chainChompShine = 10.0f;
		this.chainChomp = new ChainChomp(
				this.shader, chainChompPos,
				chainChompAmbient, chainChompDiffuse, chainChompSpecular, chainChompShine,
				this.height, this.width
		);
		
		this.camera = new Camera();
		this.eye = new Point3D(this.player.position.x - 2, this.player.position.y + 3.0f, this.player.position.z - 2);
		this.up = new Vector3D(0.0f, 1.0f, 0.0f);
		this.camera.look(this.eye, this.player.position, this.up);

		// Square root, not ideal, I know.. but only doing this once when the game starts, so..
		// I think we can get away with that!
		this.distanceToPlayer = (float) Math.sqrt(
			Math.pow(this.eye.x - this.player.position.x, 2) +
			Math.pow(this.eye.y - this.player.position.y, 2) +
			Math.pow(this.eye.z - this.player.position.z, 2)
		);
		
		this.shader.setLightPosition(10, 15, 10, 1);
		this.shader.setLightColour(1, 1, 1, 1);
		this.shader.setGlobalAmbient(0, 0, 0, 1);
	}

	private void update() {
		float deltaTime = Gdx.graphics.getDeltaTime();

		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}
		
		if (Gdx.input.isKeyPressed(Keys.Q)) {
			this.distanceToPlayer += 5.0f * deltaTime;
		}
		if (Gdx.input.isKeyPressed(Keys.E)) {
			this.distanceToPlayer -= 5.0f * deltaTime;
		}
		
		this.camera.update(deltaTime);
		this.player.update(deltaTime);
		
		//enemies
		this.goomba1.update(deltaTime);
		this.goomba2.update(deltaTime);
		this.chainChomp.update(deltaTime);
		
		float horizontalDistance = (float) (this.distanceToPlayer * Math.cos(Math.toRadians(this.camera.pitch)));
		float verticalDistance = (float) (this.distanceToPlayer * Math.sin(Math.toRadians(this.camera.pitch)));
		
		float theta = this.player.getYRotation() + 0.0f;
		float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
		
		this.camera.eye.set(
				this.player.position.x - offsetX,
				this.player.position.y + verticalDistance,
				this.player.position.z - offsetZ
		);
		this.camera.look(this.camera.eye, this.player.position, this.up);
	}
	
	private void display() {
		//do all actual drawing and rendering here
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		ModelMatrix.main.loadIdentityMatrix();
		
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.camera.perspectiveProjection(this.fov, 1.0f, 0.4f, 10000.0f);
		this.shader.setProjectionMatrix(this.camera.getProjectionMatrix());
		this.shader.setViewMatrix(this.camera.getViewMatrix());
		this.shader.setEyePosition(this.camera.eye.x, this.camera.eye.y, this.camera.eye.z, 1.0f);
		
		ModelMatrix.main.loadIdentityMatrix();
		
		ModelMatrix.main.pushMatrix();
		this.shader.setMaterialAmbient(0, 0, 0, 1);
		this.shader.setMaterialDiffuse(0, 0, 0, 1);
		this.shader.setMaterialSpecular(0, 0, 0, 1);
		this.shader.setMaterialEmission(1, 1, 1, 1);
		ModelMatrix.main.addTranslation(10, 15, 10);
		ModelMatrix.main.addScale(0.5f, 0.5f, 0.5f);
		this.shader.setModelMatrix(ModelMatrix.main.getMatrix());
		SphereGraphic.drawSolidSphere();
		ModelMatrix.main.popMatrix();
		
		this.shader.setMaterialEmission(0, 0, 0, 1);
		
		// Temporary coordinate frame. Red is X, green is Y, blue is Z
		// REMOVE BEFORE HANDIN
		ModelMatrix.main.pushMatrix();
		this.shader.setMaterialDiffuse(1, 0, 0, 1.0f);
		ModelMatrix.main.addTranslation(5, 0, 0);
		ModelMatrix.main.addScale(10.0f, 0.2f, 0.2f);
		this.shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube();
		ModelMatrix.main.popMatrix();
		
		ModelMatrix.main.pushMatrix();
		this.shader.setMaterialDiffuse(0, 1, 0, 1.0f);
		ModelMatrix.main.addTranslation(0, 5, 0);
		ModelMatrix.main.addScale(0.2f, 10.0f, 0.2f);
		this.shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube();
		ModelMatrix.main.popMatrix();
		
		ModelMatrix.main.pushMatrix();
		this.shader.setMaterialDiffuse(0, 0, 1, 1.0f);
		ModelMatrix.main.addTranslation(0, 0, 5);
		ModelMatrix.main.addScale(0.2f, 0.2f, 10.0f);
		this.shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube();
		ModelMatrix.main.popMatrix();
		// END REMOVE BEFORE HANDIN
		
		Board.drawBoard();
		
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