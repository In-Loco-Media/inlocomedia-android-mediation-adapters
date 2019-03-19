package com.inlocomedia.android.mediation.mopub;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;

import com.inlocomedia.android.ads.AdError;
import com.inlocomedia.android.ads.AdRequest;
import com.inlocomedia.android.ads.AdType;
import com.inlocomedia.android.ads.AdView;
import com.inlocomedia.android.ads.AdViewListener;
import com.mopub.mobileads.CustomEventBanner;
import com.mopub.mobileads.MoPubErrorCode;

import java.util.Map;

@SuppressWarnings("unused")
public class InLocoMediaBannerAdapter extends CustomEventBanner {

    private AdView mAdView;
    private AdRequest mAdRequest;

    @Override
    protected void loadBanner(Context context, final CustomEventBannerListener listener, Map<String, Object> localExtras,
                              Map<String, String> serverExtras) {
        MoPubUtils.setupInLocoMedia(context, serverExtras);

        AdType adType = MoPubUtils.getAdType(serverExtras);
        if (adType == null) {
            listener.onBannerFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        mAdView = new AdView(context);
        mAdView.setType(adType);

        mAdView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBannerClicked();
            }
        });

        mAdView.setAdListener(new AdViewListener() {
            @Override
            public void onAdViewReady(AdView adView) {
                listener.onBannerLoaded(adView);
            }

            @Override
            public void onAdLeftApplication(AdView adView) {
                listener.onLeaveApplication();
            }

            @Override
            public void onAdError(final AdView adView, final AdError error) {
                listener.onBannerFailed(MoPubUtils.convertAdErrorToMoPubError(error));
                mAdView.destroy();
            }
        });

        String adUnitId = MoPubUtils.getAdUnit(serverExtras);
        mAdRequest = MoPubUtils.createAdRequest(adUnitId);
        mAdView.loadAd(mAdRequest);
    }

    @Override
    protected void onInvalidate() {
        if (mAdView != null) {
            mAdView.destroy();
        }
    }

    @Nullable
    public AdRequest getAdRequest() {
        return mAdRequest;
    }
}
