package com.machinemode.beaconscanner.widget;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Set;

public class SetAdapter<T> extends BaseAdapter
{
    private Set<T> set;

    public SetAdapter(Set<T> set)
    {
        super();
        this.set = set;
    }

    @Override
    public int getCount()
    {
        return set.size();
    }

    @Override
    public Object getItem(int position)
    {
        if (position < set.size())
        {
            int currentPosition = 0;

            for (T item : set)
            {
                if (currentPosition == position)
                {
                    return item;
                }
                currentPosition++;
            }
        }

        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        return null;
    }
}
