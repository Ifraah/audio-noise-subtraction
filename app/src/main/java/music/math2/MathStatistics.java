package music.math2;




public abstract class MathStatistics{

    protected double[] values;

    public void setValues(double[] values){
        this.values=values;
    }

    public abstract double evaluate();
}

