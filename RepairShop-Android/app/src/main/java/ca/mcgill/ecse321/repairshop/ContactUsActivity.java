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
import ca.mcgill.ecse321.repairshop.Utils.HttpUtils;
import cz.msebera.android.httpclient.Header;

import static ca.mcgill.ecse321.repairshop.Utils.HelperMethods.displayDateTime;

public class ContactUsActivity extends BaseActivity {

    /**
     * Initializes the page
     * @param savedInstanceState (Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        addAllBusinessInfo();
    }

    /**
     * Displays the business name, address, email and phone number
     */
    private void addAllBusinessInfo() {

        // Get business info
        HttpUtils.get("/api/business/info/", new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    TextView businessName = findViewById((R.id.business_name));
                    businessName.setText(response.getString("name"));

                    TextView address = findViewById((R.id.business_address));
                    String addressString = "Address: " + response.getString("address");
                    address.setText(addressString);

                    TextView email = findViewById((R.id.business_email));
                    String emailString = "Email: " + response.getString("email");
                    email.setText(emailString);

                    TextView phoneNumber = findViewById((R.id.business_phoneNumber));
                    String phoneString = "Phone Number: " + response.getString("phoneNumber");
                    phoneNumber.setText(phoneString);
                } catch(Exception e) {
                    setError(e.getMessage());
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    setError(errorResponse.get("message").toString());
                } catch(Exception e) {
                    setError(e.getMessage());
                }
            }
        });

        // Get holidays
        getHolidays();
    }

    /**
     * Displays all the holidays.
     * If there are no holidays, an error message will be displayed.
     */
    private void getHolidays() {
        HttpUtils.get("/api/business/holidays/", new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                try {

                    if (response.length() == 0) {
                        setError("There are currently no holidays");
                        return;
                    }

                    ArrayList<String> displayHolidays = new ArrayList<>();

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject currTimeSlot = response.getJSONObject(i);
                            String currStart = displayDateTime(currTimeSlot.getString("startDateTime"));
                            String currEnd = displayDateTime(currTimeSlot.getString("endDateTime"));
                            displayHolidays.add(currStart + " to " + currEnd);
                        } catch (JSONException e) {
                            setError(e.getMessage());
                            return;
                        }
                    }

                    ListView holidaysListView = findViewById(R.id.holidayList);
                    ArrayAdapter<String> holidaysArrayAdapter = new ArrayAdapter<>(ContactUsActivity.this, android.R.layout.simple_list_item_1, displayHolidays);

                    holidaysListView.setAdapter(holidaysArrayAdapter);

                } catch(Exception e) {
                    setError(e.getMessage());
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    setError(errorResponse.get("message").toString());
                } catch(Exception e) {
                    setError(e.getMessage());
                }
            }
        });
    }


    /**
     * Helper to display or hide an error message
     * @param errorMessage error message to display (String)
     *
     */
    private void setError(String errorMessage) {
        TextView error = findViewById(R.id.contact_us_error);
        error.setText(errorMessage);
        error.setVisibility(errorMessage.equals("") ? View.GONE : View.VISIBLE);
    }

}