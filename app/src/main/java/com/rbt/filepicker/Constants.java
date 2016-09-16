package com.rbt.filepicker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by user on 9/16/2016.
 */
public class Constants {
    public static final List<String> MIME_TYPE_RESUME = Collections.unmodifiableList(
            new ArrayList<String>() {{
                add("application/msword");
                add("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                add("application/vnd.google-apps.document");
                add("application/pdf");
                // etc
            }});
}
