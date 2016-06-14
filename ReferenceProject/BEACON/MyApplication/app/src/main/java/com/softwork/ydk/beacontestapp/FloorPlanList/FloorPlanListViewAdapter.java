package com.softwork.ydk.beacontestapp.FloorPlanList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.softwork.ydk.beacontestapp.FloorPlan.FloorPlan;
import com.softwork.ydk.beacontestapp.FloorPlanActivity.FloorPlanActivity;
import com.softwork.ydk.beacontestapp.FloorPlanActivity.FloorPlanEditActivity;
import com.softwork.ydk.beacontestapp.R;
import com.softwork.ydk.beacontestapp.Server.ServerManager;

import java.util.ArrayList;

/**
 * Created by DongKyu on 2016-05-24.
 */
public class FloorPlanListViewAdapter extends BaseAdapter {
    private ArrayList<FloorPlan> floorPlans = new ArrayList<FloorPlan>();

    public FloorPlanListViewAdapter() {
    }

    @Override
    public int getCount() {
        return floorPlans.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        final View v = convertView;

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.floor_plan_list_view_item, parent, false);
        }

        ImageView floorPlanImageView = (ImageView) convertView.findViewById(R.id.floorPlanImageView);
        TextView floorPlanNameTextView = (TextView) convertView.findViewById(R.id.floorPlanNameAndSizeTextView);
        TextView floorPlanDescriptionTextView = (TextView) convertView.findViewById(R.id.floorPlanDescriptionTextView);
        Button floorPlanPositionMapButton = (Button) convertView.findViewById(R.id.modifyFloorPlanButton);
        floorPlanPositionMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent floorPlanActivity = new Intent(v.getContext(), FloorPlanEditActivity.class);
                floorPlanActivity.putExtra("FLOOR_PLAN", pos);//ServerManager.getInstance().getFloorPlans().get(pos));
                v.getContext().startActivity(floorPlanActivity);
            }
        });

        FloorPlan floorPlan = floorPlans.get(position);

        floorPlanImageView.setImageDrawable(floorPlan.getFloorPlanImage());
        floorPlanNameTextView.setText(floorPlan.getBuildingName() + " " + floorPlan.getName() + " " + floorPlan.getFloor() + "층");
        floorPlanDescriptionTextView.setText(
                " (" + floorPlan.getLongitude() + ", " + floorPlan.getLatitude() + ")" + "\n" +floorPlan.getDescription());

        return convertView;
    }


    @Override
    public Object getItem(int position) {
        return floorPlans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(Drawable floorPlanImage, String floorPlanName, String floorPlanDescription, double logitude, double latitude) {
        FloorPlan item = new FloorPlan();

        item.setFloorPlanImage(floorPlanImage);
        item.setName(floorPlanName);
        item.setDescription(floorPlanDescription);
        item.setLongitude(logitude);
        item.setLatitude(latitude);

        floorPlans.add(item);
    }

    public void addItem(FloorPlan floorPlan) {
        floorPlans.add(floorPlan);
    }


}
