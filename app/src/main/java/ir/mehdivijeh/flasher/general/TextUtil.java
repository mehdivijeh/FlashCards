package ir.mehdivijeh.flasher.general;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TextUtil {

    private static Typeface typeface;

    public static Typeface getEnglishFont(Context context) {
        if (typeface == null) {
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/lato.ttf");
        }
        return typeface;
    }

    public static void setFonts(View v) {
        if (v == null) return;
        if (v instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) v).getChildCount(); i++) {
                setFonts(((ViewGroup) v).getChildAt(i));
            }
        } else if (v instanceof TextView) {
            ((TextView) v).setTypeface(getEnglishFont(v.getContext()));
        }
    }

}
