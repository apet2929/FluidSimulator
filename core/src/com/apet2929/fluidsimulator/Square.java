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

public class Square extends Renderable {
    private static final Mesh SQUARE_MESH = getSquareMesh();

    public Square() {
        color = Color.FOREST;
        size = new Vector2(50,50);
        position = new Vector3(0,0,0);
    }

    public Square(Vector2 size) {
        this.size = size;
        color = Color.FOREST;
        position = new Vector3(0,0,0);
    }

    @Override
    public void render(Matrix4 matrix, ShaderProgram shader) {
        super.render(scaleAndTranslateMatrix(matrix, position, size), shader, SQUARE_MESH, GL20.GL_LINES);
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
