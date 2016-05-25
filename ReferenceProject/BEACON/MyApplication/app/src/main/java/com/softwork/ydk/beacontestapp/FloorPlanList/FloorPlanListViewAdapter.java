package com.softwork.ydk.beacontestapp.FloorPlanList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.softwork.ydk.beacontestapp.R;

import java.util.ArrayList;

/**
 * Created by DongKyu on 2016-05-24.
 */
public class FloorPlanListViewAdapter extends BaseAdapter {
    private ArrayList<FloorPlanListViewItem> listViewItemList = new ArrayList<FloorPlanListViewItem>();

    public FloorPlanListViewAdapter() {
    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.floor_plan_list_view_item, parent, false);
        }

        ImageView floorPlanImageView = (ImageView) convertView.findViewById(R.id.floorPlanImageView);
        TextView floorPlanNameTextView = (TextView) convertView.findViewById(R.id.floorPlanNameAndSizeTextView);
        TextView floorPlanDescriptionTextView = (TextView) convertView.findViewById(R.id.floorPlanDescriptionTextView);

        FloorPlanListViewItem listViewItem = listViewItemList.get(position);

        floorPlanImageView.setImageDrawable(listViewItem.getFloorPlanImage());
        floorPlanNameTextView.setText(listViewItem.getFloorPlanName() + " " + listViewItem.getFloorPlanSize());
        floorPlanDescriptionTextView.setText(listViewItem.getFloorPlanDescription());

        return convertView;
    }


    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(Drawable floorPlanImage, String floorPlanName, String floorPlanSize, String floorPlanDescription) {
        FloorPlanListViewItem item = new FloorPlanListViewItem();

        item.setFloorPlanImage(floorPlanImage);
        item.setFloorPlanName(floorPlanName);
        item.setFloorPlanSize(floorPlanSize);
        item.setFloorPlanDescription(floorPlanDescription);

        listViewItemList.add(item);
    }


}
