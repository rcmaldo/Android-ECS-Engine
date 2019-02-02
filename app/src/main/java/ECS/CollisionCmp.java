package ECS;

import android.graphics.Rect;

import ECS.AABB.AABBNode;

public class CollisionCmp extends Component {

    private double _x, _y, _w, _h; //frame is relative to corresponding coordinatesCmp
    private CoordinatesCmp coordinates;
    private AABBNode node;

    public CollisionCmp(int id, CoordinatesCmp c) {
        super(id);

        //Set frame to default (equal to corresponding coordinatesCmp)
        _x = 0;
        _y = 0;
        _w = c._w;
        _h = c._h;

        coordinates = c;
    }

    public void setHitBox(double x, double y, double w, double h) {
        _x = x; _y = y;
        _w = w; _h = h;
    }

    public void setRelativeHitBox(Rect hitBox) { //Android
        _x = hitBox.left;
        _y = hitBox.top;
        _w = hitBox.right - hitBox.left;
        _h = hitBox.bottom - hitBox.top;
    }

    public Rect getRelativeHitBox() {
        Rect relHitBox = new Rect((int)_x,
                                (int)_y,
                                (int)(_x + _w),
                                (int)(_y + _h));
        return relHitBox;
    }

    public Rect getHitBox() {
        Rect hitBox = new Rect((int)(coordinates._x + _x),
                            (int)(coordinates._y + _y),
                            (int)(coordinates._x + _x + _w),
                            (int)(coordinates._y + _y + _h));
        return hitBox;
    }

    public void setNode(AABBNode node) {
        this.node = node;
    }

    public AABBNode getNode() {
        return node;
    }

    public void onCollision(CollisionCmp other) {
        return; //stub for overriding
    }
}
