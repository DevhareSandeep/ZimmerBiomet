package sg.totalebizsolutions.genie.views.home;

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
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.EditorInfo;

import com.google.common.base.Objects;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import sg.totalebizsolutions.foundation.util.Validation;
import sg.totalebizsolutions.foundation.view.navigation.NavigationItemFragment;
import sg.totalebizsolutions.foundation.widgets.SimpleTextWatch;
import sg.totalebizsolutions.genie.R;
import sg.totalebizsolutions.genie.core.Callback;
import sg.totalebizsolutions.genie.core.ZimmerServices;
import sg.totalebizsolutions.genie.core.file.File;
import sg.totalebizsolutions.genie.core.file.FileConstants;
import sg.totalebizsolutions.genie.databinding.HomeFragmentBinding;
import sg.totalebizsolutions.genie.misc.widgets.LinearDividerItemDecoration;
import sg.totalebizsolutions.genie.util.AlertDialogFactory;
import sg.totalebizsolutions.genie.util.Logger;
import sg.totalebizsolutions.genie.util.Util;
import sg.totalebizsolutions.genie.views.categories.CategoriesFragment;
import sg.totalebizsolutions.genie.views.categories.CategoryMetaData;
import sg.totalebizsolutions.genie.views.explorer.MainExplorerFragment;
import sg.totalebizsolutions.genie.views.explorer.MuPDFFragment;
import sg.totalebizsolutions.genie.views.explorer.OnFileClickListener;
import sg.totalebizsolutions.genie.views.explorer.file.BrowserRecyclerAdapter;
import sg.totalebizsolutions.genie.views.explorer.file.details.FileFragment;
import sg.totalebizsolutions.genie.views.home.HomeCategoryRecyclerAdapter.HomeCategoryUpdateListener;

public class HomeFragment extends NavigationItemFragment {
  /* Properties */

    private static final String STATE_SEARCH_TEXT = "searchText";
    private int resourceID = 0;
    private String nameOfFile;
    private String fName;
    private HomeFragmentBinding m_binding;
    private BrowserRecyclerAdapter m_adapter;
    public static java.io.File ioFile;
    private String displayFileName;
    private Context mContext;
    private List<File> m_fileSearchList;
    private Callback<List<File>> m_callback;

