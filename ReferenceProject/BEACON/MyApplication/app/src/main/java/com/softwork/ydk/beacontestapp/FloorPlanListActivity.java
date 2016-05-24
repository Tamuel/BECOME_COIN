package com.softwork.ydk.beacontestapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

public class FloorPlanListActivity extends Activity {

    private ListView floorPlanListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_plan_list);

        floorPlanListView = (ListView) findViewById(R.id.floorPlanListView);
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
