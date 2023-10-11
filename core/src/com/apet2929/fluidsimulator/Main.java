package com.apet2929.fluidsimulator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;

public class Main  extends ApplicationAdapter {
	private static final float SCALE = 1f;
	private static final int NUM_PARTICLES = 200;
	private static final float PARTICLE_SPACING = 20;
	SpriteBatch sb;
	ShaderProgram basicShader;
	ArrayList<Particle> particles;
	Simulator simulator;
	Square boundingBox;

	ShapeRenderer sr;

	@Override
	public void create() {
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		basicShader = loadShader("shader");
		particles = new ArrayList<>();
		simulator = new Simulator(particles);
		spawnParticles(NUM_PARTICLES);
		sb.getProjectionMatrix().setToScaling(SCALE,SCALE,1); // IMPORTANT: DONT REMOVE
		boundingBox = new Square(Particle.getWorldBounds());
	}

	private void spawnParticles(int numParticles) {
		float particleSpacing = PARTICLE_SPACING;
		float particleSize = Particle.PARTICLE_RADIUS;
		int particlesPerRow = (int) Math.sqrt(numParticles);
		int particlesPerCol = (numParticles - 1) / particlesPerRow + 1;
		float spacing = particleSize * 2 + particleSpacing;
		for (int i = 0; i < numParticles; i++) {
			float x = ((i % particlesPerRow) - (particlesPerRow / 2f) + 0.5f) * spacing;
			float y = ((i / particlesPerRow) - (particlesPerCol / 2f) + 0.5f) * spacing;
			x += Gdx.graphics.getWidth() / 2f;
			y += Gdx.graphics.getHeight() / 2f;
			particles.add(new Particle(new Vector2(x, y)));
		}
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
		float smoothingRadius = 0.2f;
		simulator.update(smoothingRadius);

		boundingBox.size = Particle.getWorldBounds();
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