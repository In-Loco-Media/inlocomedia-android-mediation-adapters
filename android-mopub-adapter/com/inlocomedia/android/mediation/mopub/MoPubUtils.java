package com.inlocomedia.android.mediation.mopub;

import android.content.Context;
import android.util.Log;

import com.inlocomedia.android.ads.InLocoMedia;
import com.inlocomedia.android.ads.InLocoMediaOptions;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.nativeads.NativeErrorCode;

import java.util.Map;

import com.inlocomedia.android.ads.AdError;
import com.inlocomedia.android.ads.AdRequest;
import com.inlocomedia.android.ads.AdType;

public class MoPubUtils {

    public static final String TAG = "InLocoMedia";

    public static final String REQUEST_AGENT = "mopub";

    private static final String AD_WIDTH = "ad_width";
    private static final String AD_HEIGHT = "ad_height";
    private static final String AD_UNIT = "ad_unit_id";

    public static final String APP_ID = "app_id";

    public static MoPubErrorCode convertAdErrorToMoPubError(AdError error) {
        switch (error) {
            case NO_FILL:
                return MoPubErrorCode.NETWORK_NO_FILL;
            case TIMEOUT:
                return MoPubErrorCode.NETWORK_TIMEOUT;
            case UNAUTHORIZED:
                return MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR;
            case INTERNAL_ERROR:
                return MoPubErrorCode.NETWORK_INVALID_STATE;
            case NETWORK_NOT_AVAILABLE:
                return MoPubErrorCode.NO_CONNECTION;
            case INVALID_SDK_VERSION:
            case INVALID_REQUEST:
            case GOOGLE_PLAY_SERVICES_NOT_FOUND:
                return MoPubErrorCode.NETWORK_INVALID_STATE;
            default:
                return MoPubErrorCode.UNSPECIFIED;
        }
    }

    public static NativeErrorCode convertAdErrorToNativeErrorCode(final AdError adError) {
        switch (adError) {

            case NO_FILL:
                return NativeErrorCode.NETWORK_NO_FILL;
            case TIMEOUT:
                return NativeErrorCode.NETWORK_TIMEOUT;
            case UNAUTHORIZED:
                return NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR;
            case INTERNAL_ERROR:
                return NativeErrorCode.NETWORK_INVALID_STATE;
            case NETWORK_NOT_AVAILABLE:
                return NativeErrorCode.CONNECTION_ERROR;
            case GOOGLE_PLAY_SERVICES_NOT_FOUND:
            case INVALID_SDK_VERSION:
            case INVALID_REQUEST:
                return NativeErrorCode.NETWORK_INVALID_REQUEST;
            default:
                return NativeErrorCode.UNSPECIFIED;
        }
    }

    public static AdType getAdType(Map<String, String> serverExtras) {
        AdType adType;

        try {
            String widthStr = serverExtras.get(AD_WIDTH);
            String heightStr = serverExtras.get(AD_HEIGHT);

            int width = Integer.parseInt(widthStr);
            int height = Integer.parseInt(heightStr);

            adType = AdType.getBySize(width, height);

            if (adType == null) {
                Log.w(TAG, widthStr + "x" + heightStr + " is not compatible with any InLocoMedia type.");
            }

        } catch (NumberFormatException e) {
            Log.w(TAG, e.getMessage());
            return null;
        }

        return adType;
    }

    public static String getAdUnit(Map<String, String> serverExtras) {

        if (serverExtras.containsKey(AD_UNIT)) {
            return serverExtras.get(AD_UNIT);
        }

        return null;
    }

    public static AdRequest createAdRequest(String adUnitId) {
        return new AdRequest().setRequestAgent(REQUEST_AGENT).setAdUnitId(adUnitId);
    }

    public static void setupInLocoMedia(Context context, Map<String, String> serverExtras) {
        if (serverExtras.containsKey(APP_ID)) {
            String appId = serverExtras.get(APP_ID);
            setupInLocoMedia(context, appId);
        }
    }

    private static void setupInLocoMedia(Context context, String appId) {
        InLocoMediaOptions options = InLocoMediaOptions.getInstance(context);
        options.setAdsKey(appId);
        options.setLogEnabled(true);
        InLocoMedia.init(context, options);
    }
}
