package com.servilious.projmtn.util;

import com.servilious.projmtn.renderer.Player;
import com.servilious.projmtn.window.GameWindowManager;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {
    private float distFromPlayer = 50;
    private float angleSphericalOfPlayer = 0;

    private Vector3f position = new Vector3f(0, 0, 0);
    private float pitch = 12;
    private float yaw;
    private float yawRad;
    private float pitchRad;

    private Player player;

    public Camera(Player player) {
        this.player = player;
    }

    public float speed = 0.05f;
    public float lookSpeed = 0.3f;

    private GameWindowManager manager;
    private boolean isMouseLocked = true;

    private boolean cursorInit = false;
    private float lastMouseX, lastMouseY;
    private float lastPitchMouseY;
    private boolean wasPitching = false;
    private float lastAngleMouseX;
    private boolean wasAngling = false;
    private float scrollAccum = 0;

    public void move(boolean devMode, GameWindowManager windowManager) {
        this.manager = windowManager;

        glfwSetInputMode(windowManager.getWindow(), GLFW_CURSOR, isMouseLocked ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL);

        if (devMode) {
            if (!cursorInit) {
                double[] mx = new double[1], my = new double[1];
                glfwGetCursorPos(windowManager.getWindow(), mx, my);
                lastMouseX = (float) mx[0];
                lastMouseY = (float) my[0];
                cursorInit = true;
            }

            if (glfwGetKey(windowManager.getWindow(), GLFW_KEY_W) == GLFW_PRESS) {
                position.z -= (float) Math.cos(yawRad) * speed;
                position.x += (float) Math.sin(yawRad) * speed;
            }
            if (glfwGetKey(windowManager.getWindow(), GLFW_KEY_S) == GLFW_PRESS) {
                position.z += (float) Math.cos(yawRad) * speed;
                position.x -= (float) Math.sin(yawRad) * speed;
            }

            if (glfwGetKey(windowManager.getWindow(), GLFW_KEY_A) == GLFW_PRESS) {
                position.x -= (float) Math.cos(yawRad) * speed;
                position.z -= (float) Math.sin(yawRad) * speed;
            }
            if (glfwGetKey(windowManager.getWindow(), GLFW_KEY_D) == GLFW_PRESS) {
                position.x += (float) Math.cos(yawRad) * speed;
                position.z += (float) Math.sin(yawRad) * speed;
            }

            if (glfwGetKey(windowManager.getWindow(), GLFW_KEY_LEFT_CONTROL) == GLFW_PRESS) {
                speed += 1f;
            } else {
                speed = 0.05f;
            }

            if (glfwGetKey(windowManager.getWindow(), GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
                position.y -= speed;
            }
            if (glfwGetKey(windowManager.getWindow(), GLFW_KEY_SPACE) == GLFW_PRESS) {
                position.y += speed;
            }

            if (isMouseLocked) {
                double[] xpos = new double[1];
                double[] ypos = new double[1];
                glfwGetCursorPos(windowManager.getWindow(), xpos, ypos);
                float cxm = (float) xpos[0];
                float cym = (float) ypos[0];

                float dx = cxm - lastMouseX;
                float dy = cym - lastMouseY;

                pitch += dy * lookSpeed;
                yaw += dx * lookSpeed;
                yawRad = (float) Math.toRadians(yaw);
                pitchRad = (float) Math.toRadians(pitch);

                lastMouseX = cxm;
                lastMouseY = cym;

                if (pitch > 90.0) {
                    pitch = 89.9f;
                }
                if (pitch < -90.0) {
                    pitch = -89.9f;
                }
            }
        } else {
          calculateZoom();
          calculatePitch();
          calculateAngleSphericalFromPlayer();
          float horizDist = calculateHorizontalDist();
          float vertDist = calculateVerticelDist();
          calculateCamPos(horizDist, vertDist);
          this.yaw = 180 - (player.getRotY() + angleSphericalOfPlayer);
          this.yawRad = (float) Math.toRadians(this.yaw);
        }
    }

    private void calculateCamPos(float horizDist, float vertDist) {
        float theta = player.getRotY() + angleSphericalOfPlayer;
        float offsetX = horizDist * (float) Math.sin(Math.toRadians(theta));
        float offsetZ = horizDist * (float) Math.cos(Math.toRadians(theta));
        position.x = player.getPos().x - offsetX;
        position.z = player.getPos().z - offsetZ;
        position.y = player.getPos().y + vertDist;
    }

    private float calculateHorizontalDist() {
        return distFromPlayer * (float) Math.cos(pitchRad);
    }

    private float calculateVerticelDist() {
        return distFromPlayer * (float) Math.sin(pitchRad);
    }


    private void calculateZoom() {
        glfwSetScrollCallback(manager.getWindow(), (long window, double xoffset, double yoffset) -> {
            scrollAccum += (float) yoffset;
        });
        distFromPlayer += -(scrollAccum * 20f);
        scrollAccum = 0;
    }

    private void calculatePitch() {
        if (glfwGetMouseButton(manager.getWindow(), GLFW_MOUSE_BUTTON_2) == GLFW_PRESS) {
            double[] ypos = new double[1];
            glfwGetCursorPos(manager.getWindow(), null, ypos);
            if (!wasPitching) {
                wasPitching = true;
                lastPitchMouseY = (float) ypos[0];
            }
            float pitchChange = (lastPitchMouseY - (float) ypos[0]) * 0.1f;
            pitch += pitchChange;
            pitchRad = (float) Math.toRadians(pitch);
            lastPitchMouseY = (float) ypos[0];
        } else {
            wasPitching = false;
        }
    }

    private void calculateAngleSphericalFromPlayer() {
        if (glfwGetMouseButton(manager.getWindow(), GLFW_MOUSE_BUTTON_1) == GLFW_PRESS) {
            double[] xpos = new double[1];
            glfwGetCursorPos(manager.getWindow(), xpos, null);
            if (!wasAngling) {
                wasAngling = true;
                lastAngleMouseX = (float) xpos[0];
            }
            float angleChange = -(lastAngleMouseX - (float) xpos[0]) * 0.001f;
            angleSphericalOfPlayer += angleChange;
            lastAngleMouseX = (float) xpos[0];
        } else {
            wasAngling = false;
        }
    }

    public Vector3f getPos() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
        this.yawRad = (float) Math.toRadians(yaw);
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
        this.pitchRad = (float) Math.toRadians(pitch);
    }

    public void setPos(Vector3f pos) {
        this.position = pos;
    }
}
