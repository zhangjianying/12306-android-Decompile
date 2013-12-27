package com.google.common.net;

import com.google.common.annotations.Beta;
import java.net.InetAddress;
import java.text.ParseException;
import javax.annotation.Nullable;

@Beta
public final class HostSpecifier
{
  private final String canonicalForm;

  private HostSpecifier(String paramString)
  {
    this.canonicalForm = paramString;
  }

  public static HostSpecifier from(String paramString)
    throws ParseException
  {
    ParseException localParseException;
    try
    {
      HostSpecifier localHostSpecifier = fromValid(paramString);
      return localHostSpecifier;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      localParseException = new ParseException("Invalid host specifier: " + paramString, 0);
      localParseException.initCause(localIllegalArgumentException);
    }
    throw localParseException;
  }

  public static HostSpecifier fromValid(String paramString)
  {
    try
    {
      InetAddress localInetAddress2 = InetAddresses.forString(paramString);
      localObject = localInetAddress2;
      if (localObject != null);
    }
    catch (IllegalArgumentException localIllegalArgumentException1)
    {
      try
      {
        InetAddress localInetAddress1 = InetAddresses.forUriString(paramString);
        Object localObject = localInetAddress1;
        label22: if (localObject != null)
          return new HostSpecifier(InetAddresses.toUriString((InetAddress)localObject));
        InternetDomainName localInternetDomainName = InternetDomainName.fromLenient(paramString);
        if (localInternetDomainName.hasPublicSuffix())
          return new HostSpecifier(localInternetDomainName.name());
        throw new IllegalArgumentException("Domain name does not have a recognized public suffix: " + paramString);
        localIllegalArgumentException1 = localIllegalArgumentException1;
        localObject = null;
      }
      catch (IllegalArgumentException localIllegalArgumentException2)
      {
        break label22;
      }
    }
  }

  public static boolean isValid(String paramString)
  {
    try
    {
      fromValid(paramString);
      return true;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
    }
    return false;
  }

  public boolean equals(@Nullable Object paramObject)
  {
    if (this == paramObject)
      return true;
    if ((paramObject instanceof HostSpecifier))
    {
      HostSpecifier localHostSpecifier = (HostSpecifier)paramObject;
      return this.canonicalForm.equals(localHostSpecifier.canonicalForm);
    }
    return false;
  }

  public int hashCode()
  {
    return this.canonicalForm.hashCode();
  }

  public String toString()
  {
    return this.canonicalForm;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.net.HostSpecifier
 * JD-Core Version:    0.6.0
 */