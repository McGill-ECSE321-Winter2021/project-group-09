package ca.mcgill.ecse321.repairshop;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ContactUs extends BaseActivity {

    private String error = "";
    private String businessName = "";
    private String businessAddress = "";
    private String businessEmail = "";
    private String businessPhoneNumber = "";
    private String holidayStartDateTime = "";
    private String holidayEndDateTime = "";
    ArrayList<String> holidays = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us);
        addAllBusinessInfo();

    }

    private void refreshErrorMessage() {
        TextView tv = findViewById(R.id.contact_us_error);
        tv.setText(error);
    }

    private void getBusinessName() {
        TextView tv = findViewById((R.id.business_name));
        tv.setText(businessName);
    }

    private void getBusinessAddress() {
        TextView tv = findViewById((R.id.business_address));
        tv.setText("Address: " + businessAddress);
    }

    private void getBusinessEmail() {
        TextView tv = findViewById((R.id.business_email));
        tv.setText("Email: " + businessEmail);
    }

    private void getBusinessPhoneNumber() {
        TextView tv = findViewById((R.id.business_phoneNumber));
        tv.setText("Phone Number: " + businessPhoneNumber);
    }

    private void getHolidays() {
        for (String holiday: this.holidays) {
            TextView newHoliday = new TextView(ContactUs.this);
            newHoliday.setText(holiday);
            newHoliday.setTextColor(getResources().getColor(R.color.black));
            // TODO debug this , not displaying holidays
        }
    }


    private void addAllBusinessInfo() {
        HttpUtils.get("/api/business/info/", new RequestParams(), new JsonHttpResponseHandler() {



            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                System.out.println("HERE!");

                    JSONObject thisBusinessInfo = null;
                    try {
                        thisBusinessInfo = response;
                        addBusinessInfo(thisBusinessInfo);

                        System.out.println("***************************************************************");
                        System.out.println("businessName" + thisBusinessInfo.getString("name"));
                        System.out.println("***************************************************************");

                    } catch(Exception e) {
                        error += e.getMessage();
                    }

                getBusinessName();
                getBusinessAddress();
                getBusinessEmail();
                getBusinessPhoneNumber();
                getHolidays();
                refreshErrorMessage();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    error += errorResponse.get("message").toString();
                } catch(Exception e) {
                    error += e.getMessage();
                }
                refreshErrorMessage();
            }
        });
    }


    private void addBusinessInfo(JSONObject businessInfo) {

        try {
            this.businessName = businessInfo.getString("name");
            this.businessAddress = businessInfo.getString("address");
            this.businessEmail = businessInfo.getString("email");
            this.businessPhoneNumber = businessInfo.getString("phoneNumber");
            // might need some try-catch here
            JSONArray holidays = businessInfo.getJSONArray("holidays");
            if (holidays != null) {
                for (int i = 0; i < holidays.length(); i++) {
                    String holidayStart = holidays.getJSONObject(i).getString("startDateTime");
                    String holidayEnd = holidays.getJSONObject(i).getString("endDateTime");
                    this.holidays.add(holidayStart + " to " + holidayEnd);
                }
            }

        } catch(Exception e) {
            error += e.getMessage();
            refreshErrorMessage();
            return;
        }

    }

}