package au.edu.federation.itech3107.studentattendance30395712;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private HandlerThread handlerThread;
    private Handler backgroundHandler;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private Database database;
    private CourseDao courseDao;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the background handler thread for database operations
        handlerThread = new HandlerThread("DatabaseBackgroundThread");
        handlerThread.start();
        backgroundHandler = new Handler(handlerThread.getLooper());

        // Initialize database and DAO
        database = Database.getDatabase(this);
        courseDao = database.courseDao();

        // Initialize buttons and RecyclerView
        Button btnStudent = findViewById(R.id.btnStudent);
        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnAttendance = findViewById(R.id.btn_attendance);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch courses from the database in a background thread
        backgroundHandler.post(() -> {
            List<CourseBean> courses = courseDao.getAllCourses();
            mainHandler.post(() -> {
                CourseAdapter adapter = new CourseAdapter(courses, MainActivity.this, course -> {
                    Intent intent = new Intent(MainActivity.this, AttendanceActivity.class);
                    intent.putExtra("COURSE_ID", course.getCourseId());
                    startActivity(intent);
                });
                recyclerView.setAdapter(adapter);
            });
        });

        // Button click listeners
        btnStudent.setOnClickListener(view -> {
            Log.d("Button Event", "Student Button Clicked");
            Intent intent = new Intent(this, StudentActivity.class);
            startActivity(intent);
        });

        btnAdd.setOnClickListener(view -> {
            Log.d("Button Event", "Add Button Clicked");
            Intent intent = new Intent(this, CourseEntryActivity.class);
            startActivity(intent);
        });

        btnAttendance.setOnClickListener(view -> {
            Log.d("Button Event", "Attendance Button Clicked");
            Intent intent = new Intent(this, AttendanceTrackerActivity.class);
            startActivity(intent);
        });
    }
}