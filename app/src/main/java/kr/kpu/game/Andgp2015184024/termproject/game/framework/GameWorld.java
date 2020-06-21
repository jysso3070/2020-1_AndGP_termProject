package kr.kpu.game.Andgp2015184024.termproject.game.framework;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import kr.kpu.game.Andgp2015184024.termproject.game.iface.GameObject;
import kr.kpu.game.Andgp2015184024.termproject.game.iface.Recyclable;
import kr.kpu.game.Andgp2015184024.termproject.game.obj.ScreenObject;

public abstract class GameWorld {
    private static final String TAG = GameWorld.class.getSimpleName();
    protected View view;
    protected long frameTimeNanos;
    protected long timeDiffNanos;
    protected RecyclePool recyclePool = new RecyclePool();


    public static GameWorld get(){
        if(singleton == null){
//            singleton = new GameWorld();
//            Log.e(TAG, "Gameworld subclass not created");
        }
        return singleton;
    }
    protected static GameWorld singleton;

    protected Rect rect;

    protected GameWorld() {
    }

    public RecyclePool getRecyclePool(){
        return this.recyclePool;
    }

    public ArrayList<GameObject> objectsAt(int index) {
        return layers.get(index);
    }

    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public void initResource(View view) {
        this.view = view;
        initLayers();
        initObjects();
    }

    protected void initLayers() {
        layers = new ArrayList<>();
        int LayerCount = getLayerCount();
        for(int i = 0; i < LayerCount; i++){
            ArrayList<GameObject> layer = new ArrayList<>();
            layers.add(layer);
        }
    }

    abstract protected int getLayerCount();

    public void initObjects() {
    }

    protected ArrayList<ArrayList<GameObject>> layers;

    public void draw(Canvas canvas) {
        for(ArrayList<GameObject> layer : layers){
            for(GameObject o : layer) {
                o.draw(canvas);
            }
        }
//        canvas.drawText("score: "+ scoreDisplay, 100, 100, scorePaint);
    }

    public long getTimeDiffNanos() {
        return timeDiffNanos;
    }
    public float getTimeDiffInSecond(){
        return (float) (timeDiffNanos / 1000000000.0);
    }

    public long getCurrentTimeNanos() {
        return frameTimeNanos;
    }

    public void update(long frameTimeNanos) {
        this.timeDiffNanos = frameTimeNanos - this.frameTimeNanos;
        this.frameTimeNanos = frameTimeNanos;

        if(rect == null){
            return;
        }

        for(ArrayList<GameObject> layer : layers){
            for(GameObject o : layer) {
//                if(o instanceof ScreenObject){
//                    o.update();
//                }
                o.update();
            }
        }

        //objects.removeAll(trash);
        if(trash.size() > 0){
            removeTrashObjects();
        }
        trash.clear();
    }

    protected void removeTrashObjects() {

        for (int tIndex = trash.size() - 1; tIndex >= 0; tIndex--){
            GameObject tObj = trash.get(tIndex);
            for(ArrayList<GameObject> objects : layers){
                int index = objects.indexOf(tObj);
                if(index >= 0) {
                    objects.remove(index);
                    break;
                }
            }
            trash.remove(tIndex);
            if(tObj instanceof Recyclable) {
                ((Recyclable) tObj).recycle();
                getRecyclePool().add(tObj);
            }
        }
    }

    public void setRect(Rect rect) {
//        boolean first = this.rect == null;
        this.rect = rect;
//        if(first){
//            initObjects();
//        }
    }
    public int getLeft(){
        return rect.left;
    }
    public int getTop(){
        return rect.top;
    }
    public int getRight(){
        return rect.right;
    }
    public int getBottom(){
        return rect.bottom;
    }



    public Resources getResources() {
        return view.getResources();
    }

    public void add(final int index, final GameObject obj) {
        view.post(new Runnable() {
            @Override
            public void run() {
                ArrayList<GameObject> objects = layers.get(index);
                objects.add(obj);
            }
        });
    }

    protected ArrayList<GameObject> trash = new ArrayList<>();
    public void remove(GameObject obj) {
        trash.add(obj);
    }

    public Context getContext() {
        return view.getContext();
    }

    public void pause() {

    }

    public void resume() {

    }
}
