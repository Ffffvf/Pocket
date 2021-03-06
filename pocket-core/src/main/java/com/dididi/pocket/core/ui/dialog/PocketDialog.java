package com.dididi.pocket.core.ui.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dididi.pocket.core.R;
import com.dididi.pocket.core.util.DimenUtil;
import com.mikepenz.iconics.view.IconicsTextView;

/**
 * @author dididi
 * @describe 自定义的dialog
 * @since 15/10/2018
 */

public class PocketDialog extends Dialog implements DialogInterface {

    private View mDialogView;

    private IconicsTextView mIcon;
    private AppCompatTextView mTitle;
    private AppCompatTextView mContent;
    private AppCompatTextView mCancel;
    private AppCompatTextView mConfirm;
    private RelativeLayout mMain;
    private LinearLayout mBackground;
    private RelativeLayout mTop;

    private boolean isCancelable = true;

    public PocketDialog(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PocketDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    public static PocketDialog getInstance(Context context){
        return new PocketDialog(context,R.style.pocket_dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getWindow() == null) {
            throw new RuntimeException("getWindow为空 请检查PocketDialog");
        }
        //获取屏幕宽高以设置dialog大小
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = (int) (DimenUtil.getScreenWidth() * 0.75f);
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);
    }

    private void init(Context context) {
        mDialogView = View.inflate(context, R.layout.dialog_layout, null);
        mIcon = mDialogView.findViewById(R.id.dialog_layout_title_icon);
        mTitle = mDialogView.findViewById(R.id.dialog_layout_title);
        mContent = mDialogView.findViewById(R.id.dialog_layout_content);
        mCancel = mDialogView.findViewById(R.id.dialog_layout_cancel);
        mConfirm = mDialogView.findViewById(R.id.dialog_layout_confirm);
        mMain = mDialogView.findViewById(R.id.dialog_layout_main);
        mBackground = mDialogView.findViewById(R.id.dialog_layout_background);
        mTop = mDialogView.findViewById(R.id.dialog_layout_top_layout);
        setContentView(mDialogView);
        mMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCancelable) {
                    dismiss();
                }
            }
        });
    }

    public PocketDialog setIcon(String icon){
        mIcon.setText(icon);
        return this;
    }

    public PocketDialog setTitle(String title){
        mTitle.setText(title);
        return this;
    }

    public PocketDialog setTopColor(String color){
        mTop.setBackgroundColor(Color.parseColor(color));
        return this;
    }

    public PocketDialog setTopColor(int color){
        mTop.setBackgroundColor(color);
        return this;
    }

    public PocketDialog setBackgroundColor(String color){
        mBackground.setBackgroundColor(Color.parseColor(color));
        return this;
    }

    public PocketDialog setBackgroundColor(int color){
        mBackground.setBackgroundColor(color);
        return this;
    }

    public PocketDialog setContent(String content){
        mContent.setText(content);
        return this;
    }

    public PocketDialog setCancelClickListener(View.OnClickListener onClickListener){
        mCancel.setVisibility(View.VISIBLE);
        mCancel.setOnClickListener(onClickListener);
        return this;
    }

    public PocketDialog setButton1ClickListener(String text,View.OnClickListener onClickListener){
        mCancel.setVisibility(View.VISIBLE);
        mCancel.setText(text);
        mCancel.setOnClickListener(onClickListener);
        return this;
    }

    public PocketDialog setConfirmClickListener(View.OnClickListener onClickListener){
        mConfirm.setVisibility(View.VISIBLE);
        mConfirm.setOnClickListener(onClickListener);
        return this;
    }

    public PocketDialog setButton2ClickListener(String text,View.OnClickListener onClickListener){
        mConfirm.setVisibility(View.VISIBLE);
        mConfirm.setText(text);
        mConfirm.setOnClickListener(onClickListener);
        return this;
    }

    public PocketDialog isCancelableOnTouchOutside(boolean cancelable){
        this.isCancelable = cancelable;
        this.setCanceledOnTouchOutside(cancelable);
        return this;
    }

    public PocketDialog isCancelable(boolean cancelable){
        this.isCancelable = cancelable;
        this.setCancelable(cancelable);
        return this;
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mConfirm.setVisibility(View.GONE);
        mCancel.setVisibility(View.GONE);
    }
}
