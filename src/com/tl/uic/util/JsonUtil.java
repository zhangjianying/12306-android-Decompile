package com.tl.uic.util;

import com.tl.uic.model.ClientMessageHeader;
import com.tl.uic.model.MessageType;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;

public final class JsonUtil
{
  private static String _messageTypes;
  private static Hashtable<Integer, Integer> _messageTypesSet = new Hashtable();
  private static volatile JsonUtil _myInstance;
  private static String _pattern = "type(\"|'):[\\d]+";
  private static final Pattern pattern = Pattern.compile(_pattern);

  private static Boolean getAddMessageTypeHeader()
  {
    return ConfigurationUtil.getBoolean("AddMessageTypeHeader");
  }

  private static Boolean getFilterMessageTypes()
  {
    return ConfigurationUtil.getBoolean("FilterMessageTypes");
  }

  public static JSONObject getHashValues(Map<String, String> paramMap)
  {
    JSONObject localJSONObject1;
    if (paramMap != null)
      try
      {
        if (!paramMap.isEmpty())
        {
          JSONObject localJSONObject2 = new JSONObject();
          try
          {
            Iterator localIterator = paramMap.entrySet().iterator();
            while (true)
            {
              if (!localIterator.hasNext())
              {
                localJSONObject1 = localJSONObject2;
                break;
              }
              Map.Entry localEntry = (Map.Entry)localIterator.next();
              localJSONObject2.accumulate((String)localEntry.getKey(), ValueUtil.trimValue((String)localEntry.getValue()));
            }
          }
          catch (JSONException localJSONException1)
          {
            localJSONObject1 = localJSONObject2;
          }
          LogInternal.logException(localJSONException1);
        }
      }
      catch (JSONException localJSONException2)
      {
        while (true)
          localJSONObject1 = null;
      }
    else
      return null;
    return localJSONObject1;
  }

  public static JsonUtil getInstance()
  {
    if (_myInstance == null)
      monitorenter;
    try
    {
      _myInstance = new JsonUtil();
      return _myInstance;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public static Boolean testMessageType(Object paramObject, Boolean paramBoolean)
  {
    if (paramObject == null)
      return Boolean.valueOf(false);
    if (((getFilterMessageTypes().booleanValue()) && (!paramBoolean.booleanValue())) || ((getAddMessageTypeHeader().booleanValue()) && (paramBoolean.booleanValue())))
    {
      String[] arrayOfString1;
      if ((_messageTypes == null) || (!_messageTypes.equals(ConfigurationUtil.getString("MessageTypes"))))
      {
        _messageTypes = ConfigurationUtil.getString("MessageTypes");
        _messageTypesSet.clear();
        arrayOfString1 = _messageTypes.split(",");
      }
      for (int i = 0; ; i++)
      {
        if (i >= arrayOfString1.length)
        {
          if ((!(paramObject instanceof ClientMessageHeader)) || (_messageTypesSet.containsKey(Integer.valueOf(((ClientMessageHeader)paramObject).getMessageType().getValue()))))
            break;
          return Boolean.valueOf(false);
        }
        Integer localInteger = Integer.valueOf(Integer.parseInt(arrayOfString1[i]));
        _messageTypesSet.put(localInteger, localInteger);
      }
      if ((paramObject instanceof String))
      {
        Matcher localMatcher = pattern.matcher((CharSequence)paramObject);
        String[] arrayOfString2;
        do
        {
          if (!localMatcher.find())
            return Boolean.valueOf(false);
          arrayOfString2 = localMatcher.group().split(":");
        }
        while (!_messageTypesSet.containsKey(Integer.valueOf(Integer.parseInt(arrayOfString2[1]))));
        return Boolean.valueOf(true);
      }
    }
    return Boolean.valueOf(true);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.util.JsonUtil
 * JD-Core Version:    0.6.0
 */