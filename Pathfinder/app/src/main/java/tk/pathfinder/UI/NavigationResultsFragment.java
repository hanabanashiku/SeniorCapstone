package tk.pathfinder.UI;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import tk.pathfinder.Map.Room;
import tk.pathfinder.Networking.AppStatus;
import tk.pathfinder.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NavigationResultsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NavigationResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavigationResultsFragment extends Fragment implements NavigationResult.NavigationResultListener {

    private String keywords;

    private OnFragmentInteractionListener mListener;

    public NavigationResultsFragment() {
        // Required empty public constructor
    }

    public static NavigationResultsFragment newInstance(String keywords) {
        NavigationResultsFragment fragment = new NavigationResultsFragment();
        Bundle args = new Bundle();
        args.putString("keywords", keywords);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            keywords = getArguments().getString("keywords");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_navigation_results, container, false);
        LinearLayout layout = v.findViewById(R.id.nav_search_results_content);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction t = fm.beginTransaction();
        List<Room> results = null;
        if(keywords != null){
            results = AppStatus.getCurrentMap().findDestination(keywords);
            for(Room r : results){
                NavigationResult result = NavigationResult.newInstance(r);
                t.add(layout.getId(), result);
            }
        }

        if(results == null || results.size() == 0){
            TextView text = new TextView(getContext());
            text.setText(getString(R.string.dest_not_found));
            text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            layout.addView(text);
        }
        else
            t.commit();

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // a room has been selected, show navigation page.
    public void onRoomSelected(int roomId){

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}