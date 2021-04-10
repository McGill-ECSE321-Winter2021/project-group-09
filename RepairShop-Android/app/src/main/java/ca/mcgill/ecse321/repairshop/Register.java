package ca.mcgill.ecse321.repairshop;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class Register extends AppCompatActivity {
    private Button registerButton;
    private String error = null;
    private String success = null;
    private String name = null;
    private String email = null;
    private String phoneNumber = null;
    private String address = null;
    private String password = null;

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

    private void refreshSuccessMessage() {
        // set the error message
        TextView tvError = (TextView) findViewById(R.id.signUpSuccess);
        tvError.setText(success);
        if (success == null || success.length() == 0) {
            tvError.setVisibility(View.GONE);
        } else {
            tvError.setVisibility(View.VISIBLE);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        //set the onClick methods for the Sign Up button
        registerButton = findViewById(R.id.signUpButton);
        registerButton.setOnClickListener(this::signUp);

        refreshErrorMessage();
    }


    public void signUp(View v) {
        error = "";


        TextView customerName = (TextView)findViewById(R.id.customerNameRegister);
        name = String.valueOf(customerName.getText());
        TextView customerAddress = (TextView)findViewById(R.id.customerAddressRegister);
        address = String.valueOf(customerAddress.getText());
        TextView customerPassword = (TextView)findViewById(R.id.customerPasswordRegister);
        password = String.valueOf(customerPassword.getText());
        TextView customerPhone = (TextView)findViewById(R.id.customerPhoneRegister);
        phoneNumber = String.valueOf(customerPhone.getText());
        TextView customerEmail = (TextView)findViewById(R.id.customerEmailRegister);
        email = String.valueOf(customerEmail.getText());


        RequestParams params = new RequestParams();
        params.add("name", name);
        params.add("email", email);
        params.add("password", password);
        params.add("phoneNumber", phoneNumber);
        params.add("address", address);

        Log.d("SignUp", "Here");

        HttpUtils.post("/api/customer/register", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("Success", "Here");
                success = "Account Created";
                refreshSuccessMessage();
                customerName.setText("");
                customerEmail.setText("");
                customerPhone.setText("");
                customerAddress.setText("");
                customerPassword.setText("");
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                try {
                    Log.d("Error Message", "message");
                    error = errorResponse.get("message").toString();
                    Log.d("Error ", error);
                } catch (JSONException e) {
                    Log.d("Error catch", "message");
                    error = e.getMessage();

                }
                refreshErrorMessage();
            }
        });


    }

}
