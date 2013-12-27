package com.worklight.common.security;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.webkit.WebView;
import com.worklight.androidgap.WLDroidGap;
import com.worklight.common.WLConfig;
import com.worklight.common.WLUtils;
import com.worklight.utils.Base64;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.UUID;
import org.json.JSONObject;

public class WLDeviceAuthManager
{
  private static final String ALG = "alg";
  private static final String APPLICATION = "application";
  private static final String EXP = "exp";
  private static final String JPK = "jpk";
  private static final String KEYSTORE_FILENAME = ".keystore";
  private static final String MOD = "mod";
  private static final String RSA = "RSA";
  private static final String X5C = "x5c";
  private static WLDeviceAuthManager instance;
  private static char[] keyStorePassword = null;
  private Context context;
  private String deviceUuid;
  private HashMap<String, KeyPair> keyPairHash = new HashMap();
  private WebView webView;

  private String getAlias(String paramString)
  {
    if (paramString.equals("application"))
      paramString = "app:" + WLDroidGap.getWLConfig().getAppId();
    return paramString;
  }

  public static WLDeviceAuthManager getInstance()
  {
    monitorenter;
    try
    {
      if (instance == null)
        instance = new WLDeviceAuthManager();
      WLDeviceAuthManager localWLDeviceAuthManager = instance;
      return localWLDeviceAuthManager;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  private byte[] signCsrData(String paramString, PrivateKey paramPrivateKey)
    throws NoSuchAlgorithmException, InvalidKeyException, SignatureException
  {
    Signature localSignature = Signature.getInstance("SHA256withRSA");
    localSignature.initSign(paramPrivateKey);
    localSignature.update(paramString.getBytes());
    return localSignature.sign();
  }

  private byte[] signData(String paramString, PrivateKey paramPrivateKey)
    throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException
  {
    Signature localSignature = Signature.getInstance("SHA256withRSA");
    localSignature.initSign(paramPrivateKey);
    localSignature.update(paramString.getBytes());
    return localSignature.sign();
  }

  public void csrCertificateRecieveFailed(String paramString)
  {
    WLUtils.error(paramString);
    ((WLDroidGap)this.context).runOnUiThread(new Runnable()
    {
      public void run()
      {
        WLDeviceAuthManager.this.webView.loadUrl("javascript:WL.DiagnosticDialog.showDialog(WL.ClientMessages.wlclientInitFailure, WL.ClientMessages.deviceAuthenticationFail, false, false);");
      }
    });
  }

  public KeyPair generateKeyPair(String paramString)
    throws NoSuchAlgorithmException
  {
    KeyPairGenerator localKeyPairGenerator = KeyPairGenerator.getInstance("RSA");
    localKeyPairGenerator.initialize(512);
    KeyPair localKeyPair = localKeyPairGenerator.genKeyPair();
    this.keyPairHash.put(getAlias(paramString), localKeyPair);
    return localKeyPair;
  }

  public String getDeviceUUID(Context paramContext)
  {
    if (this.deviceUuid == null)
    {
      boolean bool = paramContext.getPackageManager().hasSystemFeature("android.hardware.wifi");
      String str1 = null;
      if (bool)
        str1 = ((WifiManager)paramContext.getSystemService("wifi")).getConnectionInfo().getMacAddress();
      String str2 = Settings.Secure.getString(paramContext.getContentResolver(), "android_id");
      if (str1 != null)
        str2 = str2 + str1;
      this.deviceUuid = UUID.nameUUIDFromBytes(str2.getBytes()).toString();
    }
    return this.deviceUuid;
  }

  // ERROR //
  protected KeyStore.PrivateKeyEntry getPrivateKeyEntry(String paramString)
    throws java.io.IOException, java.security.KeyStoreException, NoSuchAlgorithmException, java.security.cert.CertificateException, android.content.pm.PackageManager.NameNotFoundException, java.security.UnrecoverableEntryException
  {
    // Byte code:
    //   0: invokestatic 236	java/security/KeyStore:getDefaultType	()Ljava/lang/String;
    //   3: invokestatic 239	java/security/KeyStore:getInstance	(Ljava/lang/String;)Ljava/security/KeyStore;
    //   6: astore_2
    //   7: new 241	java/io/File
    //   10: dup
    //   11: new 66	java/lang/StringBuilder
    //   14: dup
    //   15: invokespecial 67	java/lang/StringBuilder:<init>	()V
    //   18: aload_0
    //   19: getfield 135	com/worklight/common/security/WLDeviceAuthManager:context	Landroid/content/Context;
    //   22: invokevirtual 245	android/content/Context:getFilesDir	()Ljava/io/File;
    //   25: invokevirtual 248	java/io/File:getAbsolutePath	()Ljava/lang/String;
    //   28: invokevirtual 73	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   31: ldc 250
    //   33: invokevirtual 73	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   36: ldc 20
    //   38: invokevirtual 73	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   41: invokevirtual 88	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   44: invokespecial 252	java/io/File:<init>	(Ljava/lang/String;)V
    //   47: astore_3
    //   48: aload_0
    //   49: aload_1
    //   50: invokespecial 161	com/worklight/common/security/WLDeviceAuthManager:getAlias	(Ljava/lang/String;)Ljava/lang/String;
    //   53: astore 4
    //   55: aconst_null
    //   56: astore 5
    //   58: aload_3
    //   59: invokevirtual 256	java/io/File:exists	()Z
    //   62: ifeq +346 -> 408
    //   65: new 258	java/io/FileInputStream
    //   68: dup
    //   69: aload_3
    //   70: invokespecial 261	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   73: astore 19
    //   75: aload_2
    //   76: aload 19
    //   78: getstatic 44	com/worklight/common/security/WLDeviceAuthManager:keyStorePassword	[C
    //   81: invokevirtual 265	java/security/KeyStore:load	(Ljava/io/InputStream;[C)V
    //   84: aload_2
    //   85: aload 4
    //   87: new 267	java/security/KeyStore$PasswordProtection
    //   90: dup
    //   91: getstatic 44	com/worklight/common/security/WLDeviceAuthManager:keyStorePassword	[C
    //   94: invokespecial 270	java/security/KeyStore$PasswordProtection:<init>	([C)V
    //   97: invokevirtual 274	java/security/KeyStore:getEntry	(Ljava/lang/String;Ljava/security/KeyStore$ProtectionParameter;)Ljava/security/KeyStore$Entry;
    //   100: checkcast 276	java/security/KeyStore$PrivateKeyEntry
    //   103: astore 17
    //   105: aload 17
    //   107: ifnull +16 -> 123
    //   110: aload 19
    //   112: ifnull +8 -> 120
    //   115: aload 19
    //   117: invokevirtual 279	java/io/FileInputStream:close	()V
    //   120: aload 17
    //   122: areturn
    //   123: ldc 11
    //   125: aload_1
    //   126: invokevirtual 282	java/lang/String:equalsIgnoreCase	(Ljava/lang/String;)Z
    //   129: istore 22
    //   131: iload 22
    //   133: ifeq +15 -> 148
    //   136: aload 19
    //   138: ifnull +8 -> 146
    //   141: aload 19
    //   143: invokevirtual 279	java/io/FileInputStream:close	()V
    //   146: aconst_null
    //   147: areturn
    //   148: aload 19
    //   150: ifnull +509 -> 659
    //   153: aload 19
    //   155: invokevirtual 279	java/io/FileInputStream:close	()V
    //   158: aload_0
    //   159: getfield 135	com/worklight/common/security/WLDeviceAuthManager:context	Landroid/content/Context;
    //   162: invokevirtual 175	android/content/Context:getPackageManager	()Landroid/content/pm/PackageManager;
    //   165: aload_0
    //   166: getfield 135	com/worklight/common/security/WLDeviceAuthManager:context	Landroid/content/Context;
    //   169: invokevirtual 286	android/content/Context:getApplicationInfo	()Landroid/content/pm/ApplicationInfo;
    //   172: getfield 292	android/content/pm/ApplicationInfo:uid	I
    //   175: invokevirtual 296	android/content/pm/PackageManager:getPackagesForUid	(I)[Ljava/lang/String;
    //   178: astore 6
    //   180: invokestatic 236	java/security/KeyStore:getDefaultType	()Ljava/lang/String;
    //   183: invokestatic 239	java/security/KeyStore:getInstance	(Ljava/lang/String;)Ljava/security/KeyStore;
    //   186: astore 7
    //   188: aload 6
    //   190: arraylength
    //   191: istore 8
    //   193: iconst_0
    //   194: istore 9
    //   196: iload 9
    //   198: iload 8
    //   200: if_icmpge +386 -> 586
    //   203: aload 6
    //   205: iload 9
    //   207: aaload
    //   208: astore 10
    //   210: new 241	java/io/File
    //   213: dup
    //   214: new 66	java/lang/StringBuilder
    //   217: dup
    //   218: invokespecial 67	java/lang/StringBuilder:<init>	()V
    //   221: aload_0
    //   222: getfield 135	com/worklight/common/security/WLDeviceAuthManager:context	Landroid/content/Context;
    //   225: aload 10
    //   227: iconst_0
    //   228: invokevirtual 300	android/content/Context:createPackageContext	(Ljava/lang/String;I)Landroid/content/Context;
    //   231: invokevirtual 245	android/content/Context:getFilesDir	()Ljava/io/File;
    //   234: invokevirtual 248	java/io/File:getAbsolutePath	()Ljava/lang/String;
    //   237: invokevirtual 73	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   240: ldc 250
    //   242: invokevirtual 73	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   245: ldc 20
    //   247: invokevirtual 73	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   250: invokevirtual 88	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   253: invokespecial 252	java/io/File:<init>	(Ljava/lang/String;)V
    //   256: astore 11
    //   258: aconst_null
    //   259: astore 12
    //   261: aconst_null
    //   262: astore 13
    //   264: aload 11
    //   266: invokevirtual 256	java/io/File:exists	()Z
    //   269: ifeq +74 -> 343
    //   272: new 258	java/io/FileInputStream
    //   275: dup
    //   276: aload 11
    //   278: invokespecial 261	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   281: astore 14
    //   283: aload 7
    //   285: aload 14
    //   287: getstatic 44	com/worklight/common/security/WLDeviceAuthManager:keyStorePassword	[C
    //   290: invokevirtual 265	java/security/KeyStore:load	(Ljava/io/InputStream;[C)V
    //   293: aload 7
    //   295: aload 4
    //   297: new 267	java/security/KeyStore$PasswordProtection
    //   300: dup
    //   301: getstatic 44	com/worklight/common/security/WLDeviceAuthManager:keyStorePassword	[C
    //   304: invokespecial 270	java/security/KeyStore$PasswordProtection:<init>	([C)V
    //   307: invokevirtual 274	java/security/KeyStore:getEntry	(Ljava/lang/String;Ljava/security/KeyStore$ProtectionParameter;)Ljava/security/KeyStore$Entry;
    //   310: checkcast 276	java/security/KeyStore$PrivateKeyEntry
    //   313: astore 17
    //   315: aload 17
    //   317: ifnonnull +113 -> 430
    //   320: aload 14
    //   322: invokevirtual 279	java/io/FileInputStream:close	()V
    //   325: aload 14
    //   327: ifnull +8 -> 335
    //   330: aload 14
    //   332: invokevirtual 279	java/io/FileInputStream:close	()V
    //   335: iconst_0
    //   336: ifeq +320 -> 656
    //   339: aconst_null
    //   340: invokevirtual 303	java/io/FileOutputStream:close	()V
    //   343: iinc 9 1
    //   346: goto -150 -> 196
    //   349: astore 20
    //   351: new 66	java/lang/StringBuilder
    //   354: dup
    //   355: invokespecial 67	java/lang/StringBuilder:<init>	()V
    //   358: ldc_w 305
    //   361: invokevirtual 73	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   364: aload 20
    //   366: invokevirtual 308	java/io/IOException:getMessage	()Ljava/lang/String;
    //   369: invokevirtual 73	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   372: invokevirtual 88	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   375: aload 20
    //   377: invokestatic 311	com/worklight/common/WLUtils:error	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   380: aload 5
    //   382: ifnull -224 -> 158
    //   385: aload 5
    //   387: invokevirtual 279	java/io/FileInputStream:close	()V
    //   390: goto -232 -> 158
    //   393: astore 21
    //   395: aload 5
    //   397: ifnull +8 -> 405
    //   400: aload 5
    //   402: invokevirtual 279	java/io/FileInputStream:close	()V
    //   405: aload 21
    //   407: athrow
    //   408: ldc 11
    //   410: aload_1
    //   411: invokevirtual 282	java/lang/String:equalsIgnoreCase	(Ljava/lang/String;)Z
    //   414: ifeq +5 -> 419
    //   417: aconst_null
    //   418: areturn
    //   419: aload_2
    //   420: aconst_null
    //   421: getstatic 44	com/worklight/common/security/WLDeviceAuthManager:keyStorePassword	[C
    //   424: invokevirtual 265	java/security/KeyStore:load	(Ljava/io/InputStream;[C)V
    //   427: goto -269 -> 158
    //   430: new 302	java/io/FileOutputStream
    //   433: dup
    //   434: aload_3
    //   435: invokespecial 312	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   438: astore 18
    //   440: aload_2
    //   441: aload 4
    //   443: aload 17
    //   445: invokevirtual 316	java/security/KeyStore$PrivateKeyEntry:getPrivateKey	()Ljava/security/PrivateKey;
    //   448: getstatic 44	com/worklight/common/security/WLDeviceAuthManager:keyStorePassword	[C
    //   451: aload 17
    //   453: invokevirtual 320	java/security/KeyStore$PrivateKeyEntry:getCertificateChain	()[Ljava/security/cert/Certificate;
    //   456: invokevirtual 324	java/security/KeyStore:setKeyEntry	(Ljava/lang/String;Ljava/security/Key;[C[Ljava/security/cert/Certificate;)V
    //   459: aload_2
    //   460: aload 18
    //   462: getstatic 44	com/worklight/common/security/WLDeviceAuthManager:keyStorePassword	[C
    //   465: invokevirtual 328	java/security/KeyStore:store	(Ljava/io/OutputStream;[C)V
    //   468: aload 18
    //   470: invokevirtual 303	java/io/FileOutputStream:close	()V
    //   473: aload 14
    //   475: invokevirtual 279	java/io/FileInputStream:close	()V
    //   478: aload 14
    //   480: ifnull +8 -> 488
    //   483: aload 14
    //   485: invokevirtual 279	java/io/FileInputStream:close	()V
    //   488: aload 18
    //   490: ifnull -370 -> 120
    //   493: aload 18
    //   495: invokevirtual 303	java/io/FileOutputStream:close	()V
    //   498: aload 17
    //   500: areturn
    //   501: astore 15
    //   503: new 66	java/lang/StringBuilder
    //   506: dup
    //   507: invokespecial 67	java/lang/StringBuilder:<init>	()V
    //   510: ldc_w 330
    //   513: invokevirtual 73	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   516: aload 15
    //   518: invokevirtual 308	java/io/IOException:getMessage	()Ljava/lang/String;
    //   521: invokevirtual 73	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   524: ldc_w 332
    //   527: invokevirtual 73	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   530: invokevirtual 88	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   533: aload 15
    //   535: invokestatic 311	com/worklight/common/WLUtils:error	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   538: aload 12
    //   540: ifnull +8 -> 548
    //   543: aload 12
    //   545: invokevirtual 279	java/io/FileInputStream:close	()V
    //   548: aload 13
    //   550: ifnull -207 -> 343
    //   553: aload 13
    //   555: invokevirtual 303	java/io/FileOutputStream:close	()V
    //   558: goto -215 -> 343
    //   561: astore 16
    //   563: aload 12
    //   565: ifnull +8 -> 573
    //   568: aload 12
    //   570: invokevirtual 279	java/io/FileInputStream:close	()V
    //   573: aload 13
    //   575: ifnull +8 -> 583
    //   578: aload 13
    //   580: invokevirtual 303	java/io/FileOutputStream:close	()V
    //   583: aload 16
    //   585: athrow
    //   586: aconst_null
    //   587: areturn
    //   588: astore 16
    //   590: aload 14
    //   592: astore 12
    //   594: aconst_null
    //   595: astore 13
    //   597: goto -34 -> 563
    //   600: astore 16
    //   602: aload 18
    //   604: astore 13
    //   606: aload 14
    //   608: astore 12
    //   610: goto -47 -> 563
    //   613: astore 15
    //   615: aload 14
    //   617: astore 12
    //   619: aconst_null
    //   620: astore 13
    //   622: goto -119 -> 503
    //   625: astore 15
    //   627: aload 18
    //   629: astore 13
    //   631: aload 14
    //   633: astore 12
    //   635: goto -132 -> 503
    //   638: astore 21
    //   640: aload 19
    //   642: astore 5
    //   644: goto -249 -> 395
    //   647: astore 20
    //   649: aload 19
    //   651: astore 5
    //   653: goto -302 -> 351
    //   656: goto -313 -> 343
    //   659: goto -501 -> 158
    //
    // Exception table:
    //   from	to	target	type
    //   65	75	349	java/io/IOException
    //   65	75	393	finally
    //   351	380	393	finally
    //   272	283	501	java/io/IOException
    //   272	283	561	finally
    //   503	538	561	finally
    //   283	315	588	finally
    //   320	325	588	finally
    //   430	440	588	finally
    //   440	478	600	finally
    //   283	315	613	java/io/IOException
    //   320	325	613	java/io/IOException
    //   430	440	613	java/io/IOException
    //   440	478	625	java/io/IOException
    //   75	105	638	finally
    //   123	131	638	finally
    //   75	105	647	java/io/IOException
    //   123	131	647	java/io/IOException
  }

  public void init(Activity paramActivity, WebView paramWebView)
  {
    this.webView = paramWebView;
    this.context = paramActivity;
  }

  public boolean isCertificateExists(String paramString)
  {
    if (this.context == null)
      return false;
    try
    {
      KeyStore.PrivateKeyEntry localPrivateKeyEntry = getPrivateKeyEntry(paramString);
      int i = 0;
      if (localPrivateKeyEntry != null)
        i = 1;
      return i;
    }
    catch (Exception localException)
    {
      WLUtils.error("Failed to determine the existence of certificate for device authentication with " + localException.getMessage(), localException);
    }
    return false;
  }

  public void saveCertificate(String paramString1, String paramString2, String paramString3)
    throws Exception
  {
    if (paramString2 == null)
      throw new Exception("cannot save null certificate");
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(Base64.decode(paramString2.getBytes()));
    X509Certificate localX509Certificate = (X509Certificate)CertificateFactory.getInstance("X.509").generateCertificate(localByteArrayInputStream);
    localByteArrayInputStream.close();
    saveCertificate(paramString1, localX509Certificate, paramString3);
  }

  // ERROR //
  public void saveCertificate(String paramString1, Certificate paramCertificate, String paramString2)
    throws Exception
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokevirtual 376	com/worklight/common/security/WLDeviceAuthManager:isCertificateExists	(Ljava/lang/String;)Z
    //   5: ifeq +14 -> 19
    //   8: new 337	java/lang/Exception
    //   11: dup
    //   12: ldc_w 378
    //   15: invokespecial 345	java/lang/Exception:<init>	(Ljava/lang/String;)V
    //   18: athrow
    //   19: invokestatic 236	java/security/KeyStore:getDefaultType	()Ljava/lang/String;
    //   22: invokestatic 239	java/security/KeyStore:getInstance	(Ljava/lang/String;)Ljava/security/KeyStore;
    //   25: astore 4
    //   27: new 241	java/io/File
    //   30: dup
    //   31: new 66	java/lang/StringBuilder
    //   34: dup
    //   35: invokespecial 67	java/lang/StringBuilder:<init>	()V
    //   38: aload_0
    //   39: getfield 135	com/worklight/common/security/WLDeviceAuthManager:context	Landroid/content/Context;
    //   42: invokevirtual 245	android/content/Context:getFilesDir	()Ljava/io/File;
    //   45: invokevirtual 248	java/io/File:getAbsolutePath	()Ljava/lang/String;
    //   48: invokevirtual 73	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   51: ldc 250
    //   53: invokevirtual 73	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   56: ldc 20
    //   58: invokevirtual 73	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   61: invokevirtual 88	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   64: invokespecial 252	java/io/File:<init>	(Ljava/lang/String;)V
    //   67: astore 5
    //   69: aconst_null
    //   70: astore 6
    //   72: aconst_null
    //   73: astore 7
    //   75: aload 5
    //   77: invokevirtual 256	java/io/File:exists	()Z
    //   80: istore 10
    //   82: aconst_null
    //   83: astore 6
    //   85: aconst_null
    //   86: astore 7
    //   88: iload 10
    //   90: ifeq +123 -> 213
    //   93: new 258	java/io/FileInputStream
    //   96: dup
    //   97: aload 5
    //   99: invokespecial 261	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   102: astore 11
    //   104: aload 4
    //   106: aload 11
    //   108: getstatic 44	com/worklight/common/security/WLDeviceAuthManager:keyStorePassword	[C
    //   111: invokevirtual 265	java/security/KeyStore:load	(Ljava/io/InputStream;[C)V
    //   114: aload 11
    //   116: invokevirtual 279	java/io/FileInputStream:close	()V
    //   119: aload 11
    //   121: astore 6
    //   123: aload 4
    //   125: aload_0
    //   126: aload_1
    //   127: invokespecial 161	com/worklight/common/security/WLDeviceAuthManager:getAlias	(Ljava/lang/String;)Ljava/lang/String;
    //   130: aload_0
    //   131: getfield 52	com/worklight/common/security/WLDeviceAuthManager:keyPairHash	Ljava/util/HashMap;
    //   134: aload_0
    //   135: aload_1
    //   136: invokespecial 161	com/worklight/common/security/WLDeviceAuthManager:getAlias	(Ljava/lang/String;)Ljava/lang/String;
    //   139: invokevirtual 382	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   142: checkcast 384	java/security/KeyPair
    //   145: invokevirtual 387	java/security/KeyPair:getPrivate	()Ljava/security/PrivateKey;
    //   148: getstatic 44	com/worklight/common/security/WLDeviceAuthManager:keyStorePassword	[C
    //   151: iconst_1
    //   152: anewarray 389	java/security/cert/Certificate
    //   155: dup
    //   156: iconst_0
    //   157: aload_2
    //   158: aastore
    //   159: invokevirtual 324	java/security/KeyStore:setKeyEntry	(Ljava/lang/String;Ljava/security/Key;[C[Ljava/security/cert/Certificate;)V
    //   162: new 302	java/io/FileOutputStream
    //   165: dup
    //   166: aload 5
    //   168: invokespecial 312	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   171: astore 12
    //   173: aload 4
    //   175: aload 12
    //   177: getstatic 44	com/worklight/common/security/WLDeviceAuthManager:keyStorePassword	[C
    //   180: invokevirtual 328	java/security/KeyStore:store	(Ljava/io/OutputStream;[C)V
    //   183: aload_0
    //   184: getfield 52	com/worklight/common/security/WLDeviceAuthManager:keyPairHash	Ljava/util/HashMap;
    //   187: aload_3
    //   188: invokevirtual 392	java/util/HashMap:remove	(Ljava/lang/Object;)Ljava/lang/Object;
    //   191: pop
    //   192: aload 12
    //   194: ifnull +8 -> 202
    //   197: aload 12
    //   199: invokevirtual 303	java/io/FileOutputStream:close	()V
    //   202: aload 6
    //   204: ifnull +143 -> 347
    //   207: aload 6
    //   209: invokevirtual 279	java/io/FileInputStream:close	()V
    //   212: return
    //   213: aload 4
    //   215: aconst_null
    //   216: getstatic 44	com/worklight/common/security/WLDeviceAuthManager:keyStorePassword	[C
    //   219: invokevirtual 265	java/security/KeyStore:load	(Ljava/io/InputStream;[C)V
    //   222: aconst_null
    //   223: astore 6
    //   225: goto -102 -> 123
    //   228: astore 9
    //   230: new 66	java/lang/StringBuilder
    //   233: dup
    //   234: invokespecial 67	java/lang/StringBuilder:<init>	()V
    //   237: ldc_w 394
    //   240: invokevirtual 73	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   243: aload 9
    //   245: invokevirtual 340	java/lang/Exception:getMessage	()Ljava/lang/String;
    //   248: invokevirtual 73	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   251: invokevirtual 88	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   254: aload 9
    //   256: invokestatic 311	com/worklight/common/WLUtils:error	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   259: aload 7
    //   261: ifnull +8 -> 269
    //   264: aload 7
    //   266: invokevirtual 303	java/io/FileOutputStream:close	()V
    //   269: aload 6
    //   271: ifnull -59 -> 212
    //   274: aload 6
    //   276: invokevirtual 279	java/io/FileInputStream:close	()V
    //   279: return
    //   280: astore 8
    //   282: aload 7
    //   284: ifnull +8 -> 292
    //   287: aload 7
    //   289: invokevirtual 303	java/io/FileOutputStream:close	()V
    //   292: aload 6
    //   294: ifnull +8 -> 302
    //   297: aload 6
    //   299: invokevirtual 279	java/io/FileInputStream:close	()V
    //   302: aload 8
    //   304: athrow
    //   305: astore 8
    //   307: aload 11
    //   309: astore 6
    //   311: aconst_null
    //   312: astore 7
    //   314: goto -32 -> 282
    //   317: astore 8
    //   319: aload 12
    //   321: astore 7
    //   323: goto -41 -> 282
    //   326: astore 9
    //   328: aload 11
    //   330: astore 6
    //   332: aconst_null
    //   333: astore 7
    //   335: goto -105 -> 230
    //   338: astore 9
    //   340: aload 12
    //   342: astore 7
    //   344: goto -114 -> 230
    //   347: return
    //
    // Exception table:
    //   from	to	target	type
    //   75	82	228	java/lang/Exception
    //   93	104	228	java/lang/Exception
    //   123	173	228	java/lang/Exception
    //   213	222	228	java/lang/Exception
    //   75	82	280	finally
    //   93	104	280	finally
    //   123	173	280	finally
    //   213	222	280	finally
    //   230	259	280	finally
    //   104	119	305	finally
    //   173	192	317	finally
    //   104	119	326	java/lang/Exception
    //   173	192	338	java/lang/Exception
  }

  public String signCsr(JSONObject paramJSONObject, String paramString)
    throws Exception
  {
    JSONObject localJSONObject1 = new JSONObject();
    localJSONObject1.put("alg", "RS256");
    JSONObject localJSONObject2 = new JSONObject();
    String str1 = getAlias(paramString);
    KeyPair localKeyPair = (KeyPair)this.keyPairHash.get(str1);
    RSAPublicKey localRSAPublicKey = (RSAPublicKey)localKeyPair.getPublic();
    localJSONObject2.put("alg", "RSA");
    localJSONObject2.put("mod", Base64.encodeUrlSafe(localRSAPublicKey.getModulus().toByteArray(), "UTF-8"));
    localJSONObject2.put("exp", Base64.encodeUrlSafe(localRSAPublicKey.getPublicExponent().toByteArray(), "UTF-8"));
    localJSONObject1.put("jpk", localJSONObject2);
    String str2 = localJSONObject1.toString();
    String str3 = paramJSONObject.toString();
    String str4 = Base64.encodeUrlSafe(str2.getBytes(), "UTF-8") + "." + Base64.encodeUrlSafe(str3.getBytes(), "UTF-8");
    String str5 = Base64.encodeUrlSafe(signCsrData(str4, localKeyPair.getPrivate()), "UTF-8");
    return str4 + "." + str5;
  }

  public String signDeviceAuth(String paramString1, String paramString2, boolean paramBoolean)
    throws Exception
  {
    if ((paramBoolean) && (isCertificateExists(paramString2)))
    {
      JSONObject localJSONObject = new JSONObject();
      localJSONObject.put("alg", "RS256");
      KeyStore.PrivateKeyEntry localPrivateKeyEntry = getPrivateKeyEntry(paramString2);
      localJSONObject.put("x5c", Base64.encodeUrlSafe(localPrivateKeyEntry.getCertificate().getEncoded(), "UTF-8"));
      String str1 = localJSONObject.toString();
      String str2 = Base64.encodeUrlSafe(str1.getBytes(), "UTF-8") + "." + Base64.encodeUrlSafe(paramString1.getBytes(), "UTF-8");
      String str3 = Base64.encodeUrlSafe(signData(str2, localPrivateKeyEntry.getPrivateKey()), "UTF-8");
      return str2 + "." + str3;
    }
    return paramString1;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.common.security.WLDeviceAuthManager
 * JD-Core Version:    0.6.0
 */