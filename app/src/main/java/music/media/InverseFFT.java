package music.media;

/**
 * Created by ifraah on 11/28/15.
 */
import android.graphics.Color;
import java.lang.Math.*;


public class InverseFFT{
    public int progress;
    /**
     * Default no argument constructor.
     */
    public InverseFFT(){
    }

    /**
     * Recursively applies the 1D inverse FFT algorithm.
     *
     * @param x ComplexNumber array containing the input to the 1D inverse FFT.
     * @return ComplexNumber array containing the result of the 1D inverse FFT.
     */
    public ComplexNumber [] recursiveInverseFFT (ComplexNumber [] x){
        ComplexNumber z1,z2,z3,z4,tmp,cTwo;
        int n = x.length;
        int m = n/2;
        ComplexNumber [] result = new ComplexNumber [n];
        ComplexNumber [] even = new ComplexNumber [m];
        ComplexNumber [] odd = new ComplexNumber [m];
        ComplexNumber [] sum = new ComplexNumber [m];
        ComplexNumber [] diff = new ComplexNumber [m];
        cTwo = new ComplexNumber(2,0);
        if(n==1){
            result[0] = x[0];
        }else{
            z1 = new ComplexNumber(0.0, 2*(Math.PI)/n);
            tmp = ComplexNumber.cExp(z1);
            z1 = new ComplexNumber(1.0, 0.0);
            for(int i=0;i<m;++i){
                z3 = ComplexNumber.cSum(x[i],x[i+m]);
                sum[i] = ComplexNumber.cDiv(z3,cTwo);

                z3 = ComplexNumber.cDif(x[i],x[i+m]);
                z4 = ComplexNumber.cMult(z3,z1);
                diff[i] = ComplexNumber.cDiv(z4,cTwo);

                z2 = ComplexNumber.cMult(z1,tmp);
                z1 = new ComplexNumber(z2);
            }
            even = recursiveInverseFFT(sum);
            odd = recursiveInverseFFT(diff);

            for(int i=0;i<m;++i){
                result[i*2] = new ComplexNumber(even[i]);
                result[i*2 + 1] = new ComplexNumber(odd[i]);
            }
        }
        return result;
    }

    /**
     * Takes a TwoDArray, applies the 2D inverse FFT to the input by applying
     * the 1D inverse FFT to each column and then each row in turn.
     *
     * @param input TwoDArray containing the input image data.
     * @return TwoDArray containing the new image data.
     */
    public TwoDArray transform(TwoDArray input){
        progress = 0;
      TwoDArray intermediate = new TwoDArray(input.width,input.height);
        TwoDArray output = new TwoDArray(input.width,input.height);

        for(int i=0;i<input.size;++i){
            progress++;
            intermediate.putColumn(i, recursiveInverseFFT(input.getColumn(i)));
        }

        for(int i=0;i<intermediate.size;++i){
            progress++;
            output.putRow(i, recursiveInverseFFT(intermediate.getRow(i)));
        }
        return output;
    }



    /**
     * Takes a TwoDArray, applies the 2D inverse FFT to the input by applying
     * the 1D inverse FFT to each column and then each row in turn.
     *
     * @param input TwoDArray containing the input image data.
     * @return TwoDArray containing the new image data.
     */
    public TwoDArray transformAbs1(TwoDArray input){
        progress = 0;
        input = TwoDArray.setAbs1(input);

        TwoDArray intermediate = new TwoDArray(input.width, input.height);
        TwoDArray output = new TwoDArray(input.width, input.height);


        for(int i=0;i<input.size;++i){
            progress++;
            intermediate.putColumn(i, recursiveInverseFFT(input.getColumn(i)));
        }

        for(int i=0;i<intermediate.size;++i){
            progress++;
            output.putRow(i, recursiveInverseFFT(intermediate.getRow(i)));
        }
        return output;
    }



    public int[] getPixels(TwoDArray output) {
        double [] outputArrayDoubles =
                new double [output.width*output.height];
        outputArrayDoubles = output.getReal();
        //outputArrayDoubles = output.getMagnitude();
        int[] outputArray = new int [outputArrayDoubles.length];
        //outputArray = ImageMods.toPixels(outputArrayDoubles);
        outputArray =
                toPixels(allPositive(outputArrayDoubles));

        return outputArray;
    }
    /**
     * Method to slide and scale an array of doubles so that the minimum
     * values is 0 (all positive).
     *
     * @param values An array of doubles.
     * @return An array of positive doubles.
     */
    private double [] allPositive(double [] values){
        double [] output = new double [values.length];
        double m = minValue(values);
        if(m<0){
            for(int i=0;i<values.length;++i){
                output[i] = values[i]-m;
            }
            return output;
        }
        else return values;
    }

    /**
     * A method to convert an array of grey values to an array of pixel values.
     *
     * @param values An array of grey values (all positive).
     * @return An array of pixel values.
     */
    private int [] toPixels(double [] values){
        int grey;
//    double [] greys = new double [values.length];
        int [] pixels = new int [values.length];
//    for(int i=0;i<greys.length;++i){
//      greys[i] = values[i];
//    }
//    double max = maxValue(greys);
        double max = maxValue(values);
        double scale;
        if (max == 0) scale = 1.0;
        else scale = 255.0/max;

        //System.out.println(max);
//    for(int i=0;i<greys.length;++i){
        for(int i=0;i<values.length;++i){
//      greys[i] = greys[i] * 255/max;
//      grey = (int) Math.round(greys[i]);
            grey = (int) Math.round(values[i]*scale);
            //pixels[i] = new Color(grey,grey,grey).getRGB();
            //recombine greys
            pixels[i] = Color.rgb(grey, grey, grey);
        }
        return pixels;
    }
    /**
     * Method to find the maximum value from an array of doubles.
     *
     * @param values an array of doubles.
     * @return The maximum value.
     */
    private double maxValue(double [] values){
        double max = values[0];
        for(int i=1;i<values.length;++i){
            if(values[i]>max) max=values[i];
        }
        return max;
    }

    /**
     * Method to find the minimum value from an array of doubles.
     *
     * @param values an array of doubles.
     * @return The minimum value.
     */
    private double minValue(double [] values){
        double min = values[0];
        for(int i=1;i<values.length;++i){
            if(values[i]<min) min=values[i];
        }
        return min;
    }

    public int getProgress() { return progress; }
}
