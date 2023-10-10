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

public class Circle {
    private static final Mesh CIRCLE_MESH = buildCircleMesh(200);
    public Vector3 position;
    public Vector2 size;
    public Color color;

    public Circle() {
        position = new Vector3(0,0,0);
        color = Color.BLUE;
    }

    public void render(Matrix4 origin, ShaderProgram shader) {
        /* NOTE: Assumes shader is already bound */
        Matrix4 matrix4 = origin.cpy();
        matrix4.translate(position);
        shader.setUniformMatrix("u_projModelView", matrix4);
        shader.setUniform4fv("u_color", new float[]{color.r, color.g, color.b, color.a}, 0, 4);
        CIRCLE_MESH.render(shader, GL20.GL_TRIANGLES);
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

        System.out.println("verts.length = " + verts.length);
        for (Vector3 vert : vertices) {
            System.out.println(vert.toString());
        }
        Mesh circle = new Mesh(true, vCount, indices.length, VertexAttribute.Position());
        circle.setVertices(verts);
        circle.setIndices(indices);
        return circle;
    }
}
