package com.tl.uic;

import android.app.Activity;
import android.app.Application;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.view.View;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AbsSpinner;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.DialerFilter;
import android.widget.ExpandableListView;
import android.widget.Gallery;
import android.widget.HorizontalScrollView;
import android.widget.ImageSwitcher;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;
import com.tl.uic.model.Connection;
import com.tl.uic.model.Control;
import com.tl.uic.model.CustomEvent;
import com.tl.uic.model.EventInfo;
import com.tl.uic.model.JSONException;
import com.tl.uic.model.Position;
import com.tl.uic.model.Screenview;
import com.tl.uic.model.ScreenviewType;
import com.tl.uic.model.Target;
import com.tl.uic.util.LogInternal;
import com.tl.uic.util.ValueUtil;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;

public class Logger
{
  private static final String textKeyName = "text";
  private Application _application;
  private Activity _currentActivity;
  private final ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Control>> _formActivities;
  private Date _previousFocusControlDate;
  private Target _previousFocusTarget;
  private Screenview currentScreenview;
  private Date loadDate;

  public Logger(Application paramApplication)
  {
    this._application = paramApplication;
    this._formActivities = new ConcurrentHashMap();
  }

  private Control createControl(View paramView, String paramString, int paramInt)
  {
    Control localControl = new Control();
    localControl.setLogLevel(paramInt);
    Target localTarget = createTarget(paramView);
    Boolean localBoolean = getState(localTarget, paramView, paramString);
    localControl.setTarget(localTarget);
    if (paramString != null)
    {
      EventInfo localEventInfo = new EventInfo();
      localEventInfo.setType(paramString);
      localEventInfo.setTlEvent(getTlEvent(localControl.getTarget().getTlType(), localEventInfo.getType()));
      localControl.setEventInfo(localEventInfo);
      if ((paramString.equals("OnFocusChange_Out")) && (localControl.getTarget() != null) && (localControl.getTarget().getDwell() > 0L))
        localControl.setFocusInOffset(localControl.getOffset() - localControl.getTarget().getDwell());
    }
    if ((localBoolean.booleanValue()) && (this._currentActivity != null))
    {
      if (!this._formActivities.containsKey(Integer.valueOf(this._currentActivity.hashCode())))
        this._formActivities.put(Integer.valueOf(this._currentActivity.hashCode()), new ConcurrentHashMap());
      ((ConcurrentHashMap)this._formActivities.get(Integer.valueOf(this._currentActivity.hashCode()))).put(Integer.valueOf(paramView.getId()), localControl);
    }
    return localControl;
  }

  private Target createTarget(View paramView)
  {
    Target localTarget = new Target();
    localTarget.setType(getControlType(paramView));
    localTarget.setSubType(getControlSubType(paramView));
    localTarget.setId(getPropertyName(paramView));
    localTarget.setPosition(getPosition(paramView));
    localTarget.setTlType(getTlType(paramView));
    return localTarget;
  }

  private String getControlSubType(View paramView)
  {
    if (paramView == null);
    do
      return null;
    while (paramView.getClass().getSuperclass() == null);
    return paramView.getClass().getSuperclass().getSimpleName();
  }

  private String getControlType(View paramView)
  {
    if (paramView == null)
      return null;
    return paramView.getClass().getSimpleName();
  }

  private Position getPosition(View paramView)
  {
    Position localPosition = new Position();
    localPosition.setHeight(paramView.getHeight());
    localPosition.setWidth(paramView.getWidth());
    localPosition.setX(paramView.getLeft());
    localPosition.setY(paramView.getTop());
    return localPosition;
  }

