package com.example.marill_many_events.models;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

//https://developer.android.com/reference/android/graphics/Canvas
public class ProfilePictureGenerator {

    public static Bitmap generateProfilePicture(String input, int size) {
        // Create a bitmap with a square shape
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();

        // Generate a unique color based on the input string
        int color = generateColor(input);
        paint.setColor(color);
        canvas.drawRect(0, 0, size, size, paint); // Fill the background with color

        // Set up text paint
        paint.setColor(Color.WHITE);
        paint.setTextSize(size);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        // Draw the first letter of the input string in the center of the rectangle
        String letter = input.substring(0, 1).toUpperCase();
        canvas.drawText(letter, size/2, size/2 + (paint.getTextSize() / 3), paint);

        return bitmap;
    }

    /**
     * Generates a color based on the hash code of the input string.
     *
     * @param input The input string used to generate the color.
     * @return An integer representing the generated color in ARGB format.
     */
    private static int generateColor(String input) {

        int hash = input.hashCode(); // Generate a hash code from the input string
        return Color.argb(255, (hash & 0xff0000) >> 16, (hash & 0x00ff00) >> 8, (hash & 0x0000ff)); // Use the hash to generate a color
    }
}

