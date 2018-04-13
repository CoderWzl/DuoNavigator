package com.shoujiduoduo.duonavigator;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * package: com.shoujiduoduo.duonavigator
 * create by zhilin on 2018/3/31 15:24
 * des: 导航栏
 */
public class DuoNavigator extends FrameLayout {

    private static final String TAG = "DuoNavigator";

    public final int DEFAULT_POSITION = -1;

    private FrameLayout mBgOverLay;

    private FrameLayout mContainer;

    private View mAnimView;

    private LinearLayout mTabContainer;

    private ObservableScrollView mScroll;

    private float mElevation;

    private boolean hasAnim = false;

    private boolean isAniming = false;

    private int mScrolledY = 0;

    private final int DEFAULT_DURATION = 100;

    private int mAnimDuration = DEFAULT_DURATION;

    private int mActiveTabBgColor = Color.WHITE;

    private ArrayList<NavigatorItem> items = new ArrayList<>();
    private ArrayList<DuoTag> tabs = new ArrayList<>();

    private OnTabSelectedListener mListener;

    private int mSelectedPosition;

    public void setTabSelectedListener(@NonNull OnTabSelectedListener listener){
        this.mListener = listener;
    }

    public DuoNavigator(@NonNull Context context) {
        this(context,null);
    }

    public DuoNavigator(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public DuoNavigator(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        View parent = LayoutInflater.from(context).inflate(R.layout.navigator_layout,this,true);
        mContainer = parent.findViewById(R.id.navigator_container);
        mBgOverLay = parent.findViewById(R.id.navigator_overlay);
        mTabContainer = parent.findViewById(R.id.navigator_tab_container);
        mScroll = parent.findViewById(R.id.navigator_tab_scroll);
        mScroll.setScrollable(true);
        mScroll.setOnScrollChangeListener(new ObservableScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(int scrollX, int scrollY, int oldX, int oldY) {
                mScrolledY = scrollY;
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.setOutlineProvider(ViewOutlineProvider.BOUNDS);
        } else {
            //to do
        }

//        ViewCompat.setElevation(this, mElevation);
        setClipToPadding(false);
    }

    public void selectTab(int newPosition) {
        selectTab(newPosition, true);
    }

    public void selectTab(int newPosition, boolean callListener) {
        selectTabInternal(newPosition, false, callListener, callListener);
    }

    public DuoNavigator animEnable(boolean aninEnable){
        hasAnim = aninEnable;
        return this;
    }

    public DuoNavigator setAnimDuration(int duration){
        this.mAnimDuration = duration;
        return this;
    }

    public DuoNavigator setActiveTabBgColor(int color){
        mActiveTabBgColor = color;
        return this;
    }

    public DuoNavigator addNavigatorItem(@NonNull NavigatorItem item){
        items.add(item);
        return this;
    }

    public DuoNavigator addNavigatorItems(@NonNull ArrayList<NavigatorItem> items){
        this.items.addAll(items);
        return this;
    }

    public void initialise() {
        mSelectedPosition = DEFAULT_POSITION;
        tabs.clear();

        if (hasAnim) {
            mAnimView = new DuoTag(getContext());
            mAnimView.setBackground(new ColorDrawable(mActiveTabBgColor));
            mAnimView.setVisibility(GONE);
            mContainer.addView(mAnimView, 0);
        }

        for (NavigatorItem item: items){
            generateTab(item);
        }
        selectTabInternal(0,true,false,false);
    }

    private void generateTab(NavigatorItem item) {
        DuoTag tab = new DuoTag(getContext());
        tab.setIcon(item.getIcon(getContext()));
        tab.setInactiveIcon(item.getInactiveIcon(getContext()));
        tab.setPosition(items.indexOf(item));
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTabInternal(((DuoTag) v).getPosition(),false,true,false);
            }
        });
        tab.initialise();
        tabs.add(tab);
        mTabContainer.addView(tab);
    }

    /**
     *
     * @param newPosition
     * @param firstTab 第一次选中
     * @param callListener 是否调用回调
     * @param forcedSelection 监听重复点击事件
     */
    private void selectTabInternal(final int newPosition, boolean firstTab, boolean callListener, boolean forcedSelection) {
        if (isAniming){
            return;
        }
        final int oldPosition = mSelectedPosition;
        if (newPosition != oldPosition){
            // TODO: 2018/3/31 添加动画
            if (hasAnim && !firstTab){
                animUpdateView(newPosition, oldPosition,callListener, forcedSelection);
            }else {
                updateView(newPosition, oldPosition);
                if (callListener){
                    sendListenerEvent(oldPosition,newPosition,forcedSelection);
                }
            }
        }else {
            if (callListener){
                sendListenerEvent(oldPosition,newPosition,forcedSelection);
            }
        }

    }

    private void animUpdateView(final int newPosition, final int oldPosition,final boolean callListener, final boolean forcedSelection) {
        DuoTag oldTag = tabs.get(oldPosition);
        DuoTag newTag = tabs.get(newPosition);
        oldTag.setBackground(new ColorDrawable(Color.TRANSPARENT));
        mAnimView.setVisibility(VISIBLE);
        TranslateAnimation animation = new TranslateAnimation(oldTag.getX(),newTag.getX(),
                oldTag.getY()-mScrolledY,newTag.getY()-mScrolledY);
        animation.setDuration(mAnimDuration);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isAniming = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mAnimView.setVisibility(GONE);
                updateView(newPosition,oldPosition);
                isAniming = false;
                if (callListener){
                    sendListenerEvent(oldPosition,newPosition,forcedSelection);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mAnimView.startAnimation(animation);
    }

    private void updateView(int newPosition, int oldPosition) {
        if (oldPosition != -1 && oldPosition < tabs.size()) {
            tabs.get(oldPosition).unSelect();
            tabs.get(oldPosition).setBackground(new ColorDrawable(Color.TRANSPARENT));
        }
        if (newPosition >= 0 && newPosition < tabs.size()) {
            tabs.get(newPosition).select();
            tabs.get(newPosition).setBackground(new ColorDrawable(mActiveTabBgColor));
        }
        mSelectedPosition = newPosition;
    }

    private void sendListenerEvent(int oldPos, int newPos, boolean forcedSelection) {
        if (mListener!=null) {
            if (forcedSelection) {
                mListener.onTabSelected(newPos);
            } else {
                if (oldPos == newPos) {
                    mListener.onTabReselected(oldPos);
                } else {
                    mListener.onTabSelected(newPos);
                    if (oldPos != -1) {
                        mListener.onTabUnselected(oldPos);
                    }
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // description 接口回调
    ///////////////////////////////////////////////////////////////////////////
    public interface OnTabSelectedListener {

        void onTabSelected(int position);

        void onTabUnselected(int position);

        void onTabReselected(int position);
    }

    public static class SimpleOnTabSelectedListener implements OnTabSelectedListener {
        @Override
        public void onTabSelected(int position) {
        }

        @Override
        public void onTabUnselected(int position) {
        }

        @Override
        public void onTabReselected(int position) {
        }
    }
}
