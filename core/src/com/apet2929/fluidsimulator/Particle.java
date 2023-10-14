package com.apet2929.fluidsimulator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import static com.apet2929.fluidsimulator.Settings.*;

public class Particle extends Renderable {

    Vector3 lastPos;
    Vector2 velocity;
    Circle circle;

    public Particle() {
        this(new Vector2(0,0));
    }

    public Particle(Vector2 position) {
        velocity = new Vector2(0,0);
        this.position = new Vector3(Renderable.pixelsToUV(position.x, position.y), 0);
        lastPos = this.position.cpy();
        this.color = Color.BLUE;

        Vector2 size = new Vector2(PARTICLE_RADIUS * 2, PARTICLE_RADIUS * 2);
        circle = new Circle(this.position, size, color);
    }

    public Particle fromUV(Vector2 positionUV) {
        velocity = new Vector2(0,0);
        this.position = new Vector3(positionUV, 0);
        this.color = Color.BLUE;

        Vector2 size = new Vector2(PARTICLE_RADIUS * 2, PARTICLE_RADIUS * 2);
        circle = new Circle(this.position, size, color);
        return this;
    }

    public void update() {
        lastPos = position.cpy();
        position.x += velocity.x * Gdx.graphics.getDeltaTime();
        position.y += velocity.y * Gdx.graphics.getDeltaTime();
//        velocity.y -= GRAVITY;
        resolveCollisions();
    }

    @Override
    public void render(Matrix4 matrix, ShaderProgram shader) {
        circle.color = getColorByVelocity();
        circle.render(matrix, shader);
    }

    public Color getColorByVelocity(){
        float velMag = lastPos.sub(position).scl(1/Gdx.graphics.getDeltaTime()).len();

        Vector3 red = new Vector3(1,0,0);
        Vector3 blue = new Vector3(0,0,1);
        Vector3 c = blue.lerp(red, velMag / MAX_VEL_FOR_COLOR);
        return new Color(c.x, c.y, c.z, 1);
    }

    private void resolveCollisions() {
        Vector2 uvRadius = pixelsToUV(PARTICLE_RADIUS, PARTICLE_RADIUS).add(1, 1);

        if(Math.abs(position.y) > 1 - uvRadius.y - (MARGIN_Y * 2)) {
            velocity.y *= -1 * COLLISION_DAMPENING;
            position.y = Math.signum(position.y) * (1 - uvRadius.y - (MARGIN_Y * 2));
        }

        if(Math.abs(position.x) > 1 - uvRadius.x - (MARGIN_X*2)) {
            velocity.x *= -1 * COLLISION_DAMPENING;
            position.x = Math.signum(position.x) * (1 - uvRadius.x - (MARGIN_X*2));
        }
    }

    public static Vector2 getWorldBounds(){
        float width = 1 - (MARGIN_X * 2);
        float height = 1 - (MARGIN_Y * 2);
        width *= Gdx.graphics.getWidth();
        height *= Gdx.graphics.getHeight();
        return new Vector2(width, height);
    }

    public Vector2 getPosition(){
        return new Vector2(position.x, position.y);
    }
}
