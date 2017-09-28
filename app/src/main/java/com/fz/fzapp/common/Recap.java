package com.fz.fzapp.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.fz.fzapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Recap extends AppCompatActivity
{
	@BindView(R.id.btnRecapDone)
	Button btnRecapDone;

	static String TAG = "[Recap]";
	private Context context = this;

	private SimpleDateFormat tf;
	private Calendar calendar;
	private Integer onDuty = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recap_lay);
		ButterKnife.bind(this);

		tf = new SimpleDateFormat("HH:mm:ss", Locale.US);
	}

	@Override
	public void onBackPressed()
	{
		moveTaskToBack(true);
	}

	@OnClick(R.id.btnRecapDone)
	public void onViewClicked(View view)
	{
		switch(view.getId())
		{
			case R.id.btnRecapDone:
				Intent DutyIntent = new Intent(Recap.this, Planning.class);
				startActivity(DutyIntent);
				finish();
			break;
		}
	}
}
