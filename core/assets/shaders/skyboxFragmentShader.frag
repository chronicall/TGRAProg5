
#ifdef GL_ES
precision mediump float;
#endif

uniform samplerCube u_cubeMap;

varying vec3 v_uv;

void main() {
	gl_FragColor = textureCube(u_cubeMap, v_uv);
}