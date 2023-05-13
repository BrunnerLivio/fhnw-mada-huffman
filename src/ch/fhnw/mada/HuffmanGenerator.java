package src.ch.fhnw.mada;

public class HuffmanGenerator {
  private static final String PATH = "generated/";

  public static void main(String[] args) {
    String inputContent = FileHelper.readFile("input.txt");

    Huffman huffman = new Huffman(inputContent);
  }
}