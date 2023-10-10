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

public class Main  extends ApplicationAdapter {
	private static final float SCALE = 1f;
	SpriteBatch sb;
	Texture img;
	ShaderProgram basicShader;

	Vector2 mousePos;
	Circle circle;



	@Override
	public void create() {
		sb = new SpriteBatch();
		basicShader = loadShader("shader");
		img = new Texture(Gdx.files.internal("badlogic.jpg"));

		mousePos = new Vector2(0,0);
		circle = new Circle();
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

	@Override
	public void render() {
		ScreenUtils.clear(1,1,1,1);
		basicShader.bind();
		circle.render(sb.getTransformMatrix(), basicShader);

	}

	@Override
	public void dispose() {
		super.dispose();
	}
}