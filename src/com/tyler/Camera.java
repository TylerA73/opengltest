package com.tyler;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

    // Projection values
    private float FoV; // field of view
    private float aspectRatio, zNear, zFar;

    // View values
    private float eyeX, eyeY, eyeZ;
    private float centerX, centerY, centerZ;
    private float upX, upY, upZ;

    Camera() {

        // Default the Projection's values
        this.FoV = (float) Math.toRadians(45); // default the camera's fovy to 45 degrees
        this.aspectRatio = (float) 4/3; // default the camera's aspect ratio to 4:3
        this.zNear = 0.1f;
        this.zFar = 100f;



        // Default the View's values
        this.eyeX = this.eyeY = this.eyeZ = 0f;
        this.centerX = this.centerY = this.centerZ = 0f;
        this.upX = this.upY = this.upZ = 0f;

    }

    public float getFoV() {
        return FoV;
    }

    public void setFoV(float foV) {
        FoV = foV;
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public float getzNear() {
        return zNear;
    }

    public void setzNear(float zNear) {
        this.zNear = zNear;
    }

    public float getzFar() {
        return zFar;
    }

    public void setzFar(float zFar) {
        this.zFar = zFar;
    }

    public float getEyeX() {
        return eyeX;
    }

    public void setEyeX(float eyeX) {
        this.eyeX = eyeX;
    }

    public float getEyeY() {
        return eyeY;
    }

    public void setEyeY(float eyeY) {
        this.eyeY = eyeY;
    }

    public float getEyeZ() {
        return eyeZ;
    }

    public void setEyeZ(float eyeZ) {
        this.eyeZ = eyeZ;
    }

    public void setEye(float eyeX, float eyeY, float eyeZ) {
        this.eyeX = eyeX;
        this.eyeY = eyeY;
        this.eyeZ = eyeZ;
    }

    public float getCenterX() {
        return centerX;
    }

    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    public float getCenterZ() {
        return centerZ;
    }

    public void setCenterZ(float centerZ) {
        this.centerZ = centerZ;
    }

    public float getUpX() {
        return upX;
    }

    public void setUpX(float upX) {
        this.upX = upX;
    }

    public float getUpY() {
        return upY;
    }

    public void setUpY(float upY) {
        this.upY = upY;
    }

    public float getUpZ() {
        return upZ;
    }

    public void setUpZ(float upZ) {
        this.upZ = upZ;
    }

    public Matrix4f getModelViewProjection() {
        Matrix4f projection = new Matrix4f().perspective(this.FoV, this.aspectRatio, this.zNear, this.zFar);
        Matrix4f view = new Matrix4f().lookAt(
                new Vector3f(this.eyeX, this.eyeY, this.eyeZ),
                new Vector3f(this.centerX, this.centerY, this.centerZ),
                new Vector3f(this.upX, this.upY, this.upZ)
        );
        Matrix4f model = new Matrix4f().identity();

        return projection.mul(view.mul(model));
    }
}
