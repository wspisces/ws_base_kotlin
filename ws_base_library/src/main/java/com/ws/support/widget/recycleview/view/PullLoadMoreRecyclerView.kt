package com.ws.support.widget.recycleview.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.RecyclerView.ItemAnimator
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.orhanobut.logger.Logger
import com.ws.base.R

/**
 * Created by WuXiaolong on 2015/7/2.
 * github:https://github.com/WuXiaolong/PullLoadMoreRecyclerView
 * weibo:http://weibo.com/u/2175011601
 * 微信公众号：吴小龙同学
 * 个人博客：http://wuxiaolong.me/
 */
class PullLoadMoreRecyclerView : LinearLayout {
    private var mRecyclerView: RecyclerView? = null
    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    private var mPullLoadMoreListener: PullLoadMoreListener? = null
    private var hasMore = true
    var isRefresh = false
    private var isLoadMore = false
    var pullRefreshEnable = true
    var pushRefreshEnable = true
    private var mIcon = 0
    private lateinit var mFooterView: View
    private var mContext: Context? = null
    private var loadMoreText: TextView? = null
    private var loadMoreLayout: LinearLayout? = null
    private var tvNoDate: TextView? = null
    lateinit var iv_no_data: ImageView

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.PullLoadMoreRecyclerView, 0, 0)
        mIcon = attr.getResourceId(R.styleable.PullLoadMoreRecyclerView_noDataIcon, 0)
        initView(context)
    }

    private fun initView(context: Context) {
        mContext = context
        val view = LayoutInflater.from(context).inflate(R.layout.view_base_m_pull_load_more, null)
        mSwipeRefreshLayout = view.findViewById<View>(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        mSwipeRefreshLayout!!.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark)
        mSwipeRefreshLayout!!.setOnRefreshListener(SwipeRefreshLayoutOnRefresh(this))
        tvNoDate = view.findViewById<View>(R.id.tv_no_date) as TextView
        mRecyclerView = view.findViewById<View>(R.id.recycler_view) as RecyclerView
        mRecyclerView!!.isVerticalScrollBarEnabled = true
        mRecyclerView!!.setHasFixedSize(true)
        mRecyclerView!!.itemAnimator = DefaultItemAnimator()
        mRecyclerView!!.addOnScrollListener(RecyclerViewOnScroll(this))
        mRecyclerView!!.setOnTouchListener(onTouchRecyclerView())
        mFooterView = view.findViewById(R.id.footerView)
        loadMoreLayout = view.findViewById<View>(R.id.loadMoreLayout) as LinearLayout
        loadMoreText = view.findViewById<View>(R.id.loadMoreText) as TextView
        iv_no_data = view.findViewById(R.id.iv_no_data)
        if (mIcon != 0) {
            iv_no_data.setImageResource(mIcon)
        }
        mFooterView.setVisibility(GONE)
        this.addView(view)
    }

    fun showNoDataView(visible: Int) {
        iv_no_data!!.visibility = visible
        tvNoDate!!.visibility = GONE
    }

    /**
     * LinearLayoutManager
     */
    fun setLinearLayout() {
        val linearLayoutManager = LinearLayoutManager(mContext)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        mRecyclerView!!.layoutManager = linearLayoutManager
    }

    /**
     * GridLayoutManager
     */
    fun setGridLayout(spanCount: Int) {
        val gridLayoutManager = GridLayoutManager(mContext, spanCount)
        gridLayoutManager.orientation = LinearLayoutManager.VERTICAL
        mRecyclerView!!.layoutManager = gridLayoutManager
    }

    /**
     * StaggeredGridLayoutManager
     */
    fun setStaggeredGridLayout(spanCount: Int) {
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(spanCount, LinearLayoutManager.VERTICAL)
        mRecyclerView!!.layoutManager = staggeredGridLayoutManager
    }

    fun getLayoutManager(): RecyclerView.LayoutManager? {
        return mRecyclerView!!.layoutManager
    }

    fun getRecyclerView(): RecyclerView? {
        return mRecyclerView
    }

    fun setItemAnimator(animator: ItemAnimator?) {
        mRecyclerView!!.itemAnimator = animator
    }

    fun addItemDecoration(decor: ItemDecoration?, index: Int) {
        mRecyclerView!!.addItemDecoration(decor!!, index)
    }

    fun addItemDecoration(decor: ItemDecoration?) {
        mRecyclerView!!.addItemDecoration(decor!!)
    }

    fun scrollToTop() {
        mRecyclerView!!.scrollToPosition(0)
    }

    fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        if (adapter != null) {
            mRecyclerView!!.adapter = adapter
        }
    }

    @JvmName("setPullRefreshEnable1")
    fun setPullRefreshEnable(enable: Boolean) {
        pullRefreshEnable = enable
        setSwipeRefreshEnable(enable)
    }

    @JvmName("getPullRefreshEnable1")
    fun getPullRefreshEnable(): Boolean {
        return pullRefreshEnable
    }

    fun setSwipeRefreshEnable(enable: Boolean) {
        mSwipeRefreshLayout!!.isEnabled = enable
    }

    fun getSwipeRefreshEnable(): Boolean {
        return mSwipeRefreshLayout!!.isEnabled
    }

    fun setColorSchemeResources(vararg colorResIds: Int) {
        mSwipeRefreshLayout!!.setColorSchemeResources(*colorResIds)
    }

    fun getSwipeRefreshLayout(): SwipeRefreshLayout? {
        return mSwipeRefreshLayout
    }

    fun setRefreshing(isRefreshing: Boolean) {
        mSwipeRefreshLayout!!.post { if (pullRefreshEnable) mSwipeRefreshLayout!!.isRefreshing = isRefreshing }
    }

    private var mDownstartY // 按下点的x值
            = 0
    private var mUpEndY // 按下点的y值
            = 0

    /**
     * Solve IndexOutOfBoundsException exception
     */
    inner class onTouchRecyclerView : OnTouchListener {
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    mDownstartY = event.y.toInt()
                    Logger.v("mDownstartY=$mDownstartY")
                }
                MotionEvent.ACTION_MOVE -> {
                }
                MotionEvent.ACTION_UP -> {
                    mUpEndY = event.y.toInt()
                    Logger.v("mUpEndY=$mUpEndY")
                    if (mDownstartY - mUpEndY > 60) {
                        if (getPushRefreshEnable()
                                && !isRefresh()
                                && isHasMore()
                                && !isLoadMore()
                                && !mRecyclerView!!.canScrollVertically(1)) {
                            setIsLoadMore(true)
                            loadMore()
                        }
                    }
                }
            }
            return isRefresh
        }
    }

    @JvmName("getPushRefreshEnable1")
    fun getPushRefreshEnable(): Boolean {
        return pushRefreshEnable
    }

    @JvmName("setPushRefreshEnable1")
    fun setPushRefreshEnable(pushRefreshEnable: Boolean) {
        this.pushRefreshEnable = pushRefreshEnable
    }

    fun getFooterViewLayout(): LinearLayout? {
        return loadMoreLayout
    }

    fun setFooterViewBackgroundColor(color: Int) {
        loadMoreLayout!!.setBackgroundColor(ContextCompat.getColor(mContext!!, color))
    }

    fun setFooterViewText(text: CharSequence?) {
        loadMoreText!!.text = text
    }

    fun setFooterViewText(resid: Int) {
        loadMoreText!!.setText(resid)
    }

    fun setFooterViewTextColor(color: Int) {
        loadMoreText!!.setTextColor(ContextCompat.getColor(mContext!!, color))
    }

    fun refresh() {
        if (mPullLoadMoreListener != null) {
            mPullLoadMoreListener!!.onRefresh()
        }
    }

    fun loadMore() {
        if (mPullLoadMoreListener != null && hasMore) {
            setHasMore(false)
            mFooterView!!.translationY = 0f
            mFooterView!!.visibility = VISIBLE
            /*mFooterView.animate()
                    .translationY(0)
                    .setDuration(300)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            mFooterView.setVisibility(View.VISIBLE);
                        }
                    })
                    .start();*/invalidate()
            mPullLoadMoreListener!!.onLoadMore()
        }
    }

    fun setPullLoadMoreCompleted() {
        mFooterView!!.animate()
                .translationY(mFooterView!!.height.toFloat())
                .setDuration(300)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator) {}
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        mFooterView!!.visibility = GONE
                        isRefresh = false
                        setRefreshing(false)
                        setHasMore(true)
                        isLoadMore = false
                    }
                })
                .start()
    }

    fun setOnPullLoadMoreListener(listener: PullLoadMoreListener?) {
        mPullLoadMoreListener = listener
    }

    fun isLoadMore(): Boolean {
        return isLoadMore
    }

    fun setIsLoadMore(isLoadMore: Boolean) {
        this.isLoadMore = isLoadMore
    }

    @JvmName("isRefresh1")
    fun isRefresh(): Boolean {
        return isRefresh
    }

    fun setIsRefresh(isRefresh: Boolean) {
        this.isRefresh = isRefresh
    }

    fun isHasMore(): Boolean {
        return hasMore
    }

    fun setHasMore(hasMore: Boolean) {
        this.hasMore = hasMore
    }

    interface PullLoadMoreListener {
        fun onRefresh()
        fun onLoadMore()
    }
}