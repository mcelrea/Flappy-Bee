package com.mcelrea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

public class Flappy {

    private static final float COLLISION_RADIUS = 24f;
    private static final float DIVE_ACCEL = 0.30f;
    private static final float FLY_SPEED = 5f;
    private float ySpeed = 0;
    private final Circle collisionCircle;

    private float x;
    private float y;
    private Texture bee;

    public Flappy() {
        bee = new Texture(Gdx.files.internal("Bee_normal.png"));
        x = 0;
        y = 0;
        collisionCircle = new Circle(x, y, COLLISION_RADIUS);
    }

    public void update() {
        ySpeed -= DIVE_ACCEL;
        setPosition(x, y + ySpeed);
    }

    public void flyUp() {
        ySpeed = FLY_SPEED;
        setPosition(x, y + ySpeed);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateCollisionCircle();
    }

    private void updateCollisionCircle() {
        collisionCircle.setX(x);
        collisionCircle.setY(y);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(bee,
                collisionCircle.x - COLLISION_RADIUS,
                collisionCircle.y - COLLISION_RADIUS);
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(collisionCircle.x,
                             collisionCircle.y,
                             collisionCircle.radius);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Circle getCollisionCircle() {
        return collisionCircle;
    }
}
