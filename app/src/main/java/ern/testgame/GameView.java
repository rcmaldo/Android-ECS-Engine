package ern.testgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

import ECS.CoordinatesCmp;
import ECS.MainManager;
import ECS.SpriteCmp;

/**
 * Created by rcmal on 07/01/2018.
 */

public class GameView extends SurfaceView implements Runnable {

    private final int FRAME_RATE = 12;
    private final int FRAME_WIDTH = 800;
    private final int FRAME_HEIGHT = 480;

    volatile boolean playing;
    private Thread gameThread = null;

    private Player player;

    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    private MainManager mainManager;

    public GameView(Context context) {
        super(context);

        playing = true;

        surfaceHolder = getHolder();

        //TESTING FIELD

        mainManager = MainManager.initialize(surfaceHolder, FRAME_RATE, FRAME_WIDTH, FRAME_HEIGHT);
        int entityID0 = mainManager.newID();
        CoordinatesCmp coords = mainManager.addCoordinates(entityID0);
        SpriteCmp sprite = mainManager.addSprite(entityID0);
        sprite.setFrame(0,0,128,92);
        coords._w = 128;
        coords._h = 92;
        coords._vy = 0;
        coords._y = 0;

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inScaled = false;
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.player, opts);
        sprite.setBitmap(bm);

        int entityID1 = mainManager.newID();
        CoordinatesCmp coords1 = mainManager.addCoordinates(entityID1);
        SpriteCmp sprite1 = mainManager.addSprite(entityID1);
        sprite1.setFrame(0,0,128,92);
        coords1._w = 128;
        coords1._h = 92;
        coords1._x = 64;
        coords1._y = 300;

        sprite1.setBitmap(bm);

        //TESTING FIELD
    }

    @Override
    public void run() {
        while (playing) {
            mainManager.update();
        }
    }

    private void update() {
        player.update();
    }

    private void draw() {
        //Check surface validity
        if (surfaceHolder.getSurface().isValid()) {
            //Lock canvas
            canvas = surfaceHolder.lockCanvas();
            //Draw background color
            canvas.drawColor(Color.BLACK);
            //Draw player
            canvas.drawBitmap(
                    player.getBitmap(),
                    player.getX(),
                    player.getY(),
                    paint);
            //Unlock canvas
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
}
