package com.colin.demo.app.activity.view.text;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.colin.demo.app.R;
import com.colin.demo.app.base.BaseActivity;
import com.colin.demo.app.bean.ItemBean;
import com.colin.demo.app.utils.StringUtil;
import com.colin.demo.app.utils.ToastUtil;

public class TextViewActivity extends BaseActivity {
    private ItemBean mItemBean;
    private TextView text_test_auto_xml;
    private TextView text_test_auto_java;
    private String autoString = "TextView->>AutoSize：Android版本26之后，TextView新增属性效果，在TextView大小固定，显示文字不断增加" +
            "，文字自动化所缩小的效果，实现适配效果，让文字显示完整";
    private char[] autoCharArray = null;

    @Override
    protected void onDestroy() {
        mItemBean = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_view);
    }

    @Override
    protected void initView() {
        text_test_auto_xml = findViewById(R.id.text_test_auto_xml);
        text_test_auto_java = findViewById(R.id.text_test_auto_java);
        text_test_auto_java.setText(autoString);
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mItemBean = bundle.getParcelable("clazz");
        }
        setTitle(null == mItemBean ? "" : mItemBean.title);

        autoCharArray = autoString.toCharArray();

        text_test_auto_xml.setText("TextView->>AutoSize：");
    }


    @Override
    protected void initListener() {
        text_test_auto_xml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addText(StringUtil.getText(v).length());
            }
        });
    }

    /**
     * 增加文字内容
     *
     * @param index
     */
    private void addText(int index) {
        if (index == 0) {
            return;
        }
        if (index >= autoCharArray.length) {
            ToastUtil.showToast("显示完了文案");
            return;
        }
        text_test_auto_xml.append(String.valueOf(autoCharArray[index]));
    }

    @Override
    protected void initAsync() {

    }
}
