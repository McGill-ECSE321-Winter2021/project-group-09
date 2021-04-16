package ca.mcgill.ecse321.repairshop;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;

public class ContactUs extends BaseActivity {

    private String error = "";
    private String businessName = "";
    private TableLayout contactUsTable;

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


    private void addAllBusinessInfo() {
        HttpUtils.get("/api/business/info/", new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                System.out.println("HERE!");
                int n = response.length();

                for(int i = 0; i < n; i++) {
                    JSONObject thisBusinessInfo = null;
                    try {
                        thisBusinessInfo = response.getJSONObject(i);
                        addBusinessInfo(thisBusinessInfo);
                        System.out.println("businessName" + thisBusinessInfo.getString("name"));
                    } catch(Exception e) {
                        error += e.getMessage();
                        System.out.println("error???");
                    }
                }
                getBusinessName();
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
//        String name;
//        String address;
//        String email;
//        String phoneNumber;
//        int numberOfRepairSpots;

        try {
            this.businessName = businessInfo.getString("name");
//            address = businessInfo.getString("address");
//            email = businessInfo.getString("email");
//            phoneNumber  = businessInfo.getString("phoneNumber");
//            numberOfRepairSpots  = businessInfo.getInt("numberOfRepairSpots");
        } catch(Exception e) {
            error += e.getMessage();
            refreshErrorMessage();
            return;
        }


//        TableRow row = initializeRow(ContactUs.this);
//        contactUsTable.addView(row);

//        LinearLayout rowVerticalLayout = new LinearLayout(ContactUs.this);
//        rowVerticalLayout.setOrientation(LinearLayout.VERTICAL);
//        row.addView(rowVerticalLayout);
//
//        LinearLayout subRow1 = new LinearLayout(ContactUs.this);
//        subRow1.setOrientation(LinearLayout.HORIZONTAL);
//        rowVerticalLayout.addView(subRow1);
//
//        LinearLayout subRow2 = new LinearLayout(ContactUs.this);
//        subRow2.setOrientation(LinearLayout.HORIZONTAL);
//        rowVerticalLayout.addView(subRow2);
//
//        LinearLayout subRow3 = new LinearLayout(ContactUs.this);
//        subRow3.setOrientation(LinearLayout.HORIZONTAL);
//        rowVerticalLayout.addView(subRow3);
//
//        TextView nameView = new TextView(ContactUs.this);
//        String businessName = name;
//        nameView.setText(businessName);
//        nameView.setTextColor(getResources().getColor(R.color.black));
//        nameView.setTypeface(Typeface.DEFAULT_BOLD);
//        subRow1.addView(nameView);
//
//        TextView addressView = new TextView(ContactUs.this);
//        String businessAddress = address;
//        addressView.setText(businessAddress);
//        addressView.setTextColor(getResources().getColor(R.color.black));
//        addressView.setTypeface(Typeface.DEFAULT_BOLD);
//        subRow1.addView(addressView);

    }

}