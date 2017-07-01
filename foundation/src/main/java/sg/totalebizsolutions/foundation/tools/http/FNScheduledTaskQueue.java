package sg.totalebizsolutions.foundation.tools.http;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FNScheduledTaskQueue
{
  private ScheduledExecutorService m_executor;

  /* Initializations */

  /**
   * Creates a single threaded scheduled executor that schedules command to run
   * on a given delay or execute sequentially.
   */
  public FNScheduledTaskQueue ()
  {
    this(0);
  }

  /**
   * Creates a threaded scheduled executor that executes scheduled command on a
   * given no of thread pools.
   */
  public FNScheduledTaskQueue (int poolSize)
  {
    m_executor = poolSize == 0 ?
           Executors.newSingleThreadScheduledExecutor()
         : Executors.newScheduledThreadPool(poolSize);
  }

  /**
   * Schedules a {@link Runnable} instance that will be executed on the
   * specified delay relative to the time unit given.
   *
   * @param runnable
   *          the task to execute
   * @param delay
   *          the time when the runnable will be executed
   * @param timeUnit
   *          the time unit of the delay parameter
   */
  public void schedule (Runnable runnable, long delay, TimeUnit timeUnit)
  {
    if (isShutdown())
    {
      return;
    }

    Runnable wrapper = buildWrapperRunnable(runnable);
    m_executor.schedule(wrapper,delay, timeUnit);
  }

  /**
   * Schedules a task that will be executed on the specified delay relative to
   * the time unit and waits for the task to be executed and retrieve result
   * data.
   * 
   * @param callable
   *          the task to execute
   * @param delay
   *          the time when the runnable will be executed
   * @param timeUnit
   *          the time unit of the delay parameter
   * @return V data
   */
  public <V> V scheduleAndWait (Callable<V> callable, long delay,
      TimeUnit timeUnit)
  {
    if (isShutdown())
    {
      return null;
    }

    V value = null;
    try
    {
      value = m_executor.schedule(callable, delay, timeUnit).get();
    }
    catch (InterruptedException | ExecutionException e)
    {
      e.printStackTrace();
    }
    return value;
  }

  /**
   * Schedules that will execute the specified {@link Runnable} task at initial
   * time delay periodically at the given period relative to time unit given.
   *
   * @param runnable
   *          the task to execute
   * @param initialDelay
   *          the initial delay for the first task to execute
   * @param period
   *          the time between successive execution
   * @param timeUnit
   *          the time of the initial delay and period
   */
  public void scheduleAtFixedrate (Runnable runnable, long initialDelay,
      long period, TimeUnit timeUnit)
  {
    if (isShutdown())
    {
      return;
    }

    Runnable wrapper = buildWrapperRunnable(runnable);
    m_executor.scheduleAtFixedRate(wrapper, initialDelay, period, timeUnit);
  }

  /**
   * Schedules that will execute the specified {@link Runnable} task at initial
   * time delay periodically and subsequently after each task termination at
   * a given period.
   *
   * @param runnable
   *          the task to execute
   * @param initialDelay
   *          the initial delay for the first task to execute
   * @param period
   *          the time between successive execution
   * @param timeUnit
   *          the time of the initial delay and period
   */
  public void scheduleAtFixedDelay (Runnable runnable, long initialDelay,
      long period, TimeUnit timeUnit)
  {
    if (isShutdown())
    {
      return;
    }

    Runnable wrapper = buildWrapperRunnable(runnable);
    m_executor.scheduleWithFixedDelay(wrapper, initialDelay, period, timeUnit);
  }

  /**
   * @return true if the executor has been shutdown, otherwise, false. See
   *         {@link #shutdown()}.
   */
  public boolean isShutdown ()
  {
    return m_executor.isShutdown();
  }

  /**
   * Stop all active executing task and awaiting tasks. Calling this method will
   * no longer accept any future task to be scheduled.
   */
  public void shutdown ()
  {
    m_executor.shutdownNow();
  }

  /* Internal methods */

  /**
   * Builds a wrapper runnable instances for the specified runnable instance.
   */
  private Runnable buildWrapperRunnable (final Runnable runnable)
  {
    Runnable wrapper = new Runnable()
    {
      @Override
      public void run ()
      {
        try
        {
          runnable.run();
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
    };
    return wrapper;
  }
}
