package com.ninecraft.booket

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ninecraft.booket.core.data.api.repository.UserRepository
import com.ninecraft.booket.core.designsystem.R
import com.ninecraft.booket.core.di.ServiceKey
import com.ninecraft.booket.feature.main.MainActivity
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@ContributesIntoMap(AppScope::class, binding = binding<Service>())
@ServiceKey(ReedFirebaseMessagingService::class)
@Inject
class ReedFirebaseMessagingService(
    private val userRepository: UserRepository,
) : FirebaseMessagingService() {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        scope.launch {
            userRepository.syncFcmToken(token)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title ?: "Reed"
        val body = message.notification?.body ?: ""

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val builder = NotificationCompat.Builder(this, REED_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setColor(ContextCompat.getColor(this, R.color.green_500))
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        // 알림 타입별로 고정된 ID 사용 (같은 타입의 알림은 업데이트됨)
        // val notificationType = message.data["type"] ?: NOTIFICATION_TYPE_DEFAULT
        // val notificationId = notificationType.hashCode()
        
        // 고정된 ID 사용하여 알림이 쌓이지 않고 업데이트되도록 처리
        val notificationId = REED_CHANNEL_ID.hashCode()

        manager.notify(notificationId, builder.build())
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    companion object {
        private const val REED_CHANNEL_ID = "REED_PUSH_CHANNEL"
        private const val REED_CHANNEL_NAME = "리드 푸시 알림"
        private const val REED_CHANNEL_DESC = "리드 앱에서 보내는 푸시 알림을 관리합니다."

        // 알림 타입
        private const val NOTIFICATION_TYPE_DEFAULT = "DEFAULT" // 타입이 지정되지 않은 경우
        private const val NOTIFICATION_TYPE_INACTIVE = "INACTIVE" // 미기록 알림 (7일 동안 기록 안 함)
        private const val NOTIFICATION_TYPE_DORMANT = "DORMANT" // 휴면 알림 (30일 동안 기록 안 함)

        // Android 8.0 이상 필수 채널 생성
        fun createNotificationChannel(context: Context) {
            val channel = NotificationChannel(
                REED_CHANNEL_ID,
                REED_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT,
            ).apply {
                description = REED_CHANNEL_DESC
            }

            val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}
