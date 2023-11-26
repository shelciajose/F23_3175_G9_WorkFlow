package com.example.workflow.utils;

public class ConstantUtils {

    public static final String NO_SIM_CARD = "NO_SIM_CARD";
    public static final String BELLOW_KITKAT = "BELLOW_KITKAT";
    public static final String AUTHORITY = "com.example.workflow.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "com.example.workflow";
    // The account name
    public static final String ACCOUNT = "Workflow";

    public static final String NOTIFICATION_ALERT_CHANNEL_FOR_CHECK_IN = "NOTIFICATION_ALERT_CHANNEL_FOR_CHECK_IN";
    public static final String NOTIFICATION_ALERT_CHANNEL_FOR_CHECK_OUT = "NOTIFICATION_ALERT_CHANNEL_FOR_CHECK_OUT";

    public static final int NOTIFICATION_ALERT_ID_FOR_CHECK_IN = 104;
    public static final int NOTIFICATION_ALERT_ID_FOR_CHECK_OUT = 105;


    public static final String CHECK_IN_BUTTON_NAME = "Check-In";
    public static final String CHECK_OUT_BUTTON_NAME = "Check-Out";
    public static final String CHECKING_IN_BUTTON_NAME = "Checking-In...";
    public static final String CHECKING_OUT_BUTTON_NAME = "Checking-Out...";
    public static final String CHECK_IN_OUT_LISNER_NAME = "CHECK_IN_OUT_LISNER";
    public static final int OFFLINE_MODE = 0;
    public static final int ONLINE_MODE = 1;

    public static final int MIN_GPS_WAIT_TIME = 15000;
    public static final float GEOFENCE_RADIUS = 500.0f; // in meters
    public static final long LOCATION_REQ_INTERVAL = 10000;
    public static final long LOCATION_REQ_FAST_INTERVAL = 10000;
    public static final long LOCATION_REQ_SMALLEST_DISPLACEMENT = 10;
    public static final int MIN_ACCURACY_OF_LOCATION = 100;
    public static int REQUEST_CHECK_SETTINGS_HOME_FRAGMENT = 108;


    public static int REQUEST_CHECK_SETTINGS_HOME_FRAGMENT_AT_THE_TIME_OF_CHECK_IN_OUT = 108;
    public static int REQUEST_CHECK_SETTINGS_HOME_FRAGMENT_AT_THE_TIME_OF_MY_VISIT = 109;

    ///////////////////////////////////////////// capture camera image stuff
    ///////////////// image folder directory name
    public static final String IMAGE_STORAGE_DIRECTORY_NAME = "WORKFLOW_CACHE";
    ///// file provider authority
    public static final String FILE_PROVIDER_AUTHORITY = "com.example.workflow.fileprovider";

    public static final String NOTIFICATION_TYPE_RELOG_USER = "NOTIFICATION_TYPE_RELOG_USER";
    public static final String NOTIFICATION_TYPE_ON_GPS = "NOTIFICATION_TYPE_ON_GPS";
    public static final String NOTIFICATION_TYPE_GRANT_PERMISSION = "NOTIFICATION_TYPE_GRANT_PERMISSION";
    public static final String NOTIFICATION_TYPE_NOTHING = "NOTIFICATION_TYPE_NOTHING";
    public static final String NOTIFICATION_TYPE_CHECK_IN_NOT_SYNCED = "NOTIFICATION_TYPE_CHECK_IN_NOT_SYNCED";
    // notification id's
    public static final int NOTIFICATION_TYPE_RELOG_USER_ID = 100;
    public static final int NOTIFICATION_TYPE_ON_GPS_ID = 101;
    public static final int NOTIFICATION_TYPE_GRANT_PERMISSION_ID = 102;
    public static final int NOTIFICATION_TYPE_NOTHING_ID = 103;

    /////  tracking service
    public static final String NOTIFICATION_CHANNEL_CHECK_OUT_OF_TRACKING_NOTIFICATION = "NOTIFICATION_CHANNEL_CHECK_OUT_OF_TRACKING_NOTIFICATION";
    public static final String NOTIFICATION_CHANNEL_CHECK_IN_OF_TRACKING_NOTIFICATION = "NOTIFICATION_CHANNEL_CHECK_IN_OF_TRACKING_NOTIFICATION";
    public static final String NOTIFICATION_CHANNEL_ID_OF_TRACKING_NOTIFICATION = "NOTIFICATION_CHANNEL_ID_OF_TRACKING_NOTIFICATION";
    public static final String NOTIFICATION_CHANNEL_NAME_OF_TRACKING_NOTIFICATION = "NOTIFICATION_CHANNEL_NAME_OF_TRACKING_NOTIFICATION";
    public static final String NOTIFICATION_CHANNEL_ID_OF_COMM_NOTIFICATION = "NOTIFICATION_CHANNEL_ID_OF_COMM_NOTIFICATION";
    public static final String NOTIFICATION_CHANNEL_NAME_OF_COMM_NOTIFICATION = "NOTIFICATION_CHANNEL_NAME_OF_COMM_NOTIFICATION";

