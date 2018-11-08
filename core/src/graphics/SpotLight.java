package graphics;

import utils.Point3D;
import utils.Vector3D;

public class SpotLight {
	private Point3D position;
	private Vector3D attenuation;
	private Colour colour;
	
	public SpotLight(Point3D position, Colour colour, Vector3D attenuation) {
		super();
		this.position = position;
		this.attenuation = attenuation;
		this.colour = colour;
	}

	public Point3D getPosition() {
		return this.position;
	}
	public Vector3D getAttenuation() {
		return this.attenuation;
	}
	public Colour getColour() {
		return this.colour;
	}
	
	public void setColour(Colour colour) {
		this.colour.r = colour.r;
		this.colour.g = colour.g;
		this.colour.b = colour.b;
		this.colour.a = colour.a;
	}
}
