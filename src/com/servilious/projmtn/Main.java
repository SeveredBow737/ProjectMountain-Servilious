package com.servilious.projmtn;


import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.*;

public class Main {


    public static void main(String[] args) {
        DisplayManager display = new DisplayManager();
        display.createDisplay();
        while (!display.isDestroyed()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glClearColor(0.2f, 0.4f, 0.9f, 1.0f);
            display.updateDisplay();
        }
        display.destroyDisplay();
    }
}
