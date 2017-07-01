package sg.totalebizsolutions.genie.views.home;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import sg.totalebizsolutions.genie.R;
import sg.totalebizsolutions.genie.databinding.HomeCategoryItemBinding;

class HomeCategoryRecyclerAdapter extends RecyclerView.Adapter
{
  /* Properties */

  private HomeCategoryUpdateListener m_updateListener;
  private List<String> categoriesList;
  HomeCategoryRecyclerAdapter (HomeCategoryUpdateListener updateListener)
  {
    m_updateListener = updateListener;
  }

  public HomeCategoryRecyclerAdapter(HomeCategoryUpdateListener updateListener,List<String> listCategories) {
    m_updateListener = updateListener;
    categoriesList = listCategories;
  }

  /* Adapter methods */

  @Override
  public int getItemCount ()
  {
    //return CategoryMetaData.SELECTED_CATEGORIES.size();
    return categoriesList.size();
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType)
  {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    HomeCategoryItemBinding binding =
        DataBindingUtil.inflate(inflater, R.layout.home_category_item, parent, false);

    return new InternalViewHolder(binding);
  }

  @Override
  public void onBindViewHolder (RecyclerView.ViewHolder holder, int position)
  {
    ((InternalViewHolder) holder).onBind(position);
  }

  private class InternalViewHolder extends RecyclerView.ViewHolder
  {
    private HomeCategoryItemBinding im_binding;

    InternalViewHolder (HomeCategoryItemBinding binding)
    {
      super(binding.getRoot());
      im_binding = binding;
    }

    void onBind (int position)
    {
      //final String category = CategoryMetaData.SELECTED_CATEGORIES.get(position);
      final String category = categoriesList.get(position);
      im_binding.titleTextView.setText(category);
      im_binding.removeImageView.setOnClickListener(v ->
      {
        int adapterPosition = getAdapterPosition();
        //CategoryMetaData.SELECTED_CATEGORIES.remove(adapterPosition);
        categoriesList.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
        //m_updateListener.didUpdateSelectedCategories(CategoryMetaData.SELECTED_CATEGORIES);
        m_updateListener.didUpdateSelectedCategories(categoriesList);
      });
    }
  }

  interface HomeCategoryUpdateListener
  {
    void didUpdateSelectedCategories(List<String> selectedCategoryes);
  }
}
