package com.machinemode.beaconscanner.scanner;

import com.machinemode.beaconscanner.util.ByteConverter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Supplement to the Bluetooth Core Specification
 * 1.4 MANUFACTURER SPECIFIC DATA
 *
 * This data type is used for manufacturer specific data. The first two data octets
 * shall contain a company identifier code from the Assigned Numbers - Company
 * Identifiers document. The interpretation of any other octets within the data shall
 * be defined by the manufacturer specified by the company identifier
 */
public class ManufacturerDataParser
{
    private static final String TAG = ManufacturerDataParser.class.getSimpleName();

    private ManufacturerDataParser() { }

    /**
     * https://www.bluetooth.org/en-us/specification/assigned-numbers/company-identifiers
     */
    private static final Map<Character, String> COMPANYS;
    static
    {
        Map<Character, String> map = new HashMap<Character, String>();
        map.put((char)  0x0000, "Ericsson Technology Licensing");
        map.put((char)  0x0001, "Nokia Mobile Phones");
        map.put((char)  0x0002, "Intel Corp.");
        map.put((char)  0x0003, "IBM Corp.	");
        map.put((char)  0x0004, "Toshiba Corp.");
        map.put((char)	0x0005, "3Com");
        map.put((char)	0x0006, "Microsoft");
        map.put((char)	0x0007, "Lucent");
        map.put((char)	0x0008, "Motorola");
        map.put((char)	0x0009, "Infineon Technologies AG");
        map.put((char)	0x000A, "Cambridge Silicon Radio");
        map.put((char)	0x000B, "Silicon Wave");
        map.put((char)	0x000C, "Digianswer A/S");
        map.put((char)	0x000D, "Texas Instruments Inc.");
        map.put((char)	0x000E, "Ceva, Inc. (formerly Parthus Technologies, Inc.)");
        map.put((char)	0x000F, "Broadcom Corporation");
        map.put((char)	0x0010, "Mitel Semiconductor");
        map.put((char)	0x0011, "Widcomm, Inc");
        map.put((char)	0x0012, "Zeevo, Inc.");
        map.put((char)	0x0013, "Atmel Corporation");
        map.put((char)	0x0014, "Mitsubishi Electric Corporation");
        map.put((char)	0x0015, "RTX Telecom A/S");
        map.put((char)	0x0016, "KC Technology Inc.");
        map.put((char)	0x0017, "NewLogic");
        map.put((char)	0x0018, "Transilica, Inc.");
        map.put((char)	0x0019, "Rohde & Schwarz GmbH & Co. KG");
        map.put((char)	0x001A, "TTPCom Limited");
        map.put((char)	0x001B, "Signia Technologies, Inc.");
        map.put((char)	0x001C, "Conexant Systems Inc.");
        map.put((char)	0x001D, "Qualcomm");
        map.put((char)	0x001E, "Inventel");
        map.put((char)	0x001F, "AVM Berlin");
        map.put((char)	0x0020, "BandSpeed, Inc.");
        map.put((char)	0x0021, "Mansella Ltd");
        map.put((char)	0x0022, "NEC Corporation");
        map.put((char)	0x0023, "WavePlus Technology Co., Ltd.");
        map.put((char)	0x0024, "Alcatel");
        map.put((char)	0x0025, "Philips Semiconductors");
        map.put((char)	0x0026, "C Technologies");
        map.put((char)	0x0027, "Open Interface	");
        map.put((char)	0x0028, "R F Micro Devices");
        map.put((char)	0x0029, "Hitachi Ltd	");
        map.put((char)	0x002A, "Symbol Technologies, Inc.");
        map.put((char)	0x002B, "Tenovis");
        map.put((char)	0x002C, "Macronix International Co. Ltd.");
        map.put((char)	0x002D, "GCT Semiconductor");
        map.put((char)	0x002E, "Norwood Systems");
        map.put((char)	0x002F, "MewTel Technology Inc.");
        map.put((char)	0x0030, "ST Microelectronics");
        map.put((char)	0x0031, "Synopsis");
        map.put((char)	0x0032, "Red-M (Communications) Ltd");
        map.put((char)	0x0033, "Commil Ltd");
        map.put((char)	0x0034, "Computer Access Technology Corporation (CATC)");
        map.put((char)	0x0035, "Eclipse (HQ Espana) S.L.");
        map.put((char)	0x0036, "Renesas Technology Corp.");
        map.put((char)	0x0037, "Mobilian Corporation");
        map.put((char)	0x0038, "Terax");
        map.put((char)	0x0039, "Integrated System Solution Corp.");
        map.put((char)	0x003A, "Matsushita Electric Industrial Co., Ltd.");
        map.put((char)	0x003B, "Gennum Corporation");
        map.put((char)	0x003C, "Research In Motion");
        map.put((char)	0x003D, "IPextreme, Inc.");
        map.put((char)	0x003E, "Systems and Chips, Inc.");
        map.put((char)	0x003F, "Bluetooth SIG, Inc.");
        map.put((char)	0x0040, "Seiko Epson Corporation");
        map.put((char)	0x0041, "Integrated Silicon Solution Taiwan, Inc.");
        map.put((char)	0x0042, "CONWISE Technology Corporation Ltd");
        map.put((char)	0x0043, "PARROT SA");
        map.put((char)	0x0044, "Socket Mobile");
        map.put((char)	0x0045, "Atheros Communications, Inc.");
        map.put((char)	0x0046, "MediaTek, Inc.");
        map.put((char)	0x0047, "Bluegiga");
        map.put((char)	0x0048, "Marvell Technology Group Ltd.");
        map.put((char)	0x0049, "3DSP Corporation");
        map.put((char)	0x004A, "Accel Semiconductor Ltd.");
        map.put((char)	0x004B, "Continental Automotive Systems");
        map.put((char)	0x004C, "Apple, Inc.");
        map.put((char)	0x004D, "Staccato Communications, Inc.");
        map.put((char)	0x004E, "Avago Technologies");
        map.put((char)	0x004F, "APT Licensing Ltd.");
        map.put((char)	0x0050, "SiRF Technology");
        map.put((char)	0x0051, "Tzero Technologies, Inc.");
        map.put((char)	0x0052, "J&M Corporation");
        map.put((char)	0x0053, "Free2move AB");
        map.put((char)	0x0054, "3DiJoy Corporation");
        map.put((char)	0x0055, "Plantronics, Inc.");
        map.put((char)	0x0056, "Sony Ericsson Mobile Communications");
        map.put((char)	0x0057, "Harman International Industries, Inc.");
        map.put((char)	0x0058, "Vizio, Inc.");
        map.put((char)	0x0059, "Nordic Semiconductor ASA");
        map.put((char)	0x005A, "EM Microelectronic-Marin SA");
        map.put((char)	0x005B, "Ralink Technology Corporation");
        map.put((char)	0x005C, "Belkin International, Inc.");
        map.put((char)	0x005D, "Realtek Semiconductor Corporation");
        map.put((char)	0x005E, "Stonestreet One, LLC");
        map.put((char)	0x005F, "Wicentric, Inc.");
        map.put((char)	0x0060, "RivieraWaves S.A.S");
        map.put((char)	0x0061, "RDA Microelectronics");
        map.put((char)	0x0062, "Gibson Guitars");
        map.put((char)	0x0063, "MiCommand Inc.");
        map.put((char)	0x0064, "Band XI International, LLC");
        map.put((char)	0x0065, "Hewlett-Packard Company");
        map.put((char)	0x0066, "9Solutions Oy");
        map.put((char)	0x0067, "GN Netcom A/S");
        map.put((char)	0x0068, "General Motors");
        map.put((char)	0x0069, "A&D Engineering, Inc.");
        map.put((char)	0x006A, "MindTree Ltd.");
        map.put((char)	0x006B, "Polar Electro OY");
        map.put((char)	0x006C, "Beautiful Enterprise Co., Ltd.");
        map.put((char)	0x006D, "BriarTek, Inc.");
        map.put((char)	0x006E, "Summit Data Communications, Inc.");
        map.put((char)	0x006F, "Sound ID");
        map.put((char)	0x0070, "Monster, LLC");
        map.put((char)	0x0071, "connectBlue AB");
        map.put((char)	0x0072, "ShangHai Super Smart Electronics Co. Ltd.");
        map.put((char)	0x0073, "Group Sense Ltd.");
        map.put((char)	0x0074, "Zomm, LLC");
        map.put((char)	0x0075, "Samsung Electronics Co. Ltd.");
        map.put((char)	0x0076, "Creative Technology Ltd.");
        map.put((char)	0x0077, "Laird Technologies");
        map.put((char)	0x0078, "Nike, Inc.");
        map.put((char)	0x0079, "lesswire AG");
        map.put((char)	0x007A, "MStar Semiconductor, Inc.");
        map.put((char)	0x007B, "Hanlynn Technologies");
        map.put((char)	0x007C, "A & R Cambridge");
        map.put((char)	0x007D, "Seers Technology Co. Ltd");
        map.put((char)	0x007E, "Sports Tracking Technologies Ltd.");
        map.put((char)	0x007F, "Autonet Mobile");
        map.put((char)	0x0080, "DeLorme Publishing Company, Inc.");
        map.put((char)	0x0081, "WuXi Vimicro");
        map.put((char)	0x0082, "Sennheiser Communications A/S");
        map.put((char)	0x0083, "TimeKeeping Systems, Inc.");
        map.put((char)	0x0084, "Ludus Helsinki Ltd.");
        map.put((char)	0x0085, "BlueRadios, Inc.");
        map.put((char)	0x0086, "equinox AG");
        map.put((char)	0x0087, "Garmin International, Inc.");
        map.put((char)	0x0088, "Ecotest");
        map.put((char)	0x0089, "GN ReSound A/S");
        map.put((char)	0x008A, "Jawbone");
        map.put((char)	0x008B, "Topcorn Positioning Systems, LLC");
        map.put((char)	0x008C, "Qualcomm Retail Solutions, Inc. (formerly Qualcomm Labs, Inc.)");
        map.put((char)	0x008D, "Zscan Software");
        map.put((char)	0x008E, "Quintic Corp.");
        map.put((char)	0x008F, "Stollman E+V GmbH");
        map.put((char)	0x0090, "Funai Electric Co., Ltd.");
        map.put((char)	0x0091, "Advanced PANMOBIL Systems GmbH & Co. KG");
        map.put((char)	0x0092, "ThinkOptics, Inc.");
        map.put((char)	0x0093, "Universal Electronics, Inc.");
        map.put((char)	0x0094, "Airoha Technology Corp.");
        map.put((char)	0x0095, "NEC Lighting, Ltd.");
        map.put((char)	0x0096, "ODM Technology, Inc.");
        map.put((char)	0x0097, "ConnecteDevice Ltd.");
        map.put((char)	0x0098, "zer01.tv GmbH");
        map.put((char)	0x0099, "i.Tech Dynamic Global Distribution Ltd.");
        map.put((char)	0x009A, "Alpwise");
        map.put((char)	0x009B, "Jiangsu Toppower Automotive Electronics Co., Ltd.");
        map.put((char)	0x009C, "Colorfy, Inc.");
        map.put((char)	0x009D, "Geoforce Inc.");
        map.put((char)	0x009E, "Bose Corporation");
        map.put((char)	0x009F, "Suunto Oy");
        map.put((char)	0x00A0, "Kensington Computer Products Group");
        map.put((char)	0x00A1, "SR-Medizinelektronik");
        map.put((char)	0x00A2, "Vertu Corporation Limited");
        map.put((char)	0x00A3, "Meta Watch Ltd.");
        map.put((char)	0x00A4, "LINAK A/S");
        map.put((char)	0x00A5, "OTL Dynamics LLC");
        map.put((char)	0x00A6, "Panda Ocean Inc.");
        map.put((char)	0x00A7, "Visteon Corporation");
        map.put((char)	0x00A8, "ARP Devices Limited");
        map.put((char)	0x00A9, "Magneti Marelli S.p.A");
        map.put((char)	0x00AA, "CAEN RFID srl");
        map.put((char)	0x00AB, "Ingenieur-Systemgruppe Zahn GmbH");
        map.put((char)	0x00AC, "Green Throttle Games");
        map.put((char)	0x00AD, "Peter Systemtechnik GmbH");
        map.put((char)	0x00AE, "Omegawave Oy");
        map.put((char)	0x00AF, "Cinetix");
        map.put((char)	0x00B0, "Passif Semiconductor Corp");
        map.put((char)	0x00B1, "Saris Cycling Group, Inc");
        map.put((char)	0x00B2, "Bekey A/S");
        map.put((char)  0x00B3, "Clarinox Technologies Pty. Ltd.");
        map.put((char)  0x00B4, "BDE Technology Co., Ltd.");
        map.put((char)  0x00B5, "Swirl Networks");
        map.put((char)  0x00B6, "Meso international");
        map.put((char)  0x00B7, "TreLab Ltd");
        map.put((char)  0x00B8, "Qualcomm Innovation Center, Inc. (QuIC)");
        map.put((char)  0x00B9, "Johnson Controls, Inc.");
        map.put((char)	0x00BA, "Starkey Laboratories Inc.");
        map.put((char)  0x00BB, "S-Power Electronics Limited");
        map.put((char)  0x00BC, "Ace Sensor Inc");
        map.put((char)  0x00BD, "Aplix Corporation");
        map.put((char)  0x00BE, "AAMP of America");
        map.put((char)	0x00BF, "Stalmart Technology Limited");
        map.put((char)	0x00C0, "AMICCOM Electronics Corporation");
        map.put((char)	0x00C1, "Shenzhen Excelsecu Data Technology Co.,Ltd");
        map.put((char)	0x00C2, "Geneq Inc.");
        map.put((char)  0x00C3, "adidas AG");
        map.put((char)  0x00C4, "LG Electronics");
        map.put((char)	0x00C5, "Onset Computer Corporation");
        map.put((char)	0x00C6, "Selfly BV");
        map.put((char)	0x00C7, "Quuppa Oy.");
        map.put((char)	0x00C8, "GeLo Inc");
        map.put((char)	0x00C9, "Evluma");
        map.put((char)	0x00CA, "MC10");
        map.put((char)	0x00CB, "Binauric SE");
        map.put((char)	0x00CC, "Beats Electronics");
        map.put((char)	0x00CD, "Microchip Technology Inc.");
        map.put((char)	0x00CE, "Elgato Systems GmbH");
        map.put((char)	0x00CF, "ARCHOS SA");
        map.put((char)	0x00D0, "Dexcom, Inc.");
        map.put((char)	0x00D1, "Polar Electro Europe B.V.");
        map.put((char)	0x00D2, "Dialog Semiconductor B.V.");
        map.put((char)	0x00D3, "Taixingbang Technology (HK) Co,. LTD.");
        map.put((char)	0x00D4, "Kawantech");
        map.put((char)	0x00D5, "Austco Communication Systems");
        map.put((char)	0x00D6, "Timex Group USA, Inc.");
        map.put((char)	0x00D7, "Qualcomm Technologies, Inc.");
        map.put((char)	0x00D8, "Qualcomm Connected Experiences, Inc.");
        map.put((char)	0x00D9, "Voyetra Turtle Beach");
        map.put((char)	0x00DA, "txtr GmbH");
        map.put((char)	0x00DB, "Biosentronics");
        map.put((char)	0x00DC, "Procter & Gamble");
        map.put((char)	0x00DD, "Hosiden Corporation");
        map.put((char)	0x00DE, "Muzik LLC");
        map.put((char)	0x00DF, "Misfit Wearables Corp");
        map.put((char)	0x00E0, "Google");
        map.put((char)	0x00E1, "Danlers Ltd");
        map.put((char)	0x00E2, "Semilink Inc");
        map.put((char)	0x00E3, "inMusic Brands, Inc");
        map.put((char)	0x00E4, "L.S. Research Inc.");
        map.put((char)	0x00E5, "Eden Software Consultants Ltd.");
        map.put((char)	0x00E6, "Freshtemp");
        map.put((char)	0x00E7, "KS Technologies");
        map.put((char)	0x00E8, "ACTS Technologies");
        map.put((char)	0x00E9, "Vtrack Systems");
        map.put((char)	0x00EA, "Nielsen-Kellerman Company");
        map.put((char)	0x00EB, "Server Technology, Inc.");
        map.put((char)	0x00EC, "BioResearch Associates");
        map.put((char)	0x00ED, "Jolly Logic, LLC");
        map.put((char)	0x00EE, "Above Average Outcomes, Inc.");
        map.put((char)	0x00EF, "Bitsplitters GmbH");
        map.put((char)	0x00F0, "PayPal, Inc.");
        map.put((char)	0x00F1, "Witron Technology Limited");
        map.put((char)	0x00F2, "Aether Things Inc. (formerly Morse Project Inc.)");
        map.put((char)	0x00F3, "Kent Displays Inc.");
        map.put((char)	0x00F4, "Nautilus Inc.");
        map.put((char)	0x00F5, "Smartifier Oy");
        map.put((char)	0x00F6, "Elcometer Limited");
        map.put((char)	0x00F7, "VSN Technologies Inc.");
        map.put((char)	0x00F8, "AceUni Corp., Ltd.");
        map.put((char)	0x00F9, "StickNFind");
        map.put((char)	0x00FA, "Crystal Code AB");
        map.put((char)	0x00FB, "KOUKAAM a.s.");
        map.put((char)	0x00FC, "Delphi Corporation");
        map.put((char)	0x00FD, "ValenceTech Limited");
        map.put((char)	0x00FE, "Reserved");
        map.put((char)	0x00FF, "Typo Products, LLC");
        map.put((char)	0x0100, "TomTom International BV");
        map.put((char)	0x0101, "Fugoo, Inc");
        map.put((char)	0x0102, "Keiser Corporation");
        map.put((char)	0x0103, "Bang & Olufsen A/S");
        map.put((char)	0x0104, "PLUS Locations Systems Pty Ltd");
        map.put((char)	0x0105, "Ubiquitous Computing Technology Corporation");
        map.put((char)	0x0106, "Innovative Yachtter Solutions");
        map.put((char)	0x0107, "William Demant Holding A/S");
        map.put((char)	0x0108, "Chicony Electronics Co., Ltd.");
        map.put((char)	0x0109, "Atus BV");
        map.put((char)	0x010A, "Codegate Ltd.");
        map.put((char)	0x010B, "ERi, Inc.");
        map.put((char)	0x010C, "Transducers Direct, LLC");
        map.put((char)	0x010D, "Fujitsu Ten Limited");
        map.put((char)	0x010E, "Audi AG");
        map.put((char)	0x010F, "HiSilicon Technologies Co., Ltd.");
        map.put((char)	0x0110, "Nippon Seiki Co., Ltd.");
        map.put((char)	0x0111, "Steelseries ApS");
        map.put((char)	0x0112, "vyzybl Inc.");
        map.put((char)	0x0113, "Openbrain Technologies, Co., Ltd.");
        map.put((char)	0x0114, "Xensr");
        map.put((char)	0x0115, "e.solutions");
        map.put((char)	0x0116, "1OAK Technologies");
        map.put((char)	0x0117, "Wimoto Technologies Inc");
        map.put((char)	0x0118, "Radius Networks, Inc.");
        map.put((char)	0x0119, "Wize Technology Co., Ltd.");
        map.put((char)	0x011A, "Qualcomm Labs, Inc.");
        map.put((char)	0x011B, "Aruba Networks");
        map.put((char)	0x011C, "Baidu");
        map.put((char)	0x011D, "Arendi AG");
        map.put((char)	0x011E, "Skoda Auto a.s.");
        map.put((char)	0x011F, "Volkswagon AG");
        map.put((char)	0x0120, "Porsche AG");
        map.put((char)	0x0121, "Sino Wealth Electronic Ltd.");
        map.put((char)	0x0122, "AirTurn, Inc.");
        map.put((char)	0x0123, "Kinsa, Inc.");
        map.put((char)	0x0124, "HID Global");
        map.put((char)	0x0125, "SEAT es");
        map.put((char)	0x0126, "Promethean Ltd.");
        map.put((char)	0x0127, "Salutica Allied Solutions");
        map.put((char)	0x0128, "GPSI Group Pty Ltd	");
        map.put((char)	0x0129, "Nimble Devices Oy");
        map.put((char)	0x012A, "Changzhou Yongse Infotech Co., Ltd");
        map.put((char)	0x012B, "SportIQ");
        map.put((char)	0x012C, "TEMEC Instruments B.V.");
        map.put((char)	0x012D, "Sony Corporation");
        map.put((char)	0x012E, "ASSA ABLOY");
        map.put((char)	0x012F, "Clarion Co., Ltd.");
        map.put((char)	0x0130, "Warehouse Innovations");
        map.put((char)	0x0131, "Cypress Semiconductor Corporation");
        map.put((char)	0x0132, "MADS Inc");
        map.put((char)	0x0133, "Blue Maestro Limited");
        map.put((char)	0x0134, "Resolution Products, Inc.");
        map.put((char)	0x0135, "Airewear LLC");
        map.put((char)	0x0136, "ETC sp. z.o.o.");
        map.put((char)	0x0137, "Prestigio Plaza Ltd.");
        map.put((char)	0x0138, "NTEO Inc.");
        map.put((char)	0x0139, "Focus Systems Corporation");
        map.put((char)	0x013A, "Tencent Holdings Limited");
        map.put((char)	0x013B, "Allegion");
        map.put((char)	0x013C, "Murata Manufacuring Co., Ltd.");
        map.put((char)	0x013D, "WirelessWERX");
        map.put((char)	0x013E, "ënimai");
        map.put((char)	0x013F, "B&B Manufacturing Company");
        map.put((char)	0x0140, "Alpine Electronics (China) Co., Ltd");
        map.put((char)	0x0141, "FedEx Services");
        map.put((char)  0xFFFF, "Internal test id");
        COMPANYS = Collections.unmodifiableMap(map);
    }