  private String getPropertyName(View paramView)
  {
    try
    {
      Resources localResources = this._application.getResources();
      if (paramView.getId() == -1)
        return "-1";
      String str = localResources.getResourceName(paramView.getId());
      return str;
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException, "Trying to get property id for " + Integer.toString(paramView.getId()));
    }
    return null;
  }

  private Boolean getState(Target paramTarget, Object paramObject, String paramString)
  {
    Boolean localBoolean = Boolean.valueOf(false);
    HashMap localHashMap = new HashMap();
    if ((paramTarget == null) || (paramObject == null))
      return localBoolean;
    String str13;
    if ((paramObject instanceof ToggleButton))
      if (((ToggleButton)paramObject).isChecked())
      {
        str13 = "true";
        trimAndMaskValueForControl(localHashMap, "isToggled", str13, paramTarget.getId());
      }
    label485: label1270: 
    do
    {
      do
      {
        while (true)
        {
          paramTarget.setCurrentState(localHashMap);
          return localBoolean;
          str13 = "false";
          break;
          if ((paramObject instanceof Button))
          {
            if (((Button)paramObject).getText() == null);
            for (String str12 = null; ; str12 = ((Button)paramObject).getText().toString())
            {
              trimAndMaskValueForControl(localHashMap, "text", str12, paramTarget.getId());
              break;
            }
          }
          if ((paramObject instanceof DatePicker))
          {
            trimAndMaskValueForControl(localHashMap, "date", String.valueOf(new Date(((DatePicker)paramObject).getYear(), ((DatePicker)paramObject).getMonth(), ((DatePicker)paramObject).getDayOfMonth()).getTime()), paramTarget.getId());
            continue;
          }
          if ((paramObject instanceof Scroller))
          {
            trimAndMaskValueForControl(localHashMap, "x", Integer.toString(((Scroller)paramObject).getFinalX()), paramTarget.getId());
            trimAndMaskValueForControl(localHashMap, "y", Integer.toString(((Scroller)paramObject).getFinalY()), paramTarget.getId());
            continue;
          }
          if ((paramObject instanceof HorizontalScrollView))
          {
            trimAndMaskValueForControl(localHashMap, "x", Integer.toString(((HorizontalScrollView)paramObject).getScrollX()), paramTarget.getId());
            trimAndMaskValueForControl(localHashMap, "y", Integer.toString(((HorizontalScrollView)paramObject).getScrollY()), paramTarget.getId());
            continue;
          }
          if ((paramObject instanceof ScrollView))
          {
            trimAndMaskValueForControl(localHashMap, "x", Integer.toString(((ScrollView)paramObject).getScrollX()), paramTarget.getId());
            trimAndMaskValueForControl(localHashMap, "y", Integer.toString(((ScrollView)paramObject).getScrollY()), paramTarget.getId());
            continue;
          }
          if ((paramObject instanceof DialerFilter))
          {
            CharSequence localCharSequence6 = ((DialerFilter)paramObject).getFilterText();
            String str11 = null;
            if (localCharSequence6 == null);
            while (true)
            {
              trimAndMaskValueForControl(localHashMap, "text", str11, paramTarget.getId());
              break;
              str11 = ((DialerFilter)paramObject).getFilterText().toString();
            }
          }
          if (!(paramObject instanceof ProgressBar))
            break label485;
          trimAndMaskValueForControl(localHashMap, "value", Integer.toString(((ProgressBar)paramObject).getProgress()), paramTarget.getId());
          trimAndMaskValueForControl(localHashMap, "maxValue", Integer.toString(((ProgressBar)paramObject).getMax()), paramTarget.getId());
        }
        if ((paramObject instanceof RadioGroup))
        {
          RadioButton localRadioButton = (RadioButton)((View)paramObject).findViewById(((RadioGroup)paramObject).getCheckedRadioButtonId());
          String str10 = null;
          if (localRadioButton != null)
          {
            CharSequence localCharSequence5 = localRadioButton.getText();
            str10 = null;
            if (localCharSequence5 != null)
              break label552;
          }
          while (true)
          {
            trimAndMaskValueForControl(localHashMap, "text", str10, paramTarget.getId());
            break;
            str10 = localRadioButton.getText().toString();
          }
        }
        if ((paramObject instanceof Spinner))
        {
          CharSequence localCharSequence4 = ((Spinner)paramObject).getPrompt();
          String str9 = null;
          if (localCharSequence4 == null);
          while (true)
          {
            trimAndMaskValueForControl(localHashMap, "text", str9, paramTarget.getId());
            break;
            str9 = ((Spinner)paramObject).getPrompt().toString();
          }
        }
        if ((paramObject instanceof TabHost))
        {
          String str7 = ((TabHost)paramObject).getCurrentTabTag();
          String str8 = null;
          if (str7 == null);
          while (true)
          {
            trimAndMaskValueForControl(localHashMap, "text", str8, paramTarget.getId());
            break;
            str8 = ((TabHost)paramObject).getCurrentTabTag().toString();
          }
        }
        if ((paramObject instanceof TabWidget))
        {
          Object localObject = ((TabWidget)paramObject).getTag();
          String str6 = null;
          if (localObject == null);
          while (true)
          {
            trimAndMaskValueForControl(localHashMap, "text", str6, paramTarget.getId());
            break;
            str6 = ((TabWidget)paramObject).getTag().toString();
          }
        }
        if (!(paramObject instanceof TextView))
          break label1049;
        if ((paramString == null) || ((!paramString.equals("OnFocusChange_In")) && (!paramString.equals("OnFocusChange_Out"))))
          break label988;
        if (!paramString.equals("OnFocusChange_In"))
          continue;
        this._previousFocusControlDate = new Date();
        CharSequence localCharSequence3 = ((TextView)paramObject).getText();
        String str5 = null;
        if (localCharSequence3 == null);
        while (true)
        {
          trimAndMaskValueForControl(localHashMap, "text", str5, paramTarget.getId());
          paramTarget.setCurrentState(localHashMap);
          this._previousFocusTarget = paramTarget;
          break;
          str5 = ((TextView)paramObject).getText().toString();
        }
      }
      while (!paramString.equals("OnFocusChange_Out"));
      CharSequence localCharSequence2 = ((TextView)paramObject).getText();
      String str4 = null;
      if (localCharSequence2 == null);
      while (true)
      {
        trimAndMaskValueForControl(localHashMap, "text", str4, paramTarget.getId());
        if ((this._previousFocusTarget != null) && (this._previousFocusTarget.equals(paramTarget)) && (this._previousFocusControlDate != null))
        {
          paramTarget.setPreviousState(this._previousFocusTarget.getCurrentState());
          paramTarget.setDwell(new Date().getTime() - this._previousFocusControlDate.getTime());
        }
        setVisitedCount((TextView)paramObject, paramTarget);
        localBoolean = Boolean.valueOf(true);
        break;
        str4 = ((TextView)paramObject).getText().toString();
      }
      CharSequence localCharSequence1 = ((TextView)paramObject).getText();
      String str3 = null;
      if (localCharSequence1 == null);
      while (true)
      {
        trimAndMaskValueForControl(localHashMap, "text", str3, paramTarget.getId());
        setVisitedCount((TextView)paramObject, paramTarget);
        break;
        str3 = ((TextView)paramObject).getText().toString();
      }
      if ((paramObject instanceof TimePicker))
      {
        StringBuffer localStringBuffer = new StringBuffer(Integer.toString(((TimePicker)paramObject).getCurrentHour().intValue()));
        String str1 = Integer.toString(((TimePicker)paramObject).getCurrentMinute().intValue());
        if (str1.length() == 1);
        for (String str2 = ":0" + str1; ; str2 = ":" + str1)
        {
          localStringBuffer.append(str2);
          trimAndMaskValueForControl(localHashMap, "time", localStringBuffer.toString(), paramTarget.getId());
          break;
        }
      }
      if (!(paramObject instanceof AbsListView))
        continue;
      if (((paramObject instanceof ExpandableListView)) && (paramString != null))
      {
        if (!paramString.equals("OnGroupCollapse"))
          break label1270;
        trimAndMaskValueForControl(localHashMap, "text", "collapsed", paramTarget.getId());
      }
      while (true)
      {
        trimAndMaskValueForControl(localHashMap, "x", Integer.toString(((AbsListView)paramObject).getLeft()), paramTarget.getId());
        trimAndMaskValueForControl(localHashMap, "y", Integer.toString(((AbsListView)paramObject).getTop()), paramTarget.getId());
        break;
        if (!paramString.equals("OnGroupExpand"))
          continue;
        trimAndMaskValueForControl(localHashMap, "text", "expanded", paramTarget.getId());
      }
    }
    while (!(paramObject instanceof SlidingDrawer));
    label552: label988: if (paramString != null)
    {
      if (!paramString.equals("OnDrawerOpened"))
        break label1387;
      trimAndMaskValueForControl(localHashMap, "text", "opened", paramTarget.getId());
    }
    while (true)
    {
      label1049: trimAndMaskValueForControl(localHashMap, "x", Integer.toString(((SlidingDrawer)paramObject).getLeft()), paramTarget.getId());
      trimAndMaskValueForControl(localHashMap, "y", Integer.toString(((SlidingDrawer)paramObject).getTop()), paramTarget.getId());
      break;
      label1387: if (!paramString.equals("OnDrawerClosed"))
        continue;
      trimAndMaskValueForControl(localHashMap, "text", "closed", paramTarget.getId());
    }
  }

  private String getTlEvent(String paramString1, String paramString2)
  {
    String str = "";
    if (paramString1 == null)
      str = "";
    do
    {
      return str;
      if (("button".equals(paramString1)) || ("checkBox".equals(paramString1)) || ("gallery".equals(paramString1)) || ("radioButton".equals(paramString1)) || ("toggleButton".equals(paramString1)))
        return "click";
      if (("calendar".equals(paramString1)) || ("datePicker".equals(paramString1)))
        return "dateChange";
      if (("numberPicker".equals(paramString1)) || ("searchBox".equals(paramString1)) || ("selectList".equals(paramString1)) || ("slider".equals(paramString1)))
        return "valueChange";
      if ("scroll".equals(paramString1))
        return "scrollChange";
      if ("tabContainer".equals(paramString1))
        return "tabChange";
      if (!"textBox".equals(paramString1))
        continue;
      if ((paramString2 != null) && ("unchanged".equals(paramString1)))
        return "unchanged";
      return "textChange";
    }
    while (!"timePicker".equals(paramString1));
    return "timeChange";
  }

  private String getTlType(Object paramObject)
  {
    String str = "";
    if ((paramObject instanceof ToggleButton))
      str = "toggleButton";
    do
    {
      return str;
      if ((paramObject instanceof RadioButton))
        return "radioButton";
      if ((paramObject instanceof CheckBox))
        return "checkBox";
      if ((paramObject instanceof Button))
        return "button";
      if ((paramObject instanceof DatePicker))
        return "datePicker";
      if ((paramObject instanceof Scroller))
        return "scroll";
      if ((paramObject instanceof HorizontalScrollView))
        return "scroll";
      if ((paramObject instanceof ScrollView))
        return "scroll";
      if ((paramObject instanceof DialerFilter))
        return "dialerFilter";
      if ((paramObject instanceof ProgressBar))
        return "slider";
      if ((paramObject instanceof RadioGroup))
        return "radioButton";
      if ((paramObject instanceof Gallery))
        return "gallery";
      if ((paramObject instanceof AbsSpinner))
        return "selectList";
      if ((paramObject instanceof TabHost))
        return "tabContainer";
      if ((paramObject instanceof TabWidget))
        return "tabBar";
      if ((paramObject instanceof TextView))
        return "textBox";
      if ((paramObject instanceof TimePicker))
        return "timePicker";
      if ((paramObject instanceof Canvas))
        return "view";
      if ((paramObject instanceof ImageSwitcher))
        return "imageSwitcher";
      if ((paramObject instanceof Activity))
        return "page";
      if ((paramObject instanceof TabHost.TabSpec))
        return "tabBarItem";
    }
    while (!(paramObject instanceof WebView));
    return "webView";
  }

  private void setVisitedCount(View paramView, Target paramTarget)
  {
    Control localControl;
    if ((this._currentActivity != null) && (this._formActivities.containsKey(Integer.valueOf(this._currentActivity.hashCode()))))
    {
      localControl = (Control)((ConcurrentHashMap)this._formActivities.get(Integer.valueOf(this._currentActivity.hashCode()))).get(Integer.valueOf(((TextView)paramView).getId()));
      if (localControl != null)
        break label78;
    }
    label78: for (int i = 1; ; i = 1 + localControl.getTarget().getVisitedCount())
    {
      paramTarget.setVisitedCount(i);
      return;
    }
  }

  private Object trimAndMaskValueForControl(Map<String, String> paramMap, String paramString1, String paramString2, String paramString3)
  {
    String str = "";
    if (paramString2 != null)
      str = ValueUtil.compareListAndMask(paramString3, ValueUtil.trimValue(paramString2));
    if (str == null);
    while (true)
    {
      return paramMap.put(paramString1, paramString2);
      paramString2 = str;
    }
  }

  protected final Boolean enable(String paramString)
  {
    TLFCache.startSession(paramString);
    return Boolean.valueOf(true);
  }

  protected final long getApplicationScreenviewOffset()
  {
    if (this.loadDate != null)
      return new Date().getTime() - this.loadDate.getTime();
    return 0L;
  }

  public final Screenview getCurrentScreenview()
  {
    return this.currentScreenview;
  }

  protected final Boolean logConnection(String paramString, HttpResponse paramHttpResponse, long paramLong1, long paramLong2, long paramLong3)
  {
    Connection localConnection = new Connection();
    String str;
    if (paramHttpResponse != null)
    {
      localConnection.setResponseDataSize(paramHttpResponse.getEntity().getContentLength());
      localConnection.setStatusCode(paramHttpResponse.getStatusLine().getStatusCode());
      if (paramString != null)
        break label124;
      if (paramHttpResponse.getLastHeader("Location") != null)
        break label105;
      str = null;
    }
    while (true)
    {
      localConnection.setUrl(str);
      localConnection.setInitTime(TLFCache.timestampFromSession() - paramLong1);
      localConnection.setLoadTime(TLFCache.timestampFromSession() - paramLong2);
      localConnection.setResponseTime(paramLong3);
      return TLFCache.addMessage(localConnection);
      label105: str = paramHttpResponse.getLastHeader("Location").getValue();
      continue;
      label124: str = paramString;
    }
  }

  protected final Boolean logCustomEvent(String paramString, HashMap<String, String> paramHashMap, int paramInt)
  {
    if (paramInt > TLFCache.getLogLevel())
      return Boolean.valueOf(false);
    return TLFCache.addMessage(new CustomEvent(paramInt, paramString, paramHashMap));
  }

  protected final Boolean logEvent(View paramView, String paramString, int paramInt)
  {
    if (paramInt > TLFCache.getLogLevel())
      return Boolean.valueOf(false);
    Control localControl = createControl(paramView, paramString, paramInt);
    if ((paramString != null) && (paramString.equals("OnFocusChange_In")))
      return Boolean.valueOf(true);
    return TLFCache.addMessage(localControl);
  }

  protected final Boolean logException(Throwable paramThrowable, String paramString)
  {
    return logException(paramThrowable, paramString, null);
  }

  protected final Boolean logException(Throwable paramThrowable, String paramString, HashMap<String, String> paramHashMap)
  {
    JSONException localJSONException = new JSONException();
    localJSONException.setDescription(LogInternal.getExceptionMessage(paramThrowable, paramString));
    localJSONException.setName(paramThrowable.getClass().toString());
    localJSONException.setStackTrace(LogInternal.getStackTrace(paramThrowable));
    if (paramHashMap != null)
      localJSONException.setData(paramHashMap);
    Boolean localBoolean1 = TLFCache.addMessage(localJSONException);
    Boolean localBoolean2 = TLFCache.saveToCache(Boolean.valueOf(false));
    if ((localBoolean1.booleanValue()) && (localBoolean2.booleanValue()))
      return Boolean.valueOf(true);
    return Boolean.valueOf(false);
  }

  protected final Boolean logException(Throwable paramThrowable, HashMap<String, String> paramHashMap)
  {
    return logException(paramThrowable, null, paramHashMap);
  }

  protected final Boolean logScreenview(Activity paramActivity, String paramString1, ScreenviewType paramScreenviewType, String paramString2)
  {
    Screenview localScreenview = new Screenview(paramString1, paramScreenviewType, paramString2);
    if ((paramScreenviewType != null) && (paramScreenviewType.equals(ScreenviewType.LOAD)))
    {
      if ((paramString2 == null) && (this.currentScreenview != null))
        localScreenview.setReferringLogicalPageName(this.currentScreenview.getLogicalPageName());
      this.loadDate = new Date();
      this._currentActivity = paramActivity;
    }
    Iterator localIterator;
    if ((paramScreenviewType != null) && (paramScreenviewType.equals(ScreenviewType.UNLOAD)) && (paramActivity != null))
    {
      ConcurrentHashMap localConcurrentHashMap = (ConcurrentHashMap)this._formActivities.get(Integer.valueOf(paramActivity.hashCode()));
      if (localConcurrentHashMap != null)
        localIterator = localConcurrentHashMap.entrySet().iterator();
    }
    while (true)
    {
      if (!localIterator.hasNext())
      {
        this._formActivities.remove(Integer.valueOf(paramActivity.hashCode()));
        this.currentScreenview = localScreenview;
        return TLFCache.addMessage(localScreenview);
      }
      Control localControl = (Control)((Map.Entry)localIterator.next()).getValue();
      if (localControl.getTarget().getVisitedCount() != 0)
        continue;
      TLFCache.addMessage(localControl);
    }
  }

  protected final Boolean logTLLibErrorException(Throwable paramThrowable, String paramString)
  {
    JSONException localJSONException = new JSONException();
    localJSONException.setDescription(LogInternal.getTLLibErrorExceptionMessage(paramThrowable, paramString));
    localJSONException.setName(paramThrowable.getClass().toString());
    localJSONException.setStackTrace(LogInternal.getStackTrace(paramThrowable));
    Boolean localBoolean1 = TLFCache.addMessage(localJSONException);
    Boolean localBoolean2 = TLFCache.saveToCache(Boolean.valueOf(false));
    if ((localBoolean1.booleanValue()) && (localBoolean2.booleanValue()))
      return Boolean.valueOf(true);
    return Boolean.valueOf(false);
  }

  protected final Boolean onDestroy(String paramString)
  {
    Tealeaf.logCustomEvent(paramString + " is destroyed", 5);
    Tealeaf.flush();
    return Boolean.valueOf(true);
  }

  protected final Boolean onPause(Activity paramActivity, String paramString)
  {
    Tealeaf.logCustomEvent(paramString + " goes to background");
    Tealeaf.logScreenview(paramActivity, paramString, ScreenviewType.UNLOAD);
    return Boolean.valueOf(true);
  }

  protected final Boolean onPauseNoActivityInForeground()
  {
    TLFCache.onPause();
    Tealeaf.logCustomEvent("Application goes to background");
    return Boolean.valueOf(true);
  }

  protected final Boolean onResume(String paramString, Activity paramActivity)
  {
    TLFCache.onResume();
    Tealeaf.logCustomEvent(paramString + " comes from background");
    logScreenview(paramActivity, paramString, ScreenviewType.LOAD, null);
    return Boolean.valueOf(true);
  }

  protected final Boolean registerFormField(View paramView, Activity paramActivity, int paramInt)
  {
    Control localControl = createControl(paramView, "unchanged", paramInt);
    localControl.setOffset(0L);
    localControl.getTarget().setVisitedCount(0);
    if (!this._formActivities.containsKey(Integer.valueOf(paramActivity.hashCode())))
      this._formActivities.put(Integer.valueOf(paramActivity.hashCode()), new ConcurrentHashMap());
    ((ConcurrentHashMap)this._formActivities.get(Integer.valueOf(paramActivity.hashCode()))).put(Integer.valueOf(paramView.getId()), localControl);
    return Boolean.valueOf(((ConcurrentHashMap)this._formActivities.get(Integer.valueOf(paramActivity.hashCode()))).containsKey(Integer.valueOf(paramView.getId())));
  }

  protected final Boolean startSession(String paramString)
  {
    return TLFCache.startSession(paramString);
  }

  protected final Boolean terminate()
  {
    this._application = null;
    TLFCache.terminate();
    return Boolean.valueOf(true);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.Logger
 * JD-Core Version:    0.6.0
 */