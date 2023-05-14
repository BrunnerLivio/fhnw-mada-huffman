package src.ch.fhnw.mada;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Huffman {
  private String input;
  private String output;
  private int totalAmount;
  private TreeNode root;
  private HashMap<Integer, String> codeTable;

  public Huffman(String input) {
    this.input = input;
    this.totalAmount = input.length();
    this.codeTable = generateCodeTable();
  }

  public Huffman(byte[] input, String codeTable) {
    this.output = getBinaryStringFromByteArray(input);
    this.codeTable = getCodeTableFromString(codeTable);
  }

  private String getBinaryStringFromByteArray(byte[] input) {
    String output = "";

    for (byte b : input) {
      output += String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
    }

    // Remove 1 and leading zeroes
    int lastIndex = output.length() - 1;
    while (lastIndex >= 0 && output.charAt(lastIndex) == '0') {
      lastIndex--;
    }
    return output.substring(0, lastIndex + 1);
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

  private HashMap<Integer, String> createCodeTable(TreeNode root) {
    return createCodeTable(root, "");
  }

  private HashMap<Integer, String> createCodeTable(TreeNode root, String currentCode) {
    HashMap<Integer, String> codeTable = new HashMap<>();

    if (root.getLeft() != null) {
      codeTable.putAll(createCodeTable(root.getLeft(), currentCode + "0"));
    }

    if (root.getRight() != null) {
      codeTable.putAll(createCodeTable(root.getRight(), currentCode + "1"));
    }

    if (root.getLeft() == null && root.getRight() == null) {
      codeTable.put(root.getAscii(), currentCode);
    }

    return codeTable;
  }

  private HashMap<Integer, String> getCodeTableFromString(String input) {
    HashMap<Integer, String> codeTable = new HashMap<>();
    Arrays.asList(input.split("-")).forEach(e -> {
      String[] entry = e.split(":");
      codeTable.put(Integer.parseInt(entry[0]), entry[1]);
    });
    return codeTable;
  }

  public String getCodeTableAsString() {
    return codeTable
        .entrySet()
        .stream()
        .map(e -> e.getKey() + ":" + e.getValue())
        .collect(Collectors.joining("-"));
  }

  private HashMap<Integer, String> generateCodeTable() {
    ArrayList<TreeNode> probabilityTable = createProbabilityTable()
        .stream()
        .filter(ap -> ap.getAmount() > 0)
        .collect(Collectors.toCollection(ArrayList::new));
    createTree(probabilityTable);
    return createCodeTable(root);
  }

  private int getCodeTableAsciiFromBinaryString(String binaryString) {
    int ascii = -1;

    // Find the ascii value for the current code
    for (Map.Entry<Integer, String> entry : codeTable.entrySet()) {
      if (entry.getValue().equals(binaryString)) {
        ascii = entry.getKey();
        break;
      }
    }
    return ascii;
  }

  public HashMap<Integer, String> getCodeTable() {
    return codeTable;
  }

  public byte[] encode() {
    // Add 1 to the end to indicate the end of the string
    String encodedString = input
        .chars()
        .mapToObj(c -> codeTable.get(c))
        .collect(Collectors.joining()) + "1";

    // Add leading zeroes so its a multiple of 8
    while (encodedString.length() % 8 != 0) {
      encodedString += "0";
    }

    // Convert to byte array
    byte[] output = new byte[encodedString.length() / 8];
    for (int i = 0; i < output.length; i++) {
      output[i] = (byte) Integer.parseInt(encodedString.substring(i * 8, i * 8 + 8), 2);
    }

    return output;
  }

  public String decode() {
    String currentCode = "";
    String decodedString = "";

    for (int i = 0; i < output.length(); i++) {
      // Collect bits until a code is found
      currentCode += output.charAt(i);
      if (codeTable.containsValue(currentCode)) {
        int ascii = getCodeTableAsciiFromBinaryString(currentCode);

        if (ascii != -1) {
          decodedString += (char) ascii;
          currentCode = "";
        }
      }
    }

    return decodedString;
  }
}