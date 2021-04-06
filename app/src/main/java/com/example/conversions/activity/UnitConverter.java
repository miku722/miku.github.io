package com.example.conversions.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.conversions.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UnitConverter extends AppCompatActivity {
    private final int[] IconImage = {R.drawable.unitconverter_length, R.drawable.unitconverter_area,
            R.drawable.unitconverter_weight, R.drawable.unitconverter_volume, R.drawable.unitconverter_temperature,
            R.drawable.unitconverter_time, R.drawable.unitconverter_pressure, R.drawable.unitconverter_speed,
            R.drawable.unitconverter_data, R.drawable.unitconverter_radioactivity};
    private final String[] iconName = {"长度", "面积", "重量", "体积", "温度", "时间", "压力", "速度", "数据", "放射性"};
    private final String[] Length_unit = {"mm","cm","m","km","yd","ft","inch","mile","尺","寸","丈","里","海里"};
    private final String[] Area_unit ={"cm²","m²","km²","ha²","ac","ft²","in²","yd²"};
                                                //公頃，/英亩/英尺/英寸/碼
    private String[] Weight_unit ={"mg","g","kg","t","gr","oz","lb","斤","两"};
    List<String> list = new LinkedList<String>();
    double[] valueOfLength = new double[]{1,0.1,0.001,0.000001,0.00109361329834,0.00328083989501,0.0393700787402,0.000006214,0.003,0.03,0.0003,0.000002,0.000000539956803456};
    double[] valueOfArea = new double[]{1,0.0001,0.0000000001,0.00000001,0.0000000247105381467,0.0010764,0.1550003,0.0001196};
    double[] valueOfWeight = new double[]{1,0.001,0.000001,0.000000001,0.0154324,0.0000353,0.0000022046,0.000002,0.00002};
    private List<Map<String, Object>> data_list;
    double Input;
    double ratio =1;
    String SelectedIcon = "长度";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.conversions);

        //spinner: for different button, there is a different spinner
        Spinner spinner = findViewById(R.id.spinner);

        //ListView
        ListView listView = findViewById(R.id.lv);


        //EditText: get the real-time change Text;
        EditText editText = findViewById(R.id.Et_1);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                list.clear();
                if (TextUtils.isEmpty(charSequence)) {
                    Log.d("SecondActivity", "Text is empty");
                    for(int j = 0; j< Length_unit.length; j++){
                        list.add(j,"null");
                    }
                } else {
                    //解析用戶輸入的editText,並將其解析成double型；
                    UnitConverter.this.Input = Double.parseDouble(editText.getText().toString());
                    Log.d("the input is (double)=", String.valueOf(UnitConverter.this.Input));
                    /*根据输入，通过乘以相应的值，达到不同单位的不同值；这里需要设置三个不一样的list，以适应三个按钮 */
                    //0.Length
                    if (SelectedIcon.equals("长度")) {
                        for (int j = 0; j < Length_unit.length; j++) {
                            double temp;
                            temp = valueOfLength[j] * Input * ratio;
                            Log.d("ratio", String.valueOf(ratio));
                            Log.d("Input", String.valueOf(Input));
                            String s = String.valueOf(temp).concat(Length_unit[j]);
                            list.add(s);
                        }
                    }
                    //1.Area
                    if (SelectedIcon.equals("面积")) {
                        for (int j = 0; j < Area_unit.length; j++) {
                            double temp;
                            temp = valueOfArea[j] * Input * ratio;
                            Log.d("ratio", String.valueOf(ratio));
                            Log.d("Input", String.valueOf(Input));
                            String s = String.valueOf(temp).concat(Area_unit[j]);
                            list.add(s);
                        }
                    }
                    //2.Weight
                    if (SelectedIcon.equals("重量")){
                        for (int j = 0; j < Weight_unit.length; j++) {
                            double temp;
                            temp = valueOfWeight[j] * Input * ratio;
                            Log.d("ratio", String.valueOf(ratio));
                            Log.d("Input", String.valueOf(Input));
                            String s = String.valueOf(temp).concat(Weight_unit[j]);
                            list.add(s);
                        }
                    }
                    Log.d("listOfString= ", list.toString());
                    String[] array = list.toArray(new String[list.size()]);
                    ArrayAdapter listViewAdapter = new ArrayAdapter(UnitConverter.this, android.R.layout.simple_list_item_1, array);
                    listViewAdapter.notifyDataSetChanged();
                    listView.setAdapter(listViewAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //GridView
        GridView gridView = findViewById(R.id.GV_1);
        data_list = new ArrayList<Map<String, Object>>();
        getData();
        String[] from = {"image", "text"};
        int[] to = {R.id.image, R.id.text};
        SimpleAdapter GridViewAdapter = new SimpleAdapter(this, data_list, R.layout.item, from, to);
        gridView.setAdapter(GridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("you selected =",iconName[i]);
                SelectedIcon = iconName[i];
                String[] array = list.toArray(new String[list.size()]);
                ArrayAdapter listViewAdapter = new ArrayAdapter(UnitConverter.this,android.R.layout.simple_list_item_1,array);
                listView.setAdapter(listViewAdapter);
                switch (i){
                    //selected length
                    case 0:
                        /*need to change two things:
                        one is the spinner
                        the other is the listView
                         */
                        //spinner for different page, set a different spinner;
                        spinner.setAdapter(setSpinnerAdapter(Length_unit));
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                Log.d("spinner -selected= ",Length_unit[i]);
                                list.clear();
                                String[] array = list.toArray(new String[list.size()]);
                                ArrayAdapter listViewAdapter = new ArrayAdapter(UnitConverter.this,android.R.layout.simple_list_item_1,array);
                                listViewAdapter.notifyDataSetChanged();
                                listView.setAdapter(listViewAdapter);
                                ratio =1/valueOfLength[i];
                                Log.d("ratio",String.valueOf(ratio));
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        break;
                    //selected area
                    case 1:
                        spinner.setAdapter(setSpinnerAdapter(Area_unit));
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                Log.d("spinner -selected= ",Area_unit[i]);
                                list.clear();
                                String[] array = list.toArray(new String[list.size()]);
                                ArrayAdapter listViewAdapter = new ArrayAdapter(UnitConverter.this,android.R.layout.simple_list_item_1,array);
                                listViewAdapter.notifyDataSetChanged();
                                listView.setAdapter(listViewAdapter);
                                ratio = 1/valueOfArea[i];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        break;
                    //selected weight
                    case 2:
                        spinner.setAdapter(setSpinnerAdapter(Weight_unit));
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                Log.d("spinner -selected= ",Weight_unit[i]);
                                list.clear();
                                String[] array = list.toArray(new String[list.size()]);
                                ArrayAdapter listViewAdapter = new ArrayAdapter(UnitConverter.this,android.R.layout.simple_list_item_1,array);
                                listViewAdapter.notifyDataSetChanged();
                                listView.setAdapter(listViewAdapter);
                                ratio = 1/valueOfWeight[i];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    break;
                        //
                }
                Log.d("ratio= ",String.valueOf(ratio));
            }
        });



    }
    public  List<Map<String, Object>>  getData(){
        for(int i = 0; i< IconImage.length; i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", IconImage[i]);
            map.put("text", iconName[i]);
            data_list.add(map);
        }
        return data_list;
    }

    public ArrayAdapter setSpinnerAdapter(String[] strings){
        ArrayAdapter spinnerAdapter = new ArrayAdapter(UnitConverter.this,android.R.layout.simple_spinner_item,strings);
        spinnerAdapter.notifyDataSetChanged();
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return spinnerAdapter;
    }

    public ArrayAdapter setListViewAdapter(String[] strings){
        ArrayAdapter listViewAdapter = new ArrayAdapter(UnitConverter.this, android.R.layout.simple_list_item_1,strings);
        listViewAdapter.notifyDataSetChanged();
        return listViewAdapter;
    }
}
