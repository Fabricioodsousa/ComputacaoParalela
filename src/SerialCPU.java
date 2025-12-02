public class SerialCPU {
    public static int countWordSerial(String text, String target) {
        int count = 0;
        String[] words = text.split("\\W+");
        for (String w : words) if (w.equalsIgnoreCase(target)) count++;
        return count;
    }
}
