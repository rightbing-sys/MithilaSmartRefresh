package com.mithila.smartrefresh

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MithilaFeedbackSection() {
    val context = LocalContext.current
    var feedbackMessage by remember { mutableStateOf("") }
    val supportNumber = "8177992737"
    val fullNumberWithCode = "918177992737"

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF16222F)),
        border = BorderStroke(1.dp, BannerChampagneGold.copy(alpha = 0.6f)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Text(
                text = "संपर्क एवं फीडबैक (Feedback & Support)",
                color = BannerChampagneGold,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Mithila Stream Studio • हेल्पलाइन: +91 $supportNumber",
                color = BannerElectricTeal,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = feedbackMessage,
                onValueChange = { feedbackMessage = it },
                placeholder = {
                    Text(
                        "अपना सुझाव या समस्या यहाँ लिखें...",
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BannerChampagneGold,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            val finalMsg = if (feedbackMessage.isBlank()) {
                "नमस्ते Mithila Stream Studio, मुझे Mithila Smart Refresh ऐप के बारे में बात करनी है।"
            } else {
                "Mithila Smart Refresh Feedback: $feedbackMessage"
            }

            // Row 1: WhatsApp & Telegram
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        openWhatsApp(context, fullNumberWithCode, finalMsg)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("WhatsApp", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                Button(
                    onClick = {
                        openTelegram(context, fullNumberWithCode, finalMsg)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0088CC)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Telegram", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Row 2: Arattai & Direct Call
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        openArattai(context, fullNumberWithCode, finalMsg)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6F00)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Arattai", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                Button(
                    onClick = {
                        val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+91$supportNumber"))
                        context.startActivity(dialIntent)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BannerChampagneGold,
                        contentColor = BannerMidnightCharcoal
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Call करें", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

private fun openWhatsApp(context: Context, phoneWithCode: String, text: String) {
    try {
        val url = "https://wa.me/$phoneWithCode?text=${Uri.encode(text)}"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "WhatsApp उपलब्ध नहीं है", Toast.LENGTH_SHORT).show()
    }
}

private fun openTelegram(context: Context, phoneWithCode: String, text: String) {
    try {
        val url = "https://t.me/+$phoneWithCode?text=${Uri.encode(text)}"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Telegram उपलब्ध नहीं है", Toast.LENGTH_SHORT).show()
    }
}

private fun openArattai(context: Context, phoneWithCode: String, text: String) {
    val arattaiPackages = listOf("com.arattai.android", "com.zoho.arattai")
    var launched = false
    for (pkg in arattaiPackages) {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(pkg)
        if (launchIntent != null) {
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                setPackage(pkg)
                putExtra(Intent.EXTRA_TEXT, "To: +$phoneWithCode\n$text")
            }
            try {
                context.startActivity(sendIntent)
                launched = true
                break
            } catch (_: Exception) {
                context.startActivity(launchIntent)
                launched = true
                break
            }
        }
    }
    if (!launched) {
        Toast.makeText(context, "Arattai नंबर: +$phoneWithCode पर संदेश भेजें", Toast.LENGTH_LONG).show()
        val smsIntent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:+$phoneWithCode")).apply {
            putExtra("sms_body", text)
        }
        try {
            context.startActivity(smsIntent)
        } catch (_: Exception) {}
    }
}
