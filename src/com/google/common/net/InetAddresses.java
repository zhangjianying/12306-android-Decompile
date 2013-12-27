package com.google.common.net;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.google.common.primitives.Ints;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.annotation.Nullable;

@Beta
public final class InetAddresses
{
  private static final Inet4Address ANY4;
  private static final int IPV4_PART_COUNT = 4;
  private static final int IPV6_PART_COUNT = 8;
  private static final Inet4Address LOOPBACK4 = (Inet4Address)forString("127.0.0.1");

  static
  {
    ANY4 = (Inet4Address)forString("0.0.0.0");
  }

  public static int coerceToInteger(InetAddress paramInetAddress)
  {
    return ByteStreams.newDataInput(getCoercedIPv4Address(paramInetAddress).getAddress()).readInt();
  }

  private static String convertDottedQuadToHex(String paramString)
  {
    int i = paramString.lastIndexOf(':');
    String str1 = paramString.substring(0, i + 1);
    byte[] arrayOfByte = textToNumericFormatV4(paramString.substring(i + 1));
    if (arrayOfByte == null)
      return null;
    String str2 = Integer.toHexString((0xFF & arrayOfByte[0]) << 8 | 0xFF & arrayOfByte[1]);
    String str3 = Integer.toHexString((0xFF & arrayOfByte[2]) << 8 | 0xFF & arrayOfByte[3]);
    return str1 + str2 + ":" + str3;
  }

