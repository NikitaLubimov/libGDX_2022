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

    public Physics() {
        world = new World(new Vector2(0, -9.81f), true);
        dDebugRenderer = new Box2DDebugRenderer();

    }

    // метод для добавления обьектов в наш мир физики
    public Body addObject(RectangleMapObject rectangleMapObject) {
        Rectangle rectangle = rectangleMapObject.getRectangle();
        String type = (String) rectangleMapObject.getProperties().get("BodyType");  // получаем пользовательское свойство нашего обьекта из мапы
        def = new BodyDef(); // класс описывающий настройки тела объекта
        FixtureDef fdef = new FixtureDef(); // класс описывающий физические параметры тела объекта
        PolygonShape polygonShape = new PolygonShape(); // класс шаблон для формирования структуры тела

        if (type.equals("StaticBody")) {def.type = BodyDef.BodyType.StaticBody;}
        if (type.equals("DynamicBody")) {def.type = BodyDef.BodyType.DynamicBody;}


        def.position.set(rectangle.x + rectangle.width/2, rectangle.y + rectangle.height/2);
        def.gravityScale = 1;

        polygonShape.setAsBox(rectangle.width/2,rectangle.height/2); // метод формирующий наш объект в виде коробки

        fdef.shape = polygonShape;
        fdef.friction = 1; // параметр трения (0-10, где 0 очень скользкий)
        fdef.density = 1; // плотность
        fdef.restitution = 0; // прыгучесть (0-10, где 0 инерция вся поглощается)

        Body body;
        body = world.createBody(def);
        body.createFixture(fdef).setUserData("Стена");

        polygonShape.dispose();
        return body;
    }

    public void setGravity(Vector2 gravity) {world.setGravity(gravity);}

    // рассчет физики. 1 параметр - сколько времени прошло с последнего рассчета (сек). 2 параметр - точность рассчета скоростей. 3 параметр - точность рассчета позиций.
    public void step() {world.step(1/60.0f,3,3);}
    public void debugDraw(OrthographicCamera camera) {dDebugRenderer.render(world,camera.combined);}

    public void dispose() {
        world.dispose();
        dDebugRenderer.dispose();
    }
}
