package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.myapplication.anatomy.AnatomyActivity;
import com.example.myapplication.domain.DemoActivity;
import com.example.myapplication.operation.OperationActivity;

import java.util.ArrayList;

public class StartActivity extends Activity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        setTitle("Select an activity demo");
        listView = (ListView) findViewById(R.id.list);
        ArrayList<DemoActivity> list = new ArrayList<>();
        list.add(new DemoActivity("Operation ! ImageMapView demo", R.drawable.background, OperationActivity.class));
        //list.add(new DemoActivity("Europe !  demo", R.drawable.europe, EuropeActivity.class));
        list.add(new DemoActivity("Anatomy !  demo", R.drawable.anatomical_chart_front, AnatomyActivity.class));
        CustomAdapter adapter = new CustomAdapter(getBaseContext(), list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(adapter);

    }
}
