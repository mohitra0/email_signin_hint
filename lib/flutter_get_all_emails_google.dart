import 'flutter_get_all_emails_google_platform_interface.dart';

class FlutterGetAllEmailsGoogle {
  Future<String?> getPlatformVersion() {
    return FlutterGetAllEmailsGooglePlatform.instance.getPlatformVersion();
  }

  Future<String?> getEmail() {
    return FlutterGetAllEmailsGooglePlatform.instance.getEmail();
  }
}
