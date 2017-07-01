package sg.totalebizsolutions.genie.views.explorer.file;

import android.support.v7.widget.RecyclerView;

import java.util.Locale;

import sg.totalebizsolutions.genie.core.file.File;
import sg.totalebizsolutions.genie.databinding.FileCategoryItemBinding;

class CategoryViewHolder extends RecyclerView.ViewHolder
{
  /* Properties */

  private FileCategoryItemBinding m_binding;

  /* Initializations */

  CategoryViewHolder (FileCategoryItemBinding binding)
  {
    super(binding.getRoot());
    m_binding = binding;
  }

  void onBind (int position, File file)
  {
    String displayName = file.getDisplayName();

    StringBuilder builder = new StringBuilder();

    String[] texts = displayName.split(" ");
    for (int i = 0; i < Math.min(2, texts.length); i++)
    {
      char fChar = texts[i].charAt(0);
      int ascii = (int) fChar;
      if (   (ascii >= 65 && ascii <= 90)
          || (ascii >= 97 && ascii <= 122))
      {
        builder.append(fChar);
      }
    }

    m_binding.titleTextView.setText(displayName);
    m_binding.acronymTextView.setText(builder.toString().toUpperCase(Locale.getDefault()));
  }
}
