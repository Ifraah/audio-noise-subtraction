package music.math2;

/**
 * Created by ifraah on 11/28/15.
 */
public class StandardDeviation extends MathStatistics{

    private Mean mean=new Mean();

    public StandardDeviation(){

    }

    public StandardDeviation(double[] values){
        setValues(values);
    }

    public double evaluate(){

        mean.setValues(values);
        double meanValue=mean.evaluate();

        int size=values.length;
        double diffSquare=0;
        double sd=Double.NaN;

        for (int i=0; i<size; i++){
            diffSquare+=Math.pow(values[i]-meanValue,2);
        }

        if (size>0){
            sd=Math.sqrt(diffSquare/size);
        }

        return sd;
    }
}