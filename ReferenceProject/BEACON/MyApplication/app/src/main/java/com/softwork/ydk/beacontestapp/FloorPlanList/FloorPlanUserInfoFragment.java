package com.softwork.ydk.beacontestapp.FloorPlanList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.softwork.ydk.beacontestapp.R;
import com.softwork.ydk.beacontestapp.Server.ServerManager;

/**
 * Created by DongKyu on 2016-05-25.
 */
public class FloorPlanUserInfoFragment extends Fragment {
    private ListView floorPlanListView;
    private TextView nickNameTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        int resId = R.layout.user_info_fragment_layout;
        View view = inflater.inflate(resId, null);

        nickNameTextView = (TextView) view.findViewById(R.id.userIDTextView);
        nickNameTextView.setText(ServerManager.getInstance().getUserNickName());

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }
}
