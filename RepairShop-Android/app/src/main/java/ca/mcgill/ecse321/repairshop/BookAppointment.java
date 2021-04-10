package ca.mcgill.ecse321.repairshop;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class BookAppointment extends BaseActivity {

    String errorText = "";

    JSONArray services;
    JSONObject service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        // Get all services
        HttpUtils.get("api/service/all", new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                services = response;

                ArrayList<String> displayServices = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject currService = response.getJSONObject(i);
                        String currName = currService.getString("name");
                        String currDuration = displayDuration(currService.getInt("duration"));
                        double currPrice = Math.round(currService.getDouble("price") * 100.0) / 100.0;
                        displayServices.add(currName + ", for " + currDuration + " $(" + currPrice + ")");
                    } catch (JSONException e) {
                        errorText = e.getMessage();
                        refreshError();
                        return;
                    }
                }

                ListView servicesListView = findViewById(R.id.bookServiceList);
                ArrayAdapter<String> servicesArrayAdapter = new ArrayAdapter<>(BookAppointment.this, android.R.layout.simple_list_item_1, displayServices);

                servicesListView.setAdapter(servicesArrayAdapter);
                servicesListView.setOnItemClickListener((adapterView, view, i, l) -> {
                    try {
                        service = services.getJSONObject(i);
                    } catch (JSONException e) {
                        errorText = e.getMessage();
                        refreshError();
                    }
                    Log.v("service", service.toString());
                });
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    errorText = errorResponse.get("message").toString();
                } catch (JSONException e) {
                    errorText = e.getMessage();
                }
                refreshError();
            }

        });




    }

    // Helper to display or hide an error message
    private void refreshError() {
        TextView error = findViewById(R.id.bookError);
        error.setText(errorText);
        error.setVisibility(errorText.equals("") ? View.GONE : View.VISIBLE);
    }

    // Helper to display service duration in hours
    private String displayDuration(int duration) {
        if (duration == 2) return "1 hour";
        else return (duration / 2.0) + " hours";
    }

}
