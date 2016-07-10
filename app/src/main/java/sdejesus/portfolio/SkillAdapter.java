package sdejesus.portfolio;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by sdejesus on 7/6/16.
 */

public class SkillAdapter extends RecyclerViewCursorAdapter<SkillAdapter.ViewHolder> implements View.OnClickListener {

    private final LayoutInflater mLayoutInflater;
    private SkillAdapter.OnItemClickListener onItemClickListener;
    /**
     * Cache of the children views for a skill list item.
     */


    public SkillAdapter(Context context) {
        super(context);
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public SkillAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType)
    {

        final View view = this.mLayoutInflater.inflate(R.layout.list_item_skills, parent, false);
        view.setOnClickListener(this);

        return new SkillAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final SkillAdapter.ViewHolder holder, Cursor cursor) {
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
                this.onItemClickListener.onItemClicked(cursor);
            }
        }
    }

    public void setOnItemClickListener(final SkillAdapter.OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener
    {
        void onItemClicked(Cursor cursor);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView nameView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.item_activity_textview_name);
        }
        public void bindData(final Cursor cursor)
        {
            this.nameView.setText(cursor.getString(FragmentSkills.COL_USER_ACTIVITIES_NAME));
        }
    }
}