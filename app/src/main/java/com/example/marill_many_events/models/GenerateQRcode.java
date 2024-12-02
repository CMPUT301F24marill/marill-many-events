package com.example.marill_many_events.models;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;


public class GenerateQRcode {
    public Bitmap generateQR(String documentID){

        if(documentID == null){
            return null;
        }

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        int size = 400;

        // Set the hints for QR Code encoding
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.MARGIN, 1);  // Set margin size to 1 for compact QR code

        try {
            com.google.zxing.common.BitMatrix bitMatrix = qrCodeWriter.encode(documentID, BarcodeFormat.QR_CODE, size, size, hints);
            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565);

            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}
