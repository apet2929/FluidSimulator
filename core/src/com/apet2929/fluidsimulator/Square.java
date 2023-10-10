package com.apet2929.fluidsimulator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Square implements Renderable {
    private static final Mesh SQUARE_MESH = getSquareMesh();
    private Vector2 size;
    private Vector3 position;
    private Color color;

    public Square() {
        color = Color.FOREST;
        size = new Vector2(50,50);
        position = new Vector3(0,0,0);
    }

    @Override
    public void render(Matrix4 matrix, ShaderProgram shader) {
        Matrix4 matrix4 = getMatrix(matrix);
        shader.setUniformMatrix("u_projModelView", matrix4);
        shader.setUniform4fv("u_color", new float[]{color.r, color.g, color.b, color.a}, 0, 4);
        SQUARE_MESH.render(shader, GL20.GL_LINES);
    }

    private Matrix4 getMatrix(Matrix4 origin) {
        Matrix4 matrix4 = origin.cpy();

        int maxWidth = Gdx.graphics.getWidth();
        int maxHeight = Gdx.graphics.getHeight();
        float percentageWidth = size.x / maxWidth;
        float percentageHeight = size.y / maxHeight;
        Vector3 scaling = new Vector3(percentageWidth, percentageHeight, 1);
        return matrix4.setToTranslationAndScaling(position, scaling);
    }

    private static Mesh getSquareMesh(){
        float[] vertices = {
                -1, -1, 0,
                -1, 1, 0,
                1, 1, 0,
                1, -1, 0
        };
        short[] indices = {
                0,1,
                1,2,
                2,3,
                3,0
        };
        Mesh mesh = new Mesh(true, vertices.length, indices.length, VertexAttribute.Position());
        mesh.setVertices(vertices);
        mesh.setIndices(indices);
        return mesh;
    }
}
