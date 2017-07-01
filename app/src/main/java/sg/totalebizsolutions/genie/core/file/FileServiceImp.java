package sg.totalebizsolutions.genie.core.file;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;
import java.util.concurrent.Callable;

import sg.totalebizsolutions.foundation.core.Executor;
import sg.totalebizsolutions.foundation.util.PreferenceUtil;
import sg.totalebizsolutions.genie.core.Callback;
import sg.totalebizsolutions.genie.core.file.commands.DemoInitCmd;
import sg.totalebizsolutions.genie.core.file.commands.SearchCmd;
import sg.totalebizsolutions.genie.core.file.storage.FileRealmStorage;
import sg.totalebizsolutions.genie.core.file.storage.FileStorage;

public class FileServiceImp extends Executor implements FileService
{
  /* Properties */

  private Context m_context;
  private FileStorage m_storage;

  public FileServiceImp (Context context)
  {
    m_context = context;
  }

  /* Life-cycle callbacks */

  @Override
  public void onCreate ()
  {
    super.onCreate();
    execute(this::setup);
  }

  private void setup ()
  {
    SharedPreferences prefs = m_context.getSharedPreferences("filePrefs", Context.MODE_PRIVATE);
    m_storage = new FileRealmStorage();
    //if application newly installs then it goes to here
    if (!PreferenceUtil.getBoolProperty(prefs, "init", false))
    {
      execute(new DemoInitCmd(m_context, m_storage));
      PreferenceUtil.setBoolProperty(prefs, "init", true);
    }
  }

  /* Property methods */

  @Override
  public File getFile (long fileID)
  {
    Callable<File> callable = () -> m_storage.get(fileID);
    return execute(callable);
  }

  @Override
  public void searchFile (long rootFolder, String fileName, List<String> categoryTags,
      Callback<List<File>> callback)
  {
    Callback<List<File>> wrapperCallback = (status, message, data) ->
    {
      if (callback != null)
      {
        runOnUIThread(() -> callback.onFinished(status, message, data));
      }
    };
    execute(new SearchCmd(m_storage, rootFolder, fileName, categoryTags, wrapperCallback));
  }
}
