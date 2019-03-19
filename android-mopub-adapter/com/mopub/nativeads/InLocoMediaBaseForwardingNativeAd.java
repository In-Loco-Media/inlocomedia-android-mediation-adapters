package com.mopub.nativeads;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import com.inlocomedia.android.ads.AdRequest;
import com.inlocomedia.android.ads.nativeads.NativeAdResponse;

public final class InLocoMediaBaseForwardingNativeAd extends StaticNativeAd {

    private NativeAdResponse mResponse;
    private Context mContext;

    private AdRequest mAdRequest;

    private final NativeClickHandler mNativeClickHandler;
    private final ImpressionTracker mImpressionTracker;

    public InLocoMediaBaseForwardingNativeAd(final Context context,
                                             final NativeAdResponse response,
                                             final NativeClickHandler nativeClickHandler,
                                             final ImpressionTracker impressionTracker,
                                             final AdRequest adRequest) {
        mResponse = response;
        mContext = context;
        mNativeClickHandler = nativeClickHandler;
        mImpressionTracker = impressionTracker;
        mAdRequest = adRequest;

        setTitle(mResponse.getTitle());
        setText(mResponse.getDescription());
        setCallToAction(mResponse.getCallToAction());
        setIconImageUrl(mResponse.getIconUrl());
        setMainImageUrl(mResponse.getMainImageUrl());
        setStarRating(mResponse.getRating());
    }

    @Override
    public void prepare(@NonNull final View view) {
        mImpressionTracker.addView(view, this);
        mNativeClickHandler.setOnClickListener(view, this);
    }

    @Override
    public void clear(@NonNull final View view) {
        mImpressionTracker.removeView(view);
        mNativeClickHandler.clearOnClickListener(view);
    }

    @Override
    public void recordImpression(@NonNull final View view) {
        if (mContext != null) {
            mResponse.registerImpression(mContext);
            notifyAdImpressed();
        }
    }

    @Override
    public void handleClick(@NonNull final View view) {
        if (mContext != null) {
            mResponse.performClick(mContext);
            notifyAdClicked();
        }
    }

    @Override
    public void destroy() {
        mResponse = null;
        mContext = null;
        mImpressionTracker.destroy();
    }

    @Nullable
    public AdRequest getAdRequest() {
        return mAdRequest;
    }

    @NonNull
    public List<String> getAllImageUrls() {
        final List<String> imageUrls = new ArrayList<>();
        if (getMainImageUrl() != null) {
            imageUrls.add(getMainImageUrl());
        }
        if (getIconImageUrl() != null) {
            imageUrls.add(getIconImageUrl());
        }

        return imageUrls;
    }
}
