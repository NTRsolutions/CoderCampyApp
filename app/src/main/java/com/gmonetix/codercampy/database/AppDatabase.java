package com.gmonetix.codercampy.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import com.gmonetix.codercampy.model.Course;

/**
 * Created by Gaurav Bordoloi on 1/19/2018.
 */

@Database(entities = {Course.class}, version = 1)
public abstract class AppDatabase  extends RoomDatabase {

    public abstract UserDao userDao();

}
