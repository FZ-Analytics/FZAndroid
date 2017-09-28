package com.fz.fzapp.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.fz.fzapp.R;
import com.fz.fzapp.adapter.Database_adapter;
import com.fz.fzapp.data.User;
import com.fz.fzapp.service.AllFunction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Username extends AppCompatActivity
{
  @BindView(R.id.etUsernameLogin)
  EditText etUsernameLogin;

  static String TAG = "[NamaUser]";
  private Context context = this;
  List<EditText> lstInput = new ArrayList<>();
  List<String> lstMsg = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.username_lay);
    ButterKnife.bind(this);

    if(AllFunction.CheckPermission(Username.this, this))
    {
      File file = getApplicationContext().getDatabasePath(Database_adapter.databasename);

      if(!file.exists())
      {
        Database_adapter DataBaseTasklist = new Database_adapter(this);
        DataBaseTasklist.getReadableDatabase();

        if(!copyDatabase(this)) return;
      }
    }

    etUsernameLogin.setText(User.getInstance().getUsername());
  }

  @OnClick({R.id.rlUsername, R.id.btnUsernameLogin})
  public void onViewClicked(View view)
  {
    switch(view.getId())
    {
      case R.id.rlUsername:
      case R.id.btnUsernameLogin:
        lstInput.clear();
        lstMsg.clear();
        lstInput.add(etUsernameLogin);
        lstMsg.add(getResources().getString(R.string.msgInputUserID));

        if(AllFunction.InputCheck(lstInput, lstMsg, context))
        {
          User.getInstance().setUsername(etUsernameLogin.getText().toString().trim());
          Intent NamaUserIntent = new Intent(Username.this, Password.class);
          startActivity(NamaUserIntent);
          finish();
        }
      break;
    }
  }

  private boolean copyDatabase(Context context)
  {
    try
    {
      InputStream inputStream = context.getAssets().open(Database_adapter.databasename);
      String strFile = Database_adapter.databaselocation + Database_adapter.databasename;
      OutputStream outputStream = new FileOutputStream(strFile);
      byte[] buff = new byte[1024];
      int length = 0;

      while((length = inputStream.read(buff)) > 0)
      {
        outputStream.write(buff, 0, length);
      }

      outputStream.flush();
      outputStream.close();
      return true;
    }
    catch(Exception e)
    {
      e.printStackTrace();
      Toast.makeText(context, getResources().getString(R.string.strDatabaseFailed), Toast.LENGTH_SHORT).show();
      return false;
    }
  }
}
