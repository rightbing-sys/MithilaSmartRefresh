package com.mithila.smartrefresh

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.util.Calendar

val BannerMidnightCharcoal = Color(0xFF101820)
val BannerChampagneGold = Color(0xFFD7B56D)
val BannerElectricTeal = Color(0xFF00E5FF)
val KalnirnayRed = Color(0xFFD32F2F)
val GovtHolidayBg = Color(0xFFFFEBEE)
val PublicHolidayBg = Color(0xFFE3F2FD)

@Composable
fun MithilaFullScreenSplash(onContinue: () -> Unit) {
    val context = LocalContext.current
    val bannerResId = remember {
        context.resources.getIdentifier("mithila_full_banner", "drawable", context.packageName)
    }
    val omPhotoResId = remember {
        context.resources.getIdentifier("om_kumar_photo", "drawable", context.packageName)
    }

    LaunchedEffect(Unit) {
        delay(4000)
        onContinue()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0A0F14), BannerMidnightCharcoal, Color(0xFF162430))
                )
            )
    ) {
        if (bannerResId != 0) {
            Image(
                painter = painterResource(id = bannerResId),
                contentDescription = "Mithila Full Screen Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.45f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.85f)
                            )
                        )
                    )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    color = BannerChampagneGold.copy(alpha = 0.18f),
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, BannerChampagneGold)
                ) {
                    Text(
                        text = "॥ जय मिथिला • पावन भूमि बीरपुर ॥",
                        color = BannerChampagneGold,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "दरभंगा राज एवं माँ श्यामा मंदिर विरासत",
                    color = BannerElectricTeal,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(136.dp)
                        .clip(CircleShape)
                        .border(3.dp, BannerChampagneGold, CircleShape)
                        .background(Color(0xFF1B2836)),
                    contentAlignment = Alignment.Center
                ) {
                    if (omPhotoResId != 0) {
                        Image(
                            painter = painterResource(id = omPhotoResId),
                            contentDescription = "Om Kumar Branding",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "ॐ",
                                color = BannerChampagneGold,
                                fontSize = 42.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "OM KUMAR",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "MITHILA SMART REFRESH",
                    color = BannerChampagneGold,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = "Mithila Stream Studio • Birpur",
                    color = BannerElectricTeal,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "प्रस्तुति: ओम कुमार (Om Kumar)",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 13.sp
                )
            }

            Button(
                onClick = onContinue,
                colors = ButtonDefaults.buttonColors(
                    containerColor = BannerChampagneGold,
                    contentColor = BannerMidnightCharcoal
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = "ऐप खोलें (Enter Smart Refresh) →",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun KalnirnayMithilaCalendar() {
    var selectedYear by remember { mutableStateOf(2026) }
    var selectedMonth by remember { mutableStateOf(10) }

    val monthNamesHindi = listOf(
        "जनवरी (January)", "फ़रवरी (February)", "मार्च (March)", "अप्रैल (April)",
        "मई (May)", "जून (June)", "जुलाई (July)", "अगस्त (August)",
        "सितंबर (September)", "अक्टूबर (October)", "नवंबर (November)", "दिसंबर (December)"
    )
    val dayHeaders = listOf("रवि\nSUN", "सोम\nMON", "मंगल\nTUE", "बुध\nWED", "गुरु\nTHU", "शुक्र\nFRI", "शनि\nSAT")

    val cal = remember(selectedYear, selectedMonth) {
        Calendar.getInstance().apply {
            set(Calendar.YEAR, selectedYear)
            set(Calendar.MONTH, selectedMonth)
            set(Calendar.DAY_OF_MONTH, 1)
        }
    }
    val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1
    val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

    val monthHolidays = remember(selectedYear, selectedMonth) {
        val prefix = String.format("%04d-%02d-", selectedYear, selectedMonth + 1)
        MithilaCalendarData.holidays2026to2031.filterKeys { it.startsWith(prefix) }.values.toList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDFBF7))
            .padding(10.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = KalnirnayRed),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "मिथिला कालनिर्णय पंचांग • 2026–2031",
                    color = BannerChampagneGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = {
                        if (selectedMonth == 0) {
                            if (selectedYear > 2026) { selectedYear--; selectedMonth = 11 }
                        } else selectedMonth--
                    }) {
                        Text("◀ पिछला", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = monthNamesHindi[selectedMonth],
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            (2026..2031).forEach { yr ->
                                val active = yr == selectedYear
                                Text(
                                    text = "$yr",
                                    color = if (active) KalnirnayRed else Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(if (active) BannerChampagneGold else Color.Black.copy(alpha = 0.25f))
                                        .clickable { selectedYear = yr }
                                        .padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    TextButton(onClick = {
                        if (selectedMonth == 11) {
                            if (selectedYear < 2031) { selectedYear++; selectedMonth = 0 }
                        } else selectedMonth++
                    }) {
                        Text("अगला ▶", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            dayHeaders.forEachIndexed { idx, label ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(if (idx == 0) KalnirnayRed else BannerMidnightCharcoal)
                        .border(0.5.dp, Color.White)
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = if (idx == 0) Color.White else BannerChampagneGold,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        lineHeight = 12.sp
                    )
                }
            }
        }

        val totalCells = ((firstDayOfWeek + daysInMonth + 6) / 7) * 7
        Column(modifier = Modifier.fillMaxWidth()) {
            for (row in 0 until (totalCells / 7)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (col in 0 until 7) {
                        val cellIndex = row * 7 + col
                        val dayNum = cellIndex - firstDayOfWeek + 1
                        val isValidDay = dayNum in 1..daysInMonth
                        val dateKey = if (isValidDay) String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, dayNum) else ""
                        val holiday = if (isValidDay) MithilaCalendarData.holidays2026to2031[dateKey] else null
                        val isSunday = col == 0

                        val cellBg = when {
                            !isValidDay -> Color(0xFFEEEEEE)
                            holiday?.type == "GOVT" -> GovtHolidayBg
                            holiday?.type == "PUBLIC" -> PublicHolidayBg
                            isSunday -> Color(0xFFFFF3F3)
                            else -> Color.White
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(54.dp)
                                .background(cellBg)
                                .border(0.5.dp, Color(0xFFBDBDBD))
                                .padding(3.dp)
                        ) {
                            if (isValidDay) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "$dayNum",
                                        color = if (isSunday || holiday?.type == "GOVT") KalnirnayRed else BannerMidnightCharcoal,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    if (holiday != null) {
                                        Text(
                                            text = holiday.title,
                                            color = if (holiday.type == "GOVT") KalnirnayRed else Color(0xFF0D47A1),
                                            fontSize = 7.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 2,
                                            lineHeight = 8.5.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "इस माह की सरकारी (GOVT) एवं पब्लिक छुट्टियाँ:",
            color = BannerMidnightCharcoal,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(monthHolidays) { item ->
                val isGovt = item.type == "GOVT"
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isGovt) GovtHolidayBg else PublicHolidayBg
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${item.dateKey.takeLast(2)} — ${item.title}",
                            color = BannerMidnightCharcoal,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f)
                        )
                        Surface(
                            color = if (isGovt) KalnirnayRed else Color(0xFF1565C0),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = if (isGovt) "GOVT HOLIDAY" else "PUBLIC / पर्व",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
