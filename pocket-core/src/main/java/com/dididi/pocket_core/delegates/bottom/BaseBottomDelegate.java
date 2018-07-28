package com.dididi.pocket_core.delegates.bottom;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.dididi.pocket_core.R;
import com.dididi.pocket_core.R2;
import com.dididi.pocket_core.Util.LogUtil;
import com.dididi.pocket_core.delegates.PocketDelegate;
import com.mikepenz.iconics.view.IconicsTextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import me.yokeyword.fragmentation.SupportFragment;


/**
 * Created by dididi
 * on 24/07/2018 .
 */

@SuppressWarnings({"FieldCanBeLocal", "MismatchedQueryAndUpdateOfCollection"})
public abstract class BaseBottomDelegate extends PocketDelegate implements View.OnClickListener {
    //抽象带bottomBar的页面

    @BindView(R2.id.delegate_bottom_container)
    FrameLayout mContainer = null;
    @BindView(R2.id.delegate_bottom_bar)
    LinearLayoutCompat mBottomBar = null;

    private final ArrayList<BottomTabBean> TAB_BEANS = new ArrayList<>();
    private final ArrayList<BottomItemDelegate> ITEM_DELEGATE = new ArrayList<>();

    private final LinkedHashMap<BottomTabBean, BottomItemDelegate> ITEMS = new LinkedHashMap<>();
    //当前fragment页面
    private int mCurrentDelegate = 0;
    //默认打开fragment页面
    private int mIndexDelegate = 0;
    //设置点击后tab的颜色
    private int mPressColor = R.color.pressButtonColor;
    //设置默认tab颜色
    private int mNormalColor = R.color.textColorLight;

    public abstract LinkedHashMap<BottomTabBean, BottomItemDelegate> setItems(ItemFactory factory);

    //设置颜色
    public abstract int setPressColor();

    //设置默认打开页面
    public abstract int setIndexDelegate();

    @Override
    //初始化delegate
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIndexDelegate = setIndexDelegate();
        if (setPressColor() != 0) {
            mPressColor = setPressColor();
        }
        final ItemFactory factory = ItemFactory.builder();
        final LinkedHashMap<BottomTabBean, BottomItemDelegate> items = setItems(factory);
        ITEMS.putAll(items);
        //遍历获取map键值对
        for (Map.Entry<BottomTabBean, BottomItemDelegate> item : ITEMS.entrySet()) {
            final BottomTabBean key = item.getKey();
            final BottomItemDelegate value = item.getValue();
            TAB_BEANS.add(key);
            ITEM_DELEGATE.add(value);
        }
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_bottom;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        final int size = ITEMS.size();
        for (int i = 0; i < size; i++) {
            //加载bottom_item布局并设置其父布局为mBottomBar
            LayoutInflater.from(getContext())
                    .inflate(R.layout.bottom_item_icon_title_layout, mBottomBar);
            //获取mBottomBar的子布局(即bottom_item的外层布局)
            final RelativeLayout item = (RelativeLayout) mBottomBar.getChildAt(i);
            //设置item点击事件
            item.setTag(i);
            item.setOnClickListener(this);
            //获取item的icon和title
            final IconicsTextView icon = (IconicsTextView) item.getChildAt(0);
            final AppCompatTextView title = (AppCompatTextView) item.getChildAt(1);
            //获取存储的bar
            final BottomTabBean bean = TAB_BEANS.get(i);
            icon.setText(bean.getIcon());
            title.setText(bean.getTitle());
            if (i == mIndexDelegate) {
                item.setBackgroundColor(getResources().getColor(mPressColor));
            }
        }
        //获取存储的delegate转化为数组,具体原因查看源码
        final SupportFragment[] delegateArray = ITEM_DELEGATE.toArray(new SupportFragment[size]);
        //加载多个fragment
        loadMultipleRootFragment(R.id.delegate_bottom_container, mIndexDelegate, delegateArray);
    }

    //重置bar颜色
    private void resetColor() {
        final int count = mBottomBar.getChildCount();
        for (int i = 0; i < count; i++) {
            final RelativeLayout item = (RelativeLayout) mBottomBar.getChildAt(i);
            item.setBackgroundColor(getResources().getColor(mNormalColor));
        }
    }

    @Override
    public void onClick(View view) {
        final int tag = (int) view.getTag();
        resetColor();
        final RelativeLayout item = (RelativeLayout) view;
        item.setBackgroundColor(getResources().getColor(mPressColor));
        //隐藏当前fragment显示点击的fragment
        showHideFragment(ITEM_DELEGATE.get(tag), ITEM_DELEGATE.get(mCurrentDelegate));
        //重置tag为当前所选中的fragment
        mCurrentDelegate = tag;
    }
}