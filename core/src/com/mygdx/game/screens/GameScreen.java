package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Anim;
import com.mygdx.game.KeyboardAdapter;
import com.mygdx.game.Main;
import com.sun.org.apache.xerces.internal.xs.XSTerm;

public class GameScreen implements Screen {

    private final Main main;
    private final SpriteBatch batch;
    private Anim animRun;
    private boolean dir;
    private KeyboardAdapter inputProcessor;
    private OrthographicCamera camera;
    private TiledMap map;
    private TmxMapLoader tml;
    private OrthogonalTiledMapRenderer mapRenderer;
    private float STEP = 10;

    public GameScreen(Main main) {
        this.main = main;
        batch = new SpriteBatch();
        animRun = new Anim("atlas/RunAtlas.atlas", Animation.PlayMode.LOOP,0,0);
        inputProcessor = new KeyboardAdapter();
        Gdx.input.setInputProcessor(inputProcessor);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());  // Инициализировали камеру, установили видомость камеры по размеру нашего окна
        tml = new TmxMapLoader(); // загрузчик карт
        map = tml.load("maps/MapLvl1.tmx"); // инициализация карты
        mapRenderer = new OrthogonalTiledMapRenderer(map); // отрисовщик карты

        RectangleMapObject rectCam = (RectangleMapObject) map.getLayers().get("СлойКамера").getObjects().get("Камера"); // вытянули обьект "камера" из слоя "СлойКамера" по именни

        camera.position.x = rectCam.getRectangle().x;
        camera.position.y = rectCam.getRectangle().y;


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        // управление камерой право лево
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) camera.position.x -= STEP;
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) camera.position.x += STEP;
        // зумирование карты ( 1- оригинальное значение )
        if(Gdx.input.isKeyPressed(Input.Keys.P)) camera.zoom -= 0.01f;
        if(Gdx.input.isKeyPressed(Input.Keys.M) && camera.zoom > 0) camera.zoom += 0.01f;

        camera.update();

        ScreenUtils.clear(0, 0, 0, 1);
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

        batch.setProjectionMatrix(camera.combined); // установить матрицу проекции КАМЕРЫ, ранее это выполнял сам Batch
        batch.begin();
//        batch.draw(animRun.getFrame(), animRun.position.x,animRun.position.y);
        batch.end();

        mapRenderer.setView(camera); // отрисовка карты по камере
        mapRenderer.render();

    }

    // метод позволяющий менять размер окна. В данном случае привязка идет к размеру вью порта камеры.
    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
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
        map.dispose();
    }
}
