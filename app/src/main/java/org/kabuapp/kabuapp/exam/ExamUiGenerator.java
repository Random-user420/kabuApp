package org.kabuapp.kabuapp.exam;

import static org.kabuapp.kabuapp.ui.ThemeColorResolver.resolveColorAttribute;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import org.kabuapp.kabuapp.R;
import org.kabuapp.kabuapp.data.memory.MemExam;
import org.kabuapp.kabuapp.utils.DateTimeUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ExamUiGenerator {
    private final DateTimeFormatter dateTimeFormatter;

    public void addExamElement(
            Context context,
            ViewGroup parentLayout,
            MemExam exam) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (exam.getDuration() == -1) {
            View dividerView = inflater.inflate(R.layout.layout_current_item, parentLayout, false);
            parentLayout.addView(dividerView);
        }
        else {
            View examView = inflater.inflate(R.layout.layout_exam_item, parentLayout, false);

            TextView beginTextView = examView.findViewById(R.id.text_view_exam_begin);
            TextView endTextView = examView.findViewById(R.id.text_view_exam_end);
            TextView infoTextView = examView.findViewById(R.id.text_view_exam_info);

            if (exam.getDuration() == 1) {
                endTextView.setVisibility(View.GONE);
            }
            {
                endTextView.setText(dateTimeFormatter.format(exam.getBeginn().plusDays(exam.getDuration() - 1)));
            }
            beginTextView.setText(dateTimeFormatter.format(exam.getBeginn()));
            infoTextView.setText(exam.getInfo());

            if (isCurrent(exam.getBeginn(), exam.getDuration())) {
                ((CardView) examView).setCardBackgroundColor(resolveColorAttribute(context, android.R.attr.textColorHighlight));
            }

            parentLayout.addView(examView);
        }
    }

    private boolean isCurrent(LocalDate begin, int duration) {
        return (DateTimeUtils.getLocalDate().isAfter(begin) || DateTimeUtils.getLocalDate().isEqual(begin)) &&
                (DateTimeUtils.getLocalDate().isBefore(begin.plusDays(duration - 1)) || DateTimeUtils.getLocalDate().isEqual(begin.plusDays(duration - 1)));
    }
}
