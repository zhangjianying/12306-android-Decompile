package com.worklight.utils;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Process;
import com.worklight.common.WLUtils;
import com.worklight.wlclient.api.WLClient;

public final class WLAlertDialog
  implements Runnable
{
  public static final DialogInterface.OnClickListener exitListener = new DialogInterface.OnClickListener()
  {
    public void onClick(DialogInterface paramDialogInterface, int paramInt)
    {
      Process.killProcess(Process.myPid());
    }
  };
  private DialogInterface.OnClickListener clickListener;
  private String message;
  private String positiveButtonText;
  private String title;

  public WLAlertDialog()
  {
  }

  public WLAlertDialog(String paramString1, String paramString2)
  {
    init(paramString1, paramString2, "WLClient.close");
  }

  public WLAlertDialog(String paramString1, String paramString2, String paramString3)
  {
    init(paramString1, paramString2, paramString3);
  }

  // ERROR //
  private void init(String paramString1, String paramString2, String paramString3)
  {
    // Byte code:
    //   0: ldc 34
    //   2: invokestatic 40	java/util/Locale:getDefault	()Ljava/util/Locale;
    //   5: invokestatic 46	java/util/ResourceBundle:getBundle	(Ljava/lang/String;Ljava/util/Locale;)Ljava/util/ResourceBundle;
    //   8: astore 5
    //   10: aload_1
    //   11: ifnull +25 -> 36
    //   14: aload_0
    //   15: aload 5
    //   17: aload_1
    //   18: invokevirtual 50	java/util/ResourceBundle:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   21: putfield 52	com/worklight/utils/WLAlertDialog:title	Ljava/lang/String;
    //   24: aload_0
    //   25: getfield 52	com/worklight/utils/WLAlertDialog:title	Ljava/lang/String;
    //   28: ifnonnull +8 -> 36
    //   31: aload_0
    //   32: aload_1
    //   33: putfield 52	com/worklight/utils/WLAlertDialog:title	Ljava/lang/String;
    //   36: aload_2
    //   37: ifnull +25 -> 62
    //   40: aload_0
    //   41: aload 5
    //   43: aload_2
    //   44: invokevirtual 50	java/util/ResourceBundle:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   47: putfield 54	com/worklight/utils/WLAlertDialog:message	Ljava/lang/String;
    //   50: aload_0
    //   51: getfield 54	com/worklight/utils/WLAlertDialog:message	Ljava/lang/String;
    //   54: ifnonnull +8 -> 62
    //   57: aload_0
    //   58: aload_2
    //   59: putfield 54	com/worklight/utils/WLAlertDialog:message	Ljava/lang/String;
    //   62: aload_0
    //   63: aload 5
    //   65: aload_3
    //   66: invokevirtual 50	java/util/ResourceBundle:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   69: putfield 56	com/worklight/utils/WLAlertDialog:positiveButtonText	Ljava/lang/String;
    //   72: aload_0
    //   73: getfield 56	com/worklight/utils/WLAlertDialog:positiveButtonText	Ljava/lang/String;
    //   76: ifnonnull +120 -> 196
    //   79: aload_0
    //   80: aload_3
    //   81: putfield 56	com/worklight/utils/WLAlertDialog:positiveButtonText	Ljava/lang/String;
    //   84: return
    //   85: astore 11
    //   87: aload_0
    //   88: getfield 52	com/worklight/utils/WLAlertDialog:title	Ljava/lang/String;
    //   91: ifnonnull -55 -> 36
    //   94: aload_0
    //   95: aload_1
    //   96: putfield 52	com/worklight/utils/WLAlertDialog:title	Ljava/lang/String;
    //   99: goto -63 -> 36
    //   102: astore 4
    //   104: aload 4
    //   106: invokevirtual 60	java/util/MissingResourceException:getMessage	()Ljava/lang/String;
    //   109: invokestatic 66	com/worklight/common/WLUtils:error	(Ljava/lang/String;)V
    //   112: return
    //   113: astore 10
    //   115: aload_0
    //   116: getfield 52	com/worklight/utils/WLAlertDialog:title	Ljava/lang/String;
    //   119: ifnonnull +8 -> 127
    //   122: aload_0
    //   123: aload_1
    //   124: putfield 52	com/worklight/utils/WLAlertDialog:title	Ljava/lang/String;
    //   127: aload 10
    //   129: athrow
    //   130: astore 9
    //   132: aload_0
    //   133: getfield 54	com/worklight/utils/WLAlertDialog:message	Ljava/lang/String;
    //   136: ifnonnull -74 -> 62
    //   139: aload_0
    //   140: aload_2
    //   141: putfield 54	com/worklight/utils/WLAlertDialog:message	Ljava/lang/String;
    //   144: goto -82 -> 62
    //   147: astore 8
    //   149: aload_0
    //   150: getfield 54	com/worklight/utils/WLAlertDialog:message	Ljava/lang/String;
    //   153: ifnonnull +8 -> 161
    //   156: aload_0
    //   157: aload_2
    //   158: putfield 54	com/worklight/utils/WLAlertDialog:message	Ljava/lang/String;
    //   161: aload 8
    //   163: athrow
    //   164: astore 7
    //   166: aload_0
    //   167: getfield 56	com/worklight/utils/WLAlertDialog:positiveButtonText	Ljava/lang/String;
    //   170: ifnonnull +26 -> 196
    //   173: aload_0
    //   174: aload_3
    //   175: putfield 56	com/worklight/utils/WLAlertDialog:positiveButtonText	Ljava/lang/String;
    //   178: return
    //   179: astore 6
    //   181: aload_0
    //   182: getfield 56	com/worklight/utils/WLAlertDialog:positiveButtonText	Ljava/lang/String;
    //   185: ifnonnull +8 -> 193
    //   188: aload_0
    //   189: aload_3
    //   190: putfield 56	com/worklight/utils/WLAlertDialog:positiveButtonText	Ljava/lang/String;
    //   193: aload 6
    //   195: athrow
    //   196: return
    //
    // Exception table:
    //   from	to	target	type
    //   14	24	85	java/util/MissingResourceException
    //   0	10	102	java/util/MissingResourceException
    //   24	36	102	java/util/MissingResourceException
    //   50	62	102	java/util/MissingResourceException
    //   72	84	102	java/util/MissingResourceException
    //   87	99	102	java/util/MissingResourceException
    //   115	127	102	java/util/MissingResourceException
    //   127	130	102	java/util/MissingResourceException
    //   132	144	102	java/util/MissingResourceException
    //   149	161	102	java/util/MissingResourceException
    //   161	164	102	java/util/MissingResourceException
    //   166	178	102	java/util/MissingResourceException
    //   181	193	102	java/util/MissingResourceException
    //   193	196	102	java/util/MissingResourceException
    //   14	24	113	finally
    //   40	50	130	java/util/MissingResourceException
    //   40	50	147	finally
    //   62	72	164	java/util/MissingResourceException
    //   62	72	179	finally
  }

  public DialogInterface.OnClickListener getClickListener()
  {
    return this.clickListener;
  }

  public String getMessage()
  {
    return this.message;
  }

  public String getPositiveButtonText()
  {
    return this.positiveButtonText;
  }

  public String getTitle()
  {
    return this.title;
  }

  public void run()
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder((Activity)WLClient.getInstance().getContext());
    localBuilder.setMessage(this.message);
    localBuilder.setTitle(this.title);
    localBuilder.setPositiveButton(this.positiveButtonText, this.clickListener);
    localBuilder.show();
  }

  public void setClickListener(DialogInterface.OnClickListener paramOnClickListener)
  {
    this.clickListener = paramOnClickListener;
  }

  public void setMessage(String paramString)
  {
    this.message = paramString;
  }

  public void setPositiveButtonText(String paramString)
  {
    this.positiveButtonText = paramString;
  }

  public void setTitle(String paramString)
  {
    this.title = paramString;
  }

  public void show()
  {
    try
    {
      Activity localActivity = (Activity)WLClient.getInstance().getContext();
      if (localActivity == null)
        return;
      localActivity.runOnUiThread(this);
      return;
    }
    catch (Exception localException)
    {
      WLUtils.error(localException.getMessage());
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.utils.WLAlertDialog
 * JD-Core Version:    0.6.0
 */