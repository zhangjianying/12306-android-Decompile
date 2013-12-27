package org.codehaus.jackson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.lang.ref.SoftReference;
import java.net.URL;
import org.codehaus.jackson.format.InputAccessor;
import org.codehaus.jackson.format.MatchStrength;
import org.codehaus.jackson.impl.ByteSourceBootstrapper;
import org.codehaus.jackson.impl.ReaderBasedParser;
import org.codehaus.jackson.impl.Utf8Generator;
import org.codehaus.jackson.impl.WriterBasedGenerator;
import org.codehaus.jackson.io.CharacterEscapes;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.io.InputDecorator;
import org.codehaus.jackson.io.OutputDecorator;
import org.codehaus.jackson.io.UTF8Writer;
import org.codehaus.jackson.sym.BytesToNameCanonicalizer;
import org.codehaus.jackson.sym.CharsToNameCanonicalizer;
import org.codehaus.jackson.util.BufferRecycler;
import org.codehaus.jackson.util.VersionUtil;

public class JsonFactory
  implements Versioned
{
  static final int DEFAULT_GENERATOR_FEATURE_FLAGS = 0;
  static final int DEFAULT_PARSER_FEATURE_FLAGS = 0;
  public static final String FORMAT_NAME_JSON = "JSON";
  protected static final ThreadLocal<SoftReference<BufferRecycler>> _recyclerRef;
  protected CharacterEscapes _characterEscapes;
  protected int _generatorFeatures = DEFAULT_GENERATOR_FEATURE_FLAGS;
  protected InputDecorator _inputDecorator;
  protected ObjectCodec _objectCodec;
  protected OutputDecorator _outputDecorator;
  protected int _parserFeatures = DEFAULT_PARSER_FEATURE_FLAGS;
  protected BytesToNameCanonicalizer _rootByteSymbols = BytesToNameCanonicalizer.createRoot();
  protected CharsToNameCanonicalizer _rootCharSymbols = CharsToNameCanonicalizer.createRoot();

  static
  {
    DEFAULT_GENERATOR_FEATURE_FLAGS = JsonGenerator.Feature.collectDefaults();
    _recyclerRef = new ThreadLocal();
  }

  public JsonFactory()
  {
    this(null);
  }

  public JsonFactory(ObjectCodec paramObjectCodec)
  {
    this._objectCodec = paramObjectCodec;
  }

  protected IOContext _createContext(Object paramObject, boolean paramBoolean)
  {
    return new IOContext(_getBufferRecycler(), paramObject, paramBoolean);
  }

  protected JsonGenerator _createJsonGenerator(Writer paramWriter, IOContext paramIOContext)
    throws IOException
  {
    WriterBasedGenerator localWriterBasedGenerator = new WriterBasedGenerator(paramIOContext, this._generatorFeatures, this._objectCodec, paramWriter);
    if (this._characterEscapes != null)
      localWriterBasedGenerator.setCharacterEscapes(this._characterEscapes);
    return localWriterBasedGenerator;
  }

  protected JsonParser _createJsonParser(InputStream paramInputStream, IOContext paramIOContext)
    throws IOException, JsonParseException
  {
    return new ByteSourceBootstrapper(paramIOContext, paramInputStream).constructParser(this._parserFeatures, this._objectCodec, this._rootByteSymbols, this._rootCharSymbols);
  }

  protected JsonParser _createJsonParser(Reader paramReader, IOContext paramIOContext)
    throws IOException, JsonParseException
  {
    return new ReaderBasedParser(paramIOContext, this._parserFeatures, paramReader, this._objectCodec, this._rootCharSymbols.makeChild(isEnabled(JsonParser.Feature.CANONICALIZE_FIELD_NAMES), isEnabled(JsonParser.Feature.INTERN_FIELD_NAMES)));
  }

  protected JsonParser _createJsonParser(byte[] paramArrayOfByte, int paramInt1, int paramInt2, IOContext paramIOContext)
    throws IOException, JsonParseException
  {
    return new ByteSourceBootstrapper(paramIOContext, paramArrayOfByte, paramInt1, paramInt2).constructParser(this._parserFeatures, this._objectCodec, this._rootByteSymbols, this._rootCharSymbols);
  }

  protected JsonGenerator _createUTF8JsonGenerator(OutputStream paramOutputStream, IOContext paramIOContext)
    throws IOException
  {
    Utf8Generator localUtf8Generator = new Utf8Generator(paramIOContext, this._generatorFeatures, this._objectCodec, paramOutputStream);
    if (this._characterEscapes != null)
      localUtf8Generator.setCharacterEscapes(this._characterEscapes);
    return localUtf8Generator;
  }

  protected Writer _createWriter(OutputStream paramOutputStream, JsonEncoding paramJsonEncoding, IOContext paramIOContext)
    throws IOException
  {
    if (paramJsonEncoding == JsonEncoding.UTF8)
      return new UTF8Writer(paramIOContext, paramOutputStream);
    return new OutputStreamWriter(paramOutputStream, paramJsonEncoding.getJavaName());
  }

  public BufferRecycler _getBufferRecycler()
  {
    SoftReference localSoftReference = (SoftReference)_recyclerRef.get();
    if (localSoftReference == null);
    for (BufferRecycler localBufferRecycler = null; ; localBufferRecycler = (BufferRecycler)localSoftReference.get())
    {
      if (localBufferRecycler == null)
      {
        localBufferRecycler = new BufferRecycler();
        _recyclerRef.set(new SoftReference(localBufferRecycler));
      }
      return localBufferRecycler;
    }
  }

  protected InputStream _optimizedStreamFromURL(URL paramURL)
    throws IOException
  {
    if ("file".equals(paramURL.getProtocol()))
    {
      String str = paramURL.getHost();
      if ((str == null) || (str.length() == 0))
        return new FileInputStream(paramURL.getPath());
    }
    return paramURL.openStream();
  }

  public final JsonFactory configure(JsonGenerator.Feature paramFeature, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      enable(paramFeature);
      return this;
    }
    disable(paramFeature);
    return this;
  }

  public final JsonFactory configure(JsonParser.Feature paramFeature, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      enable(paramFeature);
      return this;
    }
    disable(paramFeature);
    return this;
  }

  public JsonGenerator createJsonGenerator(File paramFile, JsonEncoding paramJsonEncoding)
    throws IOException
  {
    Object localObject = new FileOutputStream(paramFile);
    IOContext localIOContext = _createContext(localObject, true);
    localIOContext.setEncoding(paramJsonEncoding);
    if (paramJsonEncoding == JsonEncoding.UTF8)
    {
      if (this._outputDecorator != null)
        localObject = this._outputDecorator.decorate(localIOContext, (OutputStream)localObject);
      return _createUTF8JsonGenerator((OutputStream)localObject, localIOContext);
    }
    Writer localWriter = _createWriter((OutputStream)localObject, paramJsonEncoding, localIOContext);
    if (this._outputDecorator != null)
      localWriter = this._outputDecorator.decorate(localIOContext, localWriter);
    return (JsonGenerator)_createJsonGenerator(localWriter, localIOContext);
  }

  public JsonGenerator createJsonGenerator(OutputStream paramOutputStream)
    throws IOException
  {
    return createJsonGenerator(paramOutputStream, JsonEncoding.UTF8);
  }

  public JsonGenerator createJsonGenerator(OutputStream paramOutputStream, JsonEncoding paramJsonEncoding)
    throws IOException
  {
    IOContext localIOContext = _createContext(paramOutputStream, false);
    localIOContext.setEncoding(paramJsonEncoding);
    if (paramJsonEncoding == JsonEncoding.UTF8)
    {
      if (this._outputDecorator != null)
        paramOutputStream = this._outputDecorator.decorate(localIOContext, paramOutputStream);
      return _createUTF8JsonGenerator(paramOutputStream, localIOContext);
    }
    Writer localWriter = _createWriter(paramOutputStream, paramJsonEncoding, localIOContext);
    if (this._outputDecorator != null)
      localWriter = this._outputDecorator.decorate(localIOContext, localWriter);
    return _createJsonGenerator(localWriter, localIOContext);
  }

  public JsonGenerator createJsonGenerator(Writer paramWriter)
    throws IOException
  {
    IOContext localIOContext = _createContext(paramWriter, false);
    if (this._outputDecorator != null)
      paramWriter = this._outputDecorator.decorate(localIOContext, paramWriter);
    return _createJsonGenerator(paramWriter, localIOContext);
  }

  public JsonParser createJsonParser(File paramFile)
    throws IOException, JsonParseException
  {
    IOContext localIOContext = _createContext(paramFile, true);
    Object localObject = new FileInputStream(paramFile);
    if (this._inputDecorator != null)
      localObject = this._inputDecorator.decorate(localIOContext, (InputStream)localObject);
    return (JsonParser)_createJsonParser((InputStream)localObject, localIOContext);
  }

  public JsonParser createJsonParser(InputStream paramInputStream)
    throws IOException, JsonParseException
  {
    IOContext localIOContext = _createContext(paramInputStream, false);
    if (this._inputDecorator != null)
      paramInputStream = this._inputDecorator.decorate(localIOContext, paramInputStream);
    return _createJsonParser(paramInputStream, localIOContext);
  }

  public JsonParser createJsonParser(Reader paramReader)
    throws IOException, JsonParseException
  {
    IOContext localIOContext = _createContext(paramReader, false);
    if (this._inputDecorator != null)
      paramReader = this._inputDecorator.decorate(localIOContext, paramReader);
    return _createJsonParser(paramReader, localIOContext);
  }

  public JsonParser createJsonParser(String paramString)
    throws IOException, JsonParseException
  {
    Object localObject = new StringReader(paramString);
    IOContext localIOContext = _createContext(localObject, true);
    if (this._inputDecorator != null)
      localObject = this._inputDecorator.decorate(localIOContext, (Reader)localObject);
    return (JsonParser)_createJsonParser((Reader)localObject, localIOContext);
  }

  public JsonParser createJsonParser(URL paramURL)
    throws IOException, JsonParseException
  {
    IOContext localIOContext = _createContext(paramURL, true);
    InputStream localInputStream = _optimizedStreamFromURL(paramURL);
    if (this._inputDecorator != null)
      localInputStream = this._inputDecorator.decorate(localIOContext, localInputStream);
    return _createJsonParser(localInputStream, localIOContext);
  }

  public JsonParser createJsonParser(byte[] paramArrayOfByte)
    throws IOException, JsonParseException
  {
    IOContext localIOContext = _createContext(paramArrayOfByte, true);
    if (this._inputDecorator != null)
    {
      InputStream localInputStream = this._inputDecorator.decorate(localIOContext, paramArrayOfByte, 0, paramArrayOfByte.length);
      if (localInputStream != null)
        return _createJsonParser(localInputStream, localIOContext);
    }
    return _createJsonParser(paramArrayOfByte, 0, paramArrayOfByte.length, localIOContext);
  }

  public JsonParser createJsonParser(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException, JsonParseException
  {
    IOContext localIOContext = _createContext(paramArrayOfByte, true);
    if (this._inputDecorator != null)
    {
      InputStream localInputStream = this._inputDecorator.decorate(localIOContext, paramArrayOfByte, paramInt1, paramInt2);
      if (localInputStream != null)
        return _createJsonParser(localInputStream, localIOContext);
    }
    return _createJsonParser(paramArrayOfByte, paramInt1, paramInt2, localIOContext);
  }

  public JsonFactory disable(JsonGenerator.Feature paramFeature)
  {
    this._generatorFeatures &= (0xFFFFFFFF ^ paramFeature.getMask());
    return this;
  }

  public JsonFactory disable(JsonParser.Feature paramFeature)
  {
    this._parserFeatures &= (0xFFFFFFFF ^ paramFeature.getMask());
    return this;
  }

  @Deprecated
  public final void disableGeneratorFeature(JsonGenerator.Feature paramFeature)
  {
    disable(paramFeature);
  }

  public final void disableParserFeature(JsonParser.Feature paramFeature)
  {
    disable(paramFeature);
  }

  public JsonFactory enable(JsonGenerator.Feature paramFeature)
  {
    this._generatorFeatures |= paramFeature.getMask();
    return this;
  }

  public JsonFactory enable(JsonParser.Feature paramFeature)
  {
    this._parserFeatures |= paramFeature.getMask();
    return this;
  }

  @Deprecated
  public final void enableGeneratorFeature(JsonGenerator.Feature paramFeature)
  {
    enable(paramFeature);
  }

  public final void enableParserFeature(JsonParser.Feature paramFeature)
  {
    enable(paramFeature);
  }

  public CharacterEscapes getCharacterEscapes()
  {
    return this._characterEscapes;
  }

  public ObjectCodec getCodec()
  {
    return this._objectCodec;
  }

  public String getFormatName()
  {
    if (getClass() == JsonFactory.class)
      return "JSON";
    return null;
  }

  public InputDecorator getInputDecorator()
  {
    return this._inputDecorator;
  }

  public OutputDecorator getOutputDecorator()
  {
    return this._outputDecorator;
  }

  public MatchStrength hasFormat(InputAccessor paramInputAccessor)
    throws IOException
  {
    if (getClass() == JsonFactory.class)
      return hasJSONFormat(paramInputAccessor);
    return null;
  }

  protected MatchStrength hasJSONFormat(InputAccessor paramInputAccessor)
    throws IOException
  {
    return ByteSourceBootstrapper.hasJSONFormat(paramInputAccessor);
  }

  public final boolean isEnabled(JsonGenerator.Feature paramFeature)
  {
    return (this._generatorFeatures & paramFeature.getMask()) != 0;
  }

  public final boolean isEnabled(JsonParser.Feature paramFeature)
  {
    return (this._parserFeatures & paramFeature.getMask()) != 0;
  }

  @Deprecated
  public final boolean isGeneratorFeatureEnabled(JsonGenerator.Feature paramFeature)
  {
    return isEnabled(paramFeature);
  }

  public final boolean isParserFeatureEnabled(JsonParser.Feature paramFeature)
  {
    return (this._parserFeatures & paramFeature.getMask()) != 0;
  }

  public JsonFactory setCharacterEscapes(CharacterEscapes paramCharacterEscapes)
  {
    this._characterEscapes = paramCharacterEscapes;
    return this;
  }

  public JsonFactory setCodec(ObjectCodec paramObjectCodec)
  {
    this._objectCodec = paramObjectCodec;
    return this;
  }

  @Deprecated
  public final void setGeneratorFeature(JsonGenerator.Feature paramFeature, boolean paramBoolean)
  {
    configure(paramFeature, paramBoolean);
  }

  public JsonFactory setInputDecorator(InputDecorator paramInputDecorator)
  {
    this._inputDecorator = paramInputDecorator;
    return this;
  }

  public JsonFactory setOutputDecorator(OutputDecorator paramOutputDecorator)
  {
    this._outputDecorator = paramOutputDecorator;
    return this;
  }

  public final void setParserFeature(JsonParser.Feature paramFeature, boolean paramBoolean)
  {
    configure(paramFeature, paramBoolean);
  }

  public Version version()
  {
    return VersionUtil.versionFor(Utf8Generator.class);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.JsonFactory
 * JD-Core Version:    0.6.0
 */