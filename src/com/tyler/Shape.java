package com.tyler;

import org.joml.Matrix4f;
import org.joml.Vector3f;

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
    private Matrix4f projection;
    private Matrix4f view;
    private Matrix4f model;

    Shape() {
        this.name = null;
        this.vertices = null;
        this.color = null;
        this.projection = new Matrix4f().perspective(
                (float) Math.toRadians(45.0),
                (float) 4/3,
                0.1f, 100f
        );
        this.view = new Matrix4f().lookAt(
                new Vector3f(4, 3, 3),
                new Vector3f(0, 0, 0),
                new Vector3f(0, 1, 0)
        );
        this.model = new Matrix4f().identity();
    }

    Shape(String name) {
        this.name = name;
        this.vertices = null;
        this.color = null;
        this.projection = new Matrix4f().perspective(
                (float) Math.toRadians(45.0),
                (float) 4/3,
                0.1f, 100f
        );
        this.view = new Matrix4f().lookAt(
                new Vector3f(4, 3, 3),
                new Vector3f(0, 1, 1),
                new Vector3f(0, 1, 0)
        );
        this.model = new Matrix4f().identity();
    }

    Shape(String name, float[] vertices, float[] color, Matrix4f proj, Matrix4f view) {
        this.name = name;
        this.vertices = vertices;
        this.color = color;
        this.projection = proj;
        this.view = view;
        this.model = new Matrix4f().identity();
    }

    Shape(String name, float[] vertices, float[] color, float[] proj, float[] view) {
        this.name = name;
        this.vertices = vertices;
        this.color = color;
        this.projection = new Matrix4f().set(proj);
        this.view = new Matrix4f().set(view);
        this.model = new Matrix4f().identity();
    }

    Shape(String name, float[] vertices, float[] color, Matrix4f proj, Matrix4f view, Matrix4f model) {
        this.name = name;
        this.vertices = vertices;
        this.color = color;
        this.projection = proj;
        this.view = view;
        this.model = model;
    }

    Shape(String name, float[] vertices, float[] color, float[] proj, float[] view, float[] model) {
        this.name = name;
        this.vertices = vertices;
        this.color = color;
        this.projection = new Matrix4f().set(proj);
        this.view = new Matrix4f().set(view);
        this.model = new Matrix4f().set(model);
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

    public Matrix4f getProjection() {
        return projection;
    }

    public void setProjection(Matrix4f projection) {
        this.projection = projection;
    }

    public void setProjection(float[] m) {
        this.projection.set(m);
    }

    public Matrix4f getView() {
        return view;
    }

    public void setView(Matrix4f view) {
        this.view = view;
    }

    public void setView(float[] m) {
        this.view.set(m);
    }

    public Matrix4f getModel() {
        return model;
    }

    public void setModel(Matrix4f model) {
        this.model = model;
    }

    public void setModel(float[] m) {
        this.model.set(m);
    }

    /**
     * Returns the mvp matrix of the Shape.
     *
     * @return Matrix4f mvp
     */
    public Matrix4f getModelViewProjectionMatrix() {
        // If any of them are null, we can't multiply
        if (model.equals(null) || view.equals(null) || projection.equals(null)) {
            return null;
        }

        // model * view * projection
        return projection.mul(view.mul(model));
    }
}
