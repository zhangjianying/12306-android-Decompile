package org.apache.cordova;

import android.app.Activity;
import android.media.AudioManager;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;

public class AudioHandler extends CordovaPlugin
{
  public static String TAG = "AudioHandler";
  ArrayList<AudioPlayer> pausedForPhone = new ArrayList();
  HashMap<String, AudioPlayer> players = new HashMap();

  private boolean release(String paramString)
  {
    if (!this.players.containsKey(paramString))
      return false;
    AudioPlayer localAudioPlayer = (AudioPlayer)this.players.get(paramString);
    this.players.remove(paramString);
    localAudioPlayer.destroy();
    return true;
  }

  public boolean execute(String paramString, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
    throws JSONException
  {
    PluginResult.Status localStatus = PluginResult.Status.OK;
    if (paramString.equals("startRecordingAudio"))
      startRecordingAudio(paramJSONArray.getString(0), FileHelper.stripFileProtocol(paramJSONArray.getString(1)));
    while (true)
    {
      paramCallbackContext.sendPluginResult(new PluginResult(localStatus, ""));
      return true;
      if (paramString.equals("stopRecordingAudio"))
      {
        stopRecordingAudio(paramJSONArray.getString(0));
        continue;
      }
      if (paramString.equals("startPlayingAudio"))
      {
        startPlayingAudio(paramJSONArray.getString(0), FileHelper.stripFileProtocol(paramJSONArray.getString(1)));
        continue;
      }
      if (paramString.equals("seekToAudio"))
      {
        seekToAudio(paramJSONArray.getString(0), paramJSONArray.getInt(1));
        continue;
      }
      if (paramString.equals("pausePlayingAudio"))
      {
        pausePlayingAudio(paramJSONArray.getString(0));
        continue;
      }
      if (paramString.equals("stopPlayingAudio"))
      {
        stopPlayingAudio(paramJSONArray.getString(0));
        continue;
      }
      if (paramString.equals("setVolume"))
      {
        try
        {
          setVolume(paramJSONArray.getString(0), Float.parseFloat(paramJSONArray.getString(1)));
        }
        catch (NumberFormatException localNumberFormatException)
        {
        }
        continue;
      }
      if (paramString.equals("getCurrentPositionAudio"))
      {
        paramCallbackContext.sendPluginResult(new PluginResult(localStatus, getCurrentPositionAudio(paramJSONArray.getString(0))));
        return true;
      }
      if (paramString.equals("getDurationAudio"))
      {
        paramCallbackContext.sendPluginResult(new PluginResult(localStatus, getDurationAudio(paramJSONArray.getString(0), paramJSONArray.getString(1))));
        return true;
      }
      if (!paramString.equals("create"))
        break;
      String str = paramJSONArray.getString(0);
      AudioPlayer localAudioPlayer = new AudioPlayer(this, str, FileHelper.stripFileProtocol(paramJSONArray.getString(1)));
      this.players.put(str, localAudioPlayer);
    }
    if (paramString.equals("release"))
    {
      paramCallbackContext.sendPluginResult(new PluginResult(localStatus, release(paramJSONArray.getString(0))));
      return true;
    }
    return false;
  }

  public int getAudioOutputDevice()
  {
    AudioManager localAudioManager = (AudioManager)this.cordova.getActivity().getSystemService("audio");
    if (localAudioManager.getRouting(0) == 1)
      return 1;
    if (localAudioManager.getRouting(0) == 2)
      return 2;
    return -1;
  }

  public float getCurrentPositionAudio(String paramString)
  {
    AudioPlayer localAudioPlayer = (AudioPlayer)this.players.get(paramString);
    if (localAudioPlayer != null)
      return (float)localAudioPlayer.getCurrentPosition() / 1000.0F;
    return -1.0F;
  }

  public float getDurationAudio(String paramString1, String paramString2)
  {
    AudioPlayer localAudioPlayer1 = (AudioPlayer)this.players.get(paramString1);
    if (localAudioPlayer1 != null)
      return localAudioPlayer1.getDuration(paramString2);
    AudioPlayer localAudioPlayer2 = new AudioPlayer(this, paramString1, paramString2);
    this.players.put(paramString1, localAudioPlayer2);
    return localAudioPlayer2.getDuration(paramString2);
  }

  public void onDestroy()
  {
    Iterator localIterator = this.players.values().iterator();
    while (localIterator.hasNext())
      ((AudioPlayer)localIterator.next()).destroy();
    this.players.clear();
  }

  public Object onMessage(String paramString, Object paramObject)
  {
    if (paramString.equals("telephone"))
    {
      Iterator localIterator1;
      if (("ringing".equals(paramObject)) || ("offhook".equals(paramObject)))
        localIterator1 = this.players.values().iterator();
      while (localIterator1.hasNext())
      {
        AudioPlayer localAudioPlayer = (AudioPlayer)localIterator1.next();
        if (localAudioPlayer.getState() != AudioPlayer.STATE.MEDIA_RUNNING.ordinal())
          continue;
        this.pausedForPhone.add(localAudioPlayer);
        localAudioPlayer.pausePlaying();
        continue;
        if (!"idle".equals(paramObject))
          break;
        Iterator localIterator2 = this.pausedForPhone.iterator();
        while (localIterator2.hasNext())
          ((AudioPlayer)localIterator2.next()).startPlaying(null);
        this.pausedForPhone.clear();
      }
    }
    return null;
  }

  public void onReset()
  {
    onDestroy();
  }

  public void pausePlayingAudio(String paramString)
  {
    AudioPlayer localAudioPlayer = (AudioPlayer)this.players.get(paramString);
    if (localAudioPlayer != null)
      localAudioPlayer.pausePlaying();
  }

  public void seekToAudio(String paramString, int paramInt)
  {
    AudioPlayer localAudioPlayer = (AudioPlayer)this.players.get(paramString);
    if (localAudioPlayer != null)
      localAudioPlayer.seekToPlaying(paramInt);
  }

  public void setAudioOutputDevice(int paramInt)
  {
    AudioManager localAudioManager = (AudioManager)this.cordova.getActivity().getSystemService("audio");
    if (paramInt == 2)
    {
      localAudioManager.setRouting(0, 2, -1);
      return;
    }
    if (paramInt == 1)
    {
      localAudioManager.setRouting(0, 1, -1);
      return;
    }
    System.out.println("AudioHandler.setAudioOutputDevice() Error: Unknown output device.");
  }

  public void setVolume(String paramString, float paramFloat)
  {
    AudioPlayer localAudioPlayer = (AudioPlayer)this.players.get(paramString);
    if (localAudioPlayer != null)
    {
      localAudioPlayer.setVolume(paramFloat);
      return;
    }
    System.out.println("AudioHandler.setVolume() Error: Unknown Audio Player " + paramString);
  }

  public void startPlayingAudio(String paramString1, String paramString2)
  {
    AudioPlayer localAudioPlayer = (AudioPlayer)this.players.get(paramString1);
    if (localAudioPlayer == null)
    {
      localAudioPlayer = new AudioPlayer(this, paramString1, paramString2);
      this.players.put(paramString1, localAudioPlayer);
    }
    localAudioPlayer.startPlaying(paramString2);
  }

  public void startRecordingAudio(String paramString1, String paramString2)
  {
    AudioPlayer localAudioPlayer = (AudioPlayer)this.players.get(paramString1);
    if (localAudioPlayer == null)
    {
      localAudioPlayer = new AudioPlayer(this, paramString1, paramString2);
      this.players.put(paramString1, localAudioPlayer);
    }
    localAudioPlayer.startRecording(paramString2);
  }

  public void stopPlayingAudio(String paramString)
  {
    AudioPlayer localAudioPlayer = (AudioPlayer)this.players.get(paramString);
    if (localAudioPlayer != null)
      localAudioPlayer.stopPlaying();
  }

  public void stopRecordingAudio(String paramString)
  {
    AudioPlayer localAudioPlayer = (AudioPlayer)this.players.get(paramString);
    if (localAudioPlayer != null)
      localAudioPlayer.stopRecording();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.AudioHandler
 * JD-Core Version:    0.6.0
 */