package com.example.myapplication.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class myPaddle {
    private Vector3 position;
    private Vector3 velocity;

    private Texture paddle;

    public myPaddle(int x, int y){
        position  = new Vector3(x, y,0);
        velocity = new Vector3(0,0,0);
        paddle = new Texture("paddle.png");
    }

    public void update(float dt){
        velocity.add(0,0,0);
    }

    public Vector3 getPosition() {
        return position;
    }

    public Texture getTexture() {
        return paddle;
    }

    public void move(int i){
        if(position.x < Gdx.graphics.getWidth() - 100 && i == 1) {
            position.x += 20 * i;
        }
        if(position.x > 0 && i == -1) {
            position.x += 20 * i;
        }
    }
}
