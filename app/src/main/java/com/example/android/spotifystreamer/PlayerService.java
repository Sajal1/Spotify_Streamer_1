package com.example.android.spotifystreamer;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;
import java.util.Random;

/**
 * Created by sajalchowdhury on 9/11/15.
 */
public class PlayerService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener{

//    public PlayerService() {
//        super("PlayerService");
//    }
    // Binder given to clients


    //media player
    private MediaPlayer player;
    //song list
    public TrackActivity.MyPlaylist song;
    String preview;
    //current position
    private int songPosn;
    private final IBinder mBinder = new LocalBinder();
    // Random number generator
    private final Random mGenerator = new Random();

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    LocalBroadcastManager broadcaster;

    @Override
    public void onCreate(){
        super.onCreate();
        //create the service
        songPosn=0;
//create player
        player = new MediaPlayer();
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

        //initMusicPlayer();
        //initMusicPlayer();

       // broadcaster=LocalBroadcastManager.getInstance(this);
    }

//    public void sendResult(String message) {
//        Intent intent = new Intent("Result");
//        if(message != null)
//            intent.putExtra("Message", message);
//        broadcaster.sendBroadcast(intent);
//    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }


    public void initMusicPlayer(String preview_url){
        //set player properties
//        player.setWakeMode(getApplicationContext(),
//                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        preview=preview_url;

        playSong();
       // mServiceHandler.handleMessage();
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void setPreview(String preview_url)
    {
        preview=preview_url;
        playSong();
    }



    public int getduration()
    {

       return player.getDuration();
    }


    public void reset(){
        player.reset();
    }

    public void playSong(){
        //play a song
        player.reset();
        try{
            player.setDataSource(preview);
            player.prepareAsync();
        }
        catch (Exception e)
        {

        }
       // Log.v("sajal", String.valueOf(player.getDuration()));
    }
 int seek;
    public void pause()
    {
        player.pause();
       seek= player.getCurrentPosition();

    }

    public boolean isplaying()
    {
        return player.isPlaying();
    }

    public void prepare() throws IOException {
        try {
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public int getcurrentposition()
    {
        return player.getCurrentPosition();
    }

    public void start()
    {
        player.seekTo(seek);
        player.start();

    }



    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();

    }

    public class LocalBinder extends Binder {
        PlayerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return PlayerService.this;


        }
    }

  public class ServiceHandler extends Handler  {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.

//            player.setWakeMode(getApplicationContext(),
//                    PowerManager.PARTIAL_WAKE_LOCK);
//            player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        }
        }




    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }

    /** method for clients */
    public int getRandomNumber() {
        return mGenerator.nextInt(100);
    }
    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */



}
