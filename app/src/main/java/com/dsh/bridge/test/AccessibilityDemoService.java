package com.dsh.bridge.test;

import android.accessibilityservice.AccessibilityService;

public class AccessibilityDemoService extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(android.view.accessibility.AccessibilityEvent event) {
        // intentionally empty: presence of service/manifest is the native bridge marker
    }
    @Override
    public void onInterrupt() {}
}
