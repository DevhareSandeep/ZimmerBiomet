package sg.totalebizsolutions.genie.core.file.storage;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.annotations.RealmModule;
import sg.totalebizsolutions.genie.core.file.File;

public class FileRealmStorage implements FileStorage
{
  /* Properties */

  private Realm m_realm;

  /* Initializations */

  public FileRealmStorage ()
  {
    RealmConfiguration config = new RealmConfiguration.Builder()
        .name("file.realm")
        .schemaVersion(1)
        .modules(new FileModule())
        .deleteRealmIfMigrationNeeded()
        .build();
    m_realm = Realm.getInstance(config);
  }

  /* Methods */

  @Override
  public List<File> getIncludeSubFiles (long rootFolderID, List<String> categoryTags)
  {
    RealmQuery<File> query = m_realm.where(File.class);
    if (rootFolderID != -1)
    {
      query.equalTo("parent.ID", rootFolderID);
    }
    else
    {
      query.equalTo(File.Properties.PROP_LEVEL, 2);

      if (    categoryTags != null
          && !categoryTags.isEmpty())
      {
        String[] inStrings = new String[categoryTags.size()];
        categoryTags.toArray(inStrings);
        query.in(File.Properties.PROP_DISPLAY_NAME, inStrings);
      }
    }
    List<File> queryResult = query.findAll();

    List<File> result = new ArrayList<>();
    for (File file : queryResult)
    {
      result.add(file.deepCopy());
    }
    return result;
  }

  @Override
  public File get (long fileID)
  {
    File result = m_realm.where(File.class)
        .equalTo(File.Properties.PROP_ID, fileID)
        .findFirst();
    return result != null ? result.copy() : null;
  }

  @Override
  public long insertOrReplace (File file)
  {
    m_realm.beginTransaction();
    File newInstance = m_realm.copyToRealmOrUpdate(file);
    m_realm.commitTransaction();
    return newInstance.getID();
  }

  @Override
  public void bulkInsertOrReplace (List<File> files)
  {
    m_realm.executeTransaction(realm -> {
      realm.copyToRealmOrUpdate(files);
    });
  }

  @Override
  public void delete (long fileID)
  {
    m_realm.executeTransaction(realm -> {
      realm.where(File.class)
          .equalTo(File.Properties.PROP_ID, fileID)
          .findFirst()
          .deleteFromRealm();
    });
  }

  @Override
  public void clearAll ()
  {
    m_realm.executeTransaction(realm -> {
      realm.where(File.class).findAll().deleteAllFromRealm();
    });
  }

  @RealmModule(library = true, classes = { File.class })
  private class FileModule
  {
  }
}