    /// syync data notification
    public static final String NOTIFICATION_CHANNEL_ID_FOR_SYNC_DATA = "NOTIFICATION_CHANNEL_ID_FOR_SYNC_DATA";
    public static final String NOTIFICATION_CHANNEL_NAME_FOR_SYNC_NOTIFICATION = "NOTIFICATION_CHANNEL_NAME_FOR_SYNC_NOTIFICATION";
    public static final int IDENTIFIERS_FOR_SYNC_NOTIFICATION = 14;


    //The identifier for this notification as per
    public static final int IDENTIFIERS_FOR_TRACK_NOTIFICATION = 10;
    public static final int IDENTIFIERS_FOR_TRACK_COMM_NOTIFICATION = 11;
    public static final int IDENTIFIERS_FOR_CHECK_IN_NOTIFICATION = 12;
    public static final int IDENTIFIERS_FOR_CHECK_OUT_NOTIFICATION = 13;

    ///////////////// sync flags. . .
    public static final String SYNC_LOC_FLAG = "SYNC_LOC_FLAG";
    public static final String SYNC_CHECK_IN_FLAG = "SYNC_CHECK_IN_FLAG";
    public static final String SYNC_CHECK_OUT_FLAG = "SYNC_CHECK_OUT_FLAG";
    public static final String SYNC_ADD_VISITS_FLAG = "SYNC_ADD_VISITS_FLAG";

    public static final String LOCATION_SERVICE_COMMON_BROADCAST_NAME = "LOCATION_SERVICE_COMMON_BROADCAST_NAME";
    public static final String LOCATION_SERVICE_COMMON_LATITUDE = "LOCATION_SERVICE_COMMON_LATITUDE";
    public static final String LOCATION_SERVICE_COMMON_LONGITUDE = "LOCATION_SERVICE_COMMON_LONGITUDE";

    ////////////////// in call ui package. . . .
    public static final String IN_CALL_UI_PACKAGE_NAME_1 = "com.android.incallui";
    public static final String IN_CALL_UI_PACKAGE_NAME_2 = "com.android.dialer";

    //Takumi using
    public static final String KEY_ID = "id";
    public static final String KEY_USER = "users";
    public static final String KEY_RECEIVER_USER_ID = "receiverUserId";
    public static final String KEY_CONNECTED_USER = "connectedUser";
    public static final String KEY_USER_NAME = "userName";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_IMAGE_CHAT = "imageChat";
    public static final String KEY_ONLINE_STATUS = "onlineStatus";
    public static final String KEY_PROCESSING = "processing";
    public static final String KEY_PROCESSING_CHAT = "processingChat";
    public static final String KEY_ONLINE = "online";
    public static final String KEY_OFFLINE = "offline";
    public static final String KEY_CHAT = "chat";
    public static final String KEY_CHAT_LIST = "chatList";
    public static final String KEY_DATE_FORMAT_CHAT = "dd/MM/yyyy hh:mm aa";
    public static final String KEY_SENDER = "sender";
    public static final String KEY_RECEIVER = "receiver";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_TIME_STAMP = "timeStamp";
    public static final String KEY_DILIHAT = "dilihat";

    public static final int KEY_MESSAGE_TYPE_LEFT = 0;
    public static final int KEY_MESSAGE_TYPE_RIGHT = 1;
    public static final String KEY_DELETE = "delete";
    public static final String KEY_CANCEL = "cancel";

    public static final String KEY_EMAIL = "email";

    public static final String KEY_FIRST_NAME = "firstName";
    public static final String KEY_LAST_NAME ="lastName";
    public static final String KEY_DEPARTMENT = "dept";
    public static final String KEY_LEAVE_START_DATE = "leaveStartDate";
    public static final String KEY_LEAVE_END_DATE = "leaveEndDate";
    public static final String MAIL_ADDRESS_MANAGER = "workflow.g9.manager@outlook.com";
    public static final String MAIL_ADDRESS_CC = "takumi42.08@outlook.com";
    public static final String MAIL_PW = "csis3175g9";
    public static final String MAIL_EMPLOYER = "takumi42.08@outlook.com";

    public static final String FORMAT_CALENDAR_EVENT = "MM/dd/yyyy";

    //end takumi
}
