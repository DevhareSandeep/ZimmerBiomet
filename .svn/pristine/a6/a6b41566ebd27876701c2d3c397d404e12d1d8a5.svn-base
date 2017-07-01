package sg.totalebizsolutions.foundation.core;

import android.os.Handler;
import android.support.annotation.CallSuper;
import android.support.annotation.MainThread;

import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Executor
{
  /* Properties */

  private Handler m_handler;
  private ExecutorService m_internalQueue;

  private volatile boolean m_isManagerActive = false;

  private InternalThreadFactory m_internalThreadFactory;

  /* Initialization */

  /**
   * Generic constructor.
   */
  @MainThread
  public Executor ()
  {
    /* Do nothing. */
  }

  /* Manager life-cycle methods */

  /**
   * Called when the application is starting.
   */
  @CallSuper
  @MainThread
  public void onCreate ()
  {
    m_isManagerActive = true;
    m_handler = new Handler();
  }

  /**
   * This is called when the overall system is running low on memory.
   */
  @MainThread
  public void onLowMemory ()
  {
    /* Do nothing */
  }

  /**
   * Remove all pending scheduled messages queues.
   */
  @CallSuper
  @MainThread
  public void onDestroy ()
  {
    m_isManagerActive = false;

    synchronized (this)
    {
      if (m_handler != null)
      {
        m_handler.removeCallbacksAndMessages(null);
        m_handler = null;
      }

      if (m_internalQueue != null)
      {
        m_internalQueue.shutdown();
        m_internalQueue = null;
      }

      if (m_internalThreadFactory != null)
      {
        m_internalThreadFactory = null;
      }
    }
  }

  /* Property methods */

  /**
   * Internal and lazy-loading property method of a queued ExecutorService
   * instance.
   */
  private ExecutorService getInternalQueue ()
  {
    if (m_internalQueue == null)
    {
      synchronized (this)
      {
        m_internalQueue =
          Executors.newSingleThreadExecutor(getInternalThreadFactory());
      }
    }
    return m_internalQueue;
  }

  /**
   * Internal and lazy-loading property method of a InternalThreadFactory
   * instance.
   */
  private InternalThreadFactory getInternalThreadFactory ()
  {
    if (m_internalThreadFactory == null)
    {
      synchronized (this)
      {
        m_internalThreadFactory = new InternalThreadFactory();
      }
    }
    return m_internalThreadFactory;
  }

  /* Manager thread confinement methods */

  /**
   * Submits a Runnable that will be executed on the main thread.
   */
  public void runOnUIThread (Runnable r)
  {
    if (m_handler != null)
    {
      m_handler.post(r);
    }
  }

  /**
   * Submits a Runnable for internal execution.
   *
   * @param r the runnable that will be executed.
   */
  public void execute (final Runnable r)
  {
    if (!m_isManagerActive)
    {
      return;
    }

    try
    {
      if (getInternalThreadFactory().isCurrentThreadFromThisFactory())
      {
        r.run();
      }
      else
      {
        Runnable wrapperRunnable = new Runnable()
        {
          @Override
          public void run ()
          {
            r.run();
          }
        };
        getInternalQueue().submit(wrapperRunnable);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Submits a callback for internal execution.
   *
   * @param c the callback that will be executed.
   * @return the return value of the Callable.call() method
   */
  public <T> T execute (Callable<T> c)
  {
    if (!m_isManagerActive)
    {
      return null;
    }

    T value = null;
    if (getInternalThreadFactory().isCurrentThreadFromThisFactory())
    {
      try
      {
        value = c.call();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      try
      {
        value = getInternalQueue().submit(c).get();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    return value;
  }

  /* InternalThreadFactory definition */

  /**
   * A ThreadFactory class that can detect if a thread was spawned from an
   * instance of this ThreadFactory.
   */
  private static class InternalThreadFactory implements ThreadFactory
  {
    private static final AtomicInteger sm_poolNumberReference =
      new AtomicInteger(1);

    private final int m_poolNumber;
    private final ThreadGroup m_group;
    private final AtomicInteger m_threadNumber = new AtomicInteger(1);
    private final String m_namePrefix;

    /* Initialization */

    /**
     * Generic constructor.
     */
    public InternalThreadFactory ()
    {
      m_poolNumber = sm_poolNumberReference.getAndIncrement();

      SecurityManager s = System.getSecurityManager();
      m_group =
        ((s != null) ? s.getThreadGroup() : Thread.currentThread()
          .getThreadGroup());

      m_namePrefix = String.format(Locale.US, "pool-%d-thread-", m_poolNumber);
    }

    /* Thread checking methods */

    /**
     * Convenience method to check if the currently running thread was spawned
     * From this ThreadFactory instance.
     */
    public boolean isCurrentThreadFromThisFactory ()
    {
      return isThreadFromThisFactory(Thread.currentThread());
    }

    /**
     * Returns true if the Thread instance provided was spawned from this
     * ThreadFactory instance, otherwise, false.
     */
    public boolean isThreadFromThisFactory (Thread thread)
    {
      return   thread instanceof InternalThread
            && ((InternalThread) thread).getPoolNumber() == m_poolNumber;
    }

    /* ThreadFactory methods */

    @Override
    public Thread newThread (Runnable r)
    {
      Thread t = new InternalThread(
        m_group,
        r,
        String.format(
          Locale.US,
          "%s%d",
          m_namePrefix,
          m_threadNumber.getAndIncrement()),
        0,
        m_poolNumber);

      if (t.isDaemon())
      {
        t.setDaemon(false);
      }
      if (t.getPriority() != Thread.NORM_PRIORITY)
      {
        t.setPriority(Thread.NORM_PRIORITY);
      }
      return t;
    }

    /* InternalThread definition */

    /**
     * Internal definition of a Thread that tags its instance with a thread
     * factory pool number.
     */
    private static class InternalThread extends Thread
    {
      private final int m_poolNumber;

      /**
       * Constructor that includes a thread factory pool number tag it was
       * created with.
       */
      public InternalThread (ThreadGroup group, Runnable target, String name,
                             long stackSize, int poolNumber)
      {
        super(group, target, name, stackSize);
        m_poolNumber = poolNumber;
      }

      /**
       * Returns the pool number it was created with.
       */
      public int getPoolNumber ()
      {
        return m_poolNumber;
      }

      @Override
      public void run ()
      {
        try
        {
          super.run();
        }
        catch (Exception e)
        {
          /*
           * Do nothing. But for debugging purposes, we should print the
           * stack trace.
           */
          e.printStackTrace();
        }
      }
    }
  }
}
