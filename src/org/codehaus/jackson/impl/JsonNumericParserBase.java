package org.codehaus.jackson.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser.NumberType;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.io.NumberInput;
import org.codehaus.jackson.util.TextBuffer;

public abstract class JsonNumericParserBase extends JsonParserBase
{
  static final BigDecimal BD_MAX_INT;
  static final BigDecimal BD_MAX_LONG;
  static final BigDecimal BD_MIN_INT;
  static final BigDecimal BD_MIN_LONG = new BigDecimal(-9223372036854775808L);
  protected static final char CHAR_NULL = '\000';
  protected static final int INT_0 = 48;
  protected static final int INT_1 = 49;
  protected static final int INT_2 = 50;
  protected static final int INT_3 = 51;
  protected static final int INT_4 = 52;
  protected static final int INT_5 = 53;
  protected static final int INT_6 = 54;
  protected static final int INT_7 = 55;
  protected static final int INT_8 = 56;
  protected static final int INT_9 = 57;
  protected static final int INT_DECIMAL_POINT = 46;
  protected static final int INT_E = 69;
  protected static final int INT_MINUS = 45;
  protected static final int INT_PLUS = 43;
  protected static final int INT_e = 101;
  static final double MAX_INT_D = 2147483647.0D;
  static final long MAX_INT_L = 2147483647L;
  static final double MAX_LONG_D = 9.223372036854776E+018D;
  static final double MIN_INT_D = -2147483648.0D;
  static final long MIN_INT_L = -2147483648L;
  static final double MIN_LONG_D = -9.223372036854776E+018D;
  protected static final int NR_BIGDECIMAL = 16;
  protected static final int NR_BIGINT = 4;
  protected static final int NR_DOUBLE = 8;
  protected static final int NR_INT = 1;
  protected static final int NR_LONG = 2;
  protected static final int NR_UNKNOWN;
  protected int _expLength;
  protected int _fractLength;
  protected int _intLength;
  protected int _numTypesValid = 0;
  protected BigDecimal _numberBigDecimal;
  protected BigInteger _numberBigInt;
  protected double _numberDouble;
  protected int _numberInt;
  protected long _numberLong;
  protected boolean _numberNegative;

  static
  {
    BD_MAX_LONG = new BigDecimal(9223372036854775807L);
    BD_MIN_INT = new BigDecimal(-9223372036854775808L);
    BD_MAX_INT = new BigDecimal(9223372036854775807L);
  }

  protected JsonNumericParserBase(IOContext paramIOContext, int paramInt)
  {
    super(paramIOContext, paramInt);
  }

  private final void _parseSlowFloatValue(int paramInt)
    throws IOException, JsonParseException
  {
    if (paramInt == 16);
    try
    {
      this._numberBigDecimal = this._textBuffer.contentsAsDecimal();
      this._numTypesValid = 16;
      return;
      this._numberDouble = this._textBuffer.contentsAsDouble();
      this._numTypesValid = 8;
      return;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      _wrapError("Malformed numeric value '" + this._textBuffer.contentsAsString() + "'", localNumberFormatException);
    }
  }

  private final void _parseSlowIntValue(int paramInt1, char[] paramArrayOfChar, int paramInt2, int paramInt3)
    throws IOException, JsonParseException
  {
    String str = this._textBuffer.contentsAsString();
    try
    {
      if (NumberInput.inLongRange(paramArrayOfChar, paramInt2, paramInt3, this._numberNegative))
      {
        this._numberLong = Long.parseLong(str);
        this._numTypesValid = 2;
        return;
      }
      this._numberBigInt = new BigInteger(str);
      this._numTypesValid = 4;
      return;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      _wrapError("Malformed numeric value '" + str + "'", localNumberFormatException);
    }
  }

