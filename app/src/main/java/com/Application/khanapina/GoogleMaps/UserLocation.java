package com.Application.khanapina.GoogleMaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Application.khanapina.Activities.MainActivity;
import com.Application.khanapina.R;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class UserLocation extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMapLongClickListener {

    private static final int REQUEST_LOCATION = 123;
    private final float DEFAULT_ZOOM = 15;
    ArrayList<Marker> tripMarker = new ArrayList<>();
    //Initialize variable
    private TextView textLandmark, textAddress;
    private TextView txtBtnCancel, txtBtnSave;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private View mapView;
    private Geocoder geocoder;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private ResultReceiver resultReceiver;
    private Location mLastKnownLocation;
    private LocationCallback locationCallback;
    private LinearLayout bottomsheet;
    private Marker globalmarker = null;
    private BottomSheetBehavior bottomSheetBehavior;
    private SharedPreferences sharedPreferences, latitudepreferences, longitudepreference;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String UserID;


    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_location);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
        mapView = mapFragment.getView();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(UserLocation.this);
        resultReceiver = new AddressResultReciever(new Handler());


        geocoder = new Geocoder(this);
        sharedPreferences = getSharedPreferences("ADDRESS", MODE_PRIVATE);
        latitudepreferences = getSharedPreferences("latitude", MODE_PRIVATE);
        longitudepreference = getSharedPreferences("longitude", MODE_PRIVATE);


        textAddress = findViewById(R.id.textAddress);
        textLandmark = findViewById(R.id.textLandmark);
        txtBtnCancel = findViewById(R.id.btnCancel);
        txtBtnSave = findViewById(R.id.btnSave);
        bottomsheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomsheet);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        UserID = fAuth.getCurrentUser().getUid();


        txtBtnSave.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("ADDRESS", textAddress.getText().toString());
            editor.apply();
            StoreLocation();
            startActivity(new Intent(UserLocation.this, MainActivity.class));
            finish();
        });

        txtBtnCancel.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            onBackPressed();
        });


        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    resetSelectedMarker();
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

    }

    private void StoreLocation() {
        String id = fStore.collection("users").document(UserID).collection("location").document().getId();
        DocumentReference documentReference = fStore.collection("users").document(UserID).collection("location").document(id);
        HashMap<String, Object> location = new HashMap<>();

        if (textLandmark.getText().toString().isEmpty() || textLandmark.getText().toString().equals("")) {
            location.put("Address", sharedPreferences.getString("ADDRESS", null));
            location.put("latitude", latitudepreferences.getString("latitude", null));
            location.put("longitude", longitudepreference.getString("longitude", null));
            location.put("landmark", "No landmark");
        } else {
            location.put("Address", sharedPreferences.getString("ADDRESS", null));
            location.put("latitude", latitudepreferences.getString("latitude", null));
            location.put("longitude", longitudepreference.getString("longitude", null));
            location.put("landmark", textLandmark.getText().toString());
        }

        documentReference.set(location)
                .addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(), "successfully stored location", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Error storing location", Toast.LENGTH_SHORT).show());
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle_json));

            if (!success) {
                Log.e("MAPSTYLE", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MAPSTYLE", "Can't find style. Error: ", e);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
                View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
                layoutParams.setMargins(0, 1400, 40, 0);
                locationButton.setBackgroundColor(Color.YELLOW);
            }
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(UserLocation.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(UserLocation.this, locationSettingsResponse -> getDeviceLocation());

        task.addOnFailureListener(UserLocation.this, e -> {
            if (e instanceof ResolvableApiException) {
                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                try {
                    resolvableApiException.startResolutionForResult(UserLocation.this, 51);
                } catch (IntentSender.SendIntentException ex) {
                    ex.printStackTrace();
                }
            }
        });

        mMap.setOnMapLongClickListener(UserLocation.this);
        mMap.setOnMyLocationButtonClickListener(UserLocation.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                System.out.println("Location permissions granted, starting location");


            }
        }
    }


    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        fusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                Location location = new Location("providerNA");
                                location.setLatitude(mLastKnownLocation.getLatitude());
                                location.setLongitude(mLastKnownLocation.getLongitude());
                                FetchLatLong(location);
                                FetchAddressFromLatLong(location);
                            } else {
                                LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(10000);
                                locationRequest.setFastestInterval(5000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if (locationResult == null) {
                                            return;
                                        }
                                        mLastKnownLocation = locationResult.getLastLocation();
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                        Location location = new Location("providerNA");
                                        location.setLatitude(mLastKnownLocation.getLatitude());
                                        location.setLongitude(mLastKnownLocation.getLongitude());
                                        FetchLatLong(location);
                                        FetchAddressFromLatLong(location);
                                        fusedLocationProviderClient.removeLocationUpdates(locationCallback);

                                    }
                                };
                                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
                            }
                        } else {
                            Toast.makeText(UserLocation.this, "unable to get last location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @SuppressLint("MissingPermission")
    @Override
    public boolean onMyLocationButtonClick() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(UserLocation.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(final LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(UserLocation.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {

                            mapFragment.getMapAsync(googleMap -> {

                                int latestLocationIndex = locationResult.getLocations().size() - 1;
                                double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                                double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();

                                if (!tripMarker.isEmpty()) {
                                    resetSelectedMarker();
                                    globalmarker = googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));
                                    tripMarker.add(globalmarker);
                                } else {
                                    globalmarker = googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));
                                    tripMarker.add(globalmarker);
                                }
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 17));
                                Location location = new Location("providerNA");
                                location.setLatitude(latitude);
                                location.setLongitude(longitude);
                                FetchLatLong(location);
                                FetchAddressFromLatLong(location);
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            });

                        }

                    }
                }, Looper.getMainLooper());
        return false;

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Location location = new Location("providerNA");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        if (!tripMarker.isEmpty()) {
            resetSelectedMarker();
            globalmarker = mMap.addMarker(new MarkerOptions().position(latLng).draggable(false));
            tripMarker.add(globalmarker);
            FetchLatLong(location);
            FetchAddressFromLatLong(location);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            globalmarker = mMap.addMarker(new MarkerOptions().position(latLng).draggable(false));
            tripMarker.add(globalmarker);
            FetchLatLong(location);
            FetchAddressFromLatLong(location);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }

    }

    private void FetchLatLong(Location location) {
        SharedPreferences.Editor editor = latitudepreferences.edit();
        SharedPreferences.Editor editor2 = longitudepreference.edit();

        editor.putString("latitude", Double.toString(location.getLatitude()));
        editor2.putString("longitude", Double.toString(location.getLongitude()));
        editor.apply();
        editor2.apply();
    }

    private void removeMarker() {
        for (Marker marker : tripMarker) {
            marker.remove();
        }
    }

    private void resetSelectedMarker() {
        if (globalmarker != null) {
            globalmarker.setVisible(true);
            globalmarker = null;
            removeMarker();
            tripMarker.clear();
        }
    }

    private void FetchAddressFromLatLong(Location location) {
        Intent intent = new Intent(this, FerchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }

    private class AddressResultReciever extends ResultReceiver {

        public AddressResultReciever(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == Constants.SUCCESS_RESULT) {
                textAddress.setText(resultData.getString(Constants.RESULT_DATA_KEY));
            } else {
                Toast.makeText(UserLocation.this, resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }
        }
    }

}