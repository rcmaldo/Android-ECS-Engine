package ECS;

/**
 * Created by rcmal on 31/01/2018.
 */

abstract class Component {

    private int id; //Each entity has its unique id
    protected Script script;

    public Component(int id) {
        this.id = id;
        script = null;
    }

    public int getId() {
        return id;
    }

    //Returns false if a script already exists
    public boolean attachScript(Script s) {
        if (script == null) {
            return false;
        }
        script = s;
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!this.getClass().equals(o.getClass())) {
            return false;
        }

        return id == ((Component)o).getId();
    }

    @Override
    public int hashCode() {
        return id;
    }
}
