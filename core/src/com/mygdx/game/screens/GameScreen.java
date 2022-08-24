package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Anim;
import com.mygdx.game.KeyboardAdapter;
import com.mygdx.game.Main;

public class GameScreen implements Screen {

    private final Main main;
    private final SpriteBatch batch;
    private Anim animRun;
    private boolean dir;
    private KeyboardAdapter inputProcessor;

    public GameScreen(Main main) {
        this.main = main;
        batch = new SpriteBatch();
        animRun = new Anim("atlas/RunAtlas.atlas", Animation.PlayMode.LOOP,0,0);
        inputProcessor = new KeyboardAdapter();
        Gdx.input.setInputProcessor(inputProcessor);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1);
        animRun.moveTo(inputProcessor.getDirection());
        animRun.setTime(Gdx.graphics.getDeltaTime());
        float x = Gdx.input.getX() - animRun.getFrame().getRegionWidth() / 2;
        float y = Gdx.graphics.getHeight() - Gdx.input.getY() - animRun.getFrame().getRegionHeight() / 2;

        if (Gdx.input.isKeyPressed(Input.Keys.R)) dir = true;
        if (Gdx.input.isKeyPressed(Input.Keys.L)) dir = false;

        if (!animRun.getFrame().isFlipX() && !dir) {
            animRun.getFrame().flip(true, false);
        }

        if (animRun.getFrame().isFlipX() && dir) {
            animRun.getFrame().flip(true,false);
        }

        batch.begin();
        batch.draw(animRun.getFrame(), animRun.position.x,animRun.position.y);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

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
        batch.dispose();
        animRun.disposeAtlas();
    }
}
