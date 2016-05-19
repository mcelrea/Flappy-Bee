package com.mcelrea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class TitleScreen implements Screen {

    private static final float WORLD_WIDTH = 480;
    private static final float WORLD_HEIGHT = 640;
    private final MyGdxGame game;
    private Stage stage;

    private Texture back;
    private Texture playUp;
    private Texture playDown;
    private Texture exitUp;
    private Texture exitDown;

    public TitleScreen(MyGdxGame game) {
        this.game = game;
    }


    @Override
    public void show() {
        stage = new Stage(new FitViewport(WORLD_WIDTH,WORLD_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        back = new Texture(Gdx.files.internal("title.png"));
        Image background = new Image(back);

        //////////////////////////////////////////////////////////
        /////////////////////creating a button////////////////////
        //////////////////////////////////////////////////////////
        playUp = new Texture(Gdx.files.internal("play button up.png"));
        playDown = new Texture(Gdx.files.internal("play button down.png"));
        ImageButton playButton = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(playUp)),
                new TextureRegionDrawable(new TextureRegion(playDown))
        );
        playButton.setPosition(WORLD_WIDTH/2, 300, Align.center);
        playButton.addListener(new ActorGestureListener() {
            public void tap(InputEvent event, float x, float y,
                            int count, int button) {
                super.tap(event,x,y,count,button);
                game.setScreen(new GameplayScreen());
            }
        });
        //////////////////////////////////////////////////////////
        /////////////////end creating a button////////////////////
        //////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////
        /////////////////////creating a button////////////////////
        //////////////////////////////////////////////////////////
        exitUp = new Texture(Gdx.files.internal("exit button up.png"));
        exitDown = new Texture(Gdx.files.internal("exit button down.png"));
        ImageButton exitButton = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(exitUp)),
                new TextureRegionDrawable(new TextureRegion(exitDown))
        );
        exitButton.setPosition(WORLD_WIDTH/2, 150, Align.center);
        exitButton.addListener(new ActorGestureListener() {
            public void tap(InputEvent event, float x, float y,
                            int count, int button) {
                super.tap(event,x,y,count,button);
                Gdx.app.exit();
            }
        });
        //////////////////////////////////////////////////////////
        /////////////////end creating a button////////////////////
        //////////////////////////////////////////////////////////

        stage.addActor(background);
        stage.addActor(playButton);
        stage.addActor(exitButton);
    }

    @Override
    public void render(float delta) {
        //clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
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
