package com.example.android.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
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



    public static class TrackFragment extends Fragment {
        public TrackArrayAdapter mTrackArrayAdapter;

        private final String LOG_TAG = TrackFragment.class.getSimpleName();

        //private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
        // public TrackArrayAdapter mTrackArrayAdapter;
        private static final String STATE_TRACK = "state_track";
        public ArrayList<MyPlaylist> Myplaylists=new ArrayList<MyPlaylist>();
        ListView listview;
        public TrackFragment() {
            setHasOptionsMenu(true);

        }
        @Override
        public void onSaveInstanceState(Bundle outState) {


            outState.putParcelableArrayList(STATE_TRACK, Myplaylists);

            super.onSaveInstanceState(outState);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Intent intent = getActivity().getIntent();


            View rootView = inflater.inflate(R.layout.fragment_track, container, false);
//            if(savedInstanceState != null && savedInstanceState.containsKey(STATE_TRACK) ) {
//                // Myartists=
//                //onRestoreInstanceState(savedInstanceState);
//                Myplaylists=savedInstanceState.getParcelableArrayList(STATE_TRACK);
//            }



            mTrackArrayAdapter = new TrackArrayAdapter(
                    getActivity(),
                    R.layout.custom_track_layout,

                    Myplaylists
            );

            listview = (ListView) rootView.findViewById(R.id.listview_Track);
            if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
                String id = intent.getStringExtra(Intent.EXTRA_TEXT);
                FetchTrack fetchTrack = new FetchTrack();
                fetchTrack.fetchTrack(id);


            }

            listview.setAdapter(mTrackArrayAdapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                    MyPlaylist playlist = mTrackArrayAdapter.getItem(position);

                    Toast toast = Toast.makeText(getActivity(), playlist.trackname, Toast.LENGTH_SHORT);
                    toast.show();


                }
            });


            return rootView;
        }


        public class TrackArrayAdapter extends ArrayAdapter<MyPlaylist> {


            // private final SpotifyArtist[] spotifyArtists;
            // private final Context context;
            private ArrayList<MyPlaylist> Tracks;


            public TrackArrayAdapter(Context context, int resource, ArrayList<MyPlaylist> Tracks) {
                super(context, resource, Tracks);
                //this.spotifyArtists=spotifyArtists;
                this.Tracks = Tracks;
                //this.context=getContext();

            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                MyPlaylist track = Tracks.get(position);
                LayoutInflater inflater = (LayoutInflater) getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = inflater.inflate(R.layout.custom_track_layout, null);


                TextView Trackname = (TextView) rowView.findViewById(R.id.Trackname);
                Trackname.setText(track.trackname);
                TextView Albumname=(TextView) rowView.findViewById(R.id.albumname);
                Albumname.setText(track.albumname);
                ImageView TrackImage = (ImageView) rowView.findViewById(R.id.albumimage);
                // artistImage.
                // Picasso.with(context).load((Uri) artist.images).into(artistImage);
                if (track.image_url != null) {
                    Picasso.with(getContext()).load(track.image_url).into(TrackImage);
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
                                //mTrackArrayAdapter.add(track);
                                Myplaylists.add(new MyPlaylist(track));
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



    public static class MyPlaylist implements Parcelable {

        public static final Parcelable.Creator<MyPlaylist> CREATOR = new Parcelable.Creator<MyPlaylist>()  {

            public MyPlaylist createFromParcel(Parcel in)
            {
                return new MyPlaylist(in);
            }

            public MyPlaylist[] newArray(int size)
            {

                return new MyPlaylist[size];
            }
        };

        public String preview_url;

        public String trackname;
        public String albumname;
        public String image_url;


        public MyPlaylist(Track track) {
            super();
            this.preview_url = track.preview_url;
            this.trackname=track.name;
            this.albumname=track.album.name;
            this.image_url="";
            if (!track.album.images.isEmpty()) {
                this.image_url = track.album.images.get(track.album.images.size() - 2).url;
            }
        }




        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

            dest.writeString(this.preview_url);
            //dest.writeList(images);
            //dest.writeValue(artist);
            dest.writeString(this.trackname);
            dest.writeString(this.albumname);
            dest.writeString(this.image_url);
        }

        protected MyPlaylist(Parcel in) {
            //retrieve
            this.preview_url =in.readString();
            this.trackname=in.readString();
            this.albumname=in.readString();
            this.image_url=in.readString();


        }


    }



}
