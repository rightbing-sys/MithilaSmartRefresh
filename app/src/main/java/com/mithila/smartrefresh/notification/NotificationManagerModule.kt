package com.mithila.smartrefresh.notification

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mithila.smartrefresh.ui.theme.*

object NotificationStore {
    val activeNotifications = mutableStateListOf<Pair<String, String>>()
    var serviceInstance: SmartNotificationListener? = null
}

class SmartNotificationListener : NotificationListenerService() {
    override fun onListenerConnected() {
        NotificationStore.serviceInstance = this
        refreshList()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        refreshList()
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        refreshList()
    }

    fun refreshList() {
        try {
            val list = activeNotifications?.map { sbn ->
                val title = sbn.notification.extras.getString("android.title") ?: sbn.packageName
                val text = sbn.notification.extras.getCharSequence("android.text")?.toString() ?: ""
                title to text
            } ?: emptyList()
            NotificationStore.activeNotifications.clear()
            NotificationStore.activeNotifications.addAll(list.take(15))
        } catch (_: Exception) {}
    }

    fun clearAllSmart() {
        try {
            cancelAllNotifications()
            NotificationStore.activeNotifications.clear()
        } catch (_: Exception) {}
    }
}

fun isNotificationServiceEnabled(context: Context): Boolean {
    val cn = ComponentName(context, SmartNotificationListener::class.java)
    val flat = Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")

cat ~/Mithila-4K-Video-Player-Birpur/settings.gradle.kts ~/Mithila-4K-Video-Player-Birpur/build.gradle.kts ~/Mithila-4K-Video-Player-Birpur/app/build.gradle.kts

cat << 'EOF' > setup_project.sh
#!/bin/bash
echo "=== Setting up MithilaSmartRefresh ==="

# 1. 8 फीचर्स के फोल्डर और .github डायरेक्टरी बनाना
mkdir -p .github/workflows app/src/main/java/com/mithila/smartrefresh/{dashboard,refresh,upicheck,notification,storage,battery,monitor,report} app/src/main/res/values

# 2. GitHub Actions Workflow (.github/workflows/build.yml) बनाना
cat << 'WORK' > .github/workflows/build.yml
name: Build APK
on:
  push:
    branches: [ main ]
  workflow_dispatch:
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
      - uses: gradle/actions/setup-gradle@v4
        with:
          gradle-version: '8.9'
      - run: chmod +x gradlew || true
      - run: gradle assembleDebug
      - uses: actions/upload-artifact@v4
        with:
          name: MithilaSmartRefresh-APK
          path: app/build/outputs/apk/
WORK

# 3. settings.gradle.kts बनाना (Mithila-4K-Video-Player-Birpur की तरह)
cat << 'GRADLE' > settings.gradle.kts
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "MithilaSmartRefresh"
include(":app")
GRADLE

# 4. Root build.gradle.kts बनाना
cat << 'BUILD' > build.gradle.kts
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}
BUILD

# 5. AndroidManifest.xml बनाना (UPI Check, Notification, Background app monitor आदि सभी के permissions के साथ)
cat << 'MANIFEST' > app/src/main/AndroidManifest.xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.mithila.smartrefresh">
    <uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.BIND_NOTIFICATION_LISTENER_SERVICE" />
    <uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.PACKAGE_USAGE_STATS" />
    <application
        android:label="Mithila Smart Refresh"
        android:theme="@style/Theme.Material3.Dark">
        <activity android:name=".MainActivity" android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>
MANIFEST

echo "=== setup completed successfully! ==="
