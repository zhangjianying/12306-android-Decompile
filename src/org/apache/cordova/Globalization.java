package org.apache.cordova;

import android.annotation.TargetApi;
import android.os.Build.VERSION;
import android.text.format.Time;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Globalization extends CordovaPlugin
{
  public static final String CURRENCY = "currency";
  public static final String CURRENCYCODE = "currencyCode";
  public static final String DATE = "date";
  public static final String DATESTRING = "dateString";
  public static final String DATETOSTRING = "dateToString";
  public static final String DAYS = "days";
  public static final String FORMATLENGTH = "formatLength";
  public static final String FULL = "full";
  public static final String GETCURRENCYPATTERN = "getCurrencyPattern";
  public static final String GETDATENAMES = "getDateNames";
  public static final String GETDATEPATTERN = "getDatePattern";
  public static final String GETFIRSTDAYOFWEEK = "getFirstDayOfWeek";
  public static final String GETLOCALENAME = "getLocaleName";
  public static final String GETNUMBERPATTERN = "getNumberPattern";
  public static final String GETPREFERREDLANGUAGE = "getPreferredLanguage";
  public static final String ISDAYLIGHTSAVINGSTIME = "isDayLightSavingsTime";
  public static final String ITEM = "item";
  public static final String LONG = "long";
  public static final String MEDIUM = "medium";
  public static final String MONTHS = "months";
  public static final String NARROW = "narrow";
  public static final String NUMBER = "number";
  public static final String NUMBERSTRING = "numberString";
  public static final String NUMBERTOSTRING = "numberToString";
  public static final String OPTIONS = "options";
  public static final String PERCENT = "percent";
  public static final String SELECTOR = "selector";
  public static final String STRINGTODATE = "stringToDate";
  public static final String STRINGTONUMBER = "stringToNumber";
  public static final String TIME = "time";
  public static final String TYPE = "type";
  public static final String WIDE = "wide";

  private JSONObject getCurrencyPattern(JSONArray paramJSONArray)
    throws GlobalizationError
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      String str = paramJSONArray.getJSONObject(0).getString("currencyCode");
      DecimalFormat localDecimalFormat = (DecimalFormat)DecimalFormat.getCurrencyInstance(Locale.getDefault());
      Currency localCurrency = Currency.getInstance(str);
      localDecimalFormat.setCurrency(localCurrency);
      localJSONObject.put("pattern", localDecimalFormat.toPattern());
      localJSONObject.put("code", localCurrency.getCurrencyCode());
      localJSONObject.put("fraction", localDecimalFormat.getMinimumFractionDigits());
      localJSONObject.put("rounding", new Integer(0));
      localJSONObject.put("decimal", String.valueOf(localDecimalFormat.getDecimalFormatSymbols().getDecimalSeparator()));
      localJSONObject.put("grouping", String.valueOf(localDecimalFormat.getDecimalFormatSymbols().getGroupingSeparator()));
      return localJSONObject;
    }
    catch (Exception localException)
    {
    }
    throw new GlobalizationError("FORMATTING_ERROR");
  }

  @TargetApi(9)
  private JSONObject getDateNames(JSONArray paramJSONArray)
    throws GlobalizationError
  {
    JSONObject localJSONObject1 = new JSONObject();
    JSONArray localJSONArray = new JSONArray();
    ArrayList localArrayList = new ArrayList();
    Map localMap;
    while (true)
    {
      int m;
      try
      {
        int i = paramJSONArray.getJSONObject(0).length();
        int j = 0;
        int k = 0;
        if (i <= 0)
          continue;
        boolean bool1 = ((JSONObject)paramJSONArray.getJSONObject(0).get("options")).isNull("type");
        k = 0;
        if (bool1)
          continue;
        boolean bool2 = ((String)((JSONObject)paramJSONArray.getJSONObject(0).get("options")).get("type")).equalsIgnoreCase("narrow");
        k = 0;
        if (!bool2)
          continue;
        k = 0 + 1;
        boolean bool3 = ((JSONObject)paramJSONArray.getJSONObject(0).get("options")).isNull("item");
        j = 0;
        if (bool3)
          continue;
        boolean bool4 = ((String)((JSONObject)paramJSONArray.getJSONObject(0).get("options")).get("item")).equalsIgnoreCase("days");
        j = 0;
        if (!bool4)
          continue;
        j = 0 + 10;
        m = j + k;
        if (m == 1)
        {
          localMap = Calendar.getInstance().getDisplayNames(2, 1, Locale.getDefault());
          Iterator localIterator = localMap.keySet().iterator();
          if (!localIterator.hasNext())
            break;
          localArrayList.add((String)localIterator.next());
          continue;
        }
      }
      catch (Exception localException)
      {
        throw new GlobalizationError("UNKNOWN_ERROR");
      }
      if (m == 10)
      {
        localMap = Calendar.getInstance().getDisplayNames(7, 2, Locale.getDefault());
        continue;
      }
      if (m == 11)
      {
        localMap = Calendar.getInstance().getDisplayNames(7, 1, Locale.getDefault());
        continue;
      }
      localMap = Calendar.getInstance().getDisplayNames(2, 2, Locale.getDefault());
    }
    Collections.sort(localArrayList, new Comparator(localMap)
    {
      public int compare(String paramString1, String paramString2)
      {
        return ((Integer)this.val$namesMap.get(paramString1)).compareTo((Integer)this.val$namesMap.get(paramString2));
      }
    });
    for (int n = 0; n < localArrayList.size(); n++)
      localJSONArray.put(localArrayList.get(n));
    JSONObject localJSONObject2 = localJSONObject1.put("value", localJSONArray);
    return localJSONObject2;
  }

  private JSONObject getDatePattern(JSONArray paramJSONArray)
    throws GlobalizationError
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      SimpleDateFormat localSimpleDateFormat1 = (SimpleDateFormat)android.text.format.DateFormat.getDateFormat(this.cordova.getActivity());
      SimpleDateFormat localSimpleDateFormat2 = (SimpleDateFormat)android.text.format.DateFormat.getTimeFormat(this.cordova.getActivity());
      Object localObject = localSimpleDateFormat1.toLocalizedPattern() + " " + localSimpleDateFormat2.toLocalizedPattern();
      String str3;
      String str1;
      if (paramJSONArray.getJSONObject(0).length() > 1)
      {
        if (!((JSONObject)paramJSONArray.getJSONObject(0).get("options")).isNull("formatLength"))
        {
          str3 = (String)((JSONObject)paramJSONArray.getJSONObject(0).get("options")).get("formatLength");
          if (!str3.equalsIgnoreCase("medium"))
            break label332;
          localSimpleDateFormat1 = (SimpleDateFormat)android.text.format.DateFormat.getMediumDateFormat(this.cordova.getActivity());
        }
        localObject = localSimpleDateFormat1.toLocalizedPattern() + " " + localSimpleDateFormat2.toLocalizedPattern();
        if (!((JSONObject)paramJSONArray.getJSONObject(0).get("options")).isNull("selector"))
        {
          str1 = (String)((JSONObject)paramJSONArray.getJSONObject(0).get("options")).get("selector");
          if (!str1.equalsIgnoreCase("date"))
            break label372;
          localObject = localSimpleDateFormat1.toLocalizedPattern();
        }
      }
      while (true)
      {
        TimeZone localTimeZone = TimeZone.getTimeZone(Time.getCurrentTimezone());
        localJSONObject.put("pattern", localObject);
        localJSONObject.put("timezone", localTimeZone.getDisplayName(localTimeZone.inDaylightTime(Calendar.getInstance().getTime()), 0));
        localJSONObject.put("utc_offset", localTimeZone.getRawOffset() / 1000);
        localJSONObject.put("dst_offset", localTimeZone.getDSTSavings() / 1000);
        return localJSONObject;
        label332: if ((!str3.equalsIgnoreCase("long")) && (!str3.equalsIgnoreCase("full")))
          break;
        localSimpleDateFormat1 = (SimpleDateFormat)android.text.format.DateFormat.getLongDateFormat(this.cordova.getActivity());
        break;
        label372: if (!str1.equalsIgnoreCase("time"))
          continue;
        String str2 = localSimpleDateFormat2.toLocalizedPattern();
        localObject = str2;
      }
    }
    catch (Exception localException)
    {
    }
    throw new GlobalizationError("PATTERN_ERROR");
  }

  private JSONObject getDateToString(JSONArray paramJSONArray)
    throws GlobalizationError
  {
    JSONObject localJSONObject1 = new JSONObject();
    try
    {
      Date localDate = new Date(((Long)paramJSONArray.getJSONObject(0).get("date")).longValue());
      JSONObject localJSONObject2 = localJSONObject1.put("value", new SimpleDateFormat(getDatePattern(paramJSONArray).getString("pattern")).format(localDate));
      return localJSONObject2;
    }
    catch (Exception localException)
    {
    }
    throw new GlobalizationError("FORMATTING_ERROR");
  }

  private JSONObject getFirstDayOfWeek(JSONArray paramJSONArray)
    throws GlobalizationError
  {
    JSONObject localJSONObject1 = new JSONObject();
    try
    {
      JSONObject localJSONObject2 = localJSONObject1.put("value", Calendar.getInstance(Locale.getDefault()).getFirstDayOfWeek());
      return localJSONObject2;
    }
    catch (Exception localException)
    {
    }
    throw new GlobalizationError("UNKNOWN_ERROR");
  }

  private JSONObject getIsDayLightSavingsTime(JSONArray paramJSONArray)
    throws GlobalizationError
  {
    JSONObject localJSONObject1 = new JSONObject();
    try
    {
      Date localDate = new Date(((Long)paramJSONArray.getJSONObject(0).get("date")).longValue());
      JSONObject localJSONObject2 = localJSONObject1.put("dst", TimeZone.getTimeZone(Time.getCurrentTimezone()).inDaylightTime(localDate));
      return localJSONObject2;
    }
    catch (Exception localException)
    {
    }
    throw new GlobalizationError("UNKNOWN_ERROR");
  }

  private JSONObject getLocaleName()
    throws GlobalizationError
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("value", Locale.getDefault().toString());
      return localJSONObject;
    }
    catch (Exception localException)
    {
    }
    throw new GlobalizationError("UNKNOWN_ERROR");
  }

  private DecimalFormat getNumberFormatInstance(JSONArray paramJSONArray)
    throws JSONException
  {
    DecimalFormat localDecimalFormat1 = (DecimalFormat)DecimalFormat.getInstance(Locale.getDefault());
    try
    {
      if ((paramJSONArray.getJSONObject(0).length() > 1) && (!((JSONObject)paramJSONArray.getJSONObject(0).get("options")).isNull("type")))
      {
        String str = (String)((JSONObject)paramJSONArray.getJSONObject(0).get("options")).get("type");
        if (str.equalsIgnoreCase("currency"))
          return (DecimalFormat)DecimalFormat.getCurrencyInstance(Locale.getDefault());
        if (str.equalsIgnoreCase("percent"))
        {
          DecimalFormat localDecimalFormat2 = (DecimalFormat)DecimalFormat.getPercentInstance(Locale.getDefault());
          return localDecimalFormat2;
        }
      }
    }
    catch (JSONException localJSONException)
    {
    }
    return localDecimalFormat1;
  }

  private JSONObject getNumberPattern(JSONArray paramJSONArray)
    throws GlobalizationError
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      DecimalFormat localDecimalFormat = (DecimalFormat)DecimalFormat.getInstance(Locale.getDefault());
      Object localObject = String.valueOf(localDecimalFormat.getDecimalFormatSymbols().getDecimalSeparator());
      String str1;
      if ((paramJSONArray.getJSONObject(0).length() > 0) && (!((JSONObject)paramJSONArray.getJSONObject(0).get("options")).isNull("type")))
      {
        str1 = (String)((JSONObject)paramJSONArray.getJSONObject(0).get("options")).get("type");
        if (!str1.equalsIgnoreCase("currency"))
          break label231;
        localDecimalFormat = (DecimalFormat)DecimalFormat.getCurrencyInstance(Locale.getDefault());
        localObject = localDecimalFormat.getDecimalFormatSymbols().getCurrencySymbol();
      }
      while (true)
      {
        localJSONObject.put("pattern", localDecimalFormat.toPattern());
        localJSONObject.put("symbol", localObject);
        localJSONObject.put("fraction", localDecimalFormat.getMinimumFractionDigits());
        localJSONObject.put("rounding", new Integer(0));
        localJSONObject.put("positive", localDecimalFormat.getPositivePrefix());
        localJSONObject.put("negative", localDecimalFormat.getNegativePrefix());
        localJSONObject.put("decimal", String.valueOf(localDecimalFormat.getDecimalFormatSymbols().getDecimalSeparator()));
        localJSONObject.put("grouping", String.valueOf(localDecimalFormat.getDecimalFormatSymbols().getGroupingSeparator()));
        return localJSONObject;
        label231: if (!str1.equalsIgnoreCase("percent"))
          continue;
        localDecimalFormat = (DecimalFormat)DecimalFormat.getPercentInstance(Locale.getDefault());
        String str2 = String.valueOf(localDecimalFormat.getDecimalFormatSymbols().getPercent());
        localObject = str2;
      }
    }
    catch (Exception localException)
    {
    }
    throw new GlobalizationError("PATTERN_ERROR");
  }

  private JSONObject getNumberToString(JSONArray paramJSONArray)
    throws GlobalizationError
  {
    JSONObject localJSONObject1 = new JSONObject();
    try
    {
      JSONObject localJSONObject2 = localJSONObject1.put("value", getNumberFormatInstance(paramJSONArray).format(paramJSONArray.getJSONObject(0).get("number")));
      return localJSONObject2;
    }
    catch (Exception localException)
    {
    }
    throw new GlobalizationError("FORMATTING_ERROR");
  }

  private JSONObject getPreferredLanguage()
    throws GlobalizationError
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("value", Locale.getDefault().getDisplayLanguage().toString());
      return localJSONObject;
    }
    catch (Exception localException)
    {
    }
    throw new GlobalizationError("UNKNOWN_ERROR");
  }

  private JSONObject getStringToNumber(JSONArray paramJSONArray)
    throws GlobalizationError
  {
    JSONObject localJSONObject1 = new JSONObject();
    try
    {
      JSONObject localJSONObject2 = localJSONObject1.put("value", getNumberFormatInstance(paramJSONArray).parse((String)paramJSONArray.getJSONObject(0).get("numberString")));
      return localJSONObject2;
    }
    catch (Exception localException)
    {
    }
    throw new GlobalizationError("PARSING_ERROR");
  }

  private JSONObject getStringtoDate(JSONArray paramJSONArray)
    throws GlobalizationError
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      Date localDate = new SimpleDateFormat(getDatePattern(paramJSONArray).getString("pattern")).parse(paramJSONArray.getJSONObject(0).get("dateString").toString());
      Time localTime = new Time();
      localTime.set(localDate.getTime());
      localJSONObject.put("year", localTime.year);
      localJSONObject.put("month", localTime.month);
      localJSONObject.put("day", localTime.monthDay);
      localJSONObject.put("hour", localTime.hour);
      localJSONObject.put("minute", localTime.minute);
      localJSONObject.put("second", localTime.second);
      localJSONObject.put("millisecond", new Long(0L));
      return localJSONObject;
    }
    catch (Exception localException)
    {
    }
    throw new GlobalizationError("PARSING_ERROR");
  }

  public boolean execute(String paramString, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
  {
    new JSONObject();
    try
    {
      if (paramString.equals("getLocaleName"))
        localObject = getLocaleName();
      while (true)
      {
        paramCallbackContext.success((JSONObject)localObject);
        break label305;
        if (paramString.equals("getPreferredLanguage"))
        {
          localObject = getPreferredLanguage();
          continue;
        }
        if (paramString.equalsIgnoreCase("dateToString"))
        {
          localObject = getDateToString(paramJSONArray);
          continue;
        }
        if (paramString.equalsIgnoreCase("stringToDate"))
        {
          localObject = getStringtoDate(paramJSONArray);
          continue;
        }
        if (paramString.equalsIgnoreCase("getDatePattern"))
        {
          localObject = getDatePattern(paramJSONArray);
          continue;
        }
        if (paramString.equalsIgnoreCase("getDateNames"))
        {
          if (Build.VERSION.SDK_INT >= 9)
            break;
          throw new GlobalizationError("UNKNOWN_ERROR");
        }
      }
    }
    catch (GlobalizationError localGlobalizationError)
    {
      while (true)
      {
        paramCallbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, localGlobalizationError.toJson()));
        break;
        Object localObject = getDateNames(paramJSONArray);
        continue;
        if (paramString.equalsIgnoreCase("isDayLightSavingsTime"))
        {
          localObject = getIsDayLightSavingsTime(paramJSONArray);
          continue;
        }
        if (paramString.equalsIgnoreCase("getFirstDayOfWeek"))
        {
          localObject = getFirstDayOfWeek(paramJSONArray);
          continue;
        }
        if (paramString.equalsIgnoreCase("numberToString"))
        {
          localObject = getNumberToString(paramJSONArray);
          continue;
        }
        if (paramString.equalsIgnoreCase("stringToNumber"))
        {
          localObject = getStringToNumber(paramJSONArray);
          continue;
        }
        if (paramString.equalsIgnoreCase("getNumberPattern"))
        {
          localObject = getNumberPattern(paramJSONArray);
          continue;
        }
        if (paramString.equalsIgnoreCase("getCurrencyPattern"))
        {
          JSONObject localJSONObject = getCurrencyPattern(paramJSONArray);
          localObject = localJSONObject;
          continue;
        }
        return false;
      }
    }
    catch (Exception localException)
    {
      paramCallbackContext.sendPluginResult(new PluginResult(PluginResult.Status.JSON_EXCEPTION));
    }
    label305: return true;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.Globalization
 * JD-Core Version:    0.6.0
 */