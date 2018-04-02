package com.gmonetix.codercampy.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import com.gmonetix.codercampy.model.Course;
import java.util.List;
import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by Gaurav Bordoloi on 1/19/2018.
 */

@Dao
public interface UserDao {

    @Query("SELECT * FROM course")
    List<Course> getAll();

    @Insert(onConflict = REPLACE)
    void insertAll(List<Course> course);

}
