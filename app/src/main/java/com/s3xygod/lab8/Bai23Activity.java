package com.s3xygod.lab8;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import java.util.concurrent.TimeUnit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import android.net.Uri;
import android.content.ContentResolver;
import android.database.Cursor;
import android.widget.ListView;


public class Bai23Activity extends AppCompatActivity {
    public TextView startTimeField, endTimeField, nameSong;
    private MediaPlayer mediaPlayer;
    private double startTime = 0, finalTime = 0;
    private Handler myHandler = new Handler();
    private int forwardTime = 5000, backwardTime = 5000;
    private SeekBar seekBar;
    private ImageButton playButton, pauseButton, stopButton;
    public static int oneTimeOnly = 0;

    private ListView songView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai23);
        startTimeField = findViewById(R.id.txt_now);
        endTimeField = findViewById(R.id.txt_end);
        nameSong = findViewById(R.id.txt_nowPlay);
        seekBar = findViewById(R.id.pgb_play);
        playButton = findViewById(R.id.btn_play);
        pauseButton = findViewById(R.id.btn_pause);
        stopButton = findViewById(R.id.btn_stop);
        seekBar.setClickable(false);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);
        nameSong.setText("Yêu nhau nhé, bạn thân");
        mediaPlayer = MediaPlayer.create(Bai23Activity.this, R.raw.yeu_nhau_nhe_ban_than);
        songView = (ListView)findViewById(R.id.song_list);

    }


    public void Play(View view) {
        mediaPlayer.start();
        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();
        if (oneTimeOnly == 0) {
            seekBar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }
        endTimeField.setText(String.format(
                "%d phút %d giây",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                        .toMinutes((long) finalTime))));
        startTimeField.setText(String.format(
                "%d phút %d giây",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                        .toMinutes((long) startTime))));
        seekBar.setProgress((int) startTime);
        myHandler.postDelayed(UpdateSongTime, 100);
        pauseButton.setEnabled(true);
        stopButton.setEnabled(true);
        playButton.setEnabled(false);

        Toast.makeText(this, "Playing...", Toast.LENGTH_LONG).show();

    }

    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            startTimeField.setText(String.format("%d phút %d giây",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekBar.setProgress((int) startTime);
            myHandler.postDelayed(this, 100);
        }
    };

    public void Pause(View view) {
        mediaPlayer.pause();
        pauseButton.setEnabled(false);
        playButton.setEnabled(true);
        Toast.makeText(this, "Pausing...", Toast.LENGTH_LONG).show();
    }
    public void forward(View view){
        int temp = (int) startTime;
        if ((temp + forwardTime) <= finalTime){
            startTime = startTime + forwardTime;
            mediaPlayer.seekTo((int) startTime);
        }else {
            Toast.makeText(this, "Cannot jump", Toast.LENGTH_LONG).show();
        }
    }

    public void Stop(View view) {
        mediaPlayer.stop();
        stopButton.setEnabled(false);
        pauseButton.setEnabled(false);
        playButton.setEnabled(true);
        Toast.makeText(this, "Stoping...", Toast.LENGTH_LONG).show();

    }


    public void backward(View view) {
        int temp = (int) startTime;
        if ((temp - backwardTime) <= startTime){
            startTime = startTime - forwardTime;
            mediaPlayer.seekTo((int) startTime);
        }else {
            Toast.makeText(this, "Cannot jump", Toast.LENGTH_LONG).show();
        }
    }
}