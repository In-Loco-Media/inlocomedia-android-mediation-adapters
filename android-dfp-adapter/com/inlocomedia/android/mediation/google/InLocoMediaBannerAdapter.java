package com.inlocomedia.android.mediation.google;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventBanner;
import com.google.android.gms.ads.mediation.customevent.CustomEventBannerListener;
import com.inlocomedia.android.ads.AdError;
import com.inlocomedia.android.ads.AdType;
import com.inlocomedia.android.ads.AdView;
import com.inlocomedia.android.ads.AdViewListener;

public class InLocoMediaBannerAdapter implements CustomEventBanner {

    public static final String TAG = "InLocoMedia";

    private AdView mAdView;
    private String mRequestAgent;

    public InLocoMediaBannerAdapter(final String requestAgent) {
        this.mRequestAgent = requestAgent;
    }

    @Override
    public void requestBannerAd(final Context context, final CustomEventBannerListener listener, final String serverParameter, final AdSize size,
                                final MediationAdRequest mediationAdRequest, final Bundle bundle) {

        Utils.setupInLocoMedia(context, serverParameter);

        AdType adType = Utils.convertAdSizeToAdType(size);
        if (adType == null) {
            Log.w(TAG, "Requested AdSize is incompatible: " + size);
            listener.onAdFailedToLoad(com.google.android.gms.ads.AdRequest.ERROR_CODE_INVALID_REQUEST);
            return;
        }

        mAdView = new AdView(context);
        mAdView.setType(adType);

        mAdView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Ad clicked");
                listener.onAdClicked();
            }
        });

        mAdView.setAdListener(new AdViewListener() {
            @Override
            public void onAdViewReady(AdView adView) {
                Log.i(TAG, "Ad ready");
                listener.onAdLoaded(adView);
            }

            @Override
            public void onAdLeftApplication(AdView adView) {
                Log.i(TAG, "Ad left application");
                listener.onAdLeftApplication();
            }

            @Override
            public void onAdError(final AdView adView, final AdError error) {
                Log.i(TAG, "Ad error: " + error);
                listener.onAdFailedToLoad(Utils.adErrorToAdMobError(error));
                mAdView.destroy();
            }
        });

        String adUnitId = Utils.getAdUnitId(serverParameter);
        mAdView.loadAd(Utils.createAdRequest(mediationAdRequest, adUnitId, mRequestAgent));
        Log.i(TAG, "Ad requested. AdSize: " + size.toString() + " AdType: " + adType.toString());

    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause(false);
        }
    }

    @Override
    public void onResume() {
        if (mAdView != null) {
            mAdView.resume();
        }
    }
}
