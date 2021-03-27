package com.english.contacts.ui.view.indexbar;

import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.english.contacts.R;

public class IndexLetterView extends AppCompatTextView {
    private final IndexBarUnit unit;

    IndexLetterView(Context context, IndexBarUnit unit) {
        super(context);
        this.unit = unit;
        init();
    }

    private void init() {
        setText(unit.getDisplayValue());
        setTextColor(ContextCompat.getColor(getContext(), R.color.index_letter_color));
        int padding = getResources().getDimensionPixelSize(R.dimen.index_letter_padding);
        setPadding(padding, 0, padding, padding / 2);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        setLayoutParams(layoutParams);
    }

    String getLetter(MotionEvent event) {
        return unit.getPartialLetter(event.getY() - getTop(), getHeight());
    }

    boolean isHitting(MotionEvent event) {
        return getLeft() <= event.getX() && getRight() >= event.getX() &&
                getTop() <= event.getY() && getBottom() >= event.getY();
    }
}
