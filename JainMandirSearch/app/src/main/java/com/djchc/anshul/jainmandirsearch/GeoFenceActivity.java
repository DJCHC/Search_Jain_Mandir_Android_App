package com.djchc.anshul.jainmandirsearch;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.*;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class GeoFenceActivity extends FragmentActivity
    implements ConnectionCallbacks, OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private String mLatitudeText, mLongitudeText;
    private LocationRequest mLocationRequest;

//    // Request code to use when launching the resolution activity
//    private static final int REQUEST_RESOLVE_ERROR = 1001;
//    // Unique tag for the error dialog fragment
//    private static final String DIALOG_ERROR = "dialog_error";
//    // Bool to track whether the app is already resolving an error
//    private boolean mResolvingError = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_fence);

        buildGoogleApiClient();
        createLocationRequest();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_geo_fence, menu);
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
    public void onConnected(Bundle connectionHint) {
        // Connected to Google Play services!
        // The good stuff goes here.
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitudeText = String.valueOf(mLastLocation.getLatitude());
            mLongitudeText = String.valueOf(mLastLocation.getLongitude());
            Toast.makeText(getApplicationContext(),
                    "on connected: Lat: " + mLatitudeText + "long: " + mLongitudeText,
                    Toast.LENGTH_LONG).show();
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection has been interrupted.
        // Disable any UI components that depend on Google APIs
        // until onConnected() is called.
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // This callback is important for handling errors that
        // may occur while attempting to connect with Google.
        //
        // More about this in the next section.
//        if (mResolvingError) {
//            // Already attempting to resolve an error.
//            return;
//        } else if (result.hasResolution()) {
//            try {
//                mResolvingError = true;
//                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
//            } catch (SendIntentException e) {
//                // There was an error with the resolution intent. Try again.
//                mGoogleApiClient.connect();
//            }
//        } else {
//            // Show dialog using GooglePlayServicesUtil.getErrorDialog()
//            showErrorDialog(result.getErrorCode());
//            mResolvingError = true;
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if (!mResolvingError) {  // more about this later
            mGoogleApiClient.connect();
//        }
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

//    /@Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        updateUI();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
    }

    private void updateUI() {
        mLatitudeText = String.valueOf(mLastLocation.getLatitude());
        mLongitudeText = String.valueOf(mLastLocation.getLongitude());

        //show toast
        Toast.makeText(getApplicationContext(),
        "updateUI: Lat: " + mLatitudeText + "long: " + mLongitudeText,
                Toast.LENGTH_LONG).show();

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
    }

//
//    /* Creates a dialog for an error message */
//    private void showErrorDialog(int errorCode) {
//        // Create a fragment for the error dialog
//        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
//        // Pass the error that should be displayed
//        Bundle args = new Bundle();
//        args.putInt(DIALOG_ERROR, errorCode);
//        dialogFragment.setArguments(args);
//        dialogFragment.show(getSupportFragmentManager(), "errordialog");
//    }
//
//    /* Called from ErrorDialogFragment when the dialog is dismissed. */
//    public void onDialogDismissed() {
//        mResolvingError = false;
//    }
//
//    /* A fragment to display an error dialog */
//    public static class ErrorDialogFragment extends DialogFragment {
//        public ErrorDialogFragment() { }
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            // Get the error code and retrieve the appropriate dialog
//            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
//            return GooglePlayServicesUtil.getErrorDialog(errorCode,
//                    this.getActivity(), REQUEST_RESOLVE_ERROR);
//        }
//
//        @Override
//        public void onDismiss(DialogInterface dialog) {
//            ((MainActivity)getActivity()).onDialogDismissed();
//        }
//    }
}
