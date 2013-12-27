package com.tl.uic;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import com.tl.uic.model.AndroidArray;
import com.tl.uic.model.AndroidState;
import com.tl.uic.model.ClientEnvironment;
import com.tl.uic.model.ClientState;
import com.tl.uic.model.KeyboardStateType;
import com.tl.uic.model.KeyboardType;
import com.tl.uic.model.MobileEnvironment;
import com.tl.uic.model.MobileState;
import com.tl.uic.model.OrientationType;
import com.tl.uic.model.StorageType;
import com.tl.uic.util.BatteryReceiver;
import com.tl.uic.util.ClientStateTask;
import com.tl.uic.util.ConfigurationUtil;
import com.tl.uic.util.ConnectionReceiver;
import com.tl.uic.util.LogInternal;
import com.tl.uic.util.ScreenReceiver;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class EnvironmentalData
{
  private static final int IP_BIT_16 = 16;
  private static final int IP_BIT_24 = 24;
  private static final int IP_BIT_8 = 8;
  private static final int SET_BITS_TO_CHAR = 255;
  private static ClientStateTask _clientStateTask;
  private Application _application;
  private String applicationName;
  private String applicationPackageName;
  private String applicationVersion;
  private BatteryReceiver batteryReceiver;
  private TimerTask clientStateTimerTask;
  private ConnectionReceiver connectionReceiver;
  private ClientState prevClientState;
  private ScreenReceiver screenReceiver;

  public EnvironmentalData(Application paramApplication)
  {
    this._application = paramApplication;
    init();
  }

  public static final Boolean closeClientStateTask()
  {
    synchronized (_clientStateTask)
    {
      _clientStateTask = null;
      return Boolean.valueOf(true);
    }
  }

  private void init()
  {
    enableBatteryManager();
    enableConnectionListener();
    getApplicationData();
    enableClientStateTask();
    enableScreenReceiver();
  }

  private Boolean isNotApplicationContextNull()
  {
    if ((this._application != null) && (this._application.getApplicationContext() != null))
      return Boolean.valueOf(true);
    return Boolean.valueOf(false);
  }

  private Boolean isNotApplicationResourcesNull()
  {
    if ((this._application != null) && (this._application.getResources() != null))
      return Boolean.valueOf(true);
    return Boolean.valueOf(false);
  }

  public final ClientEnvironment createClientEnvironment()
  {
    ClientEnvironment localClientEnvironment = new ClientEnvironment();
    localClientEnvironment.setOsVersion(Build.VERSION.RELEASE);
    if (this.screenReceiver == null)
      hasClientStateChanged();
    localClientEnvironment.setHeight(this.screenReceiver.getHeight());
    localClientEnvironment.setWidth(this.screenReceiver.getWidth());
    MobileEnvironment localMobileEnvironment = new MobileEnvironment();
    localMobileEnvironment.setTotalStorage(getStorage(StorageType.PHONE_TOTAL));
    localMobileEnvironment.setTotalMemory(getAvailableMemory());
    localMobileEnvironment.setLocale(Locale.getDefault().getDisplayName());
    localMobileEnvironment.setLanguage(Locale.getDefault().getDisplayLanguage());
    localMobileEnvironment.setManufacturer(getManufacturer());
    localMobileEnvironment.setDeviceModel(Build.MODEL);
    if (this.applicationName == null);
    for (String str = ""; ; str = this.applicationName)
    {
      localMobileEnvironment.setAppName(str);
      localMobileEnvironment.setAppVersion(this.applicationVersion);
      localMobileEnvironment.setUserID(Build.USER);
      localMobileEnvironment.setOrientationType(getOrientationType());
      AndroidArray localAndroidArray = new AndroidArray();
      localAndroidArray.setBrand(Build.BRAND);
      localAndroidArray.setFingerPrint(Build.FINGERPRINT);
      localAndroidArray.setKeyboardType(getKeyboardType());
      localMobileEnvironment.setAndroidArray(localAndroidArray);
      localClientEnvironment.setMobileEnvironment(localMobileEnvironment);
      return localClientEnvironment;
    }
  }

  public final ClientState createClientState()
  {
    ClientState localClientState = new ClientState();
    MobileState localMobileState = new MobileState();
    localMobileState.setFreeStorage(getStorage(StorageType.PHONE_FREE));
    localMobileState.setFreeMemory(getAvailableMemory());
    if (this.batteryReceiver != null)
      localMobileState.setBattery(this.batteryReceiver.getValue());
    localMobileState.setIp(getIpAddress());
    localMobileState.setCarrier(getCarrier());
    if (this.screenReceiver != null)
      localMobileState.setOrientation(this.screenReceiver.getRotation());
    if (this.connectionReceiver != null)
    {
      localMobileState.setConnectionType(this.connectionReceiver.getConnectionType());
      localMobileState.setNetworkReachability(this.connectionReceiver.getNetworkReachability());
    }
    AndroidState localAndroidState = new AndroidState();
    localAndroidState.setKeyboardStateType(getKeyboardStateType());
    localMobileState.setAndroidState(localAndroidState);
    localClientState.setMobileState(localMobileState);
    return localClientState;
  }

  public final void disableBatteryManager()
  {
    if ((this.batteryReceiver != null) && (isNotApplicationContextNull().booleanValue()))
    {
      this._application.getApplicationContext().unregisterReceiver(this.batteryReceiver);
      this.batteryReceiver = null;
    }
  }

  public final void disableClientStateTask()
  {
    if (this.clientStateTimerTask != null)
    {
      this.clientStateTimerTask.cancel();
      this.clientStateTimerTask = null;
    }
  }

  public final void disableConnectionListener()
  {
    if ((this.connectionReceiver != null) && (isNotApplicationContextNull().booleanValue()))
    {
      this._application.getApplicationContext().unregisterReceiver(this.connectionReceiver);
      this.connectionReceiver = null;
    }
  }

  public final void disableScreenReceiver()
  {
    if ((this.screenReceiver != null) && (isNotApplicationContextNull().booleanValue()))
    {
      this._application.getApplicationContext().unregisterReceiver(this.screenReceiver);
      this.screenReceiver = null;
    }
  }

  public final void enableBatteryManager()
  {
    if ((this.batteryReceiver == null) && (isNotApplicationContextNull().booleanValue()))
    {
      this.batteryReceiver = new BatteryReceiver();
      IntentFilter localIntentFilter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
      this._application.getApplicationContext().registerReceiver(this.batteryReceiver, localIntentFilter);
    }
  }

  public final void enableClientStateTask()
  {
    if (this.clientStateTimerTask == null)
      setupClientStateTask();
  }

  public final void enableConnectionListener()
  {
    if ((this.connectionReceiver == null) && (isNotApplicationContextNull().booleanValue()))
    {
      this.connectionReceiver = new ConnectionReceiver();
      IntentFilter localIntentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
      this._application.getApplicationContext().registerReceiver(this.connectionReceiver, localIntentFilter);
    }
  }

  public final void enableScreenReceiver()
  {
    if ((this.screenReceiver == null) && (isNotApplicationContextNull().booleanValue()))
    {
      this.screenReceiver = new ScreenReceiver();
      IntentFilter localIntentFilter = new IntentFilter("android.intent.action.CONFIGURATION_CHANGED");
      this._application.getApplicationContext().registerReceiver(this.screenReceiver, localIntentFilter);
    }
  }

  public final void getApplicationData()
  {
    try
    {
      PackageInfo localPackageInfo = this._application.getPackageManager().getPackageInfo(this._application.getPackageName(), 0);
      this.applicationName = localPackageInfo.applicationInfo.name;
      this.applicationVersion = localPackageInfo.versionName;
      this.applicationPackageName = localPackageInfo.packageName;
      return;
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException);
    }
  }

  public final String getApplicationName()
  {
    return this.applicationName;
  }

  public final String getApplicationPackageName()
  {
    return this.applicationPackageName;
  }

  public final String getApplicationVersion()
  {
    return this.applicationVersion;
  }

  public final long getAvailableMemory()
  {
    try
    {
      ActivityManager.MemoryInfo localMemoryInfo = new ActivityManager.MemoryInfo();
      ActivityManager localActivityManager = (ActivityManager)this._application.getApplicationContext().getSystemService("activity");
      if (localActivityManager != null)
        localActivityManager.getMemoryInfo(localMemoryInfo);
      long l = localMemoryInfo.availMem;
      return l;
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException);
    }
    return 0L;
  }

  public final BatteryReceiver getBatteryReceiver()
  {
    return this.batteryReceiver;
  }

  public final String getCarrier()
  {
    try
    {
      String str = ((TelephonyManager)this._application.getApplicationContext().getSystemService("phone")).getNetworkOperatorName();
      return str;
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException);
    }
    return null;
  }

  public final TimerTask getClientStateTimerTask()
  {
    return this.clientStateTimerTask;
  }

  public final ConnectionReceiver getConnectionReceiver()
  {
    return this.connectionReceiver;
  }

  public final String getHttpXTealeafProperty()
  {
    String[] arrayOfString;
    StringBuilder localStringBuilder1;
    String str1;
    label24: String str2;
    label44: StringBuilder localStringBuilder2;
    if (this.applicationVersion == null)
    {
      arrayOfString = null;
      localStringBuilder1 = new StringBuilder();
      if (arrayOfString != null)
        break label207;
      str1 = "";
      localStringBuilder1.append(str1);
      if ((arrayOfString != null) && (arrayOfString.length >= 2))
        break label214;
      str2 = "";
      localStringBuilder1.append(str2);
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append("TLT_BROWSER=");
      if (this.applicationName != null)
        break label238;
    }
    label207: label214: label238: for (String str3 = ""; ; str3 = this.applicationName + " ")
    {
      localStringBuilder2.append(str3);
      localStringBuilder2.append("Native;TLT_BROWSER_VERSION=");
      localStringBuilder2.append(localStringBuilder1.toString());
      localStringBuilder2.append(";TLT_BRAND=");
      localStringBuilder2.append(Build.BRAND);
      localStringBuilder2.append(";TLT_MODEL=");
      localStringBuilder2.append(Build.MODEL);
      localStringBuilder2.append(";TLT_SCREEN_HEIGHT=");
      localStringBuilder2.append(this.screenReceiver.getHeight());
      localStringBuilder2.append(";TLT_SCREEN_WIDTH=");
      localStringBuilder2.append(this.screenReceiver.getWidth());
      return localStringBuilder2.toString();
      arrayOfString = this.applicationVersion.split("\\.");
      break;
      str1 = arrayOfString[0];
      break label24;
      str2 = "." + arrayOfString[1];
      break label44;
    }
  }

  public final String getIpAddress()
  {
    try
    {
      int i = ((WifiManager)this._application.getApplicationContext().getSystemService("wifi")).getConnectionInfo().getIpAddress();
      Locale localLocale = Locale.US;
      Object[] arrayOfObject = new Object[4];
      arrayOfObject[0] = Integer.valueOf(i & 0xFF);
      arrayOfObject[1] = Integer.valueOf(0xFF & i >> 8);
      arrayOfObject[2] = Integer.valueOf(0xFF & i >> 16);
      arrayOfObject[3] = Integer.valueOf(0xFF & i >> 24);
      String str = String.format(localLocale, "%d.%d.%d.%d", arrayOfObject);
      return str;
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException);
    }
    return "";
  }

  public final KeyboardStateType getKeyboardStateType()
  {
    KeyboardStateType localKeyboardStateType1 = KeyboardStateType.UNDEFINED;
    try
    {
      if (isNotApplicationResourcesNull().booleanValue())
        switch (this._application.getResources().getConfiguration().keyboardHidden)
        {
        case 1:
          return KeyboardStateType.HIDDEN_FALSE;
        case 2:
          KeyboardStateType localKeyboardStateType2 = KeyboardStateType.HIDDEN_TRUE;
          return localKeyboardStateType2;
        }
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException);
    }
    return localKeyboardStateType1;
  }

  public final KeyboardType getKeyboardType()
  {
    KeyboardType localKeyboardType1 = KeyboardType.UNDEFINED;
    try
    {
      if (isNotApplicationResourcesNull().booleanValue())
        switch (this._application.getResources().getConfiguration().keyboard)
        {
        case 1:
          return KeyboardType.NO_KEYS;
        case 2:
          return KeyboardType.QWERTY;
        case 3:
          KeyboardType localKeyboardType2 = KeyboardType.TWELVE_KEYS;
          return localKeyboardType2;
        }
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException);
    }
    return localKeyboardType1;
  }

  public final String getManufacturer()
  {
    String str = "";
    try
    {
      if ("1.5".equals(Build.VERSION.RELEASE))
        return str;
      Field localField = Build.class.getDeclaredField("MANUFACTURER");
      localField.setAccessible(true);
      str = (String)localField.get(Build.class);
      return str;
    }
    catch (Exception localException)
    {
      while (true)
        LogInternal.logException(localException);
    }
  }

  public final OrientationType getOrientationType()
  {
    OrientationType localOrientationType1 = OrientationType.UNDEFINED;
    try
    {
      if (isNotApplicationResourcesNull().booleanValue())
        switch (this._application.getResources().getConfiguration().orientation)
        {
        case 2:
          return OrientationType.LANDSCAPE;
        case 1:
          return OrientationType.PORTRAIT;
        case 3:
          OrientationType localOrientationType2 = OrientationType.SQUARE;
          return localOrientationType2;
        }
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException);
    }
    return localOrientationType1;
  }

  public final ScreenReceiver getScreenReceiver()
  {
    return this.screenReceiver;
  }

  public final long getStorage(StorageType paramStorageType)
  {
    try
    {
      if ((paramStorageType == StorageType.PHONE_FREE) || (paramStorageType == StorageType.PHONE_USED) || (paramStorageType == StorageType.PHONE_TOTAL));
      StatFs localStatFs;
      for (File localFile = Environment.getDataDirectory(); ; localFile = Environment.getExternalStorageDirectory())
      {
        localStatFs = new StatFs(localFile.getPath());
        switch ($SWITCH_TABLE$com$tl$uic$model$StorageType()[paramStorageType.ordinal()])
        {
        case 1:
        case 4:
        case 2:
        case 5:
        case 3:
        case 6:
        }
      }
      return localStatFs.getAvailableBlocks() * localStatFs.getBlockSize();
      return localStatFs.getAvailableBlocks() - localStatFs.getBlockSize();
      long l = localStatFs.getBlockCount();
      int i = localStatFs.getBlockSize();
      return l - i;
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException);
    }
    return 0L;
  }

  public final Boolean hasClientStateChanged()
  {
    Boolean localBoolean = Boolean.valueOf(false);
    if ((this.batteryReceiver == null) || (this.connectionReceiver == null) || (this.screenReceiver == null))
      onResume();
    ClientState localClientState = createClientState();
    if ((this.prevClientState == null) || (!this.prevClientState.equals(localClientState)))
    {
      this.prevClientState = localClientState;
      TLFCache.addMessage(this.prevClientState);
      localBoolean = Boolean.valueOf(true);
    }
    return localBoolean;
  }

  public final Boolean onPause()
  {
    disableBatteryManager();
    disableClientStateTask();
    disableConnectionListener();
    disableScreenReceiver();
    return Boolean.valueOf(true);
  }

  public final Boolean onResume()
  {
    enableBatteryManager();
    enableClientStateTask();
    enableConnectionListener();
    enableScreenReceiver();
    return Boolean.valueOf(true);
  }

  public final void setupClientStateTask()
  {
    Handler localHandler = new Handler();
    Timer localTimer = new Timer();
    this.clientStateTimerTask = new TimerTask(localHandler)
    {
      public void run()
      {
        this.val$handler.post(new Runnable()
        {
          public void run()
          {
            try
            {
              if (EnvironmentalData._clientStateTask == null)
              {
                EnvironmentalData._clientStateTask = new ClientStateTask();
                EnvironmentalData._clientStateTask.execute(new Void[0]);
              }
              return;
            }
            catch (Exception localException)
            {
              LogInternal.logException(localException);
            }
          }
        });
      }
    };
    localTimer.schedule(this.clientStateTimerTask, 0L, ConfigurationUtil.getLongMS("TimeIntervalBetweenSnapshots"));
  }

  public final Boolean terminate()
  {
    disableBatteryManager();
    disableConnectionListener();
    disableClientStateTask();
    disableScreenReceiver();
    this._application = null;
    this.applicationName = null;
    this.applicationVersion = null;
    this.applicationPackageName = null;
    return Boolean.valueOf(true);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.EnvironmentalData
 * JD-Core Version:    0.6.0
 */