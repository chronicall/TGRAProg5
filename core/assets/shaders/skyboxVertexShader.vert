
#ifdef GL_ES
precision mediump float;
#endif

attribute vec3 a_position;

uniform mat4 u_projectionMatrix;
uniform mat4 u_viewMatrix;

varying vec3 v_uv;

void main() {
	gl_Position = u_projectionMatrix * u_viewMatrix * vec4(a_position, 1);
	v_uv = a_position;
}