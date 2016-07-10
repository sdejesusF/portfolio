package sdejesus.portfolio;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;

/**
 * Created by sdejesus on 7/9/16.
 */

public abstract class RecyclerViewCursorAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH>
{
    private Cursor cursor;
    private Context context;

    public RecyclerViewCursorAdapter(Context ctx) {
        context = ctx;
    }

    public void swapCursor(final Cursor cursor)
    {
        this.cursor = cursor;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount()
    {
        return this.cursor != null
                ? this.cursor.getCount()
                : 0;
    }

    public Cursor getItem(final int position)
    {
        if (this.cursor != null && !this.cursor.isClosed())
        {
            this.cursor.moveToPosition(position);
        }

        return this.cursor;
    }

    public Cursor getCursor()
    {
        return this.cursor;
    }

    @Override
    public final void onBindViewHolder(final VH holder, final int position)
    {
        final Cursor cursor = this.getItem(position);
        this.onBindViewHolder(holder, cursor);
    }
    public Context getContext(){
        return context;
    }
    public abstract void onBindViewHolder(final VH holder, final Cursor cursor);
}