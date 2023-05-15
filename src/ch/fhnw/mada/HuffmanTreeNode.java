package src.ch.fhnw.mada;

public class HuffmanTreeNode extends TreeNode {
  private int ascii;
  private String asciiString;
  private int amount;
  private int totalAmount;

  /**
   * Creates a new leaf node
   */
  public HuffmanTreeNode(int ascii, int amount, int totalAmount) {
    this.ascii = ascii;
    this.asciiString = String.valueOf((char) ascii);
    this.amount = amount;
    this.totalAmount = totalAmount;
  }

  /**
   * Creates a new internal node
   */
  public HuffmanTreeNode(HuffmanTreeNode left, HuffmanTreeNode right, int totalAmount) {
    super(left, right);
    this.amount = left.getAmount() + right.getAmount();
    this.totalAmount = totalAmount;
  }

  /**
   * Used for debugging.
   */
  public String getAsciiString() {
    return asciiString;
  }

  public int getAscii() {
    return ascii;
  }

  /**
   * Used for debugging.
   * We prefered using the amount (int) because it is more precise than double.
   */
  public double getProbability() {
    return (double) amount / (double) totalAmount * 100.0;
  }

  public void increment() {
    amount++;
  }

  public int getAmount() {
    return amount;
  }

  @Override
  public HuffmanTreeNode getLeft() {
    return (HuffmanTreeNode) super.getLeft();
  }

  @Override
  public HuffmanTreeNode getRight() {
    return (HuffmanTreeNode) super.getRight();
  }
}
