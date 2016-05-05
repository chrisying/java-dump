// Takes the output model from MergeCounts and a test file and gives the best
// class as well as the log probability to stdout

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NBTest {

    public static void main(String[] args) {

        if (args.length != 1) {
            System.err.println("Incorrect number of command lind args");
            System.err.println("Usage: java NBTest test_file.txt");
            System.exit(1);
        }

        BufferedReader br;
        String input;
        TestHandler th = new TestHandler();

        // Pass through test data once to get needed set
        try {
            br = new BufferedReader(
                    new FileReader(args[0])
            );

            while ((input = br.readLine()) != null) {
                th.handleTestInput(input);
            }

        } catch (IOException io) {
            io.printStackTrace();
        }

        // Pass through model once to get counts
        try {
            br = new BufferedReader(
                    new InputStreamReader(System.in)
            );

            while ((input = br.readLine()) != null) {
                th.handleModelInput(input);
            }
        } catch (IOException io) {
            io.printStackTrace();
        }

        // Pass through test second time to generate classes and probabilities
        try {
            br = new BufferedReader(
                    new FileReader(args[0])
            );

            while ((input = br.readLine()) != null) {
                th.classify(input);
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
        th.flushBuffer();

    }

}
