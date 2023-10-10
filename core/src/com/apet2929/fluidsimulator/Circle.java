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

import java.util.ArrayList;

public class Circle extends Renderable {
    private static final Mesh CIRCLE_MESH = buildCircleMesh(2000);

    public Circle() {
        position = new Vector3(0,0,0);
        size = new Vector2(100, 100);
        color = Color.BLUE;
    }

    public Circle(Vector3 position, Vector2 size, Color color) {
        this.position = position;
        this.size = size;
        this.color = color;
    }

    @Override
    public void render(Matrix4 matrix, ShaderProgram shader) {
        /* NOTE: Assumes shader is already bound */
        super.render(scaleAndTranslateMatrix(matrix, position, size), shader, CIRCLE_MESH, GL20.GL_TRIANGLES);
    }

    private static Mesh buildCircleMesh(int vCount) {
        float angleDelta = 360.0f / vCount;
        float angle = 0;
        int triangleCount = vCount - 2;
        ArrayList<Vector3> vertices= new ArrayList<>();
        short[] indices = new short[triangleCount * 3];
        for(int i = 0; i < vCount; i++){
            angle += angleDelta;
            float x = (float) (Math.cos(Math.toRadians(angle)));
            float y = (float) (Math.sin(Math.toRadians(angle)));
            float z = 0;
            vertices.add(new Vector3(x, y, z));
        }

        int indiceIndex = 0;
        for (int i = 0; i < triangleCount; i++) {
            indices[indiceIndex] = 0;
            indices[indiceIndex + 1] = (short) (i + 1);
            indices[indiceIndex + 2] = (short) (i + 2);
            indiceIndex += 3;
        }

        float[] verts = new float[vertices.size()*3];
        int vertIndex = 0;
        for (int i = 0; vertIndex < vertices.size(); i+=3) {
            Vector3 vert = vertices.get(vertIndex);
            vertIndex++;
            verts[i] = vert.x;
            verts[i+1] = vert.y;
            verts[i+2] = vert.z;
        }

        Mesh circle = new Mesh(true, vCount, indices.length, VertexAttribute.Position());
        circle.setVertices(verts);
        circle.setIndices(indices);
        return circle;
    }
}
