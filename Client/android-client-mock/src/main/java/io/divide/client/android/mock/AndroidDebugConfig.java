/*
 * Copyright (C) 2014 Divide.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.divide.client.android.mock;

import android.content.Context;
import android.content.SharedPreferences;
import com.jug6ernaut.debugdrawer.preference.StringPreference;
import io.divide.client.android.AndroidConfig;
import io.divide.client.android.AndroidModule;

public class AndroidDebugConfig extends AndroidConfig {

    private static String ENDPOINT_TAG = "endpointMode";
    private static String PREF_NAME = "endpoint_pref";
    private static String defaultModule = ModuleType.Prod.name();
    private StringPreference endpointMode;
    private String prodUrl;
    private String devUrl;

    public static enum ModuleType {
        Prod,
        Dev,
        Mock;

        public static <AModule extends AndroidModule> Class<AModule> getModule(ModuleType type){
            switch (type){
                case Prod: return (Class<AModule>) AndroidModule.class;
                case Dev: return (Class<AModule>) AndroidModule.class;
                case Mock: return (Class<AModule>) MockAndroidModule.class;
            }
            return null;
        }
    }

    public AndroidDebugConfig(android.app.Application application, String prodUrl, String devURL) {
        super(application, getCurrentUrl(application,prodUrl,devURL), getCurrentModule(application));

        this.prodUrl = prodUrl;
        this.devUrl = devURL;

        endpointMode = new StringPreference(getMockPreferences(application),ENDPOINT_TAG,defaultModule);
    }

    public void setModuleType(ModuleType type){
        endpointMode.set(type.name());
        this.setModule(ModuleType.getModule(type));
        serverUrl = getCurrentUrl(endpointMode,prodUrl,devUrl);
    }

    public String getCurrentModuleType(){
        return endpointMode.get();
    }

    public String getProdUrl() {
        return prodUrl;
    }

    public String getDevUrl() {
        return devUrl;
    }

    private static String getCurrentUrl(Context context, String prodUrl, String devUrl){
        StringPreference endpointMode = new StringPreference(getMockPreferences(context),ENDPOINT_TAG,defaultModule);
        return getCurrentUrl(endpointMode,prodUrl,devUrl);
    }

    private static String getCurrentUrl(StringPreference endpointMode, String prodUrl, String devUrl){
        ModuleType endpoint = ModuleType.valueOf(endpointMode.get());
        switch (endpoint){
            case Prod: return prodUrl;
            case Dev:  return devUrl;
            case Mock: return "Mock";
            default: return "default";
        }
    }

    private static Class<AndroidModule> getCurrentModule(Context context){
        StringPreference mockMode = new StringPreference(getMockPreferences(context),ENDPOINT_TAG, defaultModule);
        return getCurrentModule(mockMode);
    }

    private static Class<AndroidModule> getCurrentModule(StringPreference modulePref){
        return ModuleType.getModule(ModuleType.valueOf(modulePref.get()));
    }

    private static SharedPreferences getMockPreferences(Context context){
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
}
