package ir.rasen.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
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

import java.lang.reflect.Array;
import java.util.ArrayList;

import ir.rasen.myapplication.adapters.HistorySimpleCursorAdapter;
import ir.rasen.myapplication.helper.Functions;
import ir.rasen.myapplication.helper.InnerFragment;
import ir.rasen.myapplication.helper.Location_M;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.HistoryDatabase;
import ir.rasen.myapplication.ui.AutoCompleteTextViewFontClickable;
import ir.rasen.myapplication.ui.DrawableClickListener;
import ir.rasen.myapplication.ui.TextViewFont;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.business.GetBusinessGategories;
import ir.rasen.myapplication.webservice.business.GetBusinessSubcategories;

public class FragmentSearch extends Fragment implements WebserviceResponse {

    private int searchType = Params.SearchType.BUSINESSES;
    private AutoCompleteTextViewFontClickable text;
    private String category;
    private boolean nearby = true;
    private View view;
    private TextViewFont btnBusinesses, btnUsers;
    private ArrayList<String> subCategories;
    private DrawerLayout drawerLayout;
    private Location_M locationM;
    private LocationManager mLocationManager;

    private HistoryDatabase database;
    private ArrayList<String> categories;

    // TODO: Rename and change types of parameters
    public static FragmentSearch newInstance() {
        FragmentSearch fragment = new FragmentSearch();
        return fragment;
    }

