package ca.mcgill.ecse321.repairshop;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends BaseActivity {

    private String error = null;
    private String success = null;

    private void refreshErrorMessage() {
        // set the error message
        TextView tvError = findViewById(R.id.error);
        tvError.setText(error);

        if (error == null || error.length() == 0) {
            tvError.setVisibility(View.GONE);
        } else {
            tvError.setVisibility(View.VISIBLE);
        }
    }

    private void refreshSuccessMessage() {
        // set the error message
        TextView tvSuccess = findViewById(R.id.success);
        tvSuccess.setText(success);

        if (success == null || success.length() == 0) {
            tvSuccess.setVisibility(View.GONE);
        } else {
            tvSuccess.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void logIn(View v) throws JSONException, UnsupportedEncodingException {

        error = "";
        success = "";

        final TextView email = findViewById(R.id.login_email);
        final TextView password = findViewById(R.id.login_password);

        JSONObject body = new JSONObject();
        body.put("email", email.getText().toString());
        body.put("password", password.getText().toString());
        body.put("userType", "Customer");

        HttpUtils.postWithBody(getApplicationContext(), "/api/authentication/login", body, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                refreshErrorMessage();
                email.setText("");
                password.setText("");
                State state = (State)getApplicationContext();
                state.token = response;
                System.out.println(state.token);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String resp, Throwable throwable) {
                System.out.println(throwable.getMessage());
                error += resp;
                refreshErrorMessage();
            }
        });
    }
}