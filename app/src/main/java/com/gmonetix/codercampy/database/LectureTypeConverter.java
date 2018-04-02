package com.gmonetix.codercampy.database;

import com.gmonetix.codercampy.model.Lecture;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Created by Gaurav Bordoloi on 4/2/2018.
 */
public class LectureTypeConverter {

    private static Gson gson = new Gson();

    @android.arch.persistence.room.TypeConverter
    public static List<Lecture> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Lecture>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @android.arch.persistence.room.TypeConverter
    public static String someObjectListToString(List<Lecture> someObjects) {
        return gson.toJson(someObjects);
    }

}
