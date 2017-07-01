package sg.totalebizsolutions.genie.core.file;

import java.util.List;

import sg.totalebizsolutions.genie.core.Callback;

public interface FileService
{
  /**
   * Retrieve existing file with the specified id.
   */
  File getFile(long fileID);

  /**
   * Search for file with specified file name.
   */
  void searchFile(long rootFolder, String fileName, List<String> categoryTags,
                  Callback<List<File>> callback);
}
