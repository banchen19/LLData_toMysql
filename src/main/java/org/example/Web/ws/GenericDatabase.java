package org.example.Web.ws;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericDatabase<T, ID> {
    private Map<ID, T> data;

    public GenericDatabase() {
        data = new HashMap<>();
    }

    public void save(ID id, T item) {
        data.put(id, item);
    }

    public T findById(ID id) {
        return data.get(id);
    }

    public void delete( T item) {
        ID idToRemove = null;
        for (Map.Entry<ID, T> entry : data.entrySet()) {
            if (entry.getValue().equals(item)) {
                idToRemove = entry.getKey();
                break;
            }
        }
        if (idToRemove != null) {
            data.remove(idToRemove);
        }
    }

    public List<T> getAllItems() {
        List<T> itemList = new ArrayList<>(data.values());
        return itemList;
    }

}
