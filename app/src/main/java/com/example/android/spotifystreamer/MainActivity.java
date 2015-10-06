package com.example.android.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.spotifystreamer.TrackActivity.TrackFragment;

public class MainActivity extends ActionBarActivity implements ArtistFragment.Callback1{
    private static final String TRACKFRAGMENT_TAG = "TFTAG";

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.track) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                Log.v("Trackfragment", "trackfragment");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.track, new TrackFragment(), TRACKFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();


            ArtistFragment ff = (ArtistFragment)getSupportFragmentManager().findFragmentById(R.id.container);
        ff.onStart();


        }

    @Override
    public void onItemSelected(String artist_id)
    {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putString(TrackFragment.ARTIST_ID, artist_id);
           //args.putParcelable(TrackFragment.ARTIST_ID, artist_id);

            TrackFragment fragment = new TrackFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.track, fragment,TRACKFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, TrackActivity.class)
                    .putExtra(Intent.EXTRA_TEXT, artist_id);
            startActivity(intent);

        }
    }

}
