package com.fz.fzapp.Krani;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.fz.fzapp.R;
import com.fz.fzapp.fragment.TimePickerFragment;
import android.widget.AdapterView.OnItemSelectedListener;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Heru Permana on 11/28/2017.
 */

public class Main_Krani  extends Activity implements OnItemSelectedListener{
    @BindView(R.id.etEstate)
    EditText etEstate;
    @BindView(R.id.rlSetTime)
    RelativeLayout rlSetTime;
    @BindView(R.id.spDirectionLocation)
    Spinner spDirectionLocation;
    @BindView(R.id.spLastOrder)
    Spinner spLastOrder;
    List<String> listDirectionLocation = new ArrayList<String>();
    List<String> listOrder = new ArrayList<String>();
    Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_lay);
        DataSpinner();
        ButterKnife.bind(this);
    }

    private void DataSpinner() {
//        spDirectionLocation.setOnItemSelectedListener(context);
//        spLastOrder.setOnItemSelectedListener(context);

        listDirectionLocation.add("Utara(North)");
        listDirectionLocation.add("Tengah(Center)");
        listDirectionLocation.add("Selatan(South)");

        listOrder.add("No");
        listOrder.add("Yes");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listDirectionLocation);
        ArrayAdapter<String> dataAdapterOrder = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listOrder);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterOrder.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spDirectionLocation.setAdapter(dataAdapter);
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
    @OnClick({R.id.rlSetTime})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlSetTime:
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "TimePicker");
                break;
        }
    }
}
