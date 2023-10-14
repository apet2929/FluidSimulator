package com.apet2929.fluidsimulator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import com.apet2929.fluidsimulator.Settings.*;

import static com.apet2929.fluidsimulator.Settings.*;

public class Main  extends ApplicationAdapter {

	SpriteBatch sb;
	ShaderProgram basicShader;
	ArrayList<Particle> particles;
	Simulator simulator;
	Square boundingBox;

	ShapeRenderer sr;
	boolean startup;
	UI ui;

	@Override
	public void create() {
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		basicShader = loadShader("shader");
		particles = new ArrayList<>();
		simulator = new Simulator(particles);
		spawnParticles(Settings.NUM_PARTICLES);
		sb.getProjectionMatrix().setToScaling(SCALE,SCALE,1); // IMPORTANT: DONT REMOVE
		boundingBox = new Square(Particle.getWorldBounds());
		startup = true;
		ui = new UI(this::onScreenClick);
		ui.create();
	}

	@Override
	public void resize(int width, int height) {
		ui.resize(width, height);
	}

	private void spawnParticles(int numParticles) {
		float particleSpacing = PARTICLE_SPACING;
		float particleSize = Settings.PARTICLE_RADIUS;
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
		if(startup) {
			startup = !startup;
			return;
		}


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
		ui.render();
	}

	private void onScreenClick(int screenX, int screenY) {
		particles.add(new Particle(new Vector2(screenX, Gdx.graphics.getHeight() - screenY)));
		particles.add(new Particle(new Vector2(screenX + PARTICLE_RADIUS + PARTICLE_SPACING, Gdx.graphics.getHeight() - screenY)));
		particles.add(new Particle(new Vector2(screenX - PARTICLE_RADIUS - PARTICLE_SPACING, Gdx.graphics.getHeight() - screenY)));
		particles.add(new Particle(new Vector2(screenX, Gdx.graphics.getHeight() - screenY + PARTICLE_SPACING + PARTICLE_RADIUS)));
		particles.add(new Particle(new Vector2(screenX, Gdx.graphics.getHeight() - screenY - PARTICLE_SPACING - PARTICLE_RADIUS)));
		particles.add(new Particle(new Vector2(screenX + PARTICLE_RADIUS + PARTICLE_SPACING, Gdx.graphics.getHeight() - screenY + PARTICLE_SPACING + PARTICLE_RADIUS)));
		particles.add(new Particle(new Vector2(screenX + PARTICLE_RADIUS + PARTICLE_SPACING, Gdx.graphics.getHeight() - screenY - PARTICLE_SPACING - PARTICLE_RADIUS)));
		particles.add(new Particle(new Vector2(screenX - PARTICLE_RADIUS - PARTICLE_SPACING, Gdx.graphics.getHeight() - screenY + PARTICLE_SPACING + PARTICLE_RADIUS)));
		particles.add(new Particle(new Vector2(screenX - PARTICLE_RADIUS - PARTICLE_SPACING, Gdx.graphics.getHeight() - screenY - PARTICLE_SPACING - PARTICLE_RADIUS)));

	}

	@Override
	public void dispose() {
		ui.dispose();
	}
}