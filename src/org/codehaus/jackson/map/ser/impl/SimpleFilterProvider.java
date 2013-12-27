package org.codehaus.jackson.map.ser.impl;

import java.util.HashMap;
import java.util.Map;
import org.codehaus.jackson.map.ser.BeanPropertyFilter;
import org.codehaus.jackson.map.ser.FilterProvider;

public class SimpleFilterProvider extends FilterProvider
{
  protected BeanPropertyFilter _defaultFilter;
  protected final Map<String, BeanPropertyFilter> _filtersById = new HashMap();

  public SimpleFilterProvider()
  {
  }

  public SimpleFilterProvider(Map<String, BeanPropertyFilter> paramMap)
  {
  }

  public SimpleFilterProvider addFilter(String paramString, BeanPropertyFilter paramBeanPropertyFilter)
  {
    this._filtersById.put(paramString, paramBeanPropertyFilter);
    return this;
  }

  public BeanPropertyFilter findFilter(Object paramObject)
  {
    BeanPropertyFilter localBeanPropertyFilter = (BeanPropertyFilter)this._filtersById.get(paramObject);
    if (localBeanPropertyFilter == null)
      localBeanPropertyFilter = this._defaultFilter;
    return localBeanPropertyFilter;
  }

  public BeanPropertyFilter removeFilter(String paramString)
  {
    return (BeanPropertyFilter)this._filtersById.remove(paramString);
  }

  public SimpleFilterProvider setDefaultFilter(BeanPropertyFilter paramBeanPropertyFilter)
  {
    this._defaultFilter = paramBeanPropertyFilter;
    return this;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.impl.SimpleFilterProvider
 * JD-Core Version:    0.6.0
 */