  protected void _parseNumericValue(int paramInt)
    throws IOException, JsonParseException
  {
    if (this._currToken == JsonToken.VALUE_NUMBER_INT)
    {
      char[] arrayOfChar = this._textBuffer.getTextBuffer();
      int i = this._textBuffer.getTextOffset();
      int j = this._intLength;
      if (this._numberNegative)
        i++;
      if (j <= 9)
      {
        int k = NumberInput.parseInt(arrayOfChar, i, j);
        if (this._numberNegative)
          k = -k;
        this._numberInt = k;
        this._numTypesValid = 1;
        return;
      }
      if (j <= 18)
      {
        long l = NumberInput.parseLong(arrayOfChar, i, j);
        if (this._numberNegative)
          l = -l;
        if (j == 10)
          if (this._numberNegative)
          {
            if (l >= -2147483648L)
            {
              this._numberInt = (int)l;
              this._numTypesValid = 1;
              return;
            }
          }
          else if (l <= 2147483647L)
          {
            this._numberInt = (int)l;
            this._numTypesValid = 1;
            return;
          }
        this._numberLong = l;
        this._numTypesValid = 2;
        return;
      }
      _parseSlowIntValue(paramInt, arrayOfChar, i, j);
      return;
    }
    if (this._currToken == JsonToken.VALUE_NUMBER_FLOAT)
    {
      _parseSlowFloatValue(paramInt);
      return;
    }
    _reportError("Current token (" + this._currToken + ") not numeric, can not use numeric value accessors");
  }

  protected void convertNumberToBigDecimal()
    throws IOException, JsonParseException
  {
    if ((0x8 & this._numTypesValid) != 0)
      this._numberBigDecimal = new BigDecimal(getText());
    while (true)
    {
      this._numTypesValid = (0x10 | this._numTypesValid);
      return;
      if ((0x4 & this._numTypesValid) != 0)
      {
        this._numberBigDecimal = new BigDecimal(this._numberBigInt);
        continue;
      }
      if ((0x2 & this._numTypesValid) != 0)
      {
        this._numberBigDecimal = BigDecimal.valueOf(this._numberLong);
        continue;
      }
      if ((0x1 & this._numTypesValid) != 0)
      {
        this._numberBigDecimal = BigDecimal.valueOf(this._numberInt);
        continue;
      }
      _throwInternal();
    }
  }

  protected void convertNumberToBigInteger()
    throws IOException, JsonParseException
  {
    if ((0x10 & this._numTypesValid) != 0)
      this._numberBigInt = this._numberBigDecimal.toBigInteger();
    while (true)
    {
      this._numTypesValid = (0x4 | this._numTypesValid);
      return;
      if ((0x2 & this._numTypesValid) != 0)
      {
        this._numberBigInt = BigInteger.valueOf(this._numberLong);
        continue;
      }
      if ((0x1 & this._numTypesValid) != 0)
      {
        this._numberBigInt = BigInteger.valueOf(this._numberInt);
        continue;
      }
      if ((0x8 & this._numTypesValid) != 0)
      {
        this._numberBigInt = BigDecimal.valueOf(this._numberDouble).toBigInteger();
        continue;
      }
      _throwInternal();
    }
  }

  protected void convertNumberToDouble()
    throws IOException, JsonParseException
  {
    if ((0x10 & this._numTypesValid) != 0)
      this._numberDouble = this._numberBigDecimal.doubleValue();
    while (true)
    {
      this._numTypesValid = (0x8 | this._numTypesValid);
      return;
      if ((0x4 & this._numTypesValid) != 0)
      {
        this._numberDouble = this._numberBigInt.doubleValue();
        continue;
      }
      if ((0x2 & this._numTypesValid) != 0)
      {
        this._numberDouble = this._numberLong;
        continue;
      }
      if ((0x1 & this._numTypesValid) != 0)
      {
        this._numberDouble = this._numberInt;
        continue;
      }
      _throwInternal();
    }
  }

