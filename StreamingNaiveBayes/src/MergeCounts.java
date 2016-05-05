// Takes the output from sort and merges the counts of the same event


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MergeCounts {

    public static void main(String[] args) {

        // Read from stdin
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(System.in)
            );
            String input;
            MergeHandler mh = new MergeHandler();

            while ((input = br.readLine()) != null) {
                mh.handleInput(input);
            }
            mh.outputPrev();
            mh.printVocabSize();
            mh.flushBuffer();

        } catch (IOException io) {
            io.printStackTrace();
        }

    }

}
