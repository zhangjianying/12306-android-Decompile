package com.worklight.wlclient.api;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;

public class WLProcedureInvocationData
{
  private String adapter;
  private boolean compressResponse = false;
  private Object[] parameters;
  private String procedure;

  public WLProcedureInvocationData(String paramString1, String paramString2)
  {
    this.adapter = paramString1;
    this.procedure = paramString2;
  }

  public WLProcedureInvocationData(String paramString1, String paramString2, boolean paramBoolean)
  {
    this.adapter = paramString1;
    this.procedure = paramString2;
    this.compressResponse = paramBoolean;
  }

  Map<String, String> getInvocationDataMap()
  {
    HashMap localHashMap = new HashMap();
    JSONArray localJSONArray = new JSONArray();
    if (this.parameters != null)
    {
      int i = 0;
      while (i < this.parameters.length)
        try
        {
          localJSONArray.put(i, this.parameters[i]);
          i++;
        }
        catch (JSONException localJSONException)
        {
          throw new RuntimeException(localJSONException);
        }
    }
    localHashMap.put("adapter", this.adapter);
    localHashMap.put("procedure", this.procedure);
    localHashMap.put("parameters", localJSONArray.toString());
    localHashMap.put("compressResponse", Boolean.toString(this.compressResponse));
    return localHashMap;
  }

  public void setCompressResponse(boolean paramBoolean)
  {
    this.compressResponse = paramBoolean;
  }

  public void setParameters(Object[] paramArrayOfObject)
  {
    this.parameters = paramArrayOfObject;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.wlclient.api.WLProcedureInvocationData
 * JD-Core Version:    0.6.0
 */