package kr.kjy.janban;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class UserProfile extends AppCompatActivity {

    private TextView tv_id, tv_pass, tv_name, tv_age;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.user_profile);

        tv_id = findViewById(R.id.tv_id);
        tv_pass = findViewById(R.id.tv_pass);
        tv_name = findViewById(R.id.tv_name);
        tv_age = findViewById(R.id.tv_age);

        Log.d("UserProfile", "UserProfile Activity Started");

        Intent intent = getIntent();



        if (intent != null) {
            String userID = intent.getStringExtra("userID");
            String userPass = intent.getStringExtra("userPass");
            String userName = intent.getStringExtra("userName");
            String userAge = intent.getStringExtra("userAge");

            Log.d("UserProfile", "Received userID: " + userID);
            Log.d("UserProfile", "Received userPass: " + userPass);
            Log.d("UserProfile", "Received userName: " + userName);
            Log.d("UserProfile", "Received userAge: " + userAge);

            if (userID != null) {
                tv_id.setText(userID);
            }
            if (userPass != null) {
                tv_pass.setText(userPass);
            }
            if (userName != null) {
                tv_name.setText(userName);
            }
            if (userAge != null) {
                tv_age.setText(userAge);
            }
        }



    }
}
