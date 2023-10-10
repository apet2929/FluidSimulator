package com.apet2929.fluidsimulator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Particle {
    private static final float GRAVITY = 0.02f;
    Vector3 position;
    Color color;
    Vector2 velocity;
    Circle circle;
    float radius;

    public Particle() {
        velocity = new Vector2(0,0);
        this.position = new Vector3(0,0,0);
        this.color = Color.RED;
        radius = 50;
        Vector2 size = new Vector2(radius * 2, radius * 2);
        circle = new Circle(position, size, color);
    }

    public Particle(Vector3 position, Color color) {
        velocity = new Vector2(0,0);
        this.position = position;
        this.color = color;
        radius = 50;
        Vector2 size = new Vector2(radius * 2, radius * 2);
        circle = new Circle(position, size, color);
    }

    public void update() {
        position.x += velocity.x * Gdx.graphics.getDeltaTime();
        position.y += velocity.y * Gdx.graphics.getDeltaTime();
        velocity.y -= GRAVITY;
        resolveCollisions();
    }

    public void render(Matrix4 origin, ShaderProgram shader) {
        circle.render(origin, shader);
    }

    private void resolveCollisions() {
        float uvRadius = pixelsToUV(radius, false) + 1;
        if(position.y < -1 + uvRadius) {
            position.y = -1 + uvRadius;
            velocity.y *= -1;
        }
    }

    private float pixelsToUV(float pixels, boolean widthRelative) {
        if(widthRelative) {
            return ((pixels / Gdx.graphics.getWidth()) * 2) - 1;
        } else {
            return ((pixels / Gdx.graphics.getHeight()) * 2) - 1;
        }
    }

    private float uvToPixels(float uv, boolean widthRelative) {
        float uvNorm = (uv + 1) / 2;
        if(widthRelative) return uvNorm * Gdx.graphics.getWidth();
        return uvNorm * Gdx.graphics.getHeight();
    }
}
