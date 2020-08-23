package encryptdecrypt;

import java.io.*;
import java.util.*;

public class Main {
    public static String getDataFromFile(String path) {
        String data = "";
        try (Scanner sc = new Scanner(new File(path))) {
            data = sc.nextLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error at getting data data from file");
        }
        return data;
    }

    public static void writeDataToFile(String what, String output) {
        try(FileWriter fw = new FileWriter(output)) {
            fw.write(what);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Error at writing data in file");
        }
    }

    public static String shiftAlg(String input, int key) {
        if (input.equals("") | key == 0) {
            System.out.println();
        }
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            int codeN = input.charAt(i);
            if (codeN >= 97 & codeN <= 122) {
                int newChar = ((codeN + key - (int)'a') % 26);
                if (newChar < 0) {
                    newChar = 26 - newChar;
                }
                output.append((char)(newChar + (int)'a'));
            } else if (codeN >= 65 & codeN <= 90) {
                int newChar = ((codeN + key - (int)'A') % 26);
                if (newChar < 0) {
                    newChar = 26 - newChar;
                }
                output.append((char)(newChar + (int)'A'));
            } else {
                output.append((char)codeN);
            }
        }
        return output.toString();
    }

    public static String unicodeAlg(String input, int key) {
        if (input.equals("") | key == 0) {
            System.out.println();
        }
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            int codeN = input.charAt(i) - 31;
            int newPos = (codeN + key) % 96;
            output.append((char) (newPos + 31));
        }
        return output.toString();
    }

    public static void main(String[] args) {
        Map<String, String> argPair = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            argPair.put(args[i], args[i + 1]);
        }

        String mode = argPair.get("-mode") == null ? "enc" : argPair.get("-mode");
        String algorithm = argPair.get("-alg") == null ? "shift" : argPair.get("-alg");
        int key = argPair.get("-key") == null ? 0 : Integer.parseInt(argPair.get("-key"));
        String in = argPair.get("-in");
        String data = "";
        String out = argPair.get("-out");
        if (in != null & argPair.get("-data") == null) {
            data = getDataFromFile(in);
        } else data = argPair.get("-data");

        String result = "";
        switch (mode) {
            case "enc" :
                if (algorithm.equals("shift")) {
                    result = shiftAlg(data, key);
                } else {
                    result = unicodeAlg(data, key);
                }
                break;
            case "dec" :
                if (algorithm.equals("shift")) {
                    result = shiftAlg(data, 26 - key); //26
                } else {
                    result = unicodeAlg(data, 96 - key);
                }
                break;
            default:
                System.out.println("Error! Undefined statement");
        }

        if (out != null) {
            System.out.println(result);
            writeDataToFile(result, out);
        } else
            System.out.println(result);
    }
}