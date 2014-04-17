package com.machinemode.beaconscanner.scanner;

public class ManufacturerDataParser
{

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
        return null;
    }
}
