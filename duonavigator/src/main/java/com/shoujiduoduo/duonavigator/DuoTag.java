package com.shoujiduoduo.duonavigator;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * package: com.shoujiduoduo.duonavigator
 * create by zhilin on 2018/3/31 16:22
 * des:
 */
public class DuoTag extends FrameLayout {

    protected int mPosition;

    protected Drawable mCompactIcon;
    protected Drawable mCompactInActiveIcon;

    private FrameLayout mContainer;

    private ImageView mIcon;
//    private boolean isInActiveIconSet = false;

    public DuoTag(@NonNull Context context) {
        this(context,null);
    }

    public DuoTag(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context , attrs, 0);
    }

    public DuoTag(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        View parent = LayoutInflater.from(context).inflate(R.layout.navigator_tab_layout,this,true);
        mContainer = parent.findViewById(R.id.navigator_tab);
        mIcon = parent.findViewById(R.id.navigator_tab_icon);
    }

    public void setIcon(Drawable icon) {
        mCompactIcon = DrawableCompat.wrap(icon);
    }

    public void setInactiveIcon(Drawable icon) {
        mCompactInActiveIcon = DrawableCompat.wrap(icon);
//        isInActiveIconSet = true;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public int getPosition() {
        return mPosition;
    }

    public void select() {
        mIcon.setSelected(true);
        mContainer.setSelected(true);
    }

    public void unSelect() {
        mIcon.setSelected(false);
        mContainer.setSelected(false);
    }

    @CallSuper
    public void initialise() {
        mIcon.setSelected(false);
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_selected},
                mCompactIcon);
        states.addState(new int[]{-android.R.attr.state_selected},
                mCompactInActiveIcon);
        states.addState(new int[]{},
                mCompactInActiveIcon);
        mIcon.setImageDrawable(states);
    }
}
