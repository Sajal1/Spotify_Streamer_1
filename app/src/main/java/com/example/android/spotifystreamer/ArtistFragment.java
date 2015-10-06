package com.example.android.spotifystreamer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.android.MainThreadExecutor;
import retrofit.client.Response;



public class ArtistFragment extends Fragment {
    private static final String LOG_TAG = ArtistFragment.class.getSimpleName();
    public ArtistArrayAdapter mSpotifyAdapter;
    private static final String STATE_ARTIST = "state_artist";
    ListView listview;
    public ArrayList<MyArtist> Myartists=new ArrayList<MyArtist>();
    public EditText search;


//    public interface Callback1 {
//
//        public void onItemSelected(String artist_id);
//    }


    public ArtistFragment() {
    }
    @Override
    public  void onStart()
    {
        super.onStart();


        //FetchArtist artist=new FetchArtist();
        // artist.fetchartist("coldplay");


    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

        try {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }






    @Override
    public void onSaveInstanceState(Bundle outState) {


        outState.putParcelableArrayList(STATE_ARTIST, Myartists);

        super.onSaveInstanceState(outState);

    }

  /*  @Override
   public void onCreate (Bundle savedInstanceState)
    {
        if(savedInstanceState != null && savedInstanceState.containsKey(STATE_ARTIST) ) {
            Myartists=savedInstanceState.getParcelableArrayList(STATE_ARTIST);
        }
    }*/


  public void onRestoreInstanceState (Bundle savedInstanceState)
    {
        Myartists=savedInstanceState.getParcelableArrayList(STATE_ARTIST);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mSpotifyAdapter = new ArtistArrayAdapter(
                getActivity(),
                R.layout.custome_layout,
                // new ArrayList<MyArtist>()
                Myartists
                //myArtist

        );

      if(savedInstanceState != null && savedInstanceState.containsKey(STATE_ARTIST) ) {
         // Myartists=
          //onRestoreInstanceState(savedInstanceState);
          Myartists=savedInstanceState.getParcelableArrayList(STATE_ARTIST);
       }
        else
      {
          search(rootView);
      }


        mSpotifyAdapter = new ArtistArrayAdapter(
                    getActivity(),
                    R.layout.custome_layout,
                    // new ArrayList<MyArtist>()
                  Myartists
                    //myArtist

            );



            listview = (ListView) rootView.findViewById(R.id.listview_spotify);
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

                    //((Callback1)getActivity()).onItemSelected(artist.artist_id);




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

   public void search(View rootView)
    {
        search = (EditText) rootView.findViewById(R.id.search);


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
                //myArtist.clear();
                mSpotifyAdapter.clear();


            }
        });



        search.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                            try {
                                if (isNetworkAvailable()) {
                                    hideSoftKeyboard(getActivity()); // hide keyboard
                                    search.clearFocus();
                                    return true;
                                } else {
                                    Toast toast = Toast.makeText(getActivity(),
                                            "Check you have a valid network connection",
                                            Toast.LENGTH_SHORT);
                                    toast.show();
                                    return true;
                                }

                            } catch (IllegalStateException e) {
                                Log.e(LOG_TAG, e.getMessage());
                            }
                        }
                        return false;
                    }
                }
        );



    }




    public class ArtistArrayAdapter extends ArrayAdapter <MyArtist>{


        private ArrayList<MyArtist> artists;


        public ArtistArrayAdapter(Context context,int resource, ArrayList<MyArtist> artists)
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
            


            return rowView;
        }


    }

    public class FetchArtist {

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
                          //  mSpotifyAdapter.add(new MyArtist(artist.id,artist.name,artist.uri));

                           // myArtist.add(new MyArtist(artist.id,artist.name,artist.uri));
                            Myartists.add(new MyArtist(artist));
                          //Myartists=new ArrayList<MyArtist>();
                           // myArtist=new MyArtist(artist.id,artist.name,artist.uri);

                            Log.v(LOG_TAG, "Artists entry: " + artist.name);

                        }
                    }
                    mSpotifyAdapter.addAll(Myartists);
                   // Myartists.clear();
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


        public MyArtist(Artist artist) {
            super();
            this.artist_id = artist.id;
            this.name=artist.name;
            this.image_url="";
            if (!artist.images.isEmpty()) {
                this.image_url = artist.images.get(artist.images.size() - 2).url;
            }
        }




        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

            dest.writeString(this.artist_id);
            //dest.writeList(images);
            //dest.writeValue(artist);
            dest.writeString(this.name);
            dest.writeString(this.image_url);
        }

        protected MyArtist(Parcel in) {
            //retrieve
            this.artist_id = in.readString();
            this.name=in.readString();
            this.image_url=in.readString();


        }


    }




}


