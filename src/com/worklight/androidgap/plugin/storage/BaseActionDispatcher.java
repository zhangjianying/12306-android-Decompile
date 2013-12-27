package com.worklight.androidgap.plugin.storage;

import com.worklight.androidgap.jsonstore.util.JsonstoreUtil;
import com.worklight.androidgap.jsonstore.util.Logger;
import com.worklight.androidgap.jsonstore.util.jackson.JsonOrgModule;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class BaseActionDispatcher
  implements ActionDispatcher
{
  protected static final int RC_FALSE = 0;
  protected static final int RC_OK = 0;
  protected static final int RC_TRUE = 1;
  protected Logger logger = JsonstoreUtil.getCoreLogger();
  private String name;
  private LinkedList<Parameter> parameters;

  public BaseActionDispatcher(String paramString)
  {
    this.name = paramString;
    this.parameters = new LinkedList();
  }

  private void collectParameters(Context paramContext, JSONArray paramJSONArray)
    throws Throwable
  {
    int i = 0;
    Iterator localIterator = this.parameters.iterator();
    while (localIterator.hasNext())
    {
      Parameter localParameter = (Parameter)localIterator.next();
      ParameterType[] arrayOfParameterType = localParameter.getTypes();
      int j = arrayOfParameterType.length;
      for (int k = 0; ; k++)
      {
        int m = 0;
        if (k < j)
        {
          Object localObject = convertParameter(paramJSONArray, i, arrayOfParameterType[k]);
          if (localObject == null)
            continue;
          m = 1;
          paramContext.addParameter(localParameter.getName(), localObject);
        }
        if ((!localParameter.isRequired()) || (m != 0))
          break;
        throw new Throwable("invalid type for parameter \"" + localParameter.getName() + "\" in action dispatcher \"" + getName() + "\"");
      }
      i++;
    }
  }

  private Object convertParameter(JSONArray paramJSONArray, int paramInt, ParameterType paramParameterType)
    throws Throwable
  {
    try
    {
      switch (1.$SwitchMap$com$worklight$androidgap$plugin$storage$BaseActionDispatcher$ParameterType[paramParameterType.ordinal()])
      {
      case 1:
        return JsonOrgModule.deserializeJSONArray(paramJSONArray.getString(paramInt));
      case 2:
        return Boolean.valueOf(paramJSONArray.getBoolean(paramInt));
      case 3:
        return Double.valueOf(paramJSONArray.getDouble(paramInt));
      case 4:
        return Integer.valueOf(paramJSONArray.getInt(paramInt));
      case 5:
        return Long.valueOf(paramJSONArray.getLong(paramInt));
      case 6:
        return JsonOrgModule.deserializeJSONObject(paramJSONArray.getString(paramInt));
      case 7:
        String str = paramJSONArray.getString(paramInt);
        return str;
      }
    }
    catch (JSONException localJSONException)
    {
    }
    return null;
  }

  protected void addParameter(String paramString, boolean paramBoolean1, boolean paramBoolean2, ParameterType[] paramArrayOfParameterType)
  {
    this.parameters.add(new Parameter(paramString, paramBoolean1, paramBoolean2, paramArrayOfParameterType, null));
  }

  protected void addParameter(String paramString, boolean paramBoolean, ParameterType[] paramArrayOfParameterType)
  {
    this.parameters.add(new Parameter(paramString, paramBoolean, true, paramArrayOfParameterType, null));
  }

  public abstract PluginResult dispatch(Context paramContext)
    throws Throwable;

  public PluginResult dispatch(CordovaInterface paramCordovaInterface, JSONArray paramJSONArray)
    throws Throwable
  {
    Context localContext = new Context(paramCordovaInterface);
    collectParameters(localContext, paramJSONArray);
    if (this.logger.isLoggable(3))
    {
      this.logger.logDebug("invoking action dispatcher \"" + this.name + "\" with parameters:");
      Iterator localIterator = this.parameters.iterator();
      while (localIterator.hasNext())
      {
        Parameter localParameter = (Parameter)localIterator.next();
        String str = localParameter.getName();
        if (localParameter.isLoggable())
        {
          this.logger.logDebug("   " + str + "=" + localContext.getUntypedParameter(str));
          continue;
        }
        this.logger.logDebug("   " + str + "=[value not logged]");
      }
    }
    return dispatch(localContext);
  }

  public String getName()
  {
    return this.name;
  }

  public static class Context
  {
    private CordovaInterface cordovaContext;
    private HashMap<String, Object> parameters;

    protected Context(CordovaInterface paramCordovaInterface)
    {
      this.cordovaContext = paramCordovaInterface;
      this.parameters = new HashMap();
    }

    private void addParameter(String paramString, Object paramObject)
    {
      this.parameters.put(paramString, paramObject);
    }

    public JSONArray getArrayParameter(String paramString)
    {
      return (JSONArray)this.parameters.get(paramString);
    }

    public Boolean getBooleanParameter(String paramString)
    {
      return (Boolean)this.parameters.get(paramString);
    }

    public CordovaInterface getCordovaContext()
    {
      return this.cordovaContext;
    }

    public float getFloatParameter(String paramString)
    {
      return ((Float)this.parameters.get(paramString)).floatValue();
    }

    public int getIntParameter(String paramString)
    {
      return ((Integer)this.parameters.get(paramString)).intValue();
    }

    public JSONObject getObjectParameter(String paramString)
    {
      return (JSONObject)this.parameters.get(paramString);
    }

    public String getStringParameter(String paramString)
    {
      return (String)this.parameters.get(paramString);
    }

    public Object getUntypedParameter(String paramString)
    {
      return this.parameters.get(paramString);
    }
  }

  private class Parameter
  {
    private boolean loggable;
    private String name;
    private boolean required;
    private BaseActionDispatcher.ParameterType[] types;

    private Parameter(String paramBoolean1, boolean paramBoolean2, boolean paramArrayOfParameterType, BaseActionDispatcher.ParameterType[] arg5)
    {
      this.name = paramBoolean1;
      this.required = paramBoolean2;
      this.loggable = paramArrayOfParameterType;
      Object localObject;
      this.types = localObject;
    }

    public String getName()
    {
      return this.name;
    }

    public BaseActionDispatcher.ParameterType[] getTypes()
    {
      return this.types;
    }

    public boolean isLoggable()
    {
      return this.loggable;
    }

    public boolean isRequired()
    {
      return this.required;
    }
  }

  public static enum ParameterType
  {
    static
    {
      ParameterType[] arrayOfParameterType = new ParameterType[7];
      arrayOfParameterType[0] = ARRAY;
      arrayOfParameterType[1] = BOOLEAN;
      arrayOfParameterType[2] = DOUBLE;
      arrayOfParameterType[3] = INTEGER;
      arrayOfParameterType[4] = LONG;
      arrayOfParameterType[5] = OBJECT;
      arrayOfParameterType[6] = STRING;
      $VALUES = arrayOfParameterType;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.storage.BaseActionDispatcher
 * JD-Core Version:    0.6.0
 */