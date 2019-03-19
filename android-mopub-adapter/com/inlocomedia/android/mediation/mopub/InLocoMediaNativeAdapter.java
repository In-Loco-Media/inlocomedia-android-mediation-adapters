package com.inlocomedia.android.mediation.mopub;

import android.content.Context;
import android.support.annotation.NonNull;

import com.inlocomedia.android.ads.AdError;
import com.inlocomedia.android.ads.AdRequest;
import com.inlocomedia.android.ads.nativeads.NativeAdManager;
import com.inlocomedia.android.ads.nativeads.NativeAdResponse;
import com.mopub.nativeads.ImpressionTracker;
import com.mopub.nativeads.InLocoMediaBaseForwardingNativeAd;
import com.mopub.nativeads.NativeClickHandler;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.NativeImageHelper;

import java.util.Map;

@SuppressWarnings("unused")
public class InLocoMediaNativeAdapter extends com.mopub.nativeads.CustomEventNative {

    @Override
    protected void loadNativeAd(@NonNull final Context context, @NonNull final CustomEventNativeListener customEventNativeListener,
                                @NonNull final Map<String, Object> localExtras, @NonNull final Map<String, String> serverExtras) {
        MoPubUtils.setupInLocoMedia(context, serverExtras);

        final NativeClickHandler nativeClickHandler = new NativeClickHandler(context);
        final ImpressionTracker impressionTracker = new ImpressionTracker(context);

        //Being created here to avoid passing the activity as a parameter to a Thread
        final Context appContext = context.getApplicationContext();

        String adUnitId = MoPubUtils.getAdUnit(serverExtras);
        final AdRequest adRequest = MoPubUtils.createAdRequest(adUnitId);
        NativeAdManager.requestAd(appContext, adRequest, new NativeAdManager.RequestListener() {

            @Override
            public void onAdReceived(final NativeAdResponse response) {
                final InLocoMediaBaseForwardingNativeAd nativeAd =
                new InLocoMediaBaseForwardingNativeAd(appContext, response, nativeClickHandler, impressionTracker, adRequest);

                NativeImageHelper.preCacheImages(appContext, nativeAd.getAllImageUrls(), new NativeImageHelper.ImageListener() {
                    @Override
                    public void onImagesCached() {
                        customEventNativeListener.onNativeAdLoaded(nativeAd);
                    }

                    @Override
                    public void onImagesFailedToCache(final NativeErrorCode errorCode) {
                        customEventNativeListener.onNativeAdFailed(errorCode);
                    }
                });
            }

            @Override
            public void onAdError(final AdError adError) {
                customEventNativeListener.onNativeAdFailed(MoPubUtils.convertAdErrorToNativeErrorCode(adError));
            }
        });
    }
}
