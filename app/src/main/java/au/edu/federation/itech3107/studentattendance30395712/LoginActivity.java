package au.edu.federation.itech3107.studentattendance30395712;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    // UI components
    private EditText etLoginUserId, etLoginPassword;
    private Button btnLogin;

    // Database components
    private  Database database;
    private TeacherDao teacherDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize the UI components
        initUI();

        // Set up the database components
        initDatabase();

        // Set up the login button functionality
        setupLoginButton();
    }

    /**
     * Initialize user interface components.
     */
    private void initUI() {
        etLoginUserId = findViewById(R.id.etLoginUserId);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
    }

    /**
     * Initialize the database components using Room.
     */
    private void initDatabase() {
        database =  Database.getDatabase(this);
        teacherDao = database.teacherDao();
    }

    /**
     * Set up the login button's functionality.
     */
    private void setupLoginButton() {
        btnLogin.setOnClickListener(v -> {
            String userId = etLoginUserId.getText().toString().trim();
            String password = etLoginPassword.getText().toString().trim();

            authenticateUser(userId, password);
        });
    }

    /**
     * Authenticate the user based on the provided user ID and password.
     */
    private void authenticateUser(String userId, String password) {
        Handler handler = new Handler(Looper.getMainLooper());

        new Thread(() -> {
            TeacherBean teacher = teacherDao.getTeacherById(userId);

            handler.post(() -> {
                if (teacher != null && teacher.getPassword().equals(password)) {
                    navigateToMainActivity();
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Invalid credentials or user not registered.", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    /**
     * Navigate to the main activity after a successful login.
     */
    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}