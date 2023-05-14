package src.ch.fhnw.mada;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.stream.Collectors;

public class FileHelper {
  public static void writeBinaryFile(String fileName, byte[] out) {
    try {
      FileOutputStream fos = new FileOutputStream(fileName);
      fos.write(out);
      fos.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static byte[] readBinaryFile(String fileName) {
    try {
      FileInputStream fis = new FileInputStream(fileName);
      byte[] bFile = new byte[(int) fis.available()];
      fis.read(bFile);
      fis.close();
      return bFile;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return new byte[0];
  }

  public static void writeFile(String fileName, String content) {
    File keyFile = new File(fileName);

    try {
      keyFile.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    }

    try {
      Files.write(keyFile.toPath(), content.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static String readFile(String filePath) {
    File file = new File(filePath);
    String content = null;
    try {
      content = new String(Files.readAllBytes(file.toPath()));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return content;
  }
}
