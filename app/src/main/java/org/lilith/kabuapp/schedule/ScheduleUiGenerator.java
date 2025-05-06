package org.lilith.kabuapp.schedule;

import static org.lilith.kabuapp.ui.ThemeColorResolver.resolveColorAttribute;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import org.lilith.kabuapp.R;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ScheduleUiGenerator
{
    public String mapBeginnToString(short lesson)
    {
        return switch (lesson)
        {
            case 1 -> "8:30";
            case 2 -> "9:15";
            case 3 -> "10:00";
            case 4 -> "11:00";
            case 5 -> "11:45";
            case 6 -> "12:30";
            case 7 -> "13:15";
            case 8 -> "14:00";
            case 9 -> "14:45";
            case 10 -> "15:30";
            case 11 -> "16:15";
            default -> "ERROR";
        };
    }

    @SuppressLint("SetTextI18n")
    public void addSingleLessonElement(
            Context context,
            ViewGroup parentLayout,
            String beginTime,
            String endTime,
            String room,
            String teacher,
            String name)
    {

        LayoutInflater inflater = LayoutInflater.from(context);
        View lessonView = inflater.inflate(R.layout.layout_lesson_item, parentLayout, false);

        TextView timeTextView = lessonView.findViewById(R.id.text_view_lesson_time);
        TextView nameTextView = lessonView.findViewById(R.id.text_view_lesson_name);
        TextView roomTextView = lessonView.findViewById(R.id.text_view_lesson_room);
        TextView teacherTextView = lessonView.findViewById(R.id.text_view_lesson_teacher);

        if (teacher.isEmpty())
        {
            nameTextView.setText(Html.fromHtml("<s>" + name + "</s>"));
            timeTextView.setText(Html.fromHtml("<s>" + beginTime + " - " + endTime + "</s>"));
            teacherTextView.getParent().clearChildFocus(teacherTextView);
            roomTextView.setVisibility(View.GONE);
            teacherTextView.setVisibility(View.GONE);
        }
        else
        {
            nameTextView.setText(name);
            timeTextView.setText(beginTime + " - " + endTime);
            teacherTextView.setText(context.getString(R.string.lesson_teacher_prefix) + ": " + teacher);
            roomTextView.setText(context.getString(R.string.lesson_room_prefix) + ": " + room);
        }

        if (isCurrent(toLocaleDate(beginTime), toLocaleDate(endTime)))
        {
            ((CardView) lessonView).setCardBackgroundColor(resolveColorAttribute(context, android.R.attr.textColorSecondaryInverse));
        }

        parentLayout.addView(lessonView);
    }

    @SuppressLint("SetTextI18n")
    public void addDoubleLessonElement(
            Context context,
            ViewGroup parentLayout,
            String beginTime,
            String endTime,
            String room,
            String room2,
            String teacher,
            String teacher2,
            String name,
            String name2)
    {

        LayoutInflater inflater = LayoutInflater.from(context);
        View lessonView = inflater.inflate(R.layout.layout_lesson_double_item, parentLayout, false);

        TextView timeTextView = lessonView.findViewById(R.id.text_view_lesson_time);
        TextView nameTextView = lessonView.findViewById(R.id.text_view_lesson_name);
        TextView name2TextView = lessonView.findViewById(R.id.text_view_lesson_name2);
        TextView roomTextView = lessonView.findViewById(R.id.text_view_lesson_room);
        TextView room2TextView = lessonView.findViewById(R.id.text_view_lesson_room2);
        TextView teacherTextView = lessonView.findViewById(R.id.text_view_lesson_teacher);
        TextView teacher2TextView = lessonView.findViewById(R.id.text_view_lesson_teacher2);

        if (teacher.isEmpty() && teacher2.isEmpty())
        {
            timeTextView.setText(Html.fromHtml("<s>" + beginTime + " - " + endTime + "</s>"));
        }
        else
        {
            timeTextView.setText(beginTime + " - " + endTime);
        }
        if (teacher.isEmpty())
        {
            nameTextView.setText(Html.fromHtml("<s>" + name + "</s>"));
            roomTextView.setVisibility(View.GONE);
            teacherTextView.setVisibility(View.GONE);
        }
        else
        {
            nameTextView.setText(name + "  ");
            roomTextView.setText(context.getString(R.string.lesson_room_prefix) + ": " + room);
            teacherTextView.setText(context.getString(R.string.lesson_teacher_prefix) + ": " + teacher);
        }
        if (teacher2.isEmpty())
        {
            name2TextView.setText(Html.fromHtml("<s>" + name + "</s>"));
            room2TextView.setVisibility(View.GONE);
            teacher2TextView.setVisibility(View.GONE);
        }
        else
        {
            name2TextView.setText(name2);
            room2TextView.setText(context.getString(R.string.lesson_room_prefix) + ": " + room2);
            teacher2TextView.setText(context.getString(R.string.lesson_teacher_prefix) + ": " + teacher2);
        }

        if (isCurrent(toLocaleDate(beginTime), toLocaleDate(endTime)))
        {
            ((CardView) lessonView).setCardBackgroundColor(resolveColorAttribute(context, android.R.attr.textColorSecondaryInverse));
        }

        parentLayout.addView(lessonView);
    }

    private boolean isCurrent(LocalTime begin, LocalTime end)
    {
        return LocalTime.now().isBefore(end) && LocalTime.now().isAfter(begin);
    }

    private LocalTime toLocaleDate(String date)
    {
        if (date.length() == 4)
        {
            date = "0" + date;
        }
        return LocalTime.parse(date, DateTimeFormatter.ofPattern("HH:mm"));
    }
}
