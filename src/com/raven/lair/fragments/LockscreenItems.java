/*
 * Copyright (C) 2017-2019 The Dirty Unicorns Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.raven.lair.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.SearchIndexableResource;
import android.provider.Settings;
import androidx.preference.*;
import android.app.WallpaperManager;
import android.os.ParcelFileDescriptor;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.search.Indexable;
import com.android.settings.SettingsPreferenceFragment;

import com.corvus.support.preferences.SystemSettingListPreference;
import com.corvus.support.preferences.SystemSettingSeekBarPreference;
import com.raven.lair.Utils;

import java.util.ArrayList;
import java.util.List;

public class LockscreenItems extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener, Indexable {

    private static final String KEY_LOCKSCREEN_BLUR = "lockscreen_blur";
    private Context mContext;
    private SystemSettingSeekBarPreference mLockscreenBlur;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.lockscreen_items);
	PreferenceScreen prefScreen = getPreferenceScreen();

	Context mContext = getContext();
	WallpaperManager manager = WallpaperManager.getInstance(mContext);
	ParcelFileDescriptor pfd = manager.getWallpaperFile(WallpaperManager.FLAG_LOCK);
	mLockscreenBlur = (SystemSettingSeekBarPreference) findPreference(KEY_LOCKSCREEN_BLUR);
        if (!Utils.isBlurSupported() || pfd != null) {
	    mLockscreenBlur.setEnabled(false);
            mLockscreenBlur.setSummary(getString(R.string.lockscreen_blur_disabled));
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.CORVUS;
    }

    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {
                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                        boolean enabled) {
                    final ArrayList<SearchIndexableResource> result = new ArrayList<>();
                    final SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.lockscreen_items;
                    result.add(sir);
                    return result;
                }

                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    final List<String> keys = super.getNonIndexableKeys(context);
                    return keys;
        }
    };
}
