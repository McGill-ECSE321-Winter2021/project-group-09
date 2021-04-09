package ca.mcgill.ecse321.repairshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menuContactUs) {
            // Switch to Contact Us
            return true;
        } else if (id == R.id.menuViewServices) {
            // Switch to View Services
            return true;
        } else if (id == R.id.menuBookAppointment) {
            // Switch to Book Appointment
            Intent intent = new Intent(this, BookAppointment.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menuViewAppointments) {
            // Switch to View Appointments
            return true;
        } else if (id == R.id.menuLogout) {
            // Switch to Logout
            return true;
        } else if (id == R.id.menuRegister) {
            // Switch to Register
            return true;
        } else if (id == R.id.menuLogin) {
            // Switch to Login
            return true;
        } else return super.onOptionsItemSelected(item);
    }

}