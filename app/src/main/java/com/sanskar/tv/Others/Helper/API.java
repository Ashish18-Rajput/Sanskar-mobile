package com.sanskar.tv.Others.Helper;

/**
 * Created by appsquadz on 1/25/18.
 */

public class API {

    //------------------------------------------------------------------------------------------------
//     public static String SERVER_URL = "https://app.sanskargroup.in/sanskar_development/data_model/";
    // public static String SERVER_URL = "https://app.sanskargroup.in/sanskar_development/data_model1/";
    public static String BASE_URL = "https://app.sanskargroup.in/";
    public static String SERVER_URL = BASE_URL+"data_model/";                          // live
    // public static String SERVER_URL ="https://dev.sanskargroup.in/";
    //------------------------------------------------------------------------------------------------


    public static String SIGN_UP = SERVER_URL + "user/Registration/login_authentication";
    public static String LOGOUT = SERVER_URL + "user/Registration/logout";


    public static String SEND_VERIFICATION_OTP = SERVER_URL + "user/Registration/send_verification_otp";
    public static String RESEND_VERIFICATION_OTP = SERVER_URL + "user/Registration/resend_verification_otp";
    public static String GET_USER_WITH_PROFILE_TOKEN = SERVER_URL + "user/Registration/get_user_profile_with_token";

    public static String SEND_OTP = SERVER_URL + "user/registration/send_otp";
    public static String RESEND_OTP = SERVER_URL + "user/registration/resend_otp";
    public static String VERIFY_OTP = SERVER_URL + "user/registration/verify_otp";

    public static String API_GET_APP_VERSION = SERVER_URL + "version/Version/get_version";

    public static String PROFILE_UPDATE = SERVER_URL + "user/registration/update_profile";
    public static String OTP_VERIFY = SERVER_URL + "user/registration/otp_verification";
    public static String GET_VIDEOS = SERVER_URL + "videos/Video_control/get_videos"; //Not in use
    public static String GET_SEARCH_VIDEOS = SERVER_URL + "videos/video_control/search_videos"; //VideoResponfseNew
    public static String HOME_PAGE_VIDEOS = SERVER_URL + "videos/Video_control/home_page_videos";

    public static String GET_CATEGORY_LANDING_PAGE = SERVER_URL + "videos/premium_video/get_category_landing_data";
    //videos/premium_video/get_category_landing_data
    public static String GET_EPISODES_LIST_BY_SEASON_ID = SERVER_URL + "videos/premium_video/get_episodes_list_by_season_id";

    public static String GET_BHAJAN_LIST_BY_MENUMASTER = SERVER_URL + "bhajan/bhajan/get_bhajan_list_by_menu_master";
    public static String GET_VIDEO_LIST_BY_MENUMASTER = SERVER_URL + "videos/video_control/get_video_list_by_menu_master";
    public static String GET_SEASON_LIST_BY_MENUMASTER = SERVER_URL + "videos/premium_video/get_seasons_list_by_menu_master";

    public static String GET_URL_DATA = SERVER_URL + "videos/video_control/get_data_by_media_id";

    public static String MENU_MASTER = SERVER_URL + "menu_master/get_menu_master";
    public static String GET_DEVICE_LIST = SERVER_URL + "user/Login_record/get_login_device_record";
    public static String Device_Logout = SERVER_URL + "user/Login_record/device_logout";
    public static String SEND_OTP_LOGIN_USERID = SERVER_URL + "user/Login_record/send_otp_login_userid";
    public static String CHANNEL_LIST = SERVER_URL + "channel/channel/get_channel_list";
    public static String MENU_MASTER_2 = SERVER_URL + "menu_master/get_menu_master_v2";
    public static final String HOME_SEARCH_DATA = SERVER_URL + "videos/Video_control/search_home_data";
    public static String GURUS_LIST = SERVER_URL + "guru/guru/get_guru_list";
    public static String GURUS_FOLLOW = SERVER_URL + "guru/guru/follow_guru";
    public static String GURUS_UNFOLLOW = SERVER_URL + "guru/guru/unfollow_guru";
    public static String GURUS_LIKE = SERVER_URL + "guru/guru/like_guru";
    public static String GURUS_UNLIKE = SERVER_URL + "guru/guru/unlike_guru";


