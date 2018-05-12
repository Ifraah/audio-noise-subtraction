package com.example.ifraah.audionoisesubtraction;

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
import android.widget.Toast;
import  android.media.MediaExtractor;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.app.Notification;
import android.app.NotificationManager;


import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.Buffer;

import java.nio.ShortBuffer;
import java.nio.ByteOrder;


/**
 * Created by ifraah on 11/10/15.
 */

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.os.Bundle;
import android.widget.Button;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

import android.os.Bundle;
import android.os.Environment;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Toast;
import android.content.Intent;
import android.content.Context;
import android.media.AudioManager;

import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Object;

public class NoiseAddition extends Activity {

    /* private  noiseFile1 = "/res/raw/" + "wavTones.com.unregistered.pink_ - 8dBFS_5s" + ".wav";
     private String noiseFile2 = "/res/raw/" + "wavTones.com.unregistered.white_ - 0dBFS_5s" + ".wav";
     private String noiseFile3 = "/res/raw/" + "wavTones.com.unregistered.white_ - 6dBFS_5s" + ".wav";

 */
    Button pink, white, blue, next, select,play, spink, swhite, sblue;
    int p = 0;
    int w = 0;
    int b = 0;
    /* private String outputfile = null;

     InputStream ins1 = getResources().openRawResource(
             getResources().getIdentifier("raw/first",
                     "raw", getPackageName()));*/
    private int bufferSize = 0;


    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }
    private String getFilename(){


        return (Environment.getExternalStorageDirectory().getAbsolutePath()+ "/combined.wav");

    }


/////////////////////

    short [] shorts= new short [300];
