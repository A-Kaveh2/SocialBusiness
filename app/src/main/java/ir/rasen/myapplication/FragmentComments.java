package ir.rasen.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rasen.myapplication.adapters.CommentsAdapter;
import ir.rasen.myapplication.classes.Comment;
import ir.rasen.myapplication.ui.EditTextFont;

/**
 * Created by 'Sina KH'.
 */
public class FragmentComments extends Fragment {
    private static final String TAG = "FragmentComments";

    private View view, listFooterView;

    private boolean isLoadingMore=false;
    private SwipeRefreshLayout swipeView;
    private ListAdapter mAdapter;

    private ListView list;

    public static FragmentComments newInstance (){
        FragmentComments fragment = new FragmentComments();
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentComments() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_comments, container, false);

        list = (ListView) view.findViewById(R.id.list_comments_comments);
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);

        // setUp ListView
        setUpListView();

        // TODO: Change Adapter to display your content
        ArrayList<Comment> comments = new ArrayList<Comment>();

        /*
            for example, i've made some fake data to show ::
        */
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        Comment comment3 = new Comment();
        comment1.userID="Sina";
        comment1.text=("سلام!!");
        comment2.userID="Hossein";
        comment2.text=("چطوری @سینا؟");
        comment3.text="SINA";
        comment3.text=("فدایت عزیز");
        comments.add(comment1);
        comments.add(comment2);
        comments.add(comment3);
        mAdapter = new CommentsAdapter(getActivity(), comments);
        ((AdapterView<ListAdapter>) view.findViewById(R.id.list_comments_comments)).setAdapter(mAdapter);
        view.findViewById(R.id.btn_comments_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendComment(view);
            }
        });

        ((EditTextFont) view.findViewById(R.id.txt_comments_comment)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean b) {
                if(!view.isFocused()) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(((EditTextFont) view.findViewById(R.id.txt_comments_comment)).getWindowToken(), 0);
                }
            }
        });
        return view;
    }

    public void sendComment(View view) {
        // TODO: SEND COMMENT HERE
        String commentText = ((EditTextFont) view.findViewById(R.id.txt_comments_comment)).getText().toString();
    }

    // TODO: LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        listFooterView.setVisibility(View.VISIBLE);
    }

    void setUpListView() {
        // SwipeRefreshLayout
        swipeView.setColorScheme(R.color.button_on_dark, R.color.red, R.color.green);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);
                // TODO: CANCEL LOADING MORE AND REFRESH HERE...
                listFooterView.setVisibility(View.INVISIBLE);
                isLoadingMore=false;
            }
        });
        listFooterView = ((LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_loading_more, null, false);
        listFooterView.setVisibility(View.INVISIBLE);
        list.addFooterView(listFooterView);
        // TODO: ListView LoadMore
        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            int currentFirstVisibleItem
                    ,
                    currentVisibleItemCount
                    ,
                    currentScrollState;

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;
            }

            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.currentScrollState = scrollState;
                this.isScrollCompleted();
            }

            private void isScrollCompleted() {
                if (this.currentVisibleItemCount > 0 && this.currentScrollState == SCROLL_STATE_IDLE) {
                    /*** In this way I detect if there's been a scroll which has completed ***/
                    /*** do the work for load more date! ***/
                    if (!swipeView.isRefreshing() && !isLoadingMore) {
                        isLoadingMore = true;
                        loadMoreData();
                    }
                }
            }
        });
    }
}
