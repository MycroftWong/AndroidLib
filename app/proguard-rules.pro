# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/navigation/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

## Gson
-keep class wang.mycroft.lib.sample.model.** {
    *;
}

-keep class wang.mycroft.lib.sample.net.NetModel {
    *;
}

-ignorewarnings
##start------------------------------android系统相关----------------------------------------------
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
##end------------------------------android系统相关-------------------------------------------------
##start---------------------------Context-----------------------------------------------------------
-keepclassmembers class * extends android.content.Context {
public void *(android.view.View);
public void *(android.view.MenuItem);
}
##end-----------------------------Context-----------------------------------------------------------
##start------------------------------枚举类不混淆--------------------------------------------------
-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}
##end----------------------------------枚举类不混淆------------------------------------------------
##start------------------------------继承自View的控件----------------------------------------------
-keep public class * extends android.view.View {
public <init>(android.content.Context);
public <init>(android.content.Context, android.util.AttributeSet);
public <init>(android.content.Context, android.util.AttributeSet, int);
public *** set*(...);
public *** get*(...);
}
-keepclasseswithmembers class * {
public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
public <init>(android.content.Context, android.util.AttributeSet, int);
}
##end------------------------------继承自View的控件------------------------------------------------
##start-----------------------------native方法----------------------------------------------------
-keepclasseswithmembernames,includedescriptorclasses class * {
native <methods>;
}
-keepclasseswithmembernames class * {
native <methods>;
}
##end--------------------------------native方法--------------------------------------------------
##start------------------------------Parcelable CREATOR变量---------------------------------------
-keepclassmembers class * implements android.os.Parcelable {
static ** CREATOR;
}
-keepclassmembers class * implements android.os.Parcelable {
public static final android.os.Parcelable$Creator CREATOR;
}
##end------------------------------Parcelable CREATOR变量-----------------------------------------
##start-----------------------------Serializable----------------------------------------------------
#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable
#保持 Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
static final long serialVersionUID;
private static final java.io.ObjectStreamField[] serialPersistentFields;
!static !transient <fields>;
!private <fields>;
!private <methods>;
private void writeObject(java.io.ObjectOutputStream);
private void readObject(java.io.ObjectInputStream);
java.lang.Object writeReplace();
java.lang.Object readResolve();
}
##end--------------------------------Serializable---------------------------------------------------
##start------------------------------R文件的静态成员-----------------------------------------------
-keep class **.R$* {
public static <fields>;
}
-keep class **.R
##end------------------------------R文件静态成员---------------------------------------------------


##start------------------------------cookiejar-----------------------------------------------
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

-dontwarn wang.mycroft.lib.net.cookiejar.**
-keep class wang.mycroft.lib.net.cookiejar.**

##end------------------------------cookiejar-----------------------------------------------
