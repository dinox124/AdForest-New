package com.scriptsbundle.adforest.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.scriptsbundle.adforest.R;
import com.scriptsbundle.adforest.helper.OnItemClickListener;
import com.scriptsbundle.adforest.home.helper.CustomCategoriesFilter;
import com.scriptsbundle.adforest.modelsList.homeCatListModel;
import com.scriptsbundle.adforest.utills.SettingsMain;

public class ItemHomeAllCategories extends RecyclerView.Adapter<ItemHomeAllCategories.CustomViewHolder>
        implements Filterable {

    SettingsMain settingsMain;
    public List<homeCatListModel> feedItemList;
    public ArrayList<homeCatListModel> feedItemListFiltered;
    private Context mContext;
    private OnItemClickListener oNItemClickListener;
    CustomCategoriesFilter filter;

    public ItemHomeAllCategories(Context context, ArrayList<homeCatListModel> feedItemList) {
        this.feedItemList = feedItemList;
        this.feedItemListFiltered = feedItemList;
        this.mContext = context;
        settingsMain = new SettingsMain(context);

    }

    @Override
    public ItemHomeAllCategories.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_viewall_categories, null);
        return new ItemHomeAllCategories.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemHomeAllCategories.CustomViewHolder customViewHolder, int i) {
        final homeCatListModel feedItem = feedItemList.get(i);
        customViewHolder.tv_cat_value.setText(feedItem.getTitle());

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oNItemClickListener.onItemClick(feedItem);
            }
        };

        customViewHolder.view.setOnClickListener(listener);

    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    public OnItemClickListener getOnItemClickListener() {
        return oNItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.oNItemClickListener = onItemClickListener;
    }
        public Filter getFilter() {
            if(filter==null)
            {
                filter=new CustomCategoriesFilter(feedItemListFiltered,this);
            }
            return filter;
        }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tv_cat_value;
        RelativeLayout view;

        CustomViewHolder(View view) {
            super(view);

            this.view = view.findViewById(R.id.layoutCategories);
            this.tv_cat_value = view.findViewById(R.id.tv_cat_value);
        }
    }
}
