package graphics;

public class Material {
	public String id;
	public Colour ambient;
	public Colour diffuse;
	public Colour specular;
	public Colour emission;
	public float opacity;
	public float shininess;

	public Material()
	{
		ambient = new Colour(0.0f, 0.0f, 0.0f);
		diffuse = new Colour(0.0f, 0.0f, 0.0f);
		specular = new Colour(0.0f, 0.0f, 0.0f);
		emission = new Colour(0.0f, 0.0f, 0.0f);
		opacity = 1.0f;
		shininess = 1.0f;
	}
}