  protected void convertNumberToInt()
    throws IOException, JsonParseException
  {
    if ((0x2 & this._numTypesValid) != 0)
    {
      int i = (int)this._numberLong;
      if (i != this._numberLong)
        _reportError("Numeric value (" + getText() + ") out of range of int");
      this._numberInt = i;
    }
    while (true)
    {
      this._numTypesValid = (0x1 | this._numTypesValid);
      return;
      if ((0x4 & this._numTypesValid) != 0)
      {
        this._numberInt = this._numberBigInt.intValue();
        continue;
      }
      if ((0x8 & this._numTypesValid) != 0)
      {
        if ((this._numberDouble < -2147483648.0D) || (this._numberDouble > 2147483647.0D))
          reportOverflowInt();
        this._numberInt = (int)this._numberDouble;
        continue;
      }
      if ((0x10 & this._numTypesValid) != 0)
      {
        if ((BD_MIN_INT.compareTo(this._numberBigDecimal) > 0) || (BD_MAX_INT.compareTo(this._numberBigDecimal) < 0))
          reportOverflowInt();
        this._numberInt = this._numberBigDecimal.intValue();
        continue;
      }
      _throwInternal();
    }
  }

  protected void convertNumberToLong()
    throws IOException, JsonParseException
  {
    if ((0x1 & this._numTypesValid) != 0)
      this._numberLong = this._numberInt;
    while (true)
    {
      this._numTypesValid = (0x2 | this._numTypesValid);
      return;
      if ((0x4 & this._numTypesValid) != 0)
      {
        this._numberLong = this._numberBigInt.longValue();
        continue;
      }
      if ((0x8 & this._numTypesValid) != 0)
      {
        if ((this._numberDouble < -9.223372036854776E+018D) || (this._numberDouble > 9.223372036854776E+018D))
          reportOverflowLong();
        this._numberLong = ()this._numberDouble;
        continue;
      }
      if ((0x10 & this._numTypesValid) != 0)
      {
        if ((BD_MIN_LONG.compareTo(this._numberBigDecimal) > 0) || (BD_MAX_LONG.compareTo(this._numberBigDecimal) < 0))
          reportOverflowLong();
        this._numberLong = this._numberBigDecimal.longValue();
        continue;
      }
      _throwInternal();
    }
  }

  public BigInteger getBigIntegerValue()
    throws IOException, JsonParseException
  {
    if ((0x4 & this._numTypesValid) == 0)
    {
      if (this._numTypesValid == 0)
        _parseNumericValue(4);
      if ((0x4 & this._numTypesValid) == 0)
        convertNumberToBigInteger();
    }
    return this._numberBigInt;
  }

  public BigDecimal getDecimalValue()
    throws IOException, JsonParseException
  {
    if ((0x10 & this._numTypesValid) == 0)
    {
      if (this._numTypesValid == 0)
        _parseNumericValue(16);
      if ((0x10 & this._numTypesValid) == 0)
        convertNumberToBigDecimal();
    }
    return this._numberBigDecimal;
  }

  public double getDoubleValue()
    throws IOException, JsonParseException
  {
    if ((0x8 & this._numTypesValid) == 0)
    {
      if (this._numTypesValid == 0)
        _parseNumericValue(8);
      if ((0x8 & this._numTypesValid) == 0)
        convertNumberToDouble();
    }
    return this._numberDouble;
  }

  public float getFloatValue()
    throws IOException, JsonParseException
  {
    return (float)getDoubleValue();
  }

  public int getIntValue()
    throws IOException, JsonParseException
  {
    if ((0x1 & this._numTypesValid) == 0)
    {
      if (this._numTypesValid == 0)
        _parseNumericValue(1);
      if ((0x1 & this._numTypesValid) == 0)
        convertNumberToInt();
    }
    return this._numberInt;
  }

