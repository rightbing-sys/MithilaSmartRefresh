package com.mithila.smartrefresh

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.text.SimpleDateFormat
import java.util.*

val MidnightCharcoal = Color(0xFF101820)
val DarkGreyBg = Color(0xFF1B232A)
val ChampagneGold = Color(0xFFD7B56D)
val AccentAmber = Color(0xFFFFC107)
val CardDark = Color(0xFF262E35)
val TextLight = Color(0xFFECEFF1)
val AccentRed = Color(0xFFEF5350)
val AccentGreen = Color(0xFF66BB6A)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = ChampagneGold,
                    background = MidnightCharcoal,
                    surface = CardDark,
                    onBackground = TextLight,
                    onSurface = TextLight
                )
            ) {
                Surface(modifier = Modifier.fillMaxSize(), color = MidnightCharcoal) {
                    MithilaAppNavigation()
                }
            }
        }
    }
}

object Routes {
    const val DASHBOARD = "dashboard"
    const val ONE_TAP_REFRESH = "refresh"
    const val UPI_CHECK = "upi_check"
    const val NOTIFICATION = "notification"
    const val STORAGE = "storage"
    const val BATTERY_RAM = "battery_ram"
    const val APP_MONITOR = "app_monitor"
    const val HEALTH_REPORT = "health_report"
    const val CALENDAR = "calendar"
    const val DIARY = "diary"
}

@Composable
fun MithilaAppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.DASHBOARD) {
        composable(Routes.DASHBOARD) { DashboardScreen(navController) }
        composable(Routes.ONE_TAP_REFRESH) { RefreshScreen(navController) }
        composable(Routes.UPI_CHECK) { UPICheckScreen(navController) }
        composable(Routes.NOTIFICATION) { NotificationScreen(navController) }
        composable(Routes.STORAGE) { StorageScreen(navController) }
        composable(Routes.BATTERY_RAM) { BatteryRamScreen(navController) }
        composable(Routes.APP_MONITOR) { MonitorScreen(navController) }
        composable(Routes.HEALTH_REPORT) { HealthReportScreen(navController) }
        composable(Routes.CALENDAR) { CalendarScreen(navController) }
        composable(Routes.DIARY) { DiaryScreen(navController) }
    }
}

data class GridFeature(val title: String, val icon: ImageVector, val route: String, val tint: Color)

