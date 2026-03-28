package com.openclaw.commentfloating

import android.Manifest
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var etVideoTitle: EditText
    private lateinit var tvComments: TextView
    private lateinit var btnGenerate: Button
    private lateinit var btnStartFloating: Button
    private lateinit var btnStopFloating: Button

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    companion object {
        private const val REQUEST_CODE_OVERLAY = 1001
        private const val API_BASE = "http://localhost:28789"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupListeners()
        checkPermissions()
    }

    private fun initViews() {
        etVideoTitle = findViewById(R.id.et_video_title)
        tvComments = findViewById(R.id.tv_comments)
        btnGenerate = findViewById(R.id.btn_generate)
        btnStartFloating = findViewById(R.id.btn_start_floating)
        btnStopFloating = findViewById(R.id.btn_stop_floating)
    }

    private fun setupListeners() {
        // 生成评论
        btnGenerate.setOnClickListener {
            val title = etVideoTitle.text.toString().trim()
            if (title.isEmpty()) {
                Toast.makeText(this, "请输入视频标题", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            generateComments(title)
        }

        // 启动悬浮窗
        btnStartFloating.setOnClickListener {
            startFloatingWindow()
        }

        // 停止悬浮窗
        btnStopFloating.setOnClickListener {
            stopFloatingWindow()
        }

        // 粘贴按钮
        findViewById<Button>(R.id.btn_paste).setOnClickListener {
            pasteFromClipboard()
        }
    }

    private fun checkPermissions() {
        // 检查悬浮窗权限
        if (!Settings.canDrawOverlays(this)) {
            tvComments.text = "请先授予悬浮窗权限"
        } else {
            tvComments.text = "权限正常，可以启动悬浮窗"
        }
    }

    private fun pasteFromClipboard() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val text = clipboard.text?.toString()
        if (!text.isNullOrEmpty()) {
            etVideoTitle.setText(text)
            Toast.makeText(this, "已粘贴", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "剪贴板为空", Toast.LENGTH_SHORT).show()
        }
    }

    private fun generateComments(title: String) {
        tvComments.text = "生成中..."
        btnGenerate.isEnabled = false

        // 本地生成评论（不依赖网络）
        thread {
            val comments = generateLocalComments(title)
            runOnUiThread {
                displayComments(comments)
                btnGenerate.isEnabled = true
            }
        }
    }

    private fun generateLocalComments(title: String): List<String> {
        // 简单的评论生成逻辑
        val comments = mutableListOf<String>()
        
        // 分析标题关键词
        val isTutorial = title.contains("教程") || title.contains("教学") || title.contains("怎么")
        val isFunny = title.contains("搞笑") || title.contains("笑") || title.contains("幽默")
        val isFood = title.contains("美食") || title.contains("吃") || title.contains("做菜")
        
        // 生成5条不同风格的评论
        comments.add("学到了！收藏了~ 👍")
        comments.add(if (isFunny) "笑死我了 😂😂😂" else "太厉害了！点赞支持！")
        comments.add(if (isTutorial) "请问这个用的是什么工具呀？" else "说的太对了！深有体会！")
        comments.add("终于有人说出我想说的了！")
        comments.add("这就是大佬和普通人的差距吗？慕了慕了 👏")
        
        return comments
    }

    private fun displayComments(comments: List<String>) {
        val sb = StringBuilder()
        sb.append("生成5条评论（点击复制）：\n\n")
        comments.forEachIndexed { index, comment ->
            sb.append("${index + 1}. $comment\n\n")
        }
        tvComments.text = sb.toString()

        // 保存到剪贴板供选择
        ClipboardManager(this).text = comments.joinToString("\n---\n")
        Toast.makeText(this, "已复制全部评论到剪贴板", Toast.LENGTH_SHORT).show()
    }

    private fun startFloatingWindow() {
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, REQUEST_CODE_OVERLAY)
            return
        }

        val serviceIntent = Intent(this, FloatingService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
        Toast.makeText(this, "悬浮窗已启动", Toast.LENGTH_SHORT).show()
        finish() // 关闭主界面
    }

    private fun stopFloatingWindow() {
        val serviceIntent = Intent(this, FloatingService::class.java)
        stopService(serviceIntent)
        Toast.makeText(this, "悬浮窗已关闭", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_OVERLAY) {
            checkPermissions()
        }
    }
}