  /* Life-cycle methods */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = HomeFragment.this.getActivity();
        OnFileClickListener fileClickListener = (file) ->
        {
            if (!Objects.equal(file.getFormat(), FileConstants.FORMAT_CATEGORY)) {
                if (Objects.equal(file.getFormat(), FileConstants.FORMAT_PDF)) {
                    try {
                        if (file.getFileName() != null) {
                            resourceID = listRaw(file.getFileName());
                            Logger.debug("rsource ID 1" + resourceID);
                            if (resourceID != 0) {
                                nameOfFile = this.getResources().getResourceEntryName(resourceID);
                                bufferFileInternal(nameOfFile);
                                pushFragmentToNavigation(MuPDFFragment.newInstance(ioFile.toString(), file, false), null, true);
                            } else if (resourceID == 0) {
                                AlertDialogFactory.buildAlertDialog(mContext, 0, R.string.file_notfound).show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Logger.debug("resource not found" + e.getMessage());
                    }

                } else {
                    pushFragmentToNavigation(FileFragment.newInstance(file, file.getID(), "test"));
                }
            }
        };
        m_adapter = new BrowserRecyclerAdapter(fileClickListener);
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
                resourceID = 0;
            }
        }
        Logger.debug("rsource ID" + resourceID);
        return resourceID;

    }

    private void bufferFileInternal(String nameOfFile) {
        copyFileAndOpenIt(nameOfFile);
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
        byte[] b = new byte[1024];
        if (b != null) {
            b = null;
            b = new byte[1024];
        }
        int i;
        while ((i = fis.read(b)) != -1) {
            fos.write(b, 0, i);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final Context context = getContext();

    /* Setup view. */
        HomeFragmentBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false);
        m_binding = binding;

        binding.extremitiesContainer.setTag(FileConstants.ID_EXTREMITIES);//0
        binding.subchinContainer.setTag(FileConstants.ID_SUBCHIN);//1
        binding.sportsmedContainer.setTag(FileConstants.ID_SPORTSMED);//2

        View.OnClickListener onClickListener = (v) -> {
            int rootID = (int) v.getTag();
            File file = ZimmerServices.getInstance().getFileService().getFile(rootID);
            pushFragmentToNavigation(MainExplorerFragment.newInstance(file));
        };
        binding.extremitiesContainer.setOnClickListener(onClickListener);
        binding.subchinContainer.setOnClickListener(onClickListener);
        binding.sportsmedContainer.setOnClickListener(onClickListener);

        binding.recyclerView.setAdapter(m_adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerView.addItemDecoration(new LinearDividerItemDecoration());

        FadeInUpAnimator animator = new FadeInUpAnimator();
        animator.setAddDuration(350L);
        animator.setChangeDuration(350);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        binding.recyclerView.setItemAnimator(animator);
        HomeCategoryUpdateListener updateListener = new HomeCategoryUpdateListener() {
            @Override
            public void didUpdateSelectedCategories(List<String> categories) {
                HomeFragment.this.updateCategoryView();
            }
        };
        binding.searchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CategoryMetaData.SELECTED_CATEGORIES.size()>0)
                {
                    updateCategoryView();
                }else {
                    CategoryMetaData.SELECTED_CATEGORIES.removeAll(Arrays.asList(CategoryMetaData.CATEGORIES));
                    CategoryMetaData.SELECTED_CATEGORIES.addAll(Arrays.asList(CategoryMetaData.CATEGORIES));
                    binding.categoryRecyclerView.setAdapter(new HomeCategoryRecyclerAdapter(updateListener,CategoryMetaData.SELECTED_CATEGORIES));
                    binding.categoryRecyclerView.setLayoutManager(new HomeCategoryGridLayoutManager(context));
                    binding.categoryRecyclerView.addItemDecoration(new HomeCategoryItemSpaceDecoration());
                    updateCategoryView();
                }


            }
        });
        binding.searchEditText.addTextChangedListener(new SimpleTextWatch() {
            @Override
            public void afterTextChanged(Editable editable) {
                updateCategoryView();
                String text = editable.toString();
                boolean isSearching = !Validation.isEmpty(text);

                if (isSearching
                        && binding.recyclerView.getVisibility() == View.GONE) {
                    m_binding.recyclerView.setVisibility(View.VISIBLE);
                } else if (!isSearching
                        && binding.recyclerView.getVisibility() == View.VISIBLE) {
                    m_binding.recyclerView.setVisibility(View.GONE);
                }

                searchFile(text);

                getToolbar().setTitle(isSearching ? R.string.search : R.string.home_title);
                int searchVisibility = isSearching ? View.VISIBLE : View.GONE;

                m_binding.searchHeader.getRoot().setVisibility(searchVisibility);
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

        binding.addCategoryImageView.setOnClickListener(
                v -> pushFragmentToNavigation(new CategoriesFragment()));


       // binding.categoryRecyclerView.setAdapter(new HomeCategoryRecyclerAdapter(updateListener));
        if (CategoryMetaData.SELECTED_CATEGORIES.size()==0)
        {
            //CategoryMetaData.SELECTED_CATEGORIES.addAll(Arrays.asList(CategoryMetaData.CATEGORIES));
            binding.categoryRecyclerView.setAdapter(new HomeCategoryRecyclerAdapter(updateListener,CategoryMetaData.SELECTED_CATEGORIES));
            binding.categoryRecyclerView.setLayoutManager(new HomeCategoryGridLayoutManager(context));
            binding.categoryRecyclerView.addItemDecoration(new HomeCategoryItemSpaceDecoration());
        }else if (CategoryMetaData.SELECTED_CATEGORIES.size()>0){

            binding.categoryRecyclerView.setAdapter(new HomeCategoryRecyclerAdapter(updateListener,CategoryMetaData.SELECTED_CATEGORIES));
            binding.categoryRecyclerView.setLayoutManager(new HomeCategoryGridLayoutManager(context));
            binding.categoryRecyclerView.addItemDecoration(new HomeCategoryItemSpaceDecoration());

        }
        Toolbar toolbar = getToolbar();
        toolbar.setTitle(R.string.home_title);

    /* Restore states. */
        if (savedInstanceState != null) {
            binding.searchEditText.setText(savedInstanceState.getString(STATE_SEARCH_TEXT, ""));
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String searchText = m_binding.searchEditText.getText().toString();
        if (!Validation.isEmpty(searchText)) {
            searchFile(searchText);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        String searchText = m_binding.searchEditText.getText().toString();
        outState.putString(STATE_SEARCH_TEXT, searchText);
    }

  /* Internal methods */

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

        List<String> categoryTags = CategoryMetaData.SELECTED_CATEGORIES;
        ZimmerServices.getInstance().getFileService().searchFile(-1, fileName, categoryTags, callback);
    }

    private void updateList() {
        m_adapter.updateDataSet(m_fileSearchList);
    }

    private void updateCategoryView() {

       int size = CategoryMetaData.SELECTED_CATEGORIES.size();
        m_binding.categoryRecyclerView.setVisibility(size == 0 ? View.GONE : View.VISIBLE);
        searchFile(m_binding.searchEditText.getText().toString());
    }
}
