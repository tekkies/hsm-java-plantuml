package uk.co.tekkies.hsm.plantuml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.Deflater;

public class PlantUmlUrlEncoder {
    public String getUrl(String plantUml) {
        byte[] array = plantUml.getBytes(StandardCharsets.UTF_8);
        byte[] deflatedUtf8PlantUml = deflate(array);
        String base64deflatedUtf8PlantUml  = new PlantUmlEncode64().encode64(deflatedUtf8PlantUml);
        String url = "https://www.plantuml.com/plantuml/png/"+base64deflatedUtf8PlantUml;
        return url;
    }

    public static byte[] deflate(byte[] data) {
        byte[] output = null;
        Deflater deflater = new Deflater(9);
        deflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        deflater.finish();
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
            output = outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    /**
     * Ported from javascript: https://plantuml.com/code-javascript-synchronous
     */
    class PlantUmlEncode64 {
        public String encode64(byte data[]) {
            final StringBuilder r = new StringBuilder();
            for (int i = 0; i < data.length; i += 3) {
                if (i+2==data.length) {
                    r.append(append3bytes(data[i] & 0xff, data[i+1] & 0xff, 0));
                } else if (i+1==data.length) {
                    r.append(append3bytes(data[i] & 0xff, 0, 0));
                } else {
                    r.append(append3bytes(data[i] & 0xff, data[i+1] & 0xff,data[i+2] & 0xff));
                }
            }
            return r.toString();
        }

        private String append3bytes(int b1, int b2, int b3) {
            final int c1 = b1 >> 2;
            final int c2 = ((b1 & 0x3) << 4) | (b2 >> 4);
            final int c3 = ((b2 & 0xF) << 2) | (b3 >> 6);
            final int c4 = b3 & 0x3F;
            String r = "";
            r += encode6bit((byte) (c1 & 0x3F));
            r += encode6bit((byte) (c2 & 0x3F));
            r += encode6bit((byte) (c3 & 0x3F));
            r += encode6bit((byte) (c4 & 0x3F));
            return r;
        }

        public char encode6bit(byte b) {
            if (b < 10) {
                return (char)(48 + b);
            }
            b -= 10;
            if (b < 26) {
                return (char)(65 + b);
            }
            b -= 26;
            if (b < 26) {
                return (char)(97 + b);
            }
            b -= 26;
            if (b == 0) {
                return '-';
            }
            if (b == 1) {
                return '_';
            }
            return '?';
        }
    }
}
