package ExpiringValuesMap;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @param <K> datatype of the map key
 * @param <V> datatype of the map value
 */
public class EvictionMap<K, V> {

    // delay of removing value from map in milliseconds.
    private long expirationTime;
    // Map which contains keys and values provided by users using method put.
    private ConcurrentHashMap<K, V> usersInputs;
    // Map which contains provided by user keys and timer with task of removing entry after certain time as value.
    private ConcurrentHashMap<K, Timer> timerTasks;

    /**
     *
     * @param time - delay of removing value from map in milliseconds
     */
    public EvictionMap(long time) {
        if (time >= 0) {
            this.usersInputs = new ConcurrentHashMap<>();
            this.timerTasks = new ConcurrentHashMap<>();
            this.expirationTime = time;
        } else {
            throw new IllegalArgumentException("Value could not be smaller than 0");
        }
    }

    /**
     * Save or update map with provided by user key and value.
     * @param key -provided by user key
     * @param value - provided by user value
     */
    public void put(K key, V value) {
        if (key != null & value != null) {
            if (usersInputs.size() == 0) {
                usersInputs.put(key, value);
            } else {
                Timer timerFromTimerTasks = timerTasks.getOrDefault(key, null);
                if (timerFromTimerTasks == null) {
                    usersInputs.put(key, value);
                } else {
                    timerFromTimerTasks.cancel();
                    usersInputs.put(key, value);
                }
            }
            timerInitializer(key);
        } else {
            throw new IllegalArgumentException("Key could not be null!");
        }

    }

    /**
     * Create task of removing entry from both maps after certain time.
     * @param key - provided by user key
     */
    private void timerInitializer(K key) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                usersInputs.remove(key);
                timerTasks.remove(key);
                timer.cancel();
            }
        };
        timer.schedule(task, expirationTime);
        timerTasks.put(key, timer);
    }

    /**
     * Find value associated with provided by user key from the map.
     * @param key - provided by user key to find associated with it value in map if it is present.
     * @return - value associated with the provided by user key if it is present or null if not.
     */
    public V get(K key){
        if (key != null) {
            return usersInputs.getOrDefault(key, null);
        } else {
            throw new IllegalArgumentException("Key could not be null!");
        }
    }
}
