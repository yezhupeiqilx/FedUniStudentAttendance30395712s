package au.edu.federation.itech3107.studentattendance30395712;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    // UI elements
    private EditText etUserId, etPassword, etConfirmPassword;
    private Button btnRegister;

    // Database elements
    private Database database;
    private TeacherDao teacherDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize UI elements
        initUI();

        // Initialize database elements
        initDatabase();

        // Set up button listener
        setUpRegisterButton();
    }

    /**
     * Initialize user interface elements.
     */
    private void initUI() {
        etUserId = findViewById(R.id.etUserId);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
    }

    /**
     * Initialize database and DAO elements.
     */
    private void initDatabase() {
        // Use Room to get database and DAO
        database = Database.getDatabase(this);
        teacherDao = database.teacherDao();
    }

    /**
     * Set up the Register button listener.
     */
    private void setUpRegisterButton() {
        btnRegister.setOnClickListener(v -> {
            String userId = etUserId.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (isValidInput(userId, password, confirmPassword)) {
                // Use Room to insert the teacher's info into the database
                insertTeacherIntoDatabase(userId, password);

                // Navigate to LoginActivity
                navigateToLogin();
            }
        });
    }

    /**
     * Check if user input is valid.
     */
    private boolean isValidInput(String userId, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Insert teacher data into the database.
     */
    private void insertTeacherIntoDatabase(String userId, String password) {
        TeacherBean teacher = new TeacherBean();
        teacher.setUsername(userId);
        teacher.setPassword(password);
        new Thread(() -> {
            teacherDao.insert(teacher);
            runOnUiThread(() -> Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show());
        }).start();
    }

    /**
     * Navigate to the login screen.
     */
    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
