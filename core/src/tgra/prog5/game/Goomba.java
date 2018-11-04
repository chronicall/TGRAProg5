package tgra.prog5.game;

public class Goomba extends Enemy{
	private boolean yMotion;
	private boolean up;
	private Point3D originalPosition;
	private float height;

	public Goomba(Shader3D shader, Point3D position, Vector3D matAmbient, Vector3D matDiffuse, Vector3D matSpecular,
			float shine, boolean yMotion, float height) {
		super(shader, position, matAmbient, matDiffuse, matSpecular, shine);
		
		this.yMotion = yMotion;
		this.up = true;
		this.originalPosition = position;
		this.height = height;
	}
	
	public void display() {
		super.display();
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		Point3D originPoint = this.origin.getOrigin();
		Vector3D move;
		
		if(yMotion) {
			if(up)
			{
				move = new Vector3D(0.0f, 4.0f * deltaTime, 0.0f);
			}
			else {
				move = new Vector3D(0.0f, -4.0f * deltaTime, 0.0f);				
			}
			
			Point3D movingTo = originPoint.add(move);
			
			
			if(movingTo.y > 12 || movingTo.y < 3.5) {
				up = !up;
			}
		}
		else {	
			if(up)
			{
				move = new Vector3D(0.0f, 0.0f, 4.0f * deltaTime);
			}
			else {
				move = new Vector3D(0.0f, 0.0f, -4.0f * deltaTime);				
			}
			
			Point3D movingTo = originPoint.add(move);
						
			if(Math.abs((movingTo.z - this.originalPosition.z)) > 5 || movingTo.z < 2 || movingTo.z > this.height - 1.0f) {
				up = !up;
			}	
			
		}
		
		this.origin.addTranslation(move.x, move.y, move.z);
	}

}
