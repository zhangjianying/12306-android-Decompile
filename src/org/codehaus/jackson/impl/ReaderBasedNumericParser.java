package org.codehaus.jackson.impl;

import java.io.IOException;
import java.io.Reader;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.util.TextBuffer;

public abstract class ReaderBasedNumericParser extends ReaderBasedParserBase
{
  public ReaderBasedNumericParser(IOContext paramIOContext, int paramInt, Reader paramReader)
  {
    super(paramIOContext, paramInt, paramReader);
  }

  private final char _verifyNoLeadingZeroes()
    throws IOException, JsonParseException
  {
    if ((this._inputPtr >= this._inputEnd) && (!loadMore()));
    int i;
    do
    {
      i = 48;
      while ((this._inputPtr >= this._inputEnd) && (!loadMore()))
        do
        {
          return i;
          i = this._inputBuffer[this._inputPtr];
          if ((i < 48) || (i > 57))
            return '0';
          if (!isEnabled(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS))
            reportInvalidNumber("Leading zeroes not allowed");
          this._inputPtr = (1 + this._inputPtr);
        }
        while (i != 48);
      i = this._inputBuffer[this._inputPtr];
      if ((i < 48) || (i > 57))
        return '0';
      this._inputPtr = (1 + this._inputPtr);
    }
    while (i == 48);
    return i;
  }

  private final JsonToken parseNumberText2(boolean paramBoolean)
    throws IOException, JsonParseException
  {
    char[] arrayOfChar1 = this._textBuffer.emptyAndGetCurrentSegment();
    int i = 0;
    if (paramBoolean)
    {
      int i16 = 0 + 1;
      arrayOfChar1[0] = '-';
      i = i16;
    }
    int j = 0;
    int k;
    label81: int m;
    if (this._inputPtr < this._inputEnd)
    {
      char[] arrayOfChar7 = this._inputBuffer;
      int i15 = this._inputPtr;
      this._inputPtr = (i15 + 1);
      k = arrayOfChar7[i15];
      if (k == 48)
        k = _verifyNoLeadingZeroes();
      if ((k < 48) || (k > 57))
        break label744;
      j++;
      if (i >= arrayOfChar1.length)
      {
        arrayOfChar1 = this._textBuffer.finishCurrentSegment();
        i = 0;
      }
      m = i + 1;
      arrayOfChar1[i] = k;
      if ((this._inputPtr < this._inputEnd) || (loadMore()))
        break label547;
      k = 0;
    }
    for (int n = 1; ; n = 0)
    {
      if (j == 0)
        reportInvalidNumber("Missing integer part (next char " + _getCharDesc(k) + ")");
      int i1 = 0;
      int i2;
      if (k == 46)
      {
        i2 = m + 1;
        arrayOfChar1[m] = k;
        label207: if ((this._inputPtr >= this._inputEnd) && (!loadMore()))
        {
          n = 1;
          label228: if (i1 == 0)
            reportUnexpectedNumberChar(k, "Decimal point not followed by a digit");
        }
      }
      while (true)
      {
        int i3 = 0;
        int i4;
        int i5;
        label329: int i6;
        if (k != 101)
        {
          i3 = 0;
          if (k != 69);
        }
        else
        {
          if (i2 >= arrayOfChar1.length)
          {
            arrayOfChar1 = this._textBuffer.finishCurrentSegment();
            i2 = 0;
          }
          i4 = i2 + 1;
          arrayOfChar1[i2] = k;
          if (this._inputPtr >= this._inputEnd)
            break label661;
          char[] arrayOfChar4 = this._inputBuffer;
          int i11 = this._inputPtr;
          this._inputPtr = (i11 + 1);
          i5 = arrayOfChar4[i11];
          if ((i5 != 45) && (i5 != 43))
            break label724;
          if (i4 < arrayOfChar1.length)
            break label717;
          arrayOfChar1 = this._textBuffer.finishCurrentSegment();
          i6 = 0;
          label361: int i7 = i6 + 1;
          arrayOfChar1[i6] = i5;
          if (this._inputPtr >= this._inputEnd)
            break label672;
          char[] arrayOfChar3 = this._inputBuffer;
          int i10 = this._inputPtr;
          this._inputPtr = (i10 + 1);
          i5 = arrayOfChar3[i10];
          label411: i2 = i7;
        }
        while (true)
        {
          int i8;
          if ((i5 <= 57) && (i5 >= 48))
          {
            i3++;
            if (i2 >= arrayOfChar1.length)
            {
              arrayOfChar1 = this._textBuffer.finishCurrentSegment();
              i2 = 0;
            }
            i8 = i2 + 1;
            arrayOfChar1[i2] = i5;
            if ((this._inputPtr >= this._inputEnd) && (!loadMore()))
            {
              n = 1;
              i2 = i8;
            }
          }
          else
          {
            if (i3 == 0)
              reportUnexpectedNumberChar(i5, "Exponent indicator not followed by a digit");
            if (n == 0)
              this._inputPtr = (-1 + this._inputPtr);
            this._textBuffer.setCurrentLength(i2);
            return reset(paramBoolean, j, i1, i3);
            k = getNextChar("No digit following minus sign");
            break;
            label547: char[] arrayOfChar6 = this._inputBuffer;
            int i14 = this._inputPtr;
            this._inputPtr = (i14 + 1);
            k = arrayOfChar6[i14];
            i = m;
            break label81;
            char[] arrayOfChar5 = this._inputBuffer;
            int i12 = this._inputPtr;
            this._inputPtr = (i12 + 1);
            k = arrayOfChar5[i12];
            if ((k < 48) || (k > 57))
              break label228;
            i1++;
            if (i2 >= arrayOfChar1.length)
            {
              arrayOfChar1 = this._textBuffer.finishCurrentSegment();
              i2 = 0;
            }
            int i13 = i2 + 1;
            arrayOfChar1[i2] = k;
            i2 = i13;
            break label207;
            label661: i5 = getNextChar("expected a digit for number exponent");
            break label329;
            label672: i5 = getNextChar("expected a digit for number exponent");
            break label411;
          }
          char[] arrayOfChar2 = this._inputBuffer;
          int i9 = this._inputPtr;
          this._inputPtr = (i9 + 1);
          i5 = arrayOfChar2[i9];
          i2 = i8;
          continue;
          label717: i6 = i4;
          break label361;
          label724: i2 = i4;
          i3 = 0;
        }
        i2 = m;
        i1 = 0;
      }
      label744: m = i;
    }
  }

