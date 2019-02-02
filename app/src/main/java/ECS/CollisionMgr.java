package ECS;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import ECS.AABB.AABBTree;
import ECS.AABB.CollisionPair;

public class CollisionMgr extends Manager {

    private Map<Integer, CollisionCmp> list;
    private Stack<CollisionPair> callStack;
    private AABBTree tree;

    public CollisionMgr() {
        list = new HashMap<>();
        callStack = new Stack<>();
        tree = new AABBTree(callStack);
    }

    public CollisionCmp add(int id, CoordinatesCmp coordinatesCmp) {
        CollisionCmp result = new CollisionCmp(id, coordinatesCmp);
        list.put(id, result);
        return result;
    }

    @Override
    public void update() {
        //Update Tree
        //Pop stack and call Cmp's one by one
    }

    @Override
    public Component getRefById(int find) {
        return list.get(find);
    }

}