package com.shoujiduoduo.duonavigator;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

/**
 * package: com.shoujiduoduo.duonavigator
 * create by zhilin on 2018/3/31 16:07
 * des:
 */
public class NavigatorItem {

    private int actDrawableRes;

    private int inactDrawableRes;

    private String title;

//    private int actBgColor;
//
//    private int inactBgColor;


    public NavigatorItem() {
    }

    public NavigatorItem(int inactDrawableRes, int actDrawableRes) {
        this.actDrawableRes = actDrawableRes;
        this.inactDrawableRes = inactDrawableRes;
    }

    public NavigatorItem(int actDrawableRes, int inactDrawableRes, String title) {
        this.actDrawableRes = actDrawableRes;
        this.inactDrawableRes = inactDrawableRes;
        this.title = title;
    }

    public int getActDrawableRes() {
        return actDrawableRes;
    }

    public void setActDrawableRes(int actDrawableRes) {
        this.actDrawableRes = actDrawableRes;
    }

    public int getInactDrawableRes() {
        return inactDrawableRes;
    }

    public void setInactDrawableRes(int inactDrawableRes) {
        this.inactDrawableRes = inactDrawableRes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    Drawable getIcon(Context context) {
        return ContextCompat.getDrawable(context, this.actDrawableRes);
    }

    Drawable getInactiveIcon(Context context) {
        return ContextCompat.getDrawable(context, this.inactDrawableRes);
    }
}
