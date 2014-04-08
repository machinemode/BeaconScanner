package com.machinemode.beaconscanner.ui;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class BeaconListFragment extends ListFragment
{
    private static final String TAG = BeaconListFragment.class.getSimpleName();
    private OnBeaconSelectedListener callback;

    public interface OnBeaconSelectedListener
    {
        public void onBeaconSelected(int position);
    }

    public BeaconListFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        try
        {
            callback = (OnBeaconSelectedListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString() + " must implement OnBeaconSelectedListener.onBeaconSelected");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated()");
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    public void onPause()
    {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        callback.onBeaconSelected(position);
    }
}
