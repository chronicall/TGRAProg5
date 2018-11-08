
#ifdef GL_ES
precision mediump float;
#endif

struct Material {
	vec4 ambient;
	vec4 diffuse;
	vec4 specular;
	float shininess;
	
	vec4 emission;
};

struct SpotLight {
	vec4 position;
	//vec3 cutOff;
	vec3 attenuation;  
    vec4 colour;
};

struct Terrain {
	sampler2D terrainTexture;
	sampler2D rTexture;
	sampler2D gTexture;
	sampler2D bTexture;
	sampler2D blendMap;
};
#define RING_SPOTLIGHTS 5

uniform float u_usesDiffuseTexture;
uniform sampler2D u_diffuseTexture;
uniform float u_usesSpecularTexture;
uniform sampler2D u_specularTexture;

uniform vec4 u_lightColour;
uniform vec4 u_skyColour;
uniform Material u_material;
uniform SpotLight u_ringLights[RING_SPOTLIGHTS];
uniform float u_shineDamper;
uniform float u_reflectivity;

uniform vec4 u_globalAmbient;

uniform float u_isTerrain;
uniform Terrain u_terrain;
uniform float u_textureTilingValue;

varying vec4 v_lightVector[RING_SPOTLIGHTS];
varying vec4 v_normal;
varying vec2 v_uv;
varying vec4 v_s;
varying vec4 v_h;
varying vec4 v_eye;
varying float v_visibility;

//vec4 spotLightValue(SpotLight light, vec4 normal, vec4 fragmentPosition, vec4 v);

void main()
{
	vec4 unitNormal = normalize(v_normal);
	vec4 unitVectorToCamera = normalize(v_eye);
	
	// Terrain colours with blended textures
	vec4 terrainColour = vec4(1.0); 
	if (u_isTerrain == 1.0)	{
		vec4 blendMapColour = texture2D(u_terrain.blendMap, v_uv);
		float terrainTextureAmount = 1.0 - (blendMapColour.r + blendMapColour.g + blendMapColour.b);
		vec2 uvTiled = v_uv * u_textureTilingValue;
		vec4 terrainTextureColour = texture2D(u_terrain.terrainTexture, uvTiled) * terrainTextureAmount;
		vec4 rTextureColour = texture2D(u_terrain.rTexture, uvTiled) * blendMapColour.r;
		vec4 gTextureColour = texture2D(u_terrain.gTexture, uvTiled) * blendMapColour.g;
		vec4 bTextureColour = texture2D(u_terrain.bTexture, uvTiled) * blendMapColour.b;
		
		terrainColour = terrainTextureColour + rTextureColour + gTextureColour + bTextureColour;
	}

	vec4 diffuseTotal = vec4(0.0);
	vec4 specularTotal = vec4(0.0);	
	for (int i = 0; i < RING_SPOTLIGHTS; i++) {
		float distance = length(v_lightVector[i]);
		float attenuationFactor = u_ringLights[i].attenuation.x + (u_ringLights[i].attenuation.y * distance) + (u_ringLights[i].attenuation.z * distance * distance);
		vec4 unitLightVector = normalize(v_lightVector[i]);
		float nDot1 = dot(unitNormal, unitLightVector);
		float brightness = max(nDot1, 0.0);
		vec4 lightDirection = -unitLightVector;
		vec4 reflectedLightDirection = reflect(lightDirection, unitNormal);
		float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
		specularFactor = max(specularFactor, 0.0);
		float dampedFactor = pow(specularFactor, u_shineDamper); 
		diffuseTotal += (brightness * u_ringLights[i].colour) / attenuationFactor;
		specularTotal += (dampedFactor * u_reflectivity * u_ringLights[i].colour) / attenuationFactor;
	}
	diffuseTotal = max(diffuseTotal, 0.2);
	
	// Material diffuse colour
	vec4 materialDiffuse;
	if (u_usesDiffuseTexture == 1.0) {
		materialDiffuse = texture2D(u_diffuseTexture, v_uv);
	} else {
		materialDiffuse = u_material.diffuse;
	}
	materialDiffuse = materialDiffuse * terrainColour;
	
	vec4 materialSpecular = u_material.specular;
	
	float length_s = length(v_s);
	float length_normal = length(v_normal);
	float length_h = length(v_h);

	float lambert = max(0.0, dot(v_normal, v_s) / (length_normal * length_s));
	float phong = max(0.0, dot(v_normal, v_h) / (length_normal * length_h));
	
	vec4 ambient = u_globalAmbient * u_material.ambient;
	vec4 diffuse = lambert * u_lightColour * materialDiffuse + diffuseTotal * materialDiffuse;
	vec4 specular = pow(phong, u_material.shininess) * u_lightColour * materialSpecular + specularTotal * materialSpecular;
	
	gl_FragColor = ambient + diffuse + specular + u_material.emission;
	gl_FragColor = mix(u_skyColour, gl_FragColor, v_visibility);
}

/*
vec4 spotLightValue(SpotLight light, vec4 normal, vec4 fragmentPosition, vec4 v) {
	vec4 lightDirection = normalize(light.position - fragmentPosition);
	vec4 reflectionDirection = reflect(-lightDirection, normal);
	
	float lambert = max(0.0, dot(lightDirection, normal) / (length(lightDirection) * length(normal)));
	float phong = max(0.0, dot(reflectionDirection, v) / (length(reflectionDirection) * length(v)));
	
	float distance = length(light.position - fragmentPosition);
	float attenuation = 1.0 / (light.attenuation.x + light.attenuation.y * distance + light.attenuation.z * (distance * distance));
	
	float theta = dot(lightDirection, normalize(-lightDirection));
	float epsilon = light.cutOff.x - light.cutOff.y;
	float intensity = clamp((theta - light.cutOff.y) / epsilon, 0.0, 1.0);
	
	vec4 ambient  = light.ambient  * u_material.ambient;
    vec4 diffuse  = lambert * light.diffuse  * u_material.diffuse;
    vec4 specular = pow(phong, u_material.shininess) * light.specular * u_material.specular;
    ambient  *= attenuation * intensity;
    diffuse  *= attenuation * intensity;
    specular *= attenuation * intensity;
    
    return (ambient + diffuse + specular);
}*/