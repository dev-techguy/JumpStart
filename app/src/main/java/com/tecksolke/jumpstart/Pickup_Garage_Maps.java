package com.tecksolke.jumpstart;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

public class Pickup_Garage_Maps extends AppCompatActivity implements OnMapReadyCallback {


    TemporaryDB temporaryDB;
    TextToSpeech toSpeech;
    int result;
    String username = null;

    static GoogleMap mGoogleMap;
    private Location mLastLocation;
    NiftyDialogBuilder niftyDialogBuilder;
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;
    public LatLng userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup__garage__maps);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        temporaryDB = new TemporaryDB(this);
        niftyDialogBuilder = NiftyDialogBuilder.getInstance(this);


        //call method for reading SQlite
        readData();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //maps code
        if (googleServicesAvailable()) {
            Toast.makeText(this, "Searching for nearest garage", Toast.LENGTH_LONG).show();
            initMap();
        } else {
            //show no layout.
        }


    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_Fragment);
        mapFragment.getMapAsync(this);
    }

    public boolean googleServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int isAvailable = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (googleApiAvailability.isUserResolvableError(isAvailable)) {
            Dialog dialog = googleApiAvailability.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Can't Connect To Google Play Services", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        //goToLocation(-0.3227005,37.6446532);
        goToLocationZoom(-1.3028618, 36.7073059, 15);
    }

    private void goToLocation(double latitude, double longitude) {
        LatLng ll = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(ll);
        mGoogleMap.moveCamera(cameraUpdate);
    }

    private void goToLocationZoom(double latitude, double longitude, float zoom) {
        //check if user has enabled location
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        } else {
            //speech method
            final String garage = "Allow JumpStart to use your Location, To find you nearest garage";
            toSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        result = toSpeech.setLanguage(Locale.UK);

                        toSpeech.speak("Hello " + getUsername() + "." + garage, TextToSpeech.QUEUE_FLUSH, null);
                    } else {
                        niftyDialogBuilder
                                .withIcon(getResources().getDrawable(R.mipmap.logologo))
                                .withTitle("Speech Talking Status")
                                .withTitleColor("#9dffffff")
                                .withMessage("Feature not supported in your device")
                                .withMessageColor("#9dffffff")
                                .withDialogColor("#2A3342")
                                .withButton1Text("OK")
                                .withDuration(700)
                                .isCancelable(false)
                                .withEffect(Effectstype.Shake)
                                .setButton1Click(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        toSpeech.stop();
                                        niftyDialogBuilder.cancel();
                                    }
                                })
                                .show();
                    }
                }
            });

            niftyDialogBuilder
                    .withIcon(getResources().getDrawable(R.mipmap.logologo))
                    .withTitle("Location Status")
                    .withTitleColor("#9dffffff")
                    .withMessage(garage)
                    .withMessageColor("#9dffffff")
                    .withDialogColor("#2A3342")
                    .withButton1Text("ALLOW")
                    .withDuration(700)
                    .isCancelable(false)
                    .withEffect(Effectstype.Shake)
                    .setButton1Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            toSpeech.stop();
                            niftyDialogBuilder.cancel();
                        }
                    })
                    .show();
        }

        LatLng ll = new LatLng(latitude, longitude);
        //move map camera
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mGoogleMap.addMarker(new MarkerOptions().position(ll).title("This is Nairobi"));
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mGoogleMap.moveCamera(cameraUpdate);
    }

    //function for reading data
    public void readData() {
        Cursor res = temporaryDB.getAllData();
        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                username = String.valueOf((res.getString(1)));
            }
        }
        setUsername(username);
    }

    //get username
    public String getUsername() {
        return username;
    }

    //set username
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        toSpeech.stop();
        startActivity(new Intent(Pickup_Garage_Maps.this, PickUp_JumpStart.class));
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            deleteData();
            startActivity(new Intent(this, Login.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    //delete data from SQlite
    public void deleteData(){
        Cursor res = temporaryDB.getAllData();
        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                temporaryDB.deleteData(res.getString(0));
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (toSpeech != null) {
            toSpeech.stop();
            toSpeech.shutdown();
        }
        super.onDestroy();
    }

}
