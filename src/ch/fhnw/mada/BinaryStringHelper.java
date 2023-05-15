package src.ch.fhnw.mada;

public class BinaryStringHelper {
  public static String fromByteArray(byte[] input) {
    String output = "";

    for (byte b : input) {
      // Converts byte to string that represents the binary value of the byte. Taken
      // from StackOverflow:
      // https://stackoverflow.com/questions/12310017/how-to-convert-a-byte-to-its-binary-string-representation
      output += String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
    }

    // Remove 1 and leading zeroes
    int lastIndex = output.length() - 1;
    while (lastIndex >= 0 && output.charAt(lastIndex) == '0') {
      lastIndex--;
    }
    return output.substring(0, lastIndex + 1);
  }

}
