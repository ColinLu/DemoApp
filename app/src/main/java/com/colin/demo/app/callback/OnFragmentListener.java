package com.colin.demo.app.callback;

import android.view.View;

/**
 * Created by Colin on 2017/8/13.
 */

public interface OnFragmentListener {
    //fragment编号；item位置，判断,回调数据
    void onFragmentClick(View view, int fragment_id, int position, Object object);
}
