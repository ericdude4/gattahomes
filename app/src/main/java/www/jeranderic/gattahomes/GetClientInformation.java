package www.jeranderic.gattahomes;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * A screen that accepts user information and writes it to a log file before continuing on to
 * virtual tour.
 */
public class GetClientInformation extends AppCompatActivity {

    private static final String TAG = "GetClientInformation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_get_client_information);
        Spinner movingTime = (Spinner) findViewById(R.id.move);
        Spinner houseType = (Spinner) findViewById(R.id.house_type);
        Spinner houseSizeType = (Spinner) findViewById(R.id.house_size);
        Spinner roomNumType = (Spinner) findViewById(R.id.num_rooms);
        Spinner lotSizeType = (Spinner) findViewById(R.id.lot_size);
        Spinner budgetType = (Spinner) findViewById(R.id.budget);
        ArrayAdapter<CharSequence> budgetAdapter = ArrayAdapter.createFromResource(this, R.array.budgets, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> lotSizeAdapter = ArrayAdapter.createFromResource(this, R.array.lot_sizes, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> numRoomAdapter = ArrayAdapter.createFromResource(this, R.array.num_rooms, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> houseSizeAdapter = ArrayAdapter.createFromResource(this, R.array.home_size, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> houseAdapter = ArrayAdapter.createFromResource(this, R.array.house_types, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> moveAdapter = ArrayAdapter.createFromResource(this, R.array.move_dates, android.R.layout.simple_spinner_item);
        moveAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        houseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numRoomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lotSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        budgetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (movingTime != null) {
            movingTime.setAdapter(moveAdapter);
        }
        if (houseType != null) {
            houseType.setAdapter(houseAdapter);
        }
        if (houseSizeType != null) {
            houseSizeType.setAdapter(houseSizeAdapter);
        }
        if (roomNumType != null) {
            roomNumType.setAdapter(numRoomAdapter);
        }
        if (lotSizeType != null) {
            lotSizeType.setAdapter(lotSizeAdapter);
        }
        if (budgetType != null) {
            budgetType.setAdapter(budgetAdapter);
        }
        // Set up the login form.
    }

    /**
     * the user has pressed the continue button, save info then continue to virtual tour
     */
    public void cont(View v) {

        TextView name = (TextView) findViewById(R.id.name);
        TextView email = (TextView) findViewById(R.id.email);
        TextView phoneNumber = (TextView) findViewById(R.id.phone);
        TextView city = (TextView) findViewById(R.id.city);
        Spinner moving = (Spinner) findViewById(R.id.move);
        Spinner houseType = (Spinner) findViewById(R.id.house_type);
        Spinner houseSize = (Spinner) findViewById(R.id.house_size);
        Spinner numRooms = (Spinner) findViewById(R.id.num_rooms);
        Spinner lotSize = (Spinner) findViewById(R.id.lot_size);
        Spinner budget = (Spinner) findViewById(R.id.budget);
        RadioGroup updates = (RadioGroup) findViewById(R.id.updates);

        RadioButton updateSel = (RadioButton) findViewById(updates.getCheckedRadioButtonId());

        boolean inValid = false;

        if (name.getText().toString().length() < 5) {
            name.setError("Name cannot be empty.");
            inValid = true;
        }

        if (!isValidEmail(email.getText())) {
            email.setError("Email cannot be empty and must be a valid email address.");
            inValid = true;
        }

        if (!isValidPhoneNumber(phoneNumber.getText()) && phoneNumber.getText().toString().length() < 10) {
            phoneNumber.setError("Phone Number cannot be empty and must be a valid phone number at least 10 digits long.");
            inValid = true;
        }

        if (city.getText().toString().length() < 3) {
            city.setError("City cannot be empty and must be at least 3 characters long.");
            inValid = true;
        }

        if (inValid) {
            return;
        }

        String emailString = "";
        emailString += "Name: " + name.getText() + ".\n";
        emailString += "Email: " + email.getText() + ".\n";
        emailString += "Phone Number: " + phoneNumber.getText() + ".\n";
        emailString += "City: " + city.getText() + ".\n";
        emailString += "Moving: " + moving.getSelectedItem().toString() + ".\n";
        emailString += "House Type: " + houseType.getSelectedItem().toString() + ".\n";
        emailString += "House Size: " + houseSize.getSelectedItem().toString() + ".\n";
        emailString += "Number of Rooms: " + numRooms.getSelectedItem().toString() + ".\n";
        emailString += "Lot Size: " + lotSize.getSelectedItem().toString() + ".\n";
        emailString += "Budget: " + budget.getSelectedItem().toString() + ".\n";
        emailString += "Send Updates: " + updateSel.getText().toString() + ".\n";

        Log.i(TAG, emailString);

        Emailer e = new Emailer();
        try {
            e.sendMail("Virtual Tour Participant",
                    emailString,
                    "virtualtour@gattahomes.com",
                    "fireynis@gmail.com");
        } catch (Exception e1) {
            //email failed to send
            e1.printStackTrace();
        }
        Intent i = new Intent();
        i.setClass(this, Display.class);
        i.putExtra("name", name.getText().toString());
        i.putExtra("email", email.getText().toString());
        startActivity(i);
    }

    private boolean isValidPhoneNumber(CharSequence text) {
        return !TextUtils.isEmpty(text) && Patterns.PHONE.matcher(text).matches();
    }

    private boolean isValidEmail(CharSequence text) {
        return !TextUtils.isEmpty(text) && Patterns.EMAIL_ADDRESS.matcher(text).matches();
    }

    /**
     * the user has pressed the skip button, continue to virtual tour
     */
    public void skip() {
        Intent i = new Intent();
        i.setClass(this, Display.class);
        startActivity(i);
    }
}

