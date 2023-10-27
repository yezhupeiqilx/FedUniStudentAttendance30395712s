package au.edu.federation.itech3107.studentattendance30395712;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CourseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(CourseBean course);

    @Update
    int update(CourseBean course);

    @Delete
    void delete(CourseBean course);

    @Query("SELECT * FROM courses")
    List<CourseBean> getAllCourses();

    @Query("SELECT * FROM courses WHERE course_id = :id")
    CourseBean getCourseById(int id);

    @Query("DELETE FROM courses WHERE course_id = :courseId")
    int deleteByCourseId(int courseId);

    // New method to get the start dates of the first 12 courses and sort them by date
    @Query("SELECT  start_date FROM courses ORDER BY  start_date ASC LIMIT 12")
    List<String> getCourseStartDatesLimited();
}
