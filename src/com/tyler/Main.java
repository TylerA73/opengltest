package com.tyler;

import org.joml.*;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.opengl.*;

import java.io.*;
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
        camera.setEye(4f, 3f, 3f);
        //camera.setUpX(0.5f);
        camera.setUpY(0.5f);
        //camera.setUpZ(1f);
        camera.setFoV(90);

        Shape shape = new Shape("Cube");
        Shape shape2 = new Shape("Triangle");

        AtomicReference<Matrix4f> mvp = new AtomicReference<>(camera.getModelViewProjection());

        final float[] g_vertex_buffer_data = {
                -1.0f,-1.0f,-1.0f, // triangle 1 : begin
                -1.0f,-1.0f, 1.0f,
                -1.0f, 1.0f, 1.0f, // triangle 1 : end
                1.0f, 1.0f,-1.0f, // triangle 2 : begin
                -1.0f,-1.0f,-1.0f,
                -1.0f, 1.0f,-1.0f, // triangle 2 : end
                1.0f,-1.0f, 1.0f,
                -1.0f,-1.0f,-1.0f,
                1.0f,-1.0f,-1.0f,
                1.0f, 1.0f,-1.0f,
                1.0f,-1.0f,-1.0f,
                -1.0f,-1.0f,-1.0f,
                -1.0f,-1.0f,-1.0f,
                -1.0f, 1.0f, 1.0f,
                -1.0f, 1.0f,-1.0f,
                1.0f,-1.0f, 1.0f,
                -1.0f,-1.0f, 1.0f,
                -1.0f,-1.0f,-1.0f,
                -1.0f, 1.0f, 1.0f,
                -1.0f,-1.0f, 1.0f,
                1.0f,-1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f,-1.0f,-1.0f,
                1.0f, 1.0f,-1.0f,
                1.0f,-1.0f,-1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f,-1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f,-1.0f,
                -1.0f, 1.0f,-1.0f,
                1.0f, 1.0f, 1.0f,
                -1.0f, 1.0f,-1.0f,
                -1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                -1.0f, 1.0f, 1.0f,
                1.0f,-1.0f, 1.0f
        };

        final float[] g_vertex_buffer_data2 = {
                -1.5f,0.0f,1.0f, // triangle 1 : begin
                0.0f,0.0f, 3.0f,
                1.0f, 2.0f, 3.0f, // triangle 1 : end
        };

        // One color for each vertex. They were generated randomly.
        final float[] g_color_buffer_data = {
                0.583f,  0.771f,  0.014f,
                0.609f,  0.115f,  0.436f,
                0.327f,  0.483f,  0.844f,
                0.822f,  0.569f,  0.201f,
                0.435f,  0.602f,  0.223f,
                0.310f,  0.747f,  0.185f,
                0.597f,  0.770f,  0.761f,
                0.559f,  0.436f,  0.730f,
                0.359f,  0.583f,  0.152f,
                0.483f,  0.596f,  0.789f,
                0.559f,  0.861f,  0.639f,
                0.195f,  0.548f,  0.859f,
                0.014f,  0.184f,  0.576f,
                0.771f,  0.328f,  0.970f,
                0.406f,  0.615f,  0.116f,
                0.676f,  0.977f,  0.133f,
                0.971f,  0.572f,  0.833f,
                0.140f,  0.616f,  0.489f,
                0.997f,  0.513f,  0.064f,
                0.945f,  0.719f,  0.592f,
                0.543f,  0.021f,  0.978f,
                0.279f,  0.317f,  0.505f,
                0.167f,  0.620f,  0.077f,
                0.347f,  0.857f,  0.137f,
                0.055f,  0.953f,  0.042f,
                0.714f,  0.505f,  0.345f,
                0.783f,  0.290f,  0.734f,
                0.722f,  0.645f,  0.174f,
                0.302f,  0.455f,  0.848f,
                0.225f,  0.587f,  0.040f,
                0.517f,  0.713f,  0.338f,
                0.053f,  0.959f,  0.120f,
                0.393f,  0.621f,  0.362f,
                0.673f,  0.211f,  0.457f,
                0.820f,  0.883f,  0.371f,
                0.982f,  0.099f,  0.879f
        };

        final float[] g_triangle_color_buffer = {
                0.5f, 0.5f, 0f,
                0f, 0.5f, 0.5f,
                0.5f, 0f, 0.5f
        };

        shape.setVertices(g_vertex_buffer_data);
        shape.setColor(g_color_buffer_data);

        shape2.setVertices(g_vertex_buffer_data2);
        shape2.setColor(g_triangle_color_buffer);

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

        glClearColor(0, 0, 0, 0);

        // Vertex Array Object Cube
        int vaoID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoID);

        // Vertex Buffer Object Cube
        int vboID = glGenBuffers();
        GL30.glBindBuffer(GL_ARRAY_BUFFER, vboID);
        GL30.glBufferData(
                GL_ARRAY_BUFFER, shape.getVertices(), GL_STATIC_DRAW
        );

        int vaotID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaotID);

        int vbotID = glGenBuffers();
        GL30.glBindBuffer(GL_ARRAY_BUFFER, vbotID);
        GL30.glBufferData(
                GL_ARRAY_BUFFER, shape2.getVertices(), GL_STATIC_DRAW
        );

        // Color Buffer
        int cbID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, cbID);
        glBufferData(
                GL_ARRAY_BUFFER, shape.getColor(), GL_STATIC_DRAW
        );

        // Color Buffer Triangle
        int cbtID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, cbtID);
        glBufferData(
                GL_ARRAY_BUFFER, shape2.getColor(), GL_STATIC_DRAW
        );

        int programID = loadShaders(vertexShaderPath, fragmentShaderPath);

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);

        AtomicInteger pressed = new AtomicInteger();
        AtomicInteger mode = new AtomicInteger();


        glfwSetKeyCallback(window, (long win, int key, int scancode, int action, int mods) -> {
            if (action == GLFW_PRESS) {
                if (key == GLFW_KEY_RIGHT) {
                    // right translation
                } else if (key == GLFW_KEY_LEFT) {
                    // left translation
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


            int matrixID = glGetUniformLocation(programID, "MVP");

            glUseProgram(programID);
            FloatBuffer fb = BufferUtils.createFloatBuffer(16);
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

            // draw triangle
            GL30.glEnableVertexAttribArray(2);
            GL30.glBindBuffer(GL_ARRAY_BUFFER, vbotID);

            GL30.glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, offset);
            GL30.glDrawArrays(GL_TRIANGLES, 0, 3);
            GL30.glDisableVertexAttribArray(2);

            // color triangle
            glEnableVertexAttribArray(3);
            glBindBuffer(GL_ARRAY_BUFFER, cbtID);
            glVertexAttribPointer(3, 3, GL_FLOAT, false, 0, offset);


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
