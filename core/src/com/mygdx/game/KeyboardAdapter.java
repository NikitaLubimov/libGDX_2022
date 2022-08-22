package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

public class KeyboardAdapter extends InputAdapter {

    private boolean leftPress;
    private boolean rightPress;

    private final Vector2 direction = new Vector2();

    @Override
    public boolean keyDown(int keycode) {
//        if (Gdx.input.isKeyPressed(Input.Keys.D)) rightPress = true;
//        System.out.println("D");
//        if (Gdx.input.isKeyPressed(Input.Keys.A)) leftPress = true;
//        System.out.println("A");
        if (keycode == Input.Keys.A) leftPress = true;
        if (keycode == Input.Keys.D) rightPress = true;
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
//        if (Gdx.input.isKeyPressed(Input.Keys.D)) rightPress = false;
//        if (Gdx.input.isKeyPressed(Input.Keys.A)) leftPress = false;
        if (keycode == Input.Keys.A) leftPress = true;
        if (keycode == Input.Keys.D) rightPress = true;
        return super.keyUp(keycode);
    }

    public Vector2 getDirection() {
        direction.set(0,0);

        if (rightPress) direction.add(+1,0);
        if (leftPress) direction.add(-1,0);

        return direction;
    }
}
