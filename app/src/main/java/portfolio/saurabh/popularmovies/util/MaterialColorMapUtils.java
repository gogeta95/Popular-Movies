package portfolio.saurabh.popularmovies.util;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Trace;
import android.support.v7.graphics.Palette;

import portfolio.saurabh.popularmovies.R;


/**
 * Created by Saurabh on 21-08-2015.
 */
public class MaterialColorMapUtils {

    private final TypedArray sPrimaryColors;
    private final TypedArray sSecondaryColors;

    public MaterialColorMapUtils(Resources resources) {
        sPrimaryColors = resources.obtainTypedArray(
                R.array.letter_tile_colors);
        sSecondaryColors = resources.obtainTypedArray(
                R.array.letter_tile_colors_dark);
    }

    public static MaterialPalette getDefaultPrimaryAndSecondaryColors(Resources resources) {
        final int primaryColor = resources.getColor(
                R.color.colorPrimary);
        final int secondaryColor = resources.getColor(
                R.color.colorPrimaryDark);
        return new MaterialPalette(primaryColor, secondaryColor);
    }

    /**
     * Returns the hue component of a color int.
     *
     * @return A value between 0.0f and 1.0f
     */
    public static float hue(int color) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        int V = Math.max(b, Math.max(r, g));
        int temp = Math.min(b, Math.min(r, g));
        float H;
        if (V == temp) {
            H = 0;
        } else {
            final float vtemp = V - temp;
            final float cr = (V - r) / vtemp;
            final float cg = (V - g) / vtemp;
            final float cb = (V - b) / vtemp;
            if (r == V) {
                H = cb - cg;
            } else if (g == V) {
                H = 2 + cr - cb;
            } else {
                H = 4 + cg - cr;
            }
            H /= 6.f;
            if (H < 0) {
                H++;
            }
        }
        return H;
    }

    public static int colorFromBitmap(Bitmap bitmap) {
        // Author of Palette recommends using 24 colors when analyzing profile photos.
        final int NUMBER_OF_PALETTE_COLORS = 24;
        final Palette palette = Palette.generate(bitmap, NUMBER_OF_PALETTE_COLORS);
        if (palette != null && palette.getVibrantSwatch() != null) {
            return palette.getVibrantSwatch().getRgb();
        }
        return 0;
    }

    /**
     * Return primary and secondary colors from the Material color palette that are similar to
     * {@param color}.
     */
    public MaterialPalette calculatePrimaryAndSecondaryColor(int color) {
        if (Build.VERSION.SDK_INT > 17) {
            Trace.beginSection("calculatePrimaryAndSecondaryColor");
        }
        final float colorHue = hue(color);
        float minimumDistance = Float.MAX_VALUE;
        int indexBestMatch = 0;
        for (int i = 0; i < sPrimaryColors.length(); i++) {
            final int primaryColor = sPrimaryColors.getColor(i, 0);
            final float comparedHue = hue(primaryColor);
            // No need to be perceptually accurate when calculating color distances since
            // we are only mapping to 15 colors. Being slightly inaccurate isn't going to change
            // the mapping very often.
            final float distance = Math.abs(comparedHue - colorHue);
            if (distance < minimumDistance) {
                minimumDistance = distance;
                indexBestMatch = i;
            }
        }
        if (Build.VERSION.SDK_INT > 17) {
            Trace.endSection();

        }
        return new MaterialPalette(sPrimaryColors.getColor(indexBestMatch, 0),
                sSecondaryColors.getColor(indexBestMatch, 0));
    }

    public static class MaterialPalette implements Parcelable {
        public static final Creator<MaterialPalette> CREATOR = new Creator<MaterialPalette>() {
            @Override
            public MaterialPalette createFromParcel(Parcel in) {
                return new MaterialPalette(in);
            }

            @Override
            public MaterialPalette[] newArray(int size) {
                return new MaterialPalette[size];
            }
        };
        public final int mPrimaryColor;
        public final int mSecondaryColor;

        public MaterialPalette(int primaryColor, int secondaryColor) {
            mPrimaryColor = primaryColor;
            mSecondaryColor = secondaryColor;
        }

        private MaterialPalette(Parcel in) {
            mPrimaryColor = in.readInt();
            mSecondaryColor = in.readInt();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            MaterialPalette other = (MaterialPalette) obj;
            if (mPrimaryColor != other.mPrimaryColor) {
                return false;
            }
            return mSecondaryColor == other.mSecondaryColor;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + mPrimaryColor;
            result = prime * result + mSecondaryColor;
            return result;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(mPrimaryColor);
            dest.writeInt(mSecondaryColor);
        }
    }
}
