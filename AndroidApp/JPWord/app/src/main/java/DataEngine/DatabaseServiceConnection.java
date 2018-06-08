package DataEngine;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.jpword.ma.jpword.DatabaseService;

/**
 * Created by u0151316 on 6/8/2018.
 */

public abstract class DatabaseServiceConnection implements ServiceConnection {

    private IDatabaseOperator databaseOperator_ = null;
    private Context context_ = null;

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        DatabaseService.BinderForDatabaseService binder = (DatabaseService.BinderForDatabaseService) service;
        databaseOperator_ = binder.getDatabaseOperator();
        context_ = binder.getContext();
        onServiceConnected();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        onServiceDisconnected();
    }

    public IDatabaseOperator getDatabaseOperator() {
        return databaseOperator_;
    }

    public Context getContext() {
        return context_;
    }

    public abstract void onServiceConnected();

    public abstract void onServiceDisconnected();
}
