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

        // TODO: make labels separate and use monospaced text for values
        viewHolder.manufacturer.setText(manufacturerData.get(ManufacturerDataParser.COMPANY_IDENTIFIER));
        viewHolder.rssi.setText(String.valueOf(beacon.getRssi()));
        viewHolder.rssi.setEnabled(beacon.isActive());
        viewHolder.name.setText("Name: " + beacon.getLocalName());
        viewHolder.uuid.setText("UUID: " + manufacturerData.get(ManufacturerDataParser.AppleData.UUID));
        viewHolder.major.setText("Major: " + manufacturerData.get(ManufacturerDataParser.AppleData.MAJOR));
        viewHolder.minor.setText("Minor: " + manufacturerData.get(ManufacturerDataParser.AppleData.MINOR));
        viewHolder.tx.setText("Tx Power: " + manufacturerData.get(ManufacturerDataParser.AppleData.TX) + " dB");

        //convertView.setEnabled(getItem(position).isActive());
        return convertView;
    }
}
