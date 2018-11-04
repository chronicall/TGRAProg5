package tgra.prog5.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;


public class Prog5Game extends ApplicationAdapter implements InputProcessor {
	private Shader3D shader;
	
	private Camera camera;
	private Camera miniMapCamera;
	private float fov;
	private float angle;
	private boolean firstPersonView;
	private Point3D eye;
	private Point3D center;
	private Vector3D up;
	
	private int width;
	private int height;
	
	//private float timer;
	
	private Player player;
	private Goomba goomba1, goomba2;
	private ChainChomp chainChomp;

	@Override
	public void create () {
		Gdx.input.setInputProcessor(this);

		this.shader = new Shader3D();
		
		BoxGraphic.create(shader.getVertexPointer(), shader.getNormalPointer());
		SphereGraphic.create(shader.getVertexPointer(), shader.getNormalPointer());
		SincGraphic.create(shader.getVertexPointer());
		CoordFrameGraphic.create(shader.getVertexPointer());
		Pyramid.create(shader);
		this.height = 50;
		this.width = 50;
		Board.create(shader, this.width, this.height);
		
		Gdx.gl.glClearColor(0.3f, 0.7f, 1.0f, 1.0f);

		ModelMatrix.main = new ModelMatrix();
		ModelMatrix.main.loadIdentityMatrix();
		shader.setModelMatrix(ModelMatrix.main.getMatrix());

		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		this.fov = 90.0f;
		this.angle = 0.0f;
		this.setup();
	}
	
	private void setup() {
		Point3D playerPos = new Point3D(2.0f, 3.5f, 3.0f);
		Vector3D playerAmbient = new Vector3D();
		Vector3D playerDiffuse = new Vector3D();
		Vector3D playerSpecular = new Vector3D();
		float playerShine = 10.0f;
		this.player = new Player(this.shader, playerPos, playerAmbient, playerDiffuse, playerSpecular, playerShine);
		
		//enemies
		Point3D goomba1Pos = new Point3D(15.0f, 3.5f, 20.0f);
		Vector3D goombaAmbient = new Vector3D();
		Vector3D goombaDiffuse = new Vector3D();
		Vector3D goombaSpecular = new Vector3D();
		float goombaShine = 10.0f;
		this.goomba1 = new Goomba(this.shader, goomba1Pos, goombaAmbient, goombaDiffuse, goombaSpecular, goombaShine, false, this.height);
		
		Point3D goomba2Pos = new Point3D(20.0f, 3.5f, 10.0f);
		this.goomba2 = new Goomba(this.shader, goomba2Pos, goombaAmbient, goombaDiffuse, goombaSpecular, goombaShine, true, this.height);
		
		Point3D chainChompPos = new Point3D(10.0f, 3.5f, 10.0f);
		Vector3D chainChompAmbient = new Vector3D();
		Vector3D chainChompDiffuse = new Vector3D();
		Vector3D chainChompSpecular = new Vector3D();
		float chainChompShine = 10.0f;
		this.chainChomp = new ChainChomp(this.shader, chainChompPos, chainChompAmbient, chainChompDiffuse, chainChompSpecular, chainChompShine, this.height, this.width);
		
		
		this.camera = new Camera();
		this.eye = new Point3D(0, 1, 0);
		this.center = new Point3D(2.0f, 1.0f, 5.0f);
		this.up = new Vector3D(0.0f, 1.0f, 0.0f);
		this.camera.look(this.eye, this.center, this.up);
		this.firstPersonView = true;
		//this.timer = 0;
		this.miniMapCamera = new Camera();
		this.miniMapCamera.orthographicProjection(-10, 10, -10, 10, 3.0f, 1000);
		
		this.shader.setLightPosition(10, 15, 10, 1);
		this.shader.setLightColour(1, 1, 1, 1);
		this.shader.setGlobalAmbient(0, 0, 0, 1);
	}

	private void input() {
	}
	
