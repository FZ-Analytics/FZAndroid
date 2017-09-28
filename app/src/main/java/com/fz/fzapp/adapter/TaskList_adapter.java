package com.fz.fzapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.fz.fzapp.R;
import com.fz.fzapp.data.TaskListData;
import com.fz.fzapp.model.TaskListResponse;

import java.util.List;

/**
 * Dibuat oleh : ignat
 * Tanggal : 20-Jul-17
 * HP/WA : 0857 7070 6 777
 */
public class TaskList_adapter extends BaseAdapter
{
  private static String TAG = "[adapter_TaskList]";
  private static Context mContext;
  static private List<TaskListResponse> listDataTask;
  private static Activity activity;

  public TaskList_adapter(Activity mactivity, Context mContext, List<TaskListResponse> listDataTask)
  {
    this.mContext = mContext;
    this.listDataTask = listDataTask;
    this.activity = mactivity;
  }

  @Override
  public int getCount()
  {
    return listDataTask.size();
  }

  @Override
  public Object getItem(int i)
  {
    TaskListData taskListData = new TaskListData();
//    taskListData.setDisplayName(listDataTask.get(i).getDisplayName());
//    taskListData.setSyncDate(listDataTask.get(i).getEstimateDate());
//    taskListData.setSyncTime(listDataTask.get(i).getEstimateTime());

    return taskListData;
  }

  @Override
  public long getItemId(int i)
  {
    return listDataTask.get(i).getTaskID();
  }

  @Override
  public View getView(int i, View view, ViewGroup viewGroup)
  {
    if(view == null)
    {
      LayoutInflater lInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
      view = lInflater.inflate(R.layout.tasklistdetail_lay, null);
    }

    ViewHolder vhView = new ViewHolder(view);
//    String strTaskList = listDataTask.get(i).getDisplayName();
//
//    if(listDataTask.get(i).getStatustask() != null)
//    {
//      if(listDataTask.get(i).getStatustask() == 1)
//        strTaskList += " " + mContext.getResources().getString(R.string.strTaskDone);
//    }
//    vhView.tvTasklist.setText(strTaskList);

    return view;
  }

  static class ViewHolder
  {
    @BindView(R.id.tvTasklist)
    TextView tvTasklist;

    ViewHolder(View view)
    {
      ButterKnife.bind(this, view);
    }

    @OnClick(R.id.ivDetailDuty)
    public void onViewClicked(View view)
    {
      switch(view.getId())
      {
        case R.id.ivDetailDuty:
        break;
      }
    }
  }
}
