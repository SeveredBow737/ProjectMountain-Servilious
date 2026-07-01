package com.servilious.projmtn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.lwjgl.opengl.GL33.*;

public class GlobalConstants {

    public static int loadShader(String shaderPath, int shaderType) {
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(shaderPath))) {
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            System.err.println("Failed to read shader file!");
        }
        int shaderID = glCreateShader(shaderType);
        glShaderSource(shaderID, sb);
        glCompileShader(shaderID);
        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Failed to Compile " + (shaderType == GL_VERTEX_SHADER ? "Vertex " : "Fragment ") + "Shader, \n" + glGetShaderInfoLog(shaderID, 512));
        }
        return shaderID;
    }

    public static String getResourcePath() {
        return "src/main/resources";
    }
}
