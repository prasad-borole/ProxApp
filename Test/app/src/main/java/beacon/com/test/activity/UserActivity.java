package beacon.com.test.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import beacon.com.test.R;
import beacon.com.test.adapter.BeaconAdapter;
import beacon.com.test.model.BeaconItem;
import beacon.com.test.model.URLConstants;
import cz.msebera.android.httpclient.Header;

/**
 * Created by abhishek on 11/6/16.
 */
public class UserActivity extends AppCompatActivity {

    private static final String TAG = UserActivity.class.getSimpleName();
    String username;
    RecyclerView recyclerView;
    BeaconAdapter beaconAdapter;
    static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    static AsyncHttpClient syncHttpClient = new SyncHttpClient();

    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        username = (String) bundle.get("username");
        Log.v(TAG, "Username in session = " + username);
        setContentView(R.layout.fragment_beacon);
        recyclerView = (RecyclerView) findViewById(R.id.beaconList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        beaconAdapter = new BeaconAdapter(getApplicationContext());
        recyclerView.setAdapter(beaconAdapter);
        FloatingActionButton scanbutton = (FloatingActionButton) findViewById(R.id.scanBeacons);
        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.addBeacon);
        scanbutton.setVisibility(View.GONE);
        addButton.setVisibility(View.VISIBLE);
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddBeaconActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);

            }
        });

        populateBeaconList(username);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void populateBeaconList(String username) {

        String registerUrl = "/getBeacons";
        String finalUrl = getAbsoluteUrl(registerUrl);
        Log.v(TAG, "Hitting url " + finalUrl);
        RequestParams requestParams = new RequestParams();
        requestParams.add("email_id", username);

        getClient().post(finalUrl, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String res = new String(responseBody);
                Log.v(TAG, "Got beacons=" + statusCode + " respnonse = " + res );
                try {
                    JSONArray objArray = new JSONArray(res);
                    for (int i = 0; i < objArray.length(); i++) {
                        JSONObject obj = objArray.getJSONObject(i);
                        Log.v(TAG, obj.getString("description"));
                        final BeaconItem newItem = new BeaconItem(obj.getString("beacon_id"), 1 , obj.getString("title"), obj.getString("description"),
                                obj.getString("ad_url"));
                        //list.add(newItem);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                beaconAdapter.add(newItem);
                            }
                        });
                        Log.v(TAG, "Added");
                    }
                    //Log.v(TAG,  Integer.toString(obj.length()));
                } catch (JSONException e) {
                    Log.v(TAG, "EXCEPTION");
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.v(TAG, "Request failed  = " + statusCode);
            }
        });


    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return URLConstants.AWS_URL + relativeUrl;
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

    private static AsyncHttpClient getClient() {
        // Return the synchronous HTTP client when the thread is not prepared
        if (Looper.myLooper() == null)
            return syncHttpClient;
        return asyncHttpClient;
    }

}
