package utils;

public class BezierMotion {

	private Point3D P1;
	private Point3D P2;
	private Point3D P3;
	private Point3D P4;
	private float startTime;
	private float endTime;
	
	public BezierMotion(Point3D P1, Point3D P2, Point3D P3, Point3D P4, float startTime, float endTime) {
		this.P1 = P1;
		this.P2 = P2;
		this.P3 = P3;
		this.P4 = P4;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public void getCurrentPosition(float currentTime, Point3D out_position) {
		if(currentTime < startTime) {
			out_position.x = P1.x;
			out_position.y = P1.y;
			out_position.z = P1.z;
		}
		
		else if(currentTime > endTime) {
			out_position.x = P4.x;
			out_position.y = P4.y;
			out_position.z = P4.z;
		}
		
		else {
			float t = (currentTime - startTime) / (endTime - startTime);
			
			out_position.x = (1-t)*(1-t)*(1-t)*P1.x + 3*(1-t)*(1-t)*t*P2.x + 3*(1-t)*t*t*P3.x + t*t*t*P4.x;
			out_position.y = (1-t)*(1-t)*(1-t)*P1.y + 3*(1-t)*(1-t)*t*P2.y + 3*(1-t)*t*t*P3.y + t*t*t*P4.y;
			out_position.z = (1-t)*(1-t)*(1-t)*P1.z + 3*(1-t)*(1-t)*t*P2.z + 3*(1-t)*t*t*P3.z + t*t*t*P4.z;
		}
	}
}
