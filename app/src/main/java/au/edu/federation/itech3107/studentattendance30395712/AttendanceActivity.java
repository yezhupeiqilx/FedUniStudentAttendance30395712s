package au.edu.federation.itech3107.studentattendance30395712;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class AttendanceActivity extends AppCompatActivity {

    private Spinner dateSpinner;
    private RecyclerView studentsRecyclerView;
    private StudentAttendanceAdapter adapter;
    private  Database appDatabase;
    private int courseId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendances);
        courseId = getIntent().getIntExtra("COURSE_ID", -1);
        initializeViews();
        setupRecyclerView();
        setupDateSpinner();
        setupSaveAttendanceButton();
    }

    private void initializeViews() {
        appDatabase =  Database.getDatabase(this);
        dateSpinner = findViewById(R.id.dateSpinner);
        studentsRecyclerView = findViewById(R.id.studentsRecyclerView);
    }

    private void setupRecyclerView() {
        new Thread(() -> {
            List<StudentBean> students = getStudentsFromDatabase(courseId);
            StudentDao studentCourseDao = appDatabase.studentCourseDao();
            runOnUiThread(() -> {
                adapter = new StudentAttendanceAdapter(this, students, studentCourseDao);
                studentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                studentsRecyclerView.setAdapter(adapter);
            });
        }).start();
    }

    private void setupDateSpinner() {
        new Thread(() -> {
            CourseBean courseByBean = appDatabase.courseDao().getCourseById(courseId);
            List<String> listData = courseByBean.getListData();

            runOnUiThread(() -> {
                ArrayAdapter<String> dateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listData);
                dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dateSpinner.setAdapter(dateAdapter);
            });
        }).start();
    }

    private void setupSaveAttendanceButton() {
        Button saveAttendanceButton = findViewById(R.id.saveAttendanceButton);
        saveAttendanceButton.setOnClickListener(v -> new Thread(this::saveAttendance).start());
    }

    private List<StudentBean> getStudentsFromDatabase(int courseId) {
        return appDatabase.studentCourseDao().getStudentByCourseId(courseId);
    }

    private void saveAttendance() {
        String selectedDate = dateSpinner.getSelectedItem().toString();
        SparseBooleanArray attendance = adapter.getAttendanceState();

        new Thread(() -> {
            for (StudentBean student : adapter.getAllStudents()) {
                int studentId = student.getStudentId();
                boolean isPresent = attendance.get(studentId, false);  // 默认值为false

                if (appDatabase.attendanceDao().studentCourseExists(studentId) > 0) {
                    Attendance newAttendance = new Attendance();
                    newAttendance.setAttendanceDate(selectedDate);
                    newAttendance.setStudentId(studentId);
                    newAttendance.setPresent(isPresent);

                    appDatabase.attendanceDao().insert(newAttendance);
                } else {
                    runOnUiThread(() -> showMessage("无效的学生ID: " + studentId));
                }
            }
            runOnUiThread(() -> {
                adapter.notifyDataSetChanged();
                showMessage("Attendance saved");
            });
        }).start();
    }


    private void showMessage(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }
}
