package com.example.familymapclient;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment {

    private View fragmentView;
    private MapView mapView;
    private GoogleMap googleMap;
    private FamilyModel familyModel;

    public void setFamilyModel(FamilyModel familyModel) {
        this.familyModel = familyModel;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.map_fragment, container, false);

        Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).colorRes(R.color.male_icon).sizeDp(40);
        ((ImageView)fragmentView.findViewById(R.id.genderImageView)).setImageDrawable(genderIcon);

        mapView = fragmentView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.onResume(); // needed to get the map to display immediately

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                for (FamilyModel.Event event : familyModel.getEvents()) {
                    LatLng latLng = new LatLng(event.getLatitude(), event.getLongitude());
                    FamilyModel.Person person = familyModel.getPerson(event.getPersonID());
                    Marker marker = googleMap.addMarker(new MarkerOptions().position(latLng).title(person.getFirstName() + " " + person.getLastName() + " (" + event.getEventType() + " - " + event.getYear() + ")").snippet(event.getCity() + ", " + event.getCountry()));

                    float markerColor;

                    switch (event.getEventType()) {
                        case "birth":
                            markerColor = BitmapDescriptorFactory.HUE_GREEN;
                            break;
                        case "baptism":
                            markerColor = BitmapDescriptorFactory.HUE_AZURE;
                            break;
                        case "marriage":
                            markerColor = BitmapDescriptorFactory.HUE_MAGENTA;
                            break;
                        case "death":
                            markerColor = BitmapDescriptorFactory.HUE_RED;
                            break;
                        default:
                            markerColor = BitmapDescriptorFactory.HUE_ROSE;
                    }
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(markerColor));
                    builder.include(marker.getPosition());
                }

                LatLngBounds bounds = builder.build();

                int padding = 100; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                googleMap.moveCamera(cu);
            }
        });

        return fragmentView;
    }
}
