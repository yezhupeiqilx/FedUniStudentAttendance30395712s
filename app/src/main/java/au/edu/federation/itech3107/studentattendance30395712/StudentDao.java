package au.edu.federation.itech3107.studentattendance30395712;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StudentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(StudentBean studentCourse);

    @Update
    int update(StudentBean studentCourse);

    @Delete
    void delete(StudentBean studentCourse);

    @Query("SELECT * FROM student_course")
    List<StudentBean> getAllStudentCourses();

    @Query("SELECT * FROM student_course WHERE student_id = :studentId")
    StudentBean getStudentCourseById(int studentId);

    @Query("DELETE FROM student_course WHERE student_id = :studentId")
    void deleteByStudentIdAndCourseId(int studentId);
    @Query("SELECT _id, student_id, student_name, course_id FROM student_course WHERE course_id = :courseId")
    List<StudentBean> getStudentByCourseId(int courseId);
}
