package au.edu.federation.itech3107.studentattendance30395712;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@androidx.room.Database(entities = {TeacherBean.class, CourseBean.class, StudentBean.class, Attendance.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class Database extends RoomDatabase {
    public abstract TeacherDao teacherDao();
    public abstract CourseDao courseDao();
    public abstract StudentDao studentCourseDao();
    public abstract AttendanceDao attendanceDao();

    private static volatile Database INSTANCE;

    public static Database getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (Database.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    Database.class, "attendance.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
