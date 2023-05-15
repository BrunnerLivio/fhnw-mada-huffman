package src.ch.fhnw.mada;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Huffman {
  private String input;
  private String outputBinaryString;
  private int totalAmount;
  private HuffmanTreeNode root;
  private HashMap<Integer, String> codeTable;

  /**
   * For encoding
   */
  public Huffman(String input) {
    this.input = input;
    this.totalAmount = input.length();
    this.codeTable = generateCodeTable();
  }

  /**
   * For decoding
   */
  public Huffman(byte[] input, String codeTable) {
    this.outputBinaryString = BinaryStringHelper.fromByteArray(input);
    this.codeTable = parseCodeTableFromString(codeTable);
  }

  private ArrayList<HuffmanTreeNode> createProbabilityTable() {
    ArrayList<HuffmanTreeNode> probabilityTable = new ArrayList<>();

    // We iterate through every character. If that character is already in the
    // probability table, we increment the amount. Otherwise we add it to the table.
    Arrays
        .asList(input.split(""))
        .forEach(c -> {
          // Get ASCII value of character
          int ascii = (int) c.charAt(0);

          Optional<HuffmanTreeNode> existingNode = probabilityTable.stream().filter(ap -> ap.getAscii() == ascii)
              .findFirst();

          if (existingNode.isPresent()) {
            existingNode.get().increment();
          } else {
            probabilityTable.add(new HuffmanTreeNode(ascii, 1, totalAmount));
          }
        });

    return probabilityTable.stream()
        .filter(ap -> ap.getAmount() > 0)
        .collect(Collectors.toCollection(ArrayList::new));
  }

  private void constructTree(ArrayList<HuffmanTreeNode> currentTable) {

    // We sort the array by the amount descending. This way the smallest two
    // elements are always at the beginning of the array.
    currentTable.sort((a, b) -> a.getAmount() - b.getAmount());

    // We take the first two elements...
    ArrayList<HuffmanTreeNode> smallestTwo = currentTable.stream()
        .limit(2)
        .collect(Collectors.toCollection(ArrayList::new));

    // ...and create a new parent node
    // __c <- new parent node
    // _/_\
    // a___b
    HuffmanTreeNode parent = new HuffmanTreeNode(smallestTwo.get(0), smallestTwo.get(1), totalAmount);
    currentTable.add(parent);
    // Remove the smallest two elements
    currentTable.remove(0);
    currentTable.remove(0);

    // We have reached the end in the tree if there is only one element left in the
    // array. This element is the root node.
    if (currentTable.size() == 1) {
      HuffmanTreeNode root = currentTable.get(0);
      this.root = root;
      return;
    }

    constructTree(currentTable);
  }

  private HashMap<Integer, String> createCodeTable(HuffmanTreeNode root) {
    return createCodeTable(root, "");
  }

  private HashMap<Integer, String> createCodeTable(HuffmanTreeNode root, String currentCode) {
    HashMap<Integer, String> codeTable = new HashMap<>();

    // We have a left node, so we add a 0 to the code and continue until we have
    // founded a leaf
    // ____c
    // _0/___\
    // _a_____b
    if (root.getLeft() != null) {
      codeTable.putAll(createCodeTable(root.getLeft(), currentCode + "0"));
    }

    // We have a right node, so we add a 1 to the code and continue until we have
    // founded a leaf
    // ____c
    // __/___\1
    // _a_____b
    if (root.getRight() != null) {
      codeTable.putAll(createCodeTable(root.getRight(), currentCode + "1"));
    }

    // We have reached a leaf, so we can now add the code to the code table
    if (root.getLeft() == null && root.getRight() == null) {
      codeTable.put(root.getAscii(), currentCode);
    }

    return codeTable;
  }

  private HashMap<Integer, String> generateCodeTable() {
    ArrayList<HuffmanTreeNode> probabilityTable = createProbabilityTable();
    constructTree(probabilityTable);
    return createCodeTable(root);
  }

  private HashMap<Integer, String> parseCodeTableFromString(String input) {
    HashMap<Integer, String> codeTable = new HashMap<>();
    Arrays.asList(input.split("-")).forEach(e -> {
      String[] entry = e.split(":");
      codeTable.put(Integer.parseInt(entry[0]), entry[1]);
    });
    return codeTable;
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

  public String getCodeTableAsString() {
    return codeTable
        .entrySet()
        .stream()
        .map(e -> e.getKey() + ":" + e.getValue())
        .collect(Collectors.joining("-"));
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
      // Get 8 bits at a time.
      // e.g.
      // i = 0
      // startIndex: 0 * 8 = 0
      // endIndex: 0 * 8 + 8 = 8
      String currentByte = encodedString.substring(i * 8, i * 8 + 8);

      // Convert to byte. Second parameter is the radix, which is 2 for binary.
      output[i] = (byte) Integer.parseInt(currentByte, 2);
    }

    return output;
  }

  public String decode() {
    String currentCode = "";
    String decodedString = "";

    for (int i = 0; i < outputBinaryString.length(); i++) {
      // Collect bits until a code is found
      currentCode += outputBinaryString.charAt(i);
      if (codeTable.containsValue(currentCode)) {
        int ascii = getCodeTableAsciiFromBinaryString(currentCode);

        if (ascii == -1) {
          continue;
        }

        decodedString += (char) ascii;
        currentCode = "";
      }
    }

    return decodedString;
  }

  public int compressionRate() {
    double inputLengthInBits = decode().length() * 8;
    double outputLengthInBits = outputBinaryString.length();

    return (int) Math.round(outputLengthInBits / inputLengthInBits * 100.0);
  }
}