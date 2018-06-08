package DataEngine;

import java.util.List;

/**
 * Created by u0151316 on 6/7/2018.
 */

public interface IDatabaseOperator {
    void loadDatabase(String dictname, boolean createdIfNotExist);

    DBEntity getDBEntity();

    List<String> getDatabaseList();
}
