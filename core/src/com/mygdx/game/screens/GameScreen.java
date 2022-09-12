package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Anim;
import com.mygdx.game.Main;
import com.mygdx.game.Physics;

import java.util.Iterator;

public class GameScreen implements Screen {

    private final Main main;
    private final SpriteBatch batch;
    private Anim myHero;
    private Anim myHeroIdle;
    private Anim myHeroRun;
    private Anim myHeroAttacks1;
    private Anim myHeroJump;
    private boolean dir = true;
    private OrthographicCamera camera;
    private TiledMap map;
    private TmxMapLoader tml;
    private OrthogonalTiledMapRenderer mapRenderer;
    private float STEP = 10;
    private final int[] layers;
    private Array<RectangleMapObject> rectObj;
    private Physics physics;
    private Body body;
    private Rectangle rectangleHero;
    private Music music;

    public GameScreen(Main main) {
        this.main = main;
        batch = new SpriteBatch();
        myHeroIdle = new Anim("atlas/IdleMyHero.atlas", Animation.PlayMode.LOOP, "Idle");
        myHeroRun = new Anim("atlas/RunMyHero.atlas", Animation.PlayMode.LOOP, "Run");
        myHeroAttacks1 = new Anim("atlas/MyHeroAttacks1.atlas", Animation.PlayMode.LOOP, "Attacks1");
        myHeroJump = new Anim("atlas/myHeroJump.atlas", Animation.PlayMode.LOOP, "Jump");
        myHero = myHeroIdle;
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());  // Инициализировали камеру, установили видомость камеры по размеру нашего окна
        tml = new TmxMapLoader(); // загрузчик карт
        map = tml.load("maps/MapLvl1.tmx"); // инициализация карты
        mapRenderer = new OrthogonalTiledMapRenderer(map); // отрисовщик карты

        physics = new Physics();
        RectangleMapObject rectMapObjHero = (RectangleMapObject) map.getLayers().get("Объекты").getObjects().get("hero");
        rectangleHero = new Rectangle();
        rectangleHero = rectMapObjHero.getRectangle();

        body = physics.addObject(rectMapObjHero);

        layers = new int[4];
        layers[0] = map.getLayers().getIndex("С4.Фон бекграунд");
        layers[1] = map.getLayers().getIndex("С3.Фон стена");
        layers[2] = map.getLayers().getIndex("C2.Фон декор");
        layers[3] = map.getLayers().getIndex("C1.Стены");

        rectObj = map.getLayers().get("Объекты").getObjects().getByType(RectangleMapObject.class);
        for (int i = 0; i < rectObj.size; i++) {
            if (!rectObj.get(i).getName().equals("hero")){
                physics.addObject(rectObj.get(i));
            }
        }

        music = Gdx.audio.newMusic(Gdx.files.internal("2.mp3"));
        music.setLooping(true);
        music.play();

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        if (body.isAwake()) {
            myHero = myHeroIdle;
        }

        // управление камерой право лево
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            dir = true;
            myHero = myHeroRun;
            body.applyForceToCenter(new Vector2(0.45f, 0), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            dir = false;
            myHero = myHeroRun;
            body.applyForceToCenter(new Vector2(-0.45f, 0), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && physics.myContList.isOnGround()) {
            myHero = myHeroJump;
            body.applyForceToCenter(new Vector2(0, +3f), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            myHero = myHeroAttacks1;
        }

        // зумирование карты ( 1- оригинальное значение )
        if (Gdx.input.isKeyPressed(Input.Keys.P)) camera.zoom -= 0.02f;
        if (Gdx.input.isKeyPressed(Input.Keys.M) && camera.zoom > 0) camera.zoom += 0.02f;

        camera.position.x = body.getPosition().x * physics.PPM;
        camera.position.y = body.getPosition().y * physics.PPM;
        camera.update();

        ScreenUtils.clear(0, 0, 0, 1);

        if (!myHero.getFrame().isFlipX() && !dir) {
            myHero.getFrame().flip(true, false);
        }

        if (myHero.getFrame().isFlipX() && dir) {
            myHero.getFrame().flip(true, false);
        }

        mapRenderer.setView(camera); // отрисовка карты по камере
        mapRenderer.render(layers); // рендер всех слоёв

        float x = Gdx.graphics.getWidth() / 2 - rectangleHero.getWidth() / 2 / camera.zoom;
        float y = Gdx.graphics.getHeight() / 2 - rectangleHero.getHeight() / 2 / camera.zoom;

        Sprite spr = new Sprite(myHero.getFrame());
        spr.setPosition(x, y);
        spr.scale(1.5f);

        batch.begin();
        batch.draw(myHero.getFrame(), x, y, rectangleHero.width / camera.zoom, rectangleHero.height / camera.zoom);
        batch.end();
        myHero.setTime(Gdx.graphics.getDeltaTime());
        physics.debugDraw(camera);
        physics.step();

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
        myHero.disposeAtlas();
        map.dispose();
        myHeroRun.disposeAtlas();
        myHeroIdle.disposeAtlas();
        music.dispose();
    }
}
