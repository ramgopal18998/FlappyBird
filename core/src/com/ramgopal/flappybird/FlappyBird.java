package com.ramgopal.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture[] birds;
	Texture toptube;
	Texture bottomtube;
	Texture background;
	int flapState = 0;
    float birdY = 0;
    float velocity = 0;
    float gap = 200;
    Random randomGenerator;
    float tubeVelocity = 4;
    int numberoftubes = 4;
    float maxoffset;
    float[] xtube = new float[numberoftubes];
    float[] tubeoffset = new float[numberoftubes];
    int gameState = 0;
    float gravity = 1.1f;
    float distancebetweentubes;
    Circle birdcircle;
    ShapeRenderer shapeRenderer;
    Rectangle[] topShape = new Rectangle[numberoftubes];
    Rectangle[] bottomShape = new Rectangle[numberoftubes];
    int score;
    int scoringTube;
    BitmapFont font;
    Texture gameover;
    BitmapFont maxscore;
    int highscore;
    Preferences preferences;



	@Override
	public void create () {
		batch = new SpriteBatch();
		background =new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");
        toptube = new Texture("toptube.png");
        bottomtube = new Texture("bottomtube.png");
        gameover = new Texture("flappy.jpg");
        birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;
        maxoffset = Gdx.graphics.getHeight()/2 - gap/2 -100;
        distancebetweentubes = Gdx.graphics.getWidth() * 15 / 16;

        preferences = Gdx.app.getPreferences("My Preferences");
        try
        {
            highscore = preferences.getInteger("score",0);
        }catch (Exception e)
        {}

        randomGenerator = new Random();
        birdcircle = new Circle();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().scale(5);
        maxscore = new BitmapFont();
        maxscore.setColor(Color.RED);
        maxscore.getData().scale(2);


        for (int i=0;i<numberoftubes;i++)
        {
            topShape[i] = new Rectangle();
            bottomShape[i] = new Rectangle();
            tubeoffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
            xtube[i] = Gdx.graphics.getWidth()+ 300 + - toptube.getWidth()/2 + i*distancebetweentubes;
        }


	}

	@Override
	public void render () {

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (gameState == 1) {
            if(xtube[scoringTube] < Gdx.graphics.getWidth()/2)
            {
                score++;
                scoringTube = (scoringTube + 1)%4;
            }




            if (Gdx.input.justTouched()) {

                velocity = -12;


            }
            for(int i=0;i<numberoftubes;i++)
            {

                if(xtube[i] < - toptube.getWidth())
                {
                    xtube[i] = xtube[i] + numberoftubes*distancebetweentubes;
                    tubeoffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

                }
                else
                    xtube[i] =xtube[i] - tubeVelocity;

                batch.draw(toptube, xtube[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i],toptube.getWidth()/1.5f,toptube.getHeight());
                batch.draw(bottomtube, xtube[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeoffset[i],bottomtube.getWidth()/1.5f,bottomtube.getHeight());

                topShape[i].set(xtube[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i],toptube.getWidth(),toptube.getHeight());
                bottomShape[i].set(xtube[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeoffset[i],bottomtube.getWidth(),bottomtube.getHeight());


            }

            if (birdY > 0) {

                velocity = velocity + gravity;
                birdY -= velocity;

            }
            else
            {
                gameState = 2;
            }

        } else if(gameState == 0){

            if (Gdx.input.justTouched()) {

                gameState = 1;


            }


        }else if(gameState == 2)
        {
            batch.draw(gameover,Gdx.graphics.getWidth()/2-gameover.getWidth()/2,Gdx.graphics.getHeight()/2-gameover.getHeight()/2);

            if(score > highscore)
            {
                highscore = score;
                preferences.putInteger("score",score);
                preferences.flush();
            }
            Gdx.app.log("highscore",Integer.toString(highscore));

            if (Gdx.input.justTouched()) {

                gameState = 1;
                birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;
                for (int i=0;i<numberoftubes;i++)
                {
                    topShape[i] = new Rectangle();
                    bottomShape[i] = new Rectangle();
                    tubeoffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
                    xtube[i] = Gdx.graphics.getWidth()+ 300 +  - toptube.getWidth()/2 + i*distancebetweentubes;
                }
                velocity = 0;
                score =0;
                scoringTube =0;

            }
        }

        if (flapState == 0) {
            flapState = 1;
        } else {
            flapState = 0;
        }


        batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[0].getWidth() / 2, birdY);
        font.draw(batch,String.valueOf(score),100,100);
        maxscore.draw(batch,"High Score " + String.valueOf(highscore),100,Gdx.graphics.getHeight() - 30);
        batch.end();
        birdcircle.set(Gdx.graphics.getWidth()/2,birdY+birds[flapState].getHeight()/2,birds[flapState].getWidth()/2);

        for (int i=0;i<numberoftubes;i++)
        {
            if(Intersector.overlaps(birdcircle,topShape[i]) ||Intersector.overlaps(birdcircle,bottomShape[i]))
            {
                gameState =2;
            }
        }
	}


}