  public long getLongValue()
    throws IOException, JsonParseException
  {
    if ((0x2 & this._numTypesValid) == 0)
    {
      if (this._numTypesValid == 0)
        _parseNumericValue(2);
      if ((0x2 & this._numTypesValid) == 0)
        convertNumberToLong();
    }
    return this._numberLong;
  }

  public JsonParser.NumberType getNumberType()
    throws IOException, JsonParseException
  {
    if (this._numTypesValid == 0)
      _parseNumericValue(0);
    if (this._currToken == JsonToken.VALUE_NUMBER_INT)
    {
      if ((0x1 & this._numTypesValid) != 0)
        return JsonParser.NumberType.INT;
      if ((0x2 & this._numTypesValid) != 0)
        return JsonParser.NumberType.LONG;
      return JsonParser.NumberType.BIG_INTEGER;
    }
    if ((0x10 & this._numTypesValid) != 0)
      return JsonParser.NumberType.BIG_DECIMAL;
    return JsonParser.NumberType.DOUBLE;
  }

  public Number getNumberValue()
    throws IOException, JsonParseException
  {
    if (this._numTypesValid == 0)
      _parseNumericValue(0);
    if (this._currToken == JsonToken.VALUE_NUMBER_INT)
    {
      if ((0x1 & this._numTypesValid) != 0)
        return Integer.valueOf(this._numberInt);
      if ((0x2 & this._numTypesValid) != 0)
        return Long.valueOf(this._numberLong);
      if ((0x4 & this._numTypesValid) != 0)
        return this._numberBigInt;
      return this._numberBigDecimal;
    }
    if ((0x10 & this._numTypesValid) != 0)
      return this._numberBigDecimal;
    if ((0x8 & this._numTypesValid) == 0)
      _throwInternal();
    return Double.valueOf(this._numberDouble);
  }

  protected void reportInvalidNumber(String paramString)
    throws JsonParseException
  {
    _reportError("Invalid numeric value: " + paramString);
  }

  protected void reportOverflowInt()
    throws IOException, JsonParseException
  {
    _reportError("Numeric value (" + getText() + ") out of range of int (" + -2147483648 + " - " + 2147483647 + ")");
  }

  protected void reportOverflowLong()
    throws IOException, JsonParseException
  {
    _reportError("Numeric value (" + getText() + ") out of range of long (" + -9223372036854775808L + " - " + 9223372036854775807L + ")");
  }

  protected void reportUnexpectedNumberChar(int paramInt, String paramString)
    throws JsonParseException
  {
    String str = "Unexpected character (" + _getCharDesc(paramInt) + ") in numeric value";
    if (paramString != null)
      str = str + ": " + paramString;
    _reportError(str);
  }

  protected final JsonToken reset(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3)
  {
    if ((paramInt2 < 1) && (paramInt3 < 1))
      return resetInt(paramBoolean, paramInt1);
    return resetFloat(paramBoolean, paramInt1, paramInt2, paramInt3);
  }

  protected final JsonToken resetAsNaN(String paramString, double paramDouble)
  {
    this._textBuffer.resetWithString(paramString);
    this._numberDouble = paramDouble;
    this._numTypesValid = 8;
    return JsonToken.VALUE_NUMBER_FLOAT;
  }

  protected final JsonToken resetFloat(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3)
  {
    this._numberNegative = paramBoolean;
    this._intLength = paramInt1;
    this._fractLength = paramInt2;
    this._expLength = paramInt3;
    this._numTypesValid = 0;
    return JsonToken.VALUE_NUMBER_FLOAT;
  }

  protected final JsonToken resetInt(boolean paramBoolean, int paramInt)
  {
    this._numberNegative = paramBoolean;
    this._intLength = paramInt;
    this._fractLength = 0;
    this._expLength = 0;
    this._numTypesValid = 0;
    return JsonToken.VALUE_NUMBER_INT;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.impl.JsonNumericParserBase
 * JD-Core Version:    0.6.0
 */