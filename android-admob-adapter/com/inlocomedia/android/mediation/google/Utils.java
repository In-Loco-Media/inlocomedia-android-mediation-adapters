package com.inlocomedia.android.mediation.google;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.inlocomedia.android.ads.AdError;
import com.inlocomedia.android.ads.AdRequest;
import com.inlocomedia.android.ads.AdType;
import com.inlocomedia.android.ads.InLocoMedia;
import com.inlocomedia.android.ads.InLocoMediaOptions;
import com.inlocomedia.android.ads.profile.UserProfile;

import org.json.JSONObject;

import java.util.Date;

public final class Utils {

    private static final String KEY_AD_UNIT_ID = "ad_unit_id";
    private static final String KEY_APP_ID = "app_id";
    public static final String TAG = "InLocoMedia";

    static int adErrorToAdMobError(AdError error) {
        switch (error) {
            case NO_FILL:
                return com.google.android.gms.ads.AdRequest.ERROR_CODE_NO_FILL;
            case INVALID_REQUEST:
                return com.google.android.gms.ads.AdRequest.ERROR_CODE_INVALID_REQUEST;
            case NETWORK_NOT_AVAILABLE:
                return com.google.android.gms.ads.AdRequest.ERROR_CODE_NETWORK_ERROR;
            case UNAUTHORIZED:
                return com.google.android.gms.ads.AdRequest.ERROR_CODE_INVALID_REQUEST;
            case INTERNAL_ERROR:
                return com.google.android.gms.ads.AdRequest.ERROR_CODE_INTERNAL_ERROR;
            case TIMEOUT:
                return com.google.android.gms.ads.AdRequest.ERROR_CODE_NETWORK_ERROR;
            case GOOGLE_PLAY_SERVICES_NOT_FOUND:
                return com.google.android.gms.ads.AdRequest.ERROR_CODE_INVALID_REQUEST;
            case INVALID_SDK_VERSION:
                return com.google.android.gms.ads.AdRequest.ERROR_CODE_NO_FILL;
        }

        return com.google.android.gms.ads.AdRequest.ERROR_CODE_INTERNAL_ERROR;
    }

    public static AdRequest createAdRequest(MediationAdRequest mediationAdRequest, final String adUnitId, final String requestAgent) {
        final AdRequest adRequest = new AdRequest();
        adRequest.setUserProfile(new UserProfile(getGender(mediationAdRequest), getBirthdate(mediationAdRequest)));
        adRequest.setKeywords(mediationAdRequest.getKeywords());
        adRequest.setRequestAgent(requestAgent);
        adRequest.setAdUnitId(adUnitId);

        int taggedForChildDirectedTreatment = mediationAdRequest.taggedForChildDirectedTreatment();
        if (taggedForChildDirectedTreatment == MediationAdRequest.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE) {
            adRequest.setTaggedForChildren(true);
        } else if (taggedForChildDirectedTreatment == MediationAdRequest.TAG_FOR_CHILD_DIRECTED_TREATMENT_FALSE) {
            adRequest.setTaggedForChildren(false);
        }

        return adRequest;
    }

    private static Date getBirthdate(MediationAdRequest mediationAdRequest) {
        Date birthdate = mediationAdRequest.getBirthday();
        if (birthdate != null && birthdate.getTime() == -1) {
            birthdate = null;
        }
        return birthdate;
    }

    private static UserProfile.Gender getGender(MediationAdRequest mediationAdRequest) {
        int gender = mediationAdRequest.getGender();

        switch (gender) {
            case com.google.android.gms.ads.AdRequest.GENDER_MALE:
                return UserProfile.Gender.MALE;
            case com.google.android.gms.ads.AdRequest.GENDER_FEMALE:
                return UserProfile.Gender.FEMALE;
            case com.google.android.gms.ads.AdRequest.GENDER_UNKNOWN:
            default:
                return UserProfile.Gender.UNDEFINED;
        }
    }

    protected static AdType convertAdSizeToAdType(AdSize adSize) {
        if (adSize.equals(AdSize.SMART_BANNER)) {
            return AdType.SMART_BANNER;
        } else {
            return AdType.getBySize(adSize.getWidth(), adSize.getHeight());
        }
    }

    protected static String getAdUnitId(final String serverParameter) {

        if (serverParameter == null || serverParameter.isEmpty()) {
            return null;
        }

        try {

            JSONObject serverJson = new JSONObject(serverParameter);

            if (serverJson.has(KEY_AD_UNIT_ID)) {
                return serverJson.getString(KEY_AD_UNIT_ID);
            }

        } catch (Throwable t) {
            Log.w(TAG, "Extraction of server parameters has failed with exception " + t.getMessage());
        }

        return null;
    }

    protected static void setupInLocoMedia(Context context, String serverParameter) {

        if (serverParameter == null || serverParameter.isEmpty()) {
            return;
        }

        try {

            JSONObject serverJson = new JSONObject(serverParameter);
            String appId = null;

            if (serverJson.has(KEY_APP_ID)) {
                appId = serverJson.getString(KEY_APP_ID);
            }

            if (appId != null) {
                setupInLocoMediaOptions(context, appId);
            }
        } catch (Throwable t) {
            Log.w(TAG, "Extraction of server parameters has failed with exception " + t.getMessage());
        }
    }

    private static void setupInLocoMediaOptions(Context context, String appId) {
        InLocoMediaOptions options = InLocoMediaOptions.getInstance(context);
        options.setAdsKey(appId);
        options.setLogEnabled(true);
        InLocoMedia.init(context, options);
    }
}