    public FragmentSearch() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new GetBusinessGategories(FragmentSearch.this).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);

        text = (AutoCompleteTextViewFontClickable) view.findViewById(R.id.txt_search_text);
        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_search_filters);
        btnBusinesses = (TextViewFont) view.findViewById(R.id.btn_search_businesses);
        btnUsers = (TextViewFont) view.findViewById(R.id.btn_search_users);

        // on click listeners
        onClickListeners();

        // setup categories filter ( drawer layout )
        setUpCategoriesFitler();

        // setup suggestions
        setUpSuggestions();
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

        mLocationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Params.LOCATION_REFRESH,
                Params.LOCATION_REFRESH_DISTANCE, mLocationListener);

        return view;
    }

    void switchDrawer() {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT))
            drawerLayout.closeDrawer(Gravity.RIGHT);
        else {
            drawerLayout.openDrawer(Gravity.RIGHT);
        }
    }

    private void searchNow() {
        // TODO:: Start search fragment
        if (text.getText().toString().trim().length() > 0) {
            if(locationM==null) {
                setLocation();
                Functions.showMessage(getActivity(), getString(R.string.no_location_found));
                return;
            }
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
            database = new HistoryDatabase(getActivity(), searchType == Params.SearchType.BUSINESSES ? HistoryDatabase.TABLE_HISTORY_BUSINESS : HistoryDatabase.TABLE_HISTORY_PRODUCTS);
            database.insert(text.getText().toString().trim());
            setUpHistory();
            InnerFragment innerFragment = new InnerFragment(getActivity());
            innerFragment.newResultsFragment("", "", false, new Location_M("", ""), searchType);
        } else {
            text.setError(getString(R.string.enter_search_keywords));
        }
    }

    void setUpCategoriesFitler() {

        final ArrayList<String> categories = new ArrayList<>();
        final ListView listViewSubCategories = (ListView) view.findViewById(R.id.list_search_subcategories);
        final ListView listViewCategories = (ListView) view.findViewById(R.id.list_search_categories);
        // Categories on item click listener
        listViewCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {
                ((TextViewFont) view.findViewById(R.id.txt_search_category)).setText(categories.get(i) + ":");
                view.findViewById(R.id.rl_search_subcategories).setVisibility(View.VISIBLE);

                //TODO where is "position" field? Is it "i"?
                new GetBusinessSubcategories(adapterView.getItemAtPosition(i).toString()
                        , FragmentSearch.this).execute();


                //TODO:remove this part
                subCategories = new ArrayList<String>();
                subCategories.add("تلویزیون");
                subCategories.add("یخچال");
                ArrayAdapter<String> subcategoriesAdapter =
                        new ArrayAdapter<String>(getActivity(), R.layout.layout_item_text, subCategories);
                listViewSubCategories.setAdapter(subcategoriesAdapter);
            }
        });
        // sub categories on item click listener
        listViewSubCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {
                switchDrawer();
                view.findViewById(R.id.rl_search_subcategories).setVisibility(View.INVISIBLE);
                category = subCategories.get(i);
                ((TextViewFont) view.findViewById(R.id.txt_search_filter)).setText(getString(R.string.filter) + " " + subCategories.get(i));
            }
        });
        // back on click listener
        view.findViewById(R.id.btn_search_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.rl_search_subcategories).setVisibility(View.INVISIBLE);
            }
        });

        // TODO:: AFTER GETTING CATEGORIES FROM INTERNET:: {{{
        // set categories adapter
        categories.add("لوازم الکترونیکی");
        categories.add("فست فود");
        ArrayAdapter<String> categoriesAdapter =
                new ArrayAdapter<String>(getActivity(), R.layout.layout_item_text, categories);
        listViewCategories.setAdapter(categoriesAdapter);
        // TODO:: }}}
    }

    void setUpSuggestions() {
        final ArrayList<String> suggestions = new ArrayList<>();
        suggestions.add("موبایل");
        suggestions.add("تبلت");
        suggestions.add("رستوران ها");
        ArrayAdapter<String> suggestionsAdapter =
                new ArrayAdapter<String>(getActivity(), R.layout.layout_item_text, suggestions);
        ((ListView) view.findViewById(R.id.list_search_suggests)).setAdapter(suggestionsAdapter);
    }

    // open set location dialog
    public void setLocation() {
        Intent intent = new Intent(getActivity(), ActivityLocation.class);
        intent.putExtra(Params.SET_LOCATION_TYPE, Params.SEARCH);
        if (locationM != null) {
            intent.putExtra(Params.LOCATION_LATITUDE, locationM.getLatitude());
            intent.putExtra(Params.LOCATION_LONGITUDE, locationM.getLongitude());
        }
        startActivityForResult(intent, Params.INTENT_LOCATION);
        getActivity().overridePendingTransition(R.anim.to_0, R.anim.to_left);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Params.INTENT_LOCATION && resultCode == Params.INTENT_OK) {
            locationM = new Location_M(data.getStringExtra(Params.LOCATION_LATITUDE), data.getStringExtra(Params.LOCATION_LONGITUDE));
            ((TextViewFont) view.findViewById(R.id.txt_search_title)).setText(R.string.selected_location);
            nearby = false;
        }

    }

    void onClickListeners() {
        btnBusinesses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnUsers.setBackgroundResource(R.drawable.selected_tab);
                btnBusinesses.setBackgroundResource(android.R.color.transparent);
                text.setHint(R.string.search_businesses);
                searchType = Params.SearchType.BUSINESSES;
                setUpHistory();
                // TODO:: BUSINESSES SELECTED
            }
        });
        btnUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnUsers.setBackgroundResource(android.R.color.transparent);
                btnBusinesses.setBackgroundResource(R.drawable.selected_tab);
                text.setHint(R.string.search_products);
                searchType = Params.SearchType.PRODUCTS;
                setUpHistory();
                // TODO:: USERS SELECTED
            }
        });
        view.findViewById(R.id.txt_search_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchDrawer();
            }
        });
        text.setDrawableClickListener(new DrawableClickListener() {
            public void onClick(DrawablePosition target) {
                switch (target) {
                    case LEFT:
                        searchNow();
                        break;
                    case RIGHT:
                        switchDrawer();
                        break;
                    default:
                        break;
                }
            }

        });
        text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (view.isFocused()) {
                    if (drawerLayout.isDrawerOpen(Gravity.RIGHT))
                        drawerLayout.closeDrawer(Gravity.RIGHT);
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

        // set location
        view.findViewById(R.id.rl_search_actionbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocation();
            }
        });
    }

    // set recents
    void setUpHistory() {
        database = new HistoryDatabase(getActivity(), searchType == Params.SearchType.BUSINESSES ? HistoryDatabase.TABLE_HISTORY_BUSINESS : HistoryDatabase.TABLE_HISTORY_PRODUCTS);
        Cursor cursor = database.getHistory(text.getText().toString());
        String[] columns = new String[]{HistoryDatabase.FIELD_HISTORY};
        int[] columnTextId = new int[]{android.R.id.text1};
        HistorySimpleCursorAdapter simple = new HistorySimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_1, cursor,
                columns, columnTextId
                , 0);
        text.setAdapter(simple);
    }

    @Override
    public void getResult(Object result) {
        if (result instanceof ArrayList) {

            if (categories == null) {
                //result from executing getBusinessCategories
                categories = new ArrayList<String>();
                categories = (ArrayList<String>) result;

                //TODO assign categories to the spinner

            }
            else {
                //result from executing getBusinessSubcategories
                ArrayList<String> businessSubcategories = new ArrayList<String>();
                businessSubcategories = (ArrayList<String>)result;

                //TODO assign businessSubcategories to the spinner
            }
        }
    }

    @Override
    public void getError(Integer errorCode) {

    }

    // location listener
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //location changed
            if(nearby)
                locationM = new Location_M(location.getLatitude()+"", location.getLongitude()+"");
        }
        @Override
        public void onStatusChanged(String string, int i, Bundle bundle) {
        }
        @Override
        public void onProviderEnabled(String string) {
        }
        @Override
        public void onProviderDisabled(String string) {
        }
    };
}
