import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'flutter_get_all_emails_google_method_channel.dart';

abstract class FlutterGetAllEmailsGooglePlatform extends PlatformInterface {
  /// Constructs a FlutterGetAllEmailsGooglePlatform.
  FlutterGetAllEmailsGooglePlatform() : super(token: _token);

  static final Object _token = Object();

  static FlutterGetAllEmailsGooglePlatform _instance =
      MethodChannelFlutterGetAllEmailsGoogle();

  /// The default instance of [FlutterGetAllEmailsGooglePlatform] to use.
  ///
  /// Defaults to [MethodChannelFlutterGetAllEmailsGoogle].
  static FlutterGetAllEmailsGooglePlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [FlutterGetAllEmailsGooglePlatform] when
  /// they register themselves.
  static set instance(FlutterGetAllEmailsGooglePlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<String?> getEmail() {
    throw UnimplementedError('Email has not been implemented.');
  }
}
