package ECS;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rcmal on 09/02/2018.
 */

//Note that this is optimized for Android devices.
    //Android dependent functions are marked with //Android

public class SpriteCmp extends Component {

    private Bitmap bitmap;                          //Android

    private Rect frame;                //Android (Rect)
    private CoordinatesCmp coordinates;

    private int zIndex;
    private boolean isVisible;

    public SpriteCmp(int id, CoordinatesCmp c) {
        super(id);
        bitmap = null;
        frame = new Rect (0, 0, 0, 0);
        coordinates = c;
        zIndex = 0;
        isVisible = true;
    }

    public Rect getFrame() { return frame; } //Android

    public CoordinatesCmp getCoordinates() {
        return coordinates;
    }

    //---v---SPECIAL PROPERTIES BELOW---v---
    public void animate() { //For Overriding
        return;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setFrame(Rect frame) {   //Android
        this.frame = frame;
    }

    public void setZ (int z) {
        zIndex = z;
    }

    public void setFrame(double x, double y, double w, double h) {  //Android
        frame = new Rect((int)x, (int)y, (int)(x+w), (int)(y+h));
    }

    public int getZ() {
        return zIndex;
    }

    public Rect getSrcRect() { //Android
        return frame;
    }

    public Rect getDstRect() { //Android
        return new Rect(
                (int) coordinates._x,
                (int) coordinates._y,
                (int) (coordinates._x+coordinates._w),
                (int) (coordinates._y+coordinates._h));
    }

    public Bitmap getBitmap() {                     //Android
        return bitmap;
    }

    public void show() { isVisible = true; }
    public void hide() { isVisible = false; }

    public boolean visible() { return isVisible; }
    //---^---SPECIAL PROPERTIES ABOVE---^---
}
