package com.prabath.mywallet.fregments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;
import com.prabath.mywallet.Others.AppPermissions;
import com.prabath.mywallet.R;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import database.local.models.GLocation;
import database.local.models.Record;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class AddRouteFragment extends Fragment implements OnMapReadyCallback {
    private static String KEY_RECORD = "KEY_RECORD";

    private GoogleMap mMap;
    private Record record;


    public AddRouteFragment() {
    }


    public static AddRouteFragment newInstance(Record record) {
        AddRouteFragment fragment = new AddRouteFragment();
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
        return inflater.inflate(R.layout.fragment_add_route, container, false);

    }

    private Activity a;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initMap();
        a = getActivity();
        ImageButton searchOrigin = a.findViewById(R.id.btnSearchOrigin);
        ImageButton searchMyLocationOrigin = a.findViewById(R.id.btnSetOriginMyLocation);
        ImageButton searchDestination = a.findViewById(R.id.btnSearchDestination);
        ImageButton searchMyLocationDestination = a.findViewById(R.id.btnSetDestinationMyLocation);

        searchOrigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPlace(AUTOCOMPLETE_REQUEST_CODE_ORIGIN);
            }
        });
        searchMyLocationOrigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                record.getLocation().setStartLocation(getMyLocation());
                updateMap();
            }
        });
        searchDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPlace(AUTOCOMPLETE_REQUEST_CODE_DESTINATION);
            }
        });
        searchMyLocationDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                record.getLocation().setEndLocation(getMyLocation());
                updateMap();
            }
        });

    }

    private LatLng getMyLocation() {
        double lat = 6.896963d;
        double lng = 79.860336;
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (AppPermissions.newInstance().check(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)) {
            @SuppressLint("MissingPermission")
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            lat = location.getLatitude();
            lng = location.getLongitude();
        } else {
            AppPermissions.newInstance().request(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        return new LatLng(lat, lng);
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
        updateMap();
    }

    private int AUTOCOMPLETE_REQUEST_CODE_ORIGIN = 1;
    private int AUTOCOMPLETE_REQUEST_CODE_DESTINATION = 2;

    private void selectPlace(int what) {
        List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields)
                .build(getContext());
        startActivityForResult(intent, what);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE_ORIGIN) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                GLocation location = record.getLocation();
                location.setStartLocation(place.getLatLng());
                updateMap();
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                System.out.println(status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        } else if (requestCode == AUTOCOMPLETE_REQUEST_CODE_DESTINATION) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                GLocation location = record.getLocation();
                location.setEndLocation(place.getLatLng());
                updateMap();
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                System.out.println(status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void updateMap() {
        mMap.clear();
        GLocation location = record.getLocation();
        if (location.getStartLocation() == null) {
            record.getLocation().setStartLocation(getMyLocation());
        }
        if (location.getEndLocation() == null) {
            record.getLocation().setEndLocation(getMyLocation());
        }
        CameraUpdate cameraUpdate;
        if (location.getStartLocation().equals(location.getEndLocation())) {
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(record.getLocation().getStartLocation(),17f);

        } else {
            cameraUpdate = CameraUpdateFactory.newLatLngBounds(
                    LatLngBounds.builder()
                            .include(new LatLng(location.getStartLocation().latitude, location.getStartLocation().longitude))
                            .include(new LatLng(location.getEndLocation().latitude, location.getEndLocation().longitude)).build(), 400, 400, 10);

        }

        mMap.animateCamera(cameraUpdate);
        DateTime now = new DateTime();
        try {
            DirectionsResult result = DirectionsApi.newRequest(getGeoContext())
                    .mode(TravelMode.DRIVING)
                    .alternatives(true)
                    .origin(new com.google.maps.model.LatLng(location.getStartLocation().latitude, location.getStartLocation().longitude))
                    .destination(new com.google.maps.model.LatLng(location.getEndLocation().latitude, location.getEndLocation().longitude)).departureTime(now)
                    .await();
            addMarkersToMap(result, mMap);
            addPolyline(result, mMap);
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext.setQueryRateLimit(3)
                .setApiKey(getString(R.string.api_kay))
                .setConnectTimeout(1, TimeUnit.SECONDS)
                .setReadTimeout(1, TimeUnit.SECONDS)
                .setWriteTimeout(1, TimeUnit.SECONDS);
    }

    private void addMarkersToMap(DirectionsResult results, GoogleMap mMap) {
        mMap.addMarker(new MarkerOptions().position(new LatLng(results.routes[0].legs[0].startLocation.lat, results.routes[0].legs[0].startLocation.lng)).title(results.routes[0].legs[0].startAddress));
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(results.routes[0].legs[0].endLocation.lat, results.routes[0].legs[0].endLocation.lng))
                .title(results.routes[0].legs[0].startAddress)
                .snippet(getEndLocationTitle(results))

        );

        marker.showInfoWindow();
    }

    private String getEndLocationTitle(DirectionsResult results) {
        return "Time :" + results.routes[0].legs[0].duration.humanReadable + " Distance :" + results.routes[0].legs[0].distance.humanReadable;
    }

    private void addPolyline(DirectionsResult results, GoogleMap mMap) {
        DirectionsRoute[] routes = results.routes;
        for (DirectionsRoute route : routes) {
            List<LatLng> decodedPath = PolyUtil.decode(route.overviewPolyline.getEncodedPath());
            mMap.addPolyline(new PolylineOptions().color(R.color.primaryBlueLight).addAll(decodedPath));
        }

    }

    public GLocation getSelectedRoute(){
        return record.getLocation();
    }


}