  protected JsonToken _handleInvalidNumberStart(int paramInt, boolean paramBoolean)
    throws IOException, JsonParseException
  {
    double d = (-1.0D / 0.0D);
    if (paramInt == 73)
    {
      if ((this._inputPtr >= this._inputEnd) && (!loadMore()))
        _reportInvalidEOFInValue();
      char[] arrayOfChar = this._inputBuffer;
      int i = this._inputPtr;
      this._inputPtr = (i + 1);
      paramInt = arrayOfChar[i];
      if (paramInt != 78)
        break label156;
      String str2;
      if (paramBoolean)
      {
        str2 = "-INF";
        if (!_matchToken(str2, 3))
          break label147;
        if (!isEnabled(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS))
          break label118;
        if (!paramBoolean)
          break label111;
      }
      while (true)
      {
        return resetAsNaN(str2, d);
        str2 = "+INF";
        break;
        label111: d = (1.0D / 0.0D);
      }
      label118: _reportError("Non-standard token '" + str2 + "': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
    }
    while (true)
    {
      label147: reportUnexpectedNumberChar(paramInt, "expected digit (0-9) to follow minus sign, for valid numeric value");
      return null;
      label156: if (paramInt != 110)
        continue;
      String str1;
      if (paramBoolean)
      {
        str1 = "-Infinity";
        label170: if (!_matchToken(str1, 3))
          break label207;
        if (!isEnabled(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS))
          break label216;
        if (!paramBoolean)
          break label209;
      }
      while (true)
      {
        return resetAsNaN(str1, d);
        str1 = "+Infinity";
        break label170;
        label207: break;
        label209: d = (1.0D / 0.0D);
      }
      label216: _reportError("Non-standard token '" + str1 + "': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
    }
  }

  protected final JsonToken parseNumberText(int paramInt)
    throws IOException, JsonParseException
  {
    boolean bool;
    int i;
    int j;
    int k;
    if (paramInt == 45)
    {
      bool = true;
      i = this._inputPtr;
      j = i - 1;
      k = this._inputEnd;
      if (!bool)
        break label104;
      if (i < this._inputEnd)
        break label60;
    }
    label60: label104: int m;
    label113: int i2;
    label234: int i3;
    label277: int i6;
    do
    {
      do
      {
        if (bool)
          j++;
        this._inputPtr = j;
        return parseNumberText2(bool);
        bool = false;
        break;
        char[] arrayOfChar6 = this._inputBuffer;
        int i10 = i + 1;
        paramInt = arrayOfChar6[i];
        if ((paramInt > 57) || (paramInt < 48))
        {
          this._inputPtr = i10;
          return _handleInvalidNumberStart(paramInt, true);
        }
        i = i10;
      }
      while (paramInt == 48);
      m = 1;
      int i1;
      if (i < this._inputEnd)
      {
        char[] arrayOfChar1 = this._inputBuffer;
        n = i + 1;
        i1 = arrayOfChar1[i];
        if ((i1 < 48) || (i1 > 57))
        {
          i2 = 0;
          if (i1 != 46)
            break label234;
        }
      }
      while (true)
      {
        if (n >= k)
        {
          break;
          m++;
          i = n;
          break label113;
          break;
        }
        char[] arrayOfChar5 = this._inputBuffer;
        int i9 = n + 1;
        i1 = arrayOfChar5[n];
        if ((i1 < 48) || (i1 > 57))
        {
          if (i2 == 0)
            reportUnexpectedNumberChar(i1, "Decimal point not followed by a digit");
          n = i9;
          i3 = 0;
          if (i1 != 101)
          {
            i3 = 0;
            if (i1 != 69)
              break label405;
          }
          if (n < k)
            break label277;
          break;
        }
        i2++;
        n = i9;
      }
      char[] arrayOfChar2 = this._inputBuffer;
      i6 = n + 1;
      i7 = arrayOfChar2[n];
      if ((i7 != 45) && (i7 != 43))
        break label451;
    }
    while (i6 >= k);
    char[] arrayOfChar3 = this._inputBuffer;
    int n = i6 + 1;
    int i7 = arrayOfChar3[i6];
    while (true)
    {
      if ((i7 <= 57) && (i7 >= 48))
      {
        i3++;
        if (n >= k)
          break;
        char[] arrayOfChar4 = this._inputBuffer;
        int i8 = n + 1;
        i7 = arrayOfChar4[n];
        n = i8;
        continue;
      }
      if (i3 == 0)
        reportUnexpectedNumberChar(i7, "Exponent indicator not followed by a digit");
      label405: int i4 = -1 + n;
      this._inputPtr = i4;
      int i5 = i4 - j;
      this._textBuffer.resetWithShared(this._inputBuffer, j, i5);
      return reset(bool, m, i2, i3);
      label451: n = i6;
      i3 = 0;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.impl.ReaderBasedNumericParser
 * JD-Core Version:    0.6.0
 */