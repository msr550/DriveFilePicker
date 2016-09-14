package com.rbt.filepicker;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.OpenFileActivityBuilder;


public class DriveActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    /**
     * DriveId of an existing folder to be used as a parent folder in
     * folder operations samples.
     */
    public static final String EXISTING_FOLDER_ID = "0B2EEtIjPUdX6MERsWlYxN3J6RU0";
    /**
     * DriveId of an existing file to be used in file operation samples..
     */
    public static final String EXISTING_FILE_ID = "0ByfSjdPVs9MZTHBmMVdSeWxaNTg";
    /**
     * Extra for account name.
     */
    protected static final String EXTRA_ACCOUNT_NAME = "account_name";
    /**
     * Request code for auto Google Play Services error resolution.
     */
    protected static final int REQUEST_CODE_RESOLUTION = 1;
    /**
     * Next available request code.
     */
    protected static final int NEXT_AVAILABLE_REQUEST_CODE = 2;
    private static final String TAG = "GoogleDriveActivity";
    private static final int REQUEST_CODE_OPENER = 2;
    /**
     * Google API client.
     */
    private GoogleApiClient mGoogleApiClient;

    /**
     * Called when activity gets visible. A connection to Drive services need to
     * be initiated as soon as the activity is visible. Registers
     * {@code ConnectionCallbacks} and {@code OnConnectionFailedListener} on the
     * activities itself.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addScope(Drive.SCOPE_APPFOLDER) // required for App Folder sample
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        mGoogleApiClient.connect();
    }

    /**
     * Handles resolution callbacks.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_OPENER:
                    if (resultCode == RESULT_OK) {
                        DriveId driveId = (DriveId) data.getParcelableExtra(
                                OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);
                       /* DriveFile file = Drive.DriveApi.getFile(mGoogleApiClient, driveId);

                        DriveResource.MetadataResult mdRslt = file.getMetadata(mGoogleApiClient).await();
                        if (mdRslt != null && mdRslt.getStatus().isSuccess()) {
                            String link = mdRslt.getMetadata().getWebContentLink();
                            Log.i("====LINK", "===" + link);
                        }*/
                        showMessage("Selected file's ID: " + driveId);
                    }
                    finish();
                    break;
                case REQUEST_CODE_RESOLUTION:
                    mGoogleApiClient.connect();
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
            }
        }

    }

    /**
     * Called when activity gets invisible. Connection to Drive service needs to
     * be disconnected as soon as an activity is invisible.
     */
    @Override
    protected void onPause() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }

    /**
     * Called when {@code mGoogleApiClient} is disconnected.
     */
    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "GoogleApiClient connection suspended");
    }

    /**
     * Called when {@code mGoogleApiClient} is trying to connect but failed.
     * Handle {@code result.getResolution()} if there is a resolution is
     * available.
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());
        if (!result.hasResolution()) {
            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0).show();
            return;
        }
        try {
            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Exception while starting resolution activity", e);
        }
    }

    /**
     * Shows a toast message.
     */
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Getter for the {@code GoogleApiClient}.
     */
    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

  /*  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);
    }*/

    @Override
    public void onConnected(Bundle connectionHint) {
        IntentSender intentSender = Drive.DriveApi
                .newOpenFileActivityBuilder()
                .setMimeType(new String[]{"application/msword", " application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.google-apps.document", "application/pdf"})
                .build(getGoogleApiClient());
        try {
            startIntentSenderForResult(
                    intentSender, REQUEST_CODE_OPENER, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            Log.w(TAG, "Unable to send intent", e);
        }
    }

}
