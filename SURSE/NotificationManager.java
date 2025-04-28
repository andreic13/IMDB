import java.util.ArrayList;
import java.util.List;

public class NotificationManager implements Subject {
    private static NotificationManager instance = null;
    private NotificationManager() {
        observers = new ArrayList<>();
    }
    public static NotificationManager getInstance() {
        if(instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    public List<Observer> observers;

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String notification) {
        for (Observer observer : observers) {
            observer.update(notification);
        }
    }
}
