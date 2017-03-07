package db;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by su on 2016/10/1.
 * database service interface
 * params: parameters of DML
 */

public interface DatabaseService {

    public boolean add(Object[] params);

    public boolean delete(Object[] params);

    public boolean update(Object[] parames);
    // select one
    public Map<String, String> viewOne(String[] selectArgs);
    // select many
    public List<Map<String, String>> listOnes(String[] selectArgs);

    public boolean alter(Object[] params);

    public boolean emptyTable();

    public boolean addColumn(Objects[] parames);

    public boolean deleteColumn(Objects[] parames);
}
