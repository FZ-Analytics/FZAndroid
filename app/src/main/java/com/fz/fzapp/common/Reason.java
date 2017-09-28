package com.fz.fzapp.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.fz.fzapp.R;
import com.fz.fzapp.adapter.AllTaskList_adapter;
import com.fz.fzapp.adapter.ReasonList_adapter;
import com.fz.fzapp.utils.PopupMessege;

public class Reason extends AppCompatActivity {
    @BindView(R.id.lvReasonList)
    ListView lvReasonList;
    @BindView(R.id.tvReason)
    TextView tvReason;

    private PopupMessege popupMessege = new PopupMessege();
    static ProgressDialog progressDialog;

    private Activity activity = this;
    static String TAG = "[DutyData]";
    private Context context = this;
    private Integer onDuty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reason_lay);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        onDuty = bundle.getInt("onDuty");

        if ((onDuty == 1) || (onDuty == 2)) {
            if (onDuty == 1)
                tvReason.setText(context.getResources().getString(R.string.strReason1));
            else if (onDuty == 2)
                tvReason.setText(context.getResources().getString(R.string.strReason2));

            onProcessDuty();
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void onProcessDuty() {
        lvReasonList.invalidateViews();
        ReasonList_adapter taskList_adapter = null;

        if (onDuty == 1)
            taskList_adapter = new ReasonList_adapter(activity, context, AllTaskList_adapter.getInstance().getAllresponsefail(), onDuty);
        else if (onDuty == 2)
            taskList_adapter = new ReasonList_adapter(activity, context, AllTaskList_adapter.getInstance().getAllresponselate(), onDuty);
        lvReasonList.setAdapter(taskList_adapter);
    }

    @OnClick(R.id.ivRefreshReason)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivRefreshReason:
                onProcessDuty();
                break;
        }
    }
}
