package com.example.funkadaa.funkadaa;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.funkadaa.classes.IMainActivity;
import com.example.funkadaa.classes.SelectUpload;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.w3c.dom.Text;

public class MainActivity extends FragmentActivity implements IMainActivity {
    private static final String TAG = "PostFragment";
    private TextView mTextMessage;
    String userid;
    Fragment F;
    TextToSpeech ts;
    private FirebaseAnalytics mFirebaseAnalytics;
    DatabaseReference mDatabase;
    String imageuri;
    Text text;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager manager = getSupportFragmentManager();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText("FunKadaa");
                    F = new HomeFragment();
                    ChangeFrag();
                    return true;

                case R.id.menu_search:
                    mTextMessage.setText("FunKadaa");
                    F= new SearchFragment();

                    ChangeFrag();
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    F = new NotificationFragment();
                    ChangeFrag();
                    return true;

                case R.id.navigation_profile:
                    mTextMessage.setText("Profile");
                    F = new ProfileFragment();
                    Bundle b = new Bundle();
                    b.putString("userid",userid);
                    F.setArguments(b);
                    ChangeFrag();
                    return true;
                case R.id.Upload:
                    mTextMessage.setText("Upload");
                    verifyPermissions();
                    Log.d(TAG, "onClick: opening dialog to choose new photo");
                    SelectUpload SU = new SelectUpload();
                    SU.show(getFragmentManager(), "fragment_edit_name");

                   return true;
            }

            return false;
        }
    };


    void  ChangeFrag() {
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.fragment,F);
        transaction.commit();
        Bundle bundle = new Bundle();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTextMessage = null;
        F=null;
        mFirebaseAnalytics=null;
        mDatabase=null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);



        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    private void verifyPermissions(){
        Log.d(TAG, "verifyPermissions: asking user for permissions");
        String[]permissions = { android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[2]) == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"Permission Granted",Toast.LENGTH_LONG).show();
        }else{
            ActivityCompat.requestPermissions(this, permissions,1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        verifyPermissions();
    }


    @Override
    public void setimage(String uri) {
        imageuri=uri;
        Toast.makeText(this,uri,Toast.LENGTH_LONG).show();
    }
}


