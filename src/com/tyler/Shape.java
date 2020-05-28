package com.tyler;

import org.joml.Matrix4f;

/**
 * Shape
 *
 * Author: Tyler Arseneault
 * Description: Class that represents a basic shape
 */
public class Shape {

    private String name;
    private float[] vertices;
    private float[] color;

    Shape() {
        this.name = null;
        this.vertices = null;
        this.color = null;
    }

    Shape(String name) {
        this.name = name;
        this.vertices = null;
        this.color = null;
    }

    Shape(String name, float[] vertices, float[] color) {
        this.name = name;
        this.vertices = vertices;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float[] getVertices() {
        return vertices;
    }

    public void setVertices(float[] vertices) {
        this.vertices = vertices;
    }

    public float[] getColor() {
        return color;
    }

    public void setColor(float[] color) {
        this.color = color;
    }
}
