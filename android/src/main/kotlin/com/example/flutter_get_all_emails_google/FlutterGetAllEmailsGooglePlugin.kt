package com.example.flutter_get_all_emails_google

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import androidx.annotation.NonNull
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.CredentialsClient
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.gms.auth.api.credentials.IdentityProviders
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry
import java.util.concurrent.CompletableFuture

class FlutterGetAllEmailsGooglePlugin: FlutterPlugin, MethodCallHandler, ActivityAware, PluginRegistry.ActivityResultListener {
  private lateinit var channel: MethodChannel
  private lateinit var credentialsClient: CredentialsClient
  private var hintFuture: CompletableFuture<String>? = null
  private var activity: Activity? = null
  private var activityPluginBinding: ActivityPluginBinding? = null

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "flutter_get_all_emails_google")
    channel.setMethodCallHandler(this)
    credentialsClient = Credentials.getClient(flutterPluginBinding.applicationContext)
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    when (call.method) {
      "getPlatformVersion" -> result.success("Android ${android.os.Build.VERSION.RELEASE}")
      "getEmail" -> getHint(result)
      else -> result.notImplemented()
    }
  }

  private fun getHint(result: Result) {
    val hintRequest = HintRequest.Builder()
      .setHintPickerConfig(com.google.android.gms.auth.api.credentials.CredentialPickerConfig.Builder()
        .setShowCancelButton(true)
        .build())
      .setEmailAddressIdentifierSupported(true)
      .setAccountTypes(IdentityProviders.GOOGLE)
      .build()
    val intent: PendingIntent = credentialsClient.getHintPickerIntent(hintRequest)

    hintFuture = CompletableFuture()

    try {
      activity?.startIntentSenderForResult(intent.intentSender, RC_HINT, null, 0, 0, 0, null)
      hintFuture!!.thenAccept { hint ->
        result.success(hint)
      }.exceptionally { throwable ->
        result.error("UNAVAILABLE", "Could not start hint picker intent", throwable)
        null
      }
    } catch (e: IntentSender.SendIntentException) {
      result.error("UNAVAILABLE", "Could not start hint picker intent", null)
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
    if (requestCode == RC_HINT) {
      if (resultCode == Activity.RESULT_OK) {
        val credential: Credential? = data?.getParcelableExtra(Credential.EXTRA_KEY)
        credential?.let {
          hintFuture?.complete(it.id)
        }
      } else {
        Log.e("FlutterGetAllEmailsGooglePlugin", "Hint Read: NOT OK")
        hintFuture?.completeExceptionally(RuntimeException("Hint read failed"))
      }
      return true
    }
    return false
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity
    activityPluginBinding = binding
    binding.addActivityResultListener(this)
  }

  override fun onDetachedFromActivity() {
    activity = null
    activityPluginBinding?.removeActivityResultListener(this)
    activityPluginBinding = null
  }

  override fun onDetachedFromActivityForConfigChanges() {
    activity = null
    activityPluginBinding?.removeActivityResultListener(this)
    activityPluginBinding = null
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    activity = binding.activity
    activityPluginBinding = binding
    binding.addActivityResultListener(this)
  }

  companion object {
    private const val RC_HINT = 1000
  }
}
