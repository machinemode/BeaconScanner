package com.machinemode.beaconscanner.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.machinemode.beaconscanner.R;
import com.machinemode.beaconscanner.model.Beacon;
import com.machinemode.beaconscanner.scanner.ManufacturerDataParser;
import com.machinemode.beaconscanner.widget.SetAdapter;

import java.util.Map;
import java.util.Set;

public class BeaconAdapter extends SetAdapter<Beacon>
{
    private LayoutInflater inflater;
    private static final  String UNKNOWN_NAME = "Unknown";
    private static final String UNKNOWN_VALUE = "?";
    private static final int RSSI_SLOPE = 255/99;

    static class ViewHolder
    {
        TextView manufacturer;
        TextView rssi;
        TextView name;
        TextView uuid;
        TextView major;
        TextView minor;
        TextView tx;
    }

    public BeaconAdapter(Context context, Set<Beacon> set)
    {
        super(set);
        inflater = LayoutInflater.from(context);
    }

    /**
     * Overridden to disable clicks
     * @param position
     * @return false
     */
    @Override
    public boolean isEnabled(int position)
    {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;
        Beacon beacon = (Beacon)getItem(position);

        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.beacon_item, parent, false);
            viewHolder.manufacturer = (TextView) convertView.findViewById(R.id.manufacturer);
            viewHolder.rssi = (TextView) convertView.findViewById(R.id.rssi);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.uuid = (TextView) convertView.findViewById(R.id.uuid);
            viewHolder.major = (TextView) convertView.findViewById(R.id.major);
            viewHolder.minor = (TextView) convertView.findViewById(R.id.minor);
            viewHolder.tx = (TextView) convertView.findViewById(R.id.tx);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        setManufacturerText(viewHolder, beacon);
        setRssiText(viewHolder, beacon);
        setNameText(viewHolder, beacon);

        //convertView.setEnabled(getItem(position).isActive());
        return convertView;
    }


    private void setNameText(ViewHolder viewHolder, Beacon beacon)
    {
        String name = beacon.getLocalName();

        if (name == null || name.isEmpty())
        {
            name = UNKNOWN_NAME;
        }
        viewHolder.name.setText(name);
    }

    private void setManufacturerText(ViewHolder viewHolder, Beacon beacon)
    {
        Map<String, String> manufacturerData = beacon.getManufacturerData();
        String manufacturer = manufacturerData.get(ManufacturerDataParser.COMPANY_IDENTIFIER);
        String uuid = manufacturerData.get(ManufacturerDataParser.AppleData.UUID);
        String major = manufacturerData.get(ManufacturerDataParser.AppleData.MAJOR);
        String minor = manufacturerData.get(ManufacturerDataParser.AppleData.MINOR);
        String tx = manufacturerData.get(ManufacturerDataParser.AppleData.TX);

        if (manufacturer == null || manufacturer.isEmpty())
        {
            manufacturer = UNKNOWN_NAME;
        }
        viewHolder.manufacturer.setText(manufacturer);

        if (uuid == null || uuid.isEmpty())
        {
            uuid = UNKNOWN_NAME;
        }
        viewHolder.uuid.setText(uuid);

        if (major == null || major.isEmpty())
        {
            major = UNKNOWN_VALUE;
        }
        viewHolder.major.setText(major);

        if (minor == null || minor.isEmpty())
        {
            minor = UNKNOWN_VALUE;
        }
        viewHolder.minor.setText(minor);

        if (tx == null || tx.isEmpty())
        {
            if (beacon.getTxPowerLevel() < 0)
            {
                tx = String.valueOf(beacon.getTxPowerLevel());
            }
            else
            {
                tx = UNKNOWN_VALUE;
            }
        }
        viewHolder.tx.setText(tx + " dBm");

    }

    private void setRssiText(ViewHolder viewHolder, Beacon beacon)
    {
        int rssi = beacon.getRssi();

        if (System.currentTimeMillis() > beacon.getTimestamp() + 2000)
        {
            beacon.setActive(false);
        }

        if (rssi > 0 || !beacon.isActive())
        {
            viewHolder.rssi.setBackgroundColor(Color.LTGRAY);
        }
        else
        {
            viewHolder.rssi.setText(String.valueOf(beacon.getRssi()) + "\ndBm");
            viewHolder.rssi.setBackgroundColor(rssiToColor(beacon.getRssi()));
        }
    }

    /**
     * 0 = no rssi available
     * -1 = near, -100 = far
     * y = mx + b
     * @param rssi
     * @return #aarrggbb
     */
    private int rssiToColor(int rssi)
    {
        int color;

        if (rssi >= 0)
        {
            color = 0xFFCCCCCC;
        }
        else
        {
            int mx = RSSI_SLOPE * rssi;
            int r = mx + 255;
            int b = -mx - RSSI_SLOPE;
            color = 0xFF000000 | (r << 16);
            color |= (char)b;
        }

        return color;
    }
}