    public static final String COMPANY_IDENTIFIER = "Company Identifier";
    public static final String MANUFACTURER_DATA = "Manufacturer Data";

    // Apple Specific data
    public static final class AppleData
    {
        public static final String BYTE0 = "Byte 0";
        public static final String BYTE1 = "Byte 1";
        public static final String UUID = "UUID";
        public static final String MAJOR = "Major";
        public static final String MINOR = "Minor";
        public static final String TX = "Tx Power";
        public static final String UNKNOWN = "Unknown data";
    }


    public static Map<String, String> decodeData(byte[] data)
    {
        Map<String, String> map = new HashMap<String, String>();

        if (data.length > 2)
        {
            char companyIdentifier = ByteConverter.toUnsignedShort(data[0], data[1], ByteConverter.Endian.LITTLE);
            String companyName = COMPANYS.get(companyIdentifier);
            map.put(COMPANY_IDENTIFIER, companyName == null ? "Unknown" : companyName);

            byte[] manufacturerData = Arrays.copyOfRange(data, 2, data.length);
            if (companyIdentifier == (short)0x004C)
            {
                map.putAll(parseAppleData(manufacturerData));
            }
            else
            {
                map.put(MANUFACTURER_DATA, ByteConverter.toHex(manufacturerData));
            }
        }
        else
        {
            map.put(MANUFACTURER_DATA, ByteConverter.toHex(data));
        }
        return map;
    }

    private static Map<String, String> parseAppleData(byte[] data)
    {
        Map<String, String> map = new HashMap<String, String>();

        if (data.length == 23)
        {
            map.put(AppleData.BYTE0, ByteConverter.toHex(data[0]));
            map.put(AppleData.BYTE1, ByteConverter.toHex(data[1]));
            map.put(AppleData.UUID, ByteConverter.toHex(Arrays.copyOfRange(data, 2, 18)));
            int major = ByteConverter.toUnsignedInt(data[18], data[19]);
            int minor = ByteConverter.toUnsignedInt(data[20], data[21]);
            map.put(AppleData.MAJOR, String.valueOf(major));
            map.put(AppleData.MINOR, String.valueOf(minor));
            map.put(AppleData.TX, String.valueOf(data[22]));
        }
        else
        {
            map.put(AppleData.UNKNOWN, ByteConverter.toHex(data));
        }
        return map;
    }
}
