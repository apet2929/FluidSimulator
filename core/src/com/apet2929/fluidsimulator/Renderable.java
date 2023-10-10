package com.apet2929.fluidsimulator;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;

public interface Renderable {
    void render(Matrix4 matrix, ShaderProgram shader);
}
