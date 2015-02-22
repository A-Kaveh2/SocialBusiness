package ir.rasen.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rasen.myapplication.adapters.CommentsAdapter;
import ir.rasen.myapplication.adapters.FollowersAdapter;
import ir.rasen.myapplication.classes.Business;
import ir.rasen.myapplication.classes.Comment;
import ir.rasen.myapplication.classes.User;
import ir.rasen.myapplication.helper.Dialogs;
import ir.rasen.myapplication.helper.EditInterface;
import ir.rasen.myapplication.helper.LoginInfo;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.ResultStatus;
import ir.rasen.myapplication.helper.SearchItemUserBusiness;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.TextProcessor;
import ir.rasen.myapplication.ui.EditTextFont;
import ir.rasen.myapplication.ui.ProgressDialogCustom;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.business.GetBusinessFollowers;
import ir.rasen.myapplication.webservice.comment.GetPostAllComments;
import ir.rasen.myapplication.webservice.comment.SendComment;
import ir.rasen.myapplication.webservice.user.GetFollowingBusinesses;

/**
 * Created by 'Sina KH'.
 */
public class FragmentComments extends Fragment implements WebserviceResponse, EditInterface {
    private static final String TAG = "FragmentComments";

    private View view, listFooterView;

    private boolean isLoadingMore = false;
    private SwipeRefreshLayout swipeView;
    private BaseAdapter mAdapter;

    private ListView list;

    private int postId;

    ArrayList<Comment> comments;

    private ProgressDialogCustom pd;
    private boolean sendingComment = false;

    public static FragmentComments newInstance(int postId) {
        FragmentComments fragment = new FragmentComments();

        Bundle bundle = new Bundle();
        bundle.putInt(Params.POST_ID, postId);
        fragment.setArguments(bundle);

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

        Bundle bundle = getArguments();
        if (bundle != null) {
            postId = bundle.getInt(Params.POST_ID);


            pd = new ProgressDialogCustom(getActivity());
            pd.show();

            new GetPostAllComments(postId, 0, getResources().getInteger(R.integer.lazy_load_limitation),FragmentComments.this).execute();

        } else {
            Log.e(TAG, "bundle is null!!");
            getActivity().finish();
            getActivity().overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_comments, container, false);

        list = (ListView) view.findViewById(R.id.list_comments_comments);
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);

        // setUp ListView
        setUpListView();

        comments = new ArrayList<Comment>();


        mAdapter = new CommentsAdapter(getActivity(), comments, FragmentComments.this, FragmentComments.this, pd);
        ((AdapterView<ListAdapter>) view.findViewById(R.id.list_comments_comments)).setAdapter(mAdapter);
        view.findViewById(R.id.btn_comments_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendComment(view);
            }
        });

        ((EditTextFont) view.findViewById(R.id.edt_comments_comment)).addTextChangedListener(new TextWatcher() {
            String oldText;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                oldText = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence.toString().equals(oldText))
                    return;
                TextProcessor textProcessor = new TextProcessor(getActivity());
                textProcessor.processEdt(((EditTextFont) view.findViewById(R.id.edt_comments_comment)).getText().toString(), ((EditTextFont) view.findViewById(R.id.edt_comments_comment)));
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        ((EditTextFont) view.findViewById(R.id.edt_comments_comment)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean b) {
                if (!view.isFocused()) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(((EditTextFont) view.findViewById(R.id.edt_comments_comment)).getWindowToken(), 0);
                }
            }
        });
        return view;
    }

    public void sendComment(View v) {
        EditTextFont commentText = (EditTextFont) view.findViewById(R.id.edt_comments_comment);
        if (commentText.getText().toString().length() < Params.COMMENT_TEXT_MIN_LENGTH) {
            commentText.setErrorC(getString(R.string.comment_is_too_short));
            return;
        }
        if (commentText.getText().toString().length() > Params.COMMENT_TEXT_MAX_LENGTH) {
            commentText.setErrorC(getString(R.string.enter_is_too_long));
            return;
        }

        new SendComment(LoginInfo.getUserId(getActivity()),
                postId,
                commentText.getText().toString(),
                FragmentComments.this).execute();
        sendingComment=true;
        pd.show();
    }

    // LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        isLoadingMore = true;
        listFooterView.setVisibility(View.VISIBLE);

        new GetPostAllComments(postId, comments.get(comments.size()-1).id, getResources().getInteger(R.integer.lazy_load_limitation),FragmentComments.this).execute();

    }

    void setUpListView() {
        // SwipeRefreshLayout
        swipeView.setColorScheme(R.color.button_on_dark, R.color.red, R.color.green);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isLoadingMore) {
                    swipeView.setRefreshing(false);
                    return;
                }
                comments = new ArrayList<Comment>();
                // get comments again
                reload();
            }
        });
        listFooterView = ((LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_loading_more, null, false);
        listFooterView.setVisibility(View.GONE);
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
                    if (!swipeView.isRefreshing() && !isLoadingMore
                            && comments.size()>0 && comments.size()%getResources().getInteger(R.integer.lazy_load_limitation)==0) {
                        loadMoreData();
                    }
                }
            }
        });
    }

    private void reload() {
        comments.clear();
        loadMoreData();
        swipeView.setRefreshing(true);
    }
    @Override
    public void getResult(Object result) {
        try {
            pd.dismiss();
            if (result instanceof ResultStatus) {
                if(sendingComment) {
                    EditTextFont commentText = (EditTextFont) view.findViewById(R.id.edt_comments_comment);
                    commentText.setText("");
                    reload();
                    pd.show();
                    return;
                }
                int editingPosition = -1;
                for (int i = 0; i < comments.size(); i++) {
                    if (comments.get(i).id == editingId) {
                        editingPosition = i;
                        break;
                    }
                }
                if (editingText == null) {
                    comments.remove(editingPosition);
                    mAdapter.notifyDataSetChanged();
                } else {
                    comments.get(editingPosition).text = editingText;
                    mAdapter.notifyDataSetChanged();
                }
            }

            if (result instanceof ArrayList) {
                //result from executing getBusinessFollowers
                ArrayList<Comment> postComments = new ArrayList<>();
                postComments.addAll(comments);
                postComments.addAll((ArrayList<Comment>) result);
                comments.clear();
                comments.addAll(postComments);
                mAdapter.notifyDataSetChanged();
                isLoadingMore = false;
                swipeView.setRefreshing(false);
                listFooterView.setVisibility(View.GONE);

            }
        } catch (Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }
        sendingComment = false;
    }

    @Override
    public void getError(Integer errorCode) {
        sendingComment = false;
        try {
            pd.dismiss();
            String errorMessage = ServerAnswer.getError(getActivity(), errorCode);
            Dialogs.showMessage(getActivity(), errorMessage, comments.size()==0 ? true : false);
        } catch(Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }
    }

    private int editingId;
    private String editingText;
    private Dialog editingDialog;
    @Override
    public void setEditing(int id, String text, Dialog dialog) {
        editingId = id;
        editingText = text;
        editingDialog = dialog;
    }
}
