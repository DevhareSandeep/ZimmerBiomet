package sg.totalebizsolutions.genie.views.explorer.file;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.common.base.Objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sg.totalebizsolutions.genie.R;
import sg.totalebizsolutions.genie.core.file.File;
import sg.totalebizsolutions.genie.core.file.FileConstants;
import sg.totalebizsolutions.genie.databinding.FileItemBinding;
import sg.totalebizsolutions.genie.views.categories.CategoryMetaData;

class FileViewHolder extends RecyclerView.ViewHolder
{
  /* Properties */

  private FileItemBinding m_binding;
  List<String> fileNames = new ArrayList<>();
  Map<String, List<String>> map = new HashMap<String, List<String>>();
    private RecyclerView mSecondaryRecyclerView;
  /* Initializations */

  FileViewHolder (FileItemBinding binding)
  {
    super(binding.getRoot());
    m_binding = binding;
  }
    public FileViewHolder(View itemView) {
        super(itemView);

      //  mSecondaryRecyclerView = (RecyclerView) itemView.findViewById(R.id.secondary_recycler_view);
    }

  void onBind (int position, File file)
  {
    String format = file.getFormat();
 /*   fileNames.add(file.getDisplayName());
    if (format.equalsIgnoreCase(FileConstants.FORMAT_MP4))
    {
      map.put(format,fileNames);
    }else {
      map.put(format,fileNames);
    }
    for (Map.Entry<String, List<String>> entry : map.entrySet()) {
      String key = entry.getKey();
      List<String> values = entry.getValue();
      System.out.println("Key = " + key);
      System.out.println("Values = " + values + "n");
      if (key.equalsIgnoreCase(FileConstants.FORMAT_MP4))
      {
        m_binding.filetype.setVisibility(View.VISIBLE);
        m_binding.filetype.setText(format);
        break;
      }else {
      *//*  m_binding.filetype.setVisibility(View.VISIBLE);
        m_binding.filetype.setText(format);*//*
        break;
      }
    }
 */   int iconResID =  Objects.equal(format, FileConstants.FORMAT_PDF) ? R.drawable.ic_pdf
                   : Objects.equal(format, FileConstants.FORMAT_MP4) ? R.drawable.ic_video
                   : Objects.equal(format, FileConstants.FORMAT_MPG) ? R.drawable.ic_video
                   : Objects.equal(format, FileConstants.FORMAT_DOCS) ? R.drawable.ic_docs
                   : Objects.equal(format, FileConstants.FORMAT_XLS) ? R.drawable.ic_xls
                   : Objects.equal(format, FileConstants.FORMAT_PPT) ? R.drawable.ic_ppt
                   : 0;

    m_binding.colorView.setBackgroundResource(
        CategoryMetaData.colorResIDForCategory(file.getCategory()));
    m_binding.imageView.setImageResource(iconResID);


   /* if (Objects.equal(format, FileConstants.FORMAT_MP4)||Objects.equal(format, FileConstants.FORMAT_MPG))
    {
      m_binding.pdfeyepreview.setVisibility(View.GONE);
    }*/
    m_binding.titleTextView.setText(file.getDisplayName());

  }


}
