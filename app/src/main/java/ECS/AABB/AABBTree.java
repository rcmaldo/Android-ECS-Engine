package ECS.AABB;

import java.util.Stack;

public class AABBTree {

    private Stack<CollisionPair> callStack; //stack from CollisionMgr parent
    private AABBNode root; //tree root

    public AABBTree(Stack<CollisionPair> callStack) {
        this.callStack = callStack;
        root = null;
    }

    public void insertLeaf() {
        //TODO
    }

    public void removeLeaf() {
        //TODO
    }

    //rotate counter-clockwise
    private AABBNode rotateNodeLeft(AABBNode root) {
        if (root == null) return null;

        AABBNode parent = root.getParentNode();
        AABBNode right = root.getRightNode();
        AABBNode center;

        //unconventional fix for the case where right-left height > right-right
        if (right.getLeftNode().getHeight() > right.getRightNode().getHeight()) {
            center = right.getRightNode();
            right.setRightNode(right.getLeftNode());
        } else {
            center = right.getLeftNode();
        }

        root.setRightNode(center);
        if (center != null) center.setParentNode(root);

        right.setLeftNode(root);
        root.setParentNode(right);

        if (parent != null) {
            if (parent.getLeftNode() == root) {
                parent.setLeftNode(right);
            } else {
                parent.setRightNode(right);
            }
            right.setParentNode(parent);
        }

        return right; //new rootNode
    }

    //rotate clockwise
    private AABBNode rotateNodeRight(AABBNode root) {
        if (root == null) return null;

        AABBNode parent = root.getParentNode();
        AABBNode left = root.getRightNode();
        AABBNode center = left.getRightNode();

        //unconventional fix for the case where right-left height > right-right
        if (left.getRightNode().getHeight() > left.getLeftNode().getHeight()) {
            center = left.getLeftNode();
            left.setLeftNode(left.getRightNode());
        } else {
            center = left.getRightNode();
        }

        root.setLeftNode(center);
        if (center != null) center.setParentNode(root);

        left.setRightNode(root);
        root.setParentNode(left);

        if (parent != null) {
            if (parent.getLeftNode() == root) {
                parent.setLeftNode(left);
            } else {
                parent.setRightNode(left);
            }
            left.setParentNode(parent);
        }

        return left; //new rootNode
    }

    private void recalculateHeights() {
        root.setHeight(0);
        recalculateHeights(root.getLeftNode());
        recalculateHeights(root.getRightNode());

    }

    private void recalculateHeights(AABBNode node) {
        if (node != null) {
            node.setHeight(node.getParentNode().getHeight() + 1);
            recalculateHeights(node.getLeftNode());
            recalculateHeights(node.getRightNode());
        }
    }

    private void rebalance() {
        rebalance(root);

        //then call recalc height?? not calling can save performance
    }

    private int rebalance(AABBNode node) {
        if (node == null) return 0;

        int leftHeight = rebalance(node.getLeftNode());
        int rightHeight = rebalance(node.getRightNode());


        int balFactor = rightHeight - leftHeight;

        while (balFactor < -1 || balFactor > 1) {
            if (balFactor < -1) {
                node = rotateNodeRight(node);
                leftHeight--;
                rightHeight++;

            }
            if (balFactor > 1) {
                node = rotateNodeLeft(node);
                leftHeight++;
                rightHeight--;
            }
            balFactor = rightHeight - leftHeight;

        }

        node.setHeight(Math.max(leftHeight, rightHeight) + 1);

        return node.getHeight();

    }

    public void update() {
        //TODO: complete frame-by-frame update, fat-AABBs
    }
}
