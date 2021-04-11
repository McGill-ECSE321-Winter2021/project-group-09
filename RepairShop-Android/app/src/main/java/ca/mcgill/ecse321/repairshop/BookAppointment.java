package ca.mcgill.ecse321.repairshop;

import android.content.Intent;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class BookAppointment extends BaseActivity {

    String errorText = "";
    String targetDate = "";
    String token = "";
    String email = "";

    JSONObject service, timeSlot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        // update target date to today
        findViewById(R.id.today).setOnClickListener(view -> {
            targetDate = "";
            try {
                System.out.println("today clicked");
                toPart2();
            } catch (JSONException e) {
                errorText = e.getMessage();
                refreshError();
            }
        });

        // update target date
        findViewById(R.id.futureDateBtn).setOnClickListener(view -> {
            try {
                System.out.println("future btn");
                updateTargetDate();
                System.out.println("updated to: " + targetDate);
            } catch (JSONException e) {
                errorText = e.getMessage();
                refreshError();
            }
        });

        // Button to go back home
        findViewById(R.id.backHome).setOnClickListener((view) -> startActivity(new Intent(BookAppointment.this, MainActivity.class)));

        State state = (State) getApplicationContext();
        token = state.token;
        email = state.email;

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
        HttpUtils.get(this, "api/appointment/possibilities", token, requestParams, new JsonHttpResponseHandler() {

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

                        // Book appointment and confirm with customer
                        toPart3();

                    } catch (JSONException | UnsupportedEncodingException e) {
                        errorText = e.getMessage();
                        refreshError();
                    }
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

    // Set up the booking confirmation - book appointment + confirm
    private void toPart3() throws JSONException, UnsupportedEncodingException {

        JSONObject body = new JSONObject();
        body.put("startTime", timeSlot.getString("startDateTime"));
        body.put("serviceName", service.getString("name"));
        body.put("customerEmail", email);

        // Get possible appointment times
        HttpUtils.post(this, "api/appointment/create", token, body, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                findViewById(R.id.book2).setVisibility(View.GONE);
                findViewById(R.id.book3).setVisibility(View.VISIBLE);

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

    // Helper to convert a timestamp format (2021-03-02T15:00:00.000+00:00) to format 2021-03-02 15:00
    private String displayDateTime(String dateTime) {
        return dateTime.substring(0,10) + " at " + dateTime.substring(12, 16);
    }

    // Helper to update the list of time slots
    private void updateTargetDate() throws JSONException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        TextView dateInput = findViewById(R.id.futureDate);

        try {
            Date targetDateDate = simpleDateFormat.parse(dateInput.getText().toString());
            if (targetDateDate != null && targetDateDate.before(new Date())) {
                errorText = "Please enter a future date";
                refreshError();
            } else {
                targetDate = dateInput.getText().toString();
                errorText = "";
                refreshError();
                toPart2();
            }
        } catch (ParseException e) {
            errorText = "Please select a valid date";
            refreshError();
        }
    }

}
