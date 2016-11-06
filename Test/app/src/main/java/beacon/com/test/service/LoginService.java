package beacon.com.test.service;

import android.util.Log;

import com.loopj.android.http.*;

import beacon.com.test.model.URLConstants;
import cz.msebera.android.httpclient.Header;

public class LoginService {

    private static final String TAG = LoginService.class.getSimpleName();

    AsyncHttpClient client;

    public LoginService() {
        this.client = new AsyncHttpClient();
    }

    public void registerUser(String email, String password) {
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
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.v(TAG, "Request failed  = " + statusCode);
            }
        });

    }

    public boolean loginUser(String email, String password) {
        String loginUrl = "/login";
        String finalUrl = getAbsoluteUrl(loginUrl);
        Log.v(TAG, "Hitting url " + finalUrl);
        RequestParams requestParams = new RequestParams();
        requestParams.add("username", email);
        requestParams.add("password", password);

        client.post(finalUrl, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.v(TAG, "Login Successful... Status code=" + statusCode + " respnonse = " + responseBody.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.v(TAG, "Request failed  = " + statusCode);
            }
        });
        return false;
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return URLConstants.AWS_URL + relativeUrl;
    }
}