	private void update() {
		float deltaTime = Gdx.graphics.getDeltaTime();

		this.angle += 180.0f * deltaTime;
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}

		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			this.camera.yaw(90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			this.camera.yaw(-90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			this.camera.pitch(90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			this.camera.pitch(-90.0f * deltaTime);
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.R)) {
			this.camera.slide(0.0f, 3.0f * deltaTime, 0.0f);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.F)) {
			this.camera.slide(0.0f, -3.0f * deltaTime, 0.0f);
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.Q)) {
			this.camera.roll(-90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.E)) {
			this.camera.roll(90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.T)) {
			this.fov -= 30.0f * deltaTime;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.G)) {
			this.fov += 30.0f * deltaTime;
		}
		
		/*this.timer += deltaTime * 1.0f;
		if (this.timer >= 10) {
			this.setup();
		}*/

		// Toggle between first and third person view
		if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
			this.firstPersonView = !this.firstPersonView;
		}
		
		this.player.update(deltaTime);
		Point3D playerOrigin = this.player.origin.getOrigin();
		
		//enemies
		this.goomba1.update(deltaTime);
		this.goomba2.update(deltaTime);
		this.chainChomp.update(deltaTime);
		
		this.camera.setEye(playerOrigin.x - 1, playerOrigin.y + 1.5f, playerOrigin.z - 2f);
	}
	
	private void display() {
		//do all actual drawing and rendering here
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		ModelMatrix.main.loadIdentityMatrix();
		
		for (int viewNum = 0; viewNum < 2; viewNum++) {
			if (viewNum == 0) {
				Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				this.camera.perspectiveProjection(this.fov, 1.0f, 0.4f, 10000.0f);
				this.shader.setProjectionMatrix(this.camera.getProjectionMatrix());
				this.shader.setViewMatrix(this.camera.getViewMatrix());
				this.shader.setEyePosition(this.camera.eye.x, this.camera.eye.y, this.camera.eye.z, 1.0f);
			} else {
				Gdx.gl.glViewport(Gdx.graphics.getWidth() * (2/3), Gdx.graphics.getHeight() * (2/3), Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() / 5);
				this.miniMapCamera.look(new Point3D(this.camera.eye.x, 20.0f, this.camera.eye.z), this.camera.eye, new Vector3D(0.0f, 0.0f, -1.0f));
				this.shader.setProjectionMatrix(this.miniMapCamera.getProjectionMatrix());
				this.shader.setViewMatrix(this.miniMapCamera.getViewMatrix());
				this.shader.setEyePosition(this.miniMapCamera.eye.x, this.miniMapCamera.eye.y, this.miniMapCamera.eye.z, 1.0f);
			}
			
			ModelMatrix.main.loadIdentityMatrix();
			
			/*ModelMatrix.main.pushMatrix();
			this.shader.setMaterialAmbient(0, 0, 0, 1);
			this.shader.setMaterialDiffuse(0, 0, 0, 1);
			this.shader.setMaterialSpecular(0, 0, 0, 1);
			this.shader.setMaterialEmission(1, 1, 1, 1);
			ModelMatrix.main.addTranslation(10, 15, 10);
			ModelMatrix.main.addScale(0.5f, 0.5f, 0.5f);
			this.shader.setModelMatrix(ModelMatrix.main.getMatrix());
			SphereGraphic.drawSolidSphere();
			ModelMatrix.main.popMatrix();*/
			
			this.shader.setMaterialEmission(0, 0, 0, 1);
			
			// Temporary coordinate frame. Red is X, green is Y, blue is Z
			// REMOVE BEFORE HANDIN
			/*ModelMatrix.main.pushMatrix();
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
			ModelMatrix.main.popMatrix();*/
			// END REMOVE BEFORE HANDIN
			
			Board.drawBoard(this.angle);
			
			/*this.shader.setMaterialAmbient(0.0f, 0.0f, 0.0f, 1.0f);
			this.shader.setMaterialDiffuse(0.5f, 0.5f, 0.5f, 1.0f);
			this.shader.setMaterialSpecular(0.1f, 0.1f, 0.1f, 1.0f);
			
			ModelMatrix.main.pushMatrix();
			ModelMatrix.main.addTranslation(2.5f, 0.0f, 2.5f);
			ModelMatrix.main.addScale(5, 0.2f, 5);
			this.shader.setModelMatrix(ModelMatrix.main.getMatrix());
			BoxGraphic.drawSolidCube();
			ModelMatrix.main.popMatrix();
			
			ModelMatrix.main.pushMatrix();
			ModelMatrix.main.addTranslation(12.5f, 0, 2.5f);
			ModelMatrix.main.addScale(12.5f, 0.2f, 5);
			this.shader.setModelMatrix(ModelMatrix.main.getMatrix());
			BoxGraphic.drawSolidCube();
			ModelMatrix.main.popMatrix();
			
			ModelMatrix.main.pushMatrix();
			ModelMatrix.main.addTranslation(20f, 0, 10);
			ModelMatrix.main.addScale(5f, 0.2f, 20);
			this.shader.setModelMatrix(ModelMatrix.main.getMatrix());
			BoxGraphic.drawSolidCube();
			ModelMatrix.main.popMatrix();
			
			ModelMatrix.main.pushMatrix();
			ModelMatrix.main.addTranslation(28f, 0, 17.5f);
			ModelMatrix.main.addScale(15f, 0.2f, 5);
			this.shader.setModelMatrix(ModelMatrix.main.getMatrix());
			BoxGraphic.drawSolidCube();
			ModelMatrix.main.popMatrix();
			
			ModelMatrix.main.pushMatrix();
			ModelMatrix.main.addTranslation(33f, 0, 7.5f);
			ModelMatrix.main.addScale(5f, 0.2f, 15);
			this.shader.setModelMatrix(ModelMatrix.main.getMatrix());
			BoxGraphic.drawSolidCube();
			ModelMatrix.main.popMatrix();*/
			
			this.player.display();
			
			//enemiesz
			this.goomba1.display();
			this.goomba2.display();
			this.chainChomp.display();
			
			/*if (viewNum == 1) {
				this.shader.setMaterialAmbient(0.0f, 0.0f, 0.0f, 1.0f);
				this.shader.setMaterialDiffuse(0.5f, 0.0f, 0.0f, 1.0f);
				this.shader.setMaterialSpecular(0.7f, 0.6f, 0.6f, 1.0f);
				this.shader.setMaterialShiniess(0.25f);
				ModelMatrix.main.pushMatrix();
				ModelMatrix.main.addTranslation(this.camera.eye.x, this.camera.eye.y, this.camera.eye.z);
				ModelMatrix.main.addScale(0.35f, 0.35f, 0.35f);
				this.shader.setModelMatrix(ModelMatrix.main.getMatrix());
				BoxGraphic.drawSolidCube();
				ModelMatrix.main.popMatrix();
			}*/
		}
		
	}

	@Override
	public void render () {
		input();
		//put the code inside the update and display methods, depending on the nature of the code
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