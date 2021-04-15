package ca.mcgill.ecse321.repairshop;

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

import java.sql.SQLOutput;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ViewAppointments extends BaseActivity {

    String token = "";
    String email = "";
    JSONObject appointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointments);

        State state = (State) getApplicationContext();
        token = state.token;
        email = state.email;

        // Get all appointments of current customer by email (Calling ViewCustomerAppointments in CustomerController.java
        HttpUtils.get(ViewAppointments.this, "api/customer/" + email + "/appointments", token, new RequestParams(), new JsonHttpResponseHandler() {
            // get(Context context, String url, String token, RequestParams params, AsyncHttpResponseHandler responseHandler) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                if (response.length() == 0) {
                    setError("There are no appointments");
                    TextView selectAppointment = findViewById(R.id.cancelSelectAnAppointment);
                    selectAppointment.setVisibility(View.GONE);
                    return;
                }
                TextView selectAppointment = findViewById(R.id.cancelSelectAnAppointment);
                selectAppointment.setVisibility(View.VISIBLE);
                ArrayList<String> displayAppointments = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject currAppointment = response.getJSONObject(i);
                        Long currApptID = currAppointment.getLong("appointmentID");
                        String currServiceName = currAppointment.getJSONObject("serviceDto").getString("name");
                        String currStart = currAppointment.getJSONObject("timeSlotDto").getString("startDateTime");
                        String currEnd = currAppointment.getJSONObject("timeSlotDto").getString("endDateTime");
                        displayAppointments.add(currServiceName + "\n" + displayDateTime(currStart, currEnd));
                    } catch (JSONException e) {
                        setError(e.getMessage());
                        return;
                    }
                }

                ListView appointmentsListView = findViewById(R.id.appointmentList);
                ArrayAdapter<String> appointmentArrayAdapter = new ArrayAdapter<>(ViewAppointments.this, android.R.layout.simple_list_item_1, displayAppointments);

                appointmentsListView.setAdapter(appointmentArrayAdapter);

                appointmentsListView.setOnItemClickListener((adapterView, view, i, l) -> {
                    try {
                        appointment = response.getJSONObject(i);

                        System.out.println("**************************************************************"); //TODO: remove this later
                        System.out.println("APPOINTMENT ID: " + appointment.getLong("appointmentID"));  //TODO: remove this later
                        System.out.println("**************************************************************"); //TODO: remove this later

                        //cancel appointment
                        HttpUtils.delete(ViewAppointments.this, "api/appointment/cancel/customer/" + appointment.getLong("appointmentID"), token, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                                if (response.length() == 0) {
                                    setError("Can only cancel 1 week in advance.");
                                    setSuccess("");
                                    return;
                                } else {
                                    setSuccess("The appointment has been cancelled successfully. A cancellation email will be sent shortly.");
                                    setError("");
                                }
                            }

        /*                    @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                                try {
                                    setError(errorResponse.get("message").toString());
                                } catch (JSONException e) {
                                    setError(e.getMessage());
                                }
                            }*/

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                               // super.onFailure(statusCode, headers, responseString, throwable);
                                setError(responseString);
                                System.out.println("****************************************************");
                                System.out.println("RESPONSE STRING: "+responseString);
                                System.out.println("THROWABLE: "+throwable);
                                System.out.println("****************************************************");

                            }

                        });

                    } catch (JSONException e) {
                        setError(e.getMessage());
                    }
                });
            }

/*            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    setError(errorResponse.get("message").toString());
                } catch (JSONException e) {
                    setError(e.getMessage());
                }
            }*/

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                setError(responseString);
                System.out.println("****************************************************");
                System.out.println("SECOND ON FAILURE~~~~~~~~~~~");
                System.out.println("****************************************************");

            }
        });
    }

    // Helper to display or hide successful message
    private void setSuccess(String successMessage) {
        TextView error = findViewById(R.id.cancelSuccess);
        error.setText(successMessage);
        error.setVisibility(successMessage.equals("") ? View.GONE : View.VISIBLE);
    }


    // Helper to display or hide an error message
    private void setError(String errorMessage) {
        TextView error = findViewById(R.id.cancelError);
        error.setText(errorMessage);
        error.setVisibility(errorMessage.equals("") ? View.GONE : View.VISIBLE);
    }

    // Helper to convert two timestamps format (2021-03-02T15:00:00.000+00:00) to format "2021-03-02 from 15:00 to endtime
    private String displayDateTime(String startDateTime, String endDateTime) {
        return startDateTime.substring(0, 10) + " from " + startDateTime.substring(11, 16) + " to " + endDateTime.substring(11, 16);
    }
}
