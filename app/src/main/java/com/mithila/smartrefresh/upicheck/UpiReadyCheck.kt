package com.mithila.smartrefresh.upicheck

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mithila.smartrefresh.battery.getDeviceVitals
import com.mithila.smartrefresh.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket

data class UpiStatus(
    val isOnline: Boolean,
    val networkType: String,
    val pingMs: Long,
    val batterySafe: Boolean,
    val isReady: Boolean
)

suspend fun checkUpiReadiness(context: Context): UpiStatus = withContext(Dispatchers.IO) {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val net = cm.activeNetwork
    val caps = cm.getNetworkCapabilities(net)
    val isOnline = caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    val netType = when {
        caps == null -> "Offline"
        caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "Wi-Fi Connected"
        caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Mobile Data (4G/5G)"
        else -> "Active Network"
    }

    var ping = -1L
    if (isOnline) {
        try {
            val start = System.currentTimeMillis()
            Socket().use { socket ->
                socket.connect(InetSocketAddress("8.8.8.8", 53), 2500)
            }
            ping = System.currentTimeMillis() - start
        } catch (_: Exception) {
            ping = 999L
        }
    }

    val batteryOk = getDeviceVitals(context).batteryPct >= 10
    val ready = isOnline && ping in 1..450 && batteryOk
    UpiStatus(isOnline, netType, ping, batteryOk, ready)
}

@Composable
fun UpiReadyScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var status by remember { mutableStateOf<UpiStatus?>(null) }
    var scanning by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        scanning = true
        status = checkUpiReadiness(context)
        scanning = false
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Security, contentDescription = null, tint = ChampagneGold)
                Spacer(Modifier.width(8.dp))
                Text("UPI Payment Ready Check", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)
            }

            if (scanning || status == null) {
                CircularProgressIndicator(color = ElectricTeal)
            } else {
                val s = status!!
                CheckRow("Internet Connection (${s.networkType})", s.isOnline)
                CheckRow("Server Latency / Ping (${if (s.pingMs > 0) "${s.pingMs} ms" else "Timeout"})", s.pingMs in 1..450)
                CheckRow("Battery Safe Level (>10%)", s.batterySafe)

                HorizontalDivider(color = MidnightCharcoal)
                Text(
                    if (s.isReady) "✅ SAFE FOR UPI PAYMENTS (भुगतान के लिए तैयार)"
                    else "⚠️ WEAK CONNECTION - UPI MAY FAIL",
                    color = if (s.isReady) SuccessGreen else DangerRed,
                    fontWeight = FontWeight.Bold
                )

                Button(
                    onClick = {
                        scope.launch {
                            scanning = true
                            status = checkUpiReadiness(context)
                            scanning = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricTeal)
                ) {
                    Text("Re-Test UPI Readiness", color = MidnightCharcoal, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun CheckRow(label: String, passed: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            if (passed) Icons.Default.CheckCircle else Icons.Default.Error,
            contentDescription = null,
            tint = if (passed) SuccessGreen else DangerRed,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(label, color = TextPrimary, fontSize = 14.sp)
    }
}
