package sg.totalebizsolutions.genie.views.explorer.file.details;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.MediaController;

import com.google.common.base.Objects;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;

import sg.totalebizsolutions.foundation.view.navigation.NavigationItemFragment;
import sg.totalebizsolutions.genie.R;
import sg.totalebizsolutions.genie.core.file.File;
import sg.totalebizsolutions.genie.core.file.FileConstants;
import sg.totalebizsolutions.genie.databinding.FileFragmentBinding;
import sg.totalebizsolutions.genie.util.AlertDialogFactory;
import sg.totalebizsolutions.genie.util.Logger;
import sg.totalebizsolutions.genie.util.Util;

import static android.content.Context.WINDOW_SERVICE;

public class FileFragment extends NavigationItemFragment {
    /* Properties */
    private static final String AUTHORITY = "sg.totalebizsolutions.zimmer";
    private static final String BUNDLE_TITLE = "title";
    private static final String BUNDLE_FILE_ID = "fileID";
    private static final String BUNDLE_FILE = "file";
    private Context mContext;
    private File m_file;
    FileFragmentBinding mBinding;
    private int resourceID;
    private Uri uri;
    private MediaController mediaController;
    private int position = 0;
    private String fName;
    private String displayFileName;
    public static java.io.File ioFile;
    private long m_fileID;
    private String m_title;
    URL url = null;
    private String nameOfFile;
    private String contentType;
    private String marketApp_ID;

  /* Creational */

