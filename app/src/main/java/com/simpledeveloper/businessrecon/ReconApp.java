package com.simpledeveloper.businessrecon;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ReconApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(config);

        /*Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());*/

    }
}
