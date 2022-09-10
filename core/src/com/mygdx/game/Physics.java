package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Physics {
    private final World world;
    private final Box2DDebugRenderer dDebugRenderer;
    private BodyDef def;
    public final float PPM = 200;
    public final MyContList myContList;

    public Physics() {
        world = new World(new Vector2(0, -9.81f), true);
        myContList = new MyContList();
        world.setContactListener(myContList);
        dDebugRenderer = new Box2DDebugRenderer();

    }

    // метод для добавления обьектов в наш мир физики
    public Body addObject(RectangleMapObject rectangleMapObject) {
        Rectangle rectangle = rectangleMapObject.getRectangle();
        String type = (String) rectangleMapObject.getProperties().get("BodyType");  // получаем пользовательское свойство нашего обьекта из мапы
        def = new BodyDef(); // класс описывающий настройки тела объекта
        FixtureDef fdef = new FixtureDef(); // класс описывающий физические параметры тела объекта
        PolygonShape polygonShape = new PolygonShape(); // класс шаблон для формирования структуры тела

        if (type.equals("StaticBody")) {
            def.type = BodyDef.BodyType.StaticBody;
        }
        if (type.equals("DynamicBody")) {
            def.type = BodyDef.BodyType.DynamicBody;
        }


        def.position.set((rectangle.x + rectangle.width / 2) / PPM, (rectangle.y + rectangle.height / 2) / PPM);
        def.gravityScale = 1;

        polygonShape.setAsBox(rectangle.width / 2 / PPM, rectangle.height / 2 / PPM); // метод формирующий наш объект в виде коробки

        fdef.shape = polygonShape;
        fdef.friction = 0.55f; // параметр трения (0-10, где 0 очень скользкий)
        fdef.density = 1; // плотность
        fdef.restitution = 0; // прыгучесть (0-10, где 0 инерция вся поглощается)

        Body body;
        body = world.createBody(def);
        String name = rectangleMapObject.getName();
        body.createFixture(fdef).setUserData(name);
        if (name != null && name.equals("hero")) {
            polygonShape.setAsBox(rectangle.width / 12 / PPM, rectangle.height / 12 / PPM, new Vector2(0, -rectangle.width / 1.2f / PPM), 0);
            body.createFixture(fdef).setUserData("ноги");
            body.setFixedRotation(true);
            body.getFixtureList().get(body.getFixtureList().size -  1).setSensor(true);

        }

        polygonShape.dispose();
        return body;
    }

    public void setGravity(Vector2 gravity) {
        world.setGravity(gravity);
    }

    // рассчет физики. 1 параметр - сколько времени прошло с последнего рассчета (сек). 2 параметр - точность рассчета скоростей. 3 параметр - точность рассчета позиций.
    public void step() {
        world.step(1 / 60.0f, 3, 3);
    }

    public void debugDraw(OrthographicCamera camera) {
        dDebugRenderer.render(world, camera.combined);
    }

    public void dispose() {
        world.dispose();
        dDebugRenderer.dispose();
    }
}
