package com.sap.mobi.test;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //new ReadJSONFeedTask().execute();
        ArrayList<Recommendation> recommendationList  = new ArrayList<Recommendation>();

        for(int i = 0 ; i < 10;i++){
            FirstPerson fp = new FirstPerson("Harshit","http//harsht.me");
            SecondPerson sp = new SecondPerson("Shubhangi","http//shubhiraj.me");
            Recommendation rec = new Recommendation(fp,sp,"20");
            recommendationList.add(rec);
        }
        Gson gson = new Gson();
        String res = gson.toJson(recommendationList);
        int len =res.length();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private HashMap<String,HashMap<String,String>> events = new HashMap<String,HashMap<String,String>>();

    public class ReadJSONFeedTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls){
            String jsonData = "{\"channel\":{\"item\":[{\"title\":{\"@text\":\"testTitle\"},\"link\":{\"@text\":\"testlink\"},\"description\":{\"@text\":\"testdescription\"},\"pubDate\":{\"@text\":\"testPubDate\"},\"http://purl.org/dc/elements/1.1/creator\":{\"@text\":\"testCreator\"},\"guid\":{\"@text\":\"testguid\"}}]}}";
            return jsonData;
        }
        protected void onPostExecute(String result){
            try{

                JSONObject object = new JSONObject(result);
                String channel = object.getString("channel");
                JSONObject object2 = new JSONObject(channel);
                JSONArray item = object2.getJSONArray("item");
                for(int i = 0; i < item.length();i++)
                {
                    HashMap<String,String> gegevens = new HashMap<String,String>();

                    JSONObject object3 = item.getJSONObject(i);

                    JSONObject objTitle = object3.getJSONObject("title");
                    String title = objTitle.getString("@text");

                    JSONObject objLink = object3.getJSONObject("link");
                    String link = objLink.getString("@text");

                    JSONObject objDescription = object3.getJSONObject("description");
                    String description = objDescription.getString("@text");

                    JSONObject objPubDate = object3.getJSONObject("pubDate");
                    String pubDate = objPubDate.getString("@text");

                    JSONObject objCreator = object3.getJSONObject("http://purl.org/dc/elements/1.1/creator");
                    String creator = objCreator.getString("@text");

                    JSONObject objGuid = object3.getJSONObject("guid");
                    String guid = objGuid.getString("@text");

                    gegevens.put("link",link);
                    gegevens.put("description",description);
                    gegevens.put("pubDate",pubDate);
                    gegevens.put("creator",creator);
                    gegevens.put("guid",guid);

                    events.put(title,gegevens);


                }



            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
