package au.edu.federation.itech3107.studentattendance30395712;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


/**
 * Adapter for managing student attendance data display in a RecyclerView.
 */
public class StudentAttendanceAdapter extends RecyclerView.Adapter<StudentAttendanceAdapter.StudentAttendanceViewHolder> {

    private final Context mContext;
    private final List<StudentBean> mStudents;
    private final SparseBooleanArray mAttendanceState;
    private final StudentDao mStudentDao;

    public StudentAttendanceAdapter(Context context, List<StudentBean> students, StudentDao studentDao) {
        this.mContext = context;
        this.mStudents = students;
        this.mStudentDao = studentDao;
        this.mAttendanceState = new SparseBooleanArray();
    }
    public List<StudentBean> getAllStudents() {
        return mStudents;
    }

    @NonNull
    @Override
    public StudentAttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_studentattendance, parent, false);
        return new StudentAttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAttendanceViewHolder holder, int position) {
        StudentBean student = mStudents.get(position);
        holder.mStudentNameTextView.setText(student.getStudentName());
        holder.mAttendanceCheckbox.setChecked(false);
        // Set the checked state based on stored attendance state
        holder.mAttendanceCheckbox.setChecked(mAttendanceState.get(student.getStudentId(), false));
        
        // Store the checked state when checkbox state is changed
        holder.mAttendanceCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> mAttendanceState.put(student.getStudentId(), isChecked));
    }

    @Override
    public int getItemCount() {
        return mStudents.size();
    }

    /**
     * Retrieves the attendance states for students.
     *
     * @return SparseBooleanArray mapping student IDs to their attendance state.
     */
    public SparseBooleanArray getAttendanceState() {
        return mAttendanceState;
    }

    /**
     * ViewHolder for student attendance items.
     */
    static class StudentAttendanceViewHolder extends RecyclerView.ViewHolder {
        final TextView mStudentNameTextView;
        final CheckBox mAttendanceCheckbox;

        public StudentAttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            mStudentNameTextView = itemView.findViewById(R.id.studentNameTextView);
            mAttendanceCheckbox = itemView.findViewById(R.id.attendanceCheckbox);
        }
    }
}
