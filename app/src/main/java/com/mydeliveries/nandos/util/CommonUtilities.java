package com.mydeliveries.nandos.util;

/**
 * Created by Andrew on 14/06/2015.
 */
import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {

    // give your server registration url here
    public static final String SERVER_URL = "http://www.mydeliveries.co.za/order_manager/v1/register.php";

    // Google project id
    public static final String SENDER_ID = "283055378703";

    /**
     * Tag used on log messages.
     */
    public static final String TAG =  "GCM";

    public static final String DISPLAY_MESSAGE_ACTION =
            "com.deliveryboy.com.mydeliveries.com.mydeliveries.nandos.DISPLAY_MESSAGE";

    public static final String EXTRA_MESSAGE = "message";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}