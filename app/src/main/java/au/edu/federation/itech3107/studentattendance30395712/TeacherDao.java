package au.edu.federation.itech3107.studentattendance30395712;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TeacherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(TeacherBean teacher);


    @Update
    int update(TeacherBean teacher);

    @Delete
    void delete(TeacherBean teacher);

    @Query("SELECT * FROM teacher")
    List<TeacherBean> getAllTeachers();

    @Query("SELECT * FROM teacher WHERE username = :username")
    TeacherBean getTeacherById(String username);
}
