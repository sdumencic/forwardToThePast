package com.example.myapplication.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.example.myapplication.sprites.Ball;
import com.example.myapplication.sprites.myPaddle;
import com.mygdx.game.MyGdxGame;



public class PlayState extends State{
    // private Texture ball;
    private myPaddle paddle;
    private myPaddle enemypaddle;
    private Ball ball;


    protected PlayState(GameStateManager gsm) {
        super(gsm);
        ball = new Ball(500,500);
        paddle = new myPaddle(50,100);
        enemypaddle = new myPaddle(1000, 1000);
        //cam.setToOrtho(false, Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4);
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.getX() > Gdx.graphics.getWidth()/2){
            if(Gdx.input.isTouched()){
                paddle.move(1);
            }

        }

        if(Gdx.input.getX() < Gdx.graphics.getWidth()/2){
            if(Gdx.input.isTouched()){
                paddle.move(-1);
            }

        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        if(enemypaddle.getPosition().y < ball.getPosition().y ){
            ball.velocity.y = ball.velocity.y * -1;
        }
        paddle.update(dt);
        ball.update(dt);
        enemypaddle.update(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
        //sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(ball.getTexture(), ball.getPosition().x ,ball.getPosition().y);
        sb.draw(paddle.getTexture(), paddle.getPosition().x ,paddle.getPosition().y);
        sb.draw(enemypaddle.getTexture(),enemypaddle.getPosition().x, enemypaddle.getPosition().y);
        sb.end();
    }

    @Override
    public void dispose() {

    }
}
