package sg.totalebizsolutions.genie.views.explorer.file;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.google.common.base.Objects;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;

import sg.totalebizsolutions.foundation.util.Validation;
import sg.totalebizsolutions.foundation.view.navigation.NavigationHandler;
import sg.totalebizsolutions.foundation.view.navigation.NavigationItemFragment;
import sg.totalebizsolutions.foundation.widgets.SimpleTextWatch;
import sg.totalebizsolutions.genie.R;
import sg.totalebizsolutions.genie.core.Callback;
import sg.totalebizsolutions.genie.core.ZimmerServices;
import sg.totalebizsolutions.genie.core.file.File;
import sg.totalebizsolutions.genie.core.file.FileConstants;
import sg.totalebizsolutions.genie.databinding.FileBrowserFragmentBinding;
import sg.totalebizsolutions.genie.misc.widgets.LinearDividerItemDecoration;
import sg.totalebizsolutions.genie.util.AlertDialogFactory;
import sg.totalebizsolutions.genie.util.Logger;
import sg.totalebizsolutions.genie.util.Util;
import sg.totalebizsolutions.genie.views.explorer.MuPDFFragment;
import sg.totalebizsolutions.genie.views.explorer.OnFileClickListener;
import sg.totalebizsolutions.genie.views.explorer.file.details.FileFragment;

public class FileBrowserFragment extends NavigationItemFragment {
  /* Properties */

    private static final String BUNDLE_FILE_ID = "fileID";
    private static final String BUNDLE_TITLE = "title";

    private long m_fileID;
    private String m_title;
    private File m_file;

    private FileBrowserFragmentBinding m_binding;

    private List<File> m_fileSearchList;
    private Callback<List<File>> m_callback;

    private BrowserRecyclerAdapter m_adapter;

    private NavigationHandler m_mainNavigationHandler;
    private String fName;
    private int resourceID = 0;
    private ProgressDialog progressDialog;
    public static java.io.File ioFile;
    private String displayFileName;
    private Context mContext;
    private String nameOfFile;
  /* Creational */

    public FileBrowserFragment() {
    }

