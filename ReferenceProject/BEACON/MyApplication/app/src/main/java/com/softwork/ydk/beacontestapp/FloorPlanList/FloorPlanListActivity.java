package com.softwork.ydk.beacontestapp.FloorPlanList;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.softwork.ydk.beacontestapp.R;

public class FloorPlanListActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_plan_list);

        ViewPager viewPager = (ViewPager) findViewById(R.id.FloorPlanViewPager);
        FloorPlanListFragmentAdapter adapter = new FloorPlanListFragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new FloorPlanListFragment(), getString(R.string.floorplan_list));
        adapter.addFragment(new FloorPlanListFragment(), "Category 2");
        adapter.addFragment(new FloorPlanUserInfoFragment(), getString(R.string.action_user));
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.FloorPlanTabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.floor_plan_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_user:
                break;

            case R.id.action_settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