    public static String IS_SOCIAL_LOGIN = SERVER_URL + "social_login/get_social_login_status";
    public static String LIKE_VIDEO = SERVER_URL + "videos/video_control/like_video";

    public static String WEB_VIEW = SERVER_URL + "testing/bhajan_view";

    public static String LIKE_CHANNEL = SERVER_URL + "videos/Video_control/like_channel";
    public static String UNLIKE_CHANNEL = SERVER_URL + "videos/Video_control/dislike_channel";
    public static String REPORT_MESSAGE = SERVER_URL + "chat/Chat/reported_chat";
    public static String INFORM_DELETE_CHAT = SERVER_URL + "chat/Chat/delete_reported_chat";
    public static String GET_PREMIUM_PLAN = SERVER_URL + "videos/premium_video/get_premium_plan";

    public static String UNLIKE_VIDEO = SERVER_URL + "videos/video_control/unlike_video";
    public static String VIEW_VIDEO = SERVER_URL + "videos/video_control/view_video";//not in use...merged into recent view
    public static String GET_BHAJANS = SERVER_URL + "bhajan/bhajan/get_bhajan_list";
    public static String SET_GURU_LIVE_STATUS = SERVER_URL + "pusher/Push_is_live/push_on_off_live";
    public static String GET_LIVE_GURU = SERVER_URL + "pusher/Push_is_live/total_is_on_live_users";
    public static String GET_BHAJANS_CATEGORY_WISE = SERVER_URL + "bhajan/bhajan/get_bhajan_list_by_category";
    public static String GET_NEWS = SERVER_URL + "news/news/get_news_list";
    public static String RELATED_VIDEOS = SERVER_URL + "videos/video_control/get_related_videos";

    public static String SUGGESTION_VIDEOS = "get_suggestion_video";

    public static String RECENT_VIEW = SERVER_URL + "videos/video_control/recent_views";
    public static String GURU_RELATED_VIDEOS = SERVER_URL + "videos/video_control/get_related_guru_videos";
    public static String GURU_RELATED_THUMBNAILS = SERVER_URL + "guru/guru/get_guru_thumbnails";
    public static String GURU_RELATED_AUDIOS = SERVER_URL + "bhajan/bhajan/get_related_guru_audios";
    public static final String SEARCH_GURU_LIST = SERVER_URL + "guru/guru/search_guru";
    public static final String NOTIFICATION_LIST = SERVER_URL + "notification/get_notification_list";
    public static final String NOTIFICATION_VIEW = SERVER_URL + "Notification/view_notification";
    public static final String NOTIFICATION_DETAIL = SERVER_URL + "notification/get_notification_detail";
    public static final String VIEW_ALL_VIDEOS_HOME = SERVER_URL + "videos/Video_control/home_page_view_all_videos";
    public static final String POST_COMMENT = SERVER_URL + "videos/Video_control/add_comment";
    public static final String GET_ALL_COMMENTS = SERVER_URL + "videos/Video_control/get_video_comment";
    public static final String SANKIRTAN_LIST = SERVER_URL + "videos/Video_control/sankirtan_home";
    public static final String SEARCH_SANKIRTAN_LIST = SERVER_URL + "videos/video_control/search_sankirtan";
    public static final String VIEW_ALL_VIDEOS_SANKIRTAN = SERVER_URL + "videos/Video_control/view_all_sankirtan_by_category";
    public static final String BHAJAN_LIKE = SERVER_URL + "bhajan/bhajan/like_bhajan";
    public static final String BHAJAN_UNLIKE = SERVER_URL + "bhajan/bhajan/unlike_bhajan";
    public static final String BHAJAN_SEARCH = SERVER_URL + "bhajan/bhajan/search_bhajan";
    public static final String BHAJAN_GET_ARTIST = SERVER_URL + "bhajan/bhajan/get_bhajan_of_artist";
    public static final String BHAJAN_GET_GOD = SERVER_URL + "bhajan/bhajan/get_bhajan_of_god";
    public static final String BHAJAN_RELATED = SERVER_URL + "videos/Video_control/get_related_bhajan";
    public static final String NEWS_RELATED_VIEWS = SERVER_URL + "news/News/view_news";


