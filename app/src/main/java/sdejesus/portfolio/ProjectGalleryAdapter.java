package sdejesus.portfolio;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by sdejesus on 7/9/16.
 */

public class ProjectGalleryAdapter extends RecyclerViewCursorAdapter<ProjectGalleryAdapter.ViewHolder> implements View.OnClickListener {


    private final LayoutInflater mLayoutInflater;
    private OnItemClickListener onItemClickListener;

    public ProjectGalleryAdapter(Context context) {
        super(context);
        this.mLayoutInflater = LayoutInflater.from(context);

    }
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType)
    {
        final View view = this.mLayoutInflater.inflate(R.layout.list_item_project_gallery, parent, false);
        view.setOnClickListener(this);

        return new ViewHolder(view,getContext());
    }
    @Override
    public void onBindViewHolder(final ProjectGalleryAdapter.ViewHolder holder, Cursor cursor) {
        holder.bindData(cursor);
    }

    @Override
    public void onClick(final View view)
    {
        if (this.onItemClickListener != null)
        {
            final RecyclerView recyclerView = (RecyclerView) view.getParent();
            final int position = recyclerView.getChildLayoutPosition(view);
            if (position != RecyclerView.NO_POSITION)
            {
                final Cursor cursor = this.getItem(position);
                this.onItemClickListener.onItemClicked(cursor,view);
            }
        }
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener
    {
        void onItemClicked(Cursor cursor,View view);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final ImageView avatarView;
        private Context context;

        public ViewHolder(View itemView,final Context ctx) {
            super(itemView);
            context = ctx;
            avatarView = (ImageView) itemView.findViewById(R.id.item_project_gallery_image);
        }
        public void bindData(final Cursor cursor)
        {
            int drawableResourceId = context.getResources().getIdentifier(cursor.getString(FragmentProjectGallery.COL_PROJECT_GALLERY_AVATAR), "drawable"
                    , context.getPackageName());
            this.avatarView.setImageResource(drawableResourceId);
        }
    }
}