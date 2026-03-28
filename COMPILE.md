# 快速编译指南

## 方案1：GitHub Actions（推荐，完全自动）

### 步骤：

1. **上传到GitHub**
   ```bash
   git init
   git add .
   git commit -m "Initial commit"
   git remote add origin https://github.com/YOUR_USERNAME/comment-floating-app.git
   git push -u origin main
   ```

2. **自动编译**
   - GitHub会自动运行 `.github/workflows/build.yml`
   - 编译完成后，APK会出现在 **Actions** 标签页
   - 点击最新的 workflow run → 下载 `app-debug` artifact

3. **下载APK**
   - 文件名：`app-debug.apk`
   - 大小：约5-10MB

---

## 方案2：Replit在线编译

### 步骤：

1. **打开Replit**
   - 访问 https://replit.com
   - 点击 **Create** → **Import from GitHub**
   - 粘贴你的GitHub仓库链接

2. **运行编译脚本**
   ```bash
   bash build.sh
   ```

3. **下载APK**
   - 编译完成后，APK在 `comment-floating-app/app/build/outputs/apk/debug/app-debug.apk`
   - 右键下载

---

## 方案3：本地编译（需要Java + Gradle）

### 前置条件：
- Java 11+
- Gradle 8.2+

### 步骤：

```bash
cd comment-floating-app
chmod +x gradlew
./gradlew assembleDebug
```

APK位置：`app/build/outputs/apk/debug/app-debug.apk`

---

## 安装到鸿蒙手机

1. **下载APK到电脑**

2. **传输到手机**
   - 用USB连接手机
   - 或用微信/QQ传输

3. **安装**
   - 打开文件管理器
   - 找到APK文件
   - 点击安装
   - 允许"安装未知来源应用"

4. **授予权限**
   - 打开App
   - 点击"启动悬浮窗"
   - 授予悬浮窗权限

---

## 常见问题

**Q: 编译失败？**
A: 检查Java版本 `java -version`，需要11+

**Q: APK太大？**
A: 正常，包含了所有依赖库

**Q: 鸿蒙不支持？**
A: 鸿蒙兼容Android APK，应该可以安装

**Q: 悬浮窗不显示？**
A: 检查是否授予了悬浮窗权限

---

## 推荐流程

```
1. 上传到GitHub
   ↓
2. GitHub Actions自动编译
   ↓
3. 下载APK
   ↓
4. 传到手机
   ↓
5. 安装并使用
```

**最简单！** 只需要一个GitHub账号。
