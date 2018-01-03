package com.fz.fzapp.Smart_Fit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.fz.fzapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Heru Permana on 12/14/2017.
 */

public class Estimation_Entry extends AppCompatActivity {
    @BindView(R.id.etDateHarvest)
    EditText etDateHarvest;
    @BindView(R.id.etDivision)
    EditText etDivision;
    @BindView(R.id.etRemark)
    EditText etRemark;
    @BindView(R.id.btnAdd)
    RelativeLayout btnAdd;
    @BindView(R.id.rvAddEstimationEntry)
    RecyclerView rvAddEstimationEntry;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.estimation_entry_lay);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btnAdd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnAdd:
                AddEstimationEntry();
                break;
        }
    }

    private void AddEstimationEntry() {

    }
}
