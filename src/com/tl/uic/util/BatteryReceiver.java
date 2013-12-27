package com.tl.uic.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BatteryReceiver extends BroadcastReceiver
{
  private int health = -1;
  private int plugged = -1;
  private boolean present = false;
  private int scale = -1;
  private int status = -1;
  private String technology;
  private int temperature = -1;
  private int value = -1;
  private int voltage = -1;

  public final int getHealth()
  {
    return this.health;
  }

  public final int getPlugged()
  {
    return this.plugged;
  }

  public final int getScale()
  {
    return this.scale;
  }

  public final int getStatus()
  {
    return this.status;
  }

  public final String getTechnology()
  {
    if (this.technology == null)
      return "NA";
    return this.technology;
  }

  public final int getTemperature()
  {
    return this.temperature;
  }

  public final int getValue()
  {
    return this.value;
  }

  public final int getVoltage()
  {
    return this.voltage;
  }

  public final boolean isPresent()
  {
    return this.present;
  }

  public final void onReceive(Context paramContext, Intent paramIntent)
  {
    if ("android.intent.action.BATTERY_CHANGED".equalsIgnoreCase(paramIntent.getAction()))
    {
      this.value = paramIntent.getIntExtra("level", 0);
      this.health = paramIntent.getIntExtra("health", 0);
      this.plugged = paramIntent.getIntExtra("plugged", 0);
      this.status = paramIntent.getIntExtra("status", 0);
      this.temperature = paramIntent.getIntExtra("temperature", 0);
      this.voltage = paramIntent.getIntExtra("voltage", 0);
      this.scale = paramIntent.getIntExtra("scale", 0);
      this.present = paramIntent.getBooleanExtra("present", false);
      this.technology = paramIntent.getStringExtra("technology");
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.util.BatteryReceiver
 * JD-Core Version:    0.6.0
 */