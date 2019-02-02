package ECS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rcmal on 31/01/2018.
 */

public class CoordinatesMgr extends Manager {

    private Map<Integer, CoordinatesCmp> list;

    public CoordinatesMgr() {
        list = new HashMap<>();
    }

    public CoordinatesCmp add(int id) {
        CoordinatesCmp result = new CoordinatesCmp(id);
        list.put(id, result);
        return result;
    }

    @Override
    public void update() {
        for (Map.Entry entry : list.entrySet()) {
            CoordinatesCmp c = (CoordinatesCmp)(entry.getValue());
            c._vx += c._ax;
            c._vy += c._ay;

            c._x += c._vx;
            c._y += c._vy;
        }
    }

    //Note: Might be relatively heavy. DO NOT USE DURING GAMEPLAY.
    @Override
    public Component getRefById(int find) {
        return list.get(find);
    }
}
