package org.lilith.kabuapp.schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.lilith.kabuapp.R;

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


        timeTextView.setText(beginTime + " - " + endTime);
        nameTextView.setText(name);
        roomTextView.setText("Raum: " + room);
        teacherTextView.setText("Lehrer: " + teacher);

        parentLayout.addView(lessonView);
    }

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


        timeTextView.setText(beginTime + " - " + endTime);
        nameTextView.setText(name + "  ");
        name2TextView.setText(name2);
        roomTextView.setText("Raum: " + room);
        room2TextView.setText("Raum: " + room2);
        teacherTextView.setText("Lehrer: " + teacher);
        teacher2TextView.setText("Lehrer: " + teacher2);

        parentLayout.addView(lessonView);
    }
}