  private static byte[] copyOfRange(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    Preconditions.checkNotNull(paramArrayOfByte);
    int i = Math.min(paramInt2, paramArrayOfByte.length);
    byte[] arrayOfByte = new byte[paramInt2 - paramInt1];
    System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, 0, i - paramInt1);
    return arrayOfByte;
  }

  public static InetAddress forString(String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokestatic 75	com/google/common/net/InetAddresses:textToNumericFormatV4	(Ljava/lang/String;)[B
    //   4: astore_1
    //   5: aload_1
    //   6: ifnonnull +8 -> 14
    //   9: aload_0
    //   10: invokestatic 118	com/google/common/net/InetAddresses:textToNumericFormatV6	(Ljava/lang/String;)[B
    //   13: astore_1
    //   14: aload_1
    //   15: ifnonnull +24 -> 39
    //   18: new 120	java/lang/IllegalArgumentException
    //   21: dup
    //   22: ldc 122
    //   24: iconst_1
    //   25: anewarray 4	java/lang/Object
    //   28: dup
    //   29: iconst_0
    //   30: aload_0
    //   31: aastore
    //   32: invokestatic 126	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   35: invokespecial 129	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   38: athrow
    //   39: aload_1
    //   40: invokestatic 135	java/net/InetAddress:getByAddress	([B)Ljava/net/InetAddress;
    //   43: astore_3
    //   44: aload_3
    //   45: areturn
    //   46: astore_2
    //   47: new 120	java/lang/IllegalArgumentException
    //   50: dup
    //   51: ldc 137
    //   53: iconst_1
    //   54: anewarray 4	java/lang/Object
    //   57: dup
    //   58: iconst_0
    //   59: aload_0
    //   60: aastore
    //   61: invokestatic 126	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   64: aload_2
    //   65: invokespecial 140	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   68: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   39	44	46	java/net/UnknownHostException
  }

  public static InetAddress forUriString(String paramString)
  {
    Preconditions.checkNotNull(paramString);
    boolean bool1;
    if (paramString.length() > 0)
      bool1 = true;
    while (true)
    {
      Preconditions.checkArgument(bool1, "host string is empty");
      try
      {
        InetAddress localInetAddress2 = forString(paramString);
        boolean bool2 = localInetAddress2 instanceof Inet4Address;
        if (bool2)
        {
          return localInetAddress2;
          bool1 = false;
        }
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        if ((!paramString.startsWith("[")) || (!paramString.endsWith("]")))
          throw new IllegalArgumentException("Not a valid address: \"" + paramString + '"');
        InetAddress localInetAddress1 = forString(paramString.substring(1, -1 + paramString.length()));
        if (!(localInetAddress1 instanceof Inet6Address))
          break;
        return localInetAddress1;
      }
    }
    throw new IllegalArgumentException("Not a valid address: \"" + paramString + '"');
  }

  public static Inet4Address fromInteger(int paramInt)
  {
    return getInet4Address(Ints.toByteArray(paramInt));
  }

  public static InetAddress fromLittleEndianByteArray(byte[] paramArrayOfByte)
    throws UnknownHostException
  {
    byte[] arrayOfByte = new byte[paramArrayOfByte.length];
    for (int i = 0; i < paramArrayOfByte.length; i++)
      arrayOfByte[i] = paramArrayOfByte[(-1 + (paramArrayOfByte.length - i))];
    return InetAddress.getByAddress(arrayOfByte);
  }

  public static Inet4Address get6to4IPv4Address(Inet6Address paramInet6Address)
  {
    boolean bool = is6to4Address(paramInet6Address);
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = paramInet6Address.getHostAddress();
    Preconditions.checkArgument(bool, "Address '%s' is not a 6to4 address.", arrayOfObject);
    return getInet4Address(copyOfRange(paramInet6Address.getAddress(), 2, 6));
  }

  public static Inet4Address getCoercedIPv4Address(InetAddress paramInetAddress)
  {
    if ((paramInetAddress instanceof Inet4Address))
      return (Inet4Address)paramInetAddress;
    byte[] arrayOfByte = paramInetAddress.getAddress();
    int i = 1;
    for (int j = 0; ; j++)
    {
      if (j < 15)
      {
        if (arrayOfByte[j] == 0)
          continue;
        i = 0;
      }
      if ((i == 0) || (arrayOfByte[15] != 1))
        break;
      return LOOPBACK4;
    }
    if ((i != 0) && (arrayOfByte[15] == 0))
      return ANY4;
    Inet6Address localInet6Address = (Inet6Address)paramInetAddress;
    long l;
    if (hasEmbeddedIPv4ClientAddress(localInet6Address))
      l = getEmbeddedIPv4ClientAddress(localInet6Address).hashCode();
    while (true)
    {
      int k = 0xE0000000 | hash64To32(l);
      if (k == -1)
        k = -2;
      return getInet4Address(Ints.toByteArray(k));
      l = ByteBuffer.wrap(localInet6Address.getAddress(), 0, 8).getLong();
    }
  }

  public static Inet4Address getCompatIPv4Address(Inet6Address paramInet6Address)
  {
    boolean bool = isCompatIPv4Address(paramInet6Address);
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = paramInet6Address.getHostAddress();
    Preconditions.checkArgument(bool, "Address '%s' is not IPv4-compatible.", arrayOfObject);
    return getInet4Address(copyOfRange(paramInet6Address.getAddress(), 12, 16));
  }

  public static Inet4Address getEmbeddedIPv4ClientAddress(Inet6Address paramInet6Address)
  {
    if (isCompatIPv4Address(paramInet6Address))
      return getCompatIPv4Address(paramInet6Address);
    if (is6to4Address(paramInet6Address))
      return get6to4IPv4Address(paramInet6Address);
    if (isTeredoAddress(paramInet6Address))
      return getTeredoInfo(paramInet6Address).getClient();
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = paramInet6Address.getHostAddress();
    throw new IllegalArgumentException(String.format("'%s' has no embedded IPv4 address.", arrayOfObject));
  }

  private static Inet4Address getInet4Address(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte.length == 4);
    InetAddress localInetAddress;
    for (boolean bool = true; ; bool = false)
    {
      Object[] arrayOfObject1 = new Object[1];
      arrayOfObject1[0] = Integer.valueOf(paramArrayOfByte.length);
      Preconditions.checkArgument(bool, "Byte array has invalid length for an IPv4 address: %s != 4.", arrayOfObject1);
      try
      {
        localInetAddress = InetAddress.getByAddress(paramArrayOfByte);
        if ((localInetAddress instanceof Inet4Address))
          break;
        Object[] arrayOfObject3 = new Object[1];
        arrayOfObject3[0] = localInetAddress.getHostAddress();
        throw new UnknownHostException(String.format("'%s' is not an IPv4 address.", arrayOfObject3));
      }
      catch (UnknownHostException localUnknownHostException)
      {
        Object[] arrayOfObject2 = new Object[1];
        arrayOfObject2[0] = Arrays.toString(paramArrayOfByte);
        throw new IllegalArgumentException(String.format("Host address '%s' is not a valid IPv4 address.", arrayOfObject2), localUnknownHostException);
      }
    }
    Inet4Address localInet4Address = (Inet4Address)localInetAddress;
    return localInet4Address;
  }

  public static Inet4Address getIsatapIPv4Address(Inet6Address paramInet6Address)
  {
    boolean bool = isIsatapAddress(paramInet6Address);
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = paramInet6Address.getHostAddress();
    Preconditions.checkArgument(bool, "Address '%s' is not an ISATAP address.", arrayOfObject);
    return getInet4Address(copyOfRange(paramInet6Address.getAddress(), 12, 16));
  }

  public static TeredoInfo getTeredoInfo(Inet6Address paramInet6Address)
  {
    boolean bool = isTeredoAddress(paramInet6Address);
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = paramInet6Address.getHostAddress();
    Preconditions.checkArgument(bool, "Address '%s' is not a Teredo address.", arrayOfObject);
    byte[] arrayOfByte1 = paramInet6Address.getAddress();
    Inet4Address localInet4Address = getInet4Address(copyOfRange(arrayOfByte1, 4, 8));
    int i = 0xFFFF & ByteStreams.newDataInput(arrayOfByte1, 8).readShort();
    int j = 0xFFFF & (0xFFFFFFFF ^ ByteStreams.newDataInput(arrayOfByte1, 10).readShort());
    byte[] arrayOfByte2 = copyOfRange(arrayOfByte1, 12, 16);
    for (int k = 0; k < arrayOfByte2.length; k++)
      arrayOfByte2[k] = (byte)(0xFFFFFFFF ^ arrayOfByte2[k]);
    return new TeredoInfo(localInet4Address, getInet4Address(arrayOfByte2), j, i);
  }

  public static boolean hasEmbeddedIPv4ClientAddress(Inet6Address paramInet6Address)
  {
    return (isCompatIPv4Address(paramInet6Address)) || (is6to4Address(paramInet6Address)) || (isTeredoAddress(paramInet6Address));
  }

  @VisibleForTesting
  static int hash64To32(long paramLong)
  {
    long l1 = (0xFFFFFFFF ^ paramLong) + (paramLong << 18);
    long l2 = 21L * (l1 ^ l1 >>> 31);
    long l3 = l2 ^ l2 >>> 11;
    long l4 = l3 + (l3 << 6);
    return (int)(l4 ^ l4 >>> 22);
  }

  public static boolean is6to4Address(Inet6Address paramInet6Address)
  {
    byte[] arrayOfByte = paramInet6Address.getAddress();
    return (arrayOfByte[0] == 32) && (arrayOfByte[1] == 2);
  }

  public static boolean isCompatIPv4Address(Inet6Address paramInet6Address)
  {
    if (!paramInet6Address.isIPv4CompatibleAddress());
    byte[] arrayOfByte;
    do
    {
      return false;
      arrayOfByte = paramInet6Address.getAddress();
    }
    while ((arrayOfByte[12] == 0) && (arrayOfByte[13] == 0) && (arrayOfByte[14] == 0) && ((arrayOfByte[15] == 0) || (arrayOfByte[15] == 1)));
    return true;
  }

  public static boolean isInetAddress(String paramString)
  {
    try
    {
      forString(paramString);
      return true;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
    }
    return false;
  }

  public static boolean isIsatapAddress(Inet6Address paramInet6Address)
  {
    if (isTeredoAddress(paramInet6Address));
    byte[] arrayOfByte;
    do
    {
      return false;
      arrayOfByte = paramInet6Address.getAddress();
    }
    while (((0x3 | arrayOfByte[8]) != 3) || (arrayOfByte[9] != 0) || (arrayOfByte[10] != 94) || (arrayOfByte[11] != -2));
    return true;
  }

  public static boolean isTeredoAddress(Inet6Address paramInet6Address)
  {
    byte[] arrayOfByte = paramInet6Address.getAddress();
    return (arrayOfByte[0] == 32) && (arrayOfByte[1] == 1) && (arrayOfByte[2] == 0) && (arrayOfByte[3] == 0);
  }

  public static boolean isUriInetAddress(String paramString)
  {
    try
    {
      forUriString(paramString);
      return true;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
    }
    return false;
  }

  private static byte[] textToNumericFormatV4(String paramString)
  {
    byte[] arrayOfByte;
    if (paramString.contains(":"))
      arrayOfByte = null;
    while (true)
    {
      return arrayOfByte;
      String[] arrayOfString = paramString.split("\\.");
      if (arrayOfString.length != 4)
        return null;
      arrayOfByte = new byte[4];
      int i = 0;
      try
      {
        while (i < arrayOfByte.length)
        {
          int j = Integer.parseInt(arrayOfString[i]);
          if ((j < 0) || (j > 255))
            break label105;
          if ((arrayOfString[i].startsWith("0")) && (arrayOfString[i].length() != 1))
            return null;
          arrayOfByte[i] = (byte)j;
          i++;
        }
      }
      catch (NumberFormatException localNumberFormatException)
      {
        return null;
      }
    }
    label105: return null;
  }

  private static byte[] textToNumericFormatV6(String paramString)
  {
    if (!paramString.contains(":"))
      return null;
    if (paramString.contains(":::"))
      return null;
    if (paramString.contains("."))
    {
      paramString = convertDottedQuadToHex(paramString);
      if (paramString == null)
        return null;
    }
    ByteBuffer localByteBuffer = ByteBuffer.allocate(16);
    String[] arrayOfString1 = paramString.split("::", 2);
    String[] arrayOfString3;
    int i3;
    if (!arrayOfString1[0].equals(""))
    {
      arrayOfString3 = arrayOfString1[0].split(":", 8);
      i3 = 0;
    }
    while (true)
    {
      try
      {
        if (i3 >= arrayOfString3.length)
          continue;
        if (arrayOfString3[i3].equals(""))
          return null;
        int i4 = Integer.parseInt(arrayOfString3[i3], 16);
        localByteBuffer.putShort(i3 * 2, (short)i4);
        i3++;
        continue;
        i = arrayOfString3.length;
        int j = arrayOfString1.length;
        int k = 0;
        if (j <= 1)
          continue;
        if (arrayOfString1[1].equals(""))
          continue;
        String[] arrayOfString2 = arrayOfString1[1].split(":", 8);
        int n = 0;
        int m;
        try
        {
          if (n >= arrayOfString2.length)
            continue;
          int i1 = -1 + (arrayOfString2.length - n);
          if (arrayOfString2[i1].equals(""))
            return null;
          int i2 = Integer.parseInt(arrayOfString2[i1], 16);
          localByteBuffer.putShort(2 * (-1 + (8 - n)), (short)i2);
          n++;
          continue;
          k = arrayOfString2.length;
          m = i + k;
          if (m <= 8)
            continue;
          return null;
        }
        catch (NumberFormatException localNumberFormatException1)
        {
          return null;
        }
        k = 1;
        continue;
        if ((arrayOfString1.length == 1) && (m != 8))
          return null;
        return localByteBuffer.array();
      }
      catch (NumberFormatException localNumberFormatException2)
      {
        return null;
      }
      int i = 1;
    }
  }

  public static String toUriString(InetAddress paramInetAddress)
  {
    if ((paramInetAddress instanceof Inet6Address))
      return "[" + paramInetAddress.getHostAddress() + "]";
    return paramInetAddress.getHostAddress();
  }

  @Beta
  public static final class TeredoInfo
  {
    private final Inet4Address client;
    private final int flags;
    private final int port;
    private final Inet4Address server;

    public TeredoInfo(@Nullable Inet4Address paramInet4Address1, @Nullable Inet4Address paramInet4Address2, int paramInt1, int paramInt2)
    {
      boolean bool1;
      boolean bool2;
      if ((paramInt1 >= 0) && (paramInt1 <= 65535))
      {
        bool1 = true;
        Object[] arrayOfObject1 = new Object[1];
        arrayOfObject1[0] = Integer.valueOf(paramInt1);
        Preconditions.checkArgument(bool1, "port '%d' is out of range (0 <= port <= 0xffff)", arrayOfObject1);
        if ((paramInt2 < 0) || (paramInt2 > 65535))
          break label115;
        bool2 = true;
        label55: Object[] arrayOfObject2 = new Object[1];
        arrayOfObject2[0] = Integer.valueOf(paramInt2);
        Preconditions.checkArgument(bool2, "flags '%d' is out of range (0 <= flags <= 0xffff)", arrayOfObject2);
        if (paramInet4Address1 == null)
          break label121;
        this.server = paramInet4Address1;
        label88: if (paramInet4Address2 == null)
          break label131;
      }
      label131: for (this.client = paramInet4Address2; ; this.client = InetAddresses.ANY4)
      {
        this.port = paramInt1;
        this.flags = paramInt2;
        return;
        bool1 = false;
        break;
        label115: bool2 = false;
        break label55;
        label121: this.server = InetAddresses.ANY4;
        break label88;
      }
    }

    public Inet4Address getClient()
    {
      return this.client;
    }

    public int getFlags()
    {
      return this.flags;
    }

    public int getPort()
    {
      return this.port;
    }

    public Inet4Address getServer()
    {
      return this.server;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.net.InetAddresses
 * JD-Core Version:    0.6.0
 */