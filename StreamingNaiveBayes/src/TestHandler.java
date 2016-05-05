// Reads the test data, creates the NEEDED list of counts, computes and
// outputs the class predictions and log probabilities

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

public class TestHandler {

    private Hashtable<String, Integer> ht;
    private HashSet<String> hs;
    private ArrayList<String> classes;
    private BufferedWriter writer;
    private int vocabSize;

    public TestHandler() {
        ht = new Hashtable<String, Integer>();
        hs = new HashSet<String>();
        classes = new ArrayList<String>();
        writer = new BufferedWriter(new OutputStreamWriter(System.out));
    }

    // Input format:
    //      cl1,cl2,cl3\tword1 word2 word3
    public void handleTestInput(String input) {

        int prevSpace = input.indexOf("\t");
        int nextSpace = input.indexOf(" ", prevSpace + 1);

        while (nextSpace != -1) {
            String word = input.substring(prevSpace + 1, nextSpace);
            if (!hs.contains(word)) {
                hs.add(word);
            }
            prevSpace = nextSpace;
            nextSpace = input.indexOf(" ", prevSpace + 1);
        }
        String word = input.substring(prevSpace + 1);
        if (!hs.contains(word)) {
            hs.add(word);
        }

    }

    public void handleModelInput(String input) {

        int tabIdx = input.indexOf("\t");
        String key = input.substring(0, tabIdx);
        int val = Integer.parseInt(input.substring(tabIdx + 1));
        int andIdx = key.indexOf("&");

        if (andIdx == -1) {
            // V (vocab size)
            if (key.equals("V")) {
                vocabSize = val;
            } else {
                // Y=* or Y=y
                ht.put(key, val);
                if (key.charAt(2) != '*') {
                    classes.add(key.substring(2));
                }
            }
        } else if (key.startsWith("X=*")) {
            // X=*&Y=y
            ht.put(key, val);
        } else {
            // X=x&Y=y
            String word = key.substring(2, andIdx);
            if (hs.contains(word)) {
                ht.put(key, val);
            }
        }

    }

    public void classify(String input) {

        String bestClass = null;
        double bestProb = 1.0;     // temporary value

        for (String c : classes) {
            int prevSpace = input.indexOf("\t");
            int nextSpace = input.indexOf(" ", prevSpace + 1);
            double prob = 0.0;

            double denom = getKeyDefault(String.format("X=*&Y=%s", c)) + vocabSize;

            while (nextSpace != -1) {
                String word = input.substring(prevSpace + 1, nextSpace);
                prob += Math.log((getKeyDefault(
                        String.format("X=%s&Y=%s", word, c)) + 1) / denom);

                prevSpace = nextSpace;
                nextSpace = input.indexOf(" ", prevSpace + 1);
            }
            String word = input.substring(prevSpace + 1);
            prob += Math.log((getKeyDefault(
                    String.format("X=%s&Y=%s", word, c)) + 1) / denom);

            // Priors
            prob += Math.log((getKeyDefault(String.format("Y=%s", c)) + 1) /
                    (getKeyDefault("Y=*") + classes.size()));

            if (bestProb == 1.0 || prob > bestProb) {
                bestClass = c;
                bestProb = prob;
            }
        }

        try {
            writer.write(String.format("%s\t%f\n", bestClass, bestProb));
        } catch (IOException io) {
            io.printStackTrace();
        }

    }

    private double getKeyDefault(String k) {
        if (ht.containsKey(k)) {
            return (double) (ht.get(k));
        } else {
            return 0.0;
        }
    }

    public void flushBuffer() {
        try {
            writer.flush();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
