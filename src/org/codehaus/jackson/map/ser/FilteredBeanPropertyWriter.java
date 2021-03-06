package org.codehaus.jackson.map.ser;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public abstract class FilteredBeanPropertyWriter
{
  public static BeanPropertyWriter constructViewBased(BeanPropertyWriter paramBeanPropertyWriter, Class<?>[] paramArrayOfClass)
  {
    if (paramArrayOfClass.length == 1)
      return new SingleView(paramBeanPropertyWriter, paramArrayOfClass[0]);
    return new MultiView(paramBeanPropertyWriter, paramArrayOfClass);
  }

  private static final class MultiView extends BeanPropertyWriter
  {
    protected final Class<?>[] _views;

    protected MultiView(BeanPropertyWriter paramBeanPropertyWriter, Class<?>[] paramArrayOfClass)
    {
      super();
      this._views = paramArrayOfClass;
    }

    protected MultiView(MultiView paramMultiView, JsonSerializer<Object> paramJsonSerializer)
    {
      super(paramJsonSerializer);
      this._views = paramMultiView._views;
    }

    public void serializeAsField(Object paramObject, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws Exception
    {
      Class localClass = paramSerializerProvider.getSerializationView();
      if (localClass != null)
      {
        int i = 0;
        int j = this._views.length;
        while (true)
        {
          if ((i >= j) || (this._views[i].isAssignableFrom(localClass)))
          {
            if (i != j)
              break;
            return;
          }
          i++;
        }
      }
      super.serializeAsField(paramObject, paramJsonGenerator, paramSerializerProvider);
    }

    public BeanPropertyWriter withSerializer(JsonSerializer<Object> paramJsonSerializer)
    {
      return new MultiView(this, paramJsonSerializer);
    }
  }

  private static final class SingleView extends BeanPropertyWriter
  {
    protected final Class<?> _view;

    protected SingleView(BeanPropertyWriter paramBeanPropertyWriter, Class<?> paramClass)
    {
      super();
      this._view = paramClass;
    }

    protected SingleView(SingleView paramSingleView, JsonSerializer<Object> paramJsonSerializer)
    {
      super(paramJsonSerializer);
      this._view = paramSingleView._view;
    }

    public void serializeAsField(Object paramObject, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws Exception
    {
      Class localClass = paramSerializerProvider.getSerializationView();
      if ((localClass == null) || (this._view.isAssignableFrom(localClass)))
        super.serializeAsField(paramObject, paramJsonGenerator, paramSerializerProvider);
    }

    public BeanPropertyWriter withSerializer(JsonSerializer<Object> paramJsonSerializer)
    {
      return new SingleView(this, paramJsonSerializer);
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.FilteredBeanPropertyWriter
 * JD-Core Version:    0.6.0
 */