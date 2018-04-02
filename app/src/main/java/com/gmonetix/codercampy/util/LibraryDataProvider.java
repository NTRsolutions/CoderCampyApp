package com.gmonetix.codercampy.util;

import android.content.Context;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.model.Library;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gaurav Bordoloi on 4/1/2018.
 */
public class LibraryDataProvider {

    public static List<Library> getData(Context context) {

        String[] names = context.getResources().getStringArray(R.array.libraries_name);
        String[] links = context.getResources().getStringArray(R.array.libraries_link);
        List<Library> list = new ArrayList<>();

        for (int i=0; i<names.length; i++) {
            list.add(new Library(names[i],links[i]));
        }

        return list;
    }

}
