package com.mithila.smartrefresh.battery

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mithila.smartrefresh.ui.theme.*
import kotlinx.coroutines.delay

data class DeviceVitals(
    val batteryPct: Int,
    val tempCelsius: Float,
    val voltageMv: Int,
    val isCharging: Boolean,
    val healthText: String,
    val totalRamGb: Float,
    val usedRamGb: Float,
    val ramUsagePct: Int
)

fun getDeviceVitals(context: Context): DeviceVitals {
    val bm = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
    val pct = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    val temp = (intent?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 300) ?: 300) / 10f
    val voltage = intent?.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 3800) ?: 3800
    val status = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
    val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL
    val health = when (intent?.getIntExtra(BatteryManager.EXTRA_HEALTH, 0)) {
        BatteryManager.BATTERY_HEALTH_GOOD -> "Good (स्वस्थ)"
        BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Overheating (गर्म)"
        else -> "Normal"
    }

    val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val memInfo = ActivityManager.MemoryInfo()
    am.getMemoryInfo(memInfo)
    val totalGb = memInfo.totalMem / (1024f * 1024f * 1024f)
    val availGb = memInfo.availMem / (1024f * 1024f * 1024f)
    val usedGb = (totalGb - availGb).coerceAtLeast(0.1f)
    val ramPct = ((usedGb / totalGb) * 100).toInt().coerceIn(1, 100)

    return DeviceVitals(pct, temp, voltage, isCharging, health, totalGb, usedGb, ramPct)
}

@Composable
fun BatteryRamScreen() {
    val context = LocalContext.current
    var vitals by remember { mutableStateOf(getDeviceVitals(context)) }

    LaunchedEffect(Unit) {
        while (true) {
            vitals = getDeviceVitals(context)
            delay(2000)
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(
            colors = CardDefaults.cardColors(containerColor = CardDark),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Memory, contentDescription = null, tint = ElectricTeal)
                    Spacer(Modifier.width(8.dp))
                    Text("Live RAM Monitor", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)
                }
                Text(
                    String.format("Used: %.2f GB / %.2f GB (%d%%)", vitals.usedRamGb, vitals.totalRamGb, vitals.ramUsagePct),
                    color = ChampagneGold, fontWeight = FontWeight.SemiBold
                )
                LinearProgressIndicator(
                    progress = { vitals.ramUsagePct / 100f },
                    modifier = Modifier.fillMaxWidth().height(10.dp),
                    color = ElectricTeal,
                    trackColor = MidnightCharcoal
                )
            }
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = CardDark),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.BatteryChargingFull, contentDescription = null, tint = SuccessGreen)
                    Spacer(Modifier.width(8.dp))
                    Text("Battery Diagnostics", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)
                }
                Text("Charge Level: ${vitals.batteryPct}\%${if (vitals.isCharging) "(Charging ⚡)" else ""}", color = TextPrimary)
                Text("Temperature: ${vitals.tempCelsius}°C", color = if (vitals.tempCelsius > 40f) DangerRed else ElectricTeal)
                Text("Voltage: ${vitals.voltageMv} mV", color = TextSecondary)
                Text("Battery Health: ${vitals.healthText}", color = ChampagneGold)
            }
        }
    }
}
