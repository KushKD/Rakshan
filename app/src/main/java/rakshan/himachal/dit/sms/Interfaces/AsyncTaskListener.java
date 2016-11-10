package rakshan.himachal.dit.sms.Interfaces;

import rakshan.himachal.dit.sms.Enum.TaskType;

/**
 * Created by kuush on 10/29/2016.
 */

public interface AsyncTaskListener {

    public void onTaskCompleted(String result, TaskType taskType);
}