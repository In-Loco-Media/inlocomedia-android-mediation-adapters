package com.inlocomedia.android.mediation.google;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitial;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitialListener;
import com.inlocomedia.android.ads.AdError;
import com.inlocomedia.android.ads.interstitial.InterstitialAd;
import com.inlocomedia.android.ads.interstitial.InterstitialAdListener;

public class InLocoMediaInterstitialAdapter implements CustomEventInterstitial {

    public static final String TAG = "InLocoMedia";
    private InterstitialAd mInterstitialAd;

    private String mRequestAgent;

    public InLocoMediaInterstitialAdapter(final String requestAgent) {
        this.mRequestAgent = requestAgent;
    }

    @Override
    public void requestInterstitialAd(final Context context, final CustomEventInterstitialListener listener, final String serverParameter,
                                      final MediationAdRequest mediationAdRequest, final Bundle bundle) {
        Utils.setupInLocoMedia(context, serverParameter);

        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setInterstitialAdListener(new InterstitialAdListener() {
            @Override
            public void onAdReady(InterstitialAd ad) {
                Log.i(TAG, "Interstitial ready");
                listener.onAdLoaded();
            }

            @Override
            public void onAdOpened(InterstitialAd ad) {
                Log.i(TAG, "Interstitial opened");
                listener.onAdOpened();
            }

            @Override
            public void onAdError(InterstitialAd ad, AdError error) {
                Log.i(TAG, "Interstitial error: " + error);
                listener.onAdFailedToLoad(Utils.adErrorToAdMobError(error));
            }

            @Override
            public void onAdClosed(InterstitialAd ad) {
                Log.i(TAG, "Interstitial closed");
                listener.onAdClosed();
            }

            @Override
            public void onAdLeftApplication(InterstitialAd ad) {
                Log.i(TAG, "Interstitial left application");
                listener.onAdLeftApplication();
            }
        });

        String adUnitId = Utils.getAdUnitId(serverParameter);
        mInterstitialAd.loadAd(Utils.createAdRequest(mediationAdRequest, adUnitId, mRequestAgent));
        Log.i(TAG, "Interstitial requested");
    }

    @Override
    public void showInterstitial() {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    @Override
    public void onDestroy() {}

    @Override
    public void onPause() {}

    @Override
    public void onResume() {}
}
