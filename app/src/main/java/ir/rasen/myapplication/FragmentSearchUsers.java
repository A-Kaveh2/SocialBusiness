package ir.rasen.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ir.rasen.myapplication.adapters.HistorySimpleCursorAdapter;
import ir.rasen.myapplication.helper.HistoryDatabase;
import ir.rasen.myapplication.helper.InnerFragment;
import ir.rasen.myapplication.helper.Location_M;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.ui.AutoCompleteTextViewFontClickable;
import ir.rasen.myapplication.ui.DrawableClickListener;
import ir.rasen.myapplication.ui.TextViewFont;

public class FragmentSearchUsers extends Fragment {

    private AutoCompleteTextViewFontClickable text;
    private View view;

    private HistoryDatabase database;

    // TODO: Rename and change types of parameters
    public static FragmentSearchUsers newInstance (){
        FragmentSearchUsers fragment = new FragmentSearchUsers();
        return fragment;
    }

    public FragmentSearchUsers() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_user, container, false);

        text = (AutoCompleteTextViewFontClickable) view.findViewById(R.id.txt_search_text);

        // on click listeners
        onClickListeners();

        // set history
        setUpHistory();
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                setUpHistory();
            }
        });


        return view;
    }

    void searchNow() {
        // Start searching...
        text.setText(text.getText().toString().trim());
        if (!text.getText().toString().trim().matches(Params.USER_USERNAME_VALIDATION) || text.getText().toString().trim().length() < Params.USER_USERNAME_MIN_LENGTH) {
            text.requestFocus();
            text.setErrorC(getString(R.string.enter_valid_username));
            return;
        }
        if(text.getText().toString().trim().length()>0) {
            database = new HistoryDatabase(getActivity(), HistoryDatabase.TABLE_HISTORY_USERS);
            database.insert(text.getText().toString().trim());
            setUpHistory();
            InnerFragment innerFragment = new InnerFragment(getActivity());
            innerFragment.newResultsUsersFragment(text.getText().toString());
        } else {
            text.setErrorC(getString(R.string.enter_search_keywords));
        }
    }

    void onClickListeners() {
        text.setDrawableClickListener(new DrawableClickListener() {
            public void onClick(DrawablePosition target) {
                switch (target) {
                    case LEFT:
                        searchNow();
                        break;
                    default:
                        break;
                }
            }

        });
        text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(view.isFocused()) {
                    //
                } else {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
                }
            }
        });
        text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchNow();
                    return true;
                }
                return false;
            }
        });

    }

    // set recents
    void setUpHistory() {
        database = new HistoryDatabase(getActivity(), HistoryDatabase.TABLE_HISTORY_USERS);
        Cursor cursor = database.getHistory(text.getText().toString());
        String[] columns = new String[]{HistoryDatabase.FIELD_HISTORY};
        int[] columnTextId = new int[]{android.R.id.text1};
        HistorySimpleCursorAdapter simple = new HistorySimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_1, cursor,
                columns, columnTextId
                , 0);
        ((ListView) view.findViewById(R.id.list_search_suggests)).setAdapter(simple);
        ((ListView) view.findViewById(R.id.list_search_suggests)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                text.setText(((TextView) view).getText().toString());
                searchNow();
            }
        });
    }

}
