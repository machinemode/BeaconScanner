package com.machinemode.beaconscanner.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.machinemode.beaconscanner.R;
import com.machinemode.beaconscanner.model.Beacon;
import com.machinemode.beaconscanner.model.ResponseData;
import com.machinemode.beaconscanner.scanner.ManufacturerDataParser;

import java.util.List;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;
        Beacon beacon = getItem(position);
        Map<String, String> manufacturerData = beacon.getManufacturerData();

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

        viewHolder.manufacturer.setText(manufacturerData.get(ManufacturerDataParser.COMPANY_IDENTIFIER));
        viewHolder.rssi.setText(String.valueOf(beacon.getRssi()) + " dBm");
        viewHolder.rssi.setBackgroundColor(rssiToColor(beacon.getRssi()));

        viewHolder.name.setText(beacon.getLocalName());
        viewHolder.uuid.setText(manufacturerData.get(ManufacturerDataParser.AppleData.UUID));
        viewHolder.major.setText(manufacturerData.get(ManufacturerDataParser.AppleData.MAJOR));
        viewHolder.minor.setText(manufacturerData.get(ManufacturerDataParser.AppleData.MINOR));
        viewHolder.tx.setText(manufacturerData.get(ManufacturerDataParser.AppleData.TX) + " dBm");

        //convertView.setEnabled(getItem(position).isActive());
        return convertView;
    }

    /**
     * 0 = no rssi available
     * -1 = near, -100 = far
     * @param rssi
     * @return #aarrggbb
     */
    private int rssiToColor(int rssi)
    {
        /*
         red   = 0xFF00 = 65280
         green = 0x00FF = 255
         y = mx + b
         m = (99/65025)
         y = (99/65025)x + b
         255 = (99/65025)(-1) + b
         255 + (99/65025) = b

         */
        return 0xFF000000 | (((99/65025) * rssi) + (255 + (99/65025)) << 8);
    }
}
