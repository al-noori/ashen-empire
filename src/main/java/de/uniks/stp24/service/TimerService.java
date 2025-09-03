package de.uniks.stp24.service;

import de.uniks.stp24.ws.ClientEndpoint;
import de.uniks.stp24.ws.EventListener;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Timer;
import java.util.TimerTask;

@Singleton
public class TimerService {

    private Timer timer;
    @Inject
    LoginService loginService;
    @Inject
    EventListener eventListener;
    private static final long REFRESH_SESSION = 30 * 60 * 1000;
    private static final long REFRESH_WEBSOCKET = 50 * 1000;

    @Inject
    public TimerService() {
        timer = new Timer();
    }

    public void startTimer(long delay, long period, TimerTask task) {
        timer.schedule(task, delay, period);
    }

    public void stopTimer() {
        timer.purge();
        timer.cancel();
    }

    public void setupTimer() {
        stopTimer();
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Observable<?> observable = loginService.refreshToken();
                if (observable != null) {
                    observable.subscribe(worked -> System.out.println("Refreshed Session"),
                            e -> e.printStackTrace());

                }
                //clientEndpoint.sendMessage("");
            }
        };
        TimerTask websocketTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Sending empty message to websocket");
                eventListener.send("");
            }
        };
        startTimer(REFRESH_SESSION, REFRESH_SESSION, task);
        startTimer(REFRESH_WEBSOCKET, REFRESH_WEBSOCKET, websocketTask);
    }

}
