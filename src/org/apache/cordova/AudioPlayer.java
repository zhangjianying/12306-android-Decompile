package org.apache.cordova;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.cordova.api.CordovaInterface;

public class AudioPlayer
  implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener
{
  private static final String LOG_TAG = "AudioPlayer";
  private static int MEDIA_DURATION;
  private static int MEDIA_ERROR;
  private static int MEDIA_ERR_ABORTED;
  private static int MEDIA_ERR_DECODE;
  private static int MEDIA_ERR_NETWORK;
  private static int MEDIA_ERR_NONE_ACTIVE;
  private static int MEDIA_ERR_NONE_SUPPORTED;
  private static int MEDIA_POSITION;
  private static int MEDIA_STATE = 1;
  private String audioFile = null;
  private float duration = -1.0F;
  private AudioHandler handler;
  private String id;
  private MODE mode = MODE.NONE;
  private MediaPlayer player = null;
  private boolean prepareOnly = true;
  private MediaRecorder recorder = null;
  private int seekOnPrepared = 0;
  private STATE state = STATE.MEDIA_NONE;
  private String tempFile = null;

  static
  {
    MEDIA_DURATION = 2;
    MEDIA_POSITION = 3;
    MEDIA_ERROR = 9;
    MEDIA_ERR_NONE_ACTIVE = 0;
    MEDIA_ERR_ABORTED = 1;
    MEDIA_ERR_NETWORK = 2;
    MEDIA_ERR_DECODE = 3;
    MEDIA_ERR_NONE_SUPPORTED = 4;
  }

