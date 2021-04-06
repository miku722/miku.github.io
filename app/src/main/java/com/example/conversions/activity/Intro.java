package com.example.conversions.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.conversions.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Intro extends AppCompatActivity {

    //Load resources for menu(GridView)
    private String[] MenuName = {"单位换算","分贝仪","水平仪","指南针"};
    private int[] MenuImage = {R.drawable.ic_menu_unit,R.drawable.ic_menu_decibel,R.drawable.ic_menu_level,R.drawable.ic_menu_compass};
    private List<Map<String, Serializable>> map_list;
    String[] from = {"image", "name"};
    int[] to = {R.id.image_menu, R.id.name_menu};
    Intent intent;
    IntentFilter intentFilter;
    AirPlaneModeChangedReceiver airPlaneModeChangedReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);
        //build GridView
        map_list = new ArrayList<>();
        GridView Menu = findViewById(R.id.GV_2);
        for(int i = 0; i < MenuImage.length; i++){
            Map<String, java.io.Serializable> map = new HashMap<>();
            map.put("image",MenuImage[i]);
            map.put("name",MenuName[i]);
            map_list.add(map);
            Log.d("map_list", map_list.toString());
        }
        Log.d("final_map_list", map_list.toString());
        SimpleAdapter MenuAdapter = new SimpleAdapter(Intro.this, map_list,R.layout.item_for_menu,from,to);
        Menu.setAdapter(MenuAdapter);
        Menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        intent = new Intent(Intro.this,UnitConverter.class);

                        break;
                    case 1:
                        intent =new Intent(Intro.this,DecibelMeter.class);
                        break;
                    case 2:
                        intent =new Intent(Intro.this, BubbleLevel.class);
                        break;
                    case 3:
                        intent =new Intent(Intro.this,Compass.class);
                        break;
                }
                startActivity(intent);
            }
        });
        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        registerReceiver(airPlaneModeChangedReceiver,intentFilter);
 }

     class AirPlaneModeChangedReceiver extends BroadcastReceiver {

         @Override
         public void onReceive(Context context, Intent intent) {
             Toast.makeText(context,"airplane mode changed",Toast.LENGTH_SHORT).show();
         }
     }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unregisterReceiver(mBatteryBroadcast);
//    }

}