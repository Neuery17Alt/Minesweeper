package com.example.minesweeper;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;

public class SleepClass {
    public void test (MineSweeperApplication application) {
        final Task<Void> task = new Task<Void>() {
            final int N_ITERATIONS = 1;

            /**
             * This method makes the loading progressbar with the actions
             * which are meant to be after the loading finished
             */

            @Override
            protected Void call() throws Exception {
                for (int i = 0; i < N_ITERATIONS; i++) {
                    updateProgress(i + 1, N_ITERATIONS);
                    // sleep is used to simulate doing some work which takes some time....
                    Thread.sleep(1000);
                }
                Platform.runLater(() -> {
                    application.initialize();
                });
                return null;
            }
        };

        final Thread thread = new Thread(task, "task-thread");
        thread.setDaemon(true);
        thread.start();
    }
}
