package sg.totalebizsolutions.genie.core;

import android.content.Context;

import sg.totalebizsolutions.foundation.core.Executor;
import sg.totalebizsolutions.genie.core.file.FileService;
import sg.totalebizsolutions.genie.core.file.FileServiceImp;

public class ZimmerServices extends Executor
{
  /* Properties */

  private static ZimmerServices sm_instance;
  public static ZimmerServices getInstance ()
  {
    // Creates a single static instance of ZimmerServices
    return sm_instance;
  }

  private Context m_context;
  private FileServiceImp m_fileService;

  public static void init (Context context)
  {
    sm_instance = new ZimmerServices(context);
  }

  public ZimmerServices (Context context)
  {
    m_context = context;
  }

  /* Life-cycle methods */

  @Override
  public void onCreate ()
  {
    super.onCreate();

    m_fileService = new FileServiceImp(m_context);
    m_fileService.onCreate();
  }

  public FileService getFileService ()
  {
    return m_fileService;
  }
}