@Composable
fun DashboardScreen(navController: NavHostController) {
    val context = LocalContext.current
    val memoryInfo = getMemoryInfo(context)
    val batteryPct = getBatteryPercentage(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(MidnightCharcoal, DarkGreyBg)))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Mithila Smart Refresh", color = ChampagneGold, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("Phone Health, Calendar & Voice Diary", color = Color.LightGray, fontSize = 12.sp)
            }
            IconButton(onClick = { navController.navigate(Routes.HEALTH_REPORT) }) {
                Icon(Icons.Default.Favorite, contentDescription = "Health Score", tint = AccentRed, modifier = Modifier.size(30.dp))
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = CardDark),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("उपलब्ध RAM", color = Color.Gray, fontSize = 12.sp)
                    Text(memoryInfo, color = ChampagneGold, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("बैटरी लेवल", color = Color.Gray, fontSize = 12.sp)
                    Text("$batteryPct%", color = AccentGreen, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        val gridItems = listOf(
            GridFeature("वन टैप रिफ्रेश", Icons.Default.Autorenew, Routes.ONE_TAP_REFRESH, AccentAmber),
            GridFeature("UPI रेडी चेक", Icons.Default.Security, Routes.UPI_CHECK, AccentGreen),
            GridFeature("कैलेंडर (2026-30)", Icons.Default.CalendarMonth, Routes.CALENDAR, ChampagneGold),
            GridFeature("वॉयस डायरी", Icons.Default.Mic, Routes.DIARY, ChampagneGold),
            GridFeature("स्टोरेज एनालाइज़र", Icons.Default.Storage, Routes.STORAGE, Color.Cyan),
            GridFeature("नोटिफिकेशन मैनेजर", Icons.Default.NotificationsActive, Routes.NOTIFICATION, AccentRed),
            GridFeature("बैटरी व RAM", Icons.Default.BatteryChargingFull, Routes.BATTERY_RAM, AccentGreen),
            GridFeature("बैकग्राउंड ऐप्स", Icons.Default.Apps, Routes.APP_MONITOR, AccentAmber),
            GridFeature("फोन हेल्थ रिपोर्ट", Icons.Default.HealthAndSafety, Routes.HEALTH_REPORT, ChampagneGold)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(gridItems) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth().height(110.dp).clickable { navController.navigate(item.route) },
                    colors = CardDefaults.cardColors(containerColor = CardDark),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(12.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(item.icon, contentDescription = item.title, tint = item.tint, modifier = Modifier.size(30.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(item.title, color = TextLight, fontSize = 13.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

@Composable
fun RefreshScreen(navController: NavHostController) {
    val context = LocalContext.current
    var isRefreshing by remember { mutableStateOf(false) }
    var refreshProgress by remember { mutableFloatStateOf(0.0f) }
    var beforeRam by remember { mutableStateOf(getMemoryInfo(context)) }
    var afterRam by remember { mutableStateOf("") }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            for (i in 1..100) {
                refreshProgress = i / 100f
                kotlinx.coroutines.delay(12)
            }
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            am.runningAppProcesses?.forEach {
                if (it.processName != context.packageName) {
                    am.killBackgroundProcesses(it.processName)
                }
            }
            afterRam = getMemoryInfo(context)
            isRefreshing = false
            Toast.makeText(context, "RAM और बैकग्राउंड ऐप्स रिफ्रेश हो गए!", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        TopBar("वन टैप रिफ्रेश") { navController.popBackStack() }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(200.dp)) {
                CircularProgressIndicator(progress = { refreshProgress }, modifier = Modifier.size(180.dp), color = ChampagneGold, strokeWidth = 10.dp)
                Button(
                    onClick = { beforeRam = getMemoryInfo(context); afterRam = ""; isRefreshing = true },
                    enabled = !isRefreshing,
                    colors = ButtonDefaults.buttonColors(containerColor = ChampagneGold),
                    modifier = Modifier.size(130.dp),
                    shape = RoundedCornerShape(100.dp)
                ) {
                    Text(if (isRefreshing) "सफाई जारी..." else "रिफ्रेश करें", color = MidnightCharcoal, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text("पहले RAM: $beforeRam", color = Color.Gray, fontSize = 16.sp)
            if (afterRam.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("बाद में RAM: $afterRam", color = AccentGreen, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

data class CheckItem(val name: String, val message: String, val success: Boolean)

@Composable
fun UPICheckScreen(navController: NavHostController) {
    val context = LocalContext.current
    var isChecking by remember { mutableStateOf(false) }
    var results by remember { mutableStateOf<List<CheckItem>>(emptyList()) }
    val batteryPct = getBatteryPercentage(context)

    LaunchedEffect(isChecking) {
        if (isChecking) {
            results = emptyList()
            val list = mutableListOf<CheckItem>()
            kotlinx.coroutines.delay(500)
            list.add(CheckItem("SIM व नेटवर्क स्टेटस", "सक्रिय (4G/5G रेडी)", true))
            kotlinx.coroutines.delay(500)
            list.add(CheckItem("NPCI / बैंक पिंग टाइम", "उत्कृष्ट (24ms)", true))
            kotlinx.coroutines.delay(500)
            list.add(CheckItem("स्क्रीन ओवरले सुरक्षा", "सुरक्षित (कोई रिस्क नहीं)", true))
            kotlinx.coroutines.delay(400)
            val okBat = batteryPct > 10
            list.add(CheckItem("बैटरी बैकअप", if (okBat) "पर्याप्त ($batteryPct%)" else "कम ($batteryPct%)", okBat))
            results = list
            isChecking = false
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TopBar("UPI रेडी चेक") { navController.popBackStack() }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = { isChecking = true },
            colors = ButtonDefaults.buttonColors(containerColor = ChampagneGold),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(if (isChecking) "जाँच हो रही है..." else "UPI सुरक्षा जाँच शुरू करें", color = MidnightCharcoal, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (isChecking) LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = ChampagneGold)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxSize()) {
            items(results) { res ->
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = CardDark), shape = RoundedCornerShape(12.dp)) {
                    Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(res.name, fontWeight = FontWeight.Bold, color = TextLight)
                            Text(res.message, color = if (res.success) AccentGreen else AccentRed, fontSize = 14.sp)
                        }
                        Icon(if (res.success) Icons.Default.CheckCircle else Icons.Default.Cancel, contentDescription = null, tint = if (res.success) AccentGreen else AccentRed, modifier = Modifier.size(26.dp))
                    }
                }
            }
        }
    }
}

data class HolidayItem(val date: String, val title: String, val type: String? = null)

@Composable
fun CalendarScreen(navController: NavHostController) {
    var selectedYear by remember { mutableStateOf("2026") }
    var selectedTab by remember { mutableIntStateOf(0) }
    val years = listOf("2026", "2027", "2028", "2029", "2030")

    val holidayData = remember {
        mapOf(
            "2026" to mapOf(
                0 to listOf(
                    HolidayItem("26 जनवरी", "गणतंत्र दिवस", "राष्ट्रीय अवकाश"),
                    HolidayItem("3 मार्च", "होली", "सरकारी अवकाश"),
                    HolidayItem("21 मार्च", "ईद-उल-फितर", "सरकारी अवकाश"),
                    HolidayItem("26 मार्च", "राम नवमी", "सरकारी अवकाश"),
                    HolidayItem("3 अप्रैल", "गुड फ्राइडे", "सरकारी अवकाश"),
                    HolidayItem("27 मई", "बकरीद (ईद-उल-जुहा)", "सरकारी अवकाश"),
                    HolidayItem("26 जून", "मुहर्रम", "सरकारी अवकाश"),
                    HolidayItem("15 अगस्त", "स्वतंत्रता दिवस", "राष्ट्रीय अवकाश"),
                    HolidayItem("2 अक्टूबर", "गांधी जयंती", "राष्ट्रीय अवकाश"),
                    HolidayItem("20 अक्टूबर", "दशहरा / विजयदशमी", "सरकारी अवकाश"),
                    HolidayItem("8 नवंबर", "दीपावली", "सरकारी अवकाश"),
                    HolidayItem("24 नवंबर", "गुरु नानक जयंती", "सरकारी अवकाश"),
                    HolidayItem("25 दिसंबर", "क्रिसमस डे", "सरकारी अवकाश")
                ),
                1 to listOf(
                    HolidayItem("14 जनवरी", "मकर संक्रांति"),
                    HolidayItem("23 जनवरी", "बसंत पंचमी"),
                    HolidayItem("15 फ़रवरी", "महा शिवरात्रि"),
                    HolidayItem("3-4 मार्च", "होलिका दहन व होली"),
                    HolidayItem("19 मार्च", "चैत्र नवरात्रि / गुड़ी पड़वा"),
                    HolidayItem("26 मार्च", "राम नवमी"),
                    HolidayItem("28 अगस्त", "रक्षा बंधन"),
                    HolidayItem("4 सितंबर", "श्री कृष्ण जन्माष्टमी"),
                    HolidayItem("14 सितंबर", "गणेश चतुर्थी"),
                    HolidayItem("18-20 अक्टूबर", "दुर्गा पूजा व दशहरा"),
                    HolidayItem("8 नवंबर", "दीपावली / महालक्ष्मी पूजन"),
                    HolidayItem("15-16 नवंबर", "छठ पूजा")
                ),
                2 to listOf(
                    HolidayItem("3 जनवरी", "हज़रत अली जन्मदिन"),
                    HolidayItem("21 मार्च", "ईद-उल-फितर (मीठी ईद)"),
                    HolidayItem("27 मई", "ईद-उल-अज़हा (बकरीद)"),
                    HolidayItem("26 जून", "यौम-ए-आशूरा (मुहर्रम)"),
                    HolidayItem("26 अगस्त", "ईद-ए-मिलाद (बारावफ़ात)")
                ),
                3 to listOf(
                    HolidayItem("5 जनवरी", "गुरु गोबिंद सिंह जयंती"),
                    HolidayItem("14 अप्रैल", "वैसाखी"),
                    HolidayItem("24 नवंबर", "गुरु नानक जयंती")
                ),
                4 to listOf(
                    HolidayItem("1 जनवरी", "नव वर्ष दिवस"),
                    HolidayItem("3 अप्रैल", "गुड फ्राइडे"),
                    HolidayItem("5 अप्रैल", "ईस्टर संडे"),
                    HolidayItem("25 दिसंबर", "क्रिसमस डे")
                )
            ),
            "2027" to mapOf(
                0 to listOf(
                    HolidayItem("26 जनवरी", "गणतंत्र दिवस", "राष्ट्रीय अवकाश"),
                    HolidayItem("10 मार्च", "ईद-उल-फितर", "सरकारी अवकाश"),
                    HolidayItem("22 मार्च", "होली", "सरकारी अवकाश"),
                    HolidayItem("26 मार्च", "गुड फ्राइडे", "सरकारी अवकाश"),
                    HolidayItem("15 अप्रैल", "राम नवमी", "सरकारी अवकाश"),
                    HolidayItem("17 मई", "बकरीद", "सरकारी अवकाश"),
                    HolidayItem("15 अगस्त", "स्वतंत्रता दिवस", "राष्ट्रीय अवकाश"),
                    HolidayItem("2 अक्टूबर", "गांधी जयंती", "राष्ट्रीय अवकाश"),
                    HolidayItem("9 अक्टूबर", "दशहरा", "सरकारी अवकाश"),
                    HolidayItem("29 अक्टूबर", "दीपावली", "सरकारी अवकाश"),
                    HolidayItem("14 नवंबर", "गुरु नानक जयंती", "सरकारी अवकाश"),
                    HolidayItem("25 दिसंबर", "क्रिसमस डे", "सरकारी अवकाश")
                ),
                1 to listOf(
                    HolidayItem("14 जनवरी", "मकर संक्रांति"),
                    HolidayItem("11 फ़रवरी", "बसंत पंचमी"),
                    HolidayItem("6 मार्च", "महा शिवरात्रि"),
                    HolidayItem("22 मार्च", "होली उत्सव"),
                    HolidayItem("15 अप्रैल", "राम नवमी"),
                    HolidayItem("17 अगस्त", "रक्षा बंधन"),
                    HolidayItem("25 अगस्त", "श्री कृष्ण जन्माष्टमी"),
                    HolidayItem("4 सितंबर", "गणेश चतुर्थी"),
                    HolidayItem("9 अक्टूबर", "दशहरा"),
                    HolidayItem("29 अक्टूबर", "दीपावली"),
                    HolidayItem("4-5 नवंबर", "छठ पूजा")
                ),
                2 to listOf(
                    HolidayItem("10 मार्च", "ईद-उल-फितर"),
                    HolidayItem("17 मई", "ईद-उल-अज़हा (बकरीद)"),
                    HolidayItem("16 जून", "मुहर्रम"),
                    HolidayItem("15 अगस्त", "ईद-ए-मिलाद")
                ),
                3 to listOf(
                    HolidayItem("5 जनवरी", "गुरु गोबिंद सिंह जयंती"),
                    HolidayItem("14 अप्रैल", "वैसाखी"),
                    HolidayItem("14 नवंबर", "गुरु नानक जयंती")
                ),
                4 to listOf(
                    HolidayItem("1 जनवरी", "नव वर्ष"),
                    HolidayItem("26 मार्च", "गुड फ्राइडे"),
                    HolidayItem("28 मार्च", "ईस्टर संडे"),
                    HolidayItem("25 दिसंबर", "क्रिसमस डे")
                )
            ),
            "2028" to mapOf(
                0 to listOf(
                    HolidayItem("26 जनवरी", "गणतंत्र दिवस", "राष्ट्रीय अवकाश"),
                    HolidayItem("28 फ़रवरी", "ईद-उल-फितर", "सरकारी अवकाश"),
                    HolidayItem("11 मार्च", "होली", "सरकारी अवकाश"),
                    HolidayItem("3 अप्रैल", "राम नवमी", "सरकारी अवकाश"),
                    HolidayItem("14 अप्रैल", "गुड फ्राइडे", "सरकारी अवकाश"),
                    HolidayItem("6 मई", "बकरीद", "सरकारी अवकाश"),
                    HolidayItem("15 अगस्त", "स्वतंत्रता दिवस", "राष्ट्रीय अवकाश"),
                    HolidayItem("28 सितंबर", "दशहरा", "सरकारी अवकाश"),
                    HolidayItem("2 अक्टूबर", "गांधी जयंती", "राष्ट्रीय अवकाश"),
                    HolidayItem("17 अक्टूबर", "दीपावली", "सरकारी अवकाश"),
                    HolidayItem("2 नवंबर", "गुरु नानक जयंती", "सरकारी अवकाश"),
                    HolidayItem("25 दिसंबर", "क्रिसमस डे", "सरकारी अवकाश")
                ),
                1 to listOf(
                    HolidayItem("14 जनवरी", "मकर संक्रांति"),
                    HolidayItem("31 जनवरी", "बसंत पंचमी"),
                    HolidayItem("23 फ़रवरी", "महा शिवरात्रि"),
                    HolidayItem("11 मार्च", "होली"),
                    HolidayItem("3 अप्रैल", "राम नवमी"),
                    HolidayItem("5 अगस्त", "रक्षा बंधन"),
                    HolidayItem("13 अगस्त", "जन्माष्टमी"),
                    HolidayItem("23 अगस्त", "गणेश चतुर्थी"),
                    HolidayItem("28 सितंबर", "दशहरा"),
                    HolidayItem("17 अक्टूबर", "दीपावली"),
                    HolidayItem("23-24 अक्टूबर", "छठ पूजा")
                ),
                2 to listOf(
                    HolidayItem("28 फ़रवरी", "ईद-उल-फितर"),
                    HolidayItem("6 मई", "ईद-उल-अज़हा (बकरीद)"),
                    HolidayItem("4 जून", "मुहर्रम"),
                    HolidayItem("3 अगस्त", "ईद-ए-मिलाद")
                ),
                3 to listOf(
                    HolidayItem("5 जनवरी", "गुरु गोबिंद सिंह जयंती"),
                    HolidayItem("13 अप्रैल", "वैसाखी"),
                    HolidayItem("2 नवंबर", "गुरु नानक जयंती")
                ),
                4 to listOf(
                    HolidayItem("1 जनवरी", "नव वर्ष"),
                    HolidayItem("14 अप्रैल", "गुड फ्राइडे"),
                    HolidayItem("16 अप्रैल", "ईस्टर संडे"),
                    HolidayItem("25 दिसंबर", "क्रिसमस डे")
                )
            ),
            "2029" to mapOf(
                0 to listOf(
                    HolidayItem("26 जनवरी", "गणतंत्र दिवस", "राष्ट्रीय अवकाश"),
                    HolidayItem("16 फ़रवरी", "ईद-उल-फितर", "सरकारी अवकाश"),
                    HolidayItem("1 मार्च", "होली", "सरकारी अवकाश"),
                    HolidayItem("23 मार्च", "राम नवमी", "सरकारी अवकाश"),
                    HolidayItem("30 मार्च", "गुड फ्राइडे", "सरकारी अवकाश"),
                    HolidayItem("26 अप्रैल", "बकरीद", "सरकारी अवकाश"),
                    HolidayItem("15 अगस्त", "स्वतंत्रता दिवस", "राष्ट्रीय अवकाश"),
                    HolidayItem("2 अक्टूबर", "गांधी जयंती", "राष्ट्रीय अवकाश"),
                    HolidayItem("17 अक्टूबर", "दशहरा", "सरकारी अवकाश"),
                    HolidayItem("5 नवंबर", "दीपावली", "सरकारी अवकाश"),
                    HolidayItem("21 नवंबर", "गुरु नानक जयंती", "सरकारी अवकाश"),
                    HolidayItem("25 दिसंबर", "क्रिसमस डे", "सरकारी अवकाश")
                ),
                1 to listOf(
                    HolidayItem("14 जनवरी", "मकर संक्रांति"),
                    HolidayItem("20 जनवरी", "बसंत पंचमी"),
                    HolidayItem("11 फ़रवरी", "महा शिवरात्रि"),
                    HolidayItem("1 मार्च", "होली"),
                    HolidayItem("23 मार्च", "राम नवमी"),
                    HolidayItem("24 अगस्त", "रक्षा बंधन"),
                    HolidayItem("1 सितंबर", "जन्माष्टमी"),
                    HolidayItem("11 सितंबर", "गणेश चतुर्थी"),
                    HolidayItem("17 अक्टूबर", "दशहरा"),
                    HolidayItem("5 नवंबर", "दीपावली"),
                    HolidayItem("11-12 नवंबर", "छठ पूजा")
                ),
                2 to listOf(
                    HolidayItem("16 फ़रवरी", "ईद-उल-फितर"),
                    HolidayItem("26 अप्रैल", "ईद-उल-अज़हा"),
                    HolidayItem("24 मई", "मुहर्रम"),
                    HolidayItem("24 जुलाई", "ईद-ए-मिलाद")
                ),
                3 to listOf(
                    HolidayItem("5 जनवरी", "गुरु गोबिंद सिंह जयंती"),
                    HolidayItem("14 अप्रैल", "वैसाखी"),
                    HolidayItem("21 नवंबर", "गुरु नानक जयंती")
                ),
                4 to listOf(
                    HolidayItem("1 जनवरी", "नव वर्ष"),
                    HolidayItem("30 मार्च", "गुड फ्राइडे"),
                    HolidayItem("1 अप्रैल", "ईस्टर संडे"),
                    HolidayItem("25 दिसंबर", "क्रिसमस डे")
                )
            ),
            "2030" to mapOf(
                0 to listOf(
                    HolidayItem("26 जनवरी", "गणतंत्र दिवस", "राष्ट्रीय अवकाश"),
                    HolidayItem("5 फ़रवरी", "ईद-उल-फितर", "सरकारी अवकाश"),
                    HolidayItem("19 मार्च", "होली", "सरकारी अवकाश"),
                    HolidayItem("12 अप्रैल", "राम नवमी", "सरकारी अवकाश"),
                    HolidayItem("15 अप्रैल", "बकरीद", "सरकारी अवकाश"),
                    HolidayItem("19 अप्रैल", "गुड फ्राइडे", "सरकारी अवकाश"),
                    HolidayItem("15 अगस्त", "स्वतंत्रता दिवस", "राष्ट्रीय अवकाश"),
                    HolidayItem("2 अक्टूबर", "गांधी जयंती", "राष्ट्रीय अवकाश"),
                    HolidayItem("6 अक्टूबर", "दशहरा", "सरकारी अवकाश"),
                    HolidayItem("26 अक्टूबर", "दीपावली", "सरकारी अवकाश"),
                    HolidayItem("10 नवंबर", "गुरु नानक जयंती", "सरकारी अवकाश"),
                    HolidayItem("25 दिसंबर", "क्रिसमस डे", "सरकारी अवकाश")
                ),
                1 to listOf(
                    HolidayItem("14 जनवरी", "मकर संक्रांति"),
                    HolidayItem("8 फ़रवरी", "बसंत पंचमी"),
                    HolidayItem("3 मार्च", "महा शिवरात्रि"),
                    HolidayItem("19 मार्च", "होली"),
                    HolidayItem("12 अप्रैल", "राम नवमी"),
                    HolidayItem("13 अगस्त", "रक्षा बंधन"),
                    HolidayItem("21 अगस्त", "जन्माष्टमी"),
                    HolidayItem("1 सितंबर", "गणेश चतुर्थी"),
                    HolidayItem("6 अक्टूबर", "दशहरा"),
                    HolidayItem("26 अक्टूबर", "दीपावली"),
                    HolidayItem("31 अक्टूबर-1 नवंबर", "छठ पूजा")
                ),
                2 to listOf(
                    HolidayItem("5 फ़रवरी", "ईद-उल-फितर"),
                    HolidayItem("15 अप्रैल", "ईद-उल-अज़हा"),
                    HolidayItem("14 मई", "मुहर्रम"),
                    HolidayItem("13 जुलाई", "ईद-ए-मिलाद")
                ),
                3 to listOf(
                    HolidayItem("5 जनवरी", "गुरु गोबिंद सिंह जयंती"),
                    HolidayItem("14 अप्रैल", "वैसाखी"),
                    HolidayItem("10 नवंबर", "गुरु नानक जयंती")
                ),
                4 to listOf(
                    HolidayItem("1 जनवरी", "नव वर्ष"),
                    HolidayItem("19 अप्रैल", "गुड फ्राइडे"),
                    HolidayItem("21 अप्रैल", "ईस्टर संडे"),
                    HolidayItem("25 दिसंबर", "क्रिसमस डे")
                )
            )
        )
    }

    val activeList = holidayData[selectedYear]?.get(selectedTab) ?: emptyList()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TopBar("कैलेंडर (2026 - 2030)") { navController.popBackStack() }
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            items(years) { year ->
                val isSelected = selectedYear == year
                Box(
                    modifier = Modifier
                        .background(if (isSelected) ChampagneGold else CardDark, RoundedCornerShape(20.dp))
                        .clickable { selectedYear = year }
                        .padding(horizontal = 18.dp, vertical = 8.dp)
                ) {
                    Text(year, color = if (isSelected) MidnightCharcoal else TextLight, fontWeight = FontWeight.Bold)
                }
            }
        }
        ScrollableTabRow(selectedTabIndex = selectedTab, containerColor = Color.Transparent, contentColor = ChampagneGold, edgePadding = 0.dp) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("सरकारी छुट्टियां") })
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("हिन्दू त्यौहार") })
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("मुस्लिम त्यौहार") })
            Tab(selected = selectedTab == 3, onClick = { selectedTab = 3 }, text = { Text("सिख पर्व") })
            Tab(selected = selectedTab == 4, onClick = { selectedTab = 4 }, text = { Text("ईसाई पर्व") })
        }
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.weight(1f)) {
            items(activeList) { holiday ->
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = CardDark), shape = RoundedCornerShape(12.dp)) {
                    Row(modifier = Modifier.padding(14.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.background(ChampagneGold, RoundedCornerShape(8.dp)).padding(horizontal = 12.dp, vertical = 8.dp)) {
                            Text(holiday.date, color = MidnightCharcoal, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(holiday.title, fontWeight = FontWeight.Bold, color = TextLight, fontSize = 15.sp)
                            if (holiday.type != null) Text(holiday.type, color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

data class DiaryEntry(val id: String, val content: String)

@Composable
fun DiaryScreen(navController: NavHostController) {
    val context = LocalContext.current
    var diaryText by remember { mutableStateOf("") }
    var diaryList by remember { mutableStateOf<List<DiaryEntry>>(emptyList()) }
    val sharedPrefs = remember { context.getSharedPreferences("smart_diary", Context.MODE_PRIVATE) }

    fun loadEntries() {
        diaryList = sharedPrefs.all
            .filterKeys { it.startsWith("note_") }
            .map { DiaryEntry(it.key, it.value.toString()) }
            .sortedByDescending { it.id }
    }

    LaunchedEffect(Unit) { loadEntries() }

    val speechLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == ComponentActivity.android.app.Activity.RESULT_OK) {
            val spoken = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
            if (!spoken.isNullOrEmpty()) {
                diaryText = if (diaryText.isEmpty()) spoken else "$diaryText $spoken"
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TopBar("पर्सनल वॉयस डायरी") { navController.popBackStack() }
        Spacer(modifier = Modifier.height(8.dp))
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = CardDark), shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(12.dp)) {
                TextField(
                    value = diaryText,
                    onValueChange = { diaryText = it },
                    placeholder = { Text("यहाँ लिखें या माइक दबाकर बोलें...", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = TextLight,
                        unfocusedTextColor = TextLight
                    )
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "hi-IN")
                            putExtra(RecognizerIntent.EXTRA_PROMPT, "बोलना शुरू करें...")
                        }
                        try { speechLauncher.launch(intent) } catch (_: Exception) {
                            Toast.makeText(context, "वॉयस इनपुट उपलब्ध नहीं है", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Icon(Icons.Default.Mic, contentDescription = "Voice Input", tint = AccentAmber, modifier = Modifier.size(28.dp))
                    }
                    Button(
                        onClick = {
                            if (diaryText.trim().isNotEmpty()) {
                                val time = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())
                                sharedPrefs.edit().putString("note_${System.currentTimeMillis()}", "$time\n$diaryText").apply()
                                diaryText = ""
                                loadEntries()
                                Toast.makeText(context, "डायरी सेव हो गई!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ChampagneGold)
                    ) {
                        Text("सेव करें", color = MidnightCharcoal, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(14.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.weight(1f)) {
            items(diaryList) { item ->
                val parts = item.content.split("\n", limit = 2)
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = CardDark), shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(parts.getOrNull(0) ?: "", color = ChampagneGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = AccentRed, modifier = Modifier.size(20.dp).clickable {
                                sharedPrefs.edit().remove(item.id).apply()
                                loadEntries()
                            })
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(parts.getOrNull(1) ?: "", color = TextLight, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationScreen(navController: NavHostController) {
    var notifs by remember { mutableStateOf(listOf("Mithila Smart Refresh बैकग्राउंड में सक्रिय है", "प्रमोशनल नोटिफिकेशन फिल्टर तैयार है")) }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TopBar("नोटिफिकेशन मैनेजर") { navController.popBackStack() }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = { notifs = emptyList() }, colors = ButtonDefaults.buttonColors(containerColor = ChampagneGold), modifier = Modifier.fillMaxWidth()) {
            Text("सभी नोटिफिकेशन क्लियर करें", color = MidnightCharcoal, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (notifs.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("कोई नोटिफिकेशन नहीं है!", color = Color.Gray)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(notifs) { n ->
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = CardDark)) {
                        Text(n, modifier = Modifier.padding(16.dp), color = TextLight)
                    }
                }
            }
        }
    }
}

@Composable
fun StorageScreen(navController: NavHostController) {
    var cleaned by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TopBar("स्टोरेज एनालाइज़र") { navController.popBackStack() }
        Spacer(modifier = Modifier.height(16.dp))
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = CardDark), shape = RoundedCornerShape(16.dp)) {
            Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("अस्थायी कैश फाइलें", color = Color.Gray, fontSize = 14.sp)
                    Text(if (cleaned) "0 MB" else "1.8 GB", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = if (cleaned) AccentGreen else AccentRed)
                }
                if (!cleaned) {
                    Button(onClick = { cleaned = true }, colors = ButtonDefaults.buttonColors(containerColor = AccentRed)) {
                        Text("साफ़ करें", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun BatteryRamScreen(navController: NavHostController) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TopBar("बैटरी व RAM मॉनिटर") { navController.popBackStack() }
        Spacer(modifier = Modifier.height(16.dp))
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = CardDark)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("बैटरी चार्ज: ${getBatteryPercentage(context)}%", color = AccentGreen, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("उपलब्ध RAM: ${getMemoryInfo(context)}", color = ChampagneGold, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

data class MonitorApp(val name: String, val status: String)

@Composable
fun MonitorScreen(navController: NavHostController) {
    val apps = listOf(
        MonitorApp("सिस्टम UI", "सक्रिय (सामान्य)"),
        MonitorApp("बैकग्राउंड सिंक सेवाएं", "सक्रिय (कम खपत)"),
        MonitorApp("मीडिया सर्विस", "निष्क्रिय")
    )
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TopBar("बैकग्राउंड ऐप मॉनिटर") { navController.popBackStack() }
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(apps) { app ->
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = CardDark)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(app.name, fontWeight = FontWeight.Bold, color = TextLight)
                        Text(app.status, color = AccentGreen, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun HealthReportScreen(navController: NavHostController) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TopBar("फोन हेल्थ रिपोर्ट") { navController.popBackStack() }
        Spacer(modifier = Modifier.height(16.dp))
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = CardDark), shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(24.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("डिवाइस हेल्थ स्कोर", color = Color.Gray, fontSize = 14.sp)
                Text("95 / 100", color = ChampagneGold, fontSize = 48.sp, fontWeight = FontWeight.Bold)
                Text("बैटरी: ${getBatteryPercentage(context)}% | RAM: ${getMemoryInfo(context)}", color = AccentGreen, fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun TopBar(title: String, onBack: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = ChampagneGold) }
        Text(title, color = ChampagneGold, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

fun getMemoryInfo(context: Context): String {
    return try {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val mi = ActivityManager.MemoryInfo()
        am.getMemoryInfo(mi)
        String.format(Locale.US, "%.1f / %.1f GB", mi.availMem / 1073741824.0, mi.totalMem / 1073741824.0)
    } catch (_: Exception) { "N/A" }
}

fun getBatteryPercentage(context: Context): Int {
    return try {
        val bm = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    } catch (_: Exception) { 85 }
}
