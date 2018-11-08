
#ifdef GL_ES
precision mediump float;
#endif

attribute vec3 a_position;
attribute vec3 a_normal;
attribute vec2 a_uv;

uniform mat4 u_modelMatrix;
uniform mat4 u_viewMatrix;
uniform mat4 u_projectionMatrix;

uniform vec4 u_eyePosition;
uniform vec4 u_lightPosition;

uniform float u_fogDensity;
uniform float u_fogGradient;

varying vec2 v_uv;
varying vec4 v_normal;
varying vec4 v_s;
varying vec4 v_h;
varying float v_visibility;

void main()
{
	// Global coordinate.
	vec4 position = u_modelMatrix * vec4(a_position, 1.0);
	vec4 normal = u_modelMatrix * vec4(a_normal, 0.0);
	
	vec4 positionRelativeToCamera = u_viewMatrix * position;
	float distance = length(positionRelativeToCamera);
	v_visibility = exp(-pow((distance * u_fogDensity), u_fogGradient));
	v_visibility = clamp(v_visibility, 0.0, 1.0);
	
	v_normal = normal;
	v_uv = a_uv;
	
	vec4 v = normalize(u_eyePosition - position);
	v_s = normalize(u_lightPosition - position);
	v_h = v_s + v; 
	
	// Local coordinates.

	gl_Position = u_projectionMatrix * positionRelativeToCamera;
}