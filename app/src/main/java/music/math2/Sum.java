package music.math2;

/**
 * Created by ifraah on 11/28/15.
 */
public class Sum extends MathStatistics{

    public Sum(){
    }

    public Sum(double[] values){
        setValues(values);
    }

    public double evaluate(){
        double sum=0;
        int size=values.length;
        for (int i=0 ;i<size; i++){
            sum+=values[i];
        }
        return sum;
    }

    public int size(){
        return values.length;
    }
}