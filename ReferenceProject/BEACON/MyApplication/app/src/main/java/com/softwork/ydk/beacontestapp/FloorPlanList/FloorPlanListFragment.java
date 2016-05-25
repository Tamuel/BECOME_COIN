package com.softwork.ydk.beacontestapp.FloorPlanList;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.softwork.ydk.beacontestapp.FloorPlanActivity;
import com.softwork.ydk.beacontestapp.R;

/**
 * Created by DongKyu on 2016-05-25.
 */
public class FloorPlanListFragment extends Fragment {
    private ListView floorPlanListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        int resId = R.layout.floor_plan_list_fragment_layout;
        View view = inflater.inflate(resId, null);
        floorPlanListView = (ListView) view.findViewById(R.id.floorPlanListView);
        FloorPlanListViewAdapter adapter = new FloorPlanListViewAdapter();

        adapter.addItem(null, "1층", "100 x 100 m", "1층 집");
        adapter.addItem(null, "2층", "100 x 200 m", "2층 집");
        adapter.addItem(null, "3층", "100 x 150 m", "3층 집");
        adapter.addItem(null, "1층", "100 x 100 m", "1층 집");
        adapter.addItem(null, "2층", "100 x 200 m", "2층 집");
        adapter.addItem(null, "3층", "100 x 150 m", "3층 집");
        adapter.addItem(null, "1층", "100 x 100 m", "1층 집");
        adapter.addItem(null, "2층", "100 x 200 m", "2층 집");
        adapter.addItem(null, "3층", "100 x 150 m", "3층 집");

        floorPlanListView.setAdapter(adapter);
        floorPlanListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent floorPlanActivity = new Intent(FloorPlanListFragment.this.getActivity(), FloorPlanActivity.class);
                startActivity(floorPlanActivity);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }
}
