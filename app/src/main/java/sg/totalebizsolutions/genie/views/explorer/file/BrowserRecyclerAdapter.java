package sg.totalebizsolutions.genie.views.explorer.file;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.base.Objects;

import java.util.ArrayList;
import java.util.List;

import sg.totalebizsolutions.genie.R;
import sg.totalebizsolutions.genie.core.file.File;
import sg.totalebizsolutions.genie.core.file.FileConstants;
import sg.totalebizsolutions.genie.databinding.FileCategoryItemBinding;
import sg.totalebizsolutions.genie.databinding.FileItemBinding;
import sg.totalebizsolutions.genie.databinding.PrimaryRecyclerViewItemBinding;
import sg.totalebizsolutions.genie.databinding.SecondaryRecyclerViewItemBinding;
import sg.totalebizsolutions.genie.views.categories.CategoryMetaData;
import sg.totalebizsolutions.genie.views.explorer.OnFileClickListener;

public class BrowserRecyclerAdapter extends RecyclerView.Adapter {
  /* Properties */
    private OnFileClickListener m_clickListener;
    private List<File> m_dataSet = new ArrayList<>();
  /* Initializations */

    public BrowserRecyclerAdapter(OnFileClickListener clickListener) {
        m_clickListener = clickListener;
    }

  /* Property methods */

    public void updateDataSet(List<File> dataSet) {
        m_dataSet.clear();
        if (dataSet != null) {
            m_dataSet.addAll(dataSet);
        }
        notifyDataSetChanged();
    }

  /* Adapter methods */

    @Override
    public int getItemCount() {
        return m_dataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        File file = m_dataSet.get(position);
        String format = file.getFormat();
        //if returns 0 means category layout inflating, else file layout inflating.
        return Objects.equal(FileConstants.FORMAT_CATEGORY, format) ? 0 : 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == 0) {
            FileCategoryItemBinding binding =
                    DataBindingUtil.inflate(inflater, R.layout.file_category_item, parent, false);
            holder = new CategoryViewHolder(binding);
        } else {
            FileItemBinding binding =
                    DataBindingUtil.inflate(inflater, R.layout.file_item, parent, false);
             holder = new FileViewHolder(binding);
            /*PrimaryRecyclerViewItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.primary_recycler_view_item, parent, false);
            holder = new FileContentViewHolder(binding);*/
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int type = getItemViewType(position);
        final File file = m_dataSet.get(position);

        if (type == 0) {
            ((CategoryViewHolder) holder).onBind(position, file);
        } else {
            ((FileViewHolder) holder).onBind(position, file);
            //((FileContentViewHolder) holder).onBind(position, file);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_clickListener.onFileClicked(file);
            }
        });
    }

    private class FileContentViewHolder extends RecyclerView.ViewHolder {

        private Activity mActivity;
        private PrimaryRecyclerViewItemBinding m_binding;

        FileContentViewHolder(PrimaryRecyclerViewItemBinding binding) {
            super(binding.getRoot());
            mActivity = (Activity) itemView.getContext();
            m_binding = binding;
        }

        void onBind(int position, File file) {
            String format = file.getFormat();
            m_binding.primaryMovieGenre.setText(format);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                    mActivity,
                    LinearLayoutManager.VERTICAL,
                    false
            );
            m_binding.secondaryRecyclerView.setLayoutManager(linearLayoutManager);
            m_binding.secondaryRecyclerView.setAdapter(getSecondaryAdapter(position));

        }

        private SecondaryAdapter getSecondaryAdapter(int position) {

            SecondaryAdapter adapter;
            switch (position) {
                case 0:
                    return new SecondaryAdapter();
                case 1:
                    return null;
                default:
                    return null;
            }
        }

        private class SecondaryAdapter extends RecyclerView.Adapter {
            private int FILETYPE_HEADER = 0;
            private int FILE_CONTENT = 1;

            @Override
            public int getItemViewType(int position) {
                File file = m_dataSet.get(position);
                String format = file.getFormat();
                if (format.equalsIgnoreCase(FileConstants.FORMAT_MP4)) {
                    return FILETYPE_HEADER;
                } else {
                    return FILE_CONTENT;
                }
            }


            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                RecyclerView.ViewHolder holder;
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                if (viewType == 0) {
                   /* FileTypeHeaderBinding binding =
                            DataBindingUtil.inflate(inflater, R.layout.file_type_header, parent, false);
                    holder = new HeaderViewHolder(binding);*/
                } else {
                    /*FileItemBinding binding =
                            DataBindingUtil.inflate(inflater, R.layout.file_item, parent, false);*//*
                    holder = new FileViewHolder(binding);
                    */
                }
                //              return holder;

                SecondaryRecyclerViewItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.secondary_recycler_view_item, parent, false);
                holder = new SecondaryViewHolder(binding);
                return holder;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                final int type = getItemViewType(position);
                final File file = m_dataSet.get(position);
                ((SecondaryViewHolder) holder).onBind(position, file);

            }

            @Override
            public int getItemCount() {
                return m_dataSet.size();
            }
        }

        private class SecondaryViewHolder extends RecyclerView.ViewHolder {

            private TextView mTextView;
            private SecondaryRecyclerViewItemBinding m_binding;
            SecondaryViewHolder (SecondaryRecyclerViewItemBinding binding)
            {
                super(binding.getRoot());
                m_binding = binding;
            }
            void onBind(int position, File file) {
                String format = file.getFormat();

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                        mActivity,
                        LinearLayoutManager.VERTICAL,
                        false
                );

                int iconResID = Objects.equal(format, FileConstants.FORMAT_PDF) ? R.drawable.ic_pdf
                        : Objects.equal(format, FileConstants.FORMAT_MP4) ? R.drawable.ic_video
                        : Objects.equal(format, FileConstants.FORMAT_MPG) ? R.drawable.ic_video
                        : Objects.equal(format, FileConstants.FORMAT_DOCS) ? R.drawable.ic_docs
                        : Objects.equal(format, FileConstants.FORMAT_XLS) ? R.drawable.ic_xls
                        : Objects.equal(format, FileConstants.FORMAT_PPT) ? R.drawable.ic_ppt
                        : 0;

                m_binding.colorView.setBackgroundResource(
                        CategoryMetaData.colorResIDForCategory(file.getCategory()));
                m_binding.imageView.setImageResource(iconResID);


                if (Objects.equal(format, FileConstants.FORMAT_MP4) || Objects.equal(format, FileConstants.FORMAT_MPG)) {
                    m_binding.pdfeyepreview.setVisibility(View.GONE);
                }
                m_binding.titleTextView.setText(file.getDisplayName());

            }

        }
    }

}
