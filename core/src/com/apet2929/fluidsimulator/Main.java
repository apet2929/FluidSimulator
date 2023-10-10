package com.apet2929.fluidsimulator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class Main  extends ApplicationAdapter {
	private static final float SCALE = 1f;
	SpriteBatch sb;
	ShapeRenderer sr;
	Texture img;
	Texture img2;
	ShaderProgram lightShader;
	ShaderProgram basicShader;

	Vector2 mousePos;
	Mesh mesh;
	Mesh lightingMesh;



	@Override
	public void create() {
		sb = new SpriteBatch();
		basicShader = loadShader("shader");
		img = new Texture(Gdx.files.internal("badlogic.jpg"));

		mousePos = new Vector2(0,0);
		mesh = buildCircle(20, 200);
		sb.getTransformMatrix().setToScaling(SCALE,SCALE,1);
	}

	private ShaderProgram loadShader(String name) {
		ShaderProgram shader = new ShaderProgram(Gdx.files.internal("shaders/" + name +".vs"), Gdx.files.internal("shaders/"+name+".fs"));
		if(shader.isCompiled()){
			System.out.println("Shader works!");

		} else {
			System.err.println(shader.getLog());
			System.exit(-1);
		}
		return shader;
	}

	public Mesh buildCircle(float radiusPixels, int vCount) {
		float radius = radiusPixels / Gdx.graphics.getWidth();
		float angleDelta = 360.0f / vCount;
		float angle = 0;
		int triangleCount = vCount - 2;
		ArrayList<Vector3> vertices= new ArrayList<>();
		short[] indices = new short[triangleCount * 3];
		for(int i = 0; i < vCount; i++){
			angle += angleDelta;
			float x = (float) (radius * Math.cos(Math.toRadians(angle)));
			float y = (float) (radius * Math.sin(Math.toRadians(angle)));
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

	private void drawCircle(Vector3 position, Color color) {
		Matrix4 matrix4 = sb.getTransformMatrix().cpy();
		matrix4.translate(position);
		basicShader.setUniformMatrix("u_projModelView", matrix4);
		basicShader.setUniform4fv("u_color", new float[]{color.r, color.g, color.b, color.a}, 0, 4);
		mesh.render(basicShader, GL20.GL_TRIANGLES);
	}

	@Override
	public void render() {
		ScreenUtils.clear(1,1,1,1);
		Vector3 position = new Vector3(0.5f,0f,0f);
		Color color = Color.RED;

		basicShader.bind();
		drawCircle(position, color);
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}