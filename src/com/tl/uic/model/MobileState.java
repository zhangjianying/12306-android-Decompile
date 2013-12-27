package com.tl.uic.model;

import com.tl.uic.util.LogInternal;
import java.io.Serializable;
import org.json.JSONObject;

public class MobileState
  implements JsonBase, Serializable
{
  private static final long serialVersionUID = -2088868938362419070L;
  private AndroidState androidState;
  private double battery;
  private String carrier;
  private String connectionType;
  private long freeMemory;
  private long freeStorage;
  private String ip;
  private ReachabilityType networkReachability;
  private int orientation;

  public final Boolean clean()
  {
    this.freeStorage = 0L;
    this.freeMemory = 0L;
    this.battery = 0.0D;
    this.ip = null;
    this.carrier = null;
    this.orientation = 0;
    this.connectionType = null;
    this.networkReachability = null;
    if (this.androidState != null)
    {
      this.androidState.clean();
      this.androidState = null;
    }
    return Boolean.valueOf(true);
  }

  public final boolean equals(Object paramObject)
  {
    if (this == paramObject);
    MobileState localMobileState;
    do
    {
      return true;
      if (paramObject == null)
        return false;
      if (getClass() != paramObject.getClass())
        return false;
      localMobileState = (MobileState)paramObject;
      if (this.androidState == null)
      {
        if (localMobileState.androidState != null)
          return false;
      }
      else if (!this.androidState.equals(localMobileState.androidState))
        return false;
      if (Double.doubleToLongBits(this.battery) != Double.doubleToLongBits(localMobileState.battery))
        return false;
      if (this.carrier == null)
      {
        if (localMobileState.carrier != null)
          return false;
      }
      else if (!this.carrier.equals(localMobileState.carrier))
        return false;
      if (this.connectionType == null)
      {
        if (localMobileState.connectionType != null)
          return false;
      }
      else if (!this.connectionType.equals(localMobileState.connectionType))
        return false;
      if (this.freeMemory != localMobileState.freeMemory)
        return false;
      if (this.freeStorage != localMobileState.freeStorage)
        return false;
      if (this.ip == null)
      {
        if (localMobileState.ip != null)
          return false;
      }
      else if (!this.ip.equals(localMobileState.ip))
        return false;
      if (this.networkReachability != localMobileState.networkReachability)
        return false;
    }
    while (this.orientation == localMobileState.orientation);
    return false;
  }

  public final AndroidState getAndroidState()
  {
    return this.androidState;
  }

  public final double getBattery()
  {
    return this.battery;
  }

  public final String getCarrier()
  {
    return this.carrier;
  }

  public final String getConnectionType()
  {
    return this.connectionType;
  }

  public final long getFreeMemory()
  {
    return this.freeMemory;
  }

  public final long getFreeStorage()
  {
    return this.freeStorage;
  }

  public final String getIp()
  {
    return this.ip;
  }

  public final JSONObject getJSON()
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("freeStorage", getFreeStorage());
      localJSONObject.put("freeMemory", getFreeMemory());
      Object localObject1;
      if (getBattery() == -1.0D)
      {
        localObject1 = "N/A";
        localJSONObject.put("battery", localObject1);
        localJSONObject.put("ip", getIp());
        localJSONObject.put("carrier", getCarrier());
        localJSONObject.put("orientation", getOrientation());
        localJSONObject.put("connectionType", getConnectionType());
        if (getNetworkReachability() != null)
          break label146;
      }
      label146: String str;
      for (Object localObject2 = "N/A"; ; localObject2 = str)
      {
        localJSONObject.put("networkReachability", localObject2);
        localJSONObject.put("androidState", getAndroidState().getJSON());
        return localJSONObject;
        localObject1 = Double.valueOf(getBattery());
        break;
        str = getNetworkReachability().name();
      }
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException);
    }
    return (JSONObject)(JSONObject)localJSONObject;
  }

  public final ReachabilityType getNetworkReachability()
  {
    return this.networkReachability;
  }

  public final int getOrientation()
  {
    return this.orientation;
  }

  public final int hashCode()
  {
    int i;
    int m;
    label49: int i1;
    label69: int i3;
    label123: int i4;
    int i5;
    if (this.androidState == null)
    {
      i = 0;
      int j = i + 31;
      long l = Double.doubleToLongBits(this.battery);
      int k = 31 * (j * 31 + (int)(l ^ l >>> 32));
      if (this.carrier != null)
        break label172;
      m = 0;
      int n = 31 * (k + m);
      if (this.connectionType != null)
        break label184;
      i1 = 0;
      int i2 = 31 * (31 * (31 * (n + i1) + (int)(this.freeMemory ^ this.freeMemory >>> 32)) + (int)(this.freeStorage ^ this.freeStorage >>> 32));
      if (this.ip != null)
        break label196;
      i3 = 0;
      i4 = 31 * (i2 + i3);
      ReachabilityType localReachabilityType = this.networkReachability;
      i5 = 0;
      if (localReachabilityType != null)
        break label208;
    }
    while (true)
    {
      return 31 * (i4 + i5) + this.orientation;
      i = this.androidState.hashCode();
      break;
      label172: m = this.carrier.hashCode();
      break label49;
      label184: i1 = this.connectionType.hashCode();
      break label69;
      label196: i3 = this.ip.hashCode();
      break label123;
      label208: i5 = this.networkReachability.hashCode();
    }
  }

  public final void setAndroidState(AndroidState paramAndroidState)
  {
    this.androidState = paramAndroidState;
  }

  public final void setBattery(double paramDouble)
  {
    this.battery = paramDouble;
  }

  public final void setCarrier(String paramString)
  {
    this.carrier = paramString;
  }

  public final void setConnectionType(String paramString)
  {
    this.connectionType = paramString;
  }

  public final void setFreeMemory(long paramLong)
  {
    this.freeMemory = paramLong;
  }

  public final void setFreeStorage(long paramLong)
  {
    this.freeStorage = paramLong;
  }

  public final void setIp(String paramString)
  {
    this.ip = paramString;
  }

  public final void setNetworkReachability(ReachabilityType paramReachabilityType)
  {
    this.networkReachability = paramReachabilityType;
  }

  public final void setOrientation(int paramInt)
  {
    this.orientation = paramInt;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.model.MobileState
 * JD-Core Version:    0.6.0
 */