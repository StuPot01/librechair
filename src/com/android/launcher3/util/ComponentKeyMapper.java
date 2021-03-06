package com.android.launcher3.util;

/**
 * Copyright (C) 2017 The Android Open Source Project
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

import androidx.annotation.Nullable;
import com.android.launcher3.ItemInfoWithIcon;
import com.android.launcher3.allapps.AllAppsStore;
import java.util.Map;

public class ComponentKeyMapper<T> {

    protected final ComponentKey mComponentKey;

    public ComponentKeyMapper(ComponentKey key) {
        this.mComponentKey = key;
    }

    public @Nullable T getItem(Map<ComponentKey, T> map) {
        return map.get(mComponentKey);
    }

    public String getPackage() {
        return mComponentKey.componentName.getPackageName();
    }

    public String getComponentClass() {
        return mComponentKey.componentName.getClassName();
    }

    @Override
    public String toString() {
        return mComponentKey.toString();
    }

    public ComponentKey getKey() {
        return mComponentKey;
    }

    public ItemInfoWithIcon getApp(AllAppsStore allAppsStore) {
        return allAppsStore.getApp(mComponentKey);

        /*
        TODO: implement this
        if (getComponentClass().equals(InstantAppResolverImpl.COMPONENT_CLASS_MARKER)) {
            allAppsStore = b.b(this.mContext);
            return (a) allAppsStore.J.get(this.componentKey.componentName.getPackageName());
        } else if ((this.componentKey instanceof ShortcutKey) == null) {
            return null;
        } else {
            return (ShortcutInfo) com.google.android.apps.nexuslauncher.a.a.a(this.mContext).d.get((ShortcutKey) mComponentKey);
        }
        */
    }

}
