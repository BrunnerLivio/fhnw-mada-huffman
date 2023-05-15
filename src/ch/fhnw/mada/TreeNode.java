package src.ch.fhnw.mada;

public class TreeNode {
  private TreeNode left;
  private TreeNode right;

  public TreeNode() {
  }

  public TreeNode(TreeNode left, TreeNode right) {
    this.left = left;
    this.right = right;
  }

  public TreeNode getLeft() {
    return left;
  }

  public TreeNode getRight() {
    return right;
  }
}
