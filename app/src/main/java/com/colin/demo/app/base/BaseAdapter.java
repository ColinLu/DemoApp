package com.colin.demo.app.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.colin.demo.app.R;
import com.colin.demo.app.callback.OnRecyclerItemClickListener;

import java.util.List;

/**
 * 描述：
 * <p>
 * 作者：Colin
 * 时间：2017/8/23
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    public final static int VIEW_HAED = 0XCCCCCCCC;
    public final static int VIEW_EMPTY = 0XDDDDDDDD;
    public final static int VIEW_LIST = 0XEEEEEEEE;
    public final static int VIEW_FOOT = 0XFFFFFFFF;

    public final static int EMPTY_STATE_LOADING = 0;
    public final static int EMPTY_STATE_NO = 1;
    public final static int EMPTY_STATE_ERROR = 2;

    public final static int FOOT_STATE_LOADING = 0;
    public final static int FOOT_STATE_FINISH = 1;
    public final static int FOOT_STATE_WAIT = 2;

    protected Context mContext;
    protected List<T> mItemList;
    protected boolean showEmpty = true;            //默认显示空布局
    protected boolean showFoot = true;             //默认显示加载更多布局    当然也要    与footState 一起使用
    protected int footState = FOOT_STATE_FINISH;   //默认 加载更多布局      状态加载完成 loadMore 一起使用
    protected int emptyState = EMPTY_STATE_LOADING;//默认 加载中
    protected OnRecyclerItemClickListener mOnRecyclerItemClickListener;
    protected LayoutInflater mLayoutInflater;


    /**
     * 构造函数 子类必须实现
     *
     * @param context
     * @param itemList
     */
    public BaseAdapter(Context context, List<T> itemList) {
        this.mContext = context;
        this.mItemList = itemList;
        this.mLayoutInflater = LayoutInflater.from(context);

    }

    public BaseAdapter setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener) {
        mOnRecyclerItemClickListener = onRecyclerItemClickListener;
        return this;
    }


    public void setFootState(int footState) {
        this.footState = footState;
    }

    public void setEmptyState(int emptyState) {
        this.emptyState = emptyState;
    }

    /**
     * Item个数 空处理 尾部处理
     *
     * @return
     */
    @Override
    public int getItemCount() {
        if (isShowEmptyView()) {
            return 1;
        }
        int addFoot = isShowFootView() ? 1 : 0;
        return isListEmpty() ? 0 : mItemList.size() + addFoot;
    }


    /**
     * 判断数据源是否为空
     *
     * @return
     */
    public boolean isListEmpty() {
        return null == mItemList || mItemList.size() == 0;
    }

    public boolean isShowEmptyView() {
        return showEmpty && isListEmpty() && (emptyState == EMPTY_STATE_ERROR || emptyState == EMPTY_STATE_LOADING || emptyState == EMPTY_STATE_NO);
    }

    /**
     * 是否应该显示 更多布局
     *
     * @return
     */
    public boolean isShowFootView() {
        return showFoot && !isListEmpty() && (footState == FOOT_STATE_LOADING || footState == FOOT_STATE_WAIT);
    }


    /**
     * 当前Item是否应该显示 更多布局
     *
     * @return
     */
    public boolean isShowFootView(int position) {
        return isShowFootView() && position + 1 == getItemCount();
    }

    /**
     * 是否应该显示 空布局
     *
     * @return
     */
    public boolean isShowEmptyView(int position) {
        return isShowEmptyView() && isListEmpty() && position == 0;
    }

    /**
     * 父类处理 默认布局
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BaseViewHolder(mLayoutInflater.inflate(bindLayoutRes(viewType), parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_LIST;
    }

    protected abstract int bindLayoutRes(int viewType);

    /**
     * 默认 加载更多 显示效果
     *
     * @param holder
     * @param position
     */
    public void setFootViewData(BaseViewHolder holder, int position) {
        switch (footState) {
            case FOOT_STATE_LOADING:
                holder.setText(R.id.text_item_foot, R.string.http_load_foot_londing)
                        .setVisible(R.id.progress_item_foot, true);
                break;
            case FOOT_STATE_FINISH:
                holder.setText(R.id.text_item_foot, R.string.http_load_foot_finish)
                        .setVisible(R.id.progress_item_foot, false);
                break;
            case FOOT_STATE_WAIT:
                holder.setText(R.id.text_item_foot, R.string.http_load_foot_wait)
                        .setVisible(R.id.progress_item_foot, false);
                break;
            default:
                break;
        }
    }

    /**
     * 显示 空布局
     *
     * @param holder
     * @param position 不显示
     */
    public void setEmptyViewData(BaseViewHolder holder, int position) {
        switch (emptyState) {
            case EMPTY_STATE_LOADING:
                holder.setImageResource(R.id.image_list_empty, R.mipmap.ic_launcher_round)
                        .setText(R.id.text_list_empty, R.string.list_empty_loading);
                break;
            case EMPTY_STATE_NO:
                holder.setImageResource(R.id.image_list_empty, R.mipmap.ic_launcher_round)
                        .setText(R.id.text_list_empty, R.string.list_empty_tips);
                break;
            case EMPTY_STATE_ERROR:
                holder.setImageResource(R.id.image_list_empty, R.mipmap.ic_launcher_round)
                        .setText(R.id.text_list_empty, R.string.list_empty_error);
                break;
            default:
                break;
        }
        holder.setOnClickListener(R.id.layout_list_empty, new AdapterClickListener(null, emptyState));
    }




    /**
     * 处理瀑布流 添加空布局 和 更多
     *
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int type = holder.getItemViewType();
        if (type == VIEW_HAED || type == VIEW_EMPTY || type == VIEW_FOOT) {
            setFullSpan(holder);
        }
    }

    /**
     * 处理网格布局 添加空布局 和 更多
     *
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    return (type == VIEW_HAED || type == VIEW_EMPTY || type == VIEW_FOOT) ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    /**
     * @param holder
     */
    protected void setFullSpan(RecyclerView.ViewHolder holder) {
        if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            params.setFullSpan(true);
        }
    }


    /**
     * 设置监听 判断接口回调
     */
    public class AdapterClickListener implements View.OnClickListener {
        private Object t;
        private int position;

        public AdapterClickListener(Object t, int position) {
            this.t = t;
            this.position = position;
        }


        @Override
        public void onClick(View v) {
            if (null != mOnRecyclerItemClickListener) {
                mOnRecyclerItemClickListener.itemClick(v, t, position);
            }
        }
    }
}