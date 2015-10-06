package com.example.android.spotifystreamer;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sajalchowdhury on 8/25/15.
 */

    public class Player extends FragmentActivity {


    private ArrayList<TrackActivity.MyPlaylist> simpleTracks=new ArrayList<>();
    private int position;
    private String previewUrl;
   private long dur;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        simpleTracks = extras.getParcelableArrayList("Track");
        position = extras.getInt("position");
        previewUrl = simpleTracks.get(position).preview_url;
        dur=simpleTracks.get(position).duration;
        //Log.v("track", "time: " +dur);
        showDialog();
//        BroadcastReceiver receiver=new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//            }
//        };
    }

    private void showDialog() {
        FragmentManager fm = getSupportFragmentManager();
        PlayerFragment fragment = new PlayerFragment();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, fragment)
                .addToBackStack(null).commit();
    }

    public class PlayerFragment extends DialogFragment implements View.OnClickListener,SeekBar.OnSeekBarChangeListener,MediaPlayer.OnSeekCompleteListener {

        private ImageView albumimage;
        private ImageButton play;
        private ImageButton next;
        private ImageButton prev;
        private TextView artistName;
        private TextView albumName;
        private TextView trackName;
        private SeekBar seekBar;
        private Handler mHandler= new Handler();


        boolean mIsLargeLayout;
        private PlayerService mediaPlayer;
        private boolean isBound = false;
        private boolean isPaused = true;
        private boolean isInit = false;
        private int pos=0;
        int max;


        private ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                PlayerService.LocalBinder mediaPlayerBinder =
                        (PlayerService.LocalBinder) service;
                mediaPlayer = mediaPlayerBinder.getService();
                isBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                isBound = false;
            }
        };

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            Log.v("sajal", "DialogPlayerFragment onDestroyView");
        }


        @Override
        public void onDestroy() {
            super.onDestroy();
//Log.v("nanda", "DialogPlayerFragment onDestroy");
        }

        @Override
        public void onDetach() {
            super.onDetach();
            if (getActivity().isFinishing() && isBound) {
                getActivity().unbindService(connection);
                isBound = false;
            }
        }

        @Override
        public void onStart() {
            super.onStart();
            if (!isBound) {
                Intent intent = new Intent(getActivity(), PlayerService.class);
                startService(intent);
                getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);
            }
        }

        @Override
        public void onStop() {
            super.onStop();
            Log.v("sajal", "DialogPlayerFragment onStop");
        }


        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            View rootView = inflater.inflate(R.layout.fragment_player, container, false);



//            albumimage = (ImageView) rootView.findViewById(R.id.albumimage);
////            Picasso.with(getActivity()).load(simpleTracks.get(position).image_url).into(albumimage);
//            play = (ImageButton) rootView.findViewById(R.id.play);
//            play.setOnClickListener(this);


            if (isPaused) {
                play = (ImageButton) rootView.findViewById(R.id.play);
                play.setOnClickListener(this);
                play.setImageResource(android.R.drawable.ic_media_play);
            } else {
                play = (ImageButton) rootView.findViewById(R.id.play);
                play.setOnClickListener(this);
                play.setImageResource(android.R.drawable.ic_media_pause);
            }

            next = (ImageButton) rootView.findViewById(R.id.next);
            next.setOnClickListener(this);
            prev = (ImageButton) rootView.findViewById(R.id.prev);
            prev.setOnClickListener(this);



              //  simpleTracks = getArguments().getParcelableArrayList("Track");
