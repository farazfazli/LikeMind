package pw.likemind.likemind.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import net.smartam.leeloo.client.OAuthClient;
import net.smartam.leeloo.client.URLConnectionClient;
import net.smartam.leeloo.client.request.OAuthClientRequest;
import net.smartam.leeloo.client.response.OAuthAccessTokenResponse;
import net.smartam.leeloo.common.exception.OAuthSystemException;
import net.smartam.leeloo.common.message.types.GrantType;

import pw.likemind.likemind.Constants;

// leeloo oAuth lib https://bitbucket.org/smartproject/oauth-2.0/wiki/Home

public class MeetupAuthActivity extends Activity {
    public static final String TOKEN = "token";
    private final String TAG = getClass().getName();

    // Meetup OAuth Endpoints


    public static final String CONSUMER_SECRET = "5bmfffhhlttuf6labngjh2a82h";





    private WebView _webview;
    private Intent _intent;
    private Context _context;

    public void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        _intent = getIntent();
        _context = getApplicationContext();

        _webview = new WebView(this);
        _webview.setWebViewClient(new MyWebViewClient());
        setContentView(_webview);

        _webview.getSettings().setJavaScriptEnabled(true);
        OAuthClientRequest request = null;
        try {
            request = OAuthClientRequest.authorizationLocation(
                    Constants.AUTH_URL).setClientId(
                    Constants.CONSUMER_KEY).setRedirectURI(
                    Constants.REDIRECT_URI).buildQueryMessage();
        } catch (OAuthSystemException e) {
            Log.d(TAG, "OAuth request failed", e);
        }
        _webview.loadUrl(request.getLocationUri() + "&response_type=code&set_mobile=on");
    }

    public void finishActivity() {
        //do something here before finishing if needed
        finish();
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri uri = Uri.parse(url);

            String code = uri.getQueryParameter("code");
            String error = uri.getQueryParameter("error");

            if (code != null) {
                new MeetupRetrieveAccessTokenTask().execute(uri);
                setResult(RESULT_OK, _intent);
                finishActivity();

            } else if (error != null) {
                setResult(RESULT_CANCELED, _intent);
                finishActivity();
            }
            return false;
        }
    }

    private class MeetupRetrieveAccessTokenTask extends AsyncTask<Uri, Void, Void> {

        @Override
        protected Void doInBackground(Uri... params) {

            Uri uri = params[0];
            String code = uri.getQueryParameter("code");

            OAuthClientRequest request = null;

            try {
                request = OAuthClientRequest.tokenLocation(Constants.TOKEN_URL)
                        .setGrantType(GrantType.AUTHORIZATION_CODE).setClientId(
                                Constants.CONSUMER_KEY).setClientSecret(
                                CONSUMER_SECRET).setRedirectURI(
                                Constants.REDIRECT_URI).setCode(code).setScope(Constants.TWO_WEEKS_EXPIRY)
                        .buildBodyMessage();

                OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

                OAuthAccessTokenResponse response = oAuthClient.accessToken(request);

                SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_FILE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(TOKEN, response.getAccessToken());
                editor.apply();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED, _intent);
        finishActivity();
    }
}