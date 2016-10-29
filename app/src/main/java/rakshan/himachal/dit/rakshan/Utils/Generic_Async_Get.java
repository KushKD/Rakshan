package rakshan.himachal.dit.rakshan.Utils;

/**
 * Created by kuush on 10/29/2016.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import rakshan.himachal.dit.rakshan.Enum.TaskType;
import rakshan.himachal.dit.rakshan.HTTP.HttpManager;
import rakshan.himachal.dit.rakshan.Interfaces.AsyncTaskListener;


/**
 * Created by kuush on 6/17/2016.
 */
public class Generic_Async_Get extends AsyncTask<String,Void ,String> {


    String outputStr;
    ProgressDialog dialog;
    Context context;
    AsyncTaskListener taskListener;
    TaskType taskType;

    public Generic_Async_Get(Context context, AsyncTaskListener taskListener, TaskType taskType){
        this.context = context;
        this.taskListener = taskListener;
        this.taskType = taskType;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = ProgressDialog.show(context, "Loading", "Connecting to Server .. Please Wait", true);
        dialog.setCancelable(false);
    }

    @Override
    protected String doInBackground(String... params) {
        String Data_From_Server = null;
        HttpManager http_manager = null;
        try{
            http_manager = new HttpManager();
            Data_From_Server = http_manager.GetData(params[0]);

            return Data_From_Server;
        }catch(Exception e){
            return e.getLocalizedMessage().toString().trim();
        }

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        taskListener.onTaskCompleted(result, taskType);
        dialog.dismiss();
    }



}