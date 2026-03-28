package com.openclaw.commentfloating

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class FloatingService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var floatingView: View
    private lateinit var panelView: View
    private var isPanelShowing = false

    private val comments = mutableListOf<String>()

    companion object {
        private const val CHANNEL_ID = "floating_service_channel"
        private const val NOTIFICATION_ID = 1001
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
        initFloatingWindow()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "悬浮窗服务",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "保持悬浮窗运行"
                setShowBadge(false)
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("评论助手")
                .setContentText("悬浮窗已启动")
                .setSmallIcon(android.R.drawable.ic_menu_edit)
                .setContentIntent(pendingIntent)
                .build()
        } else {
            @Suppress("DEPRECATION")
            Notification.Builder(this)
                .setContentTitle("评论助手")
                .setContentText("悬浮窗已启动")
                .setSmallIcon(android.R.drawable.ic_menu_edit)
                .setContentIntent(pendingIntent)
                .build()
        }
    }

    private fun initFloatingWindow() {
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        // 悬浮球布局参数
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 0
            y = 300
        }

        // 创建悬浮球视图
        floatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating, null)
        
        // 设置触摸监听（拖动）
        floatingView.setOnTouchListener(object : View.OnTouchListener {
            private var initialX = 0
            private var initialY = 0
            private var initialTouchX = 0f
            private var initialTouchY = 0f

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN ->                        initialX = params.x
                        initialY = params.y
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        return true
                    MotionEvent.ACTION_MOVE -> {
                        params.x = initialX + (event.rawX - initialTouchX).toInt()
                        params.y = initialY + (event.rawY - initialTouchY).toInt()
                        windowManager.updateViewLayout(floatingView, params)
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        val deltaX = event.rawX - initialTouchX
                        val deltaY = event.rawY - initialTouchY
                        // 点击事件（移动距离小于10px）
                        if (Math.abs(deltaX) < 10 && Math.abs(deltaY) < 10) {
                            onFloatingClick()
                        }
                        return true
                    }
                }
                return false
            }
        })

        windowManager.addView(floatingView, params)
    }

    private fun onFloatingClick() {
        // 读取剪贴板
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val text = clipboard.text?.toString()

        if (text.isNullOrBlank()) {
            Toast.makeText(this, "剪贴板为空，请先复制视频标题", Toast.LENGTH_SHORT).show()
            return
        }

        // 生成评论
        generateComments(text)
        
        // 显示面板
        showCommentPanel(text)
    }

    private fun generateComments(title: String) {
        comments.clear()
        
        val isTutorial = title.contains("教程") || title.contains("教学") || title.contains("怎么")
        val isFunny = title.contains("搞笑") || title.contains("笑") || title.contains("幽默")
        val isFood = title.contains("美食") || title.contains("吃") || title.contains("做菜")
        val isTech = title.contains("手机") || title.contains("电脑") || title.contains("软件")

        comments.add("学到了！收藏了~ 👍")
        comments.add(
            when {
                isFunny -> "笑死我了 😂😂😂"
                isTech -> "这也太牛了吧！👍"
                else -> "太厉害了！点赞支持！"
            }
        )
        comments.add(
            when {
                isTutorial -> "请问这个用的是什么工具呀？"
                isFood -> "看着就好好吃！流口水了~"
                else -> "说的太对了！深有体会！"
            }
        )
        comments.add("终于有人说出我想说的了！")
        comments.add("这就是大佬和普通人的差距吗？慕了慕了 👏")
    }

    private fun showCommentPanel(title: String) {
        if (isPanelShowing) {
            hidePanel()
            return
        }

        // 创建面板布局
        val panelParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.BOTTOM
            y = 100
        }

        panelView = LayoutInflater.from(this).inflate(R.layout.layout_panel, null)
        
        // 设置视频标题
        panelView.findViewById<TextView>(R.id.tv_title).text = 
            "视频: ${if (title.length > 30) title.take(30) + "..." else title}"

        // 设置评论按钮
        val btnClose = panelView.findViewById<Button>(R.id.btn_close)
        val btnCopy1 = panelView.findViewById<Button>(R.id.btn_comment_1)
        val btnCopy2 = panelView.findViewById<Button>(R.id.btn_comment_2)
        val btnCopy3 = panelView.findViewById<Button>(R.id.btn_comment_3)
        val btnCopy4 = panelView.findViewById<Button>(R.id.btn_comment_4)
        val btnCopy5 = panelView.findViewById<Button>(R.id.btn_comment_5)

        btnClose.setOnClickListener { hidePanel() }

        if (comments.size >= 5) {
            btnCopy1.text = "1. ${comments[0]}"
            btnCopy2.text = "2. ${comments[1]}"
            btnCopy3.text = "3. ${comments[2]}"
            btnCopy4.text = "4. ${comments[3]}"
            btnCopy5.text = "5. ${comments[4]}"

            btnCopy1.setOnClickListener { copyAndHide(comments[0]) }
            btnCopy2.setOnClickListener { copyAndHide(comments[1]) }
            btnCopy3.setOnClickListener { copyAndHide(comments[2]) }
            btnCopy4.setOnClickListener { copyAndHide(comments[3]) }
            btnCopy5.setOnClickListener { copyAndHide(comments[4]) }
        }

        windowManager.addView(panelView, panelParams)
        isPanelShowing = true
    }

    private fun copyAndHide(comment: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.text = comment
        Toast.makeText(this, "已复制: $comment", Toast.LENGTH_SHORT).show()
        hidePanel()
    }

    private fun hidePanel() {
        if (isPanelShowing && ::panelView.isInitialized) {
            try {
                windowManager.removeView(panelView)
            } catch (e: Exception) {}
            isPanelShowing = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::floatingView.isInitialized) {
            try {
                windowManager.removeView(floatingView)
            } catch (e: Exception) {}
        }
        hidePanel()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
