package me.superorca.jellyfish.core;

import lombok.experimental.UtilityClass;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

@UtilityClass
public class Util {
    public String getCountryName(String countryId) {
        return new Locale("", countryId).getDisplayCountry();
    }

    public byte[] toByteArray(BufferedImage bi, String format) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, format, baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    public BufferedImage toBufferedImage(byte[] bytes) {
        InputStream is = new ByteArrayInputStream(bytes);
        try {
            return ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
