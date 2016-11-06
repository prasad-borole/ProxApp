package beacon.com.test.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import beacon.com.test.R;
import beacon.com.test.activity.UserActivity;
import beacon.com.test.service.LoginService;

import com.loopj.android.http.*;

import org.json.JSONException;
import org.json.JSONObject;

import beacon.com.test.model.URLConstants;
import cz.msebera.android.httpclient.Header;


public class BeaconAddFragment extends Fragment {
    private static final String TAG = LoginFragment.class.getSimpleName();

    EditText BeaconID;
    EditText Title;
    EditText Desc;
    EditText Geturl;
    View rootView;
    AsyncHttpClient client;


    public BeaconAddFragment() {
        this.client = new AsyncHttpClient();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.add_beacon, container, false);

        Button addBeacon = (Button) rootView.findViewById(R.id.AddBeaconButton);
        Button cancel = (Button) rootView.findViewById(R.id.CancelAdd);

        addBeacon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BeaconID = (EditText) rootView.findViewById(R.id.editId);
                Title = (EditText) rootView.findViewById(R.id.editTitle);
                Desc = (EditText) rootView.findViewById(R.id.editDesc);
                Geturl =  (EditText) rootView.findViewById(R.id.editUrl);
                String beaconid = BeaconID.getText().toString().trim();
                String title = Title.getText().toString().trim();
                String desc =  Desc.getText().toString().trim();
                String urlString = Geturl.getText().toString().trim();
                registerBeacon(beaconid, title, desc, urlString);

            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }

    private void registerBeacon(String beaconid, String title, String desc, String urlString) {
        String registerUrl = "/beacon";
        String finalUrl = getAbsoluteUrl(registerUrl);
        Log.v(TAG, "Hitting url " + finalUrl);
        RequestParams requestParams = new RequestParams();
        requestParams.add("beacon_id", beaconid);
        requestParams.add("title", title);
        requestParams.add("description", desc);
        requestParams.add("ad_url", urlString);

        client.post(finalUrl, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.v(TAG, "Registration successful... Status code=" + statusCode + " respnonse = " + responseBody.toString());
                Snackbar.make(rootView, "Registration Successful", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.v(TAG, "Request failed  = " + statusCode);
                Snackbar.make(rootView, "Registration failed", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return URLConstants.AWS_URL + relativeUrl;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }
}
