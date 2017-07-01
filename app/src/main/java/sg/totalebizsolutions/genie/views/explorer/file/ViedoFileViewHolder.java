package sg.totalebizsolutions.genie.views.explorer.file;

import android.support.v7.widget.RecyclerView;

import sg.totalebizsolutions.genie.core.file.File;
import sg.totalebizsolutions.genie.databinding.VideoFileItemBinding;

/**
 * Created by Sandeep Devhare @APAR on 4/14/2017.
 */

public class ViedoFileViewHolder extends RecyclerView.ViewHolder {
      /* Properties */

    private VideoFileItemBinding m_binding;

  /* Initializations */

    ViedoFileViewHolder (VideoFileItemBinding binding)
    {
        super(binding.getRoot());
        m_binding = binding;
    }

    void onBind (int position, File file)
    {
        String format = file.getFormat();



        m_binding.filetype.setText("Video");
    }
}
