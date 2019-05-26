package com.scriptsbundle.adforest.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.NetworkOnMainThreadException;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;
import com.scriptsbundle.adforest.Blog.BlogDetailFragment;
import com.scriptsbundle.adforest.Blog.BlogFragment;
import com.scriptsbundle.adforest.Notification.Config;
import com.scriptsbundle.adforest.R;
import com.scriptsbundle.adforest.Search.FragmentCatSubNSearch;
import com.scriptsbundle.adforest.Search.SearchActivity;
import com.scriptsbundle.adforest.Shop.shopActivity;
import com.scriptsbundle.adforest.ad_detail.Ad_detail_activity;
import com.scriptsbundle.adforest.adapters.ItemMainHomeRelatedAdapter;
import com.scriptsbundle.adforest.adapters.ItemSearchFeatureAdsAdapter;
import com.scriptsbundle.adforest.helper.BlogItemOnclicklinstener;
import com.scriptsbundle.adforest.helper.CatSubCatOnclicklinstener;
import com.scriptsbundle.adforest.helper.GridSpacingItemDecoration;
import com.scriptsbundle.adforest.helper.MyAdsOnclicklinstener;
import com.scriptsbundle.adforest.helper.OnItemClickListener;
import com.scriptsbundle.adforest.helper.OnItemClickListener2;
import com.scriptsbundle.adforest.home.adapter.ItemBlogHomeAdapter;
import com.scriptsbundle.adforest.home.adapter.ItemMainAllCatAdapter;
import com.scriptsbundle.adforest.home.adapter.ItemMainAllLocationAds;
import com.scriptsbundle.adforest.home.adapter.ItemMainCAT_Related_All;
import com.scriptsbundle.adforest.modelsList.blogModel;
import com.scriptsbundle.adforest.modelsList.catSubCatlistModel;
import com.scriptsbundle.adforest.modelsList.homeCatListModel;
import com.scriptsbundle.adforest.modelsList.homeCatRelatedList;
import com.scriptsbundle.adforest.modelsList.myAdsModel;
import com.scriptsbundle.adforest.utills.Admob;
import com.scriptsbundle.adforest.utills.AnalyticsTrackers;
import com.scriptsbundle.adforest.utills.CustomBorderDrawable;
import com.scriptsbundle.adforest.utills.Network.RestService;
import com.scriptsbundle.adforest.utills.SettingsMain;
import com.scriptsbundle.adforest.utills.UrlController;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHome extends Fragment {

    static int adsCounter = 0;
    public JSONObject jsonObjectSubMenu, responseData;
    String regId;
    ArrayList<homeCatListModel> listitems = new ArrayList<>();
    ArrayList<homeCatListModel> locationAdscat = new ArrayList<>();
    ArrayList<homeCatRelatedList> listitemsRelated = new ArrayList<>();
    ArrayList<catSubCatlistModel> featureAdsList = new ArrayList<>();
    ArrayList<blogModel> blogsArrayList = new ArrayList<>();
    ItemSearchFeatureAdsAdapter itemFeatureAdsAdapter;
    int[] iconsId = {R.drawable.ic_pages, R.drawable.ic_help_outline_black_24dp, R.drawable.ic_about_black_24dp, R.drawable.ic_file};
    LinearLayout featureAboveLayoyut, featurebelowLayoyut, featuredMidLayout;
    Menu menu;
    RestService restService;
    TextView textViewTitleFeature, textViewTitleFeatureBelow, textViewTitleFeatureMid;
    CardView catCardView;
    LinearLayout HomeCustomLayout, staticSlider;
    TextView tv_search_title, tv_search_subTitle;
    EditText et_search;
    ImageButton img_btn_search;
    RelativeLayout searchLayout;
    ImageView backgroundImage;
    Button buttonAllCat;
    private SettingsMain settingsMain;
    private ArrayList<catSubCatlistModel> latesetAdsList = new ArrayList<>();
    private ArrayList<catSubCatlistModel> nearByAdsList = new ArrayList<>();
    private RecyclerView mRecyclerView, mRecyclerView2, featuredRecylerViewAbove, featuredRecylerViewBelow,
            featuredRecylerViewMid;
    private Context context;
    //    FirebaseDatabase database;
//    DatabaseReference myRef;
    private String btnViewAllText;

    public FragmentHome() {
    }

    public void adforest_recylerview_autoScroll(final int duration, final int pixelsToMove, final int delayMillis,
                                                final RecyclerView recyclerView, final GridLayoutManager gridLayoutManager
            , final ItemSearchFeatureAdsAdapter adapter) {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int count = 0;
            boolean flag = true;

            @Override
            public void run() {
                try {
                    if (count < adapter.getItemCount()) {
                        if (count == adapter.getItemCount() - 1) {
                            flag = false;
                        } else if (count == 0) {
                            flag = true;
                        }
                        if (flag) count++;
                        else count--;

                        recyclerView.smoothScrollToPosition(count);
                        handler.postDelayed(this, duration);
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }

            }
        };

        handler.postDelayed(runnable, delayMillis);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_home, container, false);
//        database = FirebaseDatabase.getInstance();
//        myRef = database.getReference("UserLogin");
        context = getActivity();
        settingsMain = new SettingsMain(getActivity());

        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);

        // get menu from navigationView
        menu = navigationView.getMenu();

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        ViewCompat.setNestedScrollingEnabled(mRecyclerView, false);

        featureAboveLayoyut = view.findViewById(R.id.featureAboveLayoyut);
        featurebelowLayoyut = view.findViewById(R.id.featureAboveLayoutBelow);
        featuredMidLayout = view.findViewById(R.id.featureLayoutMid);
        textViewTitleFeature = view.findViewById(R.id.textView6);
        textViewTitleFeatureBelow = view.findViewById(R.id.textView7);
        textViewTitleFeatureMid = view.findViewById(R.id.textView8);
        featuredRecylerViewAbove = view.findViewById(R.id.recycler_view3);
        featuredRecylerViewBelow = view.findViewById(R.id.featuredRecylerViewBelow);
        featuredRecylerViewMid = view.findViewById(R.id.featuredRecylerViewMid);
        catCardView = view.findViewById(R.id.card_view);
        buttonAllCat = view.findViewById(R.id.buttonAllCat);
        buttonAllCat.setBackground(CustomBorderDrawable.customButton(6, 6, 6, 6, SettingsMain.getMainColor(), SettingsMain.getMainColor(), SettingsMain.getMainColor(), 3));

        HomeCustomLayout = view.findViewById(R.id.HomeCustomLayout);
        staticSlider = view.findViewById(R.id.linear1);
        mRecyclerView2 = view.findViewById(R.id.recycler_view2);

        tv_search_title = view.findViewById(R.id.tv_search_title);
        tv_search_subTitle = view.findViewById(R.id.tv_search_subTitle);
        et_search = view.findViewById(R.id.et_search);
        img_btn_search = view.findViewById(R.id.img_btn_search);
        searchLayout = view.findViewById(R.id.searchLayout);
        backgroundImage = view.findViewById(R.id.backgroundImage);


        if (settingsMain.getRTL()) {
            Drawable mDrawable = context.getResources().getDrawable(R.drawable.bg_home_search_clickrtl);
            mDrawable.setColorFilter(new
                    PorterDuffColorFilter(0xffff00, PorterDuff.Mode.MULTIPLY));
            img_btn_search.setBackgroundDrawable(mDrawable);
        } else {

            Drawable drawable = getResources().getDrawable(R.drawable.bg_home_search_click).mutate();
            drawable.setColorFilter(Color.parseColor(SettingsMain.getMainColor()), PorterDuff.Mode.SRC_ATOP);

            img_btn_search.setBackground(drawable);
        }
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Intent intent = new Intent(getContext(), SearchActivity.class);
                    intent.putExtra("ad_title", et_search.getText().toString());
                    intent.putExtra("requestFrom", "Home");
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                }
                return false;
            }
        });
        img_btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                intent.putExtra("ad_title", et_search.getText().toString());
                intent.putExtra("requestFrom", "Home");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            }
        });


        if (settingsMain.getAppOpen()) {
            restService = UrlController.createService(RestService.class);
        } else
            restService = UrlController.createService(RestService.class, settingsMain.getUserEmail(), settingsMain.getUserPassword(), getActivity());

        ((HomeActivity) getActivity()).changeImage();


        adforest_getAllData();

        if (!settingsMain.getUserLogin().equals("0")) {
            Log.d("info Firebase topic", "subscribe");

            FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
        }
        Log.d("FireBaseId", settingsMain.getFireBaseId());
        SharedPreferences pref = getActivity().getSharedPreferences(Config.SHARED_PREF, 0);
        regId = pref.getString("Firebase Regid", null);
        if (settingsMain.getFireBaseId().equals("") && !settingsMain.getUserLogin().equals("0")) {
            adforest_AddFirebaseid(regId);

        }
        SwipeRefreshLayout swipeRefreshLayout = getActivity().findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setEnabled(true);

        buttonAllCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentAllCategories fragmentAllCategories = new FragmentAllCategories();
                replaceFragment(fragmentAllCategories, "FragmentAllCategories");
            }
        });

