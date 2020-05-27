package kr.kpu.game.Andgp2015184024.termproject.game.world;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Random;

import kr.kpu.game.Andgp2015184024.termproject.R;
import kr.kpu.game.Andgp2015184024.termproject.game.framework.GameWorld;
import kr.kpu.game.Andgp2015184024.termproject.game.iface.GameObject;
import kr.kpu.game.Andgp2015184024.termproject.game.obj.Ball;
import kr.kpu.game.Andgp2015184024.termproject.game.obj.EnemyGenerator;
import kr.kpu.game.Andgp2015184024.termproject.game.obj.Fighter;
import kr.kpu.game.Andgp2015184024.termproject.game.obj.Joystick;
import kr.kpu.game.Andgp2015184024.termproject.game.obj.Plane;
import kr.kpu.game.Andgp2015184024.termproject.game.obj.ScoreObject;
import kr.kpu.game.Andgp2015184024.termproject.game.obj.bg.ImageScrollBackground;
import kr.kpu.game.Andgp2015184024.termproject.game.obj.bg.TileScrollBackground;

public class MainWorld extends GameWorld {
    private static final int BALL_COUNT = 10;
    public static final String PREF_KEY_HIGHSCORE = "highscore";
    public static final String PREFS_NAME = "Prefs";
    private static final String TAG = MainWorld.class.getSimpleName();
    private Fighter fighter;
    private EnemyGenerator enemyGenerator = new EnemyGenerator();
    private Plane plane;
    private ScoreObject scoreObject;
    private ScoreObject highScoreObject;
    private PlayState playState = PlayState.normal;
    private Joystick joystick;

    public static void create() {
        if(singleton != null){
            Log.e(TAG, "object already, created");
        }
        singleton = new MainWorld();
    }

    public static MainWorld get(){
        return (MainWorld) singleton;
    }

    private enum PlayState{
        normal, paused, gameOver
    }


    public enum Layer{
        bg, missile, enemy, player, ui, COUNT
    }

    protected int getLayerCount(){
        return Layer.COUNT.ordinal();
    }

    @Override
    public void initObjects() {
        Resources res = view.getResources();
//        objects = new ArrayList<>();
        Random rand = new Random();
        for(int i = 0; i < BALL_COUNT; i++){
            float x = rand.nextFloat() * 1000;
            float y = rand.nextFloat() * 1000;
            float dx = (float) (rand.nextFloat() * 50.0 - 25.0);
            float dy = (float) (rand.nextFloat() *50.0 - 25.0);
            add(Layer.missile, new Ball(x, y ,dx, dy));
        }
        float playerY = rect.bottom - 100;
        plane = new Plane(500, playerY, 0.0f, 0.0f);
        add(Layer.player, plane);
        fighter = new Fighter(200, 700);
        add(Layer.player, fighter);

        scoreObject = new ScoreObject(800, 100, R.mipmap.number_64x84);
        add(Layer.ui, scoreObject);
        highScoreObject = new ScoreObject(800, 100, R.mipmap.number_24x32);
        add(Layer.ui, highScoreObject);

//        add(Layer.bg, new ImageScrollBackground(R.mipmap.bg_city,
//                ImageScrollBackground.Orientation.vertical, 25));
        add(Layer.bg, new TileScrollBackground(R.raw.earth,
                TileScrollBackground.Orientation.vertical, -25));
        add(Layer.bg, new ImageScrollBackground(R.mipmap.clouds,
                ImageScrollBackground.Orientation.vertical, 100));
//        scorePaint.setTextSize(100);
//        scorePaint.setColor(Color.BLACK);
//        scoreAnimator = ObjectAnimator.ofInt(this, "scoreDisplay", 0);
        joystick = new Joystick(300, rect.bottom - 400, Joystick.Direction.normal, 100);
        add(Layer.ui, joystick);

        plane.setJoystick(joystick);
        startGame();
    }

    public void add(Layer layer, final GameObject obj){
        add(layer.ordinal(), obj);
    }
    public ArrayList<GameObject> objectsAt(Layer layer) {
        return objectsAt(layer.ordinal());
    }

    private void startGame() {
        playState = PlayState.normal;
        scoreObject.reset();

        SharedPreferences prefs = view.getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int highScore = prefs.getInt(PREF_KEY_HIGHSCORE, 0);
        highScoreObject.setScore(highScore);

    }

    public void endGame() {
        playState = PlayState.gameOver;
        int score = scoreObject.getScore();
        SharedPreferences prefs = view.getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int highScore = prefs.getInt(PREF_KEY_HIGHSCORE, 0);
        if(score > highScore) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(PREF_KEY_HIGHSCORE, score);
            editor.commit();
            // 게임종료시 진행되야 할 것들
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        joystick.onTouchEvent(event);
        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN){
            if(playState == PlayState.gameOver){
                startGame();
                return false;
            }
            doAction();
//            plane.head(event.getX(), event.getY());
        } else if(action == MotionEvent.ACTION_MOVE){
//            plane.head(event.getX(), event.getY());
        }
        return true;
    }

    public void addScore(int score) {
        int value = scoreObject.addScore(score);
    }

    public void doAction() {
        fighter.fire();
    }

    @Override
    public void update(long frameTimeNanos) {
        super.update(frameTimeNanos);

        if(playState != PlayState.normal){
            return;
        }

        enemyGenerator.update();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }
}