package com.airsaid.sample.extension.component.message;

import androidx.annotation.AnyThread;
import androidx.annotation.NonNull;

import java.util.ArrayDeque;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;

/**
 * @author JackChen
 */
public class WorkThread<E> extends Thread {
  /**
   * max size in our queue. It's a limitation like a buffer so we don't have to wait too much time or store too many data
   */
  private final static int MAX_CAPACITY = 1;
  /**
   * max wait time, if we don't have enough data we just wait a few second and store all the data
   */
  private final static long MAX_WAIT_TIME = 2 * 60 * 1000L;
  /**
   * the mutex resource object
   */
  private final static Object LOCK = new Object();
  /**
   * the data queue
   */
  private final Queue<E> messageQueue = new ArrayDeque<>();
  /**
   * active time
   */
  private long activeTimeMillis = 0L;
  /**
   * observable will inform other class this data queue is changed
   */
  private final WorkObservable observable = new WorkObservable();
  /**
   * if this thread is running
   */
  private volatile boolean isRunning = false;

  public WorkThread() {
  }

  public WorkThread(@NonNull String name) {
    super(name);
  }


  @Override
  public void run() {
    super.run();
    try {
      while (!interrupted()) {
        //process message and wait
        flushDataAndWait();
        //If outside was stop. we stop
        if (!isRunning) {
          break;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      //flush all the left messages
      flushData();
    }
  }

  /**
   * Check and flush all the data user put in the queue
   */
  private void flushDataAndWait() {
    if (!messageQueue.isEmpty()) {
      synchronized (LOCK) {
        while (!messageQueue.isEmpty()) {
          E data = messageQueue.poll();
          observable.setChanged();
          observable.notifyObservers(data);
        }
        //refresh the active time
        activeTimeMillis = System.currentTimeMillis();
        //wait unit next time
        try {
          LOCK.wait(MAX_WAIT_TIME);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * flush all the data
   */
  private void flushData() {
    if (!messageQueue.isEmpty()) {
      while (!messageQueue.isEmpty()) {
        E data = messageQueue.poll();
        observable.setChanged();
        observable.notifyObservers(data);
      }
    }
  }


  /**
   * start this thread
   */
  @AnyThread
  public void startService() {
    if (!isAlive()) {
      activeTimeMillis = System.currentTimeMillis();
      isRunning = true;
      this.start();
    }
  }

  /**
   * stop this thread
   */
  @AnyThread
  public void stopService() {
    synchronized (LOCK) {
      isRunning = false;
      LOCK.notify();
    }
  }

  /**
   * put data to the queue
   */
  @AnyThread
  public void post(E data) {
    synchronized (LOCK) {
      messageQueue.offer(data);
      if (System.currentTimeMillis() - activeTimeMillis > MAX_WAIT_TIME) {
        LOCK.notify();
      } else if (messageQueue.size() >= MAX_CAPACITY) {
        LOCK.notify();
      }
    }
  }

  /**
   * if you want to notify the thread somehow, you could call this function
   */
  @AnyThread
  public void notifyService() {
    synchronized (LOCK) {
      LOCK.notify();
    }
  }

  @AnyThread
  public void addObserver(Observer observer) {
    synchronized (LOCK) {
      this.observable.addObserver(observer);
    }
  }

  @AnyThread
  public void removeObserver(Observer observer) {
    synchronized (LOCK) {
      this.observable.deleteObserver(observer);
    }
  }

  public void clearObserver() {
    synchronized (LOCK) {
      this.observable.deleteObservers();
    }
  }

  public static class WorkObservable extends Observable {
    @Override
    public synchronized void setChanged() {
      super.setChanged();
    }
  }

}
