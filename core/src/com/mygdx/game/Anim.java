package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Anim {

    private TextureAtlas atlas;
    private Animation<TextureRegion> anm;
    private float time;
    public final Vector2 position = new Vector2();


    //Констуктор для создания анимации через атлас
    public Anim(String pathAtlas, Animation.PlayMode playMode, String name) {
        atlas = new TextureAtlas(pathAtlas);
        anm = new Animation<TextureRegion>(1 / 4f, atlas.findRegions(name));
        anm.setPlayMode(playMode);
        time += Gdx.graphics.getDeltaTime();
    }

    public TextureRegion getFrame() {
        return anm.getKeyFrame(time);
    }

    public void setTime(float time) {
        this.time += time;
    }

    public void zeroTime() {
        this.time = 0;
    }

    public boolean isAnimationOver() {
        return anm.isAnimationFinished(time);
    }

    public void setMode(Animation.PlayMode playMode) {
        anm.setPlayMode(playMode);
    }

    public void disposeAtlas() {
        atlas.dispose();
    }

    public void moveTo(Vector2 direction) {
        position.add(direction);
    }
}
