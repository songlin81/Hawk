[1] HawkApplication
    a) Singleton class
    b) Handler to signal the splash
    c) Bundle to lift data
[2] SplashActivity
    a) Implement Animation.AnimationListener anim_slide_in/anim_fade_in
    b) Pass handler to HawkApplication instance
    c) post/postDelay to MainActivity via Runnable
    d) Log.d Tag class.getSimpleName
    e) Set FLAG_FULLSCREEN for WindowManager to display fullscreen
    f) Layout includes sub-content
[3] BroadcastService
    a) Manifest to register service
    b) Handler removeCallbacks and postDelayed
    c) Runnable sendUpdatesToUI
[4] MainActivity
    a) onCreate: new Intent(this, BroadcastService.class)
       broadcastReceiver --> updateUI
       onResume: startService, registerReceiver
       onPause: unregisterReceiver, stopService
    b) ModelAdapter
       extends BaseAdapter: public interface OnItemClickListener-->onItemClick
       layout item_mode for vehicles in CardView
       Bean Vehicle
       Add ModelAdapter to PullToRefreshRecyclerView, setItemClickListener
    c) PullToRefreshRecyclerView
       addHeaderView, addFooterView, addFooterView
       override onRefresh, onLoadMore
    d) Use onWindowFocusChanged to bind events
 [5] LoginActivity
    a) View.CustomVideoView
    b) Button android:background="@drawable/shape"
    c) VideoView start in loop
 [6] SQLite
    a) Open Device File Explorer in data directiry to access copy of HawkApp.db
    b)
----------------------------------------------------------------------------------------------------
{ref}
    [a] https://blog.csdn.net/yuankundong/article/details/51360798
    [b] https://blog.csdn.net/u013651026/article/details/79000205
    [c] https://www.cnblogs.com/tangs/articles/5463347.html
    [d] https://www.jianshu.com/p/bd6be15b3bf4
    [e] https://www.jb51.net/article/96549.htm
    [f] https://www.cnblogs.com/nanze/p/5384064.html
    [g] https://www.cnblogs.com/nanze/p/5408105.html & take image from cam
    [h] https://blog.csdn.net/z740852294/article/details/77679256
        https://docs.open.alipay.com/54/104509/
        https://blog.csdn.net/qq_41258836/article/details/80504588