package ProjectMovieCollection.utils.events;

import java.util.ArrayList;
import java.util.List;

public class EventHandler<T> {

    private List<T> listeners = new ArrayList<>();

    public void addListener(T t) {
        listeners.add(t);
    }

    public void removeListener(T t) {
        listeners.remove(t);
    }

    public List<T> getListeners() {
        return listeners;
    }
}