public void extract(String fileToPlay) {
    // see where we find a suitable audiotrack


    MediaExtractor extractor = new MediaExtractor();
    try {
        extractor.setDataSource(fileToPlay);
    } catch (IOException e) {
        //out.release();
        return;
    }

    extractor.selectTrack(0);

    /*String fileType = ".wav";
    if (fileType == null) {
        //out.release();
        extractor.release();
        return;
    }
    */
    try {
        MediaCodec codec = MediaCodec.createDecoderByType(fileType);
        MediaFormat wantedFormat = extractor.getTrackFormat(0);
        codec.configure(wantedFormat, null, null, 0);
        codec.start();

        ByteBuffer[] inputBuffers = codec.getInputBuffers();
        ByteBuffer[] outputBuffers = codec.getOutputBuffers();

        // Allocate our own buffer
        int maximumBufferSizeBytes = 0;
        for (ByteBuffer bb : outputBuffers) {
            int c = bb.capacity();
            if (c > maximumBufferSizeBytes) maximumBufferSizeBytes = c;
        }
        bufferSize= (maximumBufferSizeBytes / 4);

        final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        MediaFormat format = null;
        while (true) {
            long timeoutUs = 1000000;
            int inputBufferIndex = codec.dequeueInputBuffer(timeoutUs);
            if (inputBufferIndex >= 0) {
                ByteBuffer targetBuffer = inputBuffers[inputBufferIndex];
                int read = extractor.readSampleData(targetBuffer, 0);
                int flags = extractor.getSampleFlags();
                if (read > 0)
                    codec.queueInputBuffer(inputBufferIndex, 0, read, 0, flags);
                else
                    codec.queueInputBuffer(inputBufferIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                extractor.advance();
            }

            int outputBufferIndex = codec.dequeueOutputBuffer(bufferInfo, timeoutUs);
            if (outputBufferIndex >= 0) {
                final boolean last = bufferInfo.flags == MediaCodec.BUFFER_FLAG_END_OF_STREAM;

                int s = bufferInfo.size / 4;
                ByteBuffer bytes = outputBuffers[outputBufferIndex];
                ((ByteBuffer) bytes.position(bufferInfo.offset)).asShortBuffer().get(shorts, 0, s * 2);
                        //get(shorts, 0, s * 2);
                process(shorts, 0, s * 2);

                codec.releaseOutputBuffer(outputBufferIndex, false);
                if (last)
                    break;
            } else if (outputBufferIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                outputBuffers = codec.getOutputBuffers();
            } else if (outputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                format = codec.getOutputFormat();
            }
        }

        extractor.release();
        codec.stop();
        codec.release();
    }
    catch (IOException e) {
        e.printStackTrace();
    }
}
    int N=300;
    short[] target=new short[N];
    int idx=0;
   void process(short[] audio,int m, int l) {
       for (int i = 0; i < l; i++) {
           try {
               target[idx++] += audio[i] / 2;
           } catch (ArrayIndexOutOfBoundsException e) {
               e.printStackTrace();
           }

       }
   }

/////////////
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noise_addition);

        play=(Button) findViewById(R.id.button6);
        select = (Button) findViewById(R.id.button5);
        next = (Button) findViewById(R.id.button4);
        blue = (Button) findViewById(R.id.button3);
        white = (Button) findViewById(R.id.button2);
        pink = (Button) findViewById(R.id.button1);


        pink.setEnabled(true);
        white.setEnabled(true);
        blue.setEnabled(true);
        next.setEnabled(false);
        select.setEnabled(true);
        play.setEnabled(false);
        bufferSize = AudioRecord.getMinBufferSize(44100,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        int RECORDER_SAMPLERATE=44100;
        int RECORDER_BPP=16;



        pink.setOnClickListener(new View.OnClickListener() {
            //@Override


            android.content.Context cc = getApplicationContext();
            int ridd = R.raw.first;

            @Override
            public void onClick(View v) throws
                    IllegalArgumentException, SecurityException, IllegalStateException {
                final MediaPlayer m;


                //m.setDataSource(path2);
                m = MediaPlayer.create(cc, ridd);
                m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        if (m != null) {
                            m.release();
                            //m = null;
                        }
                    }
                });
                m.start();


                pink.setEnabled(false);
                select.setEnabled(true);
                p = 1;


                //record.setEnabled(true);
                //next.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Playing pink noise", Toast.LENGTH_LONG).show();
            }
        });

        white.setOnClickListener(new View.OnClickListener()

                                 {
                                     android.content.Context cc = getApplicationContext();
                                     int ridd = R.raw.second;

                                     @Override
                                     public void onClick(View v) throws
                                             IllegalArgumentException, SecurityException, IllegalStateException {
                                         final MediaPlayer m;


                                         //m.setDataSource(path2);
                                         m = MediaPlayer.create(cc, ridd);
                                         m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                             @Override
                                             public void onCompletion(MediaPlayer mediaPlayer) {
                                                 if (m != null) {
                                                     m.release();
                                                     //m = null;
                                                 }
                                             }
                                         });
                                         m.start();


                                         white.setEnabled(false);
                                         select.setEnabled(true);
                                         w = 1;


                                         //record.setEnabled(true);
                                         //next.setEnabled(true);
                                         Toast.makeText(getApplicationContext(), "Playing white noise", Toast.LENGTH_LONG).show();
                                     }
                                 }

        );
        blue.setOnClickListener(new View.OnClickListener()

        {
            android.content.Context cc = getApplicationContext();
            int ridd = R.raw.third;

            @Override
            public void onClick(View v) throws
                    IllegalArgumentException, SecurityException, IllegalStateException {
                final MediaPlayer m;


                //m.setDataSource(path2);
                m = MediaPlayer.create(cc, ridd);
                m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        if (m != null) {
                            m.release();
                            //m = null;
                        }
                    }
                });
                m.start();


                blue.setEnabled(false);
                select.setEnabled(true);
                b = 1;


                //record.setEnabled(true);
                //next.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Playing blue noise", Toast.LENGTH_LONG).show();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), NoiseSubtraction.class);
                //StartActivity(i);
                startActivity(i);
                Toast.makeText(getApplicationContext(), "go to the next screen", Toast.LENGTH_LONG).show();
            }
        });


        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                //StartActivity(i);
                // private void CombineWaveFile(String file1, String file2) {

                //sample rate
                int RECORDER_SAMPLERATE=44100;

                //BPP
                int RECORDER_BPP=16;


                FileInputStream  in2 = null;
                InputStream in1 = null;
                FileOutputStream out = null;
                long totalAudioLen = 0;
                long totalDataLen = totalAudioLen + 36;
                long longSampleRate = RECORDER_SAMPLERATE;
                int channels = 1;
                long byteRate = RECORDER_BPP * RECORDER_SAMPLERATE * channels / 8;

                byte[] data1 = new byte[bufferSize];
                byte[] data2 = new byte[bufferSize];
                byte[] data = new byte[bufferSize];

                try {
                    if(p==1)
                        in1 = getResources().openRawResource(getResources().getIdentifier("raw/first", "raw", getPackageName()));
                    else if (w==1)
                        in1 = getResources().openRawResource(getResources().getIdentifier("raw/second", "raw", getPackageName()));
                    else if(b==1)
                        in1 = getResources().openRawResource(getResources().getIdentifier("raw/third", "raw", getPackageName()));



                    //in1 =ins;
                    //in1 = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/record.wav");
                    in2=new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/record.wav");

                   out = new FileOutputStream(getFilename());
                    /*String[] strArray1 = new String[] {in1.toString()};
                    String[] strArray2 = new String[] {in2.toString()};
                    //FileOutputStream fo =new FileOutputStream(strArray1+strArray2);

                    totalAudioLen = in1.available() + in2.getChannel().size();
                    totalDataLen = totalAudioLen + 36;

                    WriteWaveFileHeader(out, totalAudioLen, totalDataLen,
                            longSampleRate, channels, byteRate);
                    while(  ((in1.read(data)) != -1))    //& (in2.read(data2) != -1))//
                     {

                        out.write(data);
                    }
                   /* while(  (in2.read(data)) != -1) // & (in2.read(data) != -1))//
                    {

                        out.write(data);
                    }

                    out.close();
                    in2.close();
                    in1.close();*/
                    String str=new String( in1.toString());
                    extract(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/record.wav");
                    extract(str);
                    int i=0;
                    byte[] byteBuf = new byte[2*N];
                   /* while (N >= i) {
                        try {
                            byteBuf[i] = (byte) (target[i]);
                            i++;
                        }
                        catch(ArrayIndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                    }
                    out.write(byteBuf);*/
                    // Toast.makeText(this, "Done!!", Toast.LENGTH_LONG).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

//}



                select.setEnabled(false);
                next.setEnabled(true);
                play.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Combining wav files", Toast.LENGTH_LONG).show();


            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws IllegalArgumentException, SecurityException, IllegalStateException {
            MediaPlayer m = new MediaPlayer();


            try {
                m.setDataSource(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/combined.wav");
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

                //record.setEnabled(true);
                //next.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Playing noise+speech", Toast.LENGTH_LONG).show();
            }
        });


        }
}



