package utils;

public class Quaternion {
	public float x;
	public float y;
	public float z;
	public float w;
	
	public Quaternion() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.w = 0;
	}
	
	public Quaternion(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public String toString() {
		return "(Point: {X: " + this.x + ", Y: " + this.y + ", Z: " + this.z + ", W: " + this.w + "})";
	}

	public boolean equals(Quaternion q) {
		return this.x == q.x && this.y == q.y && this.z == q.z && this.w == q.w;
	}
}