    public static final String BASE_URL_VIDEO = "http://ec2-13-126-56-231.ap-south-1.compute.amazonaws.com:1935";
    public static final String NEW_URL_VIDEO = "http://52.66.150.91:1935/vods3";
    public static final String TNC_API = SERVER_URL + "settings/Settings/get_terms_conditions";
    public static final String PRIVACY_API = SERVER_URL + "settings/Settings/get_privacy_policy";
    public static final String NOTIFICATION_STATUS = SERVER_URL + "user/Registration/change_notification_status";
    public static final String NOTIFICATION_CLEAR = SERVER_URL + "Notification/clear_notification_history";

    public static final String GET_SEASON_BY_CATEGORY = SERVER_URL + "videos/premium_video/get_seasons_by_category";
    public static final String COMPLETE_TRANSACTION = SERVER_URL + "transaction/complete_transaction";

    public static final String INITIALIZE_TRANSACTION = SERVER_URL + "transaction/initialize_transaction";

    //public static final String GET_ADVERTISEMENT = SERVER_URL + "advertisement/get_advertisement";
    public static final String GET_ADVERTISEMENT = SERVER_URL + "user/user_meta/get_subscription_url";
    public static final String GET_COUPON = SERVER_URL + "coupon/get_coupon";
    public static final String GET_PROMO_CODE = SERVER_URL + "promocode/validate_promocode";

    public static final String CONTINUE_WATCHING = SERVER_URL + "videos/video_control/continue_watching";


    public static final String BHAJAN_COUNT = SERVER_URL + "bhajan/bhajan/update_bhajan_play_count";
    public static final String ADD_TO_PLAYLIST = SERVER_URL + "videos/video_control/playlist";
    public static final String GET_PLAYLIST = SERVER_URL + "videos/video_control/get_playlist";

    public static final String GET_ADVERTISEMENT_ADS = SERVER_URL + "advertisement/get_app_advertisement";

    public static final String UPDATE_LIVE_USERS = SERVER_URL + "live_users/update_live_users";
    public static final String GET_LIVE_USERS = SERVER_URL + "live_users/get_channel_live_users";
    public static final String UPDATE_APP_ADVERTISEMENT_COUNTER = SERVER_URL + "advertisement/update_app_advertisement_counter";
    //"https://app.sanskargroup.in/sanskar_development/data_model/"


    //public static final String TV_GUIDE = SERVER_URL + "channel/TvGuide/getTvGuide";
    public static final String TV_GUIDE = SERVER_URL + "tv-guide";


    //////////////////////////////////////////////////////////////////////////////////////////////////////
    public static final String COUNTRY_WISE_USER_PLAY_HISTORY = SERVER_URL + "country_wise_user_video_play_history";

    ///////////////////////////////////////SUBSCRIPTION//////////////////////////////////////////////////
    public static final String GET_SUBSCRIPTION_PLAN = SERVER_URL + "user/user_meta/get_user_premium_plan_details";

    ///////////////////////////////////////////////WALLPAPER////////////////////////////////////////////////

    public static final String GET_WALLPAPER_MENU = SERVER_URL + "wallpaper/wallpaper/get_wallpaper_menu";

    ///////////////////////////////////////////////META DATA////////////////////////////////////////
    public static final String GET_PREMIUM_VIDEO_META_DATA = SERVER_URL + "video_meta/get_video_meta_data";

    public static final String GET_QR_LOGIN_USER_DETAIL = SERVER_URL+ "user/qr_login/get_user_data";

    public static final String GUEST_USER_LOGIN = SERVER_URL+ "user/Registration/guest_login";



}
