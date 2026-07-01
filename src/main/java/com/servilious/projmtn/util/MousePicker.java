package com.servilious.projmtn.util;

import com.servilious.projmtn.window.GameWindowManager;
import com.servilious.projmtn.world.Terrain;
import org.joml.Matrix4f;

import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class MousePicker {
    private static final int RECURSION_COUNT = 200;
    private static final float RAY_RANGE = 600;

    private Vector3f currentRay;
    private Matrix4f projectionMat;
    private Matrix4f viewMat;
    private Camera camera;
    private Terrain terrain;
    private Vector3f currentTerrainPoint;
    private GameWindowManager windowManager;


    public MousePicker(Camera camera, Matrix4f projectionMat, Terrain terrain, GameWindowManager  windowManager) {
        this.camera = camera;
        this.projectionMat = projectionMat;
        this.terrain = terrain;
        this.viewMat = MathHelper.createViewMatrix(camera);
        this.windowManager = windowManager;
    }

    public Vector3f getCurrentRay() {
        return currentRay;
    }

    public void tick() {
        viewMat = MathHelper.createViewMatrix(camera);
        currentRay = calculateMouseRay();
        if (intersectionInRange(0, RAY_RANGE, currentRay)) {
            currentTerrainPoint = binarySearch(0, 0, RAY_RANGE, currentRay);
        } else {
            currentTerrainPoint = null;
        }

    }

    private Vector3f calculateMouseRay() {
        double[] mx = new double[1];
        double[] my = new double[1];
        glfwGetCursorPos(windowManager.getWindow(), mx, my);
        Vector2f normalisedCoords = convertNormalizedDeviceCoords((float) mx[0], (float) my[0]);
        Vector4f clipCoords = new Vector4f(normalisedCoords.x, normalisedCoords.y, -1F, 1F);
        Vector4f viewCoords = inverseProjectionMatrix(clipCoords);
        Vector3f worldRay = inverseViewMatrix(viewCoords);
        return worldRay;
    }

    private Vector3f inverseViewMatrix(Vector4f viewCoords) {
        Matrix4f invertedViewMat = new Matrix4f();
        invertedViewMat.invert(viewMat);
        Vector4f worldCoords = new Vector4f();
        invertedViewMat.transform(viewCoords, worldCoords);
        Vector3f mouseRay = new Vector3f(worldCoords.x, worldCoords.y, -worldCoords.z);
        mouseRay.normalize();
        return mouseRay;
    }

    private Vector4f inverseProjectionMatrix(Vector4f clipCoords) {
        Matrix4f invertedProjectionMat = new Matrix4f();
        invertedProjectionMat.invert(projectionMat);
        Vector4f viewCoords = new Vector4f();
        viewCoords = invertedProjectionMat.transform(clipCoords, viewCoords);
        return new Vector4f(viewCoords.x, viewCoords.y, -1F, 0F);
    }

    private Vector2f convertNormalizedDeviceCoords(float mx, float my) {
        float x = (2F * mx) / GameWindowManager.getWidth() - 1;
        float y = (2F * my) / GameWindowManager.getHeight() - 1F;
        return new Vector2f(x, y);
    }

    private Vector3f getPointOnRay(Vector3f ray, float distance) {
        Vector3f camPos = camera.getPos();
        Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
        return new Vector3f(camPos.x + scaledRay.x, camPos.y + scaledRay.y, camPos.z + scaledRay.z);
    }

    private Vector3f binarySearch(int count, float start, float finish, Vector3f ray) {
        float half = start + ((finish - start) / 2f);
        if (count >= RECURSION_COUNT) {
            Vector3f endPoint = getPointOnRay(ray, half);
            Terrain terrain = getTerrain(endPoint.x(), endPoint.z());
            if (terrain != null) {
                return endPoint;
            } else {
                return null;
            }
        }
        if (intersectionInRange(start, half, ray)) {
            return binarySearch(count + 1, start, half, ray);
        } else {
            return binarySearch(count + 1, half, finish, ray);
        }
    }


    private boolean intersectionInRange(float start, float finish, Vector3f ray) {
        Vector3f startPoint = getPointOnRay(ray, start);
        Vector3f endPoint = getPointOnRay(ray, finish);
        if (!isUnderGround(startPoint) && isUnderGround(endPoint)) {
            return true;
        } else {
            return false;
        }
    }


    private boolean isUnderGround(Vector3f testPoint) {
        Terrain terrain = getTerrain(testPoint.x(), testPoint.z());
        float height = 0;
        if (terrain != null) {
            height = terrain.getHeightOfTerrain(testPoint.x(), testPoint.z());
        }
        if (testPoint.y < height) {
            return true;
        } else {
            return false;
        }
    }


    private Terrain getTerrain(float worldX, float worldZ) {
        return terrain;
    }
}
