import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'flutter_get_all_emails_google_platform_interface.dart';

/// An implementation of [FlutterGetAllEmailsGooglePlatform] that uses method channels.
class MethodChannelFlutterGetAllEmailsGoogle
    extends FlutterGetAllEmailsGooglePlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('flutter_get_all_emails_google');

  @override
  Future<String?> getPlatformVersion() async {
    final version =
        await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<String?> getEmail() async {
    final email = await methodChannel.invokeMethod<String>('getEmail');
    return email;
  }
}
