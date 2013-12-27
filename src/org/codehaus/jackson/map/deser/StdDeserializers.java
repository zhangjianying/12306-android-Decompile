package org.codehaus.jackson.map.deser;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

class StdDeserializers
{
  final HashMap<JavaType, JsonDeserializer<Object>> _deserializers = new HashMap();

  private StdDeserializers()
  {
    add(new UntypedObjectDeserializer());
    StdDeserializer.StringDeserializer localStringDeserializer = new StdDeserializer.StringDeserializer();
    add(localStringDeserializer, String.class);
    add(localStringDeserializer, CharSequence.class);
    add(new StdDeserializer.ClassDeserializer());
    add(new StdDeserializer.BooleanDeserializer(Boolean.class, null));
    add(new StdDeserializer.ByteDeserializer(Byte.class, null));
    add(new StdDeserializer.ShortDeserializer(Short.class, null));
    add(new StdDeserializer.CharacterDeserializer(Character.class, null));
    add(new StdDeserializer.IntegerDeserializer(Integer.class, null));
    add(new StdDeserializer.LongDeserializer(Long.class, null));
    add(new StdDeserializer.FloatDeserializer(Float.class, null));
    add(new StdDeserializer.DoubleDeserializer(Double.class, null));
    add(new StdDeserializer.BooleanDeserializer(Boolean.TYPE, Boolean.FALSE));
    add(new StdDeserializer.ByteDeserializer(Byte.TYPE, Byte.valueOf(0)));
    add(new StdDeserializer.ShortDeserializer(Short.TYPE, Short.valueOf(0)));
    add(new StdDeserializer.CharacterDeserializer(Character.TYPE, Character.valueOf('\000')));
    add(new StdDeserializer.IntegerDeserializer(Integer.TYPE, Integer.valueOf(0)));
    add(new StdDeserializer.LongDeserializer(Long.TYPE, Long.valueOf(0L)));
    add(new StdDeserializer.FloatDeserializer(Float.TYPE, Float.valueOf(0.0F)));
    add(new StdDeserializer.DoubleDeserializer(Double.TYPE, Double.valueOf(0.0D)));
    add(new StdDeserializer.NumberDeserializer());
    add(new StdDeserializer.BigDecimalDeserializer());
    add(new StdDeserializer.BigIntegerDeserializer());
    add(new DateDeserializer());
    add(new StdDeserializer.SqlDateDeserializer());
    add(new TimestampDeserializer());
    add(new StdDeserializer.CalendarDeserializer());
    add(new StdDeserializer.CalendarDeserializer(GregorianCalendar.class), GregorianCalendar.class);
    Iterator localIterator = FromStringDeserializer.all().iterator();
    while (localIterator.hasNext())
      add((FromStringDeserializer)localIterator.next());
    add(new StdDeserializer.StackTraceElementDeserializer());
    add(new StdDeserializer.TokenBufferDeserializer());
    add(new StdDeserializer.AtomicBooleanDeserializer());
  }

  private void add(StdDeserializer<?> paramStdDeserializer)
  {
    add(paramStdDeserializer, paramStdDeserializer.getValueClass());
  }

  private void add(StdDeserializer<?> paramStdDeserializer, Class<?> paramClass)
  {
    this._deserializers.put(TypeFactory.defaultInstance().constructType(paramClass), paramStdDeserializer);
  }

  public static HashMap<JavaType, JsonDeserializer<Object>> constructAll()
  {
    return new StdDeserializers()._deserializers;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.StdDeserializers
 * JD-Core Version:    0.6.0
 */