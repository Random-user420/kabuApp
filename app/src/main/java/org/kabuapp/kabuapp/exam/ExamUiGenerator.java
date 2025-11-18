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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
public class ExamUiGenerator
{
    private final DateTimeFormatter dateTimeFormatter;

    public void addExamElement(
            Context context,
            ViewGroup parentLayout,
            MemExam exam)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View examView = inflater.inflate(R.layout.layout_exam_item, parentLayout, false);

        TextView beginTextView = examView.findViewById(R.id.text_view_exam_begin);
        TextView endTextView = examView.findViewById(R.id.text_view_exam_end);
        TextView infoTextView = examView.findViewById(R.id.text_view_exam_info);

        if (exam.getDuration() == 1)
        {
            endTextView.setVisibility(View.GONE);
        }
        {
            endTextView.setText(dateTimeFormatter.format(exam.getBeginn().plusDays(exam.getDuration() - 1)));
        }
        beginTextView.setText(dateTimeFormatter.format(exam.getBeginn()));
        infoTextView.setText(exam.getInfo());

        if (isCurrent(exam.getBeginn(), exam.getDuration()))
        {
            ((CardView) examView).setCardBackgroundColor(resolveColorAttribute(context, android.R.attr.textColorSecondaryInverse));
        }

        parentLayout.addView(examView);
    }

    private boolean isCurrent(LocalDate begin, int duration)
    {
        return LocalDate.now().isAfter(begin) && LocalDate.now().isBefore(begin.plusDays(duration));
    }
}
