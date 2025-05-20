package org.kabuapp.kabuapp.schedule;

import static org.kabuapp.kabuapp.ui.ThemeColorResolver.resolveColorAttribute;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import org.kabuapp.kabuapp.R;
import org.kabuapp.kabuapp.data.memory.MemLesson;

import java.time.LocalTime;

public class ScheduleUiGenerator
{

    @SuppressLint("SetTextI18n")
    public void addSingleLessonElement(
            Context context,
            ViewGroup parentLayout,
            MemLesson lesson)
    {

        LayoutInflater inflater = LayoutInflater.from(context);
        View lessonView = inflater.inflate(R.layout.layout_lesson_item, parentLayout, false);

        TextView timeTextView = lessonView.findViewById(R.id.text_view_lesson_time);
        TextView nameTextView = lessonView.findViewById(R.id.text_view_lesson_name);
        TextView roomTextView = lessonView.findViewById(R.id.text_view_lesson_room);
        TextView teacherTextView = lessonView.findViewById(R.id.text_view_lesson_teacher);

        if (lesson.getTeacher().isEmpty())
        {
            nameTextView.setText(Html.fromHtml("<s>" + lesson.getName() + "</s>"));
            timeTextView.setText(Html.fromHtml("<s>" + mapBeginnToString(lesson.getBegin()) + " - "
                    + mapBeginnToString((short) (lesson.getEnd() + 1)) + "</s>"));
            teacherTextView.getParent().clearChildFocus(teacherTextView);
            roomTextView.setVisibility(View.GONE);
            teacherTextView.setVisibility(View.GONE);
        }
        else
        {
            nameTextView.setText(lesson.getName());
            timeTextView.setText(mapBeginnToString(lesson.getBegin()) + " - " + mapBeginnToString((short) (lesson.getEnd() + 1)));
            teacherTextView.setText(context.getString(R.string.lesson_teacher_prefix) + ": " + lesson.getTeacher());
            roomTextView.setText(context.getString(R.string.lesson_room_prefix) + ": " + lesson.getRoom());
        }

        if (isCurrent(toLocaleDate(lesson.getBegin()), toLocaleDate((short) (lesson.getEnd() + 1))))
        {
            ((CardView) lessonView).setCardBackgroundColor(resolveColorAttribute(context, android.R.attr.textColorSecondaryInverse));
        }

        parentLayout.addView(lessonView);
    }

    @SuppressLint("SetTextI18n")
    public void addDoubleLessonElement(
            Context context,
            ViewGroup parentLayout,
            MemLesson lesson1,
            MemLesson lesson2)
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

        if (lesson1.getTeacher().isEmpty() && lesson2.getTeacher().isEmpty())
        {
            timeTextView.setText(Html.fromHtml("<s>" + mapBeginnToString(lesson1.getBegin()) + " - "
                    + mapBeginnToString((short) (lesson1.getEnd() + 1)) + "</s>"));
        }
        else
        {
            timeTextView.setText(mapBeginnToString(lesson1.getBegin()) + " - " + mapBeginnToString((short) (lesson1.getEnd() + 1)));
        }
        if (lesson1.getTeacher().isEmpty())
        {
            nameTextView.setText(Html.fromHtml("<s>" + lesson1.getName() + "</s>"));
            roomTextView.setVisibility(View.GONE);
            teacherTextView.setVisibility(View.GONE);
        }
        else
        {
            nameTextView.setText(lesson1.getName() + "  ");
            roomTextView.setText(context.getString(R.string.lesson_room_prefix) + ": " + lesson1.getRoom());
            teacherTextView.setText(context.getString(R.string.lesson_teacher_prefix) + ": " + lesson1.getTeacher());
        }
        if (lesson2.getTeacher().isEmpty())
        {
            name2TextView.setText(Html.fromHtml("<s>" + lesson1.getName() + "</s>"));
            room2TextView.setVisibility(View.GONE);
            teacher2TextView.setVisibility(View.GONE);
        }
        else
        {
            name2TextView.setText(lesson2.getName());
            room2TextView.setText(context.getString(R.string.lesson_room_prefix) + ": " + lesson2.getRoom());
            teacher2TextView.setText(context.getString(R.string.lesson_teacher_prefix) + ": " + lesson2.getTeacher());
        }

        if (isCurrent(toLocaleDate(lesson1.getBegin()), toLocaleDate((short) (lesson1.getEnd() + 1))))
        {
            ((CardView) lessonView).setCardBackgroundColor(resolveColorAttribute(context, android.R.attr.textColorSecondaryInverse));
        }

        parentLayout.addView(lessonView);
    }

    private boolean isCurrent(LocalTime begin, LocalTime end)
    {
        return LocalTime.now().isBefore(end) && LocalTime.now().isAfter(begin);
    }

    private LocalTime toLocaleDate(short time)
    {
        return switch (time)
        {
            case 1 -> LocalTime.of(8, 30);
            case 2 -> LocalTime.of(9, 15);
            case 3 -> LocalTime.of(10, 0);
            case 4 -> LocalTime.of(11, 0);
            case 5 -> LocalTime.of(11, 45);
            case 6 -> LocalTime.of(12, 30);
            case 7 -> LocalTime.of(13, 15);
            case 8 -> LocalTime.of(14, 0);
            case 9 -> LocalTime.of(14, 45);
            case 10 -> LocalTime.of(15, 30);
            case 11 -> LocalTime.of(16, 15);
            default -> null;
        };
    }

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
}
