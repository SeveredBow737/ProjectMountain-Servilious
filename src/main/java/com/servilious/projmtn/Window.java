package com.servilious.projmtn;

import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.*;

import static org.lwjgl.glfw.GLFWErrorCallback.*;
import static org.lwjgl.glfw.GLFW.*;

public class Window {
    private String title = "Project Mountain : Servilious ";
    private long window;
    private int width, height;
    private long fullScreenMonitor;
    private boolean forceFullscreen = true; //Weather to force full screen for the monitor or to use the width and height from constructor

    public Window(int width, int height) {
        this.width = width;
        this.height = height;
    }


    public void createWindow() {
        createPrint(System.err).set();
        glfwInit();
        if (!glfwInit()) {
            throw new RuntimeException("Failed to Initialize GLFW!");
        }
        fullScreenMonitor = glfwGetPrimaryMonitor();

        window = glfwCreateWindow(width, height, title, forceFullscreen ? fullScreenMonitor : 0L, 0L);
        if (window == 0L) {
            throw new RuntimeException("Failed to create an GLFW Window!");
        }

        glfwDefaultWindowHints();

        glfwMakeContextCurrent(window);

        glfwSwapInterval(1);
        GL.createCapabilities();
        if (forceFullscreen) {
            glfwSetWindowMonitor(window, fullScreenMonitor, 0, 0, 1920, 1080, glfwGetVideoMode(glfwGetPrimaryMonitor()).refreshRate());
        } else {
            glfwSetWindowMonitor(window, 0L, 1920 / 2 - 450, 1080 / 2 - 300, width, height, glfwGetVideoMode(glfwGetPrimaryMonitor()).refreshRate());
        }
    }

    public void updateWindow() {
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    public void clearMem() { //Clears Used up Memory / Deinitializes stuff
        glfwFreeCallbacks(window);
        createPrint(null).free();
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    public boolean shouldDestroyWindow() {
      return glfwWindowShouldClose(window);
    }

    public String getTitle() {
        return title;
    }

    public long getWindow() {
        return window;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getFullScreenMonitor() {
        return fullScreenMonitor;
    }

    public boolean isForceFullscreen() {
        return forceFullscreen;
    }
}
