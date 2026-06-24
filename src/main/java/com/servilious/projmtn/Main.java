package com.servilious.projmtn;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL33.*;

public class Main {
    private Window window;
    private int vao, vbo;
    private float positions[] = {
            //Bottom Left (BL) triangle
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.5f, 0.5f, 0.0f,
            //Upper Right (UR) triangle
            0.5f, 0.5f, 0.0f,
            -0.5f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
    };

    public void setupQuad() {
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        FloatBuffer fb = BufferUtils.createFloatBuffer(positions.length);
        fb.put(positions);
        fb.flip();
        glBufferData(GL_ARRAY_BUFFER, fb, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 12, 0);
        glEnableVertexAttribArray(0);
    }


    public void main() {
        window = new Window(800, 640);
        window.createWindow();
        setupQuad();
        while (!window.shouldDestroyWindow()) {
            window.updateWindow();

            glBindVertexArray(vao);
            glDrawArrays(GL_TRIANGLES, 0, 6);
        }
        window.clearMem();
    }
}
