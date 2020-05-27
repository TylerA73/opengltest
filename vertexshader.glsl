#version 330 core

layout(location = 0) in vec3 vertexPosition_modelspace;
layout(location = 1) in vec3 vertexColor;
layout(location = 2) in vec3 vertexPosition_triangle;
layout(location = 3) in vec3 triangleColor;

uniform mat4 MVP;

out vec3 fragmentColor;

void main() {
    if (vertexPosition_modelspace != 0) {
        gl_Position = MVP * vec4(vertexPosition_modelspace, 1);
        fragmentColor = vertexColor;
    }
    else {
        gl_Position = MVP * vec4(vertexPosition_triangle, 1);
        fragmentColor = triangleColor;
    }
}