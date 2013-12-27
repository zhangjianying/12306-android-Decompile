package org.codehaus.jackson.format;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import org.codehaus.jackson.JsonFactory;

public class DataFormatDetector
{
  public static final int DEFAULT_MAX_INPUT_LOOKAHEAD = 64;
  protected final JsonFactory[] _detectors;
  protected final int _maxInputLookahead;
  protected final MatchStrength _minimalMatch;
  protected final MatchStrength _optimalMatch;

  public DataFormatDetector(Collection<JsonFactory> paramCollection)
  {
    this((JsonFactory[])paramCollection.toArray(new JsonFactory[paramCollection.size()]));
  }

  public DataFormatDetector(JsonFactory[] paramArrayOfJsonFactory)
  {
    this(paramArrayOfJsonFactory, MatchStrength.SOLID_MATCH, MatchStrength.WEAK_MATCH, 64);
  }

  private DataFormatDetector(JsonFactory[] paramArrayOfJsonFactory, MatchStrength paramMatchStrength1, MatchStrength paramMatchStrength2, int paramInt)
  {
    this._detectors = paramArrayOfJsonFactory;
    this._optimalMatch = paramMatchStrength1;
    this._minimalMatch = paramMatchStrength2;
    this._maxInputLookahead = paramInt;
  }

  private DataFormatMatcher _findFormat(InputAccessor.Std paramStd)
    throws IOException
  {
    Object localObject1 = null;
    Object localObject2 = null;
    JsonFactory[] arrayOfJsonFactory = this._detectors;
    int i = arrayOfJsonFactory.length;
    int j = 0;
    if (j < i)
    {
      JsonFactory localJsonFactory = arrayOfJsonFactory[j];
      paramStd.reset();
      MatchStrength localMatchStrength = localJsonFactory.hasFormat(paramStd);
      if ((localMatchStrength == null) || (localMatchStrength.ordinal() < this._minimalMatch.ordinal()));
      do
      {
        do
        {
          j++;
          break;
        }
        while ((localObject1 != null) && (localObject2.ordinal() >= localMatchStrength.ordinal()));
        localObject1 = localJsonFactory;
        localObject2 = localMatchStrength;
      }
      while (localMatchStrength.ordinal() < this._optimalMatch.ordinal());
    }
    return paramStd.createMatcher(localObject1, localObject2);
  }

  public DataFormatMatcher findFormat(InputStream paramInputStream)
    throws IOException
  {
    return _findFormat(new InputAccessor.Std(paramInputStream, new byte[this._maxInputLookahead]));
  }

  public DataFormatMatcher findFormat(byte[] paramArrayOfByte)
    throws IOException
  {
    return _findFormat(new InputAccessor.Std(paramArrayOfByte));
  }

  public DataFormatDetector withMaxInputLookahead(int paramInt)
  {
    if (paramInt == this._maxInputLookahead)
      return this;
    return new DataFormatDetector(this._detectors, this._optimalMatch, this._minimalMatch, paramInt);
  }

  public DataFormatDetector withMinimalMatch(MatchStrength paramMatchStrength)
  {
    if (paramMatchStrength == this._minimalMatch)
      return this;
    return new DataFormatDetector(this._detectors, this._optimalMatch, paramMatchStrength, this._maxInputLookahead);
  }

  public DataFormatDetector withOptimalMatch(MatchStrength paramMatchStrength)
  {
    if (paramMatchStrength == this._optimalMatch)
      return this;
    return new DataFormatDetector(this._detectors, paramMatchStrength, this._minimalMatch, this._maxInputLookahead);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.format.DataFormatDetector
 * JD-Core Version:    0.6.0
 */