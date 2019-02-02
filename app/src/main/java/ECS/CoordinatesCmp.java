package ECS;

/**
 * Created by rcmal on 31/01/2018.
 */

public class CoordinatesCmp extends Component {

    //Don't use Android Rect, as it limits coordinates to integers
    public double _x, _y, _w, _h; //public for later readability; underscore implies variable, NOT function
    public double _vx, _vy; //velocity components
    public double _ax, _ay; //acceleration

    public CoordinatesCmp(int id) {
        super(id);
        _x = 0; _y = 0; _w = 0; _h = 0;
        _vx = 0; _vy = 0;
        _ax = 0; _ay = 0;
    }
}
