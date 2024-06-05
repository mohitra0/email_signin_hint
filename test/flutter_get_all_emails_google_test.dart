import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_get_all_emails_google/flutter_get_all_emails_google.dart';
import 'package:flutter_get_all_emails_google/flutter_get_all_emails_google_platform_interface.dart';
import 'package:flutter_get_all_emails_google/flutter_get_all_emails_google_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockFlutterGetAllEmailsGooglePlatform
    with MockPlatformInterfaceMixin
    implements FlutterGetAllEmailsGooglePlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final FlutterGetAllEmailsGooglePlatform initialPlatform = FlutterGetAllEmailsGooglePlatform.instance;

  test('$MethodChannelFlutterGetAllEmailsGoogle is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelFlutterGetAllEmailsGoogle>());
  });

  test('getPlatformVersion', () async {
    FlutterGetAllEmailsGoogle flutterGetAllEmailsGooglePlugin = FlutterGetAllEmailsGoogle();
    MockFlutterGetAllEmailsGooglePlatform fakePlatform = MockFlutterGetAllEmailsGooglePlatform();
    FlutterGetAllEmailsGooglePlatform.instance = fakePlatform;

    expect(await flutterGetAllEmailsGooglePlugin.getPlatformVersion(), '42');
  });
}
