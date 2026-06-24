package com.servilious.projmtn.shaders;

import com.servilious.projmtn.GlobalConstants;

public class BaseShader extends BaseShaderProgram {
    private static final String vsPath = GlobalConstants.getResourcePath() + "/shaders/VertexShader.vsh";
    private static final String fsPath = GlobalConstants.getResourcePath() +  "/shaders/FragmentShader.fsh";

    public BaseShader() {
        super(vsPath, fsPath);
    }
}
