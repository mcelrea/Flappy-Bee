package com.mcelrea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Flower {

    private static final float GAP_HEIGHT = 225f;
    private static final float HEIGHT_OFFSET = -400f;
    private static final float MAX_SPEED_PER_SECOND = 100f;
    private static final float COLLISION_RECTANGLE_WIDTH = 13f;
    private static final float COLLISION_RECTANGLE_HEIGHT = 447f;
    private static final float COLLISION_CIRCLE_RADIUS = 33f;
    public static final float WIDTH = COLLISION_CIRCLE_RADIUS * 2;

    private final Circle floorCollisionCircle;
    private final Rectangle floorCollisionRectangle;
    private final Circle ceilingCollisionCircle;
    private final Rectangle ceilingCollisionRectangle;

    private float x = 0;
    private float y = 0;
    private boolean pointClaimed = false;
    private Texture sunflower;
    private Texture stem;

    public Flower() {

        sunflower = new Texture(Gdx.files.internal("sun-flower-th.png"));
        stem = new Texture(Gdx.files.internal("stem.png"));

        this.y = MathUtils.random(HEIGHT_OFFSET);

        floorCollisionRectangle = new Rectangle(x, y, COLLISION_RECTANGLE_WIDTH,
                                                 COLLISION_RECTANGLE_HEIGHT);
        floorCollisionCircle = new Circle(x + COLLISION_RECTANGLE_WIDTH / 2,
                                     y + COLLISION_RECTANGLE_HEIGHT,
                                     COLLISION_CIRCLE_RADIUS);

        ceilingCollisionRectangle = new Rectangle(x,
                floorCollisionCircle.y + GAP_HEIGHT,
                COLLISION_RECTANGLE_WIDTH,
                COLLISION_RECTANGLE_HEIGHT);

        ceilingCollisionCircle = new Circle(x + COLLISION_RECTANGLE_WIDTH / 2,
                ceilingCollisionRectangle.y,
                COLLISION_CIRCLE_RADIUS);
    }

    public void update(float delta) {
        setPosition(x - MAX_SPEED_PER_SECOND * delta);
    }

    private void updateCollisionCircle() {
        floorCollisionCircle.setX(x + floorCollisionRectangle.width / 2);
        ceilingCollisionCircle.setX(x + ceilingCollisionRectangle.width / 2);
    }

    private void updateCollisionRectangle() {
        floorCollisionRectangle.setX(x);
        ceilingCollisionRectangle.setX(x);
    }

    public void setPosition(float x) {
        this.x = x;
        updateCollisionRectangle();
        updateCollisionCircle();
    }

    public void draw(SpriteBatch batch) {

        batch.draw(stem,
                ceilingCollisionRectangle.x,
                ceilingCollisionRectangle.y);

        batch.draw(stem,
                floorCollisionRectangle.x,
                floorCollisionRectangle.y);

        batch.draw(sunflower,
                ceilingCollisionCircle.x - COLLISION_CIRCLE_RADIUS,
                ceilingCollisionCircle.y - COLLISION_CIRCLE_RADIUS,
                COLLISION_CIRCLE_RADIUS * 2,
                COLLISION_CIRCLE_RADIUS * 2);

        batch.draw(sunflower,
                floorCollisionCircle.x - COLLISION_CIRCLE_RADIUS,
                floorCollisionCircle.y - COLLISION_CIRCLE_RADIUS,
                COLLISION_CIRCLE_RADIUS * 2,
                COLLISION_CIRCLE_RADIUS * 2);

    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(floorCollisionCircle.x,
                floorCollisionCircle.y,
                floorCollisionCircle.radius);

        shapeRenderer.rect(floorCollisionRectangle.x,
                floorCollisionRectangle.y,
                floorCollisionRectangle.width,
                floorCollisionRectangle.height);

        shapeRenderer.circle(ceilingCollisionCircle.x,
                ceilingCollisionCircle.y,
                ceilingCollisionCircle.radius);

        shapeRenderer.rect(ceilingCollisionRectangle.x,
                ceilingCollisionRectangle.y,
                ceilingCollisionRectangle.width,
                ceilingCollisionRectangle.height);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean isColliding(Flappy flappy) {
        Circle flappyCollisionCircle = flappy.getCollisionCircle();

        return Intersector.overlaps(flappyCollisionCircle, ceilingCollisionCircle) ||
                Intersector.overlaps(flappyCollisionCircle, ceilingCollisionRectangle) ||
                Intersector.overlaps(flappyCollisionCircle, floorCollisionCircle) ||
                Intersector.overlaps(flappyCollisionCircle, floorCollisionRectangle);
    }

    public boolean isPointClaimed() {
        return pointClaimed;
    }

    public void setPointClaimed(boolean pointClaimed) {
        this.pointClaimed = pointClaimed;
    }
}












