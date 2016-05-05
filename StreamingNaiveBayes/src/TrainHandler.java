// Handles the input from the test files.
// Keeps a local hash table of counts, and outputs when some max buffer size
// is reached. Constants should be micro-optimized.

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class TrainHandler {

    private int htMax;
    private Hashtable<String, Integer> ht;
    private BufferedWriter writer;

    public TrainHandler(int htCap) {
        htMax = htCap;
        ht = new Hashtable<String, Integer>(htCap);
        writer = new BufferedWriter(new OutputStreamWriter(System.out));
    }

    // Input looks like:
    //      cl1,cl2,cl3\tword1 word2 word3
    public void handleInput(String input) {

        int tabIdx = input.indexOf("\t");

        // Assumes that number of classes is not very large
        ArrayList<String> classes = parseClasses(input.substring(0, tabIdx));

        // Iteratively parse and handle each word in the entry
        int prevSpace = tabIdx;
        int nextSpace = input.indexOf(" ", prevSpace + 1);

        while (nextSpace != -1) {
            handleWord(
                    input.substring(prevSpace + 1, nextSpace),
                    classes
            );
            prevSpace = nextSpace;
            nextSpace = input.indexOf(" ", prevSpace + 1);
        }
        handleWord(
                input.substring(prevSpace + 1),
                classes
        );

    }

    // classString is formatted like "cl1,cl2,cl3"
    private ArrayList<String> parseClasses(String classString) {

        ArrayList<String> classes = new ArrayList<String>();
        int prevComma = -1;
        int nextComma = classString.indexOf(",", prevComma + 1);

        while (nextComma != -1) {
            classes.add(classString.substring(prevComma + 1, nextComma));
            prevComma = nextComma;
            nextComma = classString.indexOf(",", prevComma + 1);
        }
        classes.add(classString.substring(prevComma + 1));

        for (String c : classes) {
            incrementCounter(String.format("Y=%s", c));
            incrementCounter("Y=*");
        }

        return classes;
    }

    private void handleWord(String word, ArrayList<String> classes) {

        for (String c : classes) {
            incrementCounter(String.format("X=%s&Y=%s", word, c));
            incrementCounter(String.format("X=*&Y=%s", c));
        }

    }

    private void incrementCounter(String key) {

        if (ht.containsKey(key)) {
            ht.put(key, ht.get(key) + 1);
        } else {
            ht.put(key, 1);
        }

        if (ht.size() > htMax) {
            flushTable();
        }
    }

    public void flushTable() {

        Enumeration<String> keys = ht.keys();
        String k;
        int v;
        int i = 0;

        while (keys.hasMoreElements()) {
            i++;
            k = keys.nextElement();
            v = ht.get(k);
            String output = String.format("%s\t%d\n", k, v);

            try {
                writer.write(output);
            } catch (IOException io) {
                io.printStackTrace();
            }
        }

        try {
            writer.flush();
        } catch (IOException io) {
            io.printStackTrace();
        }

        ht.clear();
    }


}
