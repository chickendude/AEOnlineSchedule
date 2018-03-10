package ch.ralena.aeonlineschedule.components;

import android.content.Context;
import android.util.AttributeSet;

import com.takisoft.fix.support.v7.preference.EditTextPreference;

import java.util.Locale;

public class EditDecimalPreference extends EditTextPreference {
	public EditDecimalPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	public EditDecimalPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public EditDecimalPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public EditDecimalPreference(Context context) {
		super(context);
	}

	@Override
	public void setText(String text) {
		text = String.format(Locale.US, "%.2f", Double.valueOf(text));
		super.setText(text);
	}

	@Override
	public CharSequence getSummary() {
		String number;
		try {
			Float value = Float.parseFloat(getText());
			number = getText();
		} catch (NumberFormatException nfe) {
			number = " *invalid*";
		}
		return String.format("$%s", number);
	}
}
