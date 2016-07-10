package sdejesus.portfolio;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoryAdapter extends RecyclerViewCursorAdapter<CategoryAdapter.ViewHolder> implements View.OnClickListener {

    private final LayoutInflater mLayoutInflater;
    private SkillAdapter.OnItemClickListener onItemClickListener;

    public CategoryAdapter(Context context) {
        super(context);
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = this.mLayoutInflater.inflate(R.layout.list_item_category, parent, false);
        view.setOnClickListener(this);

        return new ViewHolder(view,getContext());
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
                this.onItemClickListener.onItemClicked(cursor);
            }
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        holder.bindData(cursor);
    }

    public interface OnItemClickListener
    {
        void onItemClicked(Cursor cursor);
    }

    public void setOnItemClickListener(final SkillAdapter.OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView nameView;
        public final ImageView avatarView;
        private Context context;
        public ViewHolder(View itemView,final Context ctx) {
            super(itemView);
            context = ctx;
            nameView = (TextView) itemView.findViewById(R.id.item_category_textview_name);
            avatarView = (ImageView) itemView.findViewById(R.id.item_category_image_avatar);
        }
        public void bindData(final Cursor cursor)
        {
            this.nameView.setText(cursor.getString(FragmentCategories.COL_CATEGORY_NAME));
            int drawableResourceId = context.getResources().getIdentifier(cursor.getString(FragmentCategories.COL_CATEGORY_AVATAR), "drawable"
                    , context.getPackageName());
            this.avatarView.setImageResource(drawableResourceId);
            this.avatarView.setContentDescription(cursor.getString(FragmentCategories.COL_CATEGORY_DESCRIPTION));
        }
    }
}