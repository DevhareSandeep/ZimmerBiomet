package sg.totalebizsolutions.genie.views.explorer;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.base.Objects;

import sg.totalebizsolutions.foundation.view.navigation.NavigationHandler;
import sg.totalebizsolutions.foundation.view.navigation.TabNavigationFragment;
import sg.totalebizsolutions.genie.R;
import sg.totalebizsolutions.genie.core.file.File;
import sg.totalebizsolutions.genie.core.file.FileConstants;
import sg.totalebizsolutions.genie.databinding.MainExplorerFragmentBinding;
import sg.totalebizsolutions.genie.views.explorer.file.FileBrowserFragment;

public class MainExplorerFragment extends TabNavigationFragment {
  /* Constants */

    private static final String BUNDLE_ROOT = "rootFile";

  /* Properties */

    private File m_rooFile;

    private MainExplorerFragmentBinding m_binding;

    private NavigationHandler m_mainNavigation;

  /* Creational */

    public static MainExplorerFragment newInstance(File root) {
        MainExplorerFragment fragment = new MainExplorerFragment();
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_ROOT, root);
        fragment.setArguments(args);
        return fragment;
    }

  /* Life-cycle methods */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        m_rooFile = args.getParcelable(BUNDLE_ROOT);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null
                && parentFragment instanceof NavigationHandler) {
            m_mainNavigation = (NavigationHandler) parentFragment;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final File rootFile = m_rooFile;
        final String displayName = rootFile.getDisplayName();
        final String format = rootFile.getFormat();

    /* Setup view. */
        MainExplorerFragmentBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.main_explorer_fragment, container, false);
        m_binding = binding;

        setTabAdapter(new ExplorerTabAdapter(m_rooFile));

        Toolbar toolbar = getToolbar();
        toolbar.setTitle(displayName);
        toolbar.setNavigationOnClickListener(v ->
        {
            int stackSize = getBackStackSize();
            if (stackSize > 0) {
                popFragmentFromNavigation(true);
            } else {
                m_mainNavigation.popFragmentFromNavigation(true);
            }
        });

        int backgroundResID =
                Objects.equal(FileConstants.EXTREMITIES, displayName)
                        ? R.drawable.breadcrumbs_category_bg
                        : Objects.equal(FileConstants.SPORTSMED, displayName)
                        ? R.drawable.breadcrumbs_sportsmed_bg
                        : R.drawable.breadcrumbs_subchin_bg;

        binding.breadcrumbs.setCategory(displayName, backgroundResID);

        int themeBackgroundId =
                Objects.equal(FileConstants.EXTREMITIES, displayName) ? R.drawable.extrimitiestrans
                        : Objects.equal(FileConstants.SPORTSMED, displayName) ? R.drawable.sportstrans
                        : R.drawable.subchondtrans;
        binding.content.setBackgroundResource(themeBackgroundId);
       // binding.content.getBackground().setAlpha(127);

        Breadcrumbs.BreadcrumbListener breadcrumbListener = new Breadcrumbs.BreadcrumbListener() {
            @Override
            public void onHomeClicked() {
                if (m_mainNavigation != null) {
                    m_mainNavigation.popFragmentFromNavigation(true);
                }
            }

            @Override
            public void onBreadcrumbClicked(int index) {
                final int stackSize = getBackStackSize();
                for (int i = stackSize - 1; i > index; i--) {
                    popFragmentFromNavigation(i == stackSize - 1 || i == index + 1);
                }
            }
        };
        binding.breadcrumbs.setBreadcrumbListener(breadcrumbListener);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolbar();
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        if (childFragment instanceof FileBrowserFragment) {
            ((FileBrowserFragment) childFragment).setMainNavigatioNhandler(m_mainNavigation);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK
                | super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onTabSelected(int index) {
        super.onTabSelected(index);
        m_binding.breadcrumbs.setStackIndex(index);
    }

    @Override
    public void pushFragmentToNavigation(Fragment fragment, String tag, boolean animated) {
        super.pushFragmentToNavigation(fragment, tag, animated);
        String text = "";
        if (fragment instanceof FileBrowserFragment) {
            text = ((FileBrowserFragment) fragment).getFile().getDisplayName();
        }
        m_binding.breadcrumbs.push(text);
    }

    @Override
    public void popFragmentFromNavigation(boolean animated) {
        super.popFragmentFromNavigation(animated);
        m_binding.breadcrumbs.pop();
    }

    @Override
    protected void onNavigationChanged(Fragment oldFragment, Fragment newFragment) {
        super.onNavigationChanged(oldFragment, newFragment);
        setupToolbar();
    }

    private void setupToolbar() {
        int stackSize = getBackStackSize();
        Toolbar toolbar = getToolbar();
        toolbar.setNavigationIcon(stackSize > 0 ? R.drawable.ic_back : R.drawable.ic_home);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
