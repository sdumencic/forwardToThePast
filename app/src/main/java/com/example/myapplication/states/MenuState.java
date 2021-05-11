package com.example.myapplication.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuState extends State{
    private Texture background;
    private Texture playBtn;
    public MenuState(GameStateManager gsm) {
        super(gsm);
        //background = new Texture()
        playBtn = new Texture("playBtn.png");
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {
        if(Gdx.input.justTouched()){
            gsm.set(new PlayState(gsm));
            dispose();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(playBtn, Gdx.graphics.getWidth()/2 - playBtn.getWidth()/2 , Gdx.graphics.getHeight()/2 - playBtn.getHeight()/2);
        sb.end();
    }

    @Override
    public void dispose() {
        playBtn.dispose();
    }
}
