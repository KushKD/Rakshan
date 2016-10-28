package rakshan.himachal.dit.permissions;

/**
 * Created by kuush on 10/28/2016.
 */

import android.app.Activity;
import android.content.Intent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;



public class RakshamPermissions {

    private WeakReference<Activity> context;
    /**
     * The Permissions.
     */
    protected ArrayList permissions;
    /**
     * The constant listener.
     */
    protected static OnRequestPermissionsBack listener;

    private RakshamPermissions(Builder builder) {
        context = builder.context;
        permissions = new ArrayList<>(Arrays.asList(builder.permissions));
        listener = builder.listener;
        Intent callingIntent = RakshamPermissionActivity.getCallingIntent(context.get(), permissions,builder.requestId);
        context.get().startActivity(callingIntent);
    }


    /**
     * The interface On request permissions back.
     */
    public interface OnRequestPermissionsBack {
        /**
         * On request back.
         *
         * @param gotaResponse the gota response
         */
        void onRequestBack(RakshamPermissionResponse gotaResponse);
    }

    /**
     * The type Builder.
     */
    public static class Builder {
        private WeakReference<Activity> context;
        private String[] permissions;
        private int requestId;
        /**
         * The Listener.
         */
        protected OnRequestPermissionsBack listener;

        /**
         * Instantiates a new Builder.
         *
         * @param activity the activity
         */
        public Builder(Activity activity) {
            this.context = new WeakReference<>(activity);
        }


        /**
         * With permissions gota . builder.
         *
         * @param permissions the permissions
         * @return the gota . builder
         */
        public RakshamPermissions.Builder withPermissions(String... permissions) {
            this.permissions = permissions;
            return this;
        }

        /**
         * Sets listener.
         *
         * @param listener the listener
         * @return the listener
         */
        public RakshamPermissions.Builder setListener(OnRequestPermissionsBack listener) {
            this.listener = listener;
            return this;
        }

        /**
         * Request id gota . builder.
         *
         * @param requestId the request id
         * @return the gota . builder
         */
        public RakshamPermissions.Builder requestId(int requestId){
            this.requestId = requestId;
            return this;
        }

        /**
         * Check gota.
         *
         * @return the gota
         */
        public RakshamPermissions check() {
            return new RakshamPermissions(this);
        }
    }
}
