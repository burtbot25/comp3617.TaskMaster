package com.comp3617.assignment2

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.ListPreference
import android.preference.Preference
import android.preference.PreferenceActivity
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import android.preference.RingtonePreference
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.MenuItem

class SettingsActivity : PreferenceActivity() {


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        fragmentManager.beginTransaction().replace(android.R.id.content,
            SettingsFragment()).commit()

    }


}