//        if (!settingsMain.getUserLogin().equals("0")) {
//
//            ChatUserModel userModel = new ChatUserModel(true, settingsMain.getUserLogin());
//            myRef.child(settingsMain.getUserLogin()).setValue(userModel);
//        }
        return view;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    private void adforest_getAllData() {

        if (SettingsMain.isConnectingToInternet(getActivity())) {

            SettingsMain.showDilog(getActivity());
            Call<ResponseBody> myCall = restService.getHomeDetails(UrlController.AddHeaders(getActivity()));
            myCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                    try {
                        if (responseObj.isSuccessful()) {
                            Log.d("info HomeGet Responce", "" + responseObj.toString());

                            JSONObject response = new JSONObject(responseObj.body().string());
                            if (response.getBoolean("success")) {
                                responseData = response.getJSONObject("data");

                                getActivity().setTitle(response.getJSONObject("data").getString("page_title"));

                                btnViewAllText = response.getJSONObject("data").getString("view_all");
                                JSONObject sharedSettings = response.getJSONObject("settings");


                                jsonObjectSubMenu = response.getJSONObject("data").getJSONObject("menu").getJSONObject("submenu");

                                if (jsonObjectSubMenu.getBoolean("has_page")) {

                                    menu.findItem(R.id.custom).setVisible(true);

                                    final JSONArray jsonArray = jsonObjectSubMenu.getJSONArray("pages");
                                    menu.findItem(R.id.custom).setTitle(jsonObjectSubMenu.getString("title"));
                                    menu.findItem(R.id.custom).getSubMenu().clear();
                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        menu.findItem(R.id.custom).getSubMenu().add(0, jsonArray.getJSONObject(i).getInt("page_id"), Menu.NONE,
                                                jsonArray.getJSONObject(i).getString("page_title"));

                                        final int finalI = i;
                                        menu.findItem(R.id.custom).getSubMenu().getItem(i).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                            @Override
                                            public boolean onMenuItemClick(MenuItem menuItem) {
                                                Bundle bundle = new Bundle();
                                                try {
                                                    if (jsonArray.getJSONObject(finalI).getString("type").equals("webview")) {
                                                        bundle.putString("page_url", jsonArray.getJSONObject(finalI).getString("page_url"));
                                                        bundle.putString("pageTitle", jsonArray.getJSONObject(finalI).getString("page_title"));
                                                    } else {
                                                        bundle.putString("page_url", "");
                                                        bundle.putString("pageTitle", "");
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                FragmentCustomPages fragment_search = new FragmentCustomPages();

                                                bundle.putString("id", "" + menuItem.getItemId());

                                                fragment_search.setArguments(bundle);
                                                replaceFragment(fragment_search, "FragmentCustomPages");

                                                //Log.d("bject", menuItem.getGroupId() + " ==sdf == " + menuItem.getItemId() + " === " + menuItem.getTitle());
                                                return false;
                                            }
                                        });
                                    }
                                    menu.findItem(R.id.custom).getSubMenu().setGroupCheckable(0, true, true);
                                } else {
                                    menu.findItem(R.id.custom).setVisible(false);
                                }
                                if (jsonObjectSubMenu.getBoolean("has_page")) {
                                    AsyncImageTask asyncImageTask = new AsyncImageTask();
                                    asyncImageTask.execute();
                                }
                                menu.findItem(R.id.home).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("home"));
                                menu.findItem(R.id.search).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("search"));
                                menu.findItem(R.id.packages).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("packages"));

                                if (settingsMain.getAppOpen()) {
                                    menu.findItem(R.id.message).setVisible(false);
                                    menu.findItem(R.id.profile).setVisible(false);
                                    menu.findItem(R.id.myAds).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("login"));
                                    menu.findItem(R.id.inActiveAds).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("register"));
                                    menu.findItem(R.id.myAds).setIcon(R.drawable.ic_login_icon);
                                    menu.findItem(R.id.inActiveAds).setIcon(R.drawable.ic_register_user);
                                    menu.findItem(R.id.featureAds).setVisible(false);
                                    menu.findItem(R.id.favAds).setVisible(false);
                                    menu.findItem(R.id.nav_log_out).setVisible(false);
                                } else {
                                    menu.findItem(R.id.message).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("messages"));
                                    menu.findItem(R.id.profile).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("profile"));
                                    menu.findItem(R.id.myAds).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("my_ads"));
                                    menu.findItem(R.id.inActiveAds).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("inactive_ads"));
                                    menu.findItem(R.id.featureAds).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("featured_ads"));
                                    menu.findItem(R.id.favAds).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("fav_ads"));
                                }
                                menu.findItem(R.id.other).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("others"));
                                menu.findItem(R.id.nav_blog).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("blog"));
                                menu.findItem(R.id.nav_log_out).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("logout"));
                                menu.findItem(R.id.nav_shop).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("shop"));
                                menu.findItem(R.id.nav_settings).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("app_settings"));
                                menu.findItem(R.id.nav_sellers).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("sellers"));

                                shopActivity.title = response.getJSONObject("data").getJSONObject("menu").getString("shop");
                                JSONObject jsonObjectMenu = response.getJSONObject("data").getJSONObject("menu").getJSONObject("is_show_menu");
                                if (!jsonObjectMenu.getBoolean("blog")) {
                                    menu.findItem(R.id.nav_blog).setVisible(false);
                                }
                                if (!jsonObjectMenu.getBoolean("message")) {
                                    menu.findItem(R.id.message).setVisible(false);
                                }
                                if (!jsonObjectMenu.getBoolean("package") ||
                                        !response.getJSONObject("data").getJSONObject("menu").getBoolean("menu_is_show_packages")) {
                                    menu.findItem(R.id.packages).setVisible(false);
                                }
                                if (!jsonObjectMenu.getBoolean("shop")) {
                                    menu.findItem(R.id.nav_shop).setVisible(false);
                                }
                                if (!jsonObjectMenu.getBoolean("settings")) {
                                    menu.findItem(R.id.nav_settings).setVisible(false);
                                }
                                if (!jsonObjectMenu.getBoolean("sellers")) {
                                    menu.findItem(R.id.nav_sellers).setVisible(false);
                                }


                                if (responseData.getBoolean("ads_position_sorter")) {
                                    HomeCustomLayout.setVisibility(View.VISIBLE);
                                    adforest_showDynamicViews(responseData.getJSONArray("ads_position"));
                                } else {
                                    if (responseData.getJSONObject("cat_icons_column_btn").getBoolean("is_show")) {
                                        buttonAllCat.setVisibility(View.VISIBLE);
                                        buttonAllCat.setText(responseData.getJSONObject("cat_icons_column_btn").getString("text"));
                                    }
                                    if (response.getJSONObject("data").getJSONArray("cat_icons").length() == 0) {
                                        catCardView.setVisibility(View.GONE);
                                    } else {
                                        adforest_setAllCatgories(response.getJSONObject("data").getJSONArray("cat_icons"),
                                                response.getJSONObject("data").getInt("cat_icons_column"), mRecyclerView);
                                        catCardView.setVisibility(View.VISIBLE);
                                        Log.e("infoi", "asdasdasdsa");
                                    }
                                    if (response.getJSONObject("data").getJSONArray("sliders").length() > 0) {
                                        staticSlider.setVisibility(View.VISIBLE);
                                        adforest_setAllRelated(response.getJSONObject("data").getJSONArray("sliders"), mRecyclerView2);
                                    }
                                    if (response.getJSONObject("data").getBoolean("is_show_featured")) {
                                        JSONObject featuredObject = response.getJSONObject("data").getJSONObject("featured_ads");
                                        String featuredPosition = response.getJSONObject("data").getString("featured_position");

                                        switch (featuredPosition) {
                                            case "1":
                                                Log.e("infoi", "asdasdasdsa" + featuredPosition);
                                                featureAboveLayoyut.setVisibility(View.VISIBLE);
                                                adforest_setAllFeaturedAds(featuredObject, featuredRecylerViewAbove, textViewTitleFeature);
                                                break;
                                            case "2":
                                                Log.e("infoi", "asdasdasdsa" + featuredPosition);

                                                featuredMidLayout.setVisibility(View.VISIBLE);
                                                adforest_setAllFeaturedAds(featuredObject, featuredRecylerViewMid, textViewTitleFeatureMid);
                                                break;
                                            case "3":
                                                Log.e("infoi", "asdasdasdsa" + featuredPosition);
                                                featurebelowLayoyut.setVisibility(View.VISIBLE);
                                                adforest_setAllFeaturedAds(featuredObject, featuredRecylerViewBelow, textViewTitleFeatureBelow);
                                                break;
                                        }
                                    }
                                }


                                settingsMain.setKey("stripeKey", sharedSettings.getJSONObject("appKey").getString("stripe"));


                                settingsMain.setAdsShow(sharedSettings.getJSONObject("ads").getBoolean("show"));
                                if (settingsMain.getAdsShow()) {
//                                    settingsMain.setAdsType(sharedSettings.getJSONObject("ads").getString("type"));
                                    JSONObject jsonObjectAds = sharedSettings.getJSONObject("ads");
                                    if (jsonObjectAds.getBoolean("is_show_banner")) {
                                        Log.d("info banner", jsonObjectAds.toString());

                                        settingsMain.setBannerShow(jsonObjectAds.getBoolean("is_show_banner"));
                                        settingsMain.setAdsPosition(jsonObjectAds.getString("position"));
                                        settingsMain.setBannerAdsId(jsonObjectAds.getString("banner_id"));

                                    } else {
                                        settingsMain.setBannerShow(false);
                                        settingsMain.setAdsPosition("");
                                        settingsMain.setBannerAdsId("");
                                    }
                                    if (jsonObjectAds.getBoolean("is_show_initial")) {
                                        Log.d("info initial", jsonObjectAds.toString());
                                        settingsMain.setInterstitalShow(jsonObjectAds.getBoolean("is_show_initial"));
                                        settingsMain.setAdsInitialTime(jsonObjectAds.getString("time_initial"));
                                        settingsMain.setAdsDisplayTime(jsonObjectAds.getString("time"));
                                        settingsMain.setInterstitialAdsId(jsonObjectAds.getString("interstital_id"));
                                    } else {
                                        settingsMain.setInterstitalShow(false);
                                        settingsMain.setAdsInitialTime("");
                                        settingsMain.setAdsDisplayTime("");
                                        settingsMain.setInterstitialAdsId("");
                                    }
                                } else {
                                    settingsMain.setBannerShow(false);
                                    settingsMain.setAdsPosition("");
                                    settingsMain.setBannerAdsId("");
                                    settingsMain.setInterstitalShow(false);
                                    settingsMain.setAdsInitialTime("");
                                    settingsMain.setAdsDisplayTime("");
                                    settingsMain.setInterstitialAdsId("");
                                }
                                settingsMain.setAnalyticsShow(sharedSettings.getJSONObject("analytics").getBoolean("show"));
                                if (sharedSettings.getJSONObject("analytics").getBoolean("show")) {
                                    settingsMain.setAnalyticsId(sharedSettings.getJSONObject("analytics").getString("id"));
                                    Log.d("analytica======>", sharedSettings.getJSONObject("analytics").getString("id"));
                                }
                                settingsMain.setYoutubeApi(sharedSettings.getJSONObject("appKey").getString("youtube"));
                                googleAnalytics();
                                AdsNDAnalytics();
                                adforest_searchLayout(response.getJSONObject("data"));

                            } else {
                                Toast.makeText(getActivity(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        SettingsMain.hideDilog();

                    } catch (JSONException e) {
                        SettingsMain.hideDilog();
                        e.printStackTrace();
                    } catch (IOException e) {
                        SettingsMain.hideDilog();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    SettingsMain.hideDilog();
                    Log.d("info HomeGet error", String.valueOf(t));
                    Log.d("info HomeGet error", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                }
            });

        } else {
            SettingsMain.hideDilog();
            Toast.makeText(getActivity(), "Internet error", Toast.LENGTH_SHORT).show();
        }
    }


    private void adforest_searchLayout(JSONObject jsonObject) {
        try {
            JSONObject search_section = jsonObject.getJSONObject("search_section");
            if (search_section.getBoolean("is_show")) {
                searchLayout.setVisibility(View.VISIBLE);
                tv_search_title.setText(search_section.getString("main_title"));
                tv_search_subTitle.setText(search_section.getString("sub_title"));
                et_search.setHint(search_section.getString("placeholder"));
                if (!TextUtils.isEmpty(search_section.getString("image"))) {
                    Picasso.with(getContext()).load(search_section.getString("image"))
                            .error(R.drawable.placeholder)
                            .placeholder(R.drawable.placeholder)
                            .into(backgroundImage);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void adforest_showDynamicViews(JSONArray jsonArray) {
        Log.d("info ads_position", jsonArray.toString());
        for (int i = 0; i < jsonArray.length(); i++) {
            try {

                if (jsonArray.get(i).equals("cat_icons") && responseData.getJSONArray("cat_icons").length() > 0) {
                    CardView cardView = new CardView(getActivity());
                    cardView.setCardElevation(3);
                    cardView.setUseCompatPadding(true);
                    cardView.setRadius(0);
                    cardView.setContentPadding(5, 5, 5, 5);
                    LinearLayout linearLayout = new LinearLayout(getActivity());
                    linearLayout.setOrientation(LinearLayout.VERTICAL);

                    RecyclerView recyclerView = new RecyclerView(getActivity());
                    recyclerView.setScrollContainer(false);
                    recyclerView.setPadding(5, 5, 5, 5);

                    recyclerView.setHasFixedSize(true);
                    recyclerView.setNestedScrollingEnabled(false);
                    ViewCompat.setNestedScrollingEnabled(recyclerView, false);
                    linearLayout.addView(recyclerView);

                    try {
                        if (responseData.getJSONObject("cat_icons_column_btn").getBoolean("is_show")) {
                            Button button = new Button(getActivity());
                            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            buttonParams.setMargins(0, 10, 0, 20);
                            buttonParams.setMarginStart(20);
                            buttonParams.setMarginEnd(20);
                            button.setText(responseData.getJSONObject("cat_icons_column_btn").getString("text"));

                            button.setTextColor(Color.WHITE);
                            button.setTextSize(14);
                            button.setLayoutParams(buttonParams);
                            button.setTransformationMethod(null);
                            button.setBackground(CustomBorderDrawable.customButton(6, 6, 6, 6, settingsMain.getMainColor(), settingsMain.getMainColor(), settingsMain.getMainColor(), 3));
                            linearLayout.addView(button);
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    FragmentAllCategories fragmentAllCategories = new FragmentAllCategories();
                                    replaceFragment(fragmentAllCategories, "FragmentAllCategories");
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    cardView.addView(linearLayout);

                    HomeCustomLayout.addView(cardView);

                    adforest_setAllCatgories(responseData.getJSONArray("cat_icons"),
                            responseData.getInt("cat_icons_column"), recyclerView);
                }
                if (jsonArray.get(i).equals("sliders")) {

                    RecyclerView recyclerView = new RecyclerView(getActivity());
                    HomeCustomLayout.addView(recyclerView);
                    adforest_setAllRelated(responseData.getJSONArray("sliders"), recyclerView);
                }
                if (jsonArray.get(i).equals("featured_ads")) {

                    if (responseData.getBoolean("is_show_featured")) {

                        RecyclerView recyclerView = new RecyclerView(getActivity());
                        recyclerView.setPadding(4, 4, 4, 4);

                        TextView textView = new TextView(getActivity());
                        textView.setPadding(5, 5, 5, 5);
                        textView.setTextColor(Color.BLACK);
                        textView.setTextSize(18);

                        HomeCustomLayout.addView(textView);
                        HomeCustomLayout.addView(recyclerView);
                        adforest_setAllFeaturedAds(responseData.getJSONObject("featured_ads"), recyclerView, textView);
                    }
                }
                if (jsonArray.get(i).equals("latest_ads")) {
                    if (responseData.getBoolean("is_show_latest")) {
                        adforest_latesetAdsAndNearBy(responseData.getJSONObject("latest_ads"), latesetAdsList, "latest");
                    }
                }
                if (jsonArray.get(i).equals("cat_locations")) {
                    if (responseData.getJSONArray("cat_locations").length() > 0) {
                        adforest_locationAds();
                    }
                }
                if (jsonArray.get(i).equals("nearby")) {
                    if (responseData.getBoolean("is_show_nearby")) {
                        adforest_latesetAdsAndNearBy(responseData.getJSONObject("nearby_ads"), nearByAdsList, "nearby");
                    }
                }
                if (jsonArray.get(i).equals("blogNews")) {
                    adforest_setBlogs();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void adforest_setBlogs() {
        try {
            LinearLayout firstLayout = new LinearLayout(getActivity());
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            firstLayout.setOrientation(LinearLayout.HORIZONTAL);
            firstLayout.setPadding(2, 2, 2, 2);
            firstLayout.setLayoutParams(params2);


            TextView title = new TextView(getActivity());
            LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            params3.weight = 1;
            title.setPadding(3, 3, 3, 3);
            title.setTextColor(Color.BLACK);
            title.setLayoutParams(params3);
            title.setTextSize(18);

            TextView buttonAll = new TextView(getActivity());
            LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            buttonAll.setLayoutParams(params4);

            buttonAll.setPaddingRelative(23, 10, 23, 10);
            buttonAll.setTextColor(Color.WHITE);
            buttonAll.setBackground(CustomBorderDrawable.customButton(0, 0, 0, 0, settingsMain.getMainColor(), settingsMain.getMainColor(), settingsMain.getMainColor(), 3));

            firstLayout.addView(title);
            firstLayout.addView(buttonAll);

            RecyclerView recyclerView = new RecyclerView(getActivity());
            recyclerView.setPadding(4, 4, 4, 4);


            HomeCustomLayout.addView(firstLayout);
            HomeCustomLayout.addView(recyclerView);

            if (responseData.getBoolean("is_show_blog")) {
                JSONObject blogsobject = responseData.getJSONObject("latest_blog");
                JSONArray blogsArray = blogsobject.getJSONArray("blogs");
                if (blogsArray.length() > 0) {

                    blogsArrayList.clear();
                    buttonAll.setText(responseData.getString("view_all"));
                    title.setText(blogsobject.getString("text"));

                    GridLayoutManager MyLayoutManager2 = new GridLayoutManager(getActivity(), 1);
                    MyLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setNestedScrollingEnabled(false);
                    recyclerView.setLayoutManager(MyLayoutManager2);
                    ViewCompat.setNestedScrollingEnabled(recyclerView, false);

                    for (int i = 0; i < blogsArray.length(); i++) {
                        blogModel item = new blogModel();
                        JSONObject jsonObject = blogsArray.getJSONObject(i);
                        item.setPostId(jsonObject.getString("post_id"));
                        item.setName(jsonObject.getString("title"));
                        item.setCategory(jsonObject.getJSONArray("cats").getJSONObject(0).getString("name"));
                        item.setDate(jsonObject.getString("date"));
                        item.setImage(jsonObject.getString("image"));
                        item.setHasImage(jsonObject.getBoolean("has_image"));
                        blogsArrayList.add(item);
                    }
                    ItemBlogHomeAdapter itemBlogHomeAdapter = new ItemBlogHomeAdapter(getContext(), blogsArrayList);
                    recyclerView.setAdapter(itemBlogHomeAdapter);
                    itemBlogHomeAdapter.setOnItemClickListener(new BlogItemOnclicklinstener() {
                        @Override
                        public void onItemClick(blogModel item) {
                            BlogDetailFragment fragment = new BlogDetailFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("id", item.getPostId());
                            fragment.setArguments(bundle);

                            replaceFragment(fragment, "BlogDetailFragment");
                        }
                    });
                    buttonAll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            replaceFragment(new BlogFragment(), "BlogFragment");
                        }
                    });

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void adforest_setAllFeaturedAds(JSONObject featureObject, RecyclerView featuredRecylerView,
                                            TextView textViewTitleFeature) {
        featureAdsList.clear();

        GridLayoutManager MyLayoutManager2 = new GridLayoutManager(getActivity(), 1);
        MyLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);

        featuredRecylerView.setHasFixedSize(true);
        featuredRecylerView.setNestedScrollingEnabled(false);
        featuredRecylerView.setLayoutManager(MyLayoutManager2);

        ViewCompat.setNestedScrollingEnabled(featuredRecylerView, false);

        try {

            textViewTitleFeature.setText(featureObject.getString("text"));
            if (featureObject.getJSONArray("ads").length() > 0)
                for (int i = 0; i < featureObject.getJSONArray("ads").length(); i++) {

                    catSubCatlistModel item = new catSubCatlistModel();
                    JSONObject object = featureObject.getJSONArray("ads").getJSONObject(i);

                    item.setAddTypeFeature(object.getJSONObject("ad_status").getString("featured_type_text"));
                    item.setId(object.getString("ad_id"));
                    item.setCardName(object.getString("ad_title"));
                    item.setDate(object.getString("ad_date"));
                    item.setAdViews(object.getString("ad_views"));
                    item.setPath(object.getString("ad_cats_name"));
                    item.setPrice(object.getJSONObject("ad_price").getString("price"));
                    item.setImageResourceId((object.getJSONArray("ad_images").getJSONObject(0).getString("thumb")));
                    item.setLocation(object.getJSONObject("ad_location").getString("address"));
                    item.setIsfav(object.getJSONObject("ad_saved").getInt("is_saved"));
                    item.setFavBtnText(object.getJSONObject("ad_saved").getString("text"));
                    item.setIs_show_countDown(object.getJSONObject("ad_timer").getBoolean("is_show"));
                    if (object.getJSONObject("ad_timer").getBoolean("is_show"))
                        item.setTimer_array(object.getJSONObject("ad_timer").getJSONArray("timer"));
                    item.setIsturned(1);
                    featureAdsList.add(item);
                }
            itemFeatureAdsAdapter = new ItemSearchFeatureAdsAdapter(getActivity(), featureAdsList);

            featuredRecylerView.setAdapter(itemFeatureAdsAdapter);


            itemFeatureAdsAdapter.setOnItemClickListener(new CatSubCatOnclicklinstener() {
                @Override
                public void onItemClick(catSubCatlistModel item) {
                    Intent intent = new Intent(getActivity(), Ad_detail_activity.class);
                    intent.putExtra("adId", item.getId());
                    getActivity().startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                }

                @Override
                public void onItemTouch(catSubCatlistModel item) {
//                    Intent intent = new Intent(getActivity(), Ad_detail_activity.class);
//                    intent.putExtra("adId", item.getId());
//                    getActivity().startActivity(intent);
//                    getActivity().overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                }

                @Override
                public void addToFavClick(View v, String position) {
                }
            });
            if (settingsMain.isFeaturedScrollEnable()) {
                adforest_recylerview_autoScroll(settingsMain.getFeaturedScroolDuration(),
                        40, settingsMain.getFeaturedScroolLoop(),
                        featuredRecylerView, MyLayoutManager2, itemFeatureAdsAdapter);
            }
        } catch (JSONException e)

        {
            e.printStackTrace();
        }

    }

    private void adforest_setAllCatgories(JSONArray jsonArray, int noOfCol, RecyclerView recyclerView) {
        listitems = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            homeCatListModel item = new homeCatListModel();
            item.setTitle(jsonArray.optJSONObject(i).optString("name"));
            item.setThumbnail(jsonArray.optJSONObject(i).optString("img"));
            item.setId(jsonArray.optJSONObject(i).optString("cat_id"));

            listitems.add(item);
        }

        GridLayoutManager MyLayoutManager = new GridLayoutManager(getActivity(), noOfCol);
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(MyLayoutManager);
        int spacing = 30; // 50px

        recyclerView.addItemDecoration(new GridSpacingItemDecoration(noOfCol, spacing, false));

        ItemMainAllCatAdapter adapter = new ItemMainAllCatAdapter(context, listitems, noOfCol);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(homeCatListModel item) {
                //Toast.makeText(getContext(), item.getTitle(), Toast.LENGTH_LONG).show();
                FragmentCatSubNSearch fragment_search = new FragmentCatSubNSearch();
                Bundle bundle = new Bundle();
                bundle.putString("id", item.getId());
                bundle.putString("title", "");

                fragment_search.setArguments(bundle);
                replaceFragment(fragment_search, "FragmentCatSubNSearch");

            }
        });
    }

    private void adforest_setAllRelated(JSONArray jsonArray, RecyclerView recyclerView) {

        listitemsRelated.clear();

        GridLayoutManager MyLayoutManager2 = new GridLayoutManager(getActivity(), 1);
        MyLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.setLayoutManager(MyLayoutManager2);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        Log.d("data array", "" + jsonArray.length());
        for (int each = 0; each < jsonArray.length(); each++) {
            homeCatRelatedList relateItem = new homeCatRelatedList();
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(each);

                relateItem.setTitle(jsonObject.getString("name"));
                relateItem.setViewAllBtnText(btnViewAllText);
                relateItem.setCatId(jsonObject.getString("cat_id"));
                JSONArray innerList = jsonObject.getJSONArray("data");

                ArrayList<catSubCatlistModel> list = new ArrayList<>();

                for (int i = 0; i < innerList.length(); i++) {
                    catSubCatlistModel item = new catSubCatlistModel();

                    item.setId(innerList.getJSONObject(i).getString("ad_id"));
                    item.setCardName(innerList.getJSONObject(i).getString("ad_title"));
                    item.setDate(innerList.getJSONObject(i).getString("ad_date"));
                    item.setPrice(innerList.getJSONObject(i).getJSONObject("ad_price").getString("price"));
                    item.setLocation(innerList.getJSONObject(i).getJSONObject("ad_location").getString("address"));
                    item.setImageResourceId(innerList.getJSONObject(i).getJSONArray("ad_images").getJSONObject(0).getString("thumb"));

                    item.setIs_show_countDown(innerList.getJSONObject(i).getJSONObject("ad_timer").getBoolean("is_show"));
                    if (innerList.getJSONObject(i).getJSONObject("ad_timer").getBoolean("is_show"))
                        item.setTimer_array(innerList.getJSONObject(i).getJSONObject("ad_timer").getJSONArray("timer"));
                    list.add(item);
                }

                relateItem.setArrayList(list);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            listitemsRelated.add(relateItem);
        }


        ItemMainCAT_Related_All itemMainCAT_related_all = new ItemMainCAT_Related_All(context, listitemsRelated);
        recyclerView.setAdapter(itemMainCAT_related_all);

        itemMainCAT_related_all.setOnItemClickListener(new MyAdsOnclicklinstener() {
            @Override
            public void onItemClick(myAdsModel item) {

            }

            @Override
            public void delViewOnClick(View v, int position) {
                FragmentCatSubNSearch fragment_search = new FragmentCatSubNSearch();
                Bundle bundle = new Bundle();
                bundle.putString("id", v.getTag().toString());
                bundle.putString("title", "");

                fragment_search.setArguments(bundle);
                replaceFragment(fragment_search, "FragmentCatSubNSearch");
            }

            @Override
            public void editViewOnClick(View v, int position) {

            }
        });

    }

    private void adforest_latesetAdsAndNearBy(JSONObject jsonObject, ArrayList arrayList, String checkAdsType) {

        LinearLayout firstLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        firstLayout.setOrientation(LinearLayout.HORIZONTAL);
        params2.setMargins(0, 10, 0, 10);
        firstLayout.setLayoutParams(params2);


        TextView title = new TextView(getActivity());
        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        params3.weight = 1;
        title.setPaddingRelative(3, 3, 3, 3);
        title.setTextColor(Color.BLACK);
        title.setLayoutParams(params3);
        title.setTextSize(18);

        TextView buttonAll = new TextView(getActivity());
        LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonAll.setLayoutParams(params4);

        buttonAll.setPaddingRelative(23, 10, 23, 10);
        buttonAll.setTextColor(Color.WHITE);
        buttonAll.setBackground(CustomBorderDrawable.customButton(0, 0, 0, 0, settingsMain.getMainColor(), settingsMain.getMainColor(), settingsMain.getMainColor(), 3));

        firstLayout.addView(title);
        firstLayout.addView(buttonAll);

        RecyclerView recyclerView = new RecyclerView(getActivity());
        recyclerView.setPaddingRelative(4, 4, 4, 4);


        HomeCustomLayout.addView(firstLayout);
        HomeCustomLayout.addView(recyclerView);
        adforest_setAllLatesetAds(title, buttonAll, recyclerView, jsonObject, arrayList, checkAdsType);
    }

    private void adforest_setAllLatesetAds(TextView title, final TextView viewAll, RecyclerView recyclerView,
                                           JSONObject jsonObject, ArrayList arrayList, final String checkAdsType) {

        arrayList.clear();
        GridLayoutManager MyLayoutManager2 = new GridLayoutManager(getActivity(), 1);
        MyLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView.setHasFixedSize(true);

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(MyLayoutManager2);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);


        try {
            JSONObject object = jsonObject;

            JSONArray data = object.getJSONArray("ads");

            title.setText(object.getString("text"));
            viewAll.setText(responseData.getString("view_all"));


            for (int i = 0; i < data.length(); i++) {
                catSubCatlistModel item = new catSubCatlistModel();

                item.setId(data.getJSONObject(i).getString("ad_id"));
                item.setCardName(data.getJSONObject(i).getString("ad_title"));
                item.setDate(data.getJSONObject(i).getString("ad_date"));
                item.setPrice(data.getJSONObject(i).getJSONObject("ad_price").getString("price"));
                item.setLocation(data.getJSONObject(i).getJSONObject("ad_location").getString("address"));
                item.setImageResourceId(data.getJSONObject(i).getJSONArray("ad_images").getJSONObject(0).getString("thumb"));

                item.setIs_show_countDown(data.getJSONObject(i).getJSONObject("ad_timer").getBoolean("is_show"));
                if (data.getJSONObject(i).getJSONObject("ad_timer").getBoolean("is_show"))
                    item.setTimer_array(data.getJSONObject(i).getJSONObject("ad_timer").getJSONArray("timer"));


                arrayList.add(item);
            }

            ItemMainHomeRelatedAdapter adapter = new ItemMainHomeRelatedAdapter(getActivity(), arrayList);
            recyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(new OnItemClickListener2() {
                @Override
                public void onItemClick(catSubCatlistModel item) {
                    Log.d("item_id", item.getId());
                    Intent intent = new Intent(getActivity(), Ad_detail_activity.class);
                    intent.putExtra("adId", item.getId());
                    startActivity(intent);
                }
            });

            viewAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkAdsType.equals("nearby")) {
                        FragmentCatSubNSearch fragment_search = new FragmentCatSubNSearch();
                        Bundle bundle = new Bundle();
                        bundle.putString("nearby_latitude", settingsMain.getLatitude());
                        bundle.putString("nearby_longitude", settingsMain.getLongitude());
                        bundle.putString("nearby_distance", settingsMain.getDistance());

                        fragment_search.setArguments(bundle);
                        replaceFragment(fragment_search, "FragmentCatSubNSearch");
                    }
                    if (checkAdsType.equals("latest")) {
                        FragmentCatSubNSearch fragment_search = new FragmentCatSubNSearch();
                        Bundle bundle = new Bundle();
                        bundle.putString("id", "");
                        bundle.putString("title", "");

                        fragment_search.setArguments(bundle);
                        replaceFragment(fragment_search, "FragmentCatSubNSearch");
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void adforest_locationAds() {
        TextView title = new TextView(getActivity());
        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        title.setPadding(3, 3, 3, 3);
        title.setPaddingRelative(3, 3, 3, 3);
        title.setTextColor(Color.BLACK);
        title.setLayoutParams(params3);
        title.setTextSize(17);


        CardView cardView = new CardView(getActivity());
        cardView.setCardElevation(3);
        cardView.setUseCompatPadding(true);
        cardView.setRadius(0);
//        cardView.setContentPadding(5, 5, 5, 5);
        cardView.setPaddingRelative(5, 5, 5, 5);

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        RecyclerView recyclerView = new RecyclerView(getActivity());
        recyclerView.setScrollContainer(false);
//        recyclerView.setPadding(5, 5, 5, 5);
        recyclerView.setPaddingRelative(5, 5, 5, 5);

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
        linearLayout.addView(recyclerView);

        try {
            if (responseData.getJSONObject("cat_locations_btn").getBoolean("is_show")) {
                Button button = new Button(getActivity());
                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                buttonParams.setMargins(0, 10, 0, 20);
                buttonParams.setMarginStart(20);
                buttonParams.setMarginEnd(20);
                button.setText(responseData.getJSONObject("cat_locations_btn").getString("text"));

                button.setTextColor(Color.WHITE);
                button.setTextSize(14);
                button.setLayoutParams(buttonParams);
                button.setTransformationMethod(null);
                button.setBackground(CustomBorderDrawable.customButton(6, 6, 6, 6, settingsMain.getMainColor(), settingsMain.getMainColor(), settingsMain.getMainColor(), 3));
                linearLayout.addView(button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentAllLocations fragmentAllLocations = new FragmentAllLocations();
                        replaceFragment(fragmentAllLocations, "FragmentAllLocations");
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cardView.addView(linearLayout);

        HomeCustomLayout.addView(title);
        HomeCustomLayout.addView(cardView);
        try {
            title.setText(responseData.getString("cat_locations_title"));
            adforest_setAllLocationAds(responseData.getJSONArray("cat_locations"), recyclerView);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void adforest_setAllLocationAds(JSONArray jsonArray, RecyclerView recyclerView) {
        locationAdscat.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            homeCatListModel item = new homeCatListModel();
            try {
                Log.d("info location icons", jsonArray.getJSONObject(i).getString("count"));
                item.setTitle(jsonArray.optJSONObject(i).getString("name"));
                item.setThumbnail(jsonArray.optJSONObject(i).getString("img"));
                item.setId(jsonArray.optJSONObject(i).getString("cat_id"));
                item.setAdsCount(jsonArray.getJSONObject(i).getString("count"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            locationAdscat.add(item);
        }

        GridLayoutManager MyLayoutManager = new GridLayoutManager(getActivity(), 2);
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(MyLayoutManager);
        int spacing = 15; // 50px

        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacing, false));

        ItemMainAllLocationAds adapter = new ItemMainAllLocationAds(context, locationAdscat, 2);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(homeCatListModel item) {
                FragmentCatSubNSearch fragment_search = new FragmentCatSubNSearch();
                Bundle bundle = new Bundle();
                bundle.putString("ad_country", item.getId());

                fragment_search.setArguments(bundle);
                replaceFragment(fragment_search, "FragmentCatSubNSearch");

            }
        });

    }

    void replaceFragment(Fragment someFragment, String tag) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.right_enter, R.anim.left_out, R.anim.left_enter, R.anim.right_out);
        transaction.replace(R.id.frameContainer, someFragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    private void googleAnalytics() {
        if (settingsMain.getAnalyticsShow() && !settingsMain.getAnalyticsId().equals("")) {
            AnalyticsTrackers.initialize(getActivity());
            Log.d("analyticsID", settingsMain.getAnalyticsId());
            AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP, settingsMain.getAnalyticsId());
        }

        if (settingsMain.getAnalyticsShow() && !settingsMain.getAnalyticsId().equals("") && AnalyticsTrackers.getInstance() != null)
            AnalyticsTrackers.getInstance().trackScreenView("Home");
        super.onResume();
    }

    private void adforest_AddFirebaseid(String regId) {
        if (SettingsMain.isConnectingToInternet(getActivity())) {


            JsonObject params = new JsonObject();


            params.addProperty("firebase_id", regId);
            Log.e("info send FireBase ", "Firebase reg id: " + regId);

            Call<ResponseBody> myCall = restService.postFirebaseId(params, UrlController.AddHeaders(getActivity()));
            myCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                    try {
                        if (responseObj.isSuccessful()) {
                            Log.d("info FireBase Resp", "" + responseObj.toString());

                            JSONObject response = new JSONObject(responseObj.body().string());
                            if (response.getBoolean("success")) {
                                Log.d("info Data FireBase", response.getJSONObject("data").toString());
                                settingsMain.setFireBaseId(response.getJSONObject("data").getString("firebase_reg_id"));
                                Log.d("info FireBase ID", response.getJSONObject("data").getString("firebase_reg_id"));
                                Log.d("info FireBase", "Firebase id is set with server.!");
                            }
                        }
                        SettingsMain.hideDilog();
                    } catch (JSONException e) {
                        SettingsMain.hideDilog();
                        e.printStackTrace();
                    } catch (IOException e) {
                        SettingsMain.hideDilog();
                        e.printStackTrace();
                    }
                    SettingsMain.hideDilog();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof TimeoutException) {
                        Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                        settingsMain.hideDilog();
                    }
                    if (t instanceof SocketTimeoutException || t instanceof NullPointerException) {

                        Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                        settingsMain.hideDilog();
                    }
                    if (t instanceof NullPointerException || t instanceof UnknownError || t instanceof NumberFormatException) {
                        Log.d("info FireBase ", "NullPointert Exception" + t.getLocalizedMessage());
                        settingsMain.hideDilog();
                    } else {
                        SettingsMain.hideDilog();
                        Log.d("info FireBase err", String.valueOf(t));
                        Log.d("info FireBase err", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                    }
                }
            });
        } else

        {
            Toast.makeText(getActivity(), "Internet error", Toast.LENGTH_SHORT).show();
        }
    }

    public void AdsNDAnalytics() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Log.d("AdsType", settingsMain.getAdsType());
                if (settingsMain.getInterstitalShow()) {
                    adforest_InterstitalAds();
                }
                if (settingsMain.getBannerShow() && settingsMain.isAdShowOrNot()) {
                    adforest_bannersAds();
                }

            }
        }, 1500);
    }

    public void adforest_InterstitalAds() {
        if (settingsMain.getAdsShow() && !settingsMain.getInterstitialAdsId().equals("")
                && settingsMain.getAdsDisplayTime() != 0 && settingsMain.getAdsInitialTime() != 0) {
            Admob.loadInterstitial(getActivity());

        }
    }

    public void adforest_bannersAds() {
        adsCounter = 1;
        if (settingsMain.getAdsShow() && !settingsMain.getBannerAdsId().equals("")) {
            if (settingsMain.getAdsPostion().equals("top")) {
                LinearLayout frameLayout = getActivity().findViewById(R.id.adView);
                Admob.adforest_Displaybanners(getActivity(), frameLayout);
            } else {
                LinearLayout frameLayout = getActivity().findViewById(R.id.adViewBelow);
                FrameLayout maimFrame = getActivity().findViewById(R.id.frameContainer);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams.bottomMargin = 80;
                maimFrame.setLayoutParams(layoutParams);
                Admob.adforest_Displaybanners(getActivity(), frameLayout);
            }
        }
    }

    private void adforest_setCustomPagesIcon(ArrayList<Drawable> drawables) throws JSONException {
        final JSONArray jsonArray = jsonObjectSubMenu.getJSONArray("pages");
        for (int i = 0; i < jsonArray.length(); i++) {
            menu.findItem(R.id.custom).getSubMenu().getItem(i).setIcon(drawables.get(i));

        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncImageTask extends AsyncTask<Void, Void, ArrayList<Drawable>> {
        @Override
        protected void onPostExecute(ArrayList<Drawable> drawables) {
            super.onPostExecute(drawables);
            try {
                adforest_setCustomPagesIcon(drawables);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected ArrayList<Drawable> doInBackground(Void... strings) {
            Bitmap bitmap = null;
            ArrayList<Drawable> drawables = new ArrayList<>();
            try {
                final JSONArray jsonArray = jsonObjectSubMenu.getJSONArray("pages");
                for (int i = 0; i < jsonArray.length(); i++) {
                    HttpURLConnection connection = (HttpURLConnection) new URL(jsonArray.getJSONObject(i).getString("url")).openConnection();
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(input);
                    drawables.add(new BitmapDrawable(bitmap));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NetworkOnMainThreadException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return drawables;
        }

        @Override
        protected void onPreExecute() {

        }

    }
}
