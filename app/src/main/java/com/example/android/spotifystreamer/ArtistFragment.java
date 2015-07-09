package com.example.android.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.android.MainThreadExecutor;
import retrofit.client.Response;



/**
 * A placeholder fragment containing a simple view.
 */
public class ArtistFragment extends Fragment {

    public ArtistArrayAdapter mSpotifyAdapter;
    List<Artist> artists;
    private static final String STATE_ARTIST = "state_artist";
    String Artist_id;
    ListView listview;
    public ArrayList<MyArtist> Myartists;
    public EditText search;

    //EditText search;

   //public String search;



    public ArtistFragment() {
    }



    @Override
    public  void onStart()
    {
        // mSpotifyAdapter.onStart();
        super.onStart();


        //FetchArtist artist=new FetchArtist();
       // artist.fetchartist("coldplay");
        //PullArtistdata();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

       mSpotifyAdapter=new ArtistArrayAdapter(
                getActivity(),
                R.layout.custome_layout,
                new ArrayList<Artist>()


        );

       // EditText search=(EditText)rowView.findViewById(R.id.search);
        search= (EditText) rootView.findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString();
                FetchArtist artist = new FetchArtist();
                artist.fetchartist(query);
                mSpotifyAdapter.clear();


            }
        });
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int keyCode,
                                          KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_SEARCH)) {
                    // hide virtual keyboard
                    InputMethodManager imm =
                            (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
                    return true;
                }
                return false;
            }

        });




        listview =(ListView)rootView.findViewById(R.id.listview_spotify);
        listview.setAdapter(mSpotifyAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {



                Artist artist = mSpotifyAdapter.getItem(position);
               // Artist_id=artist.id;
                //MyArtist myArtist=new MyArtist(rtist_id);
                // Toast t = Toast.makeText(th, forecast, Toast.LENGTH_SHORT);
                //  t.show();;
                Intent intent = new Intent(getActivity(), TrackActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, artist.id);
                startActivity(intent);


            }
        });
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                InputMethodManager imm =
                        (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(search.getWindowToken(), 0);

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {



            }
        });








        return rootView;
    }

   @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

       // outState.putParcelableArrayList(STATE_ARTIST, Myartists);
    }

  /*  @Override
    public void onCreate(Bundle instanceState){
        super.onCreate(instanceState);
        //Log.d(LOG_TAG, "In onCreate method.");
        this.setRetainInstance(true);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        listview.setAdapter(mSpotifyAdapter);
    }*/
    public class ArtistArrayAdapter extends ArrayAdapter <Artist>{

       // private final SpotifyArtist[] spotifyArtists;
      // private final Context context;
        private List<Artist> artists;



        public ArtistArrayAdapter(Context context,int resource, List<Artist> artists)
        {
            super(context,resource,artists);
            //this.spotifyArtists=spotifyArtists;
            this.artists=artists;
            //this.context=getContext();

        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Artist artist=artists.get(position);
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.custome_layout, null);


            TextView Artistname = (TextView) rowView.findViewById(R.id.artistname);
            Artistname.setText(artist.name);
            ImageView artistImage = (ImageView) rowView.findViewById(R.id.artistimage);
           // artistImage.
           // Picasso.with(context).load((Uri) artist.images).into(artistImage);
           if( artist.images != null && !artist.images.isEmpty()) {
               Picasso.with(getContext()).load(artist.images.get(0).url).into(artistImage);
           }
            else
           {artistImage.setImageResource(R.mipmap.ic_launcher);}
            //imageView.setImageResource(R.drawable.no);
            // change the icon for Windows and iPhone


            return rowView;
        }


    }

    public class FetchArtist{
        private final String LOG_TAG = FetchArtist.class.getSimpleName();
        public String Artist_id;
        public void fetchartist(String s){


            SpotifyApi api = new SpotifyApi(Executors.newSingleThreadExecutor(),
                    new MainThreadExecutor());
            SpotifyService spotify = api.getService();
            spotify.searchArtists(s, new Callback<ArtistsPager>() {
                @Override
                public void success(ArtistsPager artistsPager, Response response) {
                    //artists=artistsPager.artists.items;
                    //Myartists=new ArrayList<Artist>(Arrays.asList(artistsPager.artists.items));

                    // artistSearchResult.artists = artistsPager.artists.items;
                    //mAdapter.addArtists(artistSearchResult);
                    if(!artistsPager.artists.items.isEmpty()) {
                        for(Artist artist : artistsPager.artists.items) {
                            mSpotifyAdapter.add(artist);

                            Log.v(LOG_TAG, "Artists entry: " + artist.name);

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



    public static class MyArtist implements Parcelable{

        public  final Parcelable.Creator<MyArtist> CREATOR = new Parcelable.Creator<MyArtist>()  {

            public MyArtist createFromParcel(Parcel in)
            {
                return new MyArtist(in);
            }

            public MyArtist[] newArray(int size)
            {

                return new MyArtist[size];
            }
        };

        public String artist_id;
        public List<Image> images;
        public String name;
        public Artist artist;

        public MyArtist(String artist_id,List<Image> images, String name) {
            super();
            this.artist_id=artist_id;
            this.images=images;
            this.name=name;

        }

        public MyArtist(Artist artist) {
            super();
            this.artist=artist;


        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            //write
            dest.writeString(this.artist_id);
            //dest.writeList(images);
        }

        protected MyArtist(Parcel in) {
            //retrieve
            artist_id = in.readString();

        }


    }


}
