package kr.jysso3070.game.Andgp.strikers5491.ui.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import kr.jysso3070.game.Andgp.strikers5491.game.sensor.GyroSensor;
import kr.jysso3070.game.Andgp.strikers5491.game.world.MainWorld;
import kr.jysso3070.game.Andgp.strikers5491.res.sound.BGSound;
import kr.jysso3070.game.Andgp.strikers5491.res.sound.SoundEffects;
import kr.jysso3070.game.Andgp.strikers5491.ui.view.GameView;

public class MainActivity extends AppCompatActivity {

    private static final long GAMEVIEW_UPDATE_INTERVAL_MSEC = 30;
    private static final String TAG = MainActivity.class.getSimpleName();
    private GameView gameView;
    private BGSound bgSound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        GyroSensor.get().setActivity(this);
        GyroSensor.get().connetSensor();

        SoundEffects se = SoundEffects.get();
        se.init(this); // 최소 생성자
        se.ladAll();

        bgSound = BGSound.get();
        bgSound.init(this);

        MainWorld.create();
        gameView = new GameView(this);
        setContentView(gameView);




//        gameView = findViewById(R.id.gameView);

//        postUpdate();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        Log.d(TAG, "new configuration: "+ newConfig);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPause() {
        gameView.pause();
        bgSound.stop();
//        GameWorld.get().pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        gameView.resume();
//        GameWorld.get().resume();
        super.onResume();
    }

    //    private void postUpdate() {
//        gameView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                gameView.update();
//                gameView.invalidate();
//                postUpdate();
//            }
//        }, GAMEVIEW_UPDATE_INTERVAL_MSEC);
//    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN){
//            gameView.doAction();
//        }
//        return true;
//    }
}
