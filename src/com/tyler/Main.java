package com.tyler;

import org.joml.*;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import java.io.*;
import java.lang.Math;
import java.nio.*;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {

    public static void main(String[] args) {
        GLFWErrorCallback.createPrint(System.err).set();
        Scanner keyboard = new Scanner(System.in);

        String vertexShaderPath, fragmentShaderPath;
        vertexShaderPath = "vertexshader.glsl";
        fragmentShaderPath = "fragmentshader.glsl";

        Camera camera = new Camera();
        camera.setEye(0f, 0f, 5f);
        camera.setUpY(0.5f);
        camera.setFoV(90);

        Cube cube = new Cube("Cube");
        Cube cube2 = new Cube("Cube 2");

        cube2.setColor(new float[] {
                1f,  0f,  0f,// start
                1f,  0f,  0f,
                1f,  0f,  0f,// end
                1f,  0f,  0f,
                1f,  0f,  0f,
                1f,  0f,  0f,
                1f,  0f,  0f,// start
                1f,  0f,  0f,
                1f,  0f,  0f,// end
                1f,  0f,  0f,
                1f,  0f,  0f,
                1f,  0f,  0f,
                1f,  0f,  0f,// start
                1f,  0f,  0f,
                1f,  0f,  0f,// end
                1f,  0f,  0f,
                1f,  0f,  0f,
                1f,  0f,  0f,
                1f,  0f,  0f,// start
                1f,  0f,  0f,
                1f,  0f,  0f,// end
                1f,  0f,  0f,
                1f,  0f,  0f,
                1f,  0f,  0f,
                1f,  0f,  0f,// start
                1f,  0f,  0f,
                1f,  0f,  0f,// end
                1f,  0f,  0f,
                1f,  0f,  0f,
                1f,  0f,  0f,
                1f,  0f,  0f,// start
                1f,  0f,  0f,
                1f,  0f,  0f,// end
                1f,  0f,  0f,
                1f,  0f,  0f,
                1f,  0f,  0f
        });

        AtomicReference<Float> c1x = new AtomicReference<>((float) 0);
        AtomicReference<Float> c1y = new AtomicReference<>((float) 0);
        float c1z;
        AtomicReference<Float> c2x = new AtomicReference<>((float) 0);
        AtomicReference<Float> c2y = new AtomicReference<>((float) 0);
        float c2z;
        c1x.set(0.5f);
        c1y.set(-0.5f);

        c2x.set(-1.5f);
        c2y.set(c1y.get());

        c1z = c2z = 0f;

        AtomicReference<Matrix4f> mvp = new AtomicReference<>(camera.getModelViewProjection());

        if (!glfwInit()) {
            throw new IllegalStateException("Could not initialize GLFW");
        }

        long window = glfwCreateWindow(1024, 768, "Tutorial", NULL, NULL);

        if (window == NULL) {
            throw new RuntimeException("Could not create GLFW Window");
        }

        glfwMakeContextCurrent(window);

        glfwSetInputMode(window, GLFW_STICKY_KEYS, GL_TRUE);

        GL.createCapabilities();

        glClearColor(0, 1, 0, 0);

        // Vertex Array Object Cube
        int vaoID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoID);

        // Vertex Buffer Object Cube
        int vboID = glGenBuffers();
        GL30.glBindBuffer(GL_ARRAY_BUFFER, vboID);
        GL30.glBufferData(
                GL_ARRAY_BUFFER, cube.getVertices(), GL_STATIC_DRAW
        );
        GL30.glBufferData(
                GL_ARRAY_BUFFER, cube2.getVertices(), GL_STATIC_DRAW
        );


        // Color Buffer
        int cbID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, cbID);
        glBufferData(
                GL_ARRAY_BUFFER, cube.getColor(), GL_STATIC_DRAW
        );


        int programID = loadShaders(vertexShaderPath, fragmentShaderPath);

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);

        AtomicInteger pressed = new AtomicInteger();
        AtomicInteger mode = new AtomicInteger();

        glfwSetKeyCallback(window, (long win, int key, int scancode, int action, int mods) -> {
            if (action == GLFW_PRESS) {
                if (key == GLFW_KEY_RIGHT) {
                    c2x.set(c2x.get() + 0.1f);
                    System.out.println("Right pressed");
                } else if (key == GLFW_KEY_LEFT) {
                    c2x.set(c2x.get() - 0.1f);
                } else if(key == GLFW_KEY_UP) {
                    c2y.set(c2y.get() + 0.1f);
                } else if (key == GLFW_KEY_DOWN) {
                    c2y.set(c2y.get() - 0.1f);
                } else if (key == GLFW_KEY_KP_4) {
                    c1x.set(c1x.get() - 0.1f);
                } else if (key == GLFW_KEY_KP_6) {
                    c1x.set(c1x.get() + 0.1f);
                } else if (key == GLFW_KEY_KP_8) {
                    c1y.set(c1y.get() + 0.1f);
                } else if (key == GLFW_KEY_KP_2) {
                    c1y.set(c1y.get() - 0.1f);
                } else {
                    mode.set(key);
                }
            }
        });

        glfwSetScrollCallback(window, (long win, double xoffset, double yoffset) -> {
            switch (mode.get()) {
                case GLFW_KEY_Q:
                    // field of view
                    if (yoffset > 0) {
                        camera.setFoV(camera.getFoV() + 0.1f);
                    } else if (yoffset < 0) {
                        camera.setFoV(camera.getFoV() - 0.1f);
                    }
                    break;
                case GLFW_KEY_W:
                    // zNear
                    if (yoffset > 0) {
                        camera.setzNear(camera.getzNear() + 0.1f);
                    } else if (yoffset < 0) {
                        camera.setzNear(camera.getzNear() - 0.1f);
                    }
                    break;
                case GLFW_KEY_E:
                    // zFar
                    if (yoffset > 0) {
                        camera.setzFar(camera.getzFar() + 0.1f);
                    } else if (yoffset < 0) {
                        camera.setzFar(camera.getzFar() - 0.1f);
                    }
                    break;
                case GLFW_KEY_A:
                    // eyeX
                    if (yoffset > 0) {
                        camera.setEyeX(camera.getEyeX() + 0.1f);
                    } else if (yoffset < 0) {
                        camera.setEyeX(camera.getEyeX() - 0.1f);
                    }
                    break;
                case GLFW_KEY_S:
                    // eyeY
                    if (yoffset > 0) {
                        camera.setEyeY(camera.getEyeY() + 0.1f);
                    } else if (yoffset < 0) {
                        camera.setEyeY(camera.getEyeY() - 0.1f);
                    }
                    break;
                case GLFW_KEY_D:
                    // eyeZ
                    if (yoffset > 0) {
                        camera.setEyeZ(camera.getEyeZ() + 0.1f);
                    } else if (yoffset < 0) {
                        camera.setEyeZ(camera.getEyeZ() - 0.1f);
                    }
                    break;
                case GLFW_KEY_Z:
                    // centerX
                    if (yoffset > 0) {
                        camera.setCenterX(camera.getCenterX() + 0.1f);
                    } else if (yoffset < 0) {
                        camera.setCenterX(camera.getCenterX() - 0.1f);
                    }
                    break;
                case GLFW_KEY_X:
                    // centerY
                    if (yoffset > 0) {
                        camera.setCenterY(camera.getCenterY() + 0.1f);
                    } else if (yoffset < 0) {
                        camera.setCenterY(camera.getCenterY() - 0.1f);
                    }
                    break;
                case GLFW_KEY_C:
                    // centerZ
                    if (yoffset > 0) {
                        camera.setCenterZ(camera.getCenterZ() + 0.1f);
                    } else if (yoffset < 0) {
                        camera.setCenterZ(camera.getCenterZ() - 0.1f);
                    }
                    break;
                case GLFW_KEY_I:
                    // upX
                    if (yoffset > 0) {
                        camera.setUpX(camera.getUpX() + 0.1f);
                    } else if (yoffset < 0) {
                        camera.setUpX(camera.getUpX() - 0.1f);
                    }
                    break;
                case GLFW_KEY_O:
                    // upY
                    if (yoffset > 0) {
                        camera.setUpY(camera.getUpY() + 0.1f);
                    } else if (yoffset < 0) {
                        camera.setUpY(camera.getUpY() - 0.1f);
                    }
                    break;
                case GLFW_KEY_P:
                    // upZ
                    if (yoffset > 0) {
                        camera.setUpZ(camera.getUpZ() + 0.1f);
                    } else if (yoffset < 0) {
                        camera.setUpZ(camera.getUpZ() - 0.1f);
                    }
                    break;
                default:
                    break;
            }

            mvp.set(camera.getModelViewProjection());



        });

        do {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            float[] s = {
                    1f, 0f, 0f, 0f,
                    0f, 1f, 0f, 0f,
                    0f,0f,1f,0f,
                    0f,0f,0f,1f
            };

            Vector3f position = new Vector3f(c1x.get(), c1y.get(), c1z);
            Quaternionf rotation = new Quaternionf(new AxisAngle4f((float) Math.toRadians(180), new Vector3f(1f,0f, 0f)));

            Matrix4f mat4 = new Matrix4f();
            mat4.translate(position);
            mat4.rotate(rotation);

            int transform = glGetUniformLocation(programID, "transform");
            float[] m = new float[16];
            glUniformMatrix4fv(transform, false, mat4.get(m));


            int matrixID = glGetUniformLocation(programID, "MVP");
            FloatBuffer fb = BufferUtils.createFloatBuffer(16);
            glUseProgram(programID);
            glUniformMatrix4fv(matrixID, false, mvp.get().get(fb));

            long offset = 0; // array buffer offset

            //draw cube

            GL30.glEnableVertexAttribArray(0);
            GL30.glBindBuffer(GL_ARRAY_BUFFER, vboID);

            GL30.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, offset);
            GL30.glDrawArrays(GL_TRIANGLES, 0, 12*3);
            GL30.glDisableVertexAttribArray(0);

            // color cube
            glEnableVertexAttribArray(1);
            glBindBuffer(GL_ARRAY_BUFFER, cbID);
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, offset);

            // draw second cube

            position = new Vector3f(c2x.get(), c2y.get(), c2z);

            mat4 = new Matrix4f();
            mat4.translate(position);

            m = new float[16];
            glUniformMatrix4fv(transform, false, mat4.get(m));

            glEnableVertexAttribArray(0);
            glBindBuffer(GL_ARRAY_BUFFER, vboID);

            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 12*3);
            glDrawArrays(GL_TRIANGLES, 0, 12*3);
            glDisableVertexAttribArray(0);

            glfwSwapBuffers(window);
            glfwPollEvents();

        } while (!glfwWindowShouldClose(window));
    }

    private static int loadShaders (String vertex_file_path, String fragment_file_path) {
        int vertexShaderID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        int fragmentShaderID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        String vertexShaderCode = null;
        String fragmentShaderCode = null;

        try {
            FileReader fr = new FileReader(vertex_file_path);

            char[] dest = new char[1024];
            int status = fr.read(dest);

            if (status == -1) {
                throw new RuntimeException("Could not load vertex shader");
            }
            vertexShaderCode = new String(dest);

            dest = new char[1024];
            fr = new FileReader(fragment_file_path);
            status = fr.read(dest);

            if (status == -1) {
                throw new RuntimeException("Could not load fragment shader");
            }
            fragmentShaderCode = new String(dest);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        glShaderSource(vertexShaderID, vertexShaderCode);
        glCompileShader(vertexShaderID);

        int status = glGetShaderi(vertexShaderID, GL_COMPILE_STATUS);
        if (status != GL_TRUE) {
            System.out.println("Failed to get vertex shader");
            System.out.println(vertexShaderCode);
            throw new RuntimeException(glGetShaderInfoLog(vertexShaderID));
        }

        glShaderSource(fragmentShaderID, fragmentShaderCode);
        glCompileShader(fragmentShaderID);

        status = glGetShaderi(fragmentShaderID, GL_COMPILE_STATUS);
        if (status != GL_TRUE) {
            System.out.println("Failed to get fragment shader");
            System.out.println(fragmentShaderCode);
            throw new RuntimeException(glGetShaderInfoLog(fragmentShaderID));
        }

        int shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShaderID);
        glAttachShader(shaderProgram, fragmentShaderID);
        GL30.glBindFragDataLocation(shaderProgram, 0, "color");
        glLinkProgram(shaderProgram);

        status = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if (status != GL_TRUE) {
            throw new RuntimeException(glGetProgramInfoLog(shaderProgram));
        }

        return shaderProgram;

    }

}
