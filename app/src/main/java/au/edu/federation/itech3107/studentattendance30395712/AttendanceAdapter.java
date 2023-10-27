package au.edu.federation.itech3107.studentattendance30395712;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class AttendanceAdapter extends BaseAdapter {
    private Context context;
    private List<Attendance> attendances;
    private StudentDao studentDao;

    public AttendanceAdapter(Context context, List<Attendance> attendances) {
        this.context = context;
        this.attendances = attendances;

        Database database = Database.getDatabase(context);
        studentDao = database.studentCourseDao();
    }

    @Override
    public int getCount() {
        return attendances.size();
    }

    @Override
    public Object getItem(int position) {
        return attendances.get(position);
    }

    @Override
    public long getItemId(int position) {
        return attendances.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;

        if (convertView == null) {
            view = inflater.inflate(R.layout.attendance_item, parent, false);
        } else {
            view = convertView;
        }

        TextView studentNameTextView = view.findViewById(R.id.studentNameTextView);
        TextView attendanceStatusTextView = view.findViewById(R.id.attendanceStatusTextView);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Attendance currentAttendance = attendances.get(position);
                int studentId = currentAttendance.getStudentId();
                 StudentBean studentCourse = studentDao.getStudentCourseById(studentId);
                 new Handler(Looper.getMainLooper()).post(() -> {
                    if (studentCourse != null) {
                        int studentId1 = studentCourse.getStudentId();
                        String studentName = studentCourse.getStudentName();
                        studentNameTextView.setText( studentId1+"");
                        attendanceStatusTextView.setText(currentAttendance.isPresent() ? "attendance" : "no attendance");
                    }
                });
            }
        }).start();

        return view;
    }
}