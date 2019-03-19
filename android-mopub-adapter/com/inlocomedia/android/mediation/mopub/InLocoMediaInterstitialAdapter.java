package com.inlocomedia.android.mediation.mopub;

import android.content.Context;
import android.support.annotation.Nullable;

import com.inlocomedia.android.ads.AdError;
import com.inlocomedia.android.ads.AdRequest;
import com.inlocomedia.android.ads.interstitial.InterstitialAd;
import com.inlocomedia.android.ads.interstitial.InterstitialAdListener;
import com.mopub.mobileads.CustomEventInterstitial;

import java.util.Map;

@SuppressWarnings("unused")
public class InLocoMediaInterstitialAdapter extends CustomEventInterstitial {

    private InterstitialAd mInterstitialAd;

    private AdRequest mAdRequest;

    @Override
    protected void loadInterstitial(Context context, final CustomEventInterstitialListener listener, Map<String, Object> localExtras,
                                    Map<String, String> serverExtras) {
        MoPubUtils.setupInLocoMedia(context, serverExtras);

        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setInterstitialAdListener(new InterstitialAdListener() {

            @Override
            public void onAdReady(InterstitialAd ad) {
                listener.onInterstitialLoaded();
            }

            @Override
            public void onAdOpened(InterstitialAd ad) {
                listener.onInterstitialShown();
            }

            @Override
            public void onAdLeftApplication(InterstitialAd ad) {
                listener.onLeaveApplication();
            }

            @Override
            public void onAdError(InterstitialAd ad, AdError error) {
                listener.onInterstitialFailed(MoPubUtils.convertAdErrorToMoPubError(error));
            }

            @Override
            public void onAdClosed(InterstitialAd ad) {
                listener.onInterstitialDismissed();
            }
        });

        String adUnitId = MoPubUtils.getAdUnit(serverExtras);
        mAdRequest = MoPubUtils.createAdRequest(adUnitId);
        mInterstitialAd.loadAd(mAdRequest);
    }

    @Override
    protected void showInterstitial() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show();
        }
    }

    @Override
    protected void onInvalidate() {
        if (mInterstitialAd != null) {
            mInterstitialAd.setInterstitialAdListener(null);
        }
    }

    @Nullable
    public AdRequest getAdRequest() {
        return mAdRequest;
    }
}
