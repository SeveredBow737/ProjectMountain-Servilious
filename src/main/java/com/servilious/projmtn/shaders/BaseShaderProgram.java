package com.servilious.projmtn.shaders;

import com.servilious.projmtn.GlobalConstants;

import static org.lwjgl.opengl.GL33.*;

public abstract class BaseShaderProgram {
    private int vertexID, fragmentID, programID;

    public BaseShaderProgram(String vsPath, String fsPath) {
        vertexID = GlobalConstants.loadShader(vsPath, GL_VERTEX_SHADER);
        fragmentID = GlobalConstants.loadShader(fsPath, GL_FRAGMENT_SHADER);
        programID = glCreateProgram();
        glAttachShader(programID, vertexID);
        glAttachShader(programID, fragmentID);
        glLinkProgram(programID);
        glDeleteShader(vertexID);
        glDeleteShader(fragmentID);
        glValidateProgram(programID);
    }

    public void start() {
        glUseProgram(programID);
    }

    public void stop() {
        glUseProgram(0);
    }

    public void clearMem() {
        glDeleteProgram(programID);
    }
}
