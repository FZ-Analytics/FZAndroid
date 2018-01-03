package com.fz.fzapp.Smart_Fit;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.fz.fzapp.R;
import com.fz.fzapp.adapter.adapter_main_smart_fit;
import com.fz.fzapp.data.JobEntryData;
import com.fz.fzapp.data.LogoutData;
import com.fz.fzapp.fragment.TimePickerFragment;
import com.fz.fzapp.model.EntryResponse;
import com.fz.fzapp.model.MobileMenuRsp;
import com.fz.fzapp.pojo.LoginPojo;
import com.fz.fzapp.pojo.OrderPojo;
import com.fz.fzapp.pojo.mobileMenuPojo;
import com.fz.fzapp.sending.JobEntryHolder;
import com.fz.fzapp.service.AllFunction;
import com.fz.fzapp.service.DataLink;
import com.fz.fzapp.utils.FixValue;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Heru Permana on 12/11/2017.
 */

public class Job_Entry extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    @BindView(R.id.etEstate)
    EditText etEstate;
    @BindView(R.id.rlSetTime)
    RelativeLayout rlSetTime;
    @BindView(R.id.spDirectionLocation)
    Spinner spDirectionLocation;
    @BindView(R.id.spLastOrder)
    Spinner spLastOrder;
    @BindView(R.id.etBeteween)
    EditText etBeteween;
    @BindView(R.id.etAndBlock)
    EditText etAndBlock;
    @BindView(R.id.etBinReady)
    EditText etBinReady;
    @BindView(R.id.etEstimateTon)
    EditText etEstimateTon;
    @BindView(R.id.etRemarks)
    EditText etRemarks;

    List<EntryResponse> listDirectionLocation;
    List<String> listOrder = new ArrayList<String>();
    Context context = this;
    Activity activity = this;
    Toolbar toolbarFooter = null;
    String gsonToString;
    mobileMenuPojo mobileMenuPojo = new mobileMenuPojo();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_lay);
        ButterKnife.bind(this);
        toolbarFooter = (Toolbar) findViewById(R.id.toolbarJobEntry);
        ToolbarHeader(toolbarFooter);
        Bundle extras = getIntent().getExtras();

        gsonToString = (String) extras.getString("gsonToString");

        Gson g = new Gson();
        mobileMenuPojo = g.fromJson(gsonToString, mobileMenuPojo.class);
        DataSpinner();

    }

    private void ToolbarHeader(Toolbar toolbarFooter) {
        toolbarFooter.setTitle("Job Entry");
        toolbarFooter.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbarFooter);
        toolbarFooter.setNavigationIcon(R.drawable.left_arrow);
        toolbarFooter.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void DataSpinner() {
//        spDirectionLocation.setOnItemSelectedListener(context);
//        spLastOrder.setOnItemSelectedListener(context);
        DataLink dataLink = AllFunction.BindingData();
        final Call<OrderPojo> listSpinner = dataLink.listSpinnerJobOrder();
        listSpinner.enqueue(new Callback<OrderPojo>() {

            @Override
            public void onResponse(Call<OrderPojo> call, Response<OrderPojo> response) {
                String[] items = new String[response.body().getEntryResponse().size()];
                listDirectionLocation = response.body().getEntryResponse();
                for (int i = 0; i < listDirectionLocation.size(); i++) {
                    items[i] = listDirectionLocation.get(i).getDirection();
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, items);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spDirectionLocation.setAdapter(dataAdapter);
            }

            @Override
            public void onFailure(Call<OrderPojo> call, Throwable t) {
                String d = t.toString();

            }
        });
        listOrder.add("No");
        listOrder.add("Yes");

        // Creating adapter for spinner

//        ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listDirectionLocation);
        ArrayAdapter<String> dataAdapterOrder = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listOrder);

        // Drop down layout style - list view with radio button
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterOrder.setDropDownViewResource(R.layout.spinner_item);

        // attaching data adapter to spinner
//        spDirectionLocation.setAdapter(dataAdapter);
        spLastOrder.setAdapter(dataAdapterOrder);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @OnClick({R.id.rlSetTime, R.id.btnAdd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlSetTime:
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "TimePicker");
                break;
            case R.id.btnAdd:
                UploadJobEntry();
                break;
        }
    }

    private void UploadJobEntry() {
        if (etEstate.getText().toString().equals("") &&
                etBeteween.getText().toString().equals("") &&
                etBinReady.getText().toString().equals("") &&
                etEstimateTon.getText().toString().equals("") &&
                etRemarks.getText().toString().equals("") ||
                etAndBlock.getText().toString().equals("")) {
            Toast.makeText(context, getResources().getString(R.string.cek_inputan), Toast.LENGTH_LONG).show();
        } else {
            String Estm = etEstimateTon.getText().toString();
            if (CheckConnection() == -1) return;
            JobEntryData jobEntryDatas = new JobEntryData();
            jobEntryDatas.setDivID(etEstate.getText().toString());
            jobEntryDatas.setBlock1(etBeteween.getText().toString());
            jobEntryDatas.setBlock2(etAndBlock.getText().toString());
            jobEntryDatas.setDirLoc(spDirectionLocation.getSelectedItem().toString());
            jobEntryDatas.setReadyTime(etBinReady.getText().toString());
            jobEntryDatas.setEstmKg(Integer.valueOf(Estm));
            jobEntryDatas.setRemark(etRemarks.getText().toString());
            jobEntryDatas.setIsLastOrder(spLastOrder.getSelectedItem().toString());

            JobEntryHolder jobEntryHolder = new JobEntryHolder(jobEntryDatas);
            DataLink dataLink = AllFunction.BindingData();
            final Call<LoginPojo> ReceiveLogout = dataLink.uploadJobEntry(jobEntryHolder);

            ReceiveLogout.enqueue(new Callback<LoginPojo>() {

                @Override
                public void onResponse(Call<LoginPojo> call, Response<LoginPojo> response) {
                    if (response.body().getCoreResponse().getCode() != FixValue.intSuccess)
                        Toast.makeText(context, response.body().getCoreResponse().getMsg(), Toast.LENGTH_LONG).show();
                    else if (response.body().getCoreResponse().getCode() == FixValue.intSuccess) {
                        activity.finish();
                    }
                }

                @Override
                public void onFailure(Call<LoginPojo> call, Throwable t) {

                }
            });
        }
    }

    private Integer CheckConnection() {
        if (AllFunction.isNetworkAvailable(context) == FixValue.TYPE_NONE) {
            Toast.makeText(context, R.string.msgCheckConnection, Toast.LENGTH_LONG).show();
            return -1;
        }

        return 0;
    }
}
