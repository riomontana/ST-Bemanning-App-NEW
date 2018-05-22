package com.stbemanning.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.stbemanning.R;
import com.stbemanning.api.Api;
import com.stbemanning.api.ApiListener;
import com.stbemanning.api.PerformNetworkRequest;
import com.stbemanning.model.WorkShift;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpecialShiftActivity extends AppCompatActivity implements ApiListener{

    private ListView lvListOfShifts;
    private TextView tvNumberOfShifts;
    private ImageButton ibBack;

    private String[] list;
    private List <WorkShift> specialWorkShiftList = new ArrayList<>();
    private int userId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_shift);

        lvListOfShifts = (ListView) findViewById(R.id.lvSpecialShifts);
        tvNumberOfShifts = (TextView) findViewById(R.id.tvNumberOfSpecialShifts);
        ibBack = (ImageButton) findViewById(R.id.ibBack);


        userId = getIntent().getIntExtra("userId", -1);
        if(userId != -1) {
            getSpecialWorkShifts(userId);
        }else {
            Toast.makeText(getBaseContext(), "Error - Problem med användar id", Toast.LENGTH_SHORT).show();
        }

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        lvListOfShifts.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WorkShift workShift = specialWorkShiftList.get(position);
                Log.d("SELECTED ITEM", workShift.getCompany());
                showWorkShiftDialog(workShift);
            }
        });
    }

    private void showWorkShiftDialog(final WorkShift workShift) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Arbetspass \n")
                .setMessage("Företag: " + workShift.getCompany() +
                        "\nFrån: " + workShift.getShiftStart() +
                        "\nTill: " + workShift.getShiftEnd());
        adb.setPositiveButton("Tacka ja till jobb", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                updateWorkShift(workShift.getWorkShiftId());
                //TODO uppdatera listan?
            }
        });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adb.show();
    }

    public void updateWorkShift(int workShiftId){
        HashMap<String, String> params = new HashMap<>();
        params.put("work_shift_id", String.valueOf(workShiftId));
        params.put("user_id", String.valueOf(String.valueOf(userId)));
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_UPDATE_WORK_SHIFT, params, this);
        request.execute();
    }

    public void setList() {
        list = new String[specialWorkShiftList.size()];
        for ( int i = 0; i < specialWorkShiftList.size(); i++){
            list[i] = (specialWorkShiftList.get(i).getCompany() + "\nFrån: "
                    + specialWorkShiftList.get(i).getShiftStart() + "\nTill: "
                    + specialWorkShiftList.get(i).getShiftEnd());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        lvListOfShifts.setAdapter(adapter);


    }

    public void getSpecialWorkShifts(int userId) {
        HashMap<String, String> params = new HashMap<>();

        params.put("user_id", String.valueOf(userId));

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_GET_SPECIAL_WORK_SHIFTS, params, this);
        request.execute();
        Log.d("SPECAL_WORK_SHIFT","NetworkRequest executed");
    }

    @Override
    public void apiResponse(JSONObject response) {
        Log.d("json ", response.toString());
        if(response.toString().startsWith("{\"error\":false,\"message\":\"Work shift updated successfully")) {
            getSpecialWorkShifts(userId);
            Toast.makeText(getBaseContext(), "Arbetspasset uppdaterat och finns nu i din kalender", Toast.LENGTH_LONG).show();
        }else{
            try {
                JSONArray jsonArray = response.getJSONArray("special_work_shifts");
                tvNumberOfShifts.setText("Antal specialjobb tilgängliga: " + jsonArray.length());
                specialWorkShiftList.clear();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int workShiftId = jsonObject.getInt("work_shift_id");
                    String shiftStart = jsonObject.getString("shift_start");
                    String shiftEnd = jsonObject.getString("shift_end");
                    String company = jsonObject.getString("customer_name");
                    WorkShift workShift = new WorkShift(workShiftId, shiftStart, shiftEnd, company);
                    specialWorkShiftList.add(workShift);
                }
                setList();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