    public static FileFragment newInstance(File file, long fileID, String title) {
        FileFragment fragment = new FileFragment();
        fragment.m_fileID = fileID;
        Bundle args = new Bundle();
        args.putLong(BUNDLE_FILE_ID, fileID);
        args.putParcelable(BUNDLE_FILE, file);
        args.putString(BUNDLE_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

  /* Life-cycle methods */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
        Bundle args = getArguments();
        m_file = args.getParcelable(BUNDLE_FILE);
        m_fileID = args.getLong(BUNDLE_FILE_ID);
        m_title = args.getString(BUNDLE_TITLE);
        if (savedInstanceState != null) {
            m_fileID = savedInstanceState.getLong(BUNDLE_FILE_ID, 0);
        }
        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_FILE_ID)) {
            m_fileID = args.getLong(BUNDLE_FILE_ID);

        }
        if (savedInstanceState != null) {

            m_title = savedInstanceState.getString(BUNDLE_TITLE);
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_TITLE)) {
            m_title = args.getString(BUNDLE_TITLE);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

    /* Setup view. */
        FileFragmentBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.file_fragment, container, false);
        mBinding = binding;
        String format = m_file.getFormat();
        mContext = FileFragment.this.getActivity();
        int iconResID = Objects.equal(format, FileConstants.FORMAT_PDF) ? R.drawable.ic_pdf
                : Objects.equal(format, FileConstants.FORMAT_MP4) ? R.drawable.ic_video
                : Objects.equal(format, FileConstants.FORMAT_MPG) ? R.drawable.ic_video
                : Objects.equal(format, FileConstants.FORMAT_DOCS) ? R.drawable.ic_docs
                : Objects.equal(format, FileConstants.FORMAT_XLS) ? R.drawable.ic_xls
                : Objects.equal(format, FileConstants.FORMAT_PPT) ? R.drawable.ic_ppt
                : 0;
        listRaw(m_file.getFileName());
        binding.imageView.setImageResource(iconResID);
        binding.fileNameTextView.setText(m_file.getDisplayName());

        StringBuilder builder = new StringBuilder();
        buildBreadcrumbs(m_file, builder);
        binding.breadcrumbsTextView.setText(builder.toString());
        //  mBinding.videoview.setDimensions(600, 600);
        binding.formatTextView.setText(m_file.getFileName());
        // binding.duration.setText("Duration: "+binding.videoview.getDuration());
        binding.webview.setVisibility(View.GONE);
        hideKeyBoard();
        Toolbar toolbar = getToolbar();
        toolbar.setTitle(m_file.getDisplayName());

        toolbar.inflateMenu(R.menu.share);
        toolbar.setOnMenuItemClickListener(menuItem ->
        {
            shareVideo();

            return true;
        });

        return binding.getRoot();
    }

    private void shareVideo() {

        java.io.File fileIs = new java.io.File(uri.getPath());
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        emailIntent.setType("video/3gp");
        // the attachment
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        // the mail subject
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Share from Zimmer mobile app.");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "I am sharing you "+m_file.getDisplayName()+"."+m_file.getFormat()+" file.");
        startActivityForResult(Intent.createChooser(emailIntent, "Send email..."),0);

    }

    public void listRaw(String fileName) {
        // Util.getRawResIdByName(fileName,mContext);
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
        if (resourceID == 0) {
            AlertDialogFactory.buildAlertDialog(FileFragment.this.getActivity(), 0, R.string.file_notfound).show();
        } else {
            openFile(resourceID);
        }

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mBinding.formatTextView.setVisibility(View.GONE);
            mBinding.breadcrumbsTextView.setVisibility(View.GONE);
            mBinding.fileNameTextView.setVisibility(View.GONE);
            mBinding.fullscreen.setImageResource(R.drawable.ic_fullscreen_exit);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mBinding.videoview.getLayoutParams();
            params.setMargins(0, 0, 0, 0); //substitute parameters for left, top, right, bottom
            mBinding.videoview.setLayoutParams(params);
            mBinding.fullscreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Display display = ((WindowManager) mContext.getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
                    final int orientation = getActivity().getResources().getConfiguration().orientation;
                    //final int orientation = display.getOrientation();
                    // OR: orientation = getRequestedOrientation();

                    switch (orientation) {
                        case Configuration.ORIENTATION_PORTRAIT:
                            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            break;
                        case Configuration.ORIENTATION_LANDSCAPE:
                            //mBinding.fullscreen.setImageResource(R.drawable.ic_fullscreen_exit);
                            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            break;
                        default:
                            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            break;

                    }

                }
            });
        } else {
            mBinding.fullscreen.setImageResource(R.drawable.ic_fullscreen);
            mBinding.formatTextView.setVisibility(View.VISIBLE);
            mBinding.breadcrumbsTextView.setVisibility(View.VISIBLE);
            mBinding.fileNameTextView.setVisibility(View.VISIBLE);
            mBinding.imageView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onResume() {

        super.onResume();
        hideKeyBoard();

        if (mediaController!=null)
        {
            mediaController=null;
            mediaController = new MediaController(mContext);

        }
    }

    private void openFile(int resourceID) {
        uri = Util.buildURiPath(resourceID);
        nameOfFile = this.getResources().getResourceEntryName(resourceID);
        bufferFileInternal(nameOfFile);

        switch (m_file.getFormat()) {

            case FileConstants.FORMAT_MP4:
                Display display = ((WindowManager) mContext.getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
                final int orientation = getActivity().getResources().getConfiguration().orientation;

                mBinding.fullscreen.setVisibility(View.VISIBLE);
                try {
                    mBinding.videoview.setVideoURI(uri);
                } catch (Exception e) {
                    Logger.debug("Error: " + e.getMessage());
                    e.printStackTrace();
                }
                mBinding.videoview.requestFocus();
                mBinding.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mediaController==null)
                        {
                            mediaController = new MediaController(mContext);
                            mBinding.videoview.setMediaController(mediaController);
                            mediaController.setAnchorView(mBinding.videoview);
                            mBinding.videoview.start();
                            mBinding.imageView.setVisibility(View.INVISIBLE);
                        }


                    }
                });

                mBinding.videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                            @Override
                            public void onVideoSizeChanged(MediaPlayer mediaPlayer, int width, int height) {
                                if (mediaController == null){
                                    mediaController = new
                                            MediaController(mContext);
                                    mBinding.videoview.setMediaController(mediaController);
                                    mediaController.setAnchorView(mBinding.videoview);
                                    mBinding.imageView.setVisibility(View.GONE);

                                }

                            }
                        });
                        Logger.debug("Duration = " +
                                mBinding.videoview.getDuration());

                    }
                });
                mBinding.videoview.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        AlertDialogFactory.buildAlertDialog(mContext, 0, R.string.video_playing_error).show();
                        return false;
                    }
                });

                mBinding.fullscreen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        switch (orientation) {
                            case Configuration.ORIENTATION_PORTRAIT:
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                               // Toast.makeText(mContext, "landscape mode", Toast.LENGTH_SHORT).show();
                                break;
                            case Configuration.ORIENTATION_LANDSCAPE:
                                //mBinding.fullscreen.setImageResource(R.drawable.ic_fullscreen_exit);
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                               // Toast.makeText(mContext, "portrait mode", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                                break;

                        }

                    }
                });




                break;
            case FileConstants.FORMAT_DOCS:
                contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                marketApp_ID = "com.microsoft.office.word&hl=en";
                mBinding.videoview.setVisibility(View.GONE);

                try {
                    viewFileByIntent(ioFile, contentType, marketApp_ID);

                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.debug("resource not found" + e.getMessage());
                }
                break;
            case FileConstants.FORMAT_PPT:
                contentType = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
                marketApp_ID = "com.microsoft.office.powerpoint&hl=en";
                try {
                    viewFileByIntent(ioFile, contentType, marketApp_ID);
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.debug("resource not found" + e.getMessage());
                }
                break;
            case FileConstants.FORMAT_MPG:
                break;
            case FileConstants.FORMAT_XLS:
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                marketApp_ID = "com.microsoft.office.excel&hl=en";
                try {
                   /* nameOfFile = this.getResources().getResourceEntryName(resourceID);
                    bufferFileInternal(nameOfFile);*/
                    viewFileByIntent(ioFile, contentType, marketApp_ID);

                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.debug("resource not found" + e.getMessage());
                }
                break;
            default:
                break;
        }

    }



    private void viewFileByIntent(java.io.File path, String contentType, String MARKETAPP_ID) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(path), contentType);
        try {
            startActivity(intent);
            //  startActivity(objIntent);
        } catch (ActivityNotFoundException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("No Application Found");
            builder.setMessage("Download one from Android Market?");
            builder.setPositiveButton("Yes, Please",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                            marketIntent.setData(Uri.parse("market://details?id=" + MARKETAPP_ID));
                            startActivity(marketIntent);
                        }
                    });
            builder.setNegativeButton("No, Thanks", null);
            builder.create().show();
        }
    }

    protected void openFileByIntent(java.io.File ioFile) {
        // File file = new File(filePath);
        MimeTypeMap map = MimeTypeMap.getSingleton();
        String ext = MimeTypeMap.getFileExtensionFromUrl(ioFile.getName());
        String type = map.getMimeTypeFromExtension(ext);

        if (type == null)
            type = "*/*";

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.fromFile(ioFile);

        intent.setDataAndType(data, type);

        startActivity(intent);
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
        hideKeyBoard();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        hideKeyBoard();
    }
    protected void hideKeyBoard() {
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    private void buildBreadcrumbs(File file, StringBuilder builder) {
        File parent = file.getParent();
        if (parent != null) {
            buildBreadcrumbs(parent, builder);

            if (builder.length() > 0) {
                builder.append(" > ");
            }
            builder.append(parent.getDisplayName());
        }
    }


}
