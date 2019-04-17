package com.example.familymapclient;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MapFragment extends Fragment {

    private static SettingsData settings;
    private static List<Marker> markers = new ArrayList<>();

    private View fragmentView;
    private MapView mapView;
    private GoogleMap googleMap;
    private FamilyModel familyModel;
    private PersonData pd;
    private FragmentActivity fragmentActivity;
    private String eventId;
    private boolean hideOptions = false;

    private static final int SETTINGS_ACTIVITY = 1;

    public void setHideOptions(boolean hideOptions) {
        this.hideOptions = hideOptions;
    }

    public void setFamilyModel(FamilyModel familyModel) {
        this.familyModel = familyModel;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!hideOptions) {
            inflater.inflate(R.menu.main_menu, menu);

            MenuItem menuItem = menu.findItem(R.id.menu_settings);
            menuItem.setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_gear)
                    .colorRes(R.color.colorMarkerGrey)
                    .actionBarSize());
            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Intent intent = new Intent(getActivity(), SettingsActivity.class);
                    intent.putExtra("SETTINGS", settings);
                    startActivityForResult(intent, SETTINGS_ACTIVITY);
                    return false;
                }
            });

            menuItem = menu.findItem(R.id.menu_filter);
            menuItem.setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_filter)
                    .colorRes(R.color.colorMarkerGrey)
                    .actionBarSize());

            menuItem = menu.findItem(R.id.menu_search);
            menuItem.setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_search)
                    .colorRes(R.color.colorMarkerGrey)
                    .actionBarSize());

            super.onCreateOptionsMenu(menu,inflater);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        settings = (SettingsData)data.getSerializableExtra("SETTINGS");

        if (requestCode == SETTINGS_ACTIVITY) {
            switch (settings.mapType) {
                case 0:
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    break;
                case 1:
                    googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    break;
                case 2:
                    googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    break;
                case 3:
                    googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    break;
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.map_fragment, container, false);

        Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_android).colorRes(R.color.android_icon).sizeDp(40);

        ((ImageView)fragmentView.findViewById(R.id.genderImageView)).setImageDrawable(genderIcon);

        fragmentActivity = getActivity();

        fragmentView.findViewById(R.id.eventInformation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean onEventActivity = fragmentActivity instanceof EventActivity;

                if (!onEventActivity && fragmentView.findViewById(R.id.introText).getVisibility() == View.VISIBLE) {
                    return;
                }

                Intent intent = new Intent(getActivity(), PersonActivity.class);
                intent.putExtra("PERSON_ID", pd.personId);
                intent.putExtra("FAMILY_MODEL", familyModel);
                startActivity(intent);

                if (onEventActivity) {
                    fragmentActivity.finish();
                }
            }
        });

        mapView = fragmentView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.onResume(); // needed to get the map to display immediately

        OnMapReadyCallback omrc = new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker m) {
                        pd = (PersonData)m.getTag();
                        ((TextView)fragmentView.findViewById(R.id.textName)).setText(pd.fullName);
                        ((TextView)fragmentView.findViewById(R.id.textEvent)).setText(pd.event + ": " + pd.city + ", " + pd.country + " (" + pd.year + ")");
                        fragmentView.findViewById(R.id.introText).setVisibility(View.GONE);
                        fragmentView.findViewById(R.id.personInfo).setVisibility(View.VISIBLE);

                        FontAwesomeIcons icon;
                        int color;
                        if (pd.gender.equals("m")) {
                            icon = FontAwesomeIcons.fa_male;
                            color = R.color.male_icon;
                        } else {
                            icon = FontAwesomeIcons.fa_female;
                            color = R.color.female_icon;
                        }

                        Drawable genderIcon = new IconDrawable(getActivity(), icon).colorRes(color).sizeDp(40);
                        ((ImageView)fragmentView.findViewById(R.id.genderImageView)).setImageDrawable(genderIcon);

                        return true;
                    }
                });

                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                markers.clear();

                Marker centerMarker = null;
                for (FamilyModel.Event event : familyModel.getEvents()) {
                    LatLng latLng = new LatLng(event.getLatitude(), event.getLongitude());
                    FamilyModel.Person person = familyModel.getPerson(event.getPersonID());
                    Marker marker = googleMap.addMarker(new MarkerOptions().position(latLng).title(person.getFirstName() + " " + person.getLastName() + " (" + event.getEventType() + " - " + event.getYear() + ")").snippet(event.getCity() + ", " + event.getCountry()));

                    markers.add(marker);

                    if (eventId != null && eventId.equals(event.getEventID())) {
                        centerMarker = marker;
                    }

                    PersonData personData = new PersonData(person.getPersonID(),person.getFirstName() + " " + person.getLastName(), event.getEventType(), event.getCity(), event.getCountry(), event.getYear(), person.getGender(), event.getEventID());
                    marker.setTag(personData);

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

                if (centerMarker != null) {
                    pd = (PersonData)centerMarker.getTag();
                    ((TextView)fragmentView.findViewById(R.id.textName)).setText(pd.fullName);
                    ((TextView)fragmentView.findViewById(R.id.textEvent)).setText(pd.event + ": " + pd.city + ", " + pd.country + " (" + pd.year + ")");
                    fragmentView.findViewById(R.id.introText).setVisibility(View.GONE);
                    fragmentView.findViewById(R.id.personInfo).setVisibility(View.VISIBLE);

                    CameraUpdate cu = CameraUpdateFactory.newLatLng(centerMarker.getPosition());
                    googleMap.moveCamera(cu);
                }

//                LatLngBounds bounds = builder.build();
//
                // adjust bounds of map to show the visible markers
//                int padding = 100; // offset from edges of the map in pixels
//                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                if (eventId == null || settings == null) {
                    return;
                }

                String personId = familyModel.getEvent(eventId).getPersonID();
                FamilyModel.Person p = familyModel.getPerson(personId);

                // life story lines
                if (settings.storyLines) {
                    List<Marker> storyLines = new ArrayList<>();

                    for (Marker marker : markers) {
                        PersonData pd = (PersonData)marker.getTag();

                        if (pd.personId.equals(personId)) {
                            storyLines.add(marker);
                        }
                    }

                    storyLines.sort(new Comparator<Marker>() {
                        @Override
                        public int compare(Marker o1, Marker o2) {
                            return ((PersonData)o1.getTag()).year - ((PersonData)o2.getTag()).year;
                        }
                    });

                    Marker temp = null;
                    for (Marker marker : storyLines) {
                        if (temp == null) {
                            temp = marker;
                        } else {
                            int color = settings.storyLineColor;
                            int lineColor = 0;
                            switch (color) {
                                case 0:
                                    lineColor = Color.RED;
                                    break;
                                case 1:
                                    lineColor = Color.GREEN;
                                    break;
                                case 2:
                                    lineColor = Color.BLUE;
                                    break;
                            }
                            PolylineOptions options = new PolylineOptions()
                                    .add(temp.getPosition(), marker.getPosition())
                                    .width(5)
                                    .color(lineColor);
                            googleMap.addPolyline(options);
                            temp = marker;
                        }
                    }
                }

                // spouse lines
                if (settings.spouseLines) {
                    List<Marker> spouseMarkers = new ArrayList<>();
                    Marker personMarker = null;
                    for (Marker marker : markers) {
                        PersonData pd = (PersonData)marker.getTag();

                        if (pd.personId.equals(p.getSpouse())) {
                            spouseMarkers.add(marker);
                        } else if (pd.eventId.equals(eventId)) {
                            personMarker = marker;
                        }
                    }

                    if (spouseMarkers.size() == 0) {
                        return;
                    }

                    spouseMarkers.sort(new Comparator<Marker>() {
                        @Override
                        public int compare(Marker o1, Marker o2) {
                            return ((PersonData)o1.getTag()).year - ((PersonData)o2.getTag()).year;
                        }
                    });

                    int color = settings.spouseLineColor;
                    int lineColor = 0;
                    switch (color) {
                        case 0:
                            lineColor = Color.RED;
                            break;
                        case 1:
                            lineColor = Color.GREEN;
                            break;
                        case 2:
                            lineColor = Color.BLUE;
                            break;
                    }

                    PolylineOptions options = new PolylineOptions()
                            .add(personMarker.getPosition(), spouseMarkers.get(0).getPosition())
                            .width(5)
                            .color(lineColor);
                    googleMap.addPolyline(options);
                }
            }
        };

        setHasOptionsMenu(true);

        mapView.getMapAsync(omrc);

        return fragmentView;
    }

    class PersonData {
        String personId;
        String fullName;
        String event;
        String city;
        String country;
        int year;
        String gender;
        String eventId;

        PersonData(String personId, String fullName, String event, String city, String country, int year, String gender, String eventId) {
            this.personId = personId;
            this.fullName = fullName;
            this.event = event;
            this.city = city;
            this.country = country;
            this.year = year;
            this.gender = gender;
            this.eventId = eventId;
        }
    }
}
