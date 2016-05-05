import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Object for storing LR trained model, includes multiple independent
 * binary classifiers, one for each class
 */
public class LRModel {

    static final double OVERFLOW = 20.0;
    static final String[] CLASSES = {
        "nl", "el", "ru", "sl", "pl", "ca", "fr",
        "tr", "hu", "de", "hr", "es", "ga", "pt"
    };

    class Model {
        // Both arrays are indexed by feature hash code
        public int[] A;         // Last regularization update
        // TODO: try float
        public double[] B;      // Weight vector

        public Model(int dict_size) {
            A = new int[dict_size];
            B = new double[dict_size];
        }
    }

    int dict_size;
    double lambda, mu;
    // class -> model
    public Hashtable<String, Model> models;

    public LRModel(int ds, double m) {
        dict_size = ds;
        mu = m;
        models = new Hashtable<>();

        for (String cl : CLASSES) {
            models.put(cl, new Model(dict_size));
        }
    }

    public void regularize(String cl, int feature, int k) {
        Model model = models.get(cl);
        model.B[feature] *= Math.pow(1.0-2.0*lambda*mu, k-model.A[feature]);
        model.A[feature] = k;
    }

    public double classify(String cl, Hashtable<Integer, Integer> features) {
        Model model = models.get(cl);
        double score = 0.0;
        Enumeration<Integer> e = features.keys();
        while(e.hasMoreElements()) {
            int f = e.nextElement();
            score += model.B[f] * features.get(f);
            //score += model.B[f];
        }

        // Prevent overflow
        if (score > OVERFLOW) {
            score = OVERFLOW;
        } else if (score < -OVERFLOW) {
            score = -OVERFLOW;
        }

        return 1.0 / (1.0 + Math.exp(-score));
    }

    public void update(String cl, int f, int v, double y, double p) {
        Model model = models.get(cl);
        model.B[f] += lambda * (y - p) * v;
        //model.B[f] += lambda * (y - p);
    }
}
