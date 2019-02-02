package ECS;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by rcmal on 09/02/2018.
 */

//Note that this is optimized for Android devices.
//Android dependent functions are marked with //Android

    //design type: Singleton

public class SpriteMgr extends Manager {

    private static SpriteMgr spriteMgr_instance = null;

    private Map<Integer, SpriteCmp> list;
    private SurfaceHolder surfaceHolder;    //Android
    private Canvas canvas;                  //Android
    private Paint paint;                    //Android

    private int frameWidth;
    private int frameHeight;
    private double frameScaleFactor;        //Multiply this to coordinates for screen fitting
    private Rect frameRect;

    public static SpriteMgr initialize(SurfaceHolder surfaceHolder, int frameWidth, int frameHeight) {
        if (spriteMgr_instance == null) {
            spriteMgr_instance = new SpriteMgr(surfaceHolder, frameWidth, frameHeight);
        }
        return spriteMgr_instance;
    }

    public static SpriteMgr getInstance() {
        return spriteMgr_instance;
    }

    public static double calculateProportions(int frameWidth, int frameHeight, int newWidth, int newHeight) {
        //follow "smaller" side ratio
        double frameRatio = ((double)frameWidth)/((double)frameHeight);
        double canvasRatio = ((double)newWidth/((double)newHeight));
        double chosenProp;
        if (frameRatio < canvasRatio) {
            chosenProp = ((double)newHeight)/((double)frameHeight);
        } else {
            chosenProp = ((double)newWidth)/((double)frameWidth);
        }
        return chosenProp;
    }

    private SpriteMgr(SurfaceHolder surfaceHolder, int frameWidth, int frameHeight) {
        list = new HashMap<>();
        this.surfaceHolder = surfaceHolder;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        updateValues(surfaceHolder.getSurfaceFrame().right, surfaceHolder.getSurfaceFrame().bottom);

        paint = new Paint();
    }

    public void updateValues(int newWidth, int newHeight) {
        this.frameScaleFactor = calculateProportions(frameWidth, frameHeight, newWidth, newHeight);
        int frameLeft = (int)((newWidth - (frameScaleFactor*frameWidth))/2.0);
        int frameTop = (int)((newHeight - (frameScaleFactor*frameHeight))/2.0);
        int frameRight = (int)(frameScaleFactor*frameWidth)+frameLeft;
        int frameBottom = (int)(frameScaleFactor*frameHeight)+frameTop;

        frameRect = new Rect(frameLeft, frameTop, frameRight, frameBottom);
    }

    public SpriteCmp add(int id, CoordinatesCmp coordinatesCmp) {
        SpriteCmp result = new SpriteCmp(id, coordinatesCmp);
        list.put(id, result);
        return result;
    }

    @Override
    public void update() {
        if (surfaceHolder.getSurface().isValid()) {
            //Lock canvas
            canvas = surfaceHolder.lockCanvas();

            canvas.drawColor(Color.BLACK);

            Paint tmpPaint = new Paint();
            tmpPaint.setColor(Color.WHITE);

            canvas.drawRect(frameRect, tmpPaint);

            List<SpriteCmp> linkedList = new LinkedList(list.values());

            //Sort by zIndex then by y+h coordinates
            Collections.sort(linkedList, new Comparator<SpriteCmp>() {
                public int compare(SpriteCmp c1, SpriteCmp c2) {
                    if (c1.getZ() == c2.getZ()) {
                        return (int) ((c1.getCoordinates()._y + c1.getCoordinates()._h)
                                - (c2.getCoordinates()._y + c2.getCoordinates()._h));
                    } else {
                        return c1.getZ() - c2.getZ();
                    }
                }
            });

            for (SpriteCmp s : linkedList) {
                s.animate();
                Rect scaledRect = new Rect(frameRect.left+(int)(frameScaleFactor*s.getDstRect().left),
                        frameRect.top+(int)(frameScaleFactor*s.getDstRect().top),
                        frameRect.left+(int)(frameScaleFactor*s.getDstRect().right),
                        frameRect.top+(int)(frameScaleFactor*s.getDstRect().bottom));
                canvas.drawBitmap(s.getBitmap(), s.getSrcRect(), scaledRect, null);
            }

            //Unlock canvas
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public Component getRefById(int find) {
        return list.get(find);
    }
}
