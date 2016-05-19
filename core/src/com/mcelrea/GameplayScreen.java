package com.mcelrea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameplayScreen implements Screen {

    private static final float WORLD_WIDTH = 480;
    private static final float WORLD_HEIGHT = 640;
    private static final float GAP_BETWEEN_FLOWERS = 200f;

    OrthographicCamera camera;//2D camera
    Viewport viewport;//lock the view
    SpriteBatch spriteBatch; //allows us to draw png, jpg
    ShapeRenderer shapeRenderer; //allows us to draw circle, rectangles, etc.
    BitmapFont font;
    Texture dayBackground, sunsetBackground, nightBackground;

    Sound music;
    Sound pointSound;
    Sound hitSound;
    Sound flySound;

    Flappy bee = new Flappy();
    Array<Flower> flowers = new Array<Flower>();
    int score = 0;

    @Override
    public void show() {

        bee.setPosition(WORLD_WIDTH / 4, WORLD_HEIGHT / 2);

        //create a new 2D camera, initialize its position
        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();

        //create the viewport
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        //create a new SpriteBatch, allows us to draw png, jpg
        spriteBatch = new SpriteBatch();

        //create a new ShapeRender, allows us to draw circle, rectangles, etc
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        font = new BitmapFont();
        dayBackground = new Texture(Gdx.files.internal("back.jpg"));
        sunsetBackground = new Texture(Gdx.files.internal("back2.jpg"));
        nightBackground = new Texture(Gdx.files.internal("back3.jpg"));

        music = Gdx.audio.newSound(Gdx.files.internal("Plants vs Zombies - Roof Stage.mp3"));
        music.loop(0.5f);
        pointSound = Gdx.audio.newSound(Gdx.files.internal("point.wav"));
        hitSound = Gdx.audio.newSound(Gdx.files.internal("hit.wav"));
        flySound = Gdx.audio.newSound(Gdx.files.internal("jump.wav"));
    }

    @Override
    public void render(float delta) {
        //clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //get user input, moves game characters, AI, collision detection
        update(delta);

        spriteBatch.setProjectionMatrix(camera.projection);
        spriteBatch.setTransformMatrix(camera.view);
        spriteBatch.begin();
        if(score < 15)
            spriteBatch.draw(dayBackground, 0, 0);
        else if(score < 30)
            spriteBatch.draw(sunsetBackground, 0, 0);
        else
            spriteBatch.draw(nightBackground, 0, 0);
        drawFlowers(spriteBatch);
        bee.draw(spriteBatch);
        font.draw(spriteBatch, "" + score, 240, 620);
        spriteBatch.end();

        //draw shapes onto the screen
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin();
        //bee.drawDebug(shapeRenderer);
        //for every flower
        for(Flower flower: flowers) {
            //flower.drawDebug(shapeRenderer); //draw
        }
        shapeRenderer.end();
    }

    public void drawFlowers(SpriteBatch batch) {
        //for every flower
        for(Flower flower: flowers) {
            flower.draw(batch);//draw the flower
        }
    }

    public void updateScore() {
        Flower flower = flowers.first();
        if(flower.getX() < bee.getX() && !flower.isPointClaimed()) {
            score++;
            pointSound.play(0.5f);
            flower.setPointClaimed(true);
        }
    }


    public void createNewFlower() {
        Flower newFlower = new Flower();
        newFlower.setPosition(WORLD_WIDTH + Flower.WIDTH);
        flowers.add(newFlower);
    }

    private void checkIfNewFlowerIsNeeded() {
        if(flowers.size == 0) {
            createNewFlower();
        }
        else {
            Flower flower = flowers.peek();
            if(flower.getX() < WORLD_WIDTH - GAP_BETWEEN_FLOWERS) {
                createNewFlower();
            }
        }
    }

    public void removeFlowerIfPassed() {
        if(flowers.size > 0) {
            Flower firstFlower = flowers.first();
            if(firstFlower.getX() < -Flower.WIDTH) {
                flowers.removeValue(firstFlower, true);
            }
        }
    }

    private void updateFlowers(float delta) {
        //for every flower
        for(Flower flower: flowers) {
            flower.update(delta); //move
        }

        checkIfNewFlowerIsNeeded();
        removeFlowerIfPassed();
    }

    public boolean checkForCollision() {
        //for every flower
        for(Flower flower: flowers) {
            if(flower.isColliding(bee)) {
                return true;
            }
        }

        return false;
    }

    public void restartGame() {
        flowers.clear();
        bee.setPosition(WORLD_WIDTH / 4, WORLD_HEIGHT / 2);
        score = 0;
    }

    private void update(float delta) {
        bee.update();
        updateFlowers(delta);
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            flySound.play(0.1f);
            bee.flyUp();
        }
        keepFlappyOnScreen();

        updateScore();

        if(checkForCollision()) {
            hitSound.play(0.5f);
            restartGame();
        }


    }

    public void keepFlappyOnScreen() {
        bee.setPosition(bee.getX(),
                MathUtils.clamp(bee.getY(),0,WORLD_HEIGHT));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
