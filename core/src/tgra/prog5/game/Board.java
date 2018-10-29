package tgra.prog5.game;


public class Board {
	private static Shader3D shader;
	private static int width;
	private static int height;
	
	public static void create(Shader3D shader, int width, int height) {
		Board.shader = shader;
		Board.width = width;
		Board.height = height;
	}

	public static void newLevel() {
	}
	
	private static void generate() {
	}
	
	public static void drawBoard(float angle) {
		//Base wall
		Board.shader.setMaterialDiffuse(0.5f, 0.5f, 0.5f, 1.0f);
		Board.shader.setMaterialShiniess(10.0f);
		Board.shader.setMaterialDiffuse(1.0f, 1.0f, 1.0f, 1.0f);
		Board.shader.setMaterialShiniess(5.0f);
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(width / 2, 0.0f, width / 2);
		ModelMatrix.main.addScale(width, 0.2f, height);
		Board.shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube();
		ModelMatrix.main.popMatrix();
	}
	
	private static void drawSurrounding() { //Todo hafa þetta inni?
	}
}
