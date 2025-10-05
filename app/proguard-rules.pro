# Keep Jetpack Compose classes
-keep class androidx.compose.** { *; }
-keep class kotlinx.coroutines.** { *; }
-keep class com.financeflow.** { *; }

# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}