    public static FileBrowserFragment newInstance(long fileID, String title) {
        FileBrowserFragment fragment = new FileBrowserFragment();
        fragment.m_fileID = fileID;
        Bundle args = new Bundle();
        args.putLong(BUNDLE_FILE_ID, fileID);
        args.putString(BUNDLE_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

  /* Life-cycle methods */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(getActivity(), R.style.AppDialogTheme);
        Bundle args = getArguments();
        m_fileID = args.getLong(BUNDLE_FILE_ID);
        m_title = args.getString(BUNDLE_TITLE);
        m_file = getFile();
        if (savedInstanceState != null) {
            m_fileID = savedInstanceState.getLong(BUNDLE_FILE_ID, 0);
            m_title = savedInstanceState.getString(BUNDLE_TITLE);
        }
        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_FILE_ID)) {
            m_fileID = args.getLong(BUNDLE_FILE_ID);

        }
        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_TITLE)) {
            m_title = args.getString(BUNDLE_TITLE);
        }

        OnFileClickListener fileClickListener = new OnFileClickListener() {
            @Override
            public void onFileClicked(File file) {
                //showing categories here.
                if (Objects.equal(file.getFormat(), FileConstants.FORMAT_CATEGORY)) {
                    FileBrowserFragment.this.pushFragmentToNavigation(FileBrowserFragment.newInstance(file.getID(),
                            file.getDisplayName()));
                } else {
                    //showing content here.

                    if (Objects.equal(file.getFormat(), FileConstants.FORMAT_PDF)) {
                        try {
                            if (file.getFileName() != null) {
                                resourceID = FileBrowserFragment.this.listRaw(file.getFileName());
                                if (resourceID != 0) {
                                    nameOfFile = FileBrowserFragment.this.getResources().getResourceEntryName(resourceID);
                                    FileBrowserFragment.this.bufferFileInternal(nameOfFile);
                                    m_mainNavigationHandler.pushFragmentToNavigation(MuPDFFragment.newInstance(ioFile.toString(), file, false), null, true);
                                } else if (resourceID == 0) {
                                    AlertDialogFactory.buildAlertDialog(FileBrowserFragment.this.getActivity(), 0, R.string.file_notfound).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Logger.debug("resource not found" + e.getMessage());
                        }
                    } else {
                        resourceID = FileBrowserFragment.this.listRaw(file.getFileName());
                        if (resourceID != 0) {
                            m_mainNavigationHandler.pushFragmentToNavigation(
                                    FileFragment.newInstance(file, file.getID(), m_title), null, true);
                        } else if (resourceID == 0) {
                            AlertDialogFactory.buildAlertDialog(FileBrowserFragment.this.getActivity(), 0, R.string.file_notfound).show();
                        }

                    }
                }
            }
        };
        m_adapter = new BrowserRecyclerAdapter(fileClickListener);
    }

    private void bufferFileInternal(String nameOfFile) {
        copyFileAndOpenIt(nameOfFile);
    }

    public int listRaw(String fileName) {
        Field[] fields = R.raw.class.getFields();
        for (int i = 0; i < fields.length; i++) {
            fName = Util.formatTheString(fileName);
            if (fields[i].getName().equalsIgnoreCase(fName)) {
                Logger.debug("same");
                try {
                    resourceID = fields[i].getInt(fields[i]);
                    break;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                Logger.debug("not same");
                resourceID=0;
            }
        }

        return resourceID;

    }

    private void copyFileAndOpenIt(String filename) {
        try {
            displayFileName = filename;
            ioFile = copyFileFromAssetsToStorage(filename);

        } catch (Exception e) {
            Log.e("PdfHandler", "Error handling the PDF file", e);
        }
    }

    private java.io.File copyFileFromAssetsToStorage(String filename) throws Exception {
        String tempFilename = displayFileName;

        // AssetManager is = context.getAssets();
        //InputStream inputStream = is.open(filename);
        InputStream inputStream = mContext.getResources().openRawResource(
                mContext.getResources().getIdentifier(filename,
                        "raw", mContext.getPackageName()));

        String outFilename = mContext.getFilesDir() + "/" + tempFilename;
        FileOutputStream outputStream = mContext.openFileOutput(tempFilename, Context.MODE_PRIVATE);
        copy(inputStream, outputStream);
        outputStream.flush();
        outputStream.close();
        inputStream.close();
        return new java.io.File(outFilename);
    }

    private void copy(InputStream fis, FileOutputStream fos) throws IOException {
        byte[] b = new byte[2024];
        if (b != null) {
            b = null;
            b = new byte[2024];
        }
        int i;
        while ((i = fis.read(b)) != -1) {
            fos.write(b, 0, i);
        }
    }


    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = getContext();
        mContext = FileBrowserFragment.this.getActivity();
    /* Setup view. */

        FileBrowserFragmentBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.file_browser_fragment, container, false);
        m_binding = binding;

        binding.recyclerView.setAdapter(m_adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerView.addItemDecoration(new LinearDividerItemDecoration());

        binding.searchEditText.addTextChangedListener(new SimpleTextWatch() {
            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                searchFile(text);

                boolean isEmpty = Validation.isEmpty(text);

                m_binding.searchHeader.getRoot().setVisibility(!isEmpty ? View.VISIBLE : View.GONE);
                getToolbar().setTitle(!isEmpty ? getString(R.string.search) : m_title);
            }
        });

        binding.searchEditText.setOnEditorActionListener((v, actionId, event) ->
        {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard();
                return true;
            }
            return false;
        });

        binding.searchHeader.clearAllTextView.setOnClickListener(v ->
        {
            hideKeyboard();
            binding.searchEditText.setText(null);
        });

        Toolbar toolbar = getToolbar();
        toolbar.setTitle(m_title);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateList();
    }

  /* Contract methods */

    public void setMainNavigatioNhandler(NavigationHandler navigatioNhandler) {
        m_mainNavigationHandler = navigatioNhandler;
    }

  /* Proeprty methods */

    /**
     * Lazy-loaded method to load file instance.
     */
    public File getFile() {
        if (m_file == null) {
            m_file = ZimmerServices.getInstance().getFileService().getFile(m_fileID);
        }
        return m_file;
    }

    private void searchFile(String fileName) {
        if (Validation.isEmpty(fileName)) {
            m_fileSearchList = null;
            m_callback = null;
            updateList();
            return;
        }

        Callback<List<File>> callback = new Callback<List<File>>() {
            @Override
            public void onFinished(int status, String message, List<File> data) {
                if (this != m_callback) {
                    return;
                }

                m_fileSearchList = data;
                updateList();
            }
        };
        m_callback = callback;
        ZimmerServices.getInstance().getFileService().searchFile(m_fileID, fileName, null, callback);
    }

    private void updateList() {
        m_adapter.updateDataSet(m_fileSearchList == null ? m_file.getSubFiles() : m_fileSearchList);
    }

}
