package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.*;

public class MyContList implements ContactListener {

    private int count;

    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        if (a.getUserData() != null && b.getUserData() != null) {
            String tmpA = (String) a.getUserData();
            String tmpB = (String) b.getUserData();

            if (tmpA.equals("ноги") && tmpB.equals("пол")) {
                count++;
            }
            if (tmpA.equals("пол") && tmpB.equals("ноги")) {
                count++;
            }

        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        if (a.getUserData() != null && b.getUserData() != null) {
            String tmpA = (String) a.getUserData();
            String tmpB = (String) b.getUserData();

            if (tmpA.equals("ноги") && tmpB.equals("пол")) {
                count--;
            }
            if (tmpA.equals("пол") && tmpB.equals("ноги")) {
                count--;
            }
        }
    }

    public boolean isOnGround() {
        return count > 0;
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

}

