package com.example.funkadaa.funkadaa;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.funkadaa.classes.ImageThumbnailDownloaderAsync;
import com.example.funkadaa.classes.SearchAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;


public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "MainActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;
    String userid;
    ImageView btn;

    DatabaseReference mref;
    GridView gridview;
    SearchAdapter ad;
    Context c;
    TextToSpeech ts;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c=getContext();
        ts=new TextToSpeech(c, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

            }
        });
        userid = getArguments().getString("userid");
        mref = FirebaseDatabase.getInstance().getReference().child("users").child(userid);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    TextView tv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    public boolean isServicesOK()
    {
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext());

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(getContext(), "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void getSpeechInput(View view) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, 007);
        } else {
            Toast.makeText(getContext(), "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn = (ImageView)getView().findViewById(R.id.imageView5);


        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        gridview = (GridView)getView().findViewById(R.id.gridview);
        ad = new SearchAdapter(c);
        gridview.setAdapter(ad);
        mref.addValueEventListener(userListener);
        Button signout = (Button)getView().findViewById(R.id.signoutbutton);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(c,FirstScreenActivity.class);
                startActivity(i);
            }
        });
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });




        Button btn2=(Button)getView().findViewById(R.id.button7);

        btn2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                if (isServicesOK()) {
                    Intent intent = new Intent(getContext(), MapActivity.class);
                    startActivity(intent);
                }
            }});

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                String postid = ad.getPostIDs().get(position);
                Intent i = new Intent(c,PostActivity.class);
                i.putExtra("postid",postid);
                startActivity(i);

            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if (resultCode == RESULT_OK) {
            Uri photoUri = data.getData();
            if (photoUri != null) {
                try {
                    Intent i = new Intent(c,UploadDpActivity.class);
                    i.putExtra("userid", userid);
                    i.putExtra("uri",photoUri.toString());
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }}


        }





    ValueEventListener userListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Get Post object and use the values to update the UI
            String username = (String)dataSnapshot.child("name").getValue();
            ts.speak(username,TextToSpeech.QUEUE_FLUSH,null);
            ImageView dp = (ImageView)getView().findViewById(R.id.imageView5);
            TextView name = (TextView)getView().findViewById(R.id.textView5);
            name.setText(username);
            String dpurl = (String)dataSnapshot.child("dp").getValue();
            new ImageThumbnailDownloaderAsync(dp,c).execute(dpurl);

            ArrayList<String> s = new ArrayList<>();
            ArrayList<String> p = new ArrayList<>();
            DataSnapshot postSnapshot = dataSnapshot.child("userposts");
            for (DataSnapshot messageSnapshot: postSnapshot.getChildren()) {
                String imageID = (String) messageSnapshot.child("imageID").getValue();
                String postID = (String) messageSnapshot.getKey();
                s.add(imageID);
                p.add(postID);
            }
            ad.setImgIDs(s);
            ad.setPostIDs(p);
            ad.notifyDataSetChanged();

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w("POSTS", "loadPost:onCancelled", databaseError.toException());
            // ...
        }
    };
}
