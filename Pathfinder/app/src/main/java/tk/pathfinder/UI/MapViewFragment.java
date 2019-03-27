package tk.pathfinder.UI;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.io.IOException;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import tk.pathfinder.Map.Api;
import tk.pathfinder.Map.Map;
import tk.pathfinder.Networking.AppStatus;
import tk.pathfinder.R;


public class MapViewFragment extends Fragment {
    private MapView view;
    private Map map;
    private MapFragment mapController;
    private int mapId;

    private SectionsPagerAdapter mPageAdapter;
    private ViewPager mViewPager;

    public MapViewFragment() {
        // Required empty public constructor
    }

    public static MapViewFragment newInstance(int mapId) {
        MapViewFragment fragment = new MapViewFragment();
        Bundle args = new Bundle();
        args.putInt("mapId", mapId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mapId = getArguments().getInt("mapId");
        }

        AppStatus status = (AppStatus)getActivity().getApplicationContext();
        if(status == null || status.getCurrentBuildingId() != mapId){
            try {
                map = Api.GetMap(mapId);
            } catch (IOException e) {
                new Alert("API Error", "Could not pull map \n" + e.getMessage(), this.getContext());
                return;
            }
        }
        else{
            map = status.getCurrentMap();
        }

        mapController = new MapFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map_view, container, false);
        view = new MapView(this.getContext());
        view.setMap(map);

        mPageAdapter = new SectionsPagerAdapter(this.getFragmentManager());
        mViewPager = v.findViewById(R.id.map_container);
        mViewPager.setAdapter(mPageAdapter);
        return v;
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private int count = -1;
        private int smallestFloor;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            int floor = smallestFloor + position;
            view.setCurrentFloor(floor);
            return mapController;
        }

        @Override
        public int getCount() {
            if(count == -1){ // define it if it's not defined and cache it
                int[] range = map.getFloorRange();
                smallestFloor = range[0];
                count = (Math.abs(range[0]) + Math.abs(range[1]));
                if(smallestFloor < 0) count += 1; // for floor 0
            }
            return count;
        }
    }

    @SuppressLint("ValidFragment")
    public class MapFragment extends Fragment{

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_map_controller, container, false);
            FrameLayout layout = v.findViewById(R.id.map_view_display);
            layout.addView(view);
            return v;
        }
    }
}
