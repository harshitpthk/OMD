package com.sap.ownmydevice.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;



import android.bluetooth.BluetoothAdapter;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sap.mobi.ownmydevice.R;
import com.sap.ownmydevice.database.OMDContract;
import com.sap.ownmydevice.models.Device;
import com.sap.ownmydevice.models.Member;
import com.sap.ownmydevice.models.Team;
import com.sap.ownmydevice.network.ApiFactory;
import com.sap.ownmydevice.network.DeviceApi;
import com.sap.ownmydevice.network.NetworkController;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends AppCompatActivity {
    static final int NUM_ITEMS = 2;
    DemoCollectionPagerAdapter mAdapter;
    Spinner teamNavigator ;

    ViewPager mPager;
    TabLayout tabLayout;
    Member selectedMember;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String currDevice = PreferenceManager.getDefaultSharedPreferences(this).getString("CURR_DEVICE", "defaultStringIfNothingFound");
        //Toast.makeText(this, currDevice, Toast.LENGTH_LONG).show();
        initUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_add_team) {
            return true;
        }
        else if(id == R.id.menu_add_member){
            AddMemberForm addMemberForm = new AddMemberForm();
            addMemberForm.show(getSupportFragmentManager(), "addMemberForm");
        }

        return super.onOptionsItemSelected(item);
    }


    public void initUI()
    {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        teamNavigator = (Spinner)findViewById(R.id.team_navigator);
        initTeamSelectors();


    }
    // Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.

    public void initTeamSelectors(){
        final ArrayList<Team> teams  = OMDContract.getDBConnection(MainActivity.this).getTeamList();
        ArrayList<String> teamNames = new ArrayList<String>();
        for(int i = 0 ; i < teams.size() ;i++){
            teamNames.add(teams.get(i).getTeamName());
        }
        ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item, teamNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamNavigator.setAdapter(adapter);

        ArrayList<Member> members = OMDContract.getDBConnection(MainActivity.this).getMembers(teams.get(0).getUrl());

        mAdapter = new DemoCollectionPagerAdapter(getSupportFragmentManager(),teamNames.get(0),members);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mPager);

        teamNavigator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<Member> members = OMDContract.getDBConnection(MainActivity.this).getMembers(teams.get(position).getUrl());
                mAdapter.updateTeamMembers(teams.get(position).getTeamName(), members);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
        String teamName;
        ArrayList<Member> members;

        public ArrayList<Member> getMembers() {
            return members;
        }

        public void setMembers(ArrayList<Member> members) {
            this.members = members;
        }

        public String getTeams() {
            return teamName;
        }

        public DemoCollectionPagerAdapter(FragmentManager fm,String teamName,ArrayList<Member> members) {
            super(fm);
            this.members = members;
            this.teamName = teamName;
        }
        public void updateTeamMembers(String teamName,ArrayList<Member> members){
            this.teamName = teamName;
            this.members = members;
            mAdapter.notifyDataSetChanged();

        }
        @Override
        public int getItemPosition(Object item) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int i) {
            if(i == 0) {
                return MemberListFragment.newInstance(members);
            }
            else{
                return new DeviceFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position==0){
                return "Members";
            }
            else if(position == 1){
                return "This Device";
            }
            else
                return null;
        }
    }

    public static class MemberListFragment extends ListFragment {


        ArrayList<Member> members;
        Context context;
        FloatingActionButton ownFab;
        private Member selectedMember;

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public ArrayList<Member> getMembers() {
            return members;
        }

        public void setMembers(ArrayList<Member> members) {
            this.members = members;
        }
        public static MemberListFragment newInstance(ArrayList<Member> members){
            Bundle bundle = new Bundle();
            bundle.putSerializable("Members", members);
            MemberListFragment memberListFragment = new MemberListFragment();
            memberListFragment.setArguments(bundle);
            return memberListFragment;
        }



        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Bundle args = getArguments();
            if(args!= null){
                this.members = (ArrayList<Member>)args.getSerializable("Members");
            }
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_pager_list, container, false);

            String[] memberNames = new String[members.size()];
            for(int i = 0 ; i < members.size();i++){
                memberNames[i] = members.get(i).getMemberName();
            }
            ArrayAdapter<String> membersAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_single_choice, memberNames);
            setListAdapter(membersAdapter);
            ownFab =(FloatingActionButton)v.findViewById(R.id.own_fab);
            ownFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectedMember !=null){
                        AlertDialog.Builder abd = new AlertDialog.Builder(getActivity());
                        abd.setTitle("Are You Sure?");
                        abd.setMessage("This will set " + selectedMember.getMemberName() + " as the new Current Owner of the device");
                        abd.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        abd.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String currDeviceString = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("CURR_DEVICE", "defaultStringIfNothingFound");
                                Gson gson = new Gson();
                                Device currDevice = gson.fromJson(currDeviceString, Device.class);
                                if (currDevice != null) {
                                    DeviceApi deviceApi = (DeviceApi) NetworkController.getInstance().getApiOfType(ApiFactory.DEVICE_API);
                                    currDevice.setDeviceCurrOwnerID(selectedMember.getUrl());
                                    deviceApi.updateDevice(currDevice.getId(), currDevice, new Callback<Device>() {
                                        @Override
                                        public void success(Device device, Response response) {
                                            Gson gson = new Gson();
                                            String currentDeviceString = gson.toJson(device);
                                            PreferenceManager.getDefaultSharedPreferences(getActivity())
                                                    .edit().putString("CURR_DEVICE", currentDeviceString).commit();

                                            Toast.makeText(getActivity(), "Device CurrentOwner changed Successfully", Toast.LENGTH_SHORT).show();


                                        }

                                        @Override
                                        public void failure(RetrofitError error) {
                                            Toast.makeText(getActivity(), "Communication to server failed, Check Network", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                } else {
                                    Toast.makeText(getActivity(), "Select a member", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                        AlertDialog alertDialog = abd.create();
                        alertDialog.show();

                    }
                    else{
                        Toast.makeText(getActivity(),"Select a member",Toast.LENGTH_SHORT).show();

                    }
                }
            });
            return v;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id   ) {
            selectedMember = members.get(position);
        }
    }

    public static class DeviceFragment extends Fragment{
        View mainLayout;
        TextView deviceName;
        TextView deviceOS;
        TextView deviceModel;
        TextView deviceOwner;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            mainLayout = inflater.inflate(R.layout.device_layout, container, false);
            deviceName = (TextView)mainLayout.findViewById(R.id.device_name);
            deviceOS = (TextView)mainLayout.findViewById(R.id.device_os);
            deviceModel = (TextView)mainLayout.findViewById(R.id.device_model);
            deviceOwner = (TextView)mainLayout.findViewById(R.id.device_owner);
            setDeviceDetails();
            return mainLayout;
        }
        @Override
        public void onResume(){
            super.onResume();
            Log.wtf("Device Frag","inside OnResume");
        }


        public void setDeviceDetails(){
            deviceName.setText(getPhoneName());
            deviceModel.setText(Build.MODEL);
            deviceOS.setText(Build.VERSION.RELEASE +" " + String.valueOf( Build.VERSION.SDK_INT));
        }
        public String getPhoneName() {
            BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
            return myDevice.getName();

        }
    }


}
