package au.edu.federation.itech3107.studentattendance30395712;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StudentActivity extends AppCompatActivity {

    private EditText studentNameField, studentIdField;
    private Spinner courseSelector;
    private Button registerStudentButton;
    private List<String> courseTitles = new ArrayList<>();
    private List<Integer> courseIdentifiers = new ArrayList<>();
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        initializeComponents();
        populateCourseSelector();
        setStudentRegistrationHandler();
        configureStudentsList();
    }

    private void initializeComponents() {
        database = Database.getDatabase(this);
        studentIdField = findViewById(R.id.edtStudentId);
        studentNameField = findViewById(R.id.edtStudentName);
        courseSelector = findViewById(R.id.spinnerCourses);
        registerStudentButton = findViewById(R.id.btnAddStudentToCourse);
    }

    private void populateCourseSelector() {
        new LoadCoursesTask().execute();
    }

    private void setStudentRegistrationHandler() {
        registerStudentButton.setOnClickListener(v -> registerStudent());
    }

    private void registerStudent() {
        String studentIdInput = studentIdField.getText().toString().trim();
        String studentNameInput = studentNameField.getText().toString().trim();
        int selectedCourseIndex = courseSelector.getSelectedItemPosition();
        int selectedCourseId = courseIdentifiers.get(selectedCourseIndex);

        StudentBean newStudent = new StudentBean();
        newStudent.setStudentId(Integer.parseInt(studentIdInput));
        newStudent.setStudentName(studentNameInput);
        newStudent.setCourseId(selectedCourseId);

        new RegisterStudentThread(newStudent).start();
    }

    private void configureStudentsList() {
        RecyclerView studentListView = findViewById(R.id.recyclerView);
        studentListView.setLayoutManager(new LinearLayoutManager(this));
        new PopulateStudentsListThread(studentListView).start();
    }

    private class LoadCoursesTask extends AsyncTask<Void, Void, List<CourseBean>> {
        @Override
        protected List<CourseBean> doInBackground(Void... voids) {
            return database.courseDao().getAllCourses();
        }

        @Override
        protected void onPostExecute(List<CourseBean> courses) {
            for (CourseBean course : courses) {
                courseIdentifiers.add(course.getCourseId());
                courseTitles.add(course.getCourseName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(StudentActivity.this, android.R.layout.simple_spinner_item, courseTitles);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            courseSelector.setAdapter(adapter);
        }
    }

    private class RegisterStudentThread extends Thread {
        private final StudentBean student;

        RegisterStudentThread(StudentBean student) {
            this.student = student;
        }

        @Override
        public void run() {
            long id = database.studentCourseDao().insert(student);
            runOnUiThread(() -> {
                if (id != -1) {
                    Toast.makeText(StudentActivity.this, "Student Registered Successfully", Toast.LENGTH_SHORT).show();
                    studentNameField.setText("");
                    finish();
                } else {
                    Toast.makeText(StudentActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private class PopulateStudentsListThread extends Thread {
        private final RecyclerView listView;

        PopulateStudentsListThread(RecyclerView listView) {
            this.listView = listView;
        }

        @Override
        public void run() {
            List<StudentBean> students = database.studentCourseDao().getAllStudentCourses();
            runOnUiThread(() -> {
                StudentListAdapter  adapter = new StudentListAdapter (StudentActivity.this, students);
                listView.setAdapter(adapter);
            });
        }
    }
}