package com.fz.fzapp.popup;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.fz.fzapp.R;
import com.fz.fzapp.service.AllFunction;
import com.fz.fzapp.utils.Preference;

/**
 * Dibuat oleh : ignat
 * Tanggal : 08-Dec-16
 * HP/WA : 0857 7070 6 777
 */
public class ReasonStatus extends Dialog
{
  @BindView(R.id.etReason)
  EditText etReason;

  private String TAG = "[Reason]";
  private Context context;

  public ReasonStatus(Context mContext)
  {
    super(mContext);
    this.context = mContext;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.reasonstatus_lay);
    ButterKnife.bind(this);
  }

  @OnClick(R.id.btnSubmitReason)
  public void onViewClicked(View view)
  {
    switch(view.getId())
    {
      case R.id.btnSubmitReason:
        if(!etReason.getText().toString().isEmpty())
          AllFunction.storeToSharedPref(context, etReason.getText().toString(), Preference.prefOtherReason);

        dismiss();
      break;
    }
  }
}

