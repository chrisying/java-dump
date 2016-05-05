// Naive Bayes trainer (streaming)

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NBTrain {

    // Determines the size of the hash table before flushing,
    // experiment with different values for runtime
    static int HT_CAP = 1000;


    public static void main(String[] args) {

        // Read from stdin
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(System.in)
            );
            String input;
            TrainHandler th = new TrainHandler(HT_CAP);

            while ((input = br.readLine()) != null) {
                th.handleInput(input);
            }
            th.flushTable();

        } catch (IOException io) {
            io.printStackTrace();
        }
    }

}
