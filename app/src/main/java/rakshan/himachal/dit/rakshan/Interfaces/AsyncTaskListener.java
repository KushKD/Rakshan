package rakshan.himachal.dit.rakshan.Interfaces;

import rakshan.himachal.dit.rakshan.Enum.TaskType;

/**
 * Created by kuush on 10/29/2016.
 */

public interface AsyncTaskListener {

    public void onTaskCompleted(String result, TaskType taskType);
}