package sg.totalebizsolutions.genie.views.explorer;

import android.content.Context;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.app.Fragment;
import android.util.StateSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.base.Objects;

import sg.totalebizsolutions.foundation.util.ViewHelper;
import sg.totalebizsolutions.foundation.view.navigation.TabNavigationFragment.TabAdapter;
import sg.totalebizsolutions.genie.R;
import sg.totalebizsolutions.genie.core.file.File;
import sg.totalebizsolutions.genie.core.file.FileConstants;
import sg.totalebizsolutions.genie.databinding.TabItemBinding;
import sg.totalebizsolutions.genie.views.categories.CategoryMetaData;
import sg.totalebizsolutions.genie.views.explorer.file.FileBrowserFragment;

class ExplorerTabAdapter extends TabAdapter {
  /* Properties */

    private File m_rootFile;
    private int drawableResID;

    ExplorerTabAdapter(File rootFile) {
        m_rootFile = rootFile;
    }

  /* Adapter methods */

    @Override
    public int getItemCount() {
        return m_rootFile.getSubFiles().size();
    }

    @Override
    public String getTitle(int index) {
        return m_rootFile.getSubFiles().get(index).getDisplayName();
    }

    @Override
    public Fragment getFragment(int position) {
        File file = m_rootFile.getSubFiles().get(position);
        return FileBrowserFragment.newInstance(file.getID(), m_rootFile.getDisplayName());
    }

    @Override
    public View getView(int position, ViewGroup parent) {
        final Context context = parent.getContext();

        final File file = m_rootFile.getSubFiles().get(position);

        final LayoutInflater inflater = LayoutInflater.from(context);
        final TabItemBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.tab_item, parent, false);

        final String category = file.getCategory();
        final String displayName = file.getDisplayName();

        final int tintColor = ViewHelper.getColor(context,
                CategoryMetaData.colorResIDForCategory(category));
        final int darkColor = ViewHelper.getColor(context, R.color.dark);

        final int[] selectedState = new int[]{android.R.attr.state_selected};
        final int[] pressedState = new int[]{android.R.attr.state_pressed};
        final int[] normalState = StateSet.WILD_CARD;

    /* Tab. */
        {
            StateListDrawable tabStateList = new StateListDrawable();
            TabDrawable tabDrawable = new TabDrawable(context, tintColor);

            tabStateList.addState(selectedState, tabDrawable);
            tabStateList.addState(pressedState, tabDrawable);
            tabStateList.addState(normalState, ViewHelper.getDrawable(context, R.color.transparent));

            ViewHelper.setBackgroundDrawable(binding.getRoot(), tabStateList);
        }

    /* Text color. */
        {
            int[] colors = new int[]{tintColor, tintColor, darkColor};
            int[][] states = new int[][]{selectedState, pressedState, normalState};
            binding.titleTextView.setTextColor(new ColorStateList(states, colors));
        }

    /* Icon resource. */
        {
            if (Objects.equal(category, FileConstants.EXTREMITIES)) {
                 drawableResID =
                        Objects.equal(displayName, FileConstants.eCAT_BRANDS) ? R.drawable.tab_brands
                                : Objects.equal(displayName, FileConstants.eCAT_MARCOM) ? R.drawable.tab_marketing
                                : Objects.equal(displayName, FileConstants.eCAT_CATALOGUE) ? R.drawable.tab_guides
                                : Objects.equal(displayName, FileConstants.eCAT_TRAINING) ? R.drawable.tab_training
                                : Objects.equal(displayName, FileConstants.eCAT_PRODUCTS) ? R.drawable.tab_products
                                : 0;

            } else if (Objects.equal(category, FileConstants.SUBCHONDROPLASTY)) {
                 drawableResID =
                         Objects.equal(displayName, FileConstants.sCAT_PRODUCTS) ? R.drawable.tab_products
                                : Objects.equal(displayName, FileConstants.sCAT_CATALOGUE) ? R.drawable.tab_guides
                                : Objects.equal(displayName, FileConstants.sCAT_TRAINING) ? R.drawable.tab_training
                                : Objects.equal(displayName, FileConstants.sCAT_MARCOM) ? R.drawable.tab_marketing
                                : Objects.equal(displayName, FileConstants.sCAT_SURGON) ? R.drawable.tips_tracks_icon_normal
                                : 0;

            } else {
                 drawableResID =
                        Objects.equal(displayName, FileConstants.spCAT_BRANDS) ? R.drawable.tab_brands
                                : Objects.equal(displayName, FileConstants.spCAT_MARCOM) ? R.drawable.tab_marketing
                                : Objects.equal(displayName, FileConstants.spCAT_CATALOGUE) ? R.drawable.tab_guides
                                : Objects.equal(displayName, FileConstants.spCAT_TRAINING) ? R.drawable.tab_training
                                : Objects.equal(displayName, FileConstants.spCAT_PRODUCTS) ? R.drawable.tab_products
                                : 0;
            }

            if (drawableResID != 0) {
                Drawable drawable = ViewHelper.getDrawable(context, drawableResID);
                Drawable tintedDrawable = ViewHelper.getDrawable(context, drawableResID);
                tintedDrawable.mutate();
                tintedDrawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_ATOP);

                StateListDrawable stateDrawable = new StateListDrawable();
                stateDrawable.addState(selectedState, tintedDrawable);
                stateDrawable.addState(pressedState, tintedDrawable);
                stateDrawable.addState(normalState, drawable);

                binding.imageView.setImageDrawable(stateDrawable);
            }
        }
        String[] str = displayName.split(" ");
        binding.titleTextView.setText(str[0]);

        return binding.getRoot();
    }
}
