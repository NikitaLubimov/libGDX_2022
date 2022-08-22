package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;
    Anim animRun;
    boolean dir ;
    TextureRegion currentFrame;
    KeyboardAdapter inputProcessor = new KeyboardAdapter();

    @Override
    public void create() {
        batch = new SpriteBatch();
        animRun = new Anim("run.png", 6, 1, Animation.PlayMode.LOOP,0,0);
        Gdx.input.setInputProcessor(inputProcessor);
    }

    @Override
    public void render() {
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
    public void dispose() {
        batch.dispose();
        animRun.dispose();
    }
}
