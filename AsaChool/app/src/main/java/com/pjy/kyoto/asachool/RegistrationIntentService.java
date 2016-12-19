package com.pjy.kyoto.asachool;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by admin on 2016/12/16.
 */

public class RegistrationIntentService extends IntentService {
    private static final String TAG = "RegistrationIntentService";
    private static final String GCM_DEFAULT_SENDER_ID = "897050172726";
    private static final String GCM_TOPIC_FOR_NOTICE = "choolcheck";
    public static final String REGISTRATION_SUCCESS = "RegistrationSuccess";
    public static final String REGISTRATION_ERROR = "RegistrationError";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Intent registrationComplete = null;

        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(GCM_DEFAULT_SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i(TAG, "GCM Registration Token: " + token);

            registrationComplete = new Intent(REGISTRATION_SUCCESS);
            registrationComplete.putExtra("token",token);
            //TODO : sending token id to server
//            Bundle data = new Bundle();
//            data.putString("my_message", "Hello World");
//            data.putString("my_action","SAY_HELLO");
//            GoogleCloudMessaging.send(GCM_DEFAULT_SENDER_ID + "@gcm.googleapis.com", token, data);
////            sendRegistrationToServer(token);


            // subscribe choolcheck topic
            GcmPubSub pubSub = GcmPubSub.getInstance(this);
            pubSub.subscribe(token, "/topics/" + GCM_TOPIC_FOR_NOTICE, null);

        } catch (IOException e) {
            Log.e(TAG, e.getLocalizedMessage());
            registrationComplete = new Intent(REGISTRATION_ERROR);
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }
}