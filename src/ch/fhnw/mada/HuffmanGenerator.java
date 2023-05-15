package src.ch.fhnw.mada;

public class HuffmanGenerator {

  public static void main(String[] args) {

    // Encode ours
    String inputContent = FileHelper.readFile("input.txt");

    Huffman huffman = new Huffman(inputContent);
    FileHelper.writeFile("dec_tab.txt", huffman.getCodeTableAsString());

    byte[] out = huffman.encode();

    FileHelper.writeBinaryFile("output.dat", out);

    // Decode ours
    String ourCodeTable = FileHelper.readFile("dec_tab.txt");
    byte[] ourBinary = FileHelper.readBinaryFile("output.dat");
    Huffman ourHuffman = new Huffman(ourBinary, ourCodeTable);
    String ourOutput = ourHuffman.decode();
    System.out.println("==== Ours ====");
    System.out.println("Output: " + ourOutput);
    System.out.println("Compression Rate: " + ourHuffman.compressionRate() + "%");

    // Decode theirs
    String codeTable = FileHelper.readFile("dec_tab-mada.txt");
    byte[] binary = FileHelper.readBinaryFile("output-mada.dat");
    Huffman theirHuffman = new Huffman(binary, codeTable);
    String theirOutput = theirHuffman.decode();
    System.out.println("==== Vogts ====");
    System.out.println("Output: " + theirOutput);
    System.out.println("Compression Rate: " + theirHuffman.compressionRate() + "%");
  }
}