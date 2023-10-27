package au.edu.federation.itech3107.studentattendance30395712;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.necer.calendar.WeekCalendar;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CourseEntryActivity extends AppCompatActivity {
    private String UpClassDate;
    private int currentWeek = 1;
    private CourseRepository repository;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        final TextInputEditText inputCourseId = findViewById(R.id.edtCourseId);
        final TextInputEditText inputCourseName = findViewById(R.id.edtCourseName);
        final TextView displayStartDate = findViewById(R.id.tvStartDate);
        final TextView displayEndDate = findViewById(R.id.tvEndDate);
        Button selectStartDateButton = findViewById(R.id.btnPickStartDate);
        WeekCalendar weekCalendar =findViewById(R.id.btnUpClass);
        weekCalendar.setOnCalendarChangedListener((baseCalendar, year, month, localDate, dateChangeBehavior) -> {
            UpClassDate = localDate.toString();
        });

        Button storeButton = findViewById(R.id.btnSave);

        repository = new CourseRepository(Database.getDatabase(this).courseDao());

        selectStartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(displayStartDate);
            }
        });



        storeButton.setOnClickListener(v -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            String inputDate = displayStartDate.getText().toString();


            String[] parts = inputDate.split("-");
            if (parts.length == 3) {
                if (parts[1].length() == 1) {
                    parts[1] = "0" + parts[1];
                }
                if (parts[2].length() == 1) {
                    parts[2] = "0" + parts[2];
                }
                inputDate = String.join("-", parts);
            }

            LocalDate currentStartDate = LocalDate.parse(inputDate, formatter);

            List<String> datesList = new ArrayList<>();
            for (int i = 0; i < 12; i++) {
                String formattedDate = currentStartDate.format(formatter);
                datesList.add(formattedDate);
                currentStartDate = currentStartDate.plusDays(7);
            }


            CourseBean newCourse = new CourseBean();
            newCourse.setCourseId(Integer.parseInt(inputCourseId.getText().toString().trim()));
            newCourse.setCourseName(inputCourseName.getText().toString().trim());
            newCourse.setStartDate(displayStartDate.getText().toString().trim());
            newCourse.setEndDate(displayEndDate.getText().toString().trim());
            newCourse.setListData(datesList);

            repository.addCourse(newCourse, success -> {
                if (success) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CourseEntryActivity.this, "Course Added Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent(CourseEntryActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CourseEntryActivity.this, "Error Adding Course", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            });
        });
    }
    private static String formatWithoutLeadingZero(LocalDate date) {
        String pattern = "yyyy-M-d";
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern(pattern);
        return customFormatter.format(date);
    }
    private void openDatePicker(final TextView targetView) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        targetView.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                        if (targetView.getId() == R.id.tvStartDate) {
                            Calendar endCal = Calendar.getInstance();
                            endCal.set(year, monthOfYear, dayOfMonth);
                            endCal.add(Calendar.WEEK_OF_YEAR, 12);

                            TextView displayEndDate = findViewById(R.id.tvEndDate);
                            displayEndDate.setText(endCal.get(Calendar.YEAR) + "-" +
                                    (endCal.get(Calendar.MONTH) + 1) + "-" +
                                    endCal.get(Calendar.DAY_OF_MONTH));
                        }
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        datePicker.show(getSupportFragmentManager(), "Datepickerdialog");
    }

    private static class CourseRepository {
        private final CourseDao dao;

        CourseRepository(CourseDao dao) {
            this.dao = dao;
        }

        void addCourse(final CourseBean course, final Callback<Boolean> callback) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    long result = dao.insert(course);
                    callback.onResult(result != -1);
                }
            }).start();
        }

        interface Callback<T> {
            void onResult(T result);
        }
    }
}
