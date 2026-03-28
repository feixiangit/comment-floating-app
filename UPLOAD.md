# GitHub上传步骤

## 前置条件
- GitHub账号（没有的话去 https://github.com/signup 注册）
- Git已安装（Windows自带或用 `choco install git`）

## 步骤

### 1. 创建新仓库
- 登录 https://github.com
- 点击右上角 **+** → **New repository**
- 仓库名：`comment-floating-app`
- 描述：`Comment Assistant - Floating Window App for HarmonyOS/Android`
- 选择 **Public**（这样GitHub Actions才能自动编译）
- 点击 **Create repository**

### 2. 获取仓库URL
创建后会看到类似这样的命令：
```
git remote add origin https://github.com/YOUR_USERNAME/comment-floating-app.git
git branch -M main
git push -u origin main
```

### 3. 在本地执行上传
```powershell
cd C:\Users\HUAWEI\.openclaw\.openclaw\workspace\comment-floating-app

# 添加远程仓库（替换YOUR_USERNAME）
git remote add origin https://github.com/YOUR_USERNAME/comment-floating-app.git

# 重命名分支为main
git branch -M main

# 推送到GitHub
git push -u origin main
```

### 4. 等待自动编译
- 打开 https://github.com/YOUR_USERNAME/comment-floating-app
- 点击 **Actions** 标签
- 等待 workflow 完成（通常2-3分钟）
- 看到绿色✅表示编译成功

### 5. 下载APK
- 点击最新的 workflow run
- 向下滚动找到 **Artifacts**
- 下载 `app-debug`
- 解压得到 `app-debug.apk`

---

## 快速命令（复制粘贴）

```powershell
cd C:\Users\HUAWEI\.openclaw\.openclaw\workspace\comment-floating-app
git remote add origin https://github.com/YOUR_USERNAME/comment-floating-app.git
git branch -M main
git push -u origin main
```

**记得替换 `YOUR_USERNAME` 为你的GitHub用户名！**

---

## 如果推送失败

可能是因为没有配置GitHub认证。用这个方法：

```powershell
# 生成Personal Access Token
# 1. 访问 https://github.com/settings/tokens
# 2. 点击 Generate new token
# 3. 勾选 repo 权限
# 4. 复制token

# 然后用token推送
git push https://YOUR_TOKEN@github.com/YOUR_USERNAME/comment-floating-app.git main
```

---

## 完成后

- ✅ GitHub Actions自动编译APK
- ✅ 下载APK到手机
- ✅ 安装到鸿蒙
- ✅ 开始使用！
