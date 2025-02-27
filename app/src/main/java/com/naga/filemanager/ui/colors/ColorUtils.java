package com.naga.filemanager.ui.colors;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import androidx.annotation.ColorInt;

import com.naga.filemanager.R;
import com.naga.filemanager.ui.icons.Icons;
import com.naga.filemanager.utils.Utils;

/**
 * @author Emmanuel
 *         on 24/5/2017, at 18:56.
 */

public class ColorUtils {


    public static void colorizeIcons(Context context, int iconType, GradientDrawable background,
                                     @ColorInt int defaultColor) {
        switch (iconType) {
            case Icons.VIDEO:
            case Icons.IMAGE:
                background.setColor(Utils.getColor(context, R.color.video_item));
                break;
            case Icons.AUDIO:
                background.setColor(Utils.getColor(context, R.color.audio_item));
                break;
            case Icons.PDF:
                background.setColor(Utils.getColor(context, R.color.pdf_item));
                break;
            case Icons.CODE:
                background.setColor(Utils.getColor(context, R.color.code_item));
                break;
            case Icons.TEXT:
                background.setColor(Utils.getColor(context, R.color.text_item));
                break;
            case Icons.COMPRESSED:
                background.setColor(Utils.getColor(context, R.color.archive_item));
                break;
            case Icons.APK:
                background.setColor(Utils.getColor(context, R.color.apk_item));
                break;
            case Icons.NOT_KNOWN:
                background.setColor(Utils.getColor(context, R.color.generic_item));
            default:
                background.setColor(defaultColor);
                break;
        }
    }
}
