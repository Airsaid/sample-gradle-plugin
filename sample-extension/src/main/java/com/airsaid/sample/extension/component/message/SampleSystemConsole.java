package com.airsaid.sample.extension.component.message;

import android.util.Log;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SampleSystemConsole {
  private static final String TAG = "SampleSystemConsole";
  private static final int MAX_RETRY_COUNT = Integer.MAX_VALUE;
  private final ExecutorService executorService = Executors.newSingleThreadExecutor(r -> new Thread(r, "system-console-thread"));
  private ReadStreamRunnable readStreamRunnable = null;

  public void setup(final WorkThread<String> workThread) {
    setupInternal(workThread, 0, MAX_RETRY_COUNT);
  }

  private void setupInternal(final WorkThread<String> workThread, final int retryCount, final int maxRetryCount) {
    PipedOutputStream pout = new PipedOutputStream();
    // set up System.out
    System.setOut(new PrintStream(pout, true));
    readStreamRunnable = new ReadStreamRunnable(workThread, pout);
    executorService.execute(new Runnable() {
      @Override
      public void run() {
        readStreamRunnable.run();
        if (retryCount < maxRetryCount) {
          // Here the thread was stopped. We try to restart the thread.
          Log.i(TAG, "Try to re-start the thread:" + (retryCount + 1));
          setupInternal(workThread, retryCount + 1, maxRetryCount);
        }
      }
    });
  }

  public void stop() {
    if (null != readStreamRunnable) {
      readStreamRunnable.stop();
    }
  }

  private static class ReadStreamRunnable implements Runnable {
    private final PipedOutputStream outputStream;
    private final WorkThread<String> workThread;
    private volatile boolean isRunning = true;

    public ReadStreamRunnable(WorkThread<String> workThread, PipedOutputStream outputStream) {
      this.workThread = workThread;
      this.outputStream = outputStream;
    }

    public void stop() {
      isRunning = false;
      System.setOut(null);
      try {
        outputStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Override
    public void run() {
      final byte[] buf = new byte[1024];
      try (PipedInputStream is = new PipedInputStream(outputStream)) {
        Log.i(TAG, "Connected to the pip.");
        while (isRunning) {
          int len = is.read(buf);
          if (len == -1) {
            continue;
          }
          String text = new String(buf, 0, len);
          workThread.post(text);
        }
      } catch (IOException e) {
        isRunning = false;
      }
    }
  }

}
