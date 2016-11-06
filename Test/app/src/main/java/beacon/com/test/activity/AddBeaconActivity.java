package beacon.com.test.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import beacon.com.test.R;
import beacon.com.test.model.URLConstants;
import cz.msebera.android.httpclient.Header;

/**
 * Created by abhishek on 11/6/16.
 */
public class AddBeaconActivity extends AppCompatActivity {
    EditText BeaconID;
    EditText Title;
    EditText Desc;
    EditText Geturl;
    static AsyncHttpClient client = new AsyncHttpClient();


    private static final String TAG = AddBeaconActivity.class.getSimpleName();
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        username = (String) bundle.get("username");
        Log.v(TAG, "Username in session = " + username);
        setContentView(R.layout.add_beacon);

        Button addBeacon = (Button) findViewById(R.id.AddBeaconButton);

        addBeacon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BeaconID = (EditText) findViewById(R.id.editId);
                Title = (EditText) findViewById(R.id.editTitle);
                Desc = (EditText) findViewById(R.id.editDesc);
                Geturl =  (EditText) findViewById(R.id.editUrl);
                String beaconid = BeaconID.getText().toString().trim();
                String title = Title.getText().toString().trim();
                String desc =  Desc.getText().toString().trim();
                String urlString = Geturl.getText().toString().trim();
                registerBeacon(beaconid, title, desc, urlString, username);

            }
        });



        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
    }


    private void registerBeacon(String beaconid, String title, String desc, String urlString, String username) {
        String registerUrl = "/beacon";
        String finalUrl = getAbsoluteUrl(registerUrl);
        Log.v(TAG, "Hitting url " + finalUrl);
        RequestParams requestParams = new RequestParams();
        requestParams.add("email_id", username);
        requestParams.add("beacon_id", beaconid);
        requestParams.add("title", title);
        requestParams.add("description", desc);
        requestParams.add("ad_url", urlString);
        final View parentLayout = findViewById(android.R.id.content);
        client.post(finalUrl, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.v(TAG, "Registration successful... Status code=" + statusCode + " respnonse = " + responseBody.toString());
                Snackbar.make(parentLayout, "Registration Successful", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.v(TAG, "Request failed  = " + statusCode);
                Snackbar.make(parentLayout, "Registration failed", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return URLConstants.AWS_URL + relativeUrl;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
}
