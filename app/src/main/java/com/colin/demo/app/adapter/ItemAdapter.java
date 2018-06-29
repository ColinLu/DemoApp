package com.colin.demo.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.colin.demo.app.R;
import com.colin.demo.app.base.BaseAdapter;
import com.colin.demo.app.base.BaseViewHolder;
import com.colin.demo.app.bean.ItemBean;

import java.util.List;

/**
 * 描述：
 * <p>
 * 作者：Colin
 * 时间：2018/6/21
 */
public class ItemAdapter extends BaseAdapter<ItemBean> {
    /**
     * 构造函数 子类必须实现
     *
     * @param context
     * @param itemList
     */
    public ItemAdapter(Context context, List<ItemBean> itemList) {
        super(context, itemList);
    }

    @Override
    protected int bindLayoutRes(int viewType) {
        if (viewType == VIEW_EMPTY) {
            return R.layout.layout_list_empty;
        }
        if (viewType == VIEW_FOOT) {
            return R.layout.layout_list_foot;
        }
        return R.layout.item_string;
    }

    @Override
    public int getItemCount() {
        if (isShowEmptyView()) {
            return 1;
        }
        int foot = isShowFootView() ? 1 : 0;
        return isListEmpty() ? 1 : mItemList.size() + foot;
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowEmptyView(position)) {
            return VIEW_EMPTY;
        }
        if (isShowFootView(position)) {
            return VIEW_FOOT;
        }
        return VIEW_LIST;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == VIEW_EMPTY) {
            setEmptyViewData(holder, R.mipmap.ic_launcher);
            return;
        }
        if (viewType == VIEW_FOOT) {
            setFootViewData(holder, position);
            return;
        }
        if (viewType == VIEW_LIST) {
            setListViewData(holder, position);
        }
    }

    private void setListViewData(BaseViewHolder holder, int position) {
        ItemBean itemBean = mItemList.get(position);
        holder.setText(R.id.text_item_id, String.valueOf(itemBean.id))
                .setText(R.id.text_item_content, itemBean.title)
                .setOnClickListener(R.id.layout_item_content, new AdapterClickListener(itemBean, position));
    }
}
