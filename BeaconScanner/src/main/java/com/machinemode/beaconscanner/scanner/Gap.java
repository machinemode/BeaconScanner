package com.machinemode.beaconscanner.scanner;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * https://www.bluetooth.org/en-us/specification/assigned-numbers/generic-access-profile
 */
public final class Gap
{
    private Gap() { }

    public static final Map<Byte, String> DATATYPE;
    static
    {
        Map<Byte, String> map = new HashMap<Byte, String>();
        map.put((byte)0x01, "Flags");
        map.put((byte)0x02, "Incomplete List of 16-bit Service Class UUIDs");
        map.put((byte)0x03, "Complete List of 16-bit Service Class UUIDs");
        map.put((byte)0x04, "Incomplete List of 32-bit Service Class UUIDs");
        map.put((byte)0x05, "Complete List of 32-bit Service Class UUIDs");
        map.put((byte)0x06, "Incomplete List of 128-bit Service Class UUIDs");
        map.put((byte)0x07, "Complete List of 128-bit Service Class UUIDs");
        map.put((byte)0x08, "Shortened Local Name");
        map.put((byte)0x09, "Complete Local Name");
        map.put((byte)0x0A, "Tx Power Level");
        map.put((byte)0x0D, "Class of Device");
        map.put((byte)0x0E, "Simple Pairing Hash C");
        map.put((byte)0x0F, "Simple Pairing Randomizer R");
        map.put((byte)0x10, "Security Manager TK Value");
        map.put((byte)0x11, "Security Manager Out of Band Flags");
        map.put((byte)0x12, "Slave Connection Interval Range");
        map.put((byte)0x14, "List of 16-bit Service Solicitation UUIDs");
        map.put((byte)0x1F, "List of 32-bit Service Solicitation UUIDs");
        map.put((byte)0x15, "List of 128-bit Service Solicitation UUIDs");
        map.put((byte)0x16, "Service Data - 16-bit UUID");
        map.put((byte)0x20, "Service Data - 32-bit UUID");
        map.put((byte)0x21, "Service Data - 128-bit UUID");
        map.put((byte)0x17, "Public Target Address");
        map.put((byte)0x18, "Random Target Address");
        map.put((byte)0x19, "Appearance");
        map.put((byte)0x1A, "Advertising Interval");
        map.put((byte)0x1B, "LE Bluetooth Device Address");
        map.put((byte)0x1C, "LE Role");
        map.put((byte)0x1D, "Simple Pairing Hash C-256");
        map.put((byte)0x1E, "Simple Pairing Randomizer R-256");
        map.put((byte)0x3D, "3D Information Data");
        map.put((byte)0xFF, "Manufacturer Specific Data");
        DATATYPE = Collections.unmodifiableMap(map);
    }

    public static class Data
    {
        int length;
        String type;
        byte[] data;
    }

    public static List<Data> parseScanRecord(byte[] scanRecord)
    {
        /*
            {2 bytes}{Flags}{0000 0110}
            02 01 06
            {26 bytes}{Manufacturer}{company-byte0-byte1-uuid-major-minor-transmission}
            1A FF 4C000215ABABABABABABABABABABABABABABABAB000A0005C2
            {nothing}
            0000000000000000000000000000000000000000000000000000000000000000

            {2 bytes}{Flags}{0000 0110}
            02 01 06
            {26 bytes}{Manufacturer}{company-byte0-byte1-uuid-major-minor-transmission}
            1A FF 4C000215B9407F30F5F8466EAFF925556B57FE6DE6D0A4ACB6
            {9 bytes}{Local Name}{estimote}
            09 09 657374696D6F7465
            {14 bytes}{Service UUID}{uuid}
            0E 16 0A18D0E6ACA457EEB6D0E6ACA4
            00000000000000
        */


    }

    /**
     * This data type is used for manufacturer specific data. The first two data octets
     * shall contain a company identifier code from the Assigned Numbers - Company
     * Identifiers document. The interpretation of any other octets within the data shall
     * be defined by the manufacturer specified by the company identifier
     * @param data
     * @return
     */
    public static String parseAppleData(byte[] data)
    {

    }
}
