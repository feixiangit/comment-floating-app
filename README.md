# 评论助手 - 悬浮窗App

抖音/快手半自动评论工具，悬浮窗设计，一键生成评论。

## 功能

- 🎈 悬浮窗按钮（可拖动）
- 📋 自动读取剪贴板
- ✨ AI生成5条评论
- 👆 点击复制
- 🔒 防封机制（评论间隔、内容去重）

## 使用流程

```
1. 打开App，授予悬浮窗权限
2. 点击"启动悬浮窗"
3. 打开抖音，刷到想评论的视频
4. 复制视频标题
5. 点击悬浮窗按钮
6. 选择一条评论，点击复制
7. 切换到抖音，粘贴发布
```

## 安装步骤

### 方法1：Android Studio编译

```bash
# 1. 克隆项目
cd comment-floating-app

# 2. 生成Gradle Wrapper
gradle wrapper

# 3. 编译Debug APK
./gradlew assembleDebug

# 4. APK位置
# app/build/outputs/apk/debug/app-debug.apk
```

### 方法2：命令行编译

```bash
# Windows
gradlew.bat assembleDebug

# macOS/Linux
./gradlew assembleDebug
```

### 方法3：直接安装

1. 下载 `app-debug.apk`
2. 传输到手机
3. 设置 → 安全 → 允许安装未知来源应用
4. 点击APK安装

## 权限说明

| 权限 | 用途 |
|------|------|
| SYSTEM_ALERT_WINDOW | 显示悬浮窗 |
| INTERNET | 网络通信 |
| READ_CLIPBOARD | 读取剪贴板 |

## 防封机制

- 同一视频评论间隔：3分钟
- 每次评论间隔：10秒
- 同一视频评论上限：3条

## 项目结构

```
app/
├── src/main/
│   ├── java/com/openclaw/commentfloating/
│   │   ├── MainActivity.kt      # 主界面
│   │   └── FloatingService.kt  # 悬浮窗服务
│   ├── res/
│   │   ├── layout/             # 布局文件
│   │   ├── drawable/           # 图标
│   │   └── values/             # 资源
│   └── AndroidManifest.xml
└── build.gradle.kts
```

## 截图预览

### 主界面
- 输入视频标题
- 生成评论选项
- 启动/关闭悬浮窗

### 悬浮窗
- 蓝色圆形悬浮球
- 可拖动位置
- 点击弹出评论面板

### 评论面板
- 显示视频标题
- 5条不同风格评论
- 点击一键复制

## 注意事项

1. 首次使用需要授予悬浮窗权限
2. 鸿蒙系统可能需要额外设置
3. 关闭App后悬浮窗也会消失
4. 建议保持App在后台运行

## License

MIT
