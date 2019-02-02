package ECS;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rcmal on 31/01/2018.
 */

public class MainManager {

    private static MainManager mainManager_instance = null;

    private List<Manager> managerList;
    private int ID_COUNT;

    private String log = "";

    private int frameRate;
    private int framePeriod; //milliseconds per frame

    private int frameWidth;
    private int frameHeight;

    private double scaleProp; //For screen fitting

    //---v---INDEX LIST BELOW---v---
    public static final int SPRITE = 0;
    public static final int COORDINATES = 1;
    //---^---INDEX LIST ABOVE---^---

    public static MainManager initialize(SurfaceHolder surfaceHolder, int frameRate, int frameWidth, int frameHeight) {
        if (mainManager_instance == null) {
            mainManager_instance = new MainManager(surfaceHolder, frameRate, frameWidth, frameHeight);
            surfaceHolder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                    SpriteMgr.getInstance().updateValues(surfaceHolder.getSurfaceFrame().right, surfaceHolder.getSurfaceFrame().bottom);
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
                    SpriteMgr.getInstance().updateValues(width, height);
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

                }
            });
        } else {
            mainManager_instance.log += "MainMgr already initialized!\n";
        }
        return mainManager_instance;
    }

    //NOTE: Requires call to initialize before getInstance.
    public static MainManager getInstance() {
        return mainManager_instance;
    }

    //Add the individual managers manually here
    private MainManager(SurfaceHolder surfaceHolder, int frameRate, int frameWidth, int frameHeight) {
        framePeriod = 1000/frameRate;

        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;

        ID_COUNT = 0;
        managerList = new ArrayList<>();                    //Indexes:
        managerList.add(SpriteMgr.initialize(surfaceHolder,
                frameWidth, frameHeight));      //Sprite Manager = 0
        managerList.add(new CoordinatesMgr());              //Coordinates Manager = 1
    }

    public int newID() {
        ID_COUNT++;
        return ID_COUNT - 1;
    }

    public void update() {
        long startTime = System.currentTimeMillis();

        for (int i = 1; i < managerList.size(); i++) {
            assert(i != SPRITE);
            managerList.get(i).update();
        }

        //Separate SpriteMgr in case of frame skips

        managerList.get(SPRITE).update();

        long elapsedTime = System.currentTimeMillis() - startTime;
        try{
            if (framePeriod - elapsedTime > 0) {
                Thread.sleep(framePeriod - elapsedTime);
            }
        } catch (InterruptedException e){
            //Ignore
        }
    }

    //Note: Might be heavy. Use sparingly.
    public Component getRefById(int id, int index) {
        return managerList.get(index).getRefById(id);
    }

    //May be needed for collision scripts
    public Manager getManager(int index) {
        return managerList.get(index);
    }

    //ADD COMPONENT methods will return null if a duplicate id is found in their respective lists

    //---v---PUT ALL ADD COMPONENT METHODS BELOW---v---

    //Coordinates Component
    public CoordinatesCmp addCoordinates(int id) {
        return ((CoordinatesMgr)(managerList.get(COORDINATES))).add(id);
    }

    //Sprite Component
    public SpriteCmp addSprite(int id) {
        CoordinatesCmp coordinatesCmp = (CoordinatesCmp)(getRefById(id, COORDINATES));
        if (coordinatesCmp == null) {
            log += "addSprite Error: Missing Corresponding CoordinatesCmp\n";
            return null;
        }
        return ((SpriteMgr)(managerList.get(SPRITE))).add(id, coordinatesCmp);
    }

    public SpriteCmp addSprite(int id, CoordinatesCmp coordinatesCmp) {
        if (coordinatesCmp.getId() != id) {
            log += "addSprite Error: Invalid ID with CoordinatesCmp\n";
            return null;
        }
        return ((SpriteMgr)(managerList.get(SPRITE))).add(id, coordinatesCmp);
    }

    //---^---PUT ALL ADD COMPONENT METHODS ABOVE---^---
}
