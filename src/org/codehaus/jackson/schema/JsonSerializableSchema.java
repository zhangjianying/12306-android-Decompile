package org.codehaus.jackson.schema;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.codehaus.jackson.annotate.JacksonAnnotation;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
@JacksonAnnotation
public @interface JsonSerializableSchema
{
  public abstract String schemaItemDefinition();

  public abstract String schemaObjectPropertiesDefinition();

  public abstract String schemaType();
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.schema.JsonSerializableSchema
 * JD-Core Version:    0.6.0
 */