package com.dididi.pocket.ec.main.mall;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.dididi.pocket.ec.R;
import com.dididi.pocket.ec.R2;
import com.dididi.pocket.ec.item.SearchBarItem;
import com.dididi.pocket.ec.main.mall.adapter.NewsAdapter;
import com.dididi.pocket.ec.main.mall.entity.News;
import com.dididi.pocket.ec.main.mall.list.FakeImageList;
import com.dididi.pocket_core.app.Pocket;
import com.dididi.pocket_core.delegates.bottom.BottomItemDelegate;
import com.dididi.pocket_core.ui.SwipeRefreshLayout.PocketSwipeRefreshLayout;
import com.dididi.pocket_core.ui.banner.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

/**
 * Created by dididi
 * on 24/07/2018 .
 */

public class HomeItemDelegate extends BottomItemDelegate
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {

    @BindView(R2.id.home_item_searchBar)
    SearchBarItem mSearchBarItem = null;
    @BindView(R2.id.home_item_nav)
    NavigationView mNav = null;
    @BindView(R2.id.home_item_drawer)
    DrawerLayout mDrawer = null;
    @BindView(R2.id.home_item_banner)
    Banner mBanner = null;
    @BindView(R2.id.home_item_scroll_view)
    NestedScrollView mScrollView = null;
    @BindView(R2.id.home_item_swipe_refresh)
    PocketSwipeRefreshLayout mRefresh = null;
    @BindView(R2.id.home_item_discover)
    RecyclerView mDiscover = null;

    private FakeImageList mFakeImages = new FakeImageList();
    private List<News> mNews = new ArrayList<>();

    private NewsAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    //setLayout().onBindView()在onCreateView()方法中.
    public Object setLayout() {
        return R.layout.delegate_mall_home;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        //设置nav默认选中item
        mNav.setCheckedItem(R.id.home_item_nav_menu_discover);
        //设置刷新样式
        mRefresh.setColorSchemeColors(getResources().getColor(R.color.textColorDark));
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshNews();
            }
        });
        //初始化banner
        initBanner();
        //设置左侧图标样式
        mSearchBarItem.setLeftIcon("{faw-bars}");
        //nav item点击事件监听
        mNav.setNavigationItemSelectedListener(this);
        //toolbar左侧图标点击事件监听
        mSearchBarItem.setLeftIconListener(this);
        //toolbar搜索按钮点击事件监听
        mSearchBarItem.setSearchIconListener(this);
        //初始化发现消息
        initFakeNews();
        //设置RecyclerView布局方式及加入适配器
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mDiscover.setLayoutManager(layoutManager);
        mAdapter = new NewsAdapter(mNews);
        mDiscover.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        //开启banner自动播放
        mBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //暂停banner自动播放
        mBanner.stopAutoPlay();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    //设置navigationItem监听事件
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.home_item_nav_menu_discover) {
            Toast.makeText(getContext(),"点击跳转发现界面",Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.home_item_nav_menu_collect) {
            Toast.makeText(getContext(),"点击跳转我的收藏界面",Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.home_item_nav_menu_attention) {
            Toast.makeText(getContext(),"点击跳转我的关注界面",Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.home_item_nav_menu_friends) {
            Toast.makeText(getContext(),"点击跳转我的朋友界面",Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.home_item_nav_menu_account) {
            Toast.makeText(getContext(),"点击跳转账户界面",Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.home_item_nav_menu_exit) {
            Toast.makeText(getContext(),"点击登出账号",Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.home_item_nav_menu_setting) {
            Toast.makeText(getContext(),"点击跳转设置界面",Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.home_item_nav_menu_help) {
            Toast.makeText(getContext(),"点击跳转帮助界面",Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    //设置banner
    private void initBanner() {
        mBanner.setBannerAnimation(Transformer.Default)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setIndicatorGravity(BannerConfig.CENTER)
                .setDelayTime(1500)
                .setImageLoader(new GlideImageLoader())
                .setImages(mFakeImages.getImages())
                .start();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == mSearchBarItem.getLeftIconId()) {
            mSearchBarItem.setLeftIconListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //开启滑动菜单
                    mDrawer.openDrawer(GravityCompat.START);
                }
            });
        } else if (view.getId() == mSearchBarItem.getSearchIconId()) {
            mSearchBarItem.setSearchIconListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "click search", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void initFakeNews() {
        mNews.clear();
        News[] news = {
                new News(R.drawable.cat, "大野猫", "我是一只大大大大野猫", "27/7/2018"),
                new News(R.drawable.flower, "大菊花", "我是一朵大菊花", "28/7/2018"),
                new News(R.drawable.guitar,"大吉它",
                        "我是一把小吉他小呀小呀小呀小呀小呀小呀小呀小呀小吉他","26/7/2018"),
        };
        for (int i = 0; i < 5; i++) {
            Random random = new Random();
            int index = random.nextInt(news.length);
            mNews.add(news[index]);
        }
    }
    //刷新假数据
    private void refreshNews() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //切换回ui线程
                if (getActivity()!=null){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initFakeNews();
                            mAdapter.notifyDataSetChanged();
                            mRefresh.setRefreshing(false);
                        }
                    });
                }
            }
        }).start();
    }
}