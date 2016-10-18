package com.zechassault.cimi.imagemap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.zechassault.cimi.imagemap.anatomie.anatomy.AnatomyTwoActivity;
import com.zechassault.cimi.imagemap.anatomy.AnatomyActivity;
import com.zechassault.cimi.imagemap.domain.DemoActivity;
import com.zechassault.cimi.imagemap.europe.EuropeActivity;
import com.zechassault.cimi.imagemap.operation.OperationActivity;

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
        list.add(new DemoActivity("Europe !  demo", R.drawable.europe, EuropeActivity.class));
        list.add(new DemoActivity("Anatomy !  demo", R.drawable.anatomical_chart_front, AnatomyActivity.class));
        list.add(new DemoActivity("Anatomy 2 !  demo", R.drawable.profile, AnatomyTwoActivity.class));
        CustomAdapter adapter = new CustomAdapter(getBaseContext(), list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(adapter);

    }
}
