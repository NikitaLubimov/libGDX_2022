package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Anim {

    private Texture img;
    private Animation<TextureRegion> anm;
    private float time;
    final Vector2 position = new Vector2();

    public Anim(String name, int col, int row, Animation.PlayMode playMode, float x, float y) {
        img = new Texture(name);
        TextureRegion region0 = new TextureRegion(img);
        int xCnt = region0.getRegionWidth() / col;
        int yCnt = region0.getRegionHeight() / row;
        TextureRegion[][] regions0 = region0.split(xCnt, yCnt);
        TextureRegion[] region1 = new TextureRegion[regions0.length * regions0[0].length];
        int count = 0;
        for (int i = 0; i < regions0.length; i++) {
            for (int j = 0; j < regions0[0].length; j++) {
                region1[count++] = regions0[i][j];
            }
        }
        anm = new Animation<TextureRegion>(1 / 20f, region1);
        anm.setPlayMode(playMode);
        time += Gdx.graphics.getDeltaTime();
        position.set(x, y);
    }

    public TextureRegion getFrame() {
        return anm.getKeyFrame(time);
    }

    public void setTime(float time) {
        this.time = time;
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

    public void dispose() {
        img.dispose();
    }

    public void moveTo(Vector2 direction) {
        position.add(direction);
    }

}
