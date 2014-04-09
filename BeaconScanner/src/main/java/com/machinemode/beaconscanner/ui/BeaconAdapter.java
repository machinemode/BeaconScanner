package com.machinemode.beaconscanner.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.machinemode.beaconscanner.R;
import com.machinemode.beaconscanner.model.Beacon;

public class BeaconAdapter extends ArrayAdapter<Beacon>
{
    private LayoutInflater inflater;

    static class ViewHolder
    {
        TextView name;
        TextView address;
        TextView rssi;
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

        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.beacon_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.address = (TextView) convertView.findViewById(R.id.address);
            viewHolder.rssi = (TextView) convertView.findViewById(R.id.rssi);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(getItem(position).getName());
        viewHolder.address.setText(getItem(position).getDevice().getAddress());
        viewHolder.rssi.setText(String.valueOf(getItem(position).getRssi()));
        viewHolder.rssi.setEnabled(getItem(position).isActive());
        //convertView.setEnabled(getItem(position).isActive());
        return convertView;
    }
}
