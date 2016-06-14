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

import com.softwork.ydk.beacontestapp.FloorPlan.FloorPlan;
import com.softwork.ydk.beacontestapp.FloorPlanActivity.FloorPlanEditActivity;
import com.softwork.ydk.beacontestapp.R;
import com.softwork.ydk.beacontestapp.Server.ServerManager;

import java.io.Serializable;

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

        for(FloorPlan floorPlan : ServerManager.getInstance().getFloorPlans()) {
            adapter.addItem(floorPlan);
        }

        floorPlanListView.setAdapter(adapter);
        floorPlanListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent floorPlanActivity = new Intent(FloorPlanListFragment.this.getActivity(), FloorPlanEditActivity.class);
                floorPlanActivity.putExtra("FLOOR_PLAN", ServerManager.getInstance().getFloorPlans().get(position));
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
