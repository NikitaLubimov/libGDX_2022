package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Main;

public class MenuScreen implements Screen {

    private final Main main;
    private final SpriteBatch batch;
    private final Texture img;
    private Rectangle rectangle;
    private ShapeRenderer shapeRenderer;

    public MenuScreen(Main main) {
        this.main = main;
        batch = new SpriteBatch();
        img = new Texture("menu2.jpg");
        rectangle = new Rectangle(0, 0, img.getWidth(), img.getHeight());
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BROWN);

        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        shapeRenderer.end();

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Vector2 vector2 = new Vector2(Gdx.input.getX(),Gdx.graphics.getHeight() - Gdx.input.getY());
            if (rectangle.contains(vector2)) {
                dispose();
                main.setScreen(new GameScreen(main));
            }
        }
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
        this.batch.dispose();
        this.img.dispose();
//        this.shapeRenderer.dispose();
    }
}
