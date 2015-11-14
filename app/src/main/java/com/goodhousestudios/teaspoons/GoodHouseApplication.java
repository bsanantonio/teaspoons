package com.goodhousestudios.teaspoons;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;

public class GoodHouseApplication extends Application {

    public ArrayList<GoodHouseTimer> goodHouseTimers = new ArrayList<>();
}
