package recommendation.mahout;

import dominio.Evaluation;
import org.apache.mahout.cf.taste.model.Preference;

public class MahoutEvaluation extends Evaluation implements Preference {

    /**
     * MahoutEvaluation class constructor
     * 
     * @param evaluation Evaluation to be adapted
     */
    public MahoutEvaluation(Evaluation evaluation) {
        super(evaluation.getId(), evaluation.getCustomer(), evaluation.getBook(), evaluation.getRating());
    }

    @Override
    public long getUserID() {
        return getCustomer().getId();
    }

    @Override
    public long getItemID() {
        return getBook().getId();
    }

    @Override
    public float getValue() {
        return (float) getRating();
    }

    @Override
    public void setValue(float v) {
        throw new UnsupportedOperationException("Evaluation does not allow changes on value / rating.");
    }
}
