package org.codehaus.jackson.io;

import org.codehaus.jackson.SerializableString;

public class SerializedString
  implements SerializableString
{
  protected char[] _quotedChars;
  protected byte[] _quotedUTF8Ref;
  protected byte[] _unquotedUTF8Ref;
  protected final String _value;

  public SerializedString(String paramString)
  {
    this._value = paramString;
  }

  public final char[] asQuotedChars()
  {
    char[] arrayOfChar = this._quotedChars;
    if (arrayOfChar == null)
    {
      arrayOfChar = JsonStringEncoder.getInstance().quoteAsString(this._value);
      this._quotedChars = arrayOfChar;
    }
    return arrayOfChar;
  }

  public final byte[] asQuotedUTF8()
  {
    byte[] arrayOfByte = this._quotedUTF8Ref;
    if (arrayOfByte == null)
    {
      arrayOfByte = JsonStringEncoder.getInstance().quoteAsUTF8(this._value);
      this._quotedUTF8Ref = arrayOfByte;
    }
    return arrayOfByte;
  }

  public final byte[] asUnquotedUTF8()
  {
    byte[] arrayOfByte = this._unquotedUTF8Ref;
    if (arrayOfByte == null)
    {
      arrayOfByte = JsonStringEncoder.getInstance().encodeAsUTF8(this._value);
      this._unquotedUTF8Ref = arrayOfByte;
    }
    return arrayOfByte;
  }

  public final int charLength()
  {
    return this._value.length();
  }

  public final boolean equals(Object paramObject)
  {
    if (paramObject == this)
      return true;
    if ((paramObject == null) || (paramObject.getClass() != getClass()))
      return false;
    SerializedString localSerializedString = (SerializedString)paramObject;
    return this._value.equals(localSerializedString._value);
  }

  public final String getValue()
  {
    return this._value;
  }

  public final int hashCode()
  {
    return this._value.hashCode();
  }

  public final String toString()
  {
    return this._value;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.io.SerializedString
 * JD-Core Version:    0.6.0
 */