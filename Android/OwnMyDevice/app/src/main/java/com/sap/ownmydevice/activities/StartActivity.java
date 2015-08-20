package com.sap.ownmydevice.activities;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;


import com.google.gson.Gson;
import com.sap.mobi.ownmydevice.R;
import com.sap.ownmydevice.database.OMDContract;
import com.sap.ownmydevice.models.Device;
import com.sap.ownmydevice.models.Member;
import com.sap.ownmydevice.models.Team;
import com.sap.ownmydevice.network.ApiFactory;
import com.sap.ownmydevice.network.DeviceApi;
import com.sap.ownmydevice.network.NetworkController;
import com.sap.ownmydevice.network.TeamApi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class StartActivity extends AppCompatActivity {
    View mainLayout;
    TextView deviceName;
    TextView deviceOS;
    TextView deviceModel;
    TextView deviceOwner;
    Spinner teamSelector;
    Spinner memberSelector;
    Member selectedOwner;
    AppCompatButton setOwnerBtn;
    EditText equipIDInput;
    ProgressDialog progressDialog;
    private TextInputLayout equipIDInputLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Set Owner");
        fetchAppData();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void fetchAppData() {
        TeamApi teamApi = (TeamApi) NetworkController.getInstance().getApiOfType(ApiFactory.TEAM_API);
        teamApi.getTeamList("yes", new Callback<ArrayList<Team>>() {
            @Override
            public void success(ArrayList<Team> teams, Response response) {
                for (int i = 0; i < teams.size(); i++) {
                    OMDContract.getDBConnection(StartActivity.this).insertTeam(teams.get(i));
                }
                initUI();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public void initUI(){

        deviceName = (TextView)findViewById(R.id.device_name);
        deviceOS = (TextView)findViewById(R.id.device_os);
        deviceModel = (TextView)findViewById(R.id.device_model);
        deviceOwner = (TextView)findViewById(R.id.device_owner);
        memberSelector=(Spinner)findViewById(R.id.start_activity_member_selector);
        teamSelector=(Spinner)findViewById(R.id.start_activity_team_selector);
        setOwnerBtn = (AppCompatButton)findViewById(R.id.set_owner);
        equipIDInput = (EditText)findViewById(R.id.device_id_layout_input);
        equipIDInputLayout = (TextInputLayout)findViewById(R.id.device_id_layout);
        equipIDInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                setOwnerBtn.setEnabled(true);
                setOwnerBtn.setBackgroundColor(getResources().getColor(R.color.blue));
                return true;
            }
        });
        setDeviceDetails();
        initSelectors();
        showHideViews();
        setOwnerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedOwner != null && equipIDInput.getText()!=null && equipIDInput.getText().length() != 0){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(StartActivity.this);
                    alertDialogBuilder.setMessage("Are you sure you want to proceed");
                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progressDialog = ProgressDialog.show(StartActivity.this,"","Please Wait",true,false);

                            String deviceNameString = deviceName.getText().toString();
                            String deviceOSString = deviceOS.getText().toString();
                            String deviceModelString = (deviceModel.getText().toString());
                            String deviceEquipIDString = (equipIDInput.getText().toString());
                            String deviceOwnerString = (selectedOwner.getUrl());
                            String deviceCurrOwnerString = (selectedOwner.getUrl());

                            String deviceArchString = (System.getProperty("os.arch"));
                            String deviceMemoryString = (String.valueOf(getTotalMemory()));
                            final Device currentDevice = new Device(deviceNameString,deviceModelString,deviceOSString,deviceArchString,deviceMemoryString,deviceArchString,deviceEquipIDString,deviceOwnerString,deviceCurrOwnerString);
                            DeviceApi deviceApi = (DeviceApi)NetworkController.getInstance().getApiOfType(ApiFactory.DEVICE_API);
                            deviceApi.addDevice(currentDevice, new Callback<Device>() {
                                @Override
                                public void success(Device device, Response response) {
                                    Gson gson = new Gson();
                                    currentDevice.setId(device.getId());
                                    String currentDeviceString = gson.toJson(currentDevice);
                                    PreferenceManager.getDefaultSharedPreferences(StartActivity.this)
                                            .edit().putString("CURR_DEVICE", currentDeviceString).commit();

                                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    progressDialog.dismiss();
                                    finish();
                                }

                                @Override
                                public void failure(RetrofitError error) {

                                }
                            });

                        }
                    });
                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }
            }
        });

//        final ArrayList<Team> teams  = OMDContract.getDBConnection(getApplicationContext()).getTeamList();
//        ArrayList<String> teamNames = new ArrayList<String>();
//        for(int i = 0 ; i < teams.size() ;i++){
//            Log.wtf("TEAM_NAMES",teams.get(i).getTeamName());
//        }

    }

    private void showHideViews() {
        ProgressBar pb = (ProgressBar)findViewById(R.id.start_activity_progress);
        pb.setVisibility(View.GONE);
        memberSelector.setVisibility(View.VISIBLE);
        teamSelector.setVisibility(View.VISIBLE);
        setOwnerBtn.setVisibility(View.VISIBLE);
        equipIDInput.setVisibility(View.VISIBLE);
        equipIDInputLayout.setVisibility((View.VISIBLE));
    }

    public void initSelectors(){
        final ArrayList<Team> teams  = OMDContract.getDBConnection(StartActivity.this).getTeamList();
        ArrayList<String> teamNames = new ArrayList<String>();
        for(int i = 0 ; i < teams.size() ;i++){
            teamNames.add(teams.get(i).getTeamName());
        }
        ArrayAdapter adapter = new ArrayAdapter(StartActivity.this, android.R.layout.simple_spinner_item, teamNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        teamSelector.setAdapter(adapter);

//        ArrayList<Member> members =  OMDContract.getDBConnection(getApplicationContext()).getMembers(teams.get(0).getUrl());
//        selectedOwner = members.get(0);
//        initMemberSelectorAction(members);

         Team team = OMDContract.getDBConnection(StartActivity.this).getTeam("1");
        Log.wtf("TEAM_NAME_QUERY", team.getTeamName());

        teamSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<Member> members = OMDContract.getDBConnection(StartActivity.this).getMembers(teams.get(position).getUrl());
                initMemberSelectorAction(members);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initMemberSelectorAction(final ArrayList<Member> members)
    {
        ArrayList<String> memberNames = new ArrayList<String>();
        for(int i = 0 ; i < members.size() ;i++){
            memberNames.add(members.get(i).getMemberName());
        }
        ArrayAdapter adapter = new ArrayAdapter(StartActivity.this, android.R.layout.simple_spinner_item, memberNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        memberSelector.setAdapter(adapter);


        memberSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOwner = members.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void setDeviceDetails(){
        deviceName.setText(getPhoneName());
        deviceModel.setText(Build.MODEL);
        deviceOS.setText(Build.VERSION.RELEASE + " " + String.valueOf(Build.VERSION.SDK_INT));
    }
    public String getPhoneName() {
        BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
        return myDevice.getName();

    }
    public long getTotalMemory() {

        String str1 = "/proc/meminfo";
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(    localFileReader, 8192);
            str2 = localBufferedReader.readLine();//meminfo
            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }
            //total Memory
            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;
            localBufferedReader.close();
            return initial_memory;
        }
        catch (IOException e)
        {
            return -1;
        }
    }
}
