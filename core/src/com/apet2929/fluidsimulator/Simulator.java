package com.apet2929.fluidsimulator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Simulator {
    private static final float TARGET_DENSITY = 40f;
    private static final float PRESSURE_MULTIPLIER = 0.2f;
    private static final float MASS = 1f;

    public ArrayList<Particle> particles;
    public ArrayList<Float> densities;
    public Simulator(ArrayList<Particle> particles){
        this.particles = particles;
        densities = new ArrayList<>();
    }

    public void update(float smoothingRadius) {
        updateDensities(smoothingRadius);

        System.out.println("Max density = " + densities.stream().max((o1, o2) -> o1 > o2 ? 1 : -1).get());

        for(int i = 0; i < particles.size(); i++) {
            Particle particle = particles.get(i);
            Vector2 pressureForce = calculatePressureForce(particle.getPosition(), smoothingRadius);
            Vector2 pressureAcceleration = pressureForce.cpy().scl(densities.get(i)).scl(1f/Gdx.graphics.getWidth(), 1f/Gdx.graphics.getHeight());
            Vector2 vel = pressureAcceleration.scl(0.1f);
            particle.velocity = vel;
        }
    }

    static float smoothingKernel(float radius, float dst) {
        if(dst >= radius) return 0;
        float volume = (float) ((Math.PI * Math.pow(radius, 4)) / 6);
        return (radius - dst) * (radius - dst) / volume;
    }

    static float smoothingKernelDerivative(float dst, float radius) {
        if(dst >= radius) return 0;
        float scale = (float) (12 / (Math.pow(radius, 4) * Math.PI));
        return (dst - radius) * scale;
    }

    Vector2 calculatePressureForce(Vector2 samplePoint, float smoothingRadius) {
        Vector2 pressureForce = Vector2.Zero.cpy();
        for (int i = 0; i < particles.size(); i++) {
            Particle particle = particles.get(i);
            Vector2 position = particle.getPosition();
            float dst = position.dst(samplePoint);
            if(dst == 0) {
                continue;
            }
            Vector2 dir = samplePoint.cpy().sub(position).setLength(1);
            float slope = smoothingKernelDerivative(dst, smoothingRadius);
            float density = densities.get(i);
            float dpMag = -convertDensityToPressure(density) * slope * MASS / density;
            Vector2 dp = dir.scl(dpMag);
            pressureForce.add(dp);
        }
        return pressureForce;
    }

    float calculateDensity(Vector2 samplePoint, float smoothing_radius) {
        float density = 0;

        for(Particle particle : particles) {
            Vector2 position = particle.getPosition();
            float dst = position.dst(samplePoint);
            float influence = (float) smoothingKernel(smoothing_radius, dst);
            density += MASS * influence;
        }
        return density;
    }

    public void updateDensities(float smoothingRadius) {
        densities.clear();
        for(int i = 0; i < particles.size(); i++) {
            densities.add(calculateDensity(particles.get(i).getPosition(), smoothingRadius));
        }
    }

    public float convertDensityToPressure(float density) {
        float densityError = density - TARGET_DENSITY;
        float pressure = densityError * PRESSURE_MULTIPLIER;
        return pressure;
    }




}
