package com.apet2929.fluidsimulator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public abstract class Renderable {
    public Vector2 size;
    public Vector3 position;
    public Color color;

    public abstract void render(Matrix4 matrix, ShaderProgram shader);

    public void render(Matrix4 matrix, ShaderProgram shader, Mesh mesh, int renderType) {
        shader.setUniformMatrix("u_projModelView", matrix);
        shader.setUniform4fv("u_color", new float[]{color.r, color.g, color.b, color.a}, 0, 4);
        mesh.render(shader, GL20.GL_LINES);
    }

    public static Matrix4 scaleAndTranslateMatrix(Matrix4 matrix, Vector3 position, Vector2 size) {
        Matrix4 matrix4 = matrix.cpy();

        int maxWidth = Gdx.graphics.getWidth();
        int maxHeight = Gdx.graphics.getHeight();
        float percentageWidth = size.x / maxWidth;
        float percentageHeight = size.y / maxHeight;
        Vector3 scaling = new Vector3(percentageWidth, percentageHeight, 1);
        return matrix4.setToTranslationAndScaling(position, scaling);
    }

    public static float pixelsToUV(float pixels, boolean widthRelative) {
        if(widthRelative) {
            return ((pixels / Gdx.graphics.getWidth()) * 2) - 1;
        } else {
            return ((pixels / Gdx.graphics.getHeight()) * 2) - 1;
        }
    }

    public static Vector2 pixelsToUV(float x, float y) {
        float uvX = ((x / Gdx.graphics.getWidth()) * 2) - 1;
        float uvY = ((y / Gdx.graphics.getHeight()) * 2) - 1;
        return new Vector2(uvX, uvY);
    }

    public static float uvToPixels(float uv, boolean widthRelative) {
        float uvNorm = (uv + 1) / 2;
        if(widthRelative) return uvNorm * Gdx.graphics.getWidth();
        return uvNorm * Gdx.graphics.getHeight();
    }
}
