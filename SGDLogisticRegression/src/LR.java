import java.io.*;
import java.util.Hashtable;

/**
 * Main class for running normalized logistic regression with stochastic
 * gradient descent. The code should be run using Makefile:
 *   make build
 *   make run
 */
public class LR {

    public static void main(String[] args) throws IOException {
        int dict_size = Integer.parseInt(args[0]);
        float lambda = Float.parseFloat(args[1]);
        float mu =  Float.parseFloat(args[2]);
        int iters = Integer.parseInt(args[3]);
        int train_size = Integer.parseInt(args[4]);
        String test_file = args[5];

        LRTrain trainer = new LRTrain(
                dict_size,
                lambda,
                mu,
                iters,
                train_size
        );
        LRModel lrmodel = trainer.Train();

        LRTest tester = new LRTest(test_file, dict_size);
        tester.Test(lrmodel);
    }

}
