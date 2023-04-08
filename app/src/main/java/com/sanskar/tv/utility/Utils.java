/*
 * Copyright 2019 Google LLC. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sanskar.tv.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import androidx.fragment.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaQueueItem;
import com.google.android.gms.cast.MediaStatus;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.gson.Gson;
import com.sanskar.tv.R;
import com.sanskar.tv.cast.ExpandedControlsActivity;
import com.sanskar.tv.cast.QueueDataProvider;
import com.sanskar.tv.module.HomeModule.Pojo.Videos;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;


/**
 * A collection of utility methods, all static.
 */
public class Utils {

    private static final String TAG = "Utils";

    private static final int PRELOAD_TIME_S = 20;

    /**
     * Making sure public utility methods remain static
     */
    private Utils() {
    }

    @SuppressWarnings("deprecation")
    /**
     * Returns the screen/display size
     *
     */
    public static Point getDisplaySize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        return new Point(width, height);
    }

    /**
     * Returns {@code true} if and only if the screen orientation is portrait.
     */
    public static boolean isOrientationPortrait(Context context) {
        return context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * Shows an error dialog with a given text message.
     */
    public static void showErrorDialog(Context context, String errorString) {
        new AlertDialog.Builder(context).setTitle("Error Occured")
                .setMessage(errorString)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .create()
                .show();
    }

    /**
     * Shows an "Oops" error dialog with a text provided by a resource ID
     */


    /**
     * Gets the version of app.
     */
    public static String getAppVersionName(Context context) {
        String versionString = null;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    0 /* basic info */);
            versionString = info.versionName;
        } catch (Exception e) {
            // do nothing
        }
        return versionString;
    }

    /**
     * Shows a (long) toast.
     */
    public static void showToast(Context context, int resourceId) {
        Toast.makeText(context, context.getString(resourceId), Toast.LENGTH_LONG).show();
    }

    /**
     * Formats time from milliseconds to hh:mm:ss string format.
     */
    public static String formatMillis(int millisec) {
        int seconds = (int) (millisec / 1000);
        int hours = seconds / (60 * 60);
        seconds %= (60 * 60);
        int minutes = seconds / 60;
        seconds %= 60;

        String time;
        if (hours > 0) {
            time = String.format(Locale.ROOT, "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            time = String.format(Locale.ROOT, "%d:%02d", minutes, seconds);
        }
        return time;
    }

    /**
     * Show a popup to select whether the selected item should play immediately, be added to the
     * end of queue or be added to the queue right after the current item.
     */
    public static void showQueuePopup(final Context context, View view, final Videos mediaInfo) {
        CastSession castSession =
                CastContext.getSharedInstance(context).getSessionManager().getCurrentCastSession();
        if (castSession == null || !castSession.isConnected()) {
            Log.w(TAG, "showQueuePopup(): not connected to a cast device");
            return;
        }
        final RemoteMediaClient remoteMediaClient = castSession.getRemoteMediaClient();
        if (remoteMediaClient == null) {
            Log.w(TAG, "showQueuePopup(): null RemoteMediaClient");
            return;
        }
        final QueueDataProvider provider = QueueDataProvider.getInstance(context);
        PopupMenu popup = new PopupMenu(context, view);
        popup.getMenuInflater().inflate(
                provider.isQueueDetached() || provider.getCount() == 0
                        ? R.menu.detached_popup_add_to_queue
                        : R.menu.popup_add_to_queue, popup.getMenu());
        PopupMenu.OnMenuItemClickListener clickListener = new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                QueueDataProvider provider = QueueDataProvider.getInstance(context);
                Gson gson = new Gson();
                String media = gson.toJson(mediaInfo.getVideo_url());
                JSONObject mediajson=null;
                try {
                    mediajson = new JSONObject(media);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                MediaQueueItem queueItem = null;
                MediaInfo mediaInfo1;
                mediaInfo1 = new MediaInfo.Builder(mediaInfo.getVideo_url()).build();

                    queueItem = new MediaQueueItem.Builder(mediaInfo1).setAutoplay(
                            true).setPreloadTime(PRELOAD_TIME_S).build();

             /*   MediaSourceInfo source3 = new MediaSourceInfo.Builder().setUrl("https://sample-videos.com/audio/mp3/crowd-cheering.mp3") //http://stream3.polskieradio.pl:8904/;
                        .setTitle("Source 3")
                        .setImageUrl("https://cdn.dribbble.com/users/20781/screenshots/573506/podcast_logo.jpg")
                        .build();
                MediaInfo mediaInfo1;
                mediaInfo1 = new MediaInfo.Builder("Stringfile").build();*/

                MediaQueueItem[] newItemArray = new MediaQueueItem[]{queueItem};
                String toastMessage = null;
                if (provider.isQueueDetached() && provider.getCount() > 0) {
                    if ((menuItem.getItemId() == R.id.action_play_now)
                            || (menuItem.getItemId() == R.id.action_add_to_queue)) {
                        MediaQueueItem[] items = Utils
                                .rebuildQueueAndAppend(provider.getItems(), queueItem);
                        remoteMediaClient.queueLoad(items, provider.getCount(),
                                MediaStatus.REPEAT_MODE_REPEAT_OFF, null);
                    } else {
                        return false;
                    }
                } else {
                    if (provider.getCount() == 0) {
                        remoteMediaClient.queueLoad(newItemArray, 0,
                                MediaStatus.REPEAT_MODE_REPEAT_OFF, null);
                    } else {
                        int currentId = provider.getCurrentItemId();
                        if (menuItem.getItemId() == R.id.action_play_now) {
                            remoteMediaClient.queueInsertAndPlayItem(queueItem, currentId, null);
                        } else if (menuItem.getItemId() == R.id.action_play_next) {
                            int currentPosition = provider.getPositionByItemId(currentId);
                            if (currentPosition == provider.getCount() - 1) {
                                //we are adding to the end of queue
                                remoteMediaClient.queueAppendItem(queueItem, null);
                            } else {
                                int nextItemId = provider.getItem(currentPosition + 1).getItemId();
                                remoteMediaClient.queueInsertItems(newItemArray, nextItemId, null);
                            }
                            toastMessage = context.getString(
                                    R.string.queue_item_added_to_play_next);
                        } else if (menuItem.getItemId() == R.id.action_add_to_queue) {
                            remoteMediaClient.queueAppendItem(queueItem, null);
                            toastMessage = context.getString(R.string.queue_item_added_to_queue);
                        } else {
                            return false;
                        }
                    }
                }
                if (menuItem.getItemId() == R.id.action_play_now) {
                    Intent intent = new Intent(context, ExpandedControlsActivity.class);
                    context.startActivity(intent);
                }
                if (!TextUtils.isEmpty(toastMessage)) {
                    Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        };
        popup.setOnMenuItemClickListener(clickListener);
        popup.show();
    }

    public static MediaQueueItem[] rebuildQueue(List<MediaQueueItem> items) {
        if (items == null || items.isEmpty()) {
            return null;
        }
        MediaQueueItem[] rebuiltQueue = new MediaQueueItem[items.size()];
        for (int i = 0; i < items.size(); i++) {
            rebuiltQueue[i] = rebuildQueueItem(items.get(i));
        }

        return rebuiltQueue;
    }

    public static MediaQueueItem[] rebuildQueueAndAppend(List<MediaQueueItem> items,
                                                         MediaQueueItem currentItem) {
        if (items == null || items.isEmpty()) {
            return new MediaQueueItem[]{currentItem};
        }
        MediaQueueItem[] rebuiltQueue = new MediaQueueItem[items.size() + 1];
        for (int i = 0; i < items.size(); i++) {
            rebuiltQueue[i] = rebuildQueueItem(items.get(i));
        }
        rebuiltQueue[items.size()] = currentItem;

        return rebuiltQueue;
    }

    public static MediaQueueItem rebuildQueueItem(MediaQueueItem item) {
        return new MediaQueueItem.Builder(item).clearItemId().build();
    }

    public static void hideKeyboard(FragmentActivity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    }

