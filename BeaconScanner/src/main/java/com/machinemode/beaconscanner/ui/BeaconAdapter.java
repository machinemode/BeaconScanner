package com.machinemode.beaconscanner.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.machinemode.beaconscanner.R;
import com.machinemode.beaconscanner.model.Beacon;

import java.util.List;

public class BeaconAdapter extends ArrayAdapter<Beacon>
{
    private LayoutInflater inflater;
    private List<Beacon> beacons;

    static class ViewHolder
    {
        TextView address;
    }

    public BeaconAdapter(Context context, int resource, List<Beacon> objects)
    {
        super(context, resource, objects);
        beacons = objects;
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
            viewHolder.address = (TextView) convertView.findViewById(R.id.address);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.address.setText(beacons.get(position).getAddress());
        return convertView;
    }
}
