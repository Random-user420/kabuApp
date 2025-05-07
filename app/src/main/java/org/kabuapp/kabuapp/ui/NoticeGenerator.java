package org.kabuapp.kabuapp.ui;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.widget.TextView;

import org.kabuapp.kabuapp.R;

public class NoticeGenerator
{
    private NoticeGenerator()
    {
    }

    public static void setNotice(Context c, TextView myTextView)
    {
        String prefix = c.getString(R.string.notice_code);
        String linkText = c.getString(R.string.github_text);
        String url = c.getString(R.string.github_link);

        String fullText = prefix + linkText + ".";

        SpannableString spannableString = new SpannableString(fullText);
        spannableString.setSpan(new URLSpan(url), fullText.indexOf(linkText), fullText.indexOf(linkText) + linkText.length(), Spanned.SPAN_COMPOSING);

        myTextView.setText(spannableString);

        myTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
