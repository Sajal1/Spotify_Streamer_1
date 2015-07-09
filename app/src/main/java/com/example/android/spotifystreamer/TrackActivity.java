package com.example.android.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.android.MainThreadExecutor;
import retrofit.client.Response;


public class TrackActivity extends ActionBarActivity {


   // String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new TrackFragment())
                    .commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.track, menu);
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



    public  class TrackFragment extends Fragment {
        public TrackArrayAdapter mTrackArrayAdapter;

        private final String LOG_TAG = TrackFragment.class.getSimpleName();

        //private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
        // public TrackArrayAdapter mTrackArrayAdapter;


        public TrackFragment() {
            setHasOptionsMenu(true);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Intent intent = getActivity().getIntent();


            View rootView = inflater.inflate(R.layout.fragment_track, container, false);


            mTrackArrayAdapter = new TrackArrayAdapter(
                    getActivity(),
                    R.layout.custom_track_layout,
                    new ArrayList<Track>());


            ListView listview = (ListView) rootView.findViewById(R.id.listview_Track);
            if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
                String id = intent.getStringExtra(Intent.EXTRA_TEXT);
                FetchTrack fetchTrack = new FetchTrack();
                fetchTrack.fetchTrack(id);


            }

            listview.setAdapter(mTrackArrayAdapter);


            return rootView;
        }


        public class TrackArrayAdapter extends ArrayAdapter<Track> {


            // private final SpotifyArtist[] spotifyArtists;
            // private final Context context;
            private List<Track> Tracks;


            public TrackArrayAdapter(Context context, int resource, List<Track> Tracks) {
                super(context, resource, Tracks);
                //this.spotifyArtists=spotifyArtists;
                this.Tracks = Tracks;
                //this.context=getContext();

            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                Track track = Tracks.get(position);
                LayoutInflater inflater = (LayoutInflater) getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = inflater.inflate(R.layout.custom_track_layout, null);


                TextView Trackname = (TextView) rowView.findViewById(R.id.Trackname);
                Trackname.setText(track.name);
                TextView Albumname=(TextView) rowView.findViewById(R.id.albumname);
                Albumname.setText(track.album.name);
                ImageView TrackImage = (ImageView) rowView.findViewById(R.id.albumimage);
                // artistImage.
                // Picasso.with(context).load((Uri) artist.images).into(artistImage);
                if (track.album.images != null) {
                    Picasso.with(getContext()).load(track.album.images.get(0).url).into(TrackImage);
                } else {
                    TrackImage.setImageResource(R.mipmap.ic_launcher);
                }
                //imageView.setImageResource(R.drawable.no);
                // change the icon for Windows and iPhone


                return rowView;
            }


        }

        public class FetchTrack {
            private final String LOG_TAG = FetchTrack.class.getSimpleName();

            //public String Artist_id;
            public void fetchTrack(String id) {


                SpotifyApi api = new SpotifyApi(Executors.newSingleThreadExecutor(),
                        new MainThreadExecutor());
                SpotifyService spotify = api.getService();
                HashMap<String, Object> countryMap = new HashMap<>();
                countryMap.put("country", "US");
                spotify.getArtistTopTrack(id, countryMap, new Callback<Tracks>() {
                    @Override
                    public void success(Tracks tracks, Response response) {

                        // artistSearchResult.artists = artistsPager.artists.items;
                        //mAdapter.addArtists(artistSearchResult);
                        if (!tracks.tracks.isEmpty()) {
                            for ( Track track : tracks.tracks) {
                                mTrackArrayAdapter.add(track);
                                Log.v(LOG_TAG, "Artists entry: " + track.name);

                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("failure", error.toString());
                    }
                });


            }


        }
    }







}
