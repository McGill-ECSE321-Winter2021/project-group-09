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

    JSONObject service, timeSlot;

    String targetDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        // Get all services
        HttpUtils.get("api/service/all", new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                if (response.length() == 0) {
                    errorText = "There are currently no services";
                    refreshError();
                    return;
                }

                ArrayList<String> displayServices = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject currService = response.getJSONObject(i);
                        String currName = currService.getString("name");
                        String currDuration = displayDuration(currService.getInt("duration"));
                        double currPrice = Math.round(currService.getDouble("price") * 100.0) / 100.0;
                        displayServices.add(currName + ", for " + currDuration + " ($" + currPrice + ")");
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
                        service = response.getJSONObject(i);

                        // Show all available booking times
                        toPart2();

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

    // Set up the next part for booking - display all available appointment times
    private void toPart2() throws JSONException {

        RequestParams requestParams = new RequestParams();
        requestParams.add("startDate", targetDate);
        requestParams.add("serviceName", service.getString("name"));

        // Get possible appointment times
        HttpUtils.get("api/appointment/possibilities", requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                findViewById(R.id.book1).setVisibility(View.GONE);
                findViewById(R.id.book2).setVisibility(View.VISIBLE);

                if (response.length() == 0) {
                    errorText = "There are currently no available time slots";
                    refreshError();
                    return;
                }

                ArrayList<String> displayPossibilities = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject currTimeSlot = response.getJSONObject(i);
                        String currStart = displayDateTime(currTimeSlot.getString("startDateTime"));
                        String currEnd = displayDateTime(currTimeSlot.getString("endDateTime"));
                        displayPossibilities.add(currStart + " to " + currEnd);
                    } catch (JSONException e) {
                        errorText = e.getMessage();
                        refreshError();
                        return;
                    }
                }

                ListView timeSlotsListView = findViewById(R.id.possibleTimeSlotsList);
                ArrayAdapter<String> timeSlotsArrayAdapter = new ArrayAdapter<>(BookAppointment.this, android.R.layout.simple_list_item_1, displayPossibilities);

                timeSlotsListView.setAdapter(timeSlotsArrayAdapter);
                timeSlotsListView.setOnItemClickListener((adapterView, view, i, l) -> {
                    try {
                        timeSlot = response.getJSONObject(i);

                        // Show all available booking times
//                        toPart2();

                    } catch (JSONException e) {
                        errorText = e.getMessage();
                        refreshError();
                    }
                    Log.v("time slot", timeSlot.toString());
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

    // Helper to convert a timestamp format (2021-03-02T15:00:00.000+00:00) to format Tue Mar 02, 2021 at HH:mm (in local timezone)
    private String displayDateTime(String dateTime) {
        Log.v("date time", dateTime);
        return dateTime;
    }

}
