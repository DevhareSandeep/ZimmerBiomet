package sg.totalebizsolutions.genie.core.file.commands;

import java.util.ArrayList;
import java.util.List;

import sg.totalebizsolutions.foundation.util.Validation;
import sg.totalebizsolutions.genie.core.Callback;
import sg.totalebizsolutions.genie.core.file.File;
import sg.totalebizsolutions.genie.core.file.FileConstants;
import sg.totalebizsolutions.genie.core.file.storage.FileStorage;

public class SearchCmd implements Runnable
{
  /* Properties */

  private FileStorage m_storage;

  private long m_rootFolder;
  private String m_fileName;
  private List<String> m_categoryTags;
  private Callback<List<File>> m_callback;

  public SearchCmd (FileStorage storage, long rootFolder, String fileName,
      List<String> categoryTags, Callback<List<File>> callback)
  {
    m_storage = storage;
    m_rootFolder = rootFolder;
    m_fileName = fileName;
    m_categoryTags = categoryTags;
    m_callback = callback;
  }

  @Override
  public void run ()
  {
    List<File> files = m_storage.getIncludeSubFiles(m_rootFolder, m_categoryTags);

    List<File> result = new ArrayList<>();
    for (File file : files)
    {
      searchFiles(m_fileName, file, result);
    }

    if (m_callback != null)
    {
      m_callback.onFinished(Callback.STATUS_OK, null, result);
    }
  }

  private void searchFiles (String fileName, File refFile, List<File> result)
  {
    if (   !Validation.contains(FileConstants.FORMAT_CATEGORY, refFile.getFormat())
        &&  Validation.contains(refFile.getDisplayName(), fileName))
    {
      result.add(refFile);
    }

    for (File subFile : refFile.getSubFiles())
    {
      searchFiles(fileName, subFile, result);
    }
  }
}
