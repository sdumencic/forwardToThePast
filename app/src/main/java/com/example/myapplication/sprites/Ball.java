package com.example.myapplication.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class Ball {
    private Vector3 position;
    public Vector3 velocity;

    private Texture ball;

    public Ball(int x, int y){
        position  = new Vector3(x, y,0);
        velocity = new Vector3(10,10,0);
        ball = new Texture("ball.png");
    }

    public void update(float dt){
        if(position.x > Gdx.graphics.getWidth() - ball.getWidth()){
            velocity.x = -20;
        }
        if(position.y > Gdx.graphics.getHeight() - ball.getHeight()){
            velocity.y = -20;
        }
        if(position.x < 0){
            velocity.x = 20;
        }
        if(position.y < 0){
            velocity.y = 20;
        }
        position.add(velocity);
    }


    public Vector3 getPosition() {
        return position;
    }

    public Texture getTexture() {
        return ball;
    }


}
