package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
import com.mygdx.game.KeyboardAdapter;
import com.mygdx.game.Main;
import com.mygdx.game.Physics;

public class GameScreen implements Screen {

    private final Main main;
    private final SpriteBatch batch;
    private Anim myHero;
    private boolean dir;
    private KeyboardAdapter inputProcessor;
    private OrthographicCamera camera;
    private TiledMap map;
    private TmxMapLoader tml;
    private OrthogonalTiledMapRenderer mapRenderer;
    private float STEP = 10;
    private final int[] layers;
    private ShapeRenderer shapeRenderer;
    private Array<RectangleMapObject> rectObj;
    private Physics physics;
    private Body body;
    private Rectangle rectangleHero;

    public GameScreen(Main main) {
        this.main = main;
        batch = new SpriteBatch();
        myHero = new Anim("atlas/Idle.png", 8, 1, Animation.PlayMode.LOOP);
        inputProcessor = new KeyboardAdapter();
        Gdx.input.setInputProcessor(inputProcessor);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());  // Инициализировали камеру, установили видомость камеры по размеру нашего окна
        tml = new TmxMapLoader(); // загрузчик карт
        map = tml.load("maps/MapLvl1.tmx"); // инициализация карты
        mapRenderer = new OrthogonalTiledMapRenderer(map); // отрисовщик карты
//        shapeRenderer = new ShapeRenderer();

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
        for (RectangleMapObject rect : rectObj) {
            if (rect.getName() == null){
                physics.addObject(rect);
            }
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        // управление камерой право лево
        if (Gdx.input.isKeyPressed(Input.Keys.D)) body.applyForceToCenter(new Vector2(100000,0), true);
        if (Gdx.input.isKeyPressed(Input.Keys.A)) body.applyForceToCenter(new Vector2(-100000,0), true);
        // зумирование карты ( 1- оригинальное значение )
        if (Gdx.input.isKeyPressed(Input.Keys.P)) camera.zoom -= 0.01f;
        if (Gdx.input.isKeyPressed(Input.Keys.M) && camera.zoom > 0) camera.zoom += 0.01f;

        camera.position.x = body.getPosition().x;
        camera.position.y = body.getPosition().y;
        camera.update();

        ScreenUtils.clear(0, 0, 0, 1);
        myHero.moveTo(inputProcessor.getDirection());
//        animRun.setTime(Gdx.graphics.getDeltaTime());
//        float x = Gdx.input.getX() - animRun.getFrame().getRegionWidth() / 2;
//        float y = Gdx.graphics.getHeight() - Gdx.input.getY() - animRun.getFrame().getRegionHeight() / 2;

        if (Gdx.input.isKeyPressed(Input.Keys.R)) dir = true;
        if (Gdx.input.isKeyPressed(Input.Keys.L)) dir = false;

//        if (!animRun.getFrame().isFlipX() && !dir) {
//            animRun.getFrame().flip(true, false);
//        }
//
//        if (animRun.getFrame().isFlipX() && dir) {
//            animRun.getFrame().flip(true, false);
//        }

        // отрисовка квадратов
//        shapeRenderer.setProjectionMatrix(camera.combined);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        for (RectangleMapObject rect : rectObj) {
//            Rectangle rectangle = rect.getRectangle();
//            shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
//        }
//        shapeRenderer.end();

        mapRenderer.setView(camera); // отрисовка карты по камере
        mapRenderer.render(layers); // рендер всех слоёв

        batch.setProjectionMatrix(camera.combined); // установить матрицу проекции КАМЕРЫ, ранее это выполнял сам Batch
        rectangleHero.x = body.getPosition().x - rectangleHero.width/2;
        rectangleHero.y = body.getPosition().y - rectangleHero.height/2;
        batch.begin();
        batch.draw(myHero.getFrame(), rectangleHero.x,  rectangleHero.y);
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
        myHero.disposeImg();
        map.dispose();
        shapeRenderer.dispose();
    }
}
