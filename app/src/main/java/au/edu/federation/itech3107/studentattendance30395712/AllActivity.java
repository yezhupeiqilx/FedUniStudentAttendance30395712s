package au.edu.federation.itech3107.studentattendance30395712;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class AllActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all);

        findViewById(R.id.register).setOnClickListener(v -> {
            Intent intent =new Intent(AllActivity.this,RegisterActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.login).setOnClickListener(v -> {
            Intent intent =new Intent(AllActivity.this,LoginActivity.class);
            startActivity(intent);
        });
    }
}