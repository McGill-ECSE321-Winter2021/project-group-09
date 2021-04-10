package ca.mcgill.ecse321.repairshop;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ViewServices extends AppCompatActivity {
    private String error = "";
    private TableLayout serviceTable;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_service);

        serviceTable = findViewById(R.id.services_table);
        serviceTable.setStretchAllColumns(true);

        addAllServicesToTable();

    }



    private void addAllServicesToTable() {
        HttpUtils.get("api/service/all", new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                int n = response.length();

                for(int i = 0; i < n; i++) {
                    JSONObject thisService = null;
                    try {
                        thisService = response.getJSONObject(i);
                    } catch(Exception e) {
                        error += e.getMessage();
                    }
                    addServiceToTable(thisService);
                }
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


    private void addServiceToTable(JSONObject service) {
        String name;
        Double price;
        Integer duration;

        try {
            name = service.getString("name");
            price = service.getDouble("price");
            duration = 30 * service.getInt("duration");
        } catch(Exception e) {
            error += e.getMessage();
            refreshErrorMessage();
            return;
        }


        TableRow row = initializeRow(ViewServices.this);
        serviceTable.addView(row);

        LinearLayout rowVerticalLayout = new LinearLayout(ViewServices.this);
        rowVerticalLayout.setOrientation(LinearLayout.VERTICAL);
        row.addView(rowVerticalLayout);

        LinearLayout subRow1 = new LinearLayout(ViewServices.this);
        subRow1.setOrientation(LinearLayout.HORIZONTAL);
        rowVerticalLayout.addView(subRow1);

        LinearLayout subRow2 = new LinearLayout(ViewServices.this);
        subRow2.setOrientation(LinearLayout.HORIZONTAL);
        rowVerticalLayout.addView(subRow2);

        LinearLayout subRow3 = new LinearLayout(ViewServices.this);
        subRow3.setOrientation(LinearLayout.HORIZONTAL);
        rowVerticalLayout.addView(subRow3);

        TextView nameView = new TextView(ViewServices.this);
        String sName = "Service: " + name;
        nameView.setText(sName);
        nameView.setTextColor(getResources().getColor(R.color.black));
        nameView.setTypeface(Typeface.DEFAULT_BOLD);
        subRow1.addView(nameView);

        TextView priceView = new TextView(ViewServices.this);
        String sPrice = "Price: $" + price.toString();
        priceView.setTextColor(getResources().getColor(R.color.black));
        priceView.setText(sPrice);
        subRow2.addView(priceView);

        TextView durationView = new TextView(ViewServices.this);
        String sDuration = "Duration: " + duration.toString() + "min.";
        durationView.setTextColor(getResources().getColor(R.color.black));
        durationView.setText(sDuration);
        subRow3.addView(durationView);

    }

    @NotNull
    public static TableRow initializeRow(Context context) {
        TableRow row = new TableRow(context);

        TableLayout.LayoutParams rowLayoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        rowLayoutParams.setMargins(0, 20, 0, 20);
        row.setLayoutParams(rowLayoutParams);

        return row;
    }


    private void refreshErrorMessage() {
        TextView tv = findViewById(R.id.servicesError);
        tv.setText(error);
    }
}
