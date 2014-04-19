package com.machinemode.beaconscanner.util;

public final class ByteConverter
{
    private static final char[] HEX_VALUE = "0123456789ABCDEF".toCharArray();
    public static enum Endian
    {
        LITTLE,
        BIG
    }

    public static String toHex(byte[] bytes)
    {
        char[] hexChars = new char[bytes.length * 2];

        for (int i = 0; i < bytes.length; ++i)
        {
            hexChars[i << 1] = HEX_VALUE[(bytes[i] & 0xF0) >>> 4];
            hexChars[(i << 1) + 1] = HEX_VALUE[bytes[i] & 0x0F];
        }

        return new String(hexChars);
    }

    public static String toHex(byte b)
    {
        byte[] bytes = new byte[] { b };
        return toHex(bytes);
    }

    public static char toUnsignedShort(byte lhs, byte rhs, Endian endian)
    {
        char left = (char)(lhs & 0x00FF);
        char right = (char)(rhs & 0x00FF);

        switch (endian)
        {
            case LITTLE:
                return (char) ((right << 8) | left);
            default: //case BIG:
                return (char) ((left << 8) | right);
        }
    }

    public static char toUnsignedShort(byte lhs, byte rhs)
    {
        return toUnsignedShort(lhs, rhs, Endian.BIG);
    }

    public static int toUnsignedInt(byte lhs, byte rhs, Endian endian)
    {
        char left = (char)(lhs & 0x00FF);
        char right = (char)(rhs & 0x00FF);

        switch (endian)
        {
            case LITTLE:
                return (char) ((right << 8) | left);
            default: //case BIG:
                return (char) ((left << 8) | right);
        }
    }

    public static int toUnsignedInt(byte lhs, byte rhs)
    {
        return toUnsignedInt(lhs, rhs, Endian.BIG);
    }
}
