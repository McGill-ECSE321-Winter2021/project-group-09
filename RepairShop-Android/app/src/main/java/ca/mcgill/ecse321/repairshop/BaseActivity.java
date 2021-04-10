package ca.mcgill.ecse321.repairshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class BaseActivity extends AppCompatActivity {

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

        if (id == R.id.menuHomepage) {
            // Switch to Homepage
            startActivity(new Intent(this, MainActivity.class));
            return true;
        } else if (id == R.id.menuContactUs) {
            // Switch to Contact Us
            return true;
        } else if (id == R.id.menuViewServices) {
            // Switch to View Services
            return true;
        } else if (id == R.id.menuBookAppointment) {
            // Switch to Book Appointment
            startActivity(new Intent(this, BookAppointment.class));
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
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        } else return super.onOptionsItemSelected(item);
    }
}