/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsought4;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author KEN20232
 */
public class JavaSynchronizationExample4 {
//thread safety of System.out.println-->https://stackoverflow.com/questions/9459657/is-multi-thread-output-from-system-out-println-interleaved

    public static void main(String... args) throws InterruptedException {
        int TASK_SIZE = 500;

        ExecutorService executorService = Executors.newFixedThreadPool(TASK_SIZE);
        Instant start = Instant.now();
        for (int i = 0; i < TASK_SIZE; i++) {
            executorService.submit(new MessagingTask("Message " + (i + 1), "Thread " + (i + 1)));
        }

        executorService.shutdown();
        executorService.awaitTermination(1l, TimeUnit.HOURS);
        System.out.println("Processed Messages:" + MessageServer.getProcessedMessagesCount());
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        System.out.println("Time taken: " + timeElapsed.toMillis() + " milliseconds");
    }
}

class MessagingTask implements Callable<Boolean> {

    private String message;
    private String threadName;

    public MessagingTask(String message, String threadName) {
        this.message = message;
        this.threadName = threadName;
    }

    @Override
    public Boolean call() throws Exception {
        MessageServer.sendMessage(message, threadName);
        return true;
    }
}

class MessageServer {

    private static int processedMessagesCount;

    public static void sendMessage(String message, String threadName) {

        try {
            //simulate a long task
            Thread.sleep((long) (Math.random() * 50));
            //increment message count
            synchronized (MessageServer.class) {
                processedMessagesCount++;
            }
            //System.out.println("Processing message ->" + message + ", from thread -->" + threadName + ", at number --->" + getProcessedMessagesCount());
        } catch (InterruptedException ex) {
            

            Logger.getLogger(MessageServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * @return the processedMessages
     */
    public static int getProcessedMessagesCount() {
        return processedMessagesCount;
    }

}
