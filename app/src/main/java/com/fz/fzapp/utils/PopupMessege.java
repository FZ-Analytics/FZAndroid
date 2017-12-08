package com.fz.fzapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fz.fzapp.R;
import com.fz.fzapp.common.SyncData;
import com.fz.fzapp.data.AllUploadData;
import com.fz.fzapp.pojo.UploadPojo;
import com.fz.fzapp.sending.UploadHolder;
import com.fz.fzapp.service.AllFunction;
import com.fz.fzapp.service.DataLink;
import com.fz.fzapp.service.UploadData;
import com.google.gson.Gson;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by aignatd on 6/25/16.
 */
public class PopupMessege extends Activity {
    public void ShowMessege0(final Context context, final Class<?> cls) {
        Intent Pesan0 = new Intent(context, cls);
        context.startActivity(Pesan0);
        finish();
    }

    public void ShowMessege1(final Context context, String strMsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(R.string.titleMessege)
                .setMessage(strMsg)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .setPositiveButton(R.string.strBtnOK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void ShowMessege2(final Context context, String strMsg, final Activity act, final Class<?> cls) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(R.string.titleMessege)
                .setMessage(strMsg)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .setPositiveButton(R.string.strBtnOK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, cls);
                        context.startActivity(intent);
                        act.finish();
                    }
                })
                .setNegativeButton(R.string.strBtnCancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void ShowMessege3(final Context context, String strMsg, final Activity cls) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(R.string.titleMessege)
                .setMessage(strMsg)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .setPositiveButton(R.string.strBtnOK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        cls.finish();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void ShowMessege4(final Context context, String strMsg, final Activity act, final Class<?> cls) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(R.string.titleMessege)
                .setMessage(strMsg)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .setPositiveButton(R.string.strBtnOK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, cls);
                        context.startActivity(intent);
                        act.finish();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void ShowMessege5(final Context context, String strMsg, final Class<?> cls) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(R.string.titleMessege)
                .setMessage(strMsg)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .setPositiveButton(R.string.strBtnOK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, cls);
                        context.startActivity(intent);
                        finish();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void ShowMessege6(final Context context, String strMsg, final Activity act, final Class<?> cls) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(R.string.titleMessege)
                .setMessage(strMsg)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .setPositiveButton(R.string.strBtnOK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, cls);
                        context.startActivity(intent);
                        act.finish();
                    }
                })
                .setNegativeButton(R.string.strBtnCancel2, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void ShowMessege7(final Context context, String strMsg, final Activity act, final Class<?> cls, final MediaPlayer player) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(R.string.titleMessege)
                .setMessage(strMsg)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .setPositiveButton(R.string.strBtnOK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        player.stop();
                        Intent intent = new Intent(context, cls);
                        context.startActivity(intent);
                        act.finish();
                    }
                })
                .setNegativeButton(R.string.strBtnCancel2, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void ShowMessegeUpload(final Context context, final Activity act) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.tonase_popup, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                final Gson gson = new Gson();
                final String userInputKg = userInput.getText().toString().trim();

                if (userInputKg.trim().isEmpty()) {
                    Toast.makeText(context, "Isi data KG terlebih dahulu", Toast.LENGTH_LONG).show();
                } else {
                    final int userInputInt = Integer.parseInt(userInputKg);
                    UploadHolder uploadHolder = new UploadHolder(AllUploadData.getInstance().getUploadData(), userInputInt);
                    Log.d("Test", "getUploadHolder: " + gson.toJson(uploadHolder));
                    if (CheckConnection(context) == -1) return;
                    DataLink dataLink = AllFunction.BindingData();
                    final Call<UploadPojo> ReceiveUpload = dataLink.uploadService(uploadHolder);
                    ReceiveUpload.enqueue(new Callback<UploadPojo>() {
                        @Override
                        public void onResponse(Call<UploadPojo> call, Response<UploadPojo> response) {
                            if (response.body().getCoreResponse().getCode() == FixValue.intSuccess) {
                                Log.d("UploadTask", String.valueOf(response));
                                onProcessSyncData(context, act);
                            } else {
                                Toast.makeText(context, response.body().getCoreResponse().getMsg(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UploadPojo> call, Throwable t) {
                            Toast.makeText(context, R.string.msgServerFailure, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void onProcessSyncData(Context context, Activity activity) {
        HashMap<String, String> listSyncTable = new HashMap<>();
        listSyncTable.clear();
        listSyncTable.put("tracking", "tracking");
        new UploadData(activity, context, listSyncTable).execute();
        Intent SyncIntent = new Intent(context, SyncData.class);
        context.startActivity(SyncIntent);
        activity.finish();
    }

    private Integer CheckConnection(Context context) {
        if (AllFunction.isNetworkAvailable(context) == FixValue.TYPE_NONE) {
            Toast.makeText(context, R.string.msgCheckConnection, Toast.LENGTH_LONG).show();
            return -1;
        }
        return 0;
    }
}
