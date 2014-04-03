package com.machinemode.beaconscanner.ui;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;

public class BeaconListFragment extends ListFragment
{
    private static final String TAG = BeaconListFragment.class.getSimpleName();


    public BeaconListFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
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

}
