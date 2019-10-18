package com.xy.commonbase.constants;

import androidx.annotation.IntDef;
import androidx.annotation.StringDef;
import androidx.annotation.StringRes;

import com.xy.commonbase.base.BaseApplication;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Constants {

    public static final String ARG_PARAM1 = "fragment_param1";

    public static final String ARG_PARAM2 = "fragment_param2";

    public static final String ARG_PARAM3 = "fragment_param3";

    public static final String ARG_PARAM4 = "fragment_param4";

    public static final String LOCATION_BROADCAST = BaseApplication.getApplication().getPackageName()+".location";

    private static final int IMAGE_SMALL = 840;

    private static final int IMAGE_MEDIA = 162;

    private static final int IMAGE_LARGE = 708;
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({IMAGE_SMALL,IMAGE_MEDIA,IMAGE_LARGE})
    public @interface ImageSizeType{
        int SMALL = IMAGE_SMALL;
        int MEDIA = IMAGE_MEDIA;
        int LARGE = IMAGE_LARGE;
    }

    private static final int OPTIONS_TEXT = 1407;

    private static final int OPTIONS_EDIT = 1408;

    private static final int OPTIONS_IMAGE = 1409;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({OPTIONS_TEXT,OPTIONS_EDIT,OPTIONS_IMAGE})
    public @interface OptionsType{
        int TEXT = OPTIONS_TEXT;
        int EDIT = OPTIONS_EDIT;
        int IMAGE = OPTIONS_IMAGE;
    }

    /**
     * Shared Preference key
     */
    public static final String MY_SHARED_PREFERENCE = "my_shared_preference";

    public static final String ACCOUNT = "account";

    public static final String PASSWORD = "password";

    public static final String TOKEN = "token";

    public static final String SESSION = "session";

    public static final String LOGIN_STATUS = "login_status";

    public static final String AUTO_CACHE_STATE = "auto_cache_state";

    public static final String NO_IMAGE_STATE = "no_image_state";

    public static final String NIGHT_MODE_STATE = "night_mode_state";

    public static final String TYPEFACE_STATE = "typeface_state";

    public static final String MERCHANT_STATE = "merchant_state";

    public static final String CURRENT_PAGE = "current_page";

    public static final String PROJECT_CURRENT_PAGE = "project_current_page";

    public static final String FIRST_INSTALL = "apk_first_install";

    public static final String FONT_SIZE = "font_size";

    public static final String LAST_LOCATION_CODE = "last_location_code";

    public static final String MEMBER_ID = "member_id";

    public static final String COOKIE = "Cookie";

    public static final String DB_NAME = "moheadlines.db";

}
