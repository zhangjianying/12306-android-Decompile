package com.worklight.androidgap.plugin;

import android.app.Activity;
import android.content.Context;
import com.worklight.common.WLUtils;
import com.worklight.utils.SecurityUtils;
import com.worklight.utils.SecurityUtils.PBKDF2;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SecurityPlugin extends CordovaPlugin
{
  private static byte[] decrypt(JSONArray paramJSONArray)
    throws IllegalBlockSizeException, BadPaddingException, JSONException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException
  {
    String str1 = paramJSONArray.getString(0);
    String str2 = paramJSONArray.getString(1);
    String str3 = paramJSONArray.getString(2);
    return initCipher(2, WLUtils.hexStringToByteArray(str1), WLUtils.hexStringToByteArray(str3)).doFinal(WLUtils.hexStringToByteArray(str2));
  }

  private static String encrypt(JSONArray paramJSONArray)
    throws IllegalBlockSizeException, BadPaddingException, JSONException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException
  {
    String str1 = paramJSONArray.getString(0);
    String str2 = paramJSONArray.getString(1);
    String str3 = paramJSONArray.getString(2);
    byte[] arrayOfByte = initCipher(1, WLUtils.hexStringToByteArray(str1), WLUtils.hexStringToByteArray(str3)).doFinal(str2.getBytes());
    JSONObject localJSONObject = new JSONObject();
    localJSONObject.put("ct", WLUtils.byteArrayToHexString(arrayOfByte));
    localJSONObject.put("iv", str3);
    return localJSONObject.toString();
  }

  private static byte[] generateKey(JSONArray paramJSONArray)
    throws NoSuchAlgorithmException, JSONException, InvalidKeyException, InvalidKeySpecException, UnsupportedEncodingException
  {
    String str1 = paramJSONArray.getString(0);
    String str2 = paramJSONArray.getString(1);
    int i = Integer.parseInt(paramJSONArray.getString(2));
    int j = Integer.parseInt(paramJSONArray.getString(3));
    return SecurityUtils.PBKDF2.genKey(str1.toCharArray(), str2.getBytes("UTF-8"), i, j).getEncoded();
  }

  private static Cipher initCipher(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException
  {
    IvParameterSpec localIvParameterSpec = new IvParameterSpec(paramArrayOfByte2);
    SecretKeySpec localSecretKeySpec = new SecretKeySpec(paramArrayOfByte1, "AES");
    Cipher localCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    localCipher.init(paramInt, localSecretKeySpec, localIvParameterSpec);
    return localCipher;
  }

  public boolean execute(String paramString, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
    throws JSONException
  {
    ACTION localACTION = ACTION.fromString(paramString);
    if (localACTION != null)
    {
      if (this.cordova != null);
      for (Activity localActivity = this.cordova.getActivity(); ; localActivity = null)
        return localACTION.execute(paramJSONArray, paramCallbackContext, localActivity);
    }
    paramCallbackContext.error("Invalid action: " + paramString);
    return true;
  }

  public static abstract enum ACTION
  {
    static
    {
      encrypt = new ACTION("encrypt", 1)
      {
        public boolean execute(JSONArray paramJSONArray, CallbackContext paramCallbackContext, Context paramContext)
        {
          try
          {
            paramCallbackContext.success(SecurityPlugin.access$200(paramJSONArray));
            return true;
          }
          catch (Exception localException)
          {
          }
          return executeError(encrypt, paramCallbackContext, localException.getLocalizedMessage());
        }
      };
      decrypt = new ACTION("decrypt", 2)
      {
        public boolean execute(JSONArray paramJSONArray, CallbackContext paramCallbackContext, Context paramContext)
        {
          try
          {
            paramCallbackContext.success(new String(SecurityPlugin.access$300(paramJSONArray)));
            return true;
          }
          catch (Exception localException)
          {
          }
          return executeError(decrypt, paramCallbackContext, localException.getLocalizedMessage());
        }
      };
      hashData = new ACTION("hashData", 3)
      {
        public boolean execute(JSONArray paramJSONArray, CallbackContext paramCallbackContext, Context paramContext)
        {
          try
          {
            paramCallbackContext.success(SecurityUtils.hashDataFromJSON(paramContext, paramJSONArray));
            return true;
          }
          catch (Exception localException)
          {
          }
          return executeError(hashData, paramCallbackContext, localException.getLocalizedMessage());
        }
      };
      ACTION[] arrayOfACTION = new ACTION[4];
      arrayOfACTION[0] = keygen;
      arrayOfACTION[1] = encrypt;
      arrayOfACTION[2] = decrypt;
      arrayOfACTION[3] = hashData;
      $VALUES = arrayOfACTION;
    }

    public static ACTION fromString(String paramString)
    {
      try
      {
        ACTION localACTION = valueOf(paramString);
        return localACTION;
      }
      catch (Exception localException)
      {
      }
      return null;
    }

    protected abstract boolean execute(JSONArray paramJSONArray, CallbackContext paramCallbackContext, Context paramContext);

    protected boolean executeError(ACTION paramACTION, CallbackContext paramCallbackContext, String paramString)
    {
      paramCallbackContext.error("Action: " + paramACTION + " failed. " + paramString);
      return true;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.SecurityPlugin
 * JD-Core Version:    0.6.0
 */