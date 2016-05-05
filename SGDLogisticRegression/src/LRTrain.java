import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

/**
 * Trainer for NLR+SGD
 * Optimized with feature hashing + lazy regularization
 * Returns an LRModel object
 */
public class LRTrain {

    BufferedReader in;
    int dict_size, iters, train_size;
    float lambda, mu;
    HashSet<String> classes;
    Hashtable<Integer, Integer> features;

    public LRTrain(int ds, float l, float m, int i, int ts) {
        in = new BufferedReader(new InputStreamReader(System.in));
        dict_size = ds;
        lambda = l;
        mu = m;
        iters = i;
        train_size = ts;
        classes = new HashSet<>();
        features = new Hashtable<>();
    }

    public LRModel Train() throws IOException {
        LRModel lrmodel = new LRModel(dict_size, mu);

        for (int iter = 0; iter < iters; iter++) {
            lrmodel.lambda = lambda / Math.pow((iter + 1), 2);
            // double objective = 0.0;
            for (int i = 0; i < train_size; i++) {
                // Parse line
                String line = in.readLine();
                if (line == null) {
                    System.out.println("Warning: less lines in data than specified in cmdline args!");
                    continue;
                }
                parseLine(line, classes, features, dict_size);

                // SGD iteration for each class
                for (String cl : LRModel.CLASSES) {
                    Set<Integer> fs = features.keySet();
                    for (int f : fs) {
                        lrmodel.regularize(cl, f, iter*i);
                    }
                    double p = lrmodel.classify(cl, features);
                    double y;
                    if (classes.contains(cl)) {
                        y = 1.0;
                        // objective += Math.log(p);
                    } else {
                        y = 0.0;
                        // objective += Math.log(1.0 - p);
                    }

                    for (int f : fs) {
                        lrmodel.update(cl, f, features.get(f), y, p);
                    }
                }
            }
            // System.out.printf("Iteration %d\tObjective: %f\n", iter, objective);
        }
        // Regularize all non-zero terms one last time
        for (String cl : LRModel.CLASSES) {
            for (int i = 0; i < dict_size; i++) {
                lrmodel.regularize(cl, i, iters*train_size);
            }
        }

        return lrmodel;
    }

    // TODO: move static functions to a Util class
    public static void parseLine(String line,
                                 HashSet<String> classes,
                                 Hashtable<Integer, Integer> features,
                                 int dict_size) {
        int tidx = line.indexOf("\t");
        classes.clear();
        int idx = 0;
        int cidx = line.indexOf(",");
        while (cidx != -1 && cidx < tidx) {
            classes.add(line.substring(idx, cidx));
            idx = cidx + 1;
            cidx = line.indexOf(",", idx);
        }
        classes.add(line.substring(idx, tidx));

        features.clear();
        idx = tidx + 1;
        cidx = line.indexOf(" ");
        while (cidx != -1) {
            incr(features,
                    mod(line.substring(idx, cidx).hashCode(),
                            dict_size)
            );
            idx = cidx + 1;
            cidx = line.indexOf(" ", idx);
        }
        incr(features, mod(
                line.substring(idx).hashCode(), dict_size));
    }

    public static int mod(int a, int b) {
        return (a % b + b) % b;
    }

    public static void incr(Hashtable<Integer, Integer> ht, int key) {
        if (!ht.containsKey(key)) {
            ht.put(key, 1);
        } else {
            ht.put(key, ht.get(key) + 1);
        }
    }
}
