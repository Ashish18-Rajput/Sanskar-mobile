package com.sanskar.tv.jwPlayer;

/*import com.brightcove.player.event.Event;
import com.brightcove.player.event.EventListener;
import com.brightcove.player.event.EventType;
import com.brightcove.player.mediacontroller.BrightcoveMediaController;
import com.brightcove.player.model.Video;
import com.brightcove.player.view.BrightcoveExoPlayerVideoView;*/


public class LiveStreamingFragment{}
        //extends Fragment implements NetworkCall.MyNetworkCallBack, VideoPlayerEvents.OnFullscreenListener,View.OnClickListener{
//
//    Activity activity;
//
//
//
//
//
//    public static final int TOTAL_ITEM_TO_LOAD = 10;
//    public SignupResponse signupResponse;
//
//    public  static Boolean COME_FROM_LIVETV=false;
//    public ArrayList<Channel> homeChannelList;
//    public List<Channel> ChannelLists = new ArrayList<>();
//    List<String> keyList = new ArrayList<String>();
//    public static String VIEWlike;
//    public static String Is_like_no;
//    public  static String video_url;
//    HomeActivityy homeActivityy;
//    BrightcoveExoPlayerVideoView playerView;
//    ImageView videoImage, playIconIV;
//    RelativeLayout like_channel, Comment_channel;
//    ImageView like_channel_button, Comment;
//    TextView comment_number_watch_channel, like_number_watch_channel;
//    LinearLayout ll_all_option;
//    LinearLayout ll_option;
//    Video video_channel_wise;
//    static String messageCount;
//    Long ccount;
//    KeepScreenOnHandler keepScreenOnHandler;
//    Channel channel;
//    public  static  Boolean deletechat=true;
//    public  static  String checkdeletechat="0";
//    public  static  Boolean checkfordeletechat=true;
//    TextView view_user_watch_channel;
//
//    String url;
//    Boolean RotationFull = false;
//    TextView name, description, date;
//    RecyclerView fragment_home_rv_livetv;
//    ImageView back;
//    ChildEventListener childEventListener;
//    static ChildEventListener showchildEventListner;
//    Videos video;
//    Video video1;
//    RecyclerView relatedVideosList;
//    ViewAllAdapter adapter;
//    RecyclerView.LayoutManager layoutManager;
//    RelativeLayout share;
//    TextView viewNumber, likeNumber;
//    SwipeRefreshLayout swipeRefreshLayout;
//    EditText comment;
//    static RecyclerView rc_chat;
//    String channelDatabase, channelmain,channel_id,channelUser, Particularchannel;
//    RelativeLayout ll_chat;
//
//    TextView postComment;
//    ImageView share_iv, chat;
//    ProgressBar commentProgress, progressvideo;
//    ArrayList<Long> list = new ArrayList<>();
//    List<Message> messageArrayList = new ArrayList<>();
//    ImageView profile_iv;
//    ImageView tv_iv;
//    String livevideourl;
//    String fromWhere;
//    String id;
//    String from;
//    PlaylistItem item;
//    String url1;
//    String url2;
//    CardView cardView;
//    DatabaseReference mDatabaseReference, mRootReference,mDatabaseReference_variable;
//    String push_id = "",push_id2="";
//    String chat_user_ref;
//    EditText et_chat;
//    ImageView iv_chat;
//    private HomeActivityy activitty;
//    private RelativeLayout noDataFound;
//    private RelativeLayout headerrel;
//    private int mPage = 1;
//    private ArrayList<Videos> videoResponses;
//    private NetworkCall networkCall;
//    private LinearLayout nonFullScreenLayout;
//    private ImageView like;
//    private TextView commentNumbers;
//    private Videos[] videos;
//    private Videos[] videosArray;
//    private Videos[] vd = new Videos[]{};
//    private int pos;
//    private Context context;
//    private CircleImageView profileImg;
//    private RelativeLayout container;
//    private boolean isLike = false;
//    private boolean isComment = false;
//    public  boolean checkForShowUser = true;
//    public  boolean IsSHOWuser = true;
//    private RelativeLayout layout, toolbar, fragment_watch_video_comment_layout;
//    private long enqueue;
//    private DownloadManager dm;
//    private DownloadManager downloadManager;
//    private long refid;
//    private Uri Download_Uri;
//    private ImageView download;
//    private long downloadID;
//    private HomeActivityy activityy;
//    private FirebaseAuth mAuth;
//    private LinearLayoutManager mLinearLayoutManager;
//    private ChatAdapter chatAdapter;
//    private int itemPos = 0;
//    private String mLastKey = "";
//    private String mPrevKey = "";
//    private LiveStreamOnTopLiveChannelthroghFragmentAdapter liveStreamOnTopLiveChannelAdapter;
//    private int mCurrentPage = 5;
//    Query messageQuery,showmessagequery;
//
//
//    static  String current_user_ref;
//    static  String channelID;
//    static  String channel_database;
//    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            //Fetching the download id received with the broadcast
//            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
//            //Checking if the received broadcast is for our enqueued download by matching download id
//            if (downloadID == id) {
//                Toast.makeText(activity, "Download Completed", Toast.LENGTH_SHORT).show();
//            }
//        }
//    };
//
//    public static void sendLink(Activity activity, String subject, String msg, String msgHtml) {
//        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//        sharingIntent.setType("text/plain");
//        sharingIntent.putExtra(Intent.EXTRA_TEXT, msg);
//        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
//        sharingIntent.putExtra(Intent.EXTRA_HTML_TEXT, msgHtml);
//        if (sharingIntent.resolveActivity(activity.getPackageManager()) != null) {
//            activity.startActivity(sharingIntent);
//        }
//    }
//
//
//    public LiveStreamingFragment() {
//        // Required empty public constructor
//    }
//
//
//    public static LiveStreamingFragment newInstance(String param1, String param2) {
//        LiveStreamingFragment fragment = new LiveStreamingFragment();
//
//
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        activity=getActivity();
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_live_streaming, container, false);
//    }
//
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//
//
//        COME_FROM_LIVETV=true;
//        if(AudioPlayerService.mediaPlayer!=null) {
//            if (AudioPlayerService.mediaPlayer.isPlaying()) {
//                AudioPlayerService.mediaPlayer.pause();
//            } else {
//            }
//        }
//
//        if (downloadmediaplayer != null) {
//            if (downloadmediaplayer.isPlaying()){
//                downloadmediaplayer.pause();}}
//        else{
//
//        }
//
//        if(playlistmediaplayer!=null){
//            if (playlistmediaplayer.isPlaying()){
//                playlistmediaplayer.pause();}}
//        else{
//
//        }
//
//        homeChannelList = new ArrayList<>();
//        ChannelLists = channellist;
//        signupResponse = PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN,
//                SignupResponse.class);
//
//        networkCall = new NetworkCall(LiveStreamingFragment.this, activity);
//        networkCall.NetworkAPICall(API.HOME_PAGE_VIDEOS, true);
//
//
//        //      activitty = ((HomeActivityy) context);
//        mAuth = FirebaseAuth.getInstance();
//        signupResponse = PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN, SignupResponse.class);
//        et_chat = view.findViewById(R.id.et_chat);
//        iv_chat = view.findViewById(R.id.iv_chat);
//        rc_chat = view.findViewById(R.id.rc_chat);
//        fragment_home_rv_livetv =view.findViewById(R.id.fragment_home_rv_livetv);
//        videoImage =view.findViewById(R.id.videoImage);
//        playIconIV = view.findViewById(R.id.playIconIV);
//        like_channel_button = view.findViewById(R.id.like);
//        view_user_watch_channel =view.findViewById(R.id.view_user_watch_channel);
//        like_channel_button.setTag(false);
//        like_channel = view.findViewById(R.id.like_channel);
//        Comment_channel = view.findViewById(R.id.Comment_channel);
//        //   like_channel_button= view.view.view.findViewById(R.id.like);
//        Comment = view.findViewById(R.id.Comment);
//        comment_number_watch_channel =view.findViewById(R.id.comment_number_watch_channel);
//        like_number_watch_channel =view.findViewById(R.id.like_number_watch_channel);
//        like_channel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (isConnectingToInternet(activity)) {
//                    if (!Boolean.parseBoolean(like_channel_button.getTag().toString())) {
//                        networkCall.NetworkAPICall(API.LIKE_CHANNEL, false);
//                    } else {
//                        networkCall.NetworkAPICall(API.UNLIKE_CHANNEL, false);
//                    }
//                } else {
//                    ToastUtil.showDialogBox(activity, Networkconstants.ERR_NETWORK_TIMEOUT);
//                }
//            }
//        });
//
//        liveStreamOnTopLiveChannelAdapter = new LiveStreamOnTopLiveChannelthroghFragmentAdapter(homeChannelList, activity, LiveStreamingFragment.this);
//
//        LinearLayoutManager ontoplivetvLM = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
//        fragment_home_rv_livetv.setLayoutManager(ontoplivetvLM);
//        fragment_home_rv_livetv.setAdapter(liveStreamOnTopLiveChannelAdapter);
//
//        Intent intent = getIntent();
//        Uri data = intent.getData();
//        livevideourl = getIntent().getStringExtra("livevideourl");
//        id = getIntent().getStringExtra("channel_id");
//        VIEWlike = getIntent().getStringExtra("VIEWlike");
//        Is_like_no = getIntent().getStringExtra("Is_like_no");
//        setChannelRelatedData();
//
//
///*
//        mRootReference = FirebaseDatabase.getInstance().getReference();
//*/
//
//        getChannelChat(id);
//
//
//
//        if (getIntent().getStringExtra("from") != null) {
//            from = getIntent().getStringExtra("from");
//        } else {
//            from = "";
//        }
//
//      //  getBundleData();
//        initView(view);
//
//
//        Download_Uri = Uri.parse(API.BASE_URL_VIDEO + "/_definst_/amazons3/mp4:bhaktiappproduction/videos/");
//
//        String baseUrl = API.BASE_URL_VIDEO + "/_definst_/amazons3/mp4:bhaktiappproduction/videos/";
//        if (video != null) {
//            if (video.getVideo_url().contains("//")) {
//                url = API.BASE_URL_VIDEO + "/_definst_/amazons3/mp4:bhaktiappproduction/videos/SampleVideo_1280x720_1mb.mp4/playlist.m3u8";
//            } else {
//                url = baseUrl + video.getVideo_url() + "/playlist.m3u8";
//            }
//
//
//            url1 = video.getVideo_url();
//
//
//            url2 = video.getVideo_url();
//            String[] urltosppend = url2.split("com");
//
//            url2 = "http://52.204.183.54:1935/vods3/" + "_definst_/mp4:amazons3/bhaktiappproduction" +
//                    urltosppend[1] + "/playlist.m3u8";
//
//
//        }
//
//
//        http:
//
//        playerView = view.view.findViewById(R.id.playerView);
//
//        if (from.equals("guru")) {
//            item = new PlaylistItem(url1);
//            //  nonFullScreenLayout.setVisibility(View.VISIBLE);
//            //cardView.setVisibility(View.VISIBLE);
//        } else if (from.equals("livetv")) {
//            //   nonFullScreenLayout.setVisibility(View.GONE);
//            //  cardView.setVisibility(View.GONE);
//            item = new PlaylistItem(livevideourl);//"https://s3.ap-south-1.amazonaws.com/dams-apps-production/12BD5F37-6AFF-4C45-9F98-C6925CEC8DB3-1458-000003B4C7C6745B.mp4");//("http://techslides.com/demos/sample-videos/small.mp4");
//            video_channel_wise = Video.createVideo(livevideourl);
//        }
//
////       keepScreenOnHandler = new KeepScreenOnHandler(playerView, getWindow());
////       // playerView.addOnFullscreenListener(this);
////        playerView.load(item);
////        playerView.play();
//
//
//
//        /*  ((ExoPlayerVideoDisplayComponent) videoView.getVideoDisplay()).setPeakBitrate(bitRate);*/
//        livestreaming_code(video_channel_wise);
//
//
//
//
//}
//
//    private void setChannelRelatedData() {
//        if (VIEWlike.equals("0")) {
//            like_number_watch_channel.setText("0 like");
//        } else if (VIEWlike.equals("1")) {
//            like_number_watch_channel.setText(VIEWlike + " like");
//        } else {
//            like_number_watch_channel.setText(VIEWlike + " likes");
//        }
//
//      /*  if (VIEWlike.equals("0")) {
//            like_number_watch_channel.setText("no view");
//        } else if (VIEWlike.equals("1")) {
//            like_number_watch_channel.setText(VIEWlike + " view");
//        } else {
//            like_number_watch_channel.setText(VIEWlike + " views");
//        }*/
//
//        if (Is_like_no.equals("1")) {
//            like_channel_button.setImageResource(R.mipmap.audio_liked);
//            like_channel_button.setTag(true);
//        } else if (
//                Is_like_no.equals("0")) {
//            like_channel_button.setImageResource(R.mipmap.white_like);
//            like_channel_button.setTag(false);
//        }
//    }
//
//
//    private void getChannelChat(String id) {
//        if(mRootReference!=null)
//        {
//            mRootReference=null;
//        }
//        mRootReference = FirebaseDatabase.getInstance().getReference();
//
//        if (id.equals("19")) {
//            channelDatabase = "sanskarTV";
//            channelmain = "sanskarliveChannels/sanskarTV";
//            channelUser = "sanskarliveUsers/sanskarTV";
//            //  channelvariable = "sanskarliveChannels/sanskarTV/count";
//            channel_id=id;
//            MessageDatabase(channelDatabase, channelmain,channel_id);
//            //ShowUsers(channelUser);
//
//
//
//        } else if (id.equals("20")) {
//            channelDatabase = "satsangTV";
//            channelmain = "sanskarliveChannels/satsangTV";
//            channelUser = "sanskarliveUsers/satsangTV";
//            channel_id=id;
//            //  ShowUsers(channelUser);
//
//            MessageDatabase(channelDatabase, channelmain, channel_id);
//            // ShowUsers(channelUser);
//
//        } else if (id.equals("21")) {
//            channelDatabase = "shubhTV";
//            channelmain = "sanskarliveChannels/shubhTV";
//            channelUser = "sanskarliveUsers/shubhTV";
//            channel_id=id;
//            //  ShowUsers(channelUser);
//            MessageDatabase(channelDatabase, channelmain, channel_id);
//            //   ShowUsers(channelUser);
//
//
//        } else if (id.equals("22")) {
//            channelDatabase = "shubhCinemaTV";
//            channelmain = "sanskarliveChannels/shubhCinemaTV";
//            channelUser = "sanskarliveUsers/shubhCinemaTV";
//            channel_id=id;
//            //  ShowUsers(channelUser);
//            MessageDatabase(channelDatabase, channelmain, channel_id);
//            //  ShowUsers(channelUser);
//
//
//        } else if (id.equals("23")) {
//            channelDatabase = "sanskarWebTV";
//            channelmain = "sanskarliveChannels/sanskarWebTV";
//            channelUser = "sanskarliveUsers/sanskarWebTV";
//            channel_id=id;
//            //  ShowUsers(channelUser);
//            MessageDatabase(channelDatabase, channelmain, channel_id);
//            // ShowUsers(channelUser);
//
//
//        } else if (id.equals("24")) {
//            channelDatabase = "satsangWebTV";
//            channelmain = "sanskarliveChannels/satsangWebTV";
//            channelUser = "sanskarliveUsers/satsangWebTV";
//            channel_id=id;
//            //  ShowUsers(channelUser);
//            MessageDatabase(channelDatabase, channelmain, channel_id);
//            //  ShowUsers(channelUser);
//
//
//        } else if (id.equals("25")) {
//            channelDatabase = "sanskarUK";
//            channelmain = "sanskarliveChannels/sanskarUK";
//            channelUser = "sanskarliveUsers/satsangWebTV";
//            channel_id=id;
//            //    ShowUsers(channelUser);
//            MessageDatabase(channelDatabase, channelmain, channel_id);
//            // ShowUsers(channelUser);
//
//
//        } else if (id.equals("26")) {
//            channelDatabase = "sanskarUSA";
//            channelmain = "sanskarliveChannels/sanskarUSA";
//            channelUser = "sanskarliveUsers/sanskarUSA";
//            channel_id=id;
//
//            //   ShowUsers(channelUser);
//            MessageDatabase(channelDatabase, channelmain, channel_id);
//            //  ShowUsers(channelUser);
//
//        } else if (id.equals("27")) {
//            channelDatabase = "SanskarTvRadio";
//            channelmain = "sanskarliveChannels/SanskarTvRadio";
//            channelUser = "sanskarliveUsers/SanskarTvRadio";
//            channel_id=id;
//
//            //      ShowUsers(channelUser);
//            MessageDatabase(channelDatabase, channelmain, channel_id);
//            //  ShowUsers(channelUser);
//
//        } else {
//            channelDatabase = "default";
//            channelmain = "sanskarliveChannels/default";
//            channelmain = "sanskarliveUsers/default";
//            channel_id=id;
//            //     ShowUsers(channelUser);
//            MessageDatabase(channelDatabase, channelmain, channel_id);
//            //ShowUsers(channelUser);
//
//
//        }
//    }
//
//    //  private void UserOnline(String channelDatabase) {
//    private void UserOnline(String channelDatabase, String channelmain, String channel_id){
//        //   mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("sanskarliveUsers/sanskarTV");
//
//        Map messageMap = new HashMap();
//
//        messageMap.put("count", ccount);
//
//
//        Log.d("UserOnline","checkinguser1");
//        Log.d("ccc",String.valueOf(ccount));
//
//
//        mDatabaseReference.updateChildren(messageMap, new DatabaseReference.CompletionListener() {
//
//            @Override
//            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                if (databaseError != null) {
//                    Log.e("CHAT_ACTIVITY", "Cannot add message to database");
//                } else {
//                    // Log.d("sss",String.valueOf(ccount));
//                    getLiveView(ccount);
//
//                    Toast.makeText(LiveStreamJWActivity.this, "Your comment posted successfully", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//
//
//
//
//
//    }
//
//
//    @Override
//    public Builders.Any.B getAPIB(String apitype) {
//        Builders.Any.B ion = null;
//        /* if (API_NAME.equals(API.RELATED_VIDEOS)) {
//            ion = (Builders.Any.B) Ion.with(this).load(API_NAME)
//                    .setMultipartParameter("user_id", signupResponse.getId())
//                    .setMultipartParameter("video_id", video.getId())
//                    .setMultipartParameter("limit", String.valueOf(10))
//                    .setMultipartParameter("page_no", String.valueOf(mPage));
//        }*/ /*else if (API_NAME.equals(API.LIKE_VIDEO) || API_NAME.equals(API.UNLIKE_VIDEO)) {
//            ion = (Builders.Any.B) Ion.with(this).load(API_NAME)
//                    .setMultipartParameter("user_id", signupResponse.getId())
//                    .setMultipartParameter("video_id", video.getId());
//        }*/
//       /* else if (API_NAME.equals(API.POST_COMMENT)) {
//            ion = (Builders.Any.B) Ion.with(this).load(API_NAME)
//                    .setMultipartParameter("user_id", signupResponse.getId())
//                    .setMultipartParameter("video_id", video.getId())
//                    .setMultipartParameter("comment", comment.getText().toString());
//        }*/
//
//
//        if (API_NAME.equals(API.HOME_PAGE_VIDEOS)) {
//            ion = (Builders.Any.B) Ion.with(this).load(API_NAME)
//                    .setMultipartParameter("user_id", signupResponse.getId());
//        } else if (API_NAME.equals(API.LIKE_CHANNEL) || API_NAME.equals(API.UNLIKE_CHANNEL)) {
//            ion = (Builders.Any.B) Ion.with(this).load(API_NAME)
//                    .setMultipartParameter("user_id", signupResponse.getId())
//                    .setMultipartParameter("channel_id", id);
//        }
//        else if(API_NAME.equals(API.INFORM_DELETE_CHAT)){
//            ion = (Builders.Any.B) Ion.with(this).load(API_NAME)
//                    .setMultipartParameter("node_id",deleted_node_id);
//
//        }
//        return ion;
//    }
//
//    @Override
//    public void SuccessCallBack(JSONObject result, String apitype) throws JSONException {
//        if (result.optBoolean("status")) {
//            if (API_NAME.equals(API.LIKE_CHANNEL)) {
//                this.homeChannelList.clear();
//
//
//                LikeDislike(true);
//                getHomeData();
//                // notifyAdapters();
//
//                // liveStreamOnTopLiveChannelAdapter.notifyDataSetChanged();
//
//
//            } else if (API_NAME.equals(API.UNLIKE_CHANNEL)) {
//                this.homeChannelList.clear();
//
//                LikeDislike(false);
//                getHomeData();
//                //notifyAdapters();
//                // liveStreamOnTopLiveChannelAdapter.notifyDataSetChanged();
//
//
//
//            }
//            else if(API_NAME.equals(API.HOME_PAGE_VIDEOS)){
//                setHomeData(result);
//                notifyAdapters();
//            }
//            else if(API_NAME.equals(API.INFORM_DELETE_CHAT)){
//                Toast.makeText(this, " you delete chat", Toast.LENGTH_SHORT).show();
//
//
//            }
//
//        }
//    }
//
//    @Override
//    public void ErrorCallBack(String jsonstring, String apitype) {
//
//    }
//
//
//
//
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.d("resume", "check rusem");
//        deletechat=true;
//        //  checkdeletechat="0";*/
//        checkfordeletechat=true;
//        checkForShowUser=true;
//        if (playerView != null){
//            playerView.start();
//            deletechat=true;
//            //  checkdeletechat="0";*/
//            checkfordeletechat=true;
//            checkForShowUser=true;
//
//            Log.d("resume", "check rusem");
//        }
//    }
//
//    @Override
//    public void onPause() {
//        if (playerView != null) {
//
//
//            playerView.pause();
//            /*if(checkForShowUser==true){
//                checkForShowUser=false;
//                ShowUsers();
//            }*/
//          /*  if(checkForShowUser==true){
//                ShowUsers()}*/
//
//            Log.d("pausemm", "check pause player");
//
//        }
//        Log.d("pause", "check pause");
//        deletechat=true;
//        //  checkdeletechat="0";*/
//        checkfordeletechat=true;
//        super.onPause();
//    }
//
//
//    @Override
//    public void onDestroy() {
//
//        super.onDestroy();
//        if (playerView != null) {
//            playerView.stopPlayback();
//            Log.d("destroy", "onDestroy1");
//
//        }
//       /* if(checkForShowUser==true){
//                checkForShowUser=false;
//                ShowUsers(channelUser);
//            }*/
//        Log.d("destroy", "onDestroy2");
//        unregisterReceiver(onDownloadComplete);
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (getSupportFragmentManager().findFragmentById(R.id.container) instanceof ViewCommentsFragment) {
//            super.onBackPressed();
//        } else {
//            if (isLike || isComment) {
//                isLike = true;
//            }
//            Intent intent = new Intent();
//            intent.putExtra("Is_like_no", Is_like_no);
//            intent.putExtra("VIEWlike", VIEWlike);
//            setResult(RESULT_OK, intent);
//           /* Bundle bundle = new Bundle();
//            bundle.putString("Is_like_no", Is_like_no);
//            bundle.putString("VIEWlike", VIEWlike);
//            f.setArguments(bundle);*/
//            finish();
//            super.onBackPressed();
//        }
//    }
//
//
//
//
//
//
//    public void getHomeData() {
//        if (isConnectingToInternet(this)) {
//            networkCall.NetworkAPICall(API.HOME_PAGE_VIDEOS, true);
//        } else {
//            swipeRefreshLayout.setRefreshing(false);
//            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
//        }
//    }
//
//
//    private void notifyAdapters() {
//        liveStreamOnTopLiveChannelAdapter.notifyDataSetChanged();
//    }
//
//
//    private void setHomeData(JSONObject result) {
//
//        JSONArray jsonArrayChannel = result.optJSONArray("channel");
//
//
//
//        // playPromotion();
//
//        if (jsonArrayChannel != null) {
//            for (int i = 0; i < jsonArrayChannel.length(); i++) {
//                channel = new Gson().fromJson(jsonArrayChannel.opt(i).toString(), Channel.class);
//                homeChannelList.add(channel);
//
//            }
//            liveStreamOnTopLiveChannelAdapter = new LiveStreamOnTopLiveChannelAdapter(homeChannelList, this, LiveStreamJWActivity.this);
//
//            LinearLayoutManager ontoplivetvLM = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
//            fragment_home_rv_livetv.setLayoutManager(ontoplivetvLM);
//            fragment_home_rv_livetv.setAdapter(liveStreamOnTopLiveChannelAdapter);
//
//        }
//
//
//    }
//
//
//    private void LikeDislike(boolean b) {
//
//       /* if (!TextUtils.isEmpty(Is_like_no) && Is_like_no != null) {
//            if (Is_like_no.equals("1")) {
//                like_channel_button.setImageResource(R.mipmap.audio_liked);
//                isLiked = true;
//                like_channel_button.setTag(true);
//            } else {
//                like_channel_button.setImageResource(R.mipmap.audio_like);
//                isLiked = false;
//                like_channel_button.setTag(false);
//            }
//        }
//
//
//
//        if (Likes_no.equals("0")) {
//            if (isLiked) {
//                like_number_watch_channel.setText("1 Like");
//            } else {
//                like_number_watch_channel.setText("0 Likes");
//            }
//
//        } else if (Likes_no.equals("1")) {
//            if (isLiked) {
//                like_number_watch_channel.setText(((Integer.parseInt(Likes_no) + 1) + " Likes").toString());
//            } else {
//                like_number_watch_channel.setText("1 Like");
//            }
//
//        } else {
//            if (isLiked) {
//                like_number_watch_channel.setText(((Integer.parseInt(Likes_no) + 1) + " Likes").toString());
//            } else {
//                like_number_watch_channel.setText((Likes_no + " Likes").toString());
//            }
//        }
//*/
//
//
//        like_channel_button.setTag(b);
//        int likenum = 0;
//        if (like_number_watch_channel.getText().toString().equalsIgnoreCase("0 like")) {
//            likenum = 0;
//        } else {
//            likenum = Integer.parseInt(like_number_watch_channel.getText().toString().split(" ")[0]);
//        }
//        if (b) {
//            VIEWlike=String.valueOf(Integer.parseInt(VIEWlike)+1);
//            if (likenum == 0) {
//                like_number_watch_channel.setText(likenum + 1 + " like");
//            } else {
//                like_number_watch_channel.setText(likenum + 1 + " likes");
//
//            }
//            like_channel_button.setImageResource(R.mipmap.audio_liked);
////            video.setLikes(String.valueOf(likenum + 1));
////            video.setIs_like("1");
//        } else {
//            VIEWlike=String.valueOf(Integer.parseInt(VIEWlike)-1);
//
//            if (likenum == 1) {
//                like_number_watch_channel.setText("0 like");
//            } else if (likenum == 2) {
//                like_number_watch_channel.setText(likenum - 1 + " like");
//            } else {
//                like_number_watch_channel.setText(likenum - 1 + " likes");
//            }
//            like_channel_button.setImageResource(R.mipmap.white_like);
////            video.setLikes(String.valueOf(likenum - 1));
////            video.setIs_like("0");
//        }
//        //  isLike = true;
//
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//
//            case R.id.share_watch_video:
//                // shareTextUrl();
////                //  shareAppUrl();
////                try {
////                    shareAppUrl();
////                } catch (Throwable throwable) {
////                    throwable.printStackTrace();
////                }
//                break;
//            case R.id.share_iv:
//                DynamicLinking();
//
//
//
//                break;
//
//        }
//    }
//
//    @Override
//    public void onFullscreen(boolean b) {
//
//    }
//
//
//
//    private void DynamicLinking() {
//        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
//                .setLink(Uri.parse("https://www.sanskar.tv/?postId=" + livevideourl + "&id=" + id))
//                .setDynamicLinkDomain("sanskar.page.link")
//                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
//                .buildShortDynamicLink()
//                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
//                    @Override
//                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
//                        if (task.isSuccessful()) {
//                            // Short link created
//                            Uri shortLink = task.getResult().getShortLink();
//                            String msgHtml = String.format("<p>Let's see live video on sanskar tv app."
//                                    + "<a href=\"%s\">Click Here</a>!</p>", shortLink.toString());
//
//                            String msg = "Let's see live video.. Click on Link : " + shortLink.toString();
//
//                            //sendLink(msg, msgHtml, this, bitmap);
//                            // sendLink(activity, msg, msgHtml);
//
//
//                            sendLink(LiveStreamingFragment.this, "sanskar", msg,
//                                    msgHtml
//                            );
//
//                        } else {
//                            Log.d("TASK_EXCEPTION", task.getException().toString());
//                            Toast.makeText(context, "Article link could not be generated please try again!", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
//
//    private void shareApp() {
//        String playStoreLink = "https://play.google.com/store/apps/details?id=" + activity.getPackageName();
//        Uri imageUri = Uri.parse("android.resource://" + activity.getPackageName() + "/mipmap/" + "ic_launcher_round");
//        Intent i = new Intent(android.content.Intent.ACTION_SEND);
//        i.setType("text/plain");
//        //i.setType("image/*");
//
//        i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Sanskar App");
//        i.putExtra(android.content.Intent.EXTRA_TEXT, "Download Sanskar App " + playStoreLink);
//        i.putExtra(Intent.EXTRA_STREAM, imageUri);
//        startActivity(Intent.createChooser(i, "Share via"));
//    }
//
//    private void getBundleData() {
//        if (getIntent().getExtras() != null) {
//
//            Bundle bundle = getIntent().getExtras();
//            if (bundle.containsKey("video_data")) {
//                videosArray = (Videos[]) bundle.getSerializable("video_data");
//                pos = bundle.getInt("position");
//                video = videosArray[pos];
//            } else if (bundle.containsKey("video_data_guru")) {
//                video = (Videos) bundle.getSerializable("video_data_guru");
//            }
//        }
//    }
//
//
//    @SuppressLint("WrongViewCast")
//    private void initView(View view) {
//        headerrel = view.findViewById(R.id.headerrel);
//        ll_all_option = view.findViewById(R.id.ll_all_option);
//        ll_option = view.findViewById(R.id.ll_option);
//        rc_chat = view.findViewById(R.id.rc_chat);
//        ll_chat = view.findViewById(R.id.ll_chat);
//        fragment_watch_video_comment_layout = view.findViewById(R.id.fragment_watch_video_comment_layout);
//        progressvideo = view.findViewById(R.id.progressvideo);
//        container = view.findViewById(R.id.container);
//        layout = view.findViewById(R.id.layout);
//        share_iv = view.findViewById(R.id.share_iv);
//        fragment_home_rv_livetv = view.findViewById(R.id.fragment_home_rv_livetv);
//        share_iv.setOnClickListener(this);
//        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
//        videoResponses = new ArrayList<>();
//       /* networkCall = new NetworkCall(this, LiveStreamJWActivity.this);
//        networkCall.NetworkAPICall(API.GET_BHAJANS, true);
//*/
//
//    }
//
//    public void handleToolbar() {
//        toolbar.setVisibility(View.VISIBLE);
//
//    }
//    private void MessageDatabase(String channelDatabase, String channelmain, String channel_id) {
//
//
//        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("sanskarliveChannels/");
//        // mDatabaseReference_variable = FirebaseDatabase.getInstance().getReference().child("sanskarliveChannels/").child(channelmain);
//
//        current_user_ref = channelmain;
//        channelID=channel_id;
//        channel_database=channelDatabase;
//   /* DatabaseReference msg=mRootReference.child(channelmain).child("sanskarvar").push();
//        push_id2=msg.getKey();
//        Map messageUserMap2 = new HashMap();
//        ccount=ccount++;
//        messageUserMap2.put("sanskarvar" , ccount);
//
//        Map messageUserMap3 = new HashMap();
//
//        messageUserMap3.put( "sanskarvar"+ "/" + push_id2, messageUserMap2);*/
//        loadmessage(channelmain);
//        // loadmessage();
//        //  DatabaseReference messageRef2=null;
//        //messageRef2=mRootReference.child(channelmain).child("sanskar_varible");
//        /* messageRef2.push();*/
//
//       /* DatabaseReference msg=mRootReference.child(channelmain).child("sanskarvar").push();
//        push_id2=msg.getKey();
//        Map messageUserMap2 = new HashMap();
//        ccount=ccount++;
//        messageUserMap2.put("sanskarvar" , ccount);
//
//        Map messageUserMap3 = new HashMap();
//
//        messageUserMap3.put( "sanskarvar"+ "/" + push_id2, messageUserMap2);*/
//        iv_chat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                checkfordeletechat=true;
//                deletechat = true;
//
//                String message = et_chat.getText().toString();
//                if(signupResponse.getUsername()==null ||signupResponse.getUsername().isEmpty())
//                {
//
//                    Toast.makeText(LiveStreamJWActivity.this,"Please Update Your Profile",Toast.LENGTH_SHORT).show();
//
//                }
//                else {
//                    if (!(message == null || message.equals(""))) {
//
//                        DatabaseReference user_message_push = mRootReference.child(channelmain).push();
//
//                        push_id = user_message_push.getKey();
//                        chat_user_ref = channelDatabase;
//                        Map messageMap = new HashMap();
//                        messageMap.put("message", message.replaceFirst("\\s++$", ""));
//                        messageMap.put("seen", false);
//                        messageMap.put("type", "text");
//                        messageMap.put("time", ServerValue.TIMESTAMP);
//                        messageMap.put("from", signupResponse.getId());
//                        messageMap.put("status", "1");
//                        messageMap.put("name", signupResponse.getUsername());
//                        messageMap.put("img", signupResponse.getProfile_picture());
//                  /*  messageMap.put("id", channelmain);
//                    messageMap.put("channel_id", channel_id);*/
//                        messageMap.put("pushid", push_id);
//                        /*   messageMap.put("channel_name",channelDatabase);*/
//                        Map messageUserMap = new HashMap();
//
//                        messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
//
//                        et_chat.setText("");
//                        mRootReference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
//
//                            @Override
//                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                                if (databaseError != null) {
//                                    Log.e("CHAT_ACTIVITY", "Cannot add message to database");
//                                } else {
//                                    Toast.makeText(LiveStreamJWActivity.this, "Your comment posted successfully", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//
//
//                    }
//                }}
//        });
//        messageCount=String.valueOf(messageArrayList.size());
//
//        getcomment(messageCount);
//        chatAdapter = new ChatAdapter(this, messageArrayList);
//
//        mLinearLayoutManager = new LinearLayoutManager(LiveStreamJWActivity.this);
//        mLinearLayoutManager.setStackFromEnd(true);
//        rc_chat.setHasFixedSize(true);
//        rc_chat.setLayoutManager(mLinearLayoutManager);
//        rc_chat.setAdapter(chatAdapter);
//
//    }
//
//    private void loadmessage(String channelmain) {
//
//        DatabaseReference messageRef = null;
//        DatabaseReference messageRef2 = null;
//
//        messageRef = mRootReference.child(channelmain);
//
//
//        //   Query messageQuery = messageRef.limitToLast(mCurrentPage * TOTAL_ITEM_TO_LOAD);
//        messageQuery = messageRef.limitToLast(mCurrentPage * TOTAL_ITEM_TO_LOAD);
//        childEventListener=new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                if(deletechat==true){
//                    Log.d("delete","abc");
//
//                    Message messages = (Message) dataSnapshot.getValue(Message.class);
//                    messages.setKey(dataSnapshot.getKey());
//                    keyList.add(dataSnapshot.getKey());
//                    itemPos++;
//
//                    if (itemPos == 1) {
//                        String mMessageKey = dataSnapshot.getKey();
//
//                        mLastKey = mMessageKey;
//                        mPrevKey = mMessageKey;
//                    }
//                    if (messages.getStatus().equals("1"))
//                        messageArrayList.add(messages);
//
//
//                    chatAdapter.notifyDataSetChanged();
//
//                    rc_chat.scrollToPosition(messageArrayList.size() - 1);
//
//                    messageCount=String.valueOf(messageArrayList.size());
//                    getcomment(messageCount);
//                    Log.d("messagesize", String.valueOf(messageArrayList.size()));
//
//
//                }else{
//                    Log.d("delete","def");
//                    //checkfordeletechat=true;
//                }
//                // Log.d("messagescount", String.valueOf(messageCount));
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//                if(checkfordeletechat==false){
//                    Log.d("delete", "cde");
//                    //    networkCall.NetworkAPICall(API.INFORM_DELETE_CHAT, true);
//                    int index = keyList.indexOf(dataSnapshot.getKey());
//                    messageArrayList.remove(index);
//                    messageCount=String.valueOf(messageArrayList.size());
//                    Log.d("msgcount",String.valueOf(messageCount));
//                    getcomment(messageCount);
//                    keyList.remove(index);
//                    chatAdapter.notifyDataSetChanged();
//                    // keyList.notifyAll();
//
//                    deletechat = false;
//                    checkfordeletechat=true;
//                }
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//
//
//
//
//
//        };
//
//        messageQuery.addChildEventListener(childEventListener);
//    }
//
//
//    private void ShowUsers(String channelUser) {
//        Log.d("take1","check");
//        DatabaseReference messageRef = null;
//        Particularchannel=channelUser;
//        //  DatabaseReference messageRef2 = null;
//        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Particularchannel);
//        //  mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Particularchannel);
//        messageRef = mDatabaseReference;
//
//        Log.d("take2","check");
//
//        Log.d("checkchangechnl1",Particularchannel);
//
//        //   Query messageQuery = messageRef.limitToLast(mCurrentPage * TOTAL_ITEM_TO_LOAD);
//        showmessagequery = messageRef.limitToLast(mCurrentPage * TOTAL_ITEM_TO_LOAD);
//        Log.d("take3","check");
//
//       /* messageRef.runTransaction(new Transaction.Handler() {
//            @NonNull
//            @Override
//            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
//                Log.d("transcation","tr1");
//                int countModel=mutableData.getValue(CountModel.class).getCount() ;
//                Log.d("transcation","tr3");
//                if(IsSHOWuser==true){
//                   mutableData.getValue(CountModel.class).setCount(countModel+1);
//                   Log.d("transcation","tr2");
//                }
//                else {
//                    mutableData.getValue(CountModel.class).setCount(countModel-1);
//                }
//
//                return Transaction.success(mutableData);
//            }
//
//            @Override
//            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
//
//            }
//        });
//
//
//
//        ValueEventListener eventListener=new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                int countModel=dataSnapshot.getValue(CountModel.class).getCount();
//                Log.d("TAG", String.valueOf(countModel) + "");
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        };
//        messageRef.addListenerForSingleValueEvent(eventListener);
//
//
//*/
//
//
//
//
//        showchildEventListner=new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                // int Count=dataSnapshot.getValue();
//                Log.d("onchild Added","checking1");
//
//                if(checkForShowUser==true){
//
//                    Log.d("show",dataSnapshot.getKey());
//                    Log.d("onchild Added","checking2");
//                    Long countModel=(Long)dataSnapshot.getValue();
//                    ccount=countModel+1;
//                    Log.d("onchild Added","checking3");
//                    // UserOnline(Particularchannel);
//                    UserOnline(channelDatabase, channelmain, channel_id);
//                }
//                else{
//                    Log.d("show2",dataSnapshot.getKey());
//                    Log.d("onchild Added","checking4");
//                    Long countModel=(Long)dataSnapshot.getValue();
//                    ccount=countModel-1;
//                    Log.d("onchild Added","checking5");
//
//                    //  UserOnline(Particularchannel);
//                    UserOnline(channelDatabase, channelmain, channel_id);
//
//                    // checkForShowUser=false;
//                }
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Log.d("checkforlistner","onChildChanged");
//                Log.d("checkforlr",String.valueOf(ccount));
//
//
//                getLiveView(ccount);
//
//
//                //  UserOnline(channelDatabase, channelmain,channel_id);
//
//                if(checkForShowUser==false){
//                    Log.d("show",dataSnapshot.getKey());
//                    Long countModel=(Long)dataSnapshot.getValue();
//                    ccount=countModel-1;
//                    UserOnline(channelDatabase, channelmain,channel_id);
//                }
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//                Log.d("checkforlistner","onChildRemoved");
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Log.d("checkforlistner","onChildMoved");
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//
//
//
//
//
//        };
//        Log.d("onchild Added","checkingnotinchildadded");
//
//        showmessagequery.addChildEventListener(showchildEventListner);
//    }
//
//    private void getcomment(String messageCount) {
//        if (messageCount.equals("0")) {
//            comment_number_watch_channel.setText("0 Comment");
//        } else if (messageCount.equals("1")) {
//            comment_number_watch_channel.setText(messageCount + " Comment");
//        } else {
//            comment_number_watch_channel.setText(messageCount + " Comments");
//        }
//    }
//
//    private void getLiveView(Long messageCount) {
//        if (messageCount.equals("0")) {
//            view_user_watch_channel.setText("0 View");
//        } else if (messageCount.equals("1")) {
//            view_user_watch_channel.setText(messageCount + " view");
//        } else {
//            view_user_watch_channel.setText(messageCount + " Views");
//        }
//    }
//
//    public void LoadChannel(String viewlike, String is_likes, String channel_url, String channel_id, String image) {
//        messageQuery.removeEventListener(childEventListener);
////       showmessagequery.removeEventListener(showchildEventListner);
////
//        messageArrayList=new ArrayList<>();
//        keyList =new ArrayList<>();
//      /*  if(checkForShowUser==true){
//            checkForShowUser=false;
//            Log.d("checkchangechnl",channelUser);
//            showmessagequery.removeEventListener(showchildEventListner);
//            ShowUsers(channelUser);
//
//        }*/
//        Log.d("checkchangechnl","not a problem");
//
//      /*  video_url=channel_url;
//        Id=id;*/
//        //    Likes_no=likes;
//        //   Is_like_no=is_likes;
//        // Id=
//
//    /*    id = channel_id;
//
//        String channel_images = image;
//
//        //   setChannelData();
//        // Home_channel_play.setVisibility(View.GONE);
//        playerView.setVisibility(View.GONE);
//        progressvideo.setVisibility(View.GONE);
//        playIconIV.setVisibility(View.VISIBLE);
//        videoImage.setVisibility(View.VISIBLE);
//        Glide.with(this).load(channel_images).into(videoImage);
//        playIconIV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                playerView.setVisibility(View.VISIBLE);
//                playIconIV.setVisibility(View.GONE);
//                progressvideo.setVisibility(View.VISIBLE);
//
//                video_channel_wise = Video.createVideo(channel_url);
//
//              *//*  AudioPlayerService.mediaPlayer.pause();
//                ((HomeActivityy) context).playpause.setImageResource(R.mipmap.audio_play);*//*
//
//                livestreaming_code();
//            }
//        });*/
//        video_url=channel_url;
//        id=channel_id;
//
//        deletechat=true;
//        // checkdeletechat="0";
//        // deletechat = false;
//        checkfordeletechat=true;
//        //  checkForShowUser = true;
//        getChannelChat(id);
//
//        // Likes_no=viewlike;
//        VIEWlike=viewlike;
//        Is_like_no=is_likes;
//        String Channel_image=image;
//        //  setChannelData();
//        setChannelRelatedData();
//        playerView.clear();
//        // Home_channel_play.stop(mediaController1);
//       /* BrightcoveMediaController mediaController2= new BrightcoveMediaController(Home_channel_play, R.layout.custom_controller_brightcove);
//        Home_channel_play.setMediaController(mediaController2);*/
//        playerView.setVisibility(View.GONE);
//        progressvideo.setVisibility(View.GONE);
//        playIconIV.setVisibility(View.VISIBLE);
//
//        Glide.with(this).load(Channel_image).into(videoImage);
//        playIconIV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                playerView.setVisibility(View.VISIBLE);
//                playIconIV.setVisibility(View.GONE);
//                progressvideo.setVisibility(View.VISIBLE);
//                video_channel_wise = Video.createVideo(channel_url);
//              /*  AudioPlayerService.mediaPlayer.pause();
//                ((HomeActivityy) context).playpause.setImageResource(R.mipmap.audio_play);*/
//
//                livestreaming_code(video_channel_wise);
//            }
//        });
//
//              /*  AudioPlayerService.mediaPlayer.pause();
//                ((HomeActivityy) context).playpause.setImageResource(R.mipmap.audio_play);*/
//
//
//    }
//
//
//    private void livestreaming_code(Video video_channel_wise) {
//        BrightcoveMediaController mediaController1 = new BrightcoveMediaController(playerView, R.layout.custom_controller_brightcove);
//        playerView.setMediaController(mediaController1);
//
//        playerView.clear();
//        playerView.add(video_channel_wise);
//        playerView.start();
//
//
//        playerView.getEventEmitter().on(EventType.BUFFERING_STARTED, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//
//                progressvideo.setVisibility(View.VISIBLE);
//            }
//        });
//        playerView.getEventEmitter().on(EventType.BUFFERING_COMPLETED, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//                progressvideo.setVisibility(View.GONE);
//            }
//        });
//     /*   LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
//                , LinearLayout.LayoutParams.MATCH_PARENT);
//        params.weight = 5;
//        // headerrel.setLayoutParams(params);
//        headerrel.setLayoutParams(new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 600));
//        playerView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));*/
//
//
//        playerView.getEventEmitter().on(EventType.DID_ENTER_FULL_SCREEN, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//                RotationFull = true;
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
//
//                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
//                        , LinearLayout.LayoutParams.MATCH_PARENT);
//                FrameLayout.LayoutParams param2 = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
//                        , LinearLayout.LayoutParams.MATCH_PARENT);
//               /* params.weight = 5;
//                // headerrel.setLayoutParams(params);
//                headerrel.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,600));
//
//*/
//                RelativeLayout.LayoutParams param3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT
//                        ,RelativeLayout.LayoutParams.MATCH_PARENT );
//
//
//                //  playerView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
//
//
//                // params.weight = 1;
//                headerrel.setLayoutParams(params);
//
//                param3.setMargins(0,80,0,0);
//                playerView.setLayoutParams(param3);
//                playerView.requestLayout();
//
///*
//                ll_option.setVisibility(View.VISIBLE);
//*/
//
//
//
//
//                //  playerView.setLayoutParams(params1);
//                //toolbar.setVisibility(View.GONE);
//                //TODO 24/8/20
//                //  param2.setMargins(0, 80, 0, 0);
//
//                //-------------------------------------
//                container.setLayoutParams(param2);
//                container.requestLayout();
//                ll_chat.setVisibility(View.GONE);
//                rc_chat.setVisibility(View.GONE);
//
//
//
//                // swipeRefreshLayout.setVisibility(View.GONE);
//
//            }
//        });
//
//        playerView.getEventEmitter().on(EventType.DID_EXIT_FULL_SCREEN, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//                /*setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);*/
//                //      if (Rotation) {
//                //          setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
//                // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
//                RotationFull = false;
//                //      }
//                //      else {
//                RotationFull = false;
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//               /* LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
//                        , LinearLayout.LayoutParams.MATCH_PARENT);*/
//                FrameLayout.LayoutParams param2 = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
//                        , LinearLayout.LayoutParams.MATCH_PARENT);
//                float scale = getResources().getDisplayMetrics().density;
//                int converter =(int) (200 * scale + 0.5f);
//                int converter1 =(int) (235 * scale + 0.5f);
//
//                //   params.weight = 5;
//                // headerrel.setLayoutParams(params);
//
//
//
//                //  playerView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, converter));
//                headerrel.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, converter1));
//
//                RelativeLayout.LayoutParams param3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT
//                        ,RelativeLayout.LayoutParams.MATCH_PARENT );
//                // playerView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
//                param3.setMargins(0,80,0,0);
//                playerView.setLayoutParams(param3);
//                playerView.requestLayout();
//
//                param2.setMargins(0, 0, 0, 0);
//                container.setLayoutParams(param2);
//                container.requestLayout();
//
//                ll_chat.setVisibility(View.VISIBLE);
//                ll_option.setVisibility(View.GONE);
//                ll_all_option.setVisibility(View.VISIBLE);
//                rc_chat.setVisibility(View.VISIBLE);
//                // swipeRefreshLayout.setVisibility(View.VISIBLE);
//                //   linearbottom.setVisibility(View.VISIBLE);
//                // linearmid.setVisibility(View.VISIBLE);
//                //   }
//            }
//        });
//
//    }
//
//    public void removemsg(String msg, String mm, int i, List<Message> messages) {
//        FirebaseDatabase.getInstance().getReference().child(msg)
//                .child(mm)
//                .removeValue()
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        // rc_chat.remove(i);
//                        //    messages.remove(i);
//
//
//                                            /*   messages.remove(i);
//                                               notifyItemRemoved(i);
//                                               notifyItemRangeChanged(i, messages.size());*/
//                        //   chatAdapter.notifyItemRemoved(i);
//                        //   chatAdapter.notifyItemRangeChanged(i, list.size());
//                        chatAdapter .notifyDataSetChanged();
//
//                        liveStreamOnTopLiveChannelAdapter.notifyDataSetChanged();
//
//                        //   chatAdapter.notifyItemRangeChanged(i, messages.size());
//
//                        Toast.makeText(activity, "deleted", Toast.LENGTH_SHORT).show();
//                    }
//                });}
//}
//
