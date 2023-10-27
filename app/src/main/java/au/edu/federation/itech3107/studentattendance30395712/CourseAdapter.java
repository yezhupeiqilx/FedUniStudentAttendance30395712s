package au.edu.federation.itech3107.studentattendance30395712;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private final List<CourseBean> courses;
    private final Context context;
    private final OnCourseClickListener clickListener;
    private final CourseDao courseDao;  // Instance variable for CourseDao

    // Interface to handle course item clicks
    public interface OnCourseClickListener {
        void onCourseClick(CourseBean course);
    }

    public CourseAdapter(List<CourseBean> courses, Context context, OnCourseClickListener clickListener) {
        this.courses = courses;
        this.context = context;
        this.clickListener = clickListener;

        Database database = Database.getDatabase(context);
        courseDao = database.courseDao();
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        CourseBean course = courses.get(position);
        holder.tvCourseName.setText(course.getCourseName());

        holder.btnDelete.setOnClickListener(v -> {
            new Thread(() -> {
                if (deleteCourse(course.getCourseId())) {
                    new Handler(Looper.getMainLooper()).post(() -> {

                            courses.remove(position);
                            if(courses.isEmpty()) {  // Check if the list is now empty
                                notifyDataSetChanged();  // Refresh the entire RecyclerView
                            } else {
                                notifyItemRemoved(position);
                                notifyDataSetChanged();
                            }
                            Log.e("Delete a course","");

                    });
                }
            }).start();
        });

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onCourseClick(course);
            }
        });
    }

    // Helper method to delete a course
    private boolean deleteCourse(int courseId) {
        int deletedRows = courseDao.deleteByCourseId(courseId);
        return deletedRows > 0;
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    // ViewHolder class for course items
    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        final TextView tvCourseName;
        final Button btnDelete;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
