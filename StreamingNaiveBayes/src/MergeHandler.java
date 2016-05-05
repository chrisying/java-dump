// Handles lines sent from MergeCounts, aggregates the counts of each hash key

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MergeHandler {

    private String prevKey;
    private int prevSum;
    private BufferedWriter writer;
    private int vocabSize;
    private static final int V_FACT = 3;

    public MergeHandler() {
        prevKey = null;
        prevSum = 0;
        writer = new BufferedWriter(new OutputStreamWriter(System.out));
        vocabSize = 0;
    }

    // Input looks like:
    //      key\t5
    public void handleInput(String input) {

        int tabIdx = input.indexOf("\t");
        String key = input.substring(0, tabIdx);
        int val = Integer.parseInt(input.substring(tabIdx + 1));

        if (key.equals(prevKey)) {
            prevSum += val;
        } else {
            outputPrev();
            vocabSize++;
            prevKey = key;
            prevSum = val;
        }

    }

    public void outputPrev() {

        if (prevKey != null) {
            try {
                writer.write(String.format("%s\t%d\n", prevKey, prevSum));
            } catch (IOException io) {
                io.printStackTrace();
            }
        }

    }

    public void flushBuffer() {
        try {
            writer.flush();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    // Estimates the vocabulary size from the number of total keys written
    public void printVocabSize() {
        try {
            writer.write(String.format("V\t%d", vocabSize / V_FACT));
            writer.flush();
        } catch (IOException io) {
            io.printStackTrace();
        }

    }

}
