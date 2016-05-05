import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Hashtable;

/**
 * Testing structure for NLR+SGD, uses the model from LRTrain.
 */
public class LRTest {

    String test_file;
    int dict_size;

    public LRTest(String tf, int ds) {
        test_file = tf;
        dict_size = ds;
    }

    public void Test(LRModel lrmodel) throws java.io.IOException {
        BufferedReader in = new BufferedReader(new FileReader(test_file));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
        String line;
        HashSet<String> classes = new HashSet<>();
        Hashtable<Integer, Integer> features = new Hashtable<>();
        // double acc_sum = 0.0;
        // int count = 0;
        while ((line = in.readLine()) != null) {
            // count++;
            LRTrain.parseLine(line, classes, features, dict_size);

            for (String cl : LRModel.CLASSES) {
                double p = lrmodel.classify(cl, features);
                /*
                if (p > 0.5) {
                    if (classes.contains(cl)) {
                        acc_sum += 1.0;
                    }
                } else {
                    if (!classes.contains(cl)) {
                        acc_sum += 1.0;
                    }
                }
                */
                out.write(String.format("%s\t%f", cl, p));
                if (!cl.equals("pt")) {
                    out.write(",");
                }
            }
            out.write("\n");
        }
        // acc_sum /= count * LRModel.CLASSES.length;
        // out.write(String.format("Accuracy: %f\n", acc_sum));
        out.flush();
    }
}
