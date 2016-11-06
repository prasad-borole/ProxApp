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

public class LoginFragment extends Fragment {
    private static final String TAG = LoginFragment.class.getSimpleName();

    EditText email;
    EditText password;
    View rootView;
    LoginService loginService;
    AsyncHttpClient client;


    public LoginFragment() {
        this.client = new AsyncHttpClient();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        loginService = new LoginService();

        Button signupButton = (Button) rootView.findViewById(R.id.btn_signup);
        Button loginButton = (Button) rootView.findViewById(R.id.btn_login);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = (EditText) rootView.findViewById(R.id.input_email);
                password = (EditText) rootView.findViewById(R.id.input_password);
                String username = email.getText().toString().trim();
                String passwd = password.getText().toString().trim();
                /*loginService.registerUser(username, passwd);*/
                registerUser(username, passwd);

            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = (EditText) rootView.findViewById(R.id.input_email);
                password = (EditText) rootView.findViewById(R.id.input_password);
                String username = email.getText().toString().trim();
                String passwd = password.getText().toString().trim();
                /*loginService.loginUser(username, passwd);*/
                loginUser(username, passwd);

            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }

    private void registerUser(String email, String password) {
        String registerUrl = "/signup";
        String finalUrl = getAbsoluteUrl(registerUrl);
        Log.v(TAG, "Hitting url " + finalUrl);
        RequestParams requestParams = new RequestParams();
        requestParams.add("username", email);
        requestParams.add("password", password);

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

    private void loginUser(final String email, String password) {
        String loginUrl = "/login";
        String finalUrl = getAbsoluteUrl(loginUrl);
        Log.v(TAG, "Hitting url " + finalUrl);
        RequestParams requestParams = new RequestParams();
        requestParams.add("username", email);
        requestParams.add("password", password);

        client.post(finalUrl, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    JSONObject obj = new JSONObject(new String(responseBody));
                    if(obj.getInt("status") == 0) {

                        Log.v(TAG, "Login Successful... Status code=" + statusCode + " respnonse = " + new String(responseBody));

                        Intent intent = new Intent(getActivity(), UserActivity.class);
                        intent.putExtra("username", email);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    } else {
                        Snackbar.make(rootView, "Login Failed", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }
                } catch(JSONException e) {

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.v(TAG, "Request failed  = " + statusCode);
                Snackbar.make(rootView, "Login Failed", Snackbar.LENGTH_SHORT)
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