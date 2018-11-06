
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

struct DirLight {
	vec4 direction;
	
	vec4 ambient;
	vec4 diffuse;
	vec4 specular;
};

struct Light {
	vec4 position;
	
	vec4 ambient;
	vec4 diffuse;
	vec4 specular;
};

struct PointLight {
	vec4 position;
	
	vec4 ambient;
	vec4 diffuse;
	vec4 specular;
	
	float constant;
	float linear;
	float quadratic;
};

struct SpotLight {
	vec4 position;
	vec4 direction;
	
	float cutOff;
	float outerCutOff;
  
    float constant;
    float linear;
    float quadratic;
  
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
};

uniform float u_usesDiffuseTexture;
uniform sampler2D u_diffuseTexture;

uniform float u_usesSpecularTexture;
uniform sampler2D u_specularTexture;

uniform vec4 u_lightColour;
uniform Material u_material;

uniform vec4 u_globalAmbient;

varying vec2 v_uv;
varying vec4 v_normal;
varying vec4 v_s;
varying vec4 v_h;

/*
vec4 posLightValue(Light light, vec4 normal, vec4 fragmentPosition, vec4 v);
vec4 dirLightValue(DirLight light, vec4 normal, vec4 v);
vec4 pointLightValue(PointLight light, vec4 normal, vec4 fragmentPosition, vec4 v);
vec4 spotLightValue(SpotLight light, vec4 normal, vec4 fragmentPosition, vec4 v);
*/

void main()
{
	vec4 materialDiffuse;
	if(u_usesDiffuseTexture >= 1.0) {
		materialDiffuse = texture2D(u_diffuseTexture, v_uv);
	} else {
		materialDiffuse = u_material.diffuse;
	}
	
	vec4 materialSpecular;
	if(u_usesSpecularTexture >= 1.0) {
		materialDiffuse = texture2D(u_specularTexture, v_uv);
	} else {
		materialSpecular = u_material.specular;
	}
	
	float length_s = length(v_s);
	float length_normal = length(v_normal);
	float length_h = length(v_h);

	float lambert = max(0.0, dot(v_normal, v_s) / (length_normal * length_s));
	float phong = max(0.0, dot(v_normal, v_h) / (length_normal * length_h));
	
	vec4 ambient = u_globalAmbient * u_material.ambient;
	vec4 diffuse = lambert * u_lightColour * materialDiffuse;
	vec4 specular = pow(phong, u_material.shininess) * u_lightColour * materialSpecular;
	
	gl_FragColor = ambient + diffuse + specular + u_material.emission;
}

/*
vec4 posLightValue(Light light, vec4 normal, vec4 fragmentPosition, vec4 v) {
	vec4 s = normalize(u_treasureLight.position - v_position);
	vec4 h = normalize(s + v);
	
	float lambert = max(0.0, dot(s, normal) / (length(s) * length(normal)));
	float phong = max(0.0, dot(h, normal) / (length(s) * length(normal)));
	
	vec4 ambient = light.ambient * u_material.ambient;
	vec4 diffuse = lambert * light.diffuse * u_material.diffuse;
	vec4 specular = pow(phong, u_material.shininess) * light.specular * u_material.specular;

	return (ambient + diffuse + specular);	
}

vec4 dirLightValue(DirLight light, vec4 normal, vec4 v) {
	vec4 lightDirection = normalize(-light.direction);
    vec4 reflectDirection = reflect(-lightDirection, normal);

    float lambert = max(0.0, dot(lightDirection, normal) / (length(lightDirection) * length(normal)));
	float phong = max(0.0, dot(reflectDirection, v) / (length(reflectDirection) * length(v)));
	
    vec4 ambient  = light.ambient  * u_material.ambient;
    vec4 diffuse  = lambert * light.diffuse  * u_material.diffuse;
    vec4 specular = pow(phong, u_material.shininess) * light.specular * u_material.specular;
    
    return (ambient + diffuse + specular);
}

vec4 pointLightValue(PointLight light, vec4 normal, vec4 fragmentPosition, vec4 v) {
	vec4 lightDirection = normalize(light.position - fragmentPosition);
	vec4 reflectionDirection = reflect(-lightDirection, normal);
	
	float lambert = max(0.0, dot(lightDirection, normal) / (length(lightDirection) * length(normal)));
	float phong = max(0.0, dot(reflectionDirection, v) / (length(reflectionDirection) * length(v)));
	
	float distance = length(light.position - fragmentPosition);
	float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * (distance * distance));
	
	vec4 ambient  = light.ambient  * u_material.ambient;
    vec4 diffuse  = lambert * light.diffuse  * u_material.diffuse;
    vec4 specular = pow(phong, u_material.shininess) * light.specular * u_material.specular;
    ambient  *= attenuation;
    diffuse  *= attenuation;
    specular *= attenuation;
    
    return (ambient + diffuse + specular);
}

vec4 spotLightValue(SpotLight light, vec4 normal, vec4 fragmentPosition, vec4 v) {
	vec4 lightDirection = normalize(light.position - fragmentPosition);
	vec4 reflectionDirection = reflect(-lightDirection, normal);
	
	float lambert = max(0.0, dot(lightDirection, normal) / (length(lightDirection) * length(normal)));
	float phong = max(0.0, dot(reflectionDirection, v) / (length(reflectionDirection) * length(v)));
	
	float distance = length(light.position - fragmentPosition);
	float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * (distance * distance));
	
	float theta = dot(lightDirection, normalize(-light.direction));
	float epsilon = light.cutOff - light.outerCutOff;
	float intensity = clamp((theta - light.outerCutOff) / epsilon, 0.0, 1.0);
	
	vec4 ambient  = light.ambient  * u_material.ambient;
    vec4 diffuse  = lambert * light.diffuse  * u_material.diffuse;
    vec4 specular = pow(phong, u_material.shininess) * light.specular * u_material.specular;
    ambient  *= attenuation * intensity;
    diffuse  *= attenuation * intensity;
    specular *= attenuation * intensity;
    
    return (ambient + diffuse + specular);
}
*/