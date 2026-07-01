package com.servilious.projmtn.util;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class MathHelper {

    private static final Vector3f X_AXIS = new Vector3f(1, 0, 0);
    private static final Vector3f Y_AXIS = new Vector3f(0, 1, 0);
    private static final Vector3f Z_AXIS = new Vector3f(0, 0, 1);

    public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
        float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
        float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
        float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
        float l3 = 1.0F - l1 - l2;
        return l1 * p1.y + l2 * p2.y + l3 * p3.y;
    }


    public static Matrix4f setupTransformationMatrix(Vector2f trans, Vector2f scale) {
        Matrix4f mat4f = new Matrix4f();
        mat4f.identity();
        mat4f.translate(new Vector3f(trans, 0.0f));
        mat4f.scale(new Vector3f(scale.x, scale.y, 1f));
        return mat4f;
    }

    public static Matrix4f setupTransformationMatrix(Vector3f trans, float rx, float ry, float rz, float scale) {
        Matrix4f mat4f = new Matrix4f();
        mat4f.identity();
        mat4f.translate(trans);
        mat4f.rotate((float) Math.toRadians(rx), X_AXIS);
        mat4f.rotate((float) Math.toRadians(ry), Y_AXIS);
        mat4f.rotate((float) Math.toRadians(rz), Z_AXIS);
        mat4f.scale(new Vector3f(scale, scale, scale));
        return mat4f;
    }

    public static void createViewMatrix(Camera camera, Matrix4f dest) {
        dest.identity();
        dest.rotate((float) Math.toRadians(camera.getPitch()), X_AXIS);
        dest.rotate((float) Math.toRadians(camera.getYaw()), Y_AXIS);
        Vector3f cameraPos = camera.getPos();
        dest.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
    }

    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f viewMatrix = new Matrix4f();
        createViewMatrix(camera, viewMatrix);
        return viewMatrix;
    }
}