  public AudioPlayer(AudioHandler paramAudioHandler, String paramString1, String paramString2)
  {
    this.handler = paramAudioHandler;
    this.id = paramString1;
    this.audioFile = paramString2;
    this.recorder = new MediaRecorder();
    if (Environment.getExternalStorageState().equals("mounted"))
    {
      this.tempFile = (Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmprecording.3gp");
      return;
    }
    this.tempFile = ("/data/data/" + paramAudioHandler.cordova.getActivity().getPackageName() + "/cache/tmprecording.3gp");
  }

  private float getDurationInSeconds()
  {
    return this.player.getDuration() / 1000.0F;
  }

  private void loadAudioFile(String paramString)
    throws IllegalArgumentException, SecurityException, IllegalStateException, IOException
  {
    if (isStreaming(paramString))
    {
      this.player.setDataSource(paramString);
      this.player.setAudioStreamType(3);
      setMode(MODE.PLAY);
      setState(STATE.MEDIA_STARTING);
      this.player.setOnPreparedListener(this);
      this.player.prepareAsync();
      return;
    }
    if (paramString.startsWith("/android_asset/"))
    {
      String str = paramString.substring(15);
      AssetFileDescriptor localAssetFileDescriptor = this.handler.cordova.getActivity().getAssets().openFd(str);
      this.player.setDataSource(localAssetFileDescriptor.getFileDescriptor(), localAssetFileDescriptor.getStartOffset(), localAssetFileDescriptor.getLength());
    }
    while (true)
    {
      setState(STATE.MEDIA_STARTING);
      this.player.setOnPreparedListener(this);
      this.player.prepare();
      this.duration = getDurationInSeconds();
      return;
      if (new File(paramString).exists())
      {
        FileInputStream localFileInputStream = new FileInputStream(paramString);
        this.player.setDataSource(localFileInputStream.getFD());
        continue;
      }
      this.player.setDataSource("/sdcard/" + paramString);
    }
  }

  private boolean playMode()
  {
    switch (1.$SwitchMap$org$apache$cordova$AudioPlayer$MODE[this.mode.ordinal()])
    {
    case 1:
    default:
    case 2:
      while (true)
      {
        return true;
        setMode(MODE.PLAY);
      }
    case 3:
    }
    Log.d("AudioPlayer", "AudioPlayer Error: Can't play in record mode.");
    this.handler.webView.sendJavascript("cordova.require('cordova/plugin/Media').onStatus('" + this.id + "', " + MEDIA_ERROR + ", { \"code\":" + MEDIA_ERR_ABORTED + "});");
    return false;
  }

  private boolean readyPlayer(String paramString)
  {
    if (playMode());
    switch (1.$SwitchMap$org$apache$cordova$AudioPlayer$STATE[this.state.ordinal()])
    {
    default:
      Log.d("AudioPlayer", "AudioPlayer Error: startPlaying() called during invalid state: " + this.state);
      this.handler.webView.sendJavascript("cordova.require('cordova/plugin/Media').onStatus('" + this.id + "', " + MEDIA_ERROR + ", { \"code\":" + MEDIA_ERR_ABORTED + "});");
      return false;
    case 1:
      if (this.player == null)
        this.player = new MediaPlayer();
      try
      {
        loadAudioFile(paramString);
        return false;
      }
      catch (Exception localException2)
      {
        this.handler.webView.sendJavascript("cordova.require('cordova/plugin/Media').onStatus('" + this.id + "', " + MEDIA_ERROR + ", { \"code\":" + MEDIA_ERR_ABORTED + "});");
        return false;
      }
    case 2:
      Log.d("AudioPlayer", "AudioPlayer Loading: startPlaying() called during media preparation: " + STATE.MEDIA_STARTING.ordinal());
      this.prepareOnly = false;
      return false;
    case 3:
    case 4:
    case 5:
      return true;
    case 6:
    }
    if (this.audioFile.compareTo(paramString) == 0)
    {
      this.player.seekTo(0);
      this.player.pause();
      return true;
    }
    this.player.reset();
    try
    {
      loadAudioFile(paramString);
      return false;
    }
    catch (Exception localException1)
    {
      this.handler.webView.sendJavascript("cordova.require('cordova/plugin/Media').onStatus('" + this.id + "', " + MEDIA_ERROR + ", { \"code\":" + MEDIA_ERR_ABORTED + "});");
    }
    return false;
  }

  private void setMode(MODE paramMODE)
  {
    if (this.mode != paramMODE);
    this.mode = paramMODE;
  }

  private void setState(STATE paramSTATE)
  {
    if (this.state != paramSTATE)
      this.handler.webView.sendJavascript("cordova.require('cordova/plugin/Media').onStatus('" + this.id + "', " + MEDIA_STATE + ", " + paramSTATE.ordinal() + ");");
    this.state = paramSTATE;
  }

  public void destroy()
  {
    if (this.player != null)
    {
      if ((this.state == STATE.MEDIA_RUNNING) || (this.state == STATE.MEDIA_PAUSED))
      {
        this.player.stop();
        setState(STATE.MEDIA_STOPPED);
      }
      this.player.release();
      this.player = null;
    }
    if (this.recorder != null)
    {
      stopRecording();
      this.recorder.release();
      this.recorder = null;
    }
  }

  public long getCurrentPosition()
  {
    if ((this.state == STATE.MEDIA_RUNNING) || (this.state == STATE.MEDIA_PAUSED))
    {
      int i = this.player.getCurrentPosition();
      this.handler.webView.sendJavascript("cordova.require('cordova/plugin/Media').onStatus('" + this.id + "', " + MEDIA_POSITION + ", " + i / 1000.0F + ");");
      return i;
    }
    return -1L;
  }

  public float getDuration(String paramString)
  {
    if (this.recorder != null)
      return -2.0F;
    if (this.player != null)
      return this.duration;
    this.prepareOnly = true;
    startPlaying(paramString);
    return this.duration;
  }

  public int getState()
  {
    return this.state.ordinal();
  }

  public boolean isStreaming(String paramString)
  {
    return (paramString.contains("http://")) || (paramString.contains("https://"));
  }

  public void moveFile(String paramString)
  {
    File localFile = new File(this.tempFile);
    if (!paramString.startsWith("/"))
      if (!Environment.getExternalStorageState().equals("mounted"))
        break label146;
    label146: for (paramString = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + paramString; ; paramString = "/data/data/" + this.handler.cordova.getActivity().getPackageName() + "/cache/" + paramString)
    {
      String str = "renaming " + this.tempFile + " to " + paramString;
      Log.d("AudioPlayer", str);
      if (!localFile.renameTo(new File(paramString)))
        Log.e("AudioPlayer", "FAILED " + str);
      return;
    }
  }

  public void onCompletion(MediaPlayer paramMediaPlayer)
  {
    Log.d("AudioPlayer", "on completion is calling stopped");
    setState(STATE.MEDIA_STOPPED);
  }

  public boolean onError(MediaPlayer paramMediaPlayer, int paramInt1, int paramInt2)
  {
    Log.d("AudioPlayer", "AudioPlayer.onError(" + paramInt1 + ", " + paramInt2 + ")");
    this.player.stop();
    this.player.release();
    this.handler.webView.sendJavascript("cordova.require('cordova/plugin/Media').onStatus('" + this.id + "', { \"code\":" + paramInt1 + "});");
    return false;
  }

  public void onPrepared(MediaPlayer paramMediaPlayer)
  {
    this.player.setOnCompletionListener(this);
    seekToPlaying(this.seekOnPrepared);
    if (!this.prepareOnly)
    {
      this.player.start();
      setState(STATE.MEDIA_RUNNING);
      this.seekOnPrepared = 0;
    }
    while (true)
    {
      this.duration = getDurationInSeconds();
      this.prepareOnly = true;
      this.handler.webView.sendJavascript("cordova.require('cordova/plugin/Media').onStatus('" + this.id + "', " + MEDIA_DURATION + "," + this.duration + ");");
      return;
      setState(STATE.MEDIA_STARTING);
    }
  }

  public void pausePlaying()
  {
    if ((this.state == STATE.MEDIA_RUNNING) && (this.player != null))
    {
      this.player.pause();
      setState(STATE.MEDIA_PAUSED);
      return;
    }
    Log.d("AudioPlayer", "AudioPlayer Error: pausePlaying() called during invalid state: " + this.state.ordinal());
    this.handler.webView.sendJavascript("cordova.require('cordova/plugin/Media').onStatus('" + this.id + "', " + MEDIA_ERROR + ", { \"code\":" + MEDIA_ERR_NONE_ACTIVE + "});");
  }

  public void seekToPlaying(int paramInt)
  {
    if (readyPlayer(this.audioFile))
    {
      this.player.seekTo(paramInt);
      Log.d("AudioPlayer", "Send a onStatus update for the new seek");
      this.handler.webView.sendJavascript("cordova.require('cordova/plugin/Media').onStatus('" + this.id + "', " + MEDIA_POSITION + ", " + paramInt / 1000.0F + ");");
      return;
    }
    this.seekOnPrepared = paramInt;
  }

  public void setVolume(float paramFloat)
  {
    this.player.setVolume(paramFloat, paramFloat);
  }

  public void startPlaying(String paramString)
  {
    if ((readyPlayer(paramString)) && (this.player != null))
    {
      this.player.start();
      setState(STATE.MEDIA_RUNNING);
      this.seekOnPrepared = 0;
      return;
    }
    this.prepareOnly = false;
  }

  public void startRecording(String paramString)
  {
    switch (1.$SwitchMap$org$apache$cordova$AudioPlayer$MODE[this.mode.ordinal()])
    {
    default:
      return;
    case 1:
      Log.d("AudioPlayer", "AudioPlayer Error: Can't record in play mode.");
      this.handler.webView.sendJavascript("cordova.require('cordova/plugin/Media').onStatus('" + this.id + "', " + MEDIA_ERROR + ", { \"code\":" + MEDIA_ERR_ABORTED + "});");
      return;
    case 2:
      this.audioFile = paramString;
      this.recorder.setAudioSource(1);
      this.recorder.setOutputFormat(0);
      this.recorder.setAudioEncoder(0);
      this.recorder.setOutputFile(this.tempFile);
      try
      {
        this.recorder.prepare();
        this.recorder.start();
        setState(STATE.MEDIA_RUNNING);
        return;
      }
      catch (IllegalStateException localIllegalStateException)
      {
        localIllegalStateException.printStackTrace();
        this.handler.webView.sendJavascript("cordova.require('cordova/plugin/Media').onStatus('" + this.id + "', " + MEDIA_ERROR + ", { \"code\":" + MEDIA_ERR_ABORTED + "});");
        return;
      }
      catch (IOException localIOException)
      {
        while (true)
          localIOException.printStackTrace();
      }
    case 3:
    }
    Log.d("AudioPlayer", "AudioPlayer Error: Already recording.");
    this.handler.webView.sendJavascript("cordova.require('cordova/plugin/Media').onStatus('" + this.id + "', " + MEDIA_ERROR + ", { \"code\":" + MEDIA_ERR_ABORTED + "});");
  }

  public void stopPlaying()
  {
    if ((this.state == STATE.MEDIA_RUNNING) || (this.state == STATE.MEDIA_PAUSED))
    {
      this.player.pause();
      this.player.seekTo(0);
      Log.d("AudioPlayer", "stopPlaying is calling stopped");
      setState(STATE.MEDIA_STOPPED);
      return;
    }
    Log.d("AudioPlayer", "AudioPlayer Error: stopPlaying() called during invalid state: " + this.state.ordinal());
    this.handler.webView.sendJavascript("cordova.require('cordova/plugin/Media').onStatus('" + this.id + "', " + MEDIA_ERROR + ", { \"code\":" + MEDIA_ERR_NONE_ACTIVE + "});");
  }

  public void stopRecording()
  {
    if (this.recorder != null);
    try
    {
      if (this.state == STATE.MEDIA_RUNNING)
      {
        this.recorder.stop();
        setState(STATE.MEDIA_STOPPED);
      }
      this.recorder.reset();
      moveFile(this.audioFile);
      return;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }

  public static enum MODE
  {
    static
    {
      MODE[] arrayOfMODE = new MODE[3];
      arrayOfMODE[0] = NONE;
      arrayOfMODE[1] = PLAY;
      arrayOfMODE[2] = RECORD;
      $VALUES = arrayOfMODE;
    }
  }

  public static enum STATE
  {
    static
    {
      MEDIA_RUNNING = new STATE("MEDIA_RUNNING", 2);
      MEDIA_PAUSED = new STATE("MEDIA_PAUSED", 3);
      MEDIA_STOPPED = new STATE("MEDIA_STOPPED", 4);
      MEDIA_LOADING = new STATE("MEDIA_LOADING", 5);
      STATE[] arrayOfSTATE = new STATE[6];
      arrayOfSTATE[0] = MEDIA_NONE;
      arrayOfSTATE[1] = MEDIA_STARTING;
      arrayOfSTATE[2] = MEDIA_RUNNING;
      arrayOfSTATE[3] = MEDIA_PAUSED;
      arrayOfSTATE[4] = MEDIA_STOPPED;
      arrayOfSTATE[5] = MEDIA_LOADING;
      $VALUES = arrayOfSTATE;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.AudioPlayer
 * JD-Core Version:    0.6.0
 */