package ca.mcgill.ecse321.repairshop;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.loopj.android.http.TextHttpResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import ca.mcgill.ecse321.repairshop.Utils.HttpUtils;
import cz.msebera.android.httpclient.Header;

public class LoginActivity extends BaseActivity {

    private String error = null;

    /**
     * Initializes the page
     * @param savedInstanceState (Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    /**
     * Logs user in when the login button is clicked.
     * @param v login button
     * @throws JSONException if not valid JSON object
     * @throws UnsupportedEncodingException if encoding is not supported
     */
    public void logIn(View v) throws JSONException, UnsupportedEncodingException {

        error = "";

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

                State.token = response;
                State.email = email.getText().toString();
                email.setText("");
                password.setText("");

                TextView successTextView = findViewById(R.id.success);
                successTextView.setVisibility(View.VISIBLE);
                updateMenuLogin(appMenu);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String resp, Throwable throwable) {
                System.out.println(throwable.getMessage());
                error += resp;
                refreshErrorMessage();
            }
        });
    }

    /**
     * Helper to display an error message
     */
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

}