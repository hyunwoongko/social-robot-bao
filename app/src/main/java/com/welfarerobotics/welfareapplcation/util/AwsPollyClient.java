package com.welfarerobotics.welfareapplcation.util;

import android.annotation.SuppressLint;
import android.content.Context;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPollyPresigningClient;
import com.welfarerobotics.welfareapplcation.entity.cache.ServerCache;
import lombok.Data;

/**
 * @author : Hyunwoong
 * @when : 7/15/2019 6:15 PM
 * @homepage : https://github.com/gusdnd852
 */
@Data
public class AwsPollyClient {

    @SuppressLint("StaticFieldLeak")
    private static AwsPollyClient instance;

    private Context context;
    private CognitoCachingCredentialsProvider credentialsProvider;
    private AmazonPollyPresigningClient client;


    private AwsPollyClient(Context context) {
        this.context = context;
        String COGNITO_POOL_ID = ServerCache.getInstance().getAwsid();
        Regions MY_REGION = Regions.AP_NORTHEAST_2;

        credentialsProvider = new CognitoCachingCredentialsProvider(
                context,
                COGNITO_POOL_ID,
                MY_REGION
        );

        client = new AmazonPollyPresigningClient(credentialsProvider);
    }


    public static AwsPollyClient getInstance(Context context) {
        if (instance == null) {
            synchronized (AwsPollyClient.class) {
                if (instance == null)
                    instance = new AwsPollyClient(context);
            }
        }
        return instance;
    }
}
