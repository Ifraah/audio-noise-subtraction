package music.math2;

/**
 * Created by ifraah on 11/28/15.
 */
public class ZeroCrossingRate{

    private short[] signals;
    private double lengthInSecond;

    /**
     * Constructor
     *
     * @param signals       input signal array
     * @param lengthInSecond        length of the signal (in second)
     */
    public ZeroCrossingRate(short[] signals, double lengthInSecond){
        setSignals(signals,1);
    }

    /**
     * set the signals
     *
     * @param signals       input signal array
     * @param lengthInSecond        length of the signal (in second)
     */
    public void setSignals(short[] signals, double lengthInSecond){
        this.signals=signals;
        this.lengthInSecond=lengthInSecond;
    }

    public double evaluate(){
        int numZC=0;
        int size=signals.length;

        for (int i=0; i<size-1; i++){
            if((signals[i]>=0 && signals[i+1]<0) || (signals[i]<0 && signals[i+1]>=0)){
                numZC++;
            }
        }

        return numZC/lengthInSecond;
    }
}
