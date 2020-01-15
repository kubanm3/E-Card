package com.ECard;

import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;

public class FilterCharacter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        boolean originalText = true;
        StringBuilder sb = new StringBuilder(end - start);
        for (int i = start; i < end; i++) {
            if (isCharBlocked(source.charAt(i))) {
                originalText = false;
            } else sb.append(source.charAt(i));
        }
        if (originalText) {
            return null;
        } else {
            if (source instanceof Spanned) {
                SpannableString sp = new SpannableString(sb);
                TextUtils.copySpansFrom((Spanned) source, start, sb.length(), null, sp, 0);
                return sp;
            } else {
                return sb;
            }
        }
    }

    private boolean isCharBlocked(char ch) {
        String blockCharacter = "ĄąĆćĘęŁłŃńÓóŚśŹźŻż";
        if (blockCharacter.contains(("" + ch))) {
            return true;
        }
        return false;
    }
}
