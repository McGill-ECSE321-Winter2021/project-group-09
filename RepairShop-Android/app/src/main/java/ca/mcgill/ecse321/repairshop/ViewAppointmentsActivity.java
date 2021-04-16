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
import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;

public class ViewAppointmentsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointments);

        State state = (State) getApplicationContext();

        // Get all appointments of current customer by email (Calling ViewCustomerAppointments in CustomerController.java)
        HttpUtils.get(ViewAppointmentsActivity.this, "api/customer/" + State.email + "/appointments", State.token, new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                if (response.length() == 0) {
                    setError("There are no appointments");
                    TextView selectAppointment = findViewById(R.id.list_of_upcoming_appointments);
                    selectAppointment.setVisibility(View.GONE);
                    return;
                }
                TextView selectAppointment = findViewById(R.id.list_of_upcoming_appointments);
                selectAppointment.setVisibility(View.VISIBLE);
                ArrayList<String> displayAppointments = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject currAppointment = response.getJSONObject(i);
                        String currServiceName = currAppointment.getJSONObject("serviceDto").getString("name");
                        String currStart = currAppointment.getJSONObject("timeSlotDto").getString("startDateTime");
                        String currEnd = currAppointment.getJSONObject("timeSlotDto").getString("endDateTime");
                        displayAppointments.add(currServiceName + "\n" + displayDateTime(currStart, currEnd));
                    } catch (JSONException e) {
                        setError(e.getMessage());
                        return;
                    }
                }

                ListView appointmentsListView = findViewById(R.id.appointmentViewList);
                ArrayAdapter<String> appointmentArrayAdapter = new ArrayAdapter<>(ViewAppointmentsActivity.this, android.R.layout.simple_list_item_1, displayAppointments);
                appointmentsListView.setAdapter(appointmentArrayAdapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                setError(responseString);
            }
        });
    }

    /**
     * Sets the error message of View Appointments page to the input. Can hide or display the error message
     * @param errorMessage (String)
     */
    private void setError(String errorMessage) {
        TextView error = findViewById(R.id.viewError);
        error.setText(errorMessage);
        error.setVisibility(errorMessage.equals("") ? View.GONE : View.VISIBLE);
    }

    /**
     *  Converts two timestamps of format (2021-03-02T15:00:00.000+00:00) to format "2021-03-02 from 15:00 to 16:00"
     *
     * @param startDateTime format: 2021-03-02T15:00:00.000+00:00 (String)
     * @param endDateTime format: 2021-03-02T16:00:00.000+00:00 (String)
     * @return "start date" from "start time" to "end time" (String)
     */
    public static String displayDateTime(String startDateTime, String endDateTime) {
        return startDateTime.substring(0, 10) + " from " + startDateTime.substring(11, 16) + " to " + endDateTime.substring(11, 16);
    }
}
