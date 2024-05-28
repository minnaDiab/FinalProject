package com.example.testversion;

import android.graphics.Bitmap;

public interface BitmapHelper {
    public Bitmap ByteArrayToBmp(byte[] b);
    public byte[] BmpToByteArray(Bitmap bitmap);
}
