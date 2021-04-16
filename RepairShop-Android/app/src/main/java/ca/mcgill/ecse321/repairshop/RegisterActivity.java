package ca.mcgill.ecse321.repairshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.loopj.android.http.TextHttpResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import ca.mcgill.ecse321.repairshop.Utils.HttpUtils;
import cz.msebera.android.httpclient.Header;


public class RegisterActivity extends BaseActivity {

    private String error = null;
    private String success = null;

    /**
     * Initializes the page
     * @param savedInstanceState (Bundle)
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_register);

        refreshErrorMessage();
        refreshSuccessMessage();
    }


    /**
     * Method that sends POST request to the backend when the "Sign Up" button is clicked
     * @param v Sign up button
     * @throws JSONException if not valid JSON object
     * @throws UnsupportedEncodingException if encoding is not supported
     */
    public void signUp(View v) throws JSONException, UnsupportedEncodingException {
        error = "";
        success = "";
        refreshErrorMessage();
        refreshSuccessMessage();

        TextView customerName = (TextView)findViewById(R.id.customerNameRegister);
        String name = customerName.getText().toString();
        TextView customerAddress = (TextView)findViewById(R.id.customerAddressRegister);
        String address = customerAddress.getText().toString();
        TextView customerPassword = (TextView)findViewById(R.id.customerPasswordRegister);
        String password = customerPassword.getText().toString();
        TextView customerPhone = (TextView)findViewById(R.id.customerPhoneRegister);
        String phoneNumber = customerPhone.getText().toString();
        TextView customerEmail = (TextView)findViewById(R.id.customerEmailRegister);
        String email = customerEmail.getText().toString();


        JSONObject body = new JSONObject();
        body.put("name", name);
        body.put("email", email);
        body.put("password", password);
        body.put("phoneNumber", phoneNumber);
        body.put("address", address);

        HttpUtils.postWithBody(getApplicationContext(),"api/customer/register", body, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                success = "Account Created";
                refreshSuccessMessage();
                customerName.setText("");
                customerEmail.setText("");
                customerPhone.setText("");
                customerAddress.setText("");
                customerPassword.setText("");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable throwable) {

                error = errorResponse;
                refreshErrorMessage();
            }

        });

    }

    /**
     * Takes the user back to the home page when "Back" button is clicked
     * @param v Back button
     */
    public void signUpBack(View v){
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Updates error message
     */
    private void refreshErrorMessage() {
        // set the error message
        TextView tvError = (TextView) findViewById(R.id.signUpError);
        tvError.setText(error);
        if (error == null || error.length() == 0) {
            tvError.setVisibility(View.GONE);
        } else {
            tvError.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Updates success message
     */
    private void refreshSuccessMessage() {
        // set the success message
        TextView tvError = (TextView) findViewById(R.id.signUpSuccess);
        tvError.setText(success);
        if (success == null || success.length() == 0) {
            tvError.setVisibility(View.GONE);
        } else {
            tvError.setVisibility(View.VISIBLE);
        }
    }

}
