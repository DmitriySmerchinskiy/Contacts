package com.english.contacts.ui.view.indexbar;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.english.contacts.R;
import com.english.contacts.data.IndexHeader;

import java.util.List;

public class IndexBarView extends RelativeLayout {

    private static final int INDEX_BAR_VIEW_MIN_UNITS_COUNT = 3;

    private LinearLayout contentView;
    private OnLetterClickListener listener;
    private List<IndexHeader> indexHeaders;
    private boolean isCreated = false;
    private int heightWas = 0;

    public IndexBarView(Context context) {
        super(context);
        init();
    }

    public IndexBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IndexBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public IndexBarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setBackgroundResource(android.R.color.white);
        setClickable(true);

        contentView = new LinearLayout(getContext());
        contentView.setOrientation(LinearLayout.VERTICAL);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        contentView.setLayoutParams(layoutParams);

        contentView.setOnTouchListener((v, event) -> {
            for (int i = 0; i < contentView.getChildCount(); ++i) {
                IndexLetterView child = (IndexLetterView) contentView.getChildAt(i);
                if (child.isHitting(event)) {
                    listener.onLetterClicked(child.getLetter(event));
                    return true;
                }
            }

            return false;
        });

        addView(contentView);
    }

    public void setIndexHeaders(List<IndexHeader> indexHeaders) {
        if (this.indexHeaders == null || !this.indexHeaders.equals(indexHeaders)) {
            isCreated = false;
        }
        this.indexHeaders = indexHeaders;
        createIndexLetterViews();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && heightWas != getHeight()) {
            isCreated = false;
            heightWas = getHeight();
        }
        createIndexLetterViews();
    }

    private void createIndexLetterViews() {
        if (getHeight() != 0 && !isCreated && indexHeaders != null) {
            contentView.removeAllViews();

            int letterMinHeight = getResources().getDimensionPixelSize(R.dimen.letter_min_height);
            int availableHeight =
                    Math.max(getHeight() - getResources().getDimensionPixelSize(R.dimen.index_bar_view_padding),
                            letterMinHeight * INDEX_BAR_VIEW_MIN_UNITS_COUNT);
            List<IndexBarUnit> indexBarUnits = IndexBarUnitsCreator.create(
                    availableHeight, letterMinHeight, indexHeaders);
            for (IndexBarUnit indexBarUnit : indexBarUnits) {
                contentView.addView(new IndexLetterView(getContext(), indexBarUnit));
            }

            isCreated = true;
        }
    }

    public void setOnLetterClickListener(OnLetterClickListener listener) {
        this.listener = listener;
    }

    public interface OnLetterClickListener {
        void onLetterClicked(String letter);
    }
}
