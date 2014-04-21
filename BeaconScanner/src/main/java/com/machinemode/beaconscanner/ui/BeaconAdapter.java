package com.machinemode.beaconscanner.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.machinemode.beaconscanner.R;
import com.machinemode.beaconscanner.model.Beacon;
import com.machinemode.beaconscanner.scanner.ManufacturerDataParser;

import java.util.Map;

public class BeaconAdapter extends ArrayAdapter<Beacon>
{
    private LayoutInflater inflater;

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

    public BeaconAdapter(Context context, int resource)
    {
        super(context, resource);
        inflater = LayoutInflater.from(getContext());
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
        Beacon beacon = getItem(position);

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

        if (name != null && !name.isEmpty())
        {
            viewHolder.name.setText(name);
        }
    }

    private void setManufacturerText(ViewHolder viewHolder, Beacon beacon)
    {
        Map<String, String> manufacturerData = beacon.getManufacturerData();
        String manufacturer = manufacturerData.get(ManufacturerDataParser.COMPANY_IDENTIFIER);
        String uuid = manufacturerData.get(ManufacturerDataParser.AppleData.UUID);
        String major = manufacturerData.get(ManufacturerDataParser.AppleData.MAJOR);
        String minor = manufacturerData.get(ManufacturerDataParser.AppleData.MINOR);
        String tx = manufacturerData.get(ManufacturerDataParser.AppleData.TX);

        if (manufacturer != null && !manufacturer.isEmpty())
        {
            viewHolder.manufacturer.setText(manufacturer);
        }

        if (uuid != null && !uuid.isEmpty())
        {
            viewHolder.uuid.setText(uuid);
        }

        if (major != null && !major.isEmpty())
        {
            viewHolder.major.setText(major);
        }

        if (minor != null && !minor.isEmpty())
        {
            viewHolder.minor.setText(minor);
        }

        if (tx != null && !tx.isEmpty())
        {
            viewHolder.tx.setText(tx + " dBm");
        }
        else if (beacon.getTxPowerLevel() < 0)
        {
            viewHolder.tx.setText(beacon.getTxPowerLevel() + " dBm");
        }
    }

    private void setRssiText(ViewHolder viewHolder, Beacon beacon)
    {
        int rssi = beacon.getRssi();

        if (rssi < 0)
        {
            viewHolder.rssi.setText(String.valueOf(beacon.getRssi()) + " dBm");
            viewHolder.rssi.setBackgroundColor(rssiToColor(beacon.getRssi()));
        }
        else
        {
            viewHolder.rssi.setBackgroundColor(Color.LTGRAY);
        }
    }

    /**
     * 0 = no rssi available
     * -1 = near, -100 = far
     * y = mb + b
     * approx slope (m) at 5 for 255/50 = 5.1
     * @param rssi
     * @return #aarrggbb
     */
    private int rssiToColor(int rssi)
    {
        int color;

        if (rssi >= 0)
        {
            color = 0xFFFFFF00;
        }
        else if (rssi > -50)
        {
            int y = -5 * rssi;
            color =  0xFF00FF00 | (y << 16);
        }
        else
        {
            int y = (5 * rssi) + 510;
            color = 0xFFFF0000 | (y << 8);
        }

        return color;
    }
}
