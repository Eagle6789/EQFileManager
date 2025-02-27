package com.naga.filemanager.ui.colors;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;

import com.naga.filemanager.R;
import com.naga.filemanager.fragments.preference_fragments.PreferencesConstants;
import com.naga.filemanager.utils.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ColorPreferenceHelper {

    public static final @ColorRes int DEFAULT_PRIMARY_FIRST_TAB = R.color.primary_indigo,
            DEFAULT_PRIMARY_SECOND_TAB = R.color.primary_indigo,
            DEFAULT_ACCENT = R.color.primary_pink,
            DEFAULT_ICON_SKIN = R.color.primary_pink;

    /**
     * Combinations used when randomizing color selection at startup.
     */
    private static final @ColorRes int[][] RANDOM_COMBINATIONS = new int[][]{
            {R.color.primary_brown, R.color.primary_amber, R.color.primary_orange},
            {R.color.primary_indigo, R.color.primary_pink, R.color.primary_indigo},
            {R.color.primary_teal, R.color.primary_orange, R.color.primary_teal},
            {R.color.primary_teal_900, R.color.primary_amber, R.color.primary_orange},
            {R.color.primary_deep_purple, R.color.primary_pink, R.color.primary_deep_purple},
            {R.color.primary_blue_grey, R.color.primary_brown, R.color.primary_blue_grey},
            {R.color.primary_pink, R.color.primary_orange, R.color.primary_pink},
            {R.color.primary_blue_grey, R.color.primary_red, R.color.primary_blue_grey},
            {R.color.primary_red, R.color.primary_orange, R.color.primary_red},
            {R.color.primary_light_blue, R.color.primary_pink, R.color.primary_light_blue},
            {R.color.primary_cyan, R.color.primary_pink, R.color.primary_cyan}
    };

    /**
     * Randomizes (but does not save) the colors used by the interface.
     *
     * @return The {@link ColorPreference} object itself.
     */
    public static UserColorPreferences randomize(Context c) {
        @ColorRes int[] colorPos = RANDOM_COMBINATIONS[new Random().nextInt(RANDOM_COMBINATIONS.length)];

        return new UserColorPreferences(Utils.getColor(c, colorPos[0]), Utils.getColor(c, colorPos[0]),
                Utils.getColor(c, colorPos[1]), Utils.getColor(c, colorPos[2]));
    }

    /**
     * Eases the retrieval of primary colors ColorUsage.
     * If the index is out of bounds, the first primary color is returned as default.
     *
     * @param num The primary color index
     * @return The ColorUsage for the given primary color.
     */
    public static @ColorInt int getPrimary(UserColorPreferences currentColors, int num) {
        return num == 1? currentColors.primarySecondTab : currentColors.primaryFirstTab;
    }

    private UserColorPreferences currentColors;

    public UserColorPreferences getCurrentUserColorPreferences(Context context, SharedPreferences prefs) {
        if(currentColors == null) currentColors = getColorPreferences(context, prefs);
        return currentColors;
    }

    public void saveColorPreferences(SharedPreferences prefs, UserColorPreferences userPrefs) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PreferencesConstants.PREFERENCE_SKIN, userPrefs.primaryFirstTab);
        editor.putInt(PreferencesConstants.PREFERENCE_SKIN_TWO, userPrefs.primarySecondTab);
        editor.putInt(PreferencesConstants.PREFERENCE_ACCENT, userPrefs.accent);
        editor.putInt(PreferencesConstants.PREFERENCE_ICON_SKIN, userPrefs.iconSkin);
        editor.apply();

        currentColors = userPrefs;
    }

    private UserColorPreferences getColorPreferences(Context c, SharedPreferences prefs) {
        if(isUsingOldColorsSystem(prefs)) correctToNewColorsSystem(c, prefs);

        int tabOne = prefs.getInt(PreferencesConstants.PREFERENCE_SKIN, Utils.getColor(c, DEFAULT_PRIMARY_FIRST_TAB));
        int tabTwo = prefs.getInt(PreferencesConstants.PREFERENCE_SKIN_TWO, Utils.getColor(c, DEFAULT_PRIMARY_SECOND_TAB));
        int accent = prefs.getInt(PreferencesConstants.PREFERENCE_ACCENT, Utils.getColor(c, DEFAULT_ACCENT));
        int iconSkin = prefs.getInt(PreferencesConstants.PREFERENCE_ICON_SKIN, Utils.getColor(c, DEFAULT_ICON_SKIN));

        return new UserColorPreferences(tabOne, tabTwo, accent, iconSkin);
    }

    /**
     * The old system used indexes, from here on in this file a correction is made so that the
     * indexes are converted into ColorInts
     */
    private boolean isUsingOldColorsSystem(SharedPreferences prefs) {
        int tabOne = prefs.getInt(PreferencesConstants.PREFERENCE_SKIN, R.color.primary_indigo);
        int tabTwo = prefs.getInt(PreferencesConstants.PREFERENCE_SKIN_TWO, R.color.primary_indigo);
        int accent = prefs.getInt(PreferencesConstants.PREFERENCE_ACCENT, R.color.primary_pink);
        int iconSkin = prefs.getInt(PreferencesConstants.PREFERENCE_ICON_SKIN, R.color.primary_pink);

        boolean r1 = tabOne >= 0 && tabTwo >= 0 && accent >= 0 && iconSkin >= 0;
        boolean r2 = tabOne < 22 && tabTwo < 22 && accent < 22 && iconSkin < 22;
        return r1 && r2;
    }

    private static final List<Integer> OLD_SYSTEM_LIST = Arrays.asList(
            R.color.primary_red,
            R.color.primary_pink,
            R.color.primary_purple,
            R.color.primary_deep_purple,
            R.color.primary_indigo,
            R.color.primary_blue,
            R.color.primary_light_blue,
            R.color.primary_cyan,
            R.color.primary_teal,
            R.color.primary_green,
            R.color.primary_light_green,
            R.color.primary_amber,
            R.color.primary_orange,
            R.color.primary_deep_orange,
            R.color.primary_brown,
            R.color.primary_grey_900,
            R.color.primary_blue_grey,
            R.color.primary_teal_900,
            R.color.accent_pink,
            R.color.accent_amber,
            R.color.accent_light_blue,
            R.color.accent_light_green
    );

    private void correctToNewColorsSystem(Context c, SharedPreferences prefs) {
        int tabOne = prefs.getInt(PreferencesConstants.PREFERENCE_SKIN, -1);
        int tabTwo = prefs.getInt(PreferencesConstants.PREFERENCE_SKIN_TWO, -1);
        int accent = prefs.getInt(PreferencesConstants.PREFERENCE_ACCENT, -1);
        int iconSkin = prefs.getInt(PreferencesConstants.PREFERENCE_ICON_SKIN, -1);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PreferencesConstants.PREFERENCE_SKIN, correctForIndex(c, tabOne));
        editor.putInt(PreferencesConstants.PREFERENCE_SKIN_TWO, correctForIndex(c, tabTwo));
        editor.putInt(PreferencesConstants.PREFERENCE_ACCENT, correctForIndex(c, accent));
        editor.putInt(PreferencesConstants.PREFERENCE_ICON_SKIN, correctForIndex(c, iconSkin));
        editor.apply();
    }

    private @ColorInt int correctForIndex(Context c, int color) {
        if(color != -1) return Utils.getColor(c, OLD_SYSTEM_LIST.get(color));
        else return Utils.getColor(c, R.color.primary_indigo);
    }

}
