package ECS.AABB;

import ECS.CollisionCmp;

public class AABBNode {

    //TODO: implement balance variable similar to AVL trees
    //TODO: then move on to fixing AABBTree

    private CollisionCmp collisionCmp;

    private AABBNode parentNode;
    private AABBNode leftNode;
    private AABBNode rightNode;

    private int height;             //height: leaf = 0, root = n

    //---------AABB SECTION---------
    private double minX, maxX;
    private double minY, maxY;
    //---------AABB SECTION---------

    public AABBNode(AABBNode parent) {
        collisionCmp = null;
        parentNode = parent;
        height = -1;
        leftNode = null;
        rightNode = null;
    }

    public AABBNode(AABBNode parent, CollisionCmp collisionCmp) {
        this.collisionCmp = collisionCmp;
        collisionCmp.setNode(this);
        parentNode = parent;
        height = -1;
        leftNode = null;
        rightNode = null;
    }

    //----------------START OF SETTERS/GETTERS-------------

    public CollisionCmp getCollisionCmp() {
        return collisionCmp;
    }

    public void setCollisionCmp(CollisionCmp collisionCmp) {
        this.collisionCmp = collisionCmp;
        collisionCmp.setNode(this);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public AABBNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(AABBNode parent) {
        parentNode = parent;
    }

    public AABBNode getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(AABBNode left) {
        leftNode = left;
        if (rightNode != null) {
            resetBorders(leftNode, rightNode);
        }
    }

    public AABBNode getRightNode() {
        return rightNode;
    }

    public void setRightNode(AABBNode right) {
        rightNode = right;
        if (leftNode != null) {
            resetBorders(leftNode, rightNode);
        }
    }

    public double getMinX() {
        if (!isLeaf()) {
            return minX;
        } else {
            return collisionCmp.getHitBox().left;
        }
    }

    public double getMaxX() {
        if (!isLeaf()) {
            return maxX;
        } else {
            return collisionCmp.getHitBox().right;
        }
    }

    public double getMinY() {
        if (!isLeaf()) {
            return minY;
        } else {
            return collisionCmp.getHitBox().top;
        }
    }

    public double getMaxY() {
        if (!isLeaf()) {
            return maxY;
        } else {
            return collisionCmp.getHitBox().bottom;
        }
    }

    //----------------END OF SETTERS/GETTERS-------------------

    public boolean isLeaf() {
        return leftNode == null && rightNode == null;
    }

    private void resetBorders(AABBNode left, AABBNode right) {
        /*
        if (left != null && right != null) {
        */
        minX = Math.min(left.getMinX(), right.getMinX());
        maxX = Math.max(left.getMaxX(), right.getMaxX());
        minY = Math.min(left.getMinY(), right.getMinY());
        maxY = Math.max(left.getMaxY(), right.getMaxY());
        /*
        }
        */

        /*
        else if (right == null) {
            minX = left.minX;
            maxX = left.maxX;
            minY = left.minY;
            maxY = left.maxY;
        } else { //left is null
            minX = right.minX;
            maxX = right.maxX;
            minY = right.minY;
            maxY = right.maxY;
        }
        */

    }

    //package private
    //balance = rightnode height - leftnode height
    private int getBalance() {
        if (rightNode == null) {
            if (leftNode == null) {
                return 0;
            } else {
                return -leftNode.height;
            }
        } else {
            if (leftNode == null) {
                return rightNode.height;
            } else {
                return rightNode.height - leftNode.height;
            }
        }
    }

    public double getSurfaceArea() {
        return (getMaxX() - getMinX()) * (getMaxY() - getMinY());
    }

    public double getHalfPerimeter() { //"Half" to eliminate unnecessary multiplication of 2
        return (getMaxX() - getMinX()) + (getMaxY() - getMinY());
    }

    //------------------ STATIC METHODS --------------------

    public static double getMergedSurfaceArea(AABBNode a, AABBNode b) {
        return (Math.max(a.getMaxX(), b.getMaxX()) - Math.min(a.getMinX(), b.getMinX())) *
                (Math.max(a.getMaxY(), b.getMaxY()) - Math.min(a.getMinY(), b.getMinY()));
    }

    //"Half" to eliminate unnecessary multiplication of 2
    public static double getMergedHalfPerimeter(AABBNode a, AABBNode b) {
        return (Math.max(a.getMaxX(), b.getMaxX()) - Math.min(a.getMinX(), b.getMinX())) +
                (Math.max(a.getMaxY(), b.getMaxY()) - Math.min(a.getMinY(), b.getMinY()));
    }

    public static boolean intersects(AABBNode a, AABBNode b) { //returns true on line/edge intersection
        return a.getMaxX() >= b.getMinX() &&
                a.getMinX() <= b.getMaxX() &&
                a.getMaxY() >= b.getMinY() &&
                a.getMinY() <= b.getMaxY();
    }
}
