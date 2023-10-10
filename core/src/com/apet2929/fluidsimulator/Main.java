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
	ShaderProgram basicShader;
	ArrayList<Particle> particles;
	Square boundingBox;

	ShapeRenderer sr;

	@Override
	public void create() {
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		basicShader = loadShader("shader");
		particles = new ArrayList<>();
		particles.add(new Particle());
		particles.add(new Particle());
		particles.add(new Particle());
		particles.get(0).position.x += 0.4;
		particles.get(1).position.x -= 0.4;
		sb.getProjectionMatrix().setToScaling(SCALE,SCALE,1);
		boundingBox = new Square();
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

	public void update() {
		for (Particle particle : particles) {
			particle.update();
		}
	}

	@Override
	public void render() {
		update();

		ScreenUtils.clear(1,1,1,1);
		basicShader.bind();
		for (Particle particle : particles) {
			particle.render(sb.getProjectionMatrix(), basicShader);
		}
		boundingBox.render(sb.getProjectionMatrix(), basicShader);
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}