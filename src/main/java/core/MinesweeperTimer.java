package core;

import java.util.Timer;
import java.util.TimerTask;


public class MinesweeperTimer {

    private Timer timer;
    private TimerTask task;
    private int time;

    public MinesweeperTimer(Minesweeper listener){
        this.time = 0;
        this.timer = new Timer();
        this.task = new TimerTask() {

            @Override
            public void run() {
                time++;
                listener.updateTimer(time);
            }
        };

        timer.schedule(task, 1000, 1000);

    }
    
    public int getTime() {
        return time;
    }

    public void stop(){
        this.timer.cancel();
    }

}
