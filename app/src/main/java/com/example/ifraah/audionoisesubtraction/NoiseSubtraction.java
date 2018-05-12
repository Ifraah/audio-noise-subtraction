package com.example.ifraah.audionoisesubtraction;

import android.app.Activity;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.widget.Toast;
import android.provider.MediaStore;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import music.dsp.FastFourierTransform;
import music.dsp.WindowFunction;
import music.media.ComplexNumber;
import music.media.TwoDArray;
import music.wave.Wave;
import music.wave.extension.Spectrogram;
import music.media.InverseFFT;

/**
 * Created by ifraah on 11/10/15.
 */
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Object;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import android.graphics.Rect;



/**
 * Created by ifraah on 11/17/15.
 */
public class NoiseSubtraction extends Activity {
    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }
    Button play;
    Button draw;
    //private Paint paint = new Paint();

    ImageView	imageView;
    Bitmap 		bitmap;
    Canvas 		canvas;
    Paint 		paint;
    LinearLayout layout;
    private int bufferSize = AudioRecord.getMinBufferSize(44100,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT);
    //private double [][] data;
    Canvas ctx; // = $("#canvas").get()[0].getContext("2d");

    //@Override
    protected void onDraw(double[][]data) {
      //  super.onDraw(canvas);

        if (data != null) {
            paint.setStrokeWidth(1);
            canvas.drawColor(Color.WHITE);
            int width = data.length;
            int height = data[0].length;
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int value;
                    value = 255 - (int) (data[i][j] * 255);
                    paint.setColor(value << 16 | value << 8 | value | 255 << 24);
                    canvas.drawPoint(i, height - 1 - j, paint);
                }
            }
        } else {
            System.err.println("Data Corrupt");
        }
    }
    //Context cont=this.getApplicationContext();
    private void WriteWaveFileHeader(FileOutputStream out, long totalAudioLen,
                                     long totalDataLen, long longSampleRate, int channels, long byteRate)
            throws IOException {

        byte[] header = new byte[44];

        header[0] = 'R';
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte)(totalDataLen & 0xff);
        header[5] = (byte)((totalDataLen >> 8) & 0xff);
        header[6] = (byte)((totalDataLen >> 16) & 0xff);
        header[7] = (byte)((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f';
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16;
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1;
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte)(longSampleRate & 0xff);
        header[25] = (byte)((longSampleRate >> 8) & 0xff);
        header[26] = (byte)((longSampleRate >> 16) & 0xff);
        header[27] = (byte)((longSampleRate >> 24) & 0xff);
        header[28] = (byte)(byteRate & 0xff);
        header[29] = (byte)((byteRate >> 8) & 0xff);
        header[30] = (byte)((byteRate >> 16) & 0xff);
        header[31] = (byte)((byteRate >> 24) & 0xff);
        header[32] = (byte)(2 * 16 / 8);
        header[33] = 0;
        header[34] = 16;
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte)(totalAudioLen & 0xff);
        header[41] = (byte)((totalAudioLen >> 8) & 0xff);
        header[42] = (byte)((totalAudioLen >> 16) & 0xff);
        header[43] = (byte)((totalAudioLen >> 24) & 0xff);

        out.write(header, 0, 44);
    }


    public void Start()
    {
       // super.onStart();
        setContentView(R.layout.main_draw);

        imageView = (ImageView)this.findViewById(R.id.imageView1);
        bitmap = Bitmap.createBitmap((int)256,(int)384,Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        canvas.drawColor(Color.BLACK);
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.FILL);
        imageView.setImageBitmap(bitmap);



        LinearLayout layout = (LinearLayout) findViewById(R.id.mylayout);

    }

    protected void onProgressUpdate(double[][] newDisplayUpdate) {
        // emulate a scrolling window
        Rect srcRect = new Rect(0, -(-1), bitmap.getWidth(), bitmap.getHeight());
        Rect destRect = new Rect(srcRect);
        destRect.offset(0, -1);
        canvas.drawBitmap(bitmap, srcRect, destRect, null);
     for(int j=0;j<newDisplayUpdate[j].length;j++) {
    // display new data in right-most column
    for (int i = 0; i < newDisplayUpdate[i].length; i++) {
        // map value, which is between 0.0 and 1.0, to an RGB color
        int[] rgb = colorMap(newDisplayUpdate[j][i]);

        // set color with constant alpha
        paint.setColor(Color.argb(255, rgb[0], rgb[1], rgb[2]));

        // paint right-most column with frequency corresponding to row i
        try {
            canvas.drawRect(i, 20, i + 1, 10000, paint);
        }
        catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        //LTRB
    }
}
        //newDisplayUpdate[0].;
        imageView.invalidate();
    }


    public int[] colorMap(double value) {
        // implements a simple linear RYGCB colormap
        if(value <= 0.25) {
            return new int[]{0, (int)(4*value*255), (int)255};
        } else if(value <= 0.5) {
            return new int[]{0, (int)255, (int)((1-4*(value-0.25))*255)};
        } else if(value <= 0.75) {
            return new int[]{(int)(4*(value-0.5)*255), (int)255, 0};
        } else {
            return new int[]{(int)255, (int)((1-4*(value-0.75))*255), 0};
        }
    }



    //type conversion from TwoDArray to double

    //inverse fft
   TwoDArray l =new TwoDArray();
   InverseFFT m = new InverseFFT();
    TwoDArray y = new TwoDArray();


    public TwoDArray func1(double [][]spd3) {
        ComplexNumber [][] x=new ComplexNumber[spd3.length][spd3.length];
        for (int j = 0; j < spd3.length-1; j++) {
            for (int i = 0; i < spd3.length-1; i++) {

                double a = spd3[i][j];
                ComplexNumber c = new ComplexNumber(a,0);

                x[i][j]=c;
                y.putRow(i,x[i]);
            }
            y.putColumn(j,x[j]);
        }
        return y;
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noise_subtraction);

        play =(Button) findViewById(R.id.button);
        draw =(Button) findViewById(R.id.button1);


        draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws IllegalArgumentException, SecurityException, IllegalStateException {
                InputStream in1 = null;
                in1 = getResources().openRawResource(getResources().getIdentifier("raw/first", "raw", getPackageName()));
                music.wave.Wave wav2 =new music.wave.Wave(in1);
                music.wave.Wave wav3 = new music.wave.Wave(Environment.getExternalStorageDirectory().getAbsolutePath() + "/record.wav");
                music.wave.Wave wav1 = new music.wave.Wave(Environment.getExternalStorageDirectory().getAbsolutePath() + "/combined.wav");

                Spectrogram sp1 = new Spectrogram(wav1);
                Spectrogram sp2 = new Spectrogram(wav2);
                Spectrogram spf = new Spectrogram(wav3);

                double spd1[][] = spd1 =sp1.getSpectrogramData();
                double spd2[][] = sp2.getSpectrogramData();
                double spd3[][]=new double[spd1.length][spd1.length];
                double spd4[][]=spf.getSpectrogramData();

                    // spd3 = func(spd1, spd2); //final spectogram;
                int j=0;
                for (int i = 0; i < 200; i++) {
                    for ( j = 0; j < 200; j++) {

                        spd3[i][j] = Math.abs(spd1[i][j] - spd2[i][j]);

                    }
                }

                try {
                   Start();
                    try {
                        onProgressUpdate(spd3); //pass the spectrogram data for the spectrogram to be displayed
                    }
                    catch (ArrayIndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                    //canvas.drawBitmap();

                }

                catch (NullPointerException e) {
                    e.printStackTrace();
                }

                TwoDArray f= func1(spd3);
                l = m.transform(f);
                System.out.println(in1);
               // SpectrogramView n= new SpectrogramView(spd1);
            }
        });




    play.setOnClickListener(new View.OnClickListener()

    {
       @Override
        public void onClick (View v) {


           int RECORDER_SAMPLERATE=44100;
           int RECORDER_BPP=16;
           FileInputStream  in2 = null;
           InputStream in1 = null;
           FileOutputStream out = null;
           long totalAudioLen = 0;
           long totalDataLen = totalAudioLen + 36;
           long longSampleRate = RECORDER_SAMPLERATE;
           int channels = 1;
           long byteRate = RECORDER_BPP * RECORDER_SAMPLERATE * channels / 8;


           System.out.println(l);
           String t = l.toString();
           byte[] bytes =t.getBytes();
           System.out.println(bytes);
           byte[] data = new byte[bufferSize];
          // byte a=(byte)0;



           try {
               out = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/last.wav");
               in2=new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/record.wav");

               totalAudioLen = in2.getChannel().size();
               totalDataLen = totalAudioLen + 36;


               WriteWaveFileHeader(out, totalAudioLen, totalDataLen,
                       longSampleRate, channels, byteRate);
               while(  (in2.read(data)) != -1)
               {
                   for(int i=0;i<data.length-100;i++) {
                       data[i] = data[i+50];
                   }
                   out.write(data);
               }

               out.close();
               in2.close();
           }
           catch (FileNotFoundException e) {
               e.printStackTrace();
           }

           catch (IOException e) {
               e.printStackTrace();
           }



           //    FileInputStream ss = new FileInputStream(l.toString());

           //music.wave.Wave f= new Wave(t);
           //byte[] bytes =t.getBytes();
///............................
           // File.WriteAllBytes();
                    //Environment.getExternalStorageDirectory().getAbsolutePath()+ "/last.wav", l);

            MediaPlayer m = new MediaPlayer();


            try {
                m.setDataSource(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/last.wav");
                //System.out.println(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/last.wav");
                //(AUDIO_RECORDER_FOLDER);///mnt/sdcard/AudioRecorder/1446943572730.wav"
            }

            catch (IOException e) {
                e.printStackTrace();
            }

            try {
                m.prepare();
            }

            catch (IOException e) {
                e.printStackTrace();
            }

            m.start();
        }
    });


        //sample write


    }
}

