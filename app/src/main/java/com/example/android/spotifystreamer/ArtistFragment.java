package com.example.android.spotifystreamer;

import android.content.Context;
import android.content.Intent;
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
    //List<Artist> artists;
    private static final String STATE_ARTIST = "state_artist";
    //String Artist_id;
    ListView listview;
    public ArrayList<MyArtist> Myartists;
    public EditText search;

    //EditText search;

   //public String search;



    public ArtistFragment() {
    }



    @Override
    public void onCreate(Bundle instanceState){
        super.onCreate(instanceState);
        //Log.d(LOG_TAG, "In onCreate method.");
        //this.setRetainInstance(true);
        Myartists=instanceState.getParcelableArrayList(STATE_ARTIST);



        mSpotifyAdapter=new ArtistArrayAdapter(
                getActivity(),
                R.layout.custome_layout,
                //new ArrayList<MyArtist>()
                Myartists


        );
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(STATE_ARTIST, Myartists);
    }

/*
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        listview.setAdapter(mSpotifyAdapter);
    }*/



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);



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



                MyArtist artist = mSpotifyAdapter.getItem(position);
               // Artist_id=artist.id;
                //MyArtist myArtist=new MyArtist(rtist_id);
                // Toast t = Toast.makeText(th, forecast, Toast.LENGTH_SHORT);
                //  t.show();;
                Intent intent = new Intent(getActivity(), TrackActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, artist.artist_id);
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

    public class ArtistArrayAdapter extends ArrayAdapter <MyArtist>{

       // private final SpotifyArtist[] spotifyArtists;
      // private final Context context;
        private List<MyArtist> artists;



        public ArtistArrayAdapter(Context context,int resource, List<MyArtist> artists)
        {
            super(context,resource,artists);
            //this.spotifyArtists=spotifyArtists;
            this.artists=artists;
            //this.context=getContext();

        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            MyArtist artist=artists.get(position);
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.custome_layout, null);


            TextView Artistname = (TextView) rowView.findViewById(R.id.artistname);
            Artistname.setText(artist.name);
            ImageView artistImage = (ImageView) rowView.findViewById(R.id.artistimage);
           // artistImage.
           // Picasso.with(context).load((Uri) artist.images).into(artistImage);
           if( artist.image_url != null && !artist.image_url.isEmpty()) {
               Picasso.with(getContext()).load(artist.image_url).into(artistImage);
           }
            else
           {artistImage.setImageResource(R.mipmap.ic_launcher);}
            //imageView.setImageResource(R.drawable.no);
            // change the icon for Windows and iPhone


            return rowView;
        }


    }

    public class FetchArtist {
        //ArrayList<MyArtist> Artist;
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
                           // mSpotifyAdapter.add(artist);

                            Myartists.add(new MyArtist(artist.id,artist.name,artist.uri));

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

        public static final Parcelable.Creator<MyArtist> CREATOR = new Parcelable.Creator<MyArtist>()  {

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
       // public List<Image> images;
        public String name;
        //public Artist artist;
        public String image_url;

        public MyArtist(String artist_id, String name,String image_url) {
            super();
            this.artist_id=artist_id;
            //this.images=images;
            this.name=name;
            //this.artist=artist;
            this.image_url=image_url;

        }




        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

            dest.writeString(artist_id);
            //dest.writeList(images);
            //dest.writeValue(artist);
            dest.writeString(name);
            dest.writeString(image_url);
        }

        protected MyArtist(Parcel in) {
            //retrieve
            artist_id = in.readString();
            name=in.readString();
            image_url=in.readString();


        }


    }


}
