package sdejesus.portfolio;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class ProjectAdapter extends RecyclerViewCursorAdapter<ProjectAdapter.ViewHolder> implements View.OnClickListener {

    private final LayoutInflater mLayoutInflater;
    private ProjectAdapter.OnItemClickListener onItemClickListener;

    public ProjectAdapter(Context context) {
        super(context);
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        holder.bindData(cursor);
    }

    public void setOnItemClickListener(final ProjectAdapter.OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = this.mLayoutInflater.inflate(R.layout.list_item_project, parent, false);
        view.setOnClickListener(this);

        return new ProjectAdapter.ViewHolder(view,getContext());
    }

    @Override
    public void onClick(View view) {
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

    public interface OnItemClickListener
    {
        void onItemClicked(Cursor cursor,View view);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView nameView;
        public final ImageView avatarView;
        private Context context;
        public ViewHolder(View itemView,final Context ctx) {
            super(itemView);
            context = ctx;
            nameView = (TextView) itemView.findViewById(R.id.item_project_textview_name);
            avatarView = (ImageView) itemView.findViewById(R.id.item_project_image);

        }
        public void bindData(final Cursor cursor)
        {
            int drawableResourceId = context.getResources().getIdentifier(cursor.getString(ProjectActivity.COL_PROJECT_AVATAR), "drawable"
                    , context.getPackageName());
            this.nameView.setText(cursor.getString(ProjectActivity.COL_PROJECT_NAME));
            this.avatarView.setImageResource(drawableResourceId);
            this.avatarView.setContentDescription(cursor.getString(ProjectActivity.COL_PROJECT_NAME));
        }
    }
}