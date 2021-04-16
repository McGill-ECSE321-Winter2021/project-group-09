package ca.mcgill.ecse321.repairshop;

import android.os.Bundle;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends BaseActivity {
    private String error = "";
    private String businessName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get the business name from backend
        getBusinessName();

    }

    private void getBusinessName(){

        HttpUtils.get("/api/business/info", new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    businessName = response.getString("name");
                    TextView tv = findViewById(R.id.welcomePageBusinessName);
                    tv.setText(businessName);
                } catch(Exception e) {
                    error += e.getMessage();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    error += errorResponse.get("message").toString();
                } catch(Exception e) {
                    error += e.getMessage();
                }

            }

        });

        refreshErrorMessage();

    }


    private void refreshErrorMessage() {
        TextView tv = findViewById(R.id.homepageError);
        tv.setText(error);
    }

}