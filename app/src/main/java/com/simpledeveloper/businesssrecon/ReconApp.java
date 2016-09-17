package com.simpledeveloper.businesssrecon;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ReconApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                .build();

        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(config);
    }
}
