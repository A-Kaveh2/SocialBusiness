package ir.rasen.myapplication.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;

import ir.rasen.myapplication.helper.HistoryDatabase;

/**
 * Created by 'Sina KH'.
 */

// SuggestionSimpleCursorAdapter.java
public class HistorySimpleCursorAdapter
        extends SimpleCursorAdapter
{

    public HistorySimpleCursorAdapter(Context context, int layout, Cursor c,
                                      String[] from, int[] to) {
        super(context, layout, c, from, to);
    }

    public HistorySimpleCursorAdapter(Context context, int layout, Cursor c,
                                      String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public CharSequence convertToString(Cursor cursor) {

        int indexColumnSuggestion = cursor.getColumnIndex(HistoryDatabase.FIELD_HISTORY);

        return cursor.getString(indexColumnSuggestion);
    }


}