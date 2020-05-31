#version 330 core

layout(location = 0) in vec3 vertexPosition_modelspace;
layout(location = 1) in vec3 vertexColor;
layout(location = 2) in vec3 vertexPosition_triangle;
layout(location = 3) in vec3 triangleColor;

uniform mat4 MVP;
uniform mat4 transform;

out vec3 fragmentColor;

void main() {
    gl_Position = MVP * transform * vec4(vertexPosition_modelspace, 1);
    fragmentColor = vertexColor;
}