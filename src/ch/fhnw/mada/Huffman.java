package src.ch.fhnw.mada;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Huffman {
  private String input;
  private TreeNode root;
  private int totalAmount;
  // private HashMap<Integer, Integer> codeTable = new ArrayList<>();

  public Huffman(String input) {
    this.input = input;
    this.totalAmount = input.length();
    ArrayList<TreeNode> probabilityTable = createProbabilityTable()
        .stream()
        .filter(ap -> ap.getAmount() > 0)
        .collect(Collectors.toCollection(ArrayList::new));

    createTree(probabilityTable);
    System.out.println(root.getAmount());
    System.out.println(root.getProbability());
    System.out.println(root);

  }

  private ArrayList<TreeNode> createProbabilityTable() {
    ArrayList<TreeNode> probabilityTable = new ArrayList<>();

    Arrays.asList(input.split("")).forEach(c -> {
      int ascii = (int) c.charAt(0);
      if (probabilityTable.stream().anyMatch(ap -> ap.getAscii() == ascii)) {
        probabilityTable.stream()
            .filter(ap -> ap.getAscii() == ascii)
            .findFirst()
            .get()
            .increment();
      } else {
        probabilityTable.add(new TreeNode(ascii, 1, totalAmount));
      }
    });

    return probabilityTable;
  }

  private void createTree(ArrayList<TreeNode> currentTable) {
    currentTable.sort((a, b) -> a.getAmount() - b.getAmount());

    if (currentTable.size() == 1) {
      TreeNode root = currentTable.get(0);
      this.root = root;
      return;
    }

    ArrayList<TreeNode> smallestTwo = currentTable.stream()
        .limit(2)
        .collect(Collectors.toCollection(ArrayList::new));

    TreeNode parent = new TreeNode(smallestTwo.get(0), smallestTwo.get(1), totalAmount);
    currentTable.add(parent);
    currentTable.remove(0);
    currentTable.remove(0);

    createTree(currentTable);
  }
}