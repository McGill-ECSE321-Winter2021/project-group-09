package ca.mcgill.ecse321.repairshop;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class BaseActivity extends AppCompatActivity {

    public Menu appMenu;

    /**
     * Creates the options menu on the top right corner according to the loggedIn state of the user.
     * @param menu (Menu)
     * @return true (boolean)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (State.loggedIn) updateMenuLogin(menu);
        else updateMenuLogout(menu);
        this.appMenu = menu;
        return true;
    }

    /**
     * Handle action bar item clicks here.
     * The action bar will automatically handle clicks on the Home/Up button,
     * so long as you specify a parent activity in AndroidManifest.xml.
     *
     * @param item (Menu Item)
     * @return true (boolean)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menuHomepage) {
            // Switch to Homepage
            startActivity(new Intent(this, MainActivity.class));
            return true;
        } else if (id == R.id.menuContactUs) {
            // Switch to Contact Us
            startActivity(new Intent(this, ContactUsActivity.class));
            return true;
        } else if (id == R.id.menuViewServices) {
            // Switch to View Services
            startActivity(new Intent(this, ViewServicesActivity.class));
            return true;
        } else if (id == R.id.menuBookAppointment) {
            // Switch to Book Appointment
            startActivity(new Intent(this, BookAppointmentActivity.class));
            return true;
        } else if (id == R.id.menuViewAppointments) {
            // Switch to View Appointments
            startActivity(new Intent(this, ViewAppointmentsActivity.class));
            return true;
        } else if (id == R.id.menuCancelAppointment) {
            // Switch to Cancel Appointment
            startActivity(new Intent(this, CancelAppointmentActivity.class));
            return true;
        }else if (id == R.id.menuLogout) {
            // Log out the user
            updateMenuLogout(appMenu);
            startActivity(new Intent(this, MainActivity.class));
            return true;
        } else if (id == R.id.menuRegister) {
            // Switch to Register
            startActivity(new Intent(this, RegisterActivity.class));
            return true;
        } else if (id == R.id.menuLogin) {
            // Switch to Login
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        } else return super.onOptionsItemSelected(item);
    }

    /**
     * Updates menu items since customer is logged IN
     * @param menu (Menu)
     */
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

    /**
     * Update menu items since customer is logged OUT.
     * @param menu (Menu)
     */
    public static void updateMenuLogout(Menu menu) {

        State.loggedIn = false;
        State.token = "";
        State.email = "";

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
