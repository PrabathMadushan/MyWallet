package com.prabath.mywallet.fregments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.prabath.mywallet.R;

import java.util.Arrays;
import java.util.List;

import database.local.models.GLocation;
import database.local.models.Record;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class AddLocationFragment extends Fragment implements OnMapReadyCallback {

    private static String KEY_RECORD = "KEYRECORD";

    private GoogleMap mMap;
    private Record record;


    public AddLocationFragment() {
    }


    public static AddLocationFragment newInstance(Record record) {
        AddLocationFragment fragment = new AddLocationFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_RECORD, record);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            record = (Record) getArguments().getSerializable(KEY_RECORD);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_location, container, false);
    }

    private Activity a;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        initMap();
        a = getActivity();
        ConstraintLayout mapwraper = a.findViewById(R.id.mapWraper);
        //YoYo.with(Techniques.ZoomIn).duration(200).playOn(mapwraper);
        ImageButton searchLocation = a.findViewById(R.id.btnSearchOrigin);
        ImageButton searchMyLocation = a.findViewById(R.id.btnSetDestinationMyLocation);
        searchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPlace();
            }
        });
        searchMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMyLocation();
            }
        });
    }

    private void initPlaceSearch() {
        if (!Places.isInitialized()) {
            Places.initialize(getContext(), getString(R.string.api_kay));
        }

    }


    private void initMap() {
        initPlaceSearch();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.locationMapFragment);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setLocationMaker();
    }

    private int AUTOCOMPLETE_REQUEST_CODE = 1;

    private void selectPlace() {
        List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields)
                .build(getContext());
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    private void setLocationMaker() {

        if (record.getLocation().getType() == GLocation.Type.LOCATION) {
            if (record.getLocation().getStartLocation() == null) {
                toMyLocation();
            } else {
                addMakerAndMoveTheCamera(record.getLocation().getStartLocation());
            }
        }
    }

    private void toMyLocation() {
        double lat = 6.896963d;
        double lng = 79.860336;

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                }

            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, 1);
            }
        }

        LatLng location = new LatLng(lat, lng);
        record.getLocation().setLocation(location);
        addMakerAndMoveTheCamera(location);

    }

    private void addMakerAndMoveTheCamera(LatLng location) {
        CameraPosition.Builder builder = new CameraPosition.Builder();
        builder.target(location);
        builder.zoom(18);
        CameraPosition cameraPosition = builder.build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        //BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromResource(R.drawable.myimage);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(location).title("My Location").flat(false);
        ((TextView) getActivity().findViewById(R.id.txtPlaceName)).setText("My Location");
        mMap.clear();
        mMap.addMarker(markerOptions);
        mMap.animateCamera(cameraUpdate);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                showLocationOnMap(place);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                System.out.println(status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void showLocationOnMap(Place place) {
        record.getLocation().setLocation(place.getLatLng());
        CameraPosition.Builder builder = new CameraPosition.Builder();
        builder.target(place.getLatLng());
        builder.zoom(18);
        CameraPosition cameraPosition = builder.build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(place.getLatLng()).title(place.getName()).flat(false);
        ((TextView) getActivity().findViewById(R.id.txtPlaceName)).setText(place.getName());
        mMap.clear();
        mMap.addMarker(markerOptions);
        mMap.animateCamera(cameraUpdate);
    }

    public GLocation getSelectedLocation() {
        return record.getLocation();
    }


}