//                position = getArguments().getInt("position");
//                previewUrl = simpleTracks.get(position).preview_url;

           // previewUrl = simpleTracks.get(position).preview_url;
            artistName = (TextView) rootView.findViewById(R.id.artist_name);
            //artistName.setText(simpleTracks.get(position).getArtist());
            albumName = (TextView) rootView.findViewById(R.id.album_name);
            albumName.setText(simpleTracks.get(position).albumname);
            trackName = (TextView) rootView.findViewById(R.id.track_name);
            trackName.setText(simpleTracks.get(position).trackname);
            seekBar=(SeekBar) rootView.findViewById(R.id.seekBar);
            //seekBar.setMax((int) dur);


            albumimage = (ImageView) rootView.findViewById(R.id.albumimage);
            Picasso.with(getActivity()).load(simpleTracks.get(position).image_url).into(albumimage);


            return rootView;

        }
        //long dur= 0;


        @Override
        public void onClick(View view) {


            switch (view.getId()) {
                case R.id.play: {

                    if (!isInit) {
                        //mediaPlayer.setSong(previewUrl);
                        mediaPlayer.initMusicPlayer(previewUrl);
                        mediaPlayer.playSong();
                        play.setImageResource(android.R.drawable.ic_media_pause);
                        isInit = true;
                        isPaused = false;
                        //seekBar.setMax(mediaPlayer.getduration());
                        //run();
                        //max=(int)dur/10000;
                        max=30;
                        seekBar.setMax(max);
//                        if(mediaPlayer.isplaying())
//                        {
//                        thread.start();
//                        }
                        thread.start();

                    } else {
                        if (!isPaused) {
                            play.setImageResource(android.R.drawable.ic_media_play);
                            isPaused = true;
                            mediaPlayer.pause();
                        } else {
                            play.setImageResource(android.R.drawable.ic_media_pause);
                            isPaused = false;
                            mediaPlayer.start();
                            thread.start();
                        }
                    }
                }

                break;
                case R.id.next: {
                    int size = simpleTracks.size();
                    if (position == size - 1) {
                        position = 0;
                    } else {
                        position += 1;
                    }
                    previewUrl = simpleTracks.get(position).preview_url;
                    //artistName.setText(simpleTracks.get(position).getArtist());
                    albumName.setText(simpleTracks.get(position).albumname);
                    trackName.setText(simpleTracks.get(position).trackname);
                    albumimage = (ImageView) getView().findViewById(R.id.albumimage);
                    Picasso.with(getActivity()).load(simpleTracks.get(position).image_url).into(albumimage);
                    Log.v("bungbagong", previewUrl);
                    mediaPlayer.reset();
                    mediaPlayer.setPreview(previewUrl);
                    mediaPlayer.playSong();
                    //thread.start();
                    isInit = true;
                    isPaused =false;


                }

                break;

                case R.id.prev: {
                    int size = simpleTracks.size();
                    if (position == 0) {
                        position = size-1;
                    } else {
                        position -= 1;
                    }
                    previewUrl = simpleTracks.get(position).preview_url;
                    //artistName.setText(simpleTracks.get(position).getArtist());
                    albumName.setText(simpleTracks.get(position).albumname);
                    trackName.setText(simpleTracks.get(position).trackname);
                    albumimage = (ImageView) getView().findViewById(R.id.albumimage);
                    Picasso.with(getActivity()).load(simpleTracks.get(position).image_url).into(albumimage);
                    Log.v("bungbagong", previewUrl);
                    mediaPlayer.reset();
                    mediaPlayer.setPreview(previewUrl);
                    mediaPlayer.playSong();
                    //thread.start();
                    isInit = true;
                    isPaused =false;


                }


            }
        }

Thread thread =new Thread(new Runnable() {

    public void run() {
       // int pos=0;
        while (pos<=max) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Update the progress bar
            mHandler.post(new Runnable() {
                public void run() {
                 seekBar.setProgress(pos);
                }
            });
            pos++;
        }
    }
});



        public void run() {
            // mp is your MediaPlayer
            // progress is your ProgressBar

            int currentPosition = 0;
            //int total =(int)dur;
            seekBar.setMax(30);
            while ( currentPosition<=27 ) {
//                try {
//                    Thread.sleep(1000);
//                    //currentPosition = mediaPlayer.getcurrentposition()/1000;
//                    currentPosition++;
//                } catch (InterruptedException e) {
//                    return;
//                } catch (Exception e) {
//                    return;
//                }
                currentPosition++;
                seekBar.setProgress(currentPosition);

            }



        }


        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if(b){
                //mediaPlayer.seekTo(progress);
                //seekBar.setProgress(seekBar);
            }
            else{
                // the event was fired from code and you shouldn't call player.seekTo()
            }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onSeekComplete(MediaPlayer mediaPlayer) {

        }
    }
}








