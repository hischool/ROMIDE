package com.romide.main.ide.view;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyntaxHighlightEditText extends EditText
{
    private TextWatcher myWatcher = null;
    private ForegroundColorSpan red = new ForegroundColorSpan(Color.rgb(255, 0, 0));

    final SyntaxHighlightEditText that = this;
    private List<String> reservedWords;

    private void createWatcherForEditor()
	{
        myWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
			{

            }

            @Override
            public void afterTextChanged(Editable s)
			{
                String actualCode = that.getText().toString();

                highlight(s, actualCode);
            }
        };
    }

    public SyntaxHighlightEditText(Context context)
	{
        super(context);
        init();
    }

    public SyntaxHighlightEditText(Context context, AttributeSet attrs)
	{
        super(context, attrs);
        init();
    }

    public SyntaxHighlightEditText(Context context, AttributeSet attrs, int defStyle)
	{
        super(context, attrs, defStyle);
        init();
    }

    private void init()
	{
        this.createWatcherForEditor();
        this.addTextChangedListener(this.myWatcher);
        this.reservedWords = Arrays.asList("int", "boolean", "float", "String", "function", "class", "new", "this", "private", "public", "protected");
    }

    private void highlight(Editable s, String text)
	{
        String regex = "(";
        int counter = 0;
        for (String word : reservedWords)
		{
            counter += 1;
            regex += "\\b" + word;
            if (counter < reservedWords.size())
			{
                regex += "|";
            }
        }
        regex += "\\s)";

        Pattern p = Pattern.compile("\\b" + regex + "\\s");
        Matcher m = p.matcher(text);

        while (m.find())
		{
            s.setSpan(new ForegroundColorSpan(Color.rgb(255, 0, 0)), m.start(), m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }
}
	
