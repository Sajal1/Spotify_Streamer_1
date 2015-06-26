package com.example.android.spotifystreamer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class SpotifyFragment extends Fragment {

    public SpotifyArrayAdapter mSpotifyAdapter;


    public SpotifyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_main, container, false);

        String[] Artistnames = new String[]{"sajal", "sajib","sajal", "sajib"};

        SpotifyArtist[] spotifyArtists= new SpotifyArtist[4];
        for( int i=0; i<4; i++ )
            spotifyArtists[i] = new SpotifyArtist(Artistnames[i]);

        List<SpotifyArtist> Artists= new ArrayList<SpotifyArtist>(Arrays.asList(spotifyArtists));
        mSpotifyAdapter=new SpotifyArrayAdapter(
                getActivity(),
                R.layout.custome_layout,
                spotifyArtists


        );

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);





        return rootView;
    }


    public class SpotifyArrayAdapter extends ArrayAdapter <SpotifyArtist>{

        private final SpotifyArtist[] spotifyArtists;
       private final Context context;


        public SpotifyArrayAdapter(Context context,int resource, SpotifyArtist[] spotifyArtists)
        {
            super(context,resource);
            this.spotifyArtists=spotifyArtists;
            this.context=context;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.custome_layout, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.artistname);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.artistimage);
            textView.setText(spotifyArtists[position].Artistname);
            //imageView.setImageResource(R.drawable.no);
            // change the icon for Windows and iPhone


            return rowView;
        }


    }

    public class SpotifyArtist{
       //public String Artistname;

       /*public int image;

        public SpotifyArtist(String Artistname,int image){
            this.Artistname=Artistname;
            this.image=image;
        }*/

        public String Artistname;



        public SpotifyArtist(String Artistname){
            this.Artistname=Artistname;

        }




    }

}
