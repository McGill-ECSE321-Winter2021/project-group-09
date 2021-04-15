package ca.mcgill.ecse321.repairshop;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;


public class Register extends BaseActivity {
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

        refreshErrorMessage();
        refreshSuccessMessage();
    }


    public void signUp(View v) throws JSONException, UnsupportedEncodingException {
        error = "";
        success = "";
        refreshErrorMessage();
        refreshSuccessMessage();

        TextView customerName = (TextView)findViewById(R.id.customerNameRegister);
        name = customerName.getText().toString();
        TextView customerAddress = (TextView)findViewById(R.id.customerAddressRegister);
        address = customerAddress.getText().toString();
        TextView customerPassword = (TextView)findViewById(R.id.customerPasswordRegister);
        password = customerPassword.getText().toString();
        TextView customerPhone = (TextView)findViewById(R.id.customerPhoneRegister);
        phoneNumber = customerPhone.getText().toString();
        TextView customerEmail = (TextView)findViewById(R.id.customerEmailRegister);
        email = customerEmail.getText().toString();


        JSONObject body = new JSONObject();
        body.put("name", name);
        body.put("email", email);
        body.put("password", password);
        body.put("phoneNumber", phoneNumber);
        body.put("address", address);

        Log.d("SignUp", "Here");

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

    public void signUpBack(View v){
        Intent intent = new Intent(Register.this, MainActivity.class);
        startActivity(intent);
    }


}
