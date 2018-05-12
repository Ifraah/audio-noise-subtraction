package music.math2;

/**
 * Created by ifraah on 11/28/15.
 */
public class Mean extends MathStatistics{

    private Sum sum=new Sum();

    public Mean(){
    }

    public Mean(double[] values){
        setValues(values);
    }

    public double evaluate(){
        sum.setValues(values);
        double mean=sum.evaluate()/sum.size();
        return mean;
    }
}
