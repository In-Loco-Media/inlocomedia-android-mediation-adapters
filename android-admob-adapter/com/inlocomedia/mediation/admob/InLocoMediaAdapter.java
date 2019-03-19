package com.inlocomedia.mediation.admob;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventBanner;
import com.google.android.gms.ads.mediation.customevent.CustomEventBannerListener;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitial;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitialListener;
import com.inlocomedia.android.mediation.google.InLocoMediaBannerAdapter;
import com.inlocomedia.android.mediation.google.InLocoMediaInterstitialAdapter;

@Deprecated
@SuppressWarnings("unused")
public class InLocoMediaAdapter implements CustomEventBanner, CustomEventInterstitial {

    private com.inlocomedia.android.mediation.google.InLocoMediaBannerAdapter mBannerAdapter;
    private com.inlocomedia.android.mediation.google.InLocoMediaInterstitialAdapter mInterstitialAdapter;

    private static final String REQUEST_AGENT = "admob";

    public InLocoMediaAdapter() {
        mBannerAdapter = new InLocoMediaBannerAdapter(REQUEST_AGENT);
        mInterstitialAdapter = new InLocoMediaInterstitialAdapter(REQUEST_AGENT) {};
    }

    @Override
    public void requestBannerAd(final Context context, final CustomEventBannerListener customEventBannerListener, final String serverParameter,
                                final AdSize adSize, final MediationAdRequest mediationAdRequest, final Bundle bundle) {
        mBannerAdapter.requestBannerAd(context, customEventBannerListener, serverParameter, adSize, mediationAdRequest, bundle);
    }

    @Override
    public void requestInterstitialAd(final Context context, final CustomEventInterstitialListener customEventInterstitialListener,
                                      final String serverParameter, final MediationAdRequest mediationAdRequest, final Bundle bundle) {
        mInterstitialAdapter.requestInterstitialAd(context, customEventInterstitialListener, serverParameter, mediationAdRequest, bundle);
    }

    @Override
    public void showInterstitial() {
        mInterstitialAdapter.showInterstitial();
    }

    @Override
    public void onDestroy() {
        mBannerAdapter.onDestroy();
        mInterstitialAdapter.onDestroy();
    }

    @Override
    public void onPause() {
        mBannerAdapter.onPause();
        mInterstitialAdapter.onPause();
    }

    @Override
    public void onResume() {
        mBannerAdapter.onResume();
        mInterstitialAdapter.onResume();
    }
}
