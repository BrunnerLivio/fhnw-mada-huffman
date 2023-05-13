package src.ch.fhnw.mada;

public class TreeNode {
  private TreeNode left;
  private TreeNode right;
  private int ascii;
  private String asciiString;
  private int amount;
  private int totalAmount;

  public TreeNode(int ascii, int amount, int totalAmount) {
    this.ascii = ascii;
    this.asciiString = String.valueOf((char) ascii);

    this.amount = amount;
    this.totalAmount = totalAmount;
  }

  public TreeNode(TreeNode left, TreeNode right, int totalAmount) {
    this.left = left;
    this.right = right;
    this.amount = left.getAmount() + right.getAmount();
    this.totalAmount = totalAmount;
  }

  public String getAsciiString() {
    return asciiString;
  }

  public TreeNode getLeft() {
    return left;
  }

  public void setLeft(TreeNode left) {
    this.left = left;
  }

  public TreeNode getRight() {
    return right;
  }

  public void setRight(TreeNode right) {
    this.right = right;
  }

  public int getAscii() {
    return ascii;
  }

  public void setAscii(int ascii) {
    this.ascii = ascii;
  }

  public double getProbability() {
    return (double) amount / (double) totalAmount * 100.0;
  }

  public void increment() {
    amount++;
  }

  public int getAmount() {
    return amount;
  }

  public String toString() {
    return "Ascii: " + ascii + " Amount: " + amount + " Probability: " + getProbability() + "\n" + left + "\n" + right;
  }
}
