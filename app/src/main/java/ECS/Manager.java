package ECS;

/**
 * Created by rcmal on 31/01/2018.
 */

abstract class Manager {

    public abstract void update();

    //Component listing method will vary, so keep this abstract.
    //Ex. CollisionMgr will list Components differently.
    public abstract Component getRefById(int find);
}
