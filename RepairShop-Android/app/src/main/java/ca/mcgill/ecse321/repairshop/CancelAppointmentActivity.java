package ca.mcgill.ecse321.repairshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import cz.msebera.android.httpclient.Header;

public class CancelAppointmentActivity extends BaseActivity {

    JSONObject appointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_appointment);

        State state = (State) getApplicationContext();

        // Get all appointments of current customer by email (Calling ViewCustomerAppointments in CustomerController.java)
        HttpUtils.get(CancelAppointmentActivity.this, "api/customer/" + State.email + "/appointments", State.token, new RequestParams(), new JsonHttpResponseHandler() {

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
                        String currServiceName = currAppointment.getJSONObject("serviceDto").getString("name");
                        String currStart = currAppointment.getJSONObject("timeSlotDto").getString("startDateTime");
                        String currEnd = currAppointment.getJSONObject("timeSlotDto").getString("endDateTime");
                        displayAppointments.add(currServiceName + "\n" + ViewAppointmentsActivity.displayDateTime(currStart, currEnd));
                    } catch (JSONException e) {
                        setError(e.getMessage());
                        return;
                    }
                }

                ListView appointmentsListView = findViewById(R.id.appointmentList);
                ArrayAdapter<String> appointmentArrayAdapter = new ArrayAdapter<>(CancelAppointmentActivity.this, android.R.layout.simple_list_item_1, displayAppointments);

                appointmentsListView.setAdapter(appointmentArrayAdapter);

                appointmentsListView.setOnItemClickListener((adapterView, view, i, l) -> {
                    try {
                        appointment = response.getJSONObject(i);
                        if (isWeekAhead(appointment.getJSONObject("timeSlotDto").getString("startDateTime"))) {
                            setError("Can only cancel 1 week in advance.");
                        } else {

                            //cancel appointment
                            HttpUtils.delete(CancelAppointmentActivity.this, "api/appointment/cancel/" + appointment.getLong("appointmentID"), State.token, new TextHttpResponseHandler() {


                                @Override
                                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                    setError("");
                                    //Hide 1st page
                                    findViewById(R.id.cancelAppointment).setVisibility(View.GONE);
                                    //Show cancellation page 2
                                    findViewById(R.id.cancelPage2).setVisibility(View.VISIBLE);
                                    // Button to go to "View Appointments"
                                    findViewById(R.id.viewAppointmentsButton).setOnClickListener((view) -> startActivity(new Intent(CancelAppointmentActivity.this, ViewAppointmentsActivity.class)));
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                    setError(responseString);
                                }

                            });

                        }

                    } catch (JSONException e) {
                        setError(e.getMessage());
                    }

                });
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                setError(responseString);
            }
        });
    }



    /**
     * Checks if the cancellation date is at least one week ahead
     * @param apptDateTime appointment's start date and time in form "2021-10-11T13:00:00.000+00:00" (String)
     * @return true or false if today is a week ahead of the appointment's date (boolean)
     */
    private boolean isWeekAhead(String apptDateTime) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,7);
        Date todayPlusSeven = new Date(cal.getTimeInMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String  todayPlusSevenStr = formatter.format(todayPlusSeven);
        return todayPlusSevenStr.compareTo(apptDateTime)>=0;
    }

    /**
     * Sets the error message of View Appointments page to the input. Can hide or display the error message
     * @param errorMessage (String)
     */
    private void setError(String errorMessage) {
        TextView error = findViewById(R.id.cancelError);
        error.setText(errorMessage);
        error.setVisibility(errorMessage.equals("") ? View.GONE : View.VISIBLE);
    }


}
