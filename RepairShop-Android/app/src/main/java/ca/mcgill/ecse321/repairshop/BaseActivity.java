package ca.mcgill.ecse321.repairshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class BaseActivity extends AppCompatActivity {

    public Menu appMenu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (State.loggedIn) updateMenuLogin(menu);
        else updateMenuLogout(menu);
        this.appMenu = menu;
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
            startActivity(new Intent(this, ContactUs.class));
            return true;
        } else if (id == R.id.menuViewServices) {
            // Switch to View Services
            startActivity(new Intent(this, ViewServices.class));
            return true;
        } else if (id == R.id.menuBookAppointment) {
            // Switch to Book Appointment
            startActivity(new Intent(this, BookAppointment.class));
            return true;
        } else if (id == R.id.menuViewAppointments) {
            // Switch to View Appointments
            startActivity(new Intent(this, ViewAppointments.class));
            return true;
        } else if (id == R.id.menuCancelAppointment) {
            // Switch to Cancel Appointment
            startActivity(new Intent(this, CancelAppointment.class));
            return true;
        }else if (id == R.id.menuLogout) {
            // Log out the user
            updateMenuLogout(appMenu);
            startActivity(new Intent(this, MainActivity.class));
            return true;
        } else if (id == R.id.menuRegister) {
            // Switch to Register
            startActivity(new Intent(this, Register.class));
            return true;
        } else if (id == R.id.menuLogin) {
            // Switch to Login
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        } else return super.onOptionsItemSelected(item);
    }

    // Update menu items since customer is logged IN
    public static void updateMenuLogin(Menu menu) {

        State.loggedIn = true;

        MenuItem register = menu.findItem(R.id.menuRegister);
        register.setVisible(false);

        MenuItem login = menu.findItem(R.id.menuLogin);
        login.setVisible(false);

        MenuItem bookAppointment = menu.findItem(R.id.menuBookAppointment);
        bookAppointment.setVisible(true);

        MenuItem viewAppointments = menu.findItem(R.id.menuViewAppointments);
        viewAppointments.setVisible(true);

        MenuItem cancelAppointment = menu.findItem(R.id.menuCancelAppointment);
        cancelAppointment.setVisible(true);

        MenuItem logout = menu.findItem(R.id.menuLogout);
        logout.setVisible(true);

    }

    // Update menu items since customer is logged OUT
    public static void updateMenuLogout(Menu menu) {

        State.loggedIn = false;

        MenuItem register = menu.findItem(R.id.menuRegister);
        register.setVisible(true);

        MenuItem login = menu.findItem(R.id.menuLogin);
        login.setVisible(true);

        MenuItem bookAppointment = menu.findItem(R.id.menuBookAppointment);
        bookAppointment.setVisible(false);

        MenuItem viewAppointments = menu.findItem(R.id.menuViewAppointments);
        viewAppointments.setVisible(false);

        MenuItem cancelAppointment = menu.findItem(R.id.menuCancelAppointment);
        cancelAppointment.setVisible(false);

        MenuItem logout = menu.findItem(R.id.menuLogout);
        logout.setVisible(false);

    }


}
