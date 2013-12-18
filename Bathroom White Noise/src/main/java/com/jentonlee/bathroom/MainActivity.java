package com.jentonlee.bathroom;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.ToggleButton;
import java.util.Random;


import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends FragmentActivity {

    //Media Players
    private MediaPlayer fanPlayer;
    private MediaPlayer flushPlayer;
    private MediaPlayer faucetPlayer;
    private MediaPlayer handDryerPlayer;
    private MediaPlayer handWashComboPlayer;
    private MediaPlayer paperTowelPlayer;

    //Timer
    Timer flushTimer = new Timer();
    Timer faucetTimer = new Timer();
    Timer fanTimer = new Timer();
    Timer handDryerTimer = new Timer();
    Timer handWashComboTimer = new Timer();
    Timer paperTowelTimer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public class PlaceholderFragment extends Fragment {

        Switch allSwitch;
        Switch fanSwitch;
        Switch flushSwitch;
        Switch faucetSwitch;
        Switch handDryerSwitch;
        Switch handWashComboSwitch;
        Switch paperTowelSwitch;
        SeekBar volumeSeekBar;
        AudioManager audioManager;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            //audio controls
            setVolumeControlStream(AudioManager.STREAM_MUSIC);

            //initializing buttons
            allSwitch = (Switch) rootView.findViewById(R.id.allSwitch);
            fanSwitch = (Switch) rootView.findViewById(R.id.fanSwitch);
            flushSwitch = (Switch) rootView.findViewById(R.id.flushSwitch);
            faucetSwitch = (Switch) rootView.findViewById(R.id.faucetSwitch);
            handDryerSwitch = (Switch) rootView.findViewById(R.id.handDryerSwitch);
            handWashComboSwitch = (Switch) rootView.findViewById(R.id.handWashComboSwitch);
            paperTowelSwitch = (Switch) rootView.findViewById(R.id.paperTowelsSwitch);
            volumeSeekBar = (SeekBar) rootView.findViewById(R.id.volumeSeekBar);
            audioManager = (AudioManager) getSystemService(MainActivity.AUDIO_SERVICE);
            volumeSeekBar.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeSeekBar.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));

            volumeSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
            {
                @Override
                public void onStopTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2)
                {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            });


            allSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        // The toggle is enabled
                        Log.e("BATHROOM", "All On");
                        fanSwitch.setChecked(true);
                        flushSwitch.setChecked(true);
                        faucetSwitch.setChecked(true);
                        handDryerSwitch.setChecked(true);
                        handWashComboSwitch.setChecked(true);
                        paperTowelSwitch.setChecked(true);

                    } else {
                        // The toggle is disabled
                        Log.e("BATHROOM", "All Off");
                        fanSwitch.setChecked(false);
                        flushSwitch.setChecked(false);
                        faucetSwitch.setChecked(false);
                        handDryerSwitch.setChecked(false);
                        handWashComboSwitch.setChecked(false);
                        paperTowelSwitch.setChecked(false);
                    }
                }
            });

            fanSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        // The toggle is enabled
                        Log.e("BATHROOM", "Fan On");
                        fanPlayer = MediaPlayer.create(MainActivity.this, R.raw.fan_apartment_cardoid_wav);

                        fanPlayer.setLooping(true);
                        fanPlayer.start();


                    } else {
                        // The toggle is disabled
                        Log.e("BATHROOM", "Fan Off");
                        fanPlayer.stop();
                    }
                }
            });

            flushSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // The toggle is enabled

                        Random random = new Random();

                        Log.e("BATHROOM", "Flush On");
                        flushPlayer = MediaPlayer.create(MainActivity.this, R.raw.flush_apartment_fanon_cardoid);
                        //currentSong = R.raw.song1;

                        //after waiting a random amount of seconds between 1-10 seconds, flush fires
                        //then will wait a random amount of seconds between 46-105 seconds before firing again
                        flushTimer.schedule(
                                new TimerTask() {
                                    @Override
                                    public void run() {
                                        // your code here
                                        flushPlayer.start();
                                    }
                                },
                                (random.nextInt(10)+1)*1000,
                                (random.nextInt(60)+46)*1000
                        );

                    } else {
                        // The toggle is disabled
                        Log.e("BATHROOM", "Flush Off");
                        flushPlayer.stop();
                    }
                }
            });
            faucetSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // The toggle is enabled

                        Random random = new Random();

                        Log.e("BATHROOM", "Faucet On");
                        faucetPlayer = MediaPlayer.create(MainActivity.this, R.raw.faucet_apartment_fanon_omni);

                        //after waiting a random amount of seconds between 1-10 seconds, faucet fires
                        //then will wait a random amount of seconds between 11-70 seconds before firing again
                        faucetTimer.schedule(
                                new TimerTask() {
                                    @Override
                                    public void run() {
                                        // your code here
                                        faucetPlayer.start();
                                    }
                                },
                                (random.nextInt(10)+1)*1000,
                                (random.nextInt(60)+11)*1000
                                //(random.nextInt(10)+1)*1000
                        );


                    } else {
                        // The toggle is disabled
                        Log.e("BATHROOM", "Faucet Off");
                        faucetPlayer.stop();
                    }
                }
            });
            handDryerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // The toggle is enabled

                        Random random = new Random();

                        Log.e("BATHROOM", "Hand Dryer On");
                        handDryerPlayer = MediaPlayer.create(MainActivity.this, R.raw.hand_dryer2);

                        //after waiting a random amount of seconds between 1-10 seconds, faucet fires
                        //then will wait a random amount of seconds between 21-80 seconds before firing again
                        handDryerTimer.schedule(
                                new TimerTask() {
                                    @Override
                                    public void run() {
                                        // your code here
                                        handDryerPlayer.start();
                                    }
                                },
                                (random.nextInt(10)+1)*1000,
                                (random.nextInt(60)+21)*1000
                                //(random.nextInt(10)+1)*1000
                        );


                    } else {
                        // The toggle is disabled
                        Log.e("BATHROOM", "Hand Dryer Off");
                        handDryerPlayer.stop();
                    }
                }
            });

            handWashComboSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // The toggle is enabled

                        Random random = new Random();

                        Log.e("BATHROOM", "Hand Wash Combo On");
                        handWashComboPlayer = MediaPlayer.create(MainActivity.this, R.raw.automatic_hand_washer_dryer_in_public_toilets_operating);

                        //after waiting a random amount of seconds between 1-10 seconds, hand wash combo fires
                        //then will wait a random amount of seconds between 21-80 seconds before firing again
                        handWashComboTimer.schedule(
                                new TimerTask() {
                                    @Override
                                    public void run() {
                                        // your code here
                                        handWashComboPlayer.start();
                                    }
                                },
                                (random.nextInt(10)+1)*1000,
                                (random.nextInt(60)+21)*1000
                                //(random.nextInt(10)+1)*1000
                        );


                    } else {
                        // The toggle is disabled
                        Log.e("BATHROOM", "Hand Wash Combo Off");
                        handWashComboPlayer.stop();
                    }
                }
            });

            paperTowelSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // The toggle is enabled

                        Random random = new Random();

                        Log.e("BATHROOM", "Paper Towel On");
                        paperTowelPlayer = MediaPlayer.create(MainActivity.this, R.raw.manual_paper_towel_dispenser_being_used_toilet);

                        //after waiting a random amount of seconds between 1-10 seconds, paper towel fires
                        //then will wait a random amount of seconds between 11-70 seconds before firing again
                        paperTowelTimer.schedule(
                                new TimerTask() {
                                    @Override
                                    public void run() {
                                        // your code here
                                        paperTowelPlayer.start();
                                    }
                                },
                                (random.nextInt(10)+1)*1000,
                                (random.nextInt(60)+11)*1000
                        );


                    } else {
                        // The toggle is disabled
                        Log.e("BATHROOM", "Paper Towel Off");
                        paperTowelPlayer.stop();
                    }
                }
            });

            return rootView;

        }

    }

}
