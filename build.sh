#!/bin/bash
# 在线编译脚本 - 用于Replit或GitHub Actions

set -e

echo "=========================================="
echo "评论助手 - 浮窗App 编译脚本"
echo "=========================================="
echo ""

# 1. 安装依赖
echo "[1/5] 安装Java和Gradle..."
apt-get update -qq
apt-get install -y -qq openjdk-11-jdk-headless gradle > /dev/null 2>&1

# 2. 验证环境
echo "[2/5] 验证编译环境..."
java -version
gradle --version

# 3. 生成Gradle Wrapper
echo "[3/5] 生成Gradle Wrapper..."
cd comment-floating-app
gradle wrapper --gradle-version 8.2

# 4. 编译APK
echo "[4/5] 编译Debug APK..."
./gradlew assembleDebug

# 5. 输出结果
echo "[5/5] 编译完成！"
echo ""
echo "APK位置: app/build/outputs/apk/debug/app-debug.apk"
echo "文件大小: $(du -h app/build/outputs/apk/debug/app-debug.apk | cut -f1)"
echo ""
echo "✅ 编译成功！"
