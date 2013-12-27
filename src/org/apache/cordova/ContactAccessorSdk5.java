package org.apache.cordova;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import android.webkit.WebView;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.cordova.api.CordovaInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContactAccessorSdk5 extends ContactAccessor
{
  private static final String EMAIL_REGEXP = ".+@.+\\.+.+";
  private static final long MAX_PHOTO_SIZE = 1048576L;
  private static final Map<String, String> dbMap = new HashMap();

  static
  {
    dbMap.put("id", "contact_id");
    dbMap.put("displayName", "display_name");
    dbMap.put("name", "data1");
    dbMap.put("name.formatted", "data1");
    dbMap.put("name.familyName", "data3");
    dbMap.put("name.givenName", "data2");
    dbMap.put("name.middleName", "data5");
    dbMap.put("name.honorificPrefix", "data4");
    dbMap.put("name.honorificSuffix", "data6");
    dbMap.put("nickname", "data1");
    dbMap.put("phoneNumbers", "data1");
    dbMap.put("phoneNumbers.value", "data1");
    dbMap.put("emails", "data1");
    dbMap.put("emails.value", "data1");
    dbMap.put("addresses", "data1");
    dbMap.put("addresses.formatted", "data1");
    dbMap.put("addresses.streetAddress", "data4");
    dbMap.put("addresses.locality", "data7");
    dbMap.put("addresses.region", "data8");
    dbMap.put("addresses.postalCode", "data9");
    dbMap.put("addresses.country", "data10");
    dbMap.put("ims", "data1");
    dbMap.put("ims.value", "data1");
    dbMap.put("organizations", "data1");
    dbMap.put("organizations.name", "data1");
    dbMap.put("organizations.department", "data5");
    dbMap.put("organizations.title", "data4");
    dbMap.put("birthday", "vnd.android.cursor.item/contact_event");
    dbMap.put("note", "data1");
    dbMap.put("photos.value", "vnd.android.cursor.item/photo");
    dbMap.put("urls", "data1");
    dbMap.put("urls.value", "data1");
  }

  public ContactAccessorSdk5(WebView paramWebView, CordovaInterface paramCordovaInterface)
  {
    this.mApp = paramCordovaInterface;
    this.mView = paramWebView;
  }

  private JSONObject addressQuery(Cursor paramCursor)
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("id", paramCursor.getString(paramCursor.getColumnIndex("_id")));
      localJSONObject.put("pref", false);
      localJSONObject.put("type", getAddressType(paramCursor.getInt(paramCursor.getColumnIndex("data2"))));
      localJSONObject.put("formatted", paramCursor.getString(paramCursor.getColumnIndex("data1")));
      localJSONObject.put("streetAddress", paramCursor.getString(paramCursor.getColumnIndex("data4")));
      localJSONObject.put("locality", paramCursor.getString(paramCursor.getColumnIndex("data7")));
      localJSONObject.put("region", paramCursor.getString(paramCursor.getColumnIndex("data8")));
      localJSONObject.put("postalCode", paramCursor.getString(paramCursor.getColumnIndex("data9")));
      localJSONObject.put("country", paramCursor.getString(paramCursor.getColumnIndex("data10")));
      return localJSONObject;
    }
    catch (JSONException localJSONException)
    {
      Log.e("ContactsAccessor", localJSONException.getMessage(), localJSONException);
    }
    return localJSONObject;
  }

  private ContactAccessor.WhereOptions buildIdClause(Set<String> paramSet, String paramString)
  {
    ContactAccessor.WhereOptions localWhereOptions = new ContactAccessor.WhereOptions(this);
    if (paramString.equals("%"))
    {
      localWhereOptions.setWhere("(contact_id LIKE ? )");
      localWhereOptions.setWhereArgs(new String[] { paramString });
      return localWhereOptions;
    }
    Iterator localIterator = paramSet.iterator();
    StringBuffer localStringBuffer = new StringBuffer("(");
    while (localIterator.hasNext())
    {
      localStringBuffer.append("'" + (String)localIterator.next() + "'");
      if (!localIterator.hasNext())
        continue;
      localStringBuffer.append(",");
    }
    localStringBuffer.append(")");
    localWhereOptions.setWhere("contact_id IN " + localStringBuffer.toString());
    localWhereOptions.setWhereArgs(null);
    return localWhereOptions;
  }

  private ContactAccessor.WhereOptions buildWhereClause(JSONArray paramJSONArray, String paramString)
  {
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = new ArrayList();
    ContactAccessor.WhereOptions localWhereOptions = new ContactAccessor.WhereOptions(this);
    if (isWildCardSearch(paramJSONArray))
    {
      if ("%".equals(paramString))
      {
        localWhereOptions.setWhere("(display_name LIKE ? )");
        localWhereOptions.setWhereArgs(new String[] { paramString });
        return localWhereOptions;
      }
      localArrayList1.add("(" + (String)dbMap.get("displayName") + " LIKE ? )");
      localArrayList2.add(paramString);
      localArrayList1.add("(" + (String)dbMap.get("name") + " LIKE ? AND " + "mimetype" + " = ? )");
      localArrayList2.add(paramString);
      localArrayList2.add("vnd.android.cursor.item/name");
      localArrayList1.add("(" + (String)dbMap.get("nickname") + " LIKE ? AND " + "mimetype" + " = ? )");
      localArrayList2.add(paramString);
      localArrayList2.add("vnd.android.cursor.item/nickname");
      localArrayList1.add("(" + (String)dbMap.get("phoneNumbers") + " LIKE ? AND " + "mimetype" + " = ? )");
      localArrayList2.add(paramString);
      localArrayList2.add("vnd.android.cursor.item/phone_v2");
      localArrayList1.add("(" + (String)dbMap.get("emails") + " LIKE ? AND " + "mimetype" + " = ? )");
      localArrayList2.add(paramString);
      localArrayList2.add("vnd.android.cursor.item/email_v2");
      localArrayList1.add("(" + (String)dbMap.get("addresses") + " LIKE ? AND " + "mimetype" + " = ? )");
      localArrayList2.add(paramString);
      localArrayList2.add("vnd.android.cursor.item/postal-address_v2");
      localArrayList1.add("(" + (String)dbMap.get("ims") + " LIKE ? AND " + "mimetype" + " = ? )");
      localArrayList2.add(paramString);
      localArrayList2.add("vnd.android.cursor.item/im");
      localArrayList1.add("(" + (String)dbMap.get("organizations") + " LIKE ? AND " + "mimetype" + " = ? )");
      localArrayList2.add(paramString);
      localArrayList2.add("vnd.android.cursor.item/organization");
      localArrayList1.add("(" + (String)dbMap.get("note") + " LIKE ? AND " + "mimetype" + " = ? )");
      localArrayList2.add(paramString);
      localArrayList2.add("vnd.android.cursor.item/note");
      localArrayList1.add("(" + (String)dbMap.get("urls") + " LIKE ? AND " + "mimetype" + " = ? )");
      localArrayList2.add(paramString);
      localArrayList2.add("vnd.android.cursor.item/website");
    }
    if ("%".equals(paramString))
    {
      localWhereOptions.setWhere("(display_name LIKE ? )");
      localWhereOptions.setWhereArgs(new String[] { paramString });
      return localWhereOptions;
    }
    label1822: for (int i = 0; ; i++)
      try
      {
        if (i < paramJSONArray.length())
        {
          str = paramJSONArray.getString(i);
          if (str.equals("id"))
          {
            localArrayList1.add("(" + (String)dbMap.get(str) + " = ? )");
            localArrayList2.add(paramString.substring(1, -1 + paramString.length()));
            continue;
          }
          if (!str.startsWith("displayName"))
            break label1011;
          localArrayList1.add("(" + (String)dbMap.get(str) + " LIKE ? )");
          localArrayList2.add(paramString);
        }
      }
      catch (JSONException localJSONException)
      {
        String str;
        Log.e("ContactsAccessor", localJSONException.getMessage(), localJSONException);
        StringBuffer localStringBuffer = new StringBuffer();
        int j = 0;
        while (j < localArrayList1.size())
        {
          localStringBuffer.append((String)localArrayList1.get(j));
          if (j != -1 + localArrayList1.size())
            localStringBuffer.append(" OR ");
          j++;
          continue;
          label1011: if (str.startsWith("name"))
          {
            localArrayList1.add("(" + (String)dbMap.get(str) + " LIKE ? AND " + "mimetype" + " = ? )");
            localArrayList2.add(paramString);
            localArrayList2.add("vnd.android.cursor.item/name");
            break label1822;
          }
          if (str.startsWith("nickname"))
          {
            localArrayList1.add("(" + (String)dbMap.get(str) + " LIKE ? AND " + "mimetype" + " = ? )");
            localArrayList2.add(paramString);
            localArrayList2.add("vnd.android.cursor.item/nickname");
            break label1822;
          }
          if (str.startsWith("phoneNumbers"))
          {
            localArrayList1.add("(" + (String)dbMap.get(str) + " LIKE ? AND " + "mimetype" + " = ? )");
            localArrayList2.add(paramString);
            localArrayList2.add("vnd.android.cursor.item/phone_v2");
            break label1822;
          }
          if (str.startsWith("emails"))
          {
            localArrayList1.add("(" + (String)dbMap.get(str) + " LIKE ? AND " + "mimetype" + " = ? )");
            localArrayList2.add(paramString);
            localArrayList2.add("vnd.android.cursor.item/email_v2");
            break label1822;
          }
          if (str.startsWith("addresses"))
          {
            localArrayList1.add("(" + (String)dbMap.get(str) + " LIKE ? AND " + "mimetype" + " = ? )");
            localArrayList2.add(paramString);
            localArrayList2.add("vnd.android.cursor.item/postal-address_v2");
            break label1822;
          }
          if (str.startsWith("ims"))
          {
            localArrayList1.add("(" + (String)dbMap.get(str) + " LIKE ? AND " + "mimetype" + " = ? )");
            localArrayList2.add(paramString);
            localArrayList2.add("vnd.android.cursor.item/im");
            break label1822;
          }
          if (str.startsWith("organizations"))
          {
            localArrayList1.add("(" + (String)dbMap.get(str) + " LIKE ? AND " + "mimetype" + " = ? )");
            localArrayList2.add(paramString);
            localArrayList2.add("vnd.android.cursor.item/organization");
            break label1822;
          }
          if (str.startsWith("note"))
          {
            localArrayList1.add("(" + (String)dbMap.get(str) + " LIKE ? AND " + "mimetype" + " = ? )");
            localArrayList2.add(paramString);
            localArrayList2.add("vnd.android.cursor.item/note");
            break label1822;
          }
          if (!str.startsWith("urls"))
            break label1822;
          localArrayList1.add("(" + (String)dbMap.get(str) + " LIKE ? AND " + "mimetype" + " = ? )");
          localArrayList2.add(paramString);
          localArrayList2.add("vnd.android.cursor.item/website");
          break label1822;
        }
        localWhereOptions.setWhere(localStringBuffer.toString());
        String[] arrayOfString = new String[localArrayList2.size()];
        for (int k = 0; k < localArrayList2.size(); k++)
          arrayOfString[k] = ((String)localArrayList2.get(k));
        localWhereOptions.setWhereArgs(arrayOfString);
        return localWhereOptions;
      }
  }

  private String createNewContact(JSONObject paramJSONObject, String paramString1, String paramString2)
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI).withValue("account_type", paramString1).withValue("account_name", paramString2).build());
    try
    {
      JSONObject localJSONObject = paramJSONObject.optJSONObject("name");
      String str5 = paramJSONObject.getString("displayName");
      if ((str5 != null) || (localJSONObject != null))
        localArrayList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference("raw_contact_id", 0).withValue("mimetype", "vnd.android.cursor.item/name").withValue("data1", str5).withValue("data3", getJsonString(localJSONObject, "familyName")).withValue("data5", getJsonString(localJSONObject, "middleName")).withValue("data2", getJsonString(localJSONObject, "givenName")).withValue("data4", getJsonString(localJSONObject, "honorificPrefix")).withValue("data6", getJsonString(localJSONObject, "honorificSuffix")).build());
    }
    catch (JSONException localJSONException1)
    {
      try
      {
        while (true)
        {
          JSONArray localJSONArray7 = paramJSONObject.getJSONArray("phoneNumbers");
          if (localJSONArray7 == null)
            break;
          for (int i3 = 0; i3 < localJSONArray7.length(); i3++)
            insertPhone(localArrayList, (JSONObject)localJSONArray7.get(i3));
          localJSONException1 = localJSONException1;
          Log.d("ContactsAccessor", "Could not get name object");
        }
      }
      catch (JSONException localJSONException2)
      {
        Log.d("ContactsAccessor", "Could not get phone numbers");
        try
        {
          JSONArray localJSONArray6 = paramJSONObject.getJSONArray("emails");
          if (localJSONArray6 != null)
            for (int i2 = 0; i2 < localJSONArray6.length(); i2++)
              insertEmail(localArrayList, (JSONObject)localJSONArray6.get(i2));
        }
        catch (JSONException localJSONException3)
        {
          Log.d("ContactsAccessor", "Could not get emails");
          try
          {
            JSONArray localJSONArray5 = paramJSONObject.getJSONArray("addresses");
            if (localJSONArray5 != null)
              for (int i1 = 0; i1 < localJSONArray5.length(); i1++)
                insertAddress(localArrayList, (JSONObject)localJSONArray5.get(i1));
          }
          catch (JSONException localJSONException4)
          {
            Log.d("ContactsAccessor", "Could not get addresses");
            try
            {
              JSONArray localJSONArray4 = paramJSONObject.getJSONArray("organizations");
              if (localJSONArray4 != null)
                for (int n = 0; n < localJSONArray4.length(); n++)
                  insertOrganization(localArrayList, (JSONObject)localJSONArray4.get(n));
            }
            catch (JSONException localJSONException5)
            {
              Log.d("ContactsAccessor", "Could not get organizations");
              try
              {
                JSONArray localJSONArray3 = paramJSONObject.getJSONArray("ims");
                if (localJSONArray3 != null)
                  for (int m = 0; m < localJSONArray3.length(); m++)
                    insertIm(localArrayList, (JSONObject)localJSONArray3.get(m));
              }
              catch (JSONException localJSONException6)
              {
                Log.d("ContactsAccessor", "Could not get emails");
                String str1 = getJsonString(paramJSONObject, "note");
                if (str1 != null)
                  localArrayList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference("raw_contact_id", 0).withValue("mimetype", "vnd.android.cursor.item/note").withValue("data1", str1).build());
                String str2 = getJsonString(paramJSONObject, "nickname");
                if (str2 != null)
                  localArrayList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference("raw_contact_id", 0).withValue("mimetype", "vnd.android.cursor.item/nickname").withValue("data1", str2).build());
                try
                {
                  JSONArray localJSONArray2 = paramJSONObject.getJSONArray("urls");
                  if (localJSONArray2 != null)
                    for (int k = 0; k < localJSONArray2.length(); k++)
                      insertWebsite(localArrayList, (JSONObject)localJSONArray2.get(k));
                }
                catch (JSONException localJSONException7)
                {
                  Log.d("ContactsAccessor", "Could not get websites");
                  String str3 = getJsonString(paramJSONObject, "birthday");
                  if (str3 != null)
                    localArrayList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference("raw_contact_id", 0).withValue("mimetype", "vnd.android.cursor.item/contact_event").withValue("data2", Integer.valueOf(3)).withValue("data1", str3).build());
                  try
                  {
                    JSONArray localJSONArray1 = paramJSONObject.getJSONArray("photos");
                    if (localJSONArray1 != null)
                      for (int j = 0; j < localJSONArray1.length(); j++)
                        insertPhoto(localArrayList, (JSONObject)localJSONArray1.get(j));
                  }
                  catch (JSONException localJSONException8)
                  {
                    Log.d("ContactsAccessor", "Could not get photos");
                    try
                    {
                      ContentProviderResult[] arrayOfContentProviderResult = this.mApp.getActivity().getContentResolver().applyBatch("com.android.contacts", localArrayList);
                      int i = arrayOfContentProviderResult.length;
                      Object localObject = null;
                      if (i >= 0)
                      {
                        String str4 = arrayOfContentProviderResult[0].uri.getLastPathSegment();
                        localObject = str4;
                      }
                      return localObject;
                    }
                    catch (RemoteException localRemoteException)
                    {
                      Log.e("ContactsAccessor", localRemoteException.getMessage(), localRemoteException);
                      return null;
                    }
                    catch (OperationApplicationException localOperationApplicationException)
                    {
                      Log.e("ContactsAccessor", localOperationApplicationException.getMessage(), localOperationApplicationException);
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    return null;
  }

  private JSONObject emailQuery(Cursor paramCursor)
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("id", paramCursor.getString(paramCursor.getColumnIndex("_id")));
      localJSONObject.put("pref", false);
      localJSONObject.put("value", paramCursor.getString(paramCursor.getColumnIndex("data1")));
      localJSONObject.put("type", getContactType(paramCursor.getInt(paramCursor.getColumnIndex("data2"))));
      return localJSONObject;
    }
    catch (JSONException localJSONException)
    {
      Log.e("ContactsAccessor", localJSONException.getMessage(), localJSONException);
    }
    return localJSONObject;
  }

  private int getAddressType(String paramString)
  {
    int i = 3;
    if (paramString != null)
    {
      if (!"work".equals(paramString.toLowerCase()))
        break label23;
      i = 2;
    }
    label23: 
    do
    {
      return i;
      if ("other".equals(paramString.toLowerCase()))
        return 3;
    }
    while (!"home".equals(paramString.toLowerCase()));
    return 1;
  }

  private String getAddressType(int paramInt)
  {
    switch (paramInt)
    {
    default:
      return "other";
    case 1:
      return "home";
    case 2:
    }
    return "work";
  }

  private int getContactType(String paramString)
  {
    int i = 3;
    if (paramString != null)
    {
      if (!"home".equals(paramString.toLowerCase()))
        break label23;
      i = 1;
    }
    label23: 
    do
    {
      return i;
      if ("work".equals(paramString.toLowerCase()))
        return 2;
      if ("other".equals(paramString.toLowerCase()))
        return 3;
      if ("mobile".equals(paramString.toLowerCase()))
        return 4;
    }
    while (!"custom".equals(paramString.toLowerCase()));
    return 0;
  }

  private String getContactType(int paramInt)
  {
    switch (paramInt)
    {
    case 3:
    default:
      return "other";
    case 0:
      return "custom";
    case 1:
      return "home";
    case 2:
      return "work";
    case 4:
    }
    return "mobile";
  }

  private int getImType(String paramString)
  {
    int i = -1;
    if (paramString != null)
    {
      if (!"aim".equals(paramString.toLowerCase()))
        break label23;
      i = 0;
    }
    label23: 
    do
    {
      return i;
      if ("google talk".equals(paramString.toLowerCase()))
        return 5;
      if ("icq".equals(paramString.toLowerCase()))
        return 6;
      if ("jabber".equals(paramString.toLowerCase()))
        return 7;
      if ("msn".equals(paramString.toLowerCase()))
        return 1;
      if ("netmeeting".equals(paramString.toLowerCase()))
        return 8;
      if ("qq".equals(paramString.toLowerCase()))
        return 4;
      if ("skype".equals(paramString.toLowerCase()))
        return 3;
    }
    while (!"yahoo".equals(paramString.toLowerCase()));
    return 2;
  }

  private String getImType(int paramInt)
  {
    switch (paramInt)
    {
    default:
      return "custom";
    case 0:
      return "AIM";
    case 5:
      return "Google Talk";
    case 6:
      return "ICQ";
    case 7:
      return "Jabber";
    case 1:
      return "MSN";
    case 8:
      return "NetMeeting";
    case 4:
      return "QQ";
    case 3:
      return "Skype";
    case 2:
    }
    return "Yahoo";
  }

  private int getOrgType(String paramString)
  {
    int i = 2;
    if (paramString != null)
    {
      if (!"work".equals(paramString.toLowerCase()))
        break label23;
      i = 1;
    }
    label23: 
    do
    {
      return i;
      if ("other".equals(paramString.toLowerCase()))
        return 2;
    }
    while (!"custom".equals(paramString.toLowerCase()));
    return 0;
  }

  private String getOrgType(int paramInt)
  {
    switch (paramInt)
    {
    default:
      return "other";
    case 0:
      return "custom";
    case 1:
    }
    return "work";
  }

  private InputStream getPathFromUri(String paramString)
    throws IOException
  {
    if (paramString.startsWith("content:"))
    {
      Uri localUri = Uri.parse(paramString);
      return this.mApp.getActivity().getContentResolver().openInputStream(localUri);
    }
    if ((paramString.startsWith("http:")) || (paramString.startsWith("https:")) || (paramString.startsWith("file:")))
      return new URL(paramString).openStream();
    return new FileInputStream(paramString);
  }

  private int getPhoneType(String paramString)
  {
    int i = 7;
    if (paramString != null)
    {
      if (!"home".equals(paramString.toLowerCase()))
        break label24;
      i = 1;
    }
    label24: 
    do
    {
      return i;
      if ("mobile".equals(paramString.toLowerCase()))
        return 2;
      if ("work".equals(paramString.toLowerCase()))
        return 3;
      if ("work fax".equals(paramString.toLowerCase()))
        return 4;
      if ("home fax".equals(paramString.toLowerCase()))
        return 5;
      if ("fax".equals(paramString.toLowerCase()))
        return 4;
      if ("pager".equals(paramString.toLowerCase()))
        return 6;
      if ("other".equals(paramString.toLowerCase()))
        return 7;
      if ("car".equals(paramString.toLowerCase()))
        return 9;
      if ("company main".equals(paramString.toLowerCase()))
        return 10;
      if ("isdn".equals(paramString.toLowerCase()))
        return 11;
      if ("main".equals(paramString.toLowerCase()))
        return 12;
      if ("other fax".equals(paramString.toLowerCase()))
        return 13;
      if ("radio".equals(paramString.toLowerCase()))
        return 14;
      if ("telex".equals(paramString.toLowerCase()))
        return 15;
      if ("work mobile".equals(paramString.toLowerCase()))
        return 17;
      if ("work pager".equals(paramString.toLowerCase()))
        return 18;
      if ("assistant".equals(paramString.toLowerCase()))
        return 19;
      if ("mms".equals(paramString.toLowerCase()))
        return 20;
      if ("callback".equals(paramString.toLowerCase()))
        return 8;
      if ("tty ttd".equals(paramString.toLowerCase()))
        return 16;
    }
    while (!"custom".equals(paramString.toLowerCase()));
    return 0;
  }

  private String getPhoneType(int paramInt)
  {
    switch (paramInt)
    {
    case 7:
    case 12:
    default:
      return "other";
    case 0:
      return "custom";
    case 5:
      return "home fax";
    case 4:
      return "work fax";
    case 1:
      return "home";
    case 2:
      return "mobile";
    case 6:
      return "pager";
    case 3:
      return "work";
    case 8:
      return "callback";
    case 9:
      return "car";
    case 10:
      return "company main";
    case 13:
      return "other fax";
    case 14:
      return "radio";
    case 15:
      return "telex";
    case 16:
      return "tty tdd";
    case 17:
      return "work mobile";
    case 18:
      return "work pager";
    case 19:
      return "assistant";
    case 20:
      return "mms";
    case 11:
    }
    return "isdn";
  }

  private byte[] getPhotoBytes(String paramString)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    long l = 0L;
    try
    {
      byte[] arrayOfByte = new byte[8192];
      InputStream localInputStream = getPathFromUri(paramString);
      while (true)
      {
        int i = localInputStream.read(arrayOfByte, 0, arrayOfByte.length);
        if ((i == -1) || (l > 1048576L))
          break;
        localByteArrayOutputStream.write(arrayOfByte, 0, i);
        l += i;
      }
      localInputStream.close();
      localByteArrayOutputStream.flush();
      return localByteArrayOutputStream.toByteArray();
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      while (true)
        Log.e("ContactsAccessor", localFileNotFoundException.getMessage(), localFileNotFoundException);
    }
    catch (IOException localIOException)
    {
      while (true)
        Log.e("ContactsAccessor", localIOException.getMessage(), localIOException);
    }
  }

  private JSONObject imQuery(Cursor paramCursor)
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("id", paramCursor.getString(paramCursor.getColumnIndex("_id")));
      localJSONObject.put("pref", false);
      localJSONObject.put("value", paramCursor.getString(paramCursor.getColumnIndex("data1")));
      localJSONObject.put("type", getImType(paramCursor.getString(paramCursor.getColumnIndex("data5"))));
      return localJSONObject;
    }
    catch (JSONException localJSONException)
    {
      Log.e("ContactsAccessor", localJSONException.getMessage(), localJSONException);
    }
    return localJSONObject;
  }

  private void insertAddress(ArrayList<ContentProviderOperation> paramArrayList, JSONObject paramJSONObject)
  {
    paramArrayList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference("raw_contact_id", 0).withValue("mimetype", "vnd.android.cursor.item/postal-address_v2").withValue("data2", Integer.valueOf(getAddressType(getJsonString(paramJSONObject, "type")))).withValue("data1", getJsonString(paramJSONObject, "formatted")).withValue("data4", getJsonString(paramJSONObject, "streetAddress")).withValue("data7", getJsonString(paramJSONObject, "locality")).withValue("data8", getJsonString(paramJSONObject, "region")).withValue("data9", getJsonString(paramJSONObject, "postalCode")).withValue("data10", getJsonString(paramJSONObject, "country")).build());
  }

  private void insertEmail(ArrayList<ContentProviderOperation> paramArrayList, JSONObject paramJSONObject)
  {
    paramArrayList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference("raw_contact_id", 0).withValue("mimetype", "vnd.android.cursor.item/email_v2").withValue("data1", getJsonString(paramJSONObject, "value")).withValue("data2", Integer.valueOf(getContactType(getJsonString(paramJSONObject, "type")))).build());
  }

  private void insertIm(ArrayList<ContentProviderOperation> paramArrayList, JSONObject paramJSONObject)
  {
    paramArrayList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference("raw_contact_id", 0).withValue("mimetype", "vnd.android.cursor.item/im").withValue("data1", getJsonString(paramJSONObject, "value")).withValue("data2", Integer.valueOf(getImType(getJsonString(paramJSONObject, "type")))).build());
  }

  private void insertOrganization(ArrayList<ContentProviderOperation> paramArrayList, JSONObject paramJSONObject)
  {
    paramArrayList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference("raw_contact_id", 0).withValue("mimetype", "vnd.android.cursor.item/organization").withValue("data2", Integer.valueOf(getOrgType(getJsonString(paramJSONObject, "type")))).withValue("data5", getJsonString(paramJSONObject, "department")).withValue("data1", getJsonString(paramJSONObject, "name")).withValue("data4", getJsonString(paramJSONObject, "title")).build());
  }

  private void insertPhone(ArrayList<ContentProviderOperation> paramArrayList, JSONObject paramJSONObject)
  {
    paramArrayList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference("raw_contact_id", 0).withValue("mimetype", "vnd.android.cursor.item/phone_v2").withValue("data1", getJsonString(paramJSONObject, "value")).withValue("data2", Integer.valueOf(getPhoneType(getJsonString(paramJSONObject, "type")))).build());
  }

  private void insertPhoto(ArrayList<ContentProviderOperation> paramArrayList, JSONObject paramJSONObject)
  {
    byte[] arrayOfByte = getPhotoBytes(getJsonString(paramJSONObject, "value"));
    paramArrayList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference("raw_contact_id", 0).withValue("is_super_primary", Integer.valueOf(1)).withValue("mimetype", "vnd.android.cursor.item/photo").withValue("data15", arrayOfByte).build());
  }

  private void insertWebsite(ArrayList<ContentProviderOperation> paramArrayList, JSONObject paramJSONObject)
  {
    paramArrayList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference("raw_contact_id", 0).withValue("mimetype", "vnd.android.cursor.item/website").withValue("data1", getJsonString(paramJSONObject, "value")).withValue("data2", Integer.valueOf(getContactType(getJsonString(paramJSONObject, "type")))).build());
  }

  private boolean isWildCardSearch(JSONArray paramJSONArray)
  {
    if (paramJSONArray.length() == 1)
      try
      {
        boolean bool = "*".equals(paramJSONArray.getString(0));
        if (bool)
          return true;
      }
      catch (JSONException localJSONException)
      {
        return false;
      }
    return false;
  }

  // ERROR //
  private String modifyContact(String paramString1, JSONObject paramJSONObject, String paramString2, String paramString3)
  {
    // Byte code:
    //   0: new 434	java/lang/Integer
    //   3: dup
    //   4: aload_0
    //   5: aload_2
    //   6: ldc_w 662
    //   9: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   12: invokespecial 663	java/lang/Integer:<init>	(Ljava/lang/String;)V
    //   15: invokevirtual 666	java/lang/Integer:intValue	()I
    //   18: istore 5
    //   20: new 265	java/util/ArrayList
    //   23: dup
    //   24: invokespecial 266	java/util/ArrayList:<init>	()V
    //   27: astore 6
    //   29: aload 6
    //   31: getstatic 342	android/provider/ContactsContract$RawContacts:CONTENT_URI	Landroid/net/Uri;
    //   34: invokestatic 669	android/content/ContentProviderOperation:newUpdate	(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;
    //   37: ldc_w 350
    //   40: aload_3
    //   41: invokevirtual 356	android/content/ContentProviderOperation$Builder:withValue	(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
    //   44: ldc_w 358
    //   47: aload 4
    //   49: invokevirtual 356	android/content/ContentProviderOperation$Builder:withValue	(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
    //   52: invokevirtual 362	android/content/ContentProviderOperation$Builder:build	()Landroid/content/ContentProviderOperation;
    //   55: invokevirtual 281	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   58: pop
    //   59: aload_0
    //   60: aload_2
    //   61: ldc 36
    //   63: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   66: astore 119
    //   68: aload_2
    //   69: ldc 40
    //   71: invokevirtual 672	org/json/JSONObject:getJSONObject	(Ljava/lang/String;)Lorg/json/JSONObject;
    //   74: astore 120
    //   76: aload 119
    //   78: ifnonnull +8 -> 86
    //   81: aload 120
    //   83: ifnull +187 -> 270
    //   86: getstatic 372	android/provider/ContactsContract$Data:CONTENT_URI	Landroid/net/Uri;
    //   89: invokestatic 669	android/content/ContentProviderOperation:newUpdate	(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;
    //   92: ldc_w 674
    //   95: iconst_2
    //   96: anewarray 203	java/lang/String
    //   99: dup
    //   100: iconst_0
    //   101: aload_1
    //   102: aastore
    //   103: dup
    //   104: iconst_1
    //   105: ldc_w 289
    //   108: aastore
    //   109: invokevirtual 678	android/content/ContentProviderOperation$Builder:withSelection	(Ljava/lang/String;[Ljava/lang/String;)Landroid/content/ContentProviderOperation$Builder;
    //   112: astore 121
    //   114: aload 119
    //   116: ifnull +13 -> 129
    //   119: aload 121
    //   121: ldc 42
    //   123: aload 119
    //   125: invokevirtual 356	android/content/ContentProviderOperation$Builder:withValue	(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
    //   128: pop
    //   129: aload_0
    //   130: aload 120
    //   132: ldc_w 380
    //   135: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   138: astore 123
    //   140: aload 123
    //   142: ifnull +13 -> 155
    //   145: aload 121
    //   147: ldc 48
    //   149: aload 123
    //   151: invokevirtual 356	android/content/ContentProviderOperation$Builder:withValue	(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
    //   154: pop
    //   155: aload_0
    //   156: aload 120
    //   158: ldc_w 386
    //   161: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   164: astore 125
    //   166: aload 125
    //   168: ifnull +13 -> 181
    //   171: aload 121
    //   173: ldc 56
    //   175: aload 125
    //   177: invokevirtual 356	android/content/ContentProviderOperation$Builder:withValue	(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
    //   180: pop
    //   181: aload_0
    //   182: aload 120
    //   184: ldc_w 388
    //   187: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   190: astore 127
    //   192: aload 127
    //   194: ifnull +13 -> 207
    //   197: aload 121
    //   199: ldc 52
    //   201: aload 127
    //   203: invokevirtual 356	android/content/ContentProviderOperation$Builder:withValue	(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
    //   206: pop
    //   207: aload_0
    //   208: aload 120
    //   210: ldc_w 390
    //   213: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   216: astore 129
    //   218: aload 129
    //   220: ifnull +13 -> 233
    //   223: aload 121
    //   225: ldc 60
    //   227: aload 129
    //   229: invokevirtual 356	android/content/ContentProviderOperation$Builder:withValue	(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
    //   232: pop
    //   233: aload_0
    //   234: aload 120
    //   236: ldc_w 392
    //   239: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   242: astore 131
    //   244: aload 131
    //   246: ifnull +13 -> 259
    //   249: aload 121
    //   251: ldc 64
    //   253: aload 131
    //   255: invokevirtual 356	android/content/ContentProviderOperation$Builder:withValue	(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
    //   258: pop
    //   259: aload 6
    //   261: aload 121
    //   263: invokevirtual 362	android/content/ContentProviderOperation$Builder:build	()Landroid/content/ContentProviderOperation;
    //   266: invokevirtual 281	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   269: pop
    //   270: aload_2
    //   271: ldc 68
    //   273: invokevirtual 396	org/json/JSONObject:getJSONArray	(Ljava/lang/String;)Lorg/json/JSONArray;
    //   276: astore 108
    //   278: aload 108
    //   280: ifnull +76 -> 356
    //   283: aload 108
    //   285: invokevirtual 311	org/json/JSONArray:length	()I
    //   288: ifne +833 -> 1121
    //   291: getstatic 372	android/provider/ContactsContract$Data:CONTENT_URI	Landroid/net/Uri;
    //   294: invokestatic 681	android/content/ContentProviderOperation:newDelete	(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;
    //   297: astore 116
    //   299: iconst_2
    //   300: anewarray 203	java/lang/String
    //   303: astore 117
    //   305: aload 117
    //   307: iconst_0
    //   308: new 237	java/lang/StringBuilder
    //   311: dup
    //   312: invokespecial 238	java/lang/StringBuilder:<init>	()V
    //   315: ldc_w 683
    //   318: invokevirtual 244	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   321: iload 5
    //   323: invokevirtual 686	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   326: invokevirtual 251	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   329: aastore
    //   330: aload 117
    //   332: iconst_1
    //   333: ldc_w 293
    //   336: aastore
    //   337: aload 6
    //   339: aload 116
    //   341: ldc_w 688
    //   344: aload 117
    //   346: invokevirtual 678	android/content/ContentProviderOperation$Builder:withSelection	(Ljava/lang/String;[Ljava/lang/String;)Landroid/content/ContentProviderOperation$Builder;
    //   349: invokevirtual 362	android/content/ContentProviderOperation$Builder:build	()Landroid/content/ContentProviderOperation;
    //   352: invokevirtual 281	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   355: pop
    //   356: aload_2
    //   357: ldc 72
    //   359: invokevirtual 396	org/json/JSONObject:getJSONArray	(Ljava/lang/String;)Lorg/json/JSONArray;
    //   362: astore 97
    //   364: aload 97
    //   366: ifnull +76 -> 442
    //   369: aload 97
    //   371: invokevirtual 311	org/json/JSONArray:length	()I
    //   374: ifne +972 -> 1346
    //   377: getstatic 372	android/provider/ContactsContract$Data:CONTENT_URI	Landroid/net/Uri;
    //   380: invokestatic 681	android/content/ContentProviderOperation:newDelete	(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;
    //   383: astore 105
    //   385: iconst_2
    //   386: anewarray 203	java/lang/String
    //   389: astore 106
    //   391: aload 106
    //   393: iconst_0
    //   394: new 237	java/lang/StringBuilder
    //   397: dup
    //   398: invokespecial 238	java/lang/StringBuilder:<init>	()V
    //   401: ldc_w 683
    //   404: invokevirtual 244	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   407: iload 5
    //   409: invokevirtual 686	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   412: invokevirtual 251	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   415: aastore
    //   416: aload 106
    //   418: iconst_1
    //   419: ldc_w 295
    //   422: aastore
    //   423: aload 6
    //   425: aload 105
    //   427: ldc_w 688
    //   430: aload 106
    //   432: invokevirtual 678	android/content/ContentProviderOperation$Builder:withSelection	(Ljava/lang/String;[Ljava/lang/String;)Landroid/content/ContentProviderOperation$Builder;
    //   435: invokevirtual 362	android/content/ContentProviderOperation$Builder:build	()Landroid/content/ContentProviderOperation;
    //   438: invokevirtual 281	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   441: pop
    //   442: aload_2
    //   443: ldc 76
    //   445: invokevirtual 396	org/json/JSONObject:getJSONArray	(Ljava/lang/String;)Lorg/json/JSONArray;
    //   448: astore 86
    //   450: aload 86
    //   452: ifnull +76 -> 528
    //   455: aload 86
    //   457: invokevirtual 311	org/json/JSONArray:length	()I
    //   460: ifne +1111 -> 1571
    //   463: getstatic 372	android/provider/ContactsContract$Data:CONTENT_URI	Landroid/net/Uri;
    //   466: invokestatic 681	android/content/ContentProviderOperation:newDelete	(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;
    //   469: astore 94
    //   471: iconst_2
    //   472: anewarray 203	java/lang/String
    //   475: astore 95
    //   477: aload 95
    //   479: iconst_0
    //   480: new 237	java/lang/StringBuilder
    //   483: dup
    //   484: invokespecial 238	java/lang/StringBuilder:<init>	()V
    //   487: ldc_w 683
    //   490: invokevirtual 244	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   493: iload 5
    //   495: invokevirtual 686	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   498: invokevirtual 251	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   501: aastore
    //   502: aload 95
    //   504: iconst_1
    //   505: ldc_w 297
    //   508: aastore
    //   509: aload 6
    //   511: aload 94
    //   513: ldc_w 688
    //   516: aload 95
    //   518: invokevirtual 678	android/content/ContentProviderOperation$Builder:withSelection	(Ljava/lang/String;[Ljava/lang/String;)Landroid/content/ContentProviderOperation$Builder;
    //   521: invokevirtual 362	android/content/ContentProviderOperation$Builder:build	()Landroid/content/ContentProviderOperation;
    //   524: invokevirtual 281	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   527: pop
    //   528: aload_2
    //   529: ldc 102
    //   531: invokevirtual 396	org/json/JSONObject:getJSONArray	(Ljava/lang/String;)Lorg/json/JSONArray;
    //   534: astore 75
    //   536: aload 75
    //   538: ifnull +76 -> 614
    //   541: aload 75
    //   543: invokevirtual 311	org/json/JSONArray:length	()I
    //   546: ifne +1388 -> 1934
    //   549: getstatic 372	android/provider/ContactsContract$Data:CONTENT_URI	Landroid/net/Uri;
    //   552: invokestatic 681	android/content/ContentProviderOperation:newDelete	(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;
    //   555: astore 83
    //   557: iconst_2
    //   558: anewarray 203	java/lang/String
    //   561: astore 84
    //   563: aload 84
    //   565: iconst_0
    //   566: new 237	java/lang/StringBuilder
    //   569: dup
    //   570: invokespecial 238	java/lang/StringBuilder:<init>	()V
    //   573: ldc_w 683
    //   576: invokevirtual 244	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   579: iload 5
    //   581: invokevirtual 686	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   584: invokevirtual 251	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   587: aastore
    //   588: aload 84
    //   590: iconst_1
    //   591: ldc_w 301
    //   594: aastore
    //   595: aload 6
    //   597: aload 83
    //   599: ldc_w 688
    //   602: aload 84
    //   604: invokevirtual 678	android/content/ContentProviderOperation$Builder:withSelection	(Ljava/lang/String;[Ljava/lang/String;)Landroid/content/ContentProviderOperation$Builder;
    //   607: invokevirtual 362	android/content/ContentProviderOperation$Builder:build	()Landroid/content/ContentProviderOperation;
    //   610: invokevirtual 281	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   613: pop
    //   614: aload_2
    //   615: ldc 98
    //   617: invokevirtual 396	org/json/JSONObject:getJSONArray	(Ljava/lang/String;)Lorg/json/JSONArray;
    //   620: astore 64
    //   622: aload 64
    //   624: ifnull +76 -> 700
    //   627: aload 64
    //   629: invokevirtual 311	org/json/JSONArray:length	()I
    //   632: ifne +1585 -> 2217
    //   635: getstatic 372	android/provider/ContactsContract$Data:CONTENT_URI	Landroid/net/Uri;
    //   638: invokestatic 681	android/content/ContentProviderOperation:newDelete	(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;
    //   641: astore 72
    //   643: iconst_2
    //   644: anewarray 203	java/lang/String
    //   647: astore 73
    //   649: aload 73
    //   651: iconst_0
    //   652: new 237	java/lang/StringBuilder
    //   655: dup
    //   656: invokespecial 238	java/lang/StringBuilder:<init>	()V
    //   659: ldc_w 683
    //   662: invokevirtual 244	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   665: iload 5
    //   667: invokevirtual 686	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   670: invokevirtual 251	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   673: aastore
    //   674: aload 73
    //   676: iconst_1
    //   677: ldc_w 299
    //   680: aastore
    //   681: aload 6
    //   683: aload 72
    //   685: ldc_w 688
    //   688: aload 73
    //   690: invokevirtual 678	android/content/ContentProviderOperation$Builder:withSelection	(Ljava/lang/String;[Ljava/lang/String;)Landroid/content/ContentProviderOperation$Builder;
    //   693: invokevirtual 362	android/content/ContentProviderOperation$Builder:build	()Landroid/content/ContentProviderOperation;
    //   696: invokevirtual 281	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   699: pop
    //   700: aload_0
    //   701: aload_2
    //   702: ldc 114
    //   704: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   707: astore 20
    //   709: aload 6
    //   711: getstatic 372	android/provider/ContactsContract$Data:CONTENT_URI	Landroid/net/Uri;
    //   714: invokestatic 669	android/content/ContentProviderOperation:newUpdate	(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;
    //   717: ldc_w 674
    //   720: iconst_2
    //   721: anewarray 203	java/lang/String
    //   724: dup
    //   725: iconst_0
    //   726: aload_1
    //   727: aastore
    //   728: dup
    //   729: iconst_1
    //   730: ldc_w 303
    //   733: aastore
    //   734: invokevirtual 678	android/content/ContentProviderOperation$Builder:withSelection	(Ljava/lang/String;[Ljava/lang/String;)Landroid/content/ContentProviderOperation$Builder;
    //   737: ldc 42
    //   739: aload 20
    //   741: invokevirtual 356	android/content/ContentProviderOperation$Builder:withValue	(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
    //   744: invokevirtual 362	android/content/ContentProviderOperation$Builder:build	()Landroid/content/ContentProviderOperation;
    //   747: invokevirtual 281	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   750: pop
    //   751: aload_0
    //   752: aload_2
    //   753: ldc 66
    //   755: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   758: astore 22
    //   760: aload 22
    //   762: ifnull +45 -> 807
    //   765: aload 6
    //   767: getstatic 372	android/provider/ContactsContract$Data:CONTENT_URI	Landroid/net/Uri;
    //   770: invokestatic 669	android/content/ContentProviderOperation:newUpdate	(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;
    //   773: ldc_w 674
    //   776: iconst_2
    //   777: anewarray 203	java/lang/String
    //   780: dup
    //   781: iconst_0
    //   782: aload_1
    //   783: aastore
    //   784: dup
    //   785: iconst_1
    //   786: ldc_w 291
    //   789: aastore
    //   790: invokevirtual 678	android/content/ContentProviderOperation$Builder:withSelection	(Ljava/lang/String;[Ljava/lang/String;)Landroid/content/ContentProviderOperation$Builder;
    //   793: ldc 42
    //   795: aload 22
    //   797: invokevirtual 356	android/content/ContentProviderOperation$Builder:withValue	(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
    //   800: invokevirtual 362	android/content/ContentProviderOperation$Builder:build	()Landroid/content/ContentProviderOperation;
    //   803: invokevirtual 281	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   806: pop
    //   807: aload_2
    //   808: ldc 120
    //   810: invokevirtual 396	org/json/JSONObject:getJSONArray	(Ljava/lang/String;)Lorg/json/JSONArray;
    //   813: astore 51
    //   815: aload 51
    //   817: ifnull +85 -> 902
    //   820: aload 51
    //   822: invokevirtual 311	org/json/JSONArray:length	()I
    //   825: ifne +1617 -> 2442
    //   828: ldc 182
    //   830: ldc_w 690
    //   833: invokestatic 407	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   836: pop
    //   837: getstatic 372	android/provider/ContactsContract$Data:CONTENT_URI	Landroid/net/Uri;
    //   840: invokestatic 681	android/content/ContentProviderOperation:newDelete	(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;
    //   843: astore 60
    //   845: iconst_2
    //   846: anewarray 203	java/lang/String
    //   849: astore 61
    //   851: aload 61
    //   853: iconst_0
    //   854: new 237	java/lang/StringBuilder
    //   857: dup
    //   858: invokespecial 238	java/lang/StringBuilder:<init>	()V
    //   861: ldc_w 683
    //   864: invokevirtual 244	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   867: iload 5
    //   869: invokevirtual 686	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   872: invokevirtual 251	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   875: aastore
    //   876: aload 61
    //   878: iconst_1
    //   879: ldc_w 305
    //   882: aastore
    //   883: aload 6
    //   885: aload 60
    //   887: ldc_w 688
    //   890: aload 61
    //   892: invokevirtual 678	android/content/ContentProviderOperation$Builder:withSelection	(Ljava/lang/String;[Ljava/lang/String;)Landroid/content/ContentProviderOperation$Builder;
    //   895: invokevirtual 362	android/content/ContentProviderOperation$Builder:build	()Landroid/content/ContentProviderOperation;
    //   898: invokevirtual 281	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   901: pop
    //   902: aload_0
    //   903: aload_2
    //   904: ldc 110
    //   906: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   909: astore 25
    //   911: aload 25
    //   913: ifnull +77 -> 990
    //   916: getstatic 372	android/provider/ContactsContract$Data:CONTENT_URI	Landroid/net/Uri;
    //   919: invokestatic 669	android/content/ContentProviderOperation:newUpdate	(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;
    //   922: astore 48
    //   924: iconst_3
    //   925: anewarray 203	java/lang/String
    //   928: astore 49
    //   930: aload 49
    //   932: iconst_0
    //   933: aload_1
    //   934: aastore
    //   935: aload 49
    //   937: iconst_1
    //   938: ldc 112
    //   940: aastore
    //   941: aload 49
    //   943: iconst_2
    //   944: new 203	java/lang/String
    //   947: dup
    //   948: ldc_w 692
    //   951: invokespecial 693	java/lang/String:<init>	(Ljava/lang/String;)V
    //   954: aastore
    //   955: aload 6
    //   957: aload 48
    //   959: ldc_w 695
    //   962: aload 49
    //   964: invokevirtual 678	android/content/ContentProviderOperation$Builder:withSelection	(Ljava/lang/String;[Ljava/lang/String;)Landroid/content/ContentProviderOperation$Builder;
    //   967: ldc 52
    //   969: iconst_3
    //   970: invokestatic 438	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   973: invokevirtual 356	android/content/ContentProviderOperation$Builder:withValue	(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
    //   976: ldc 42
    //   978: aload 25
    //   980: invokevirtual 356	android/content/ContentProviderOperation$Builder:withValue	(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
    //   983: invokevirtual 362	android/content/ContentProviderOperation$Builder:build	()Landroid/content/ContentProviderOperation;
    //   986: invokevirtual 281	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   989: pop
    //   990: aload_2
    //   991: ldc_w 440
    //   994: invokevirtual 396	org/json/JSONObject:getJSONArray	(Ljava/lang/String;)Lorg/json/JSONArray;
    //   997: astore 36
    //   999: aload 36
    //   1001: ifnull +75 -> 1076
    //   1004: aload 36
    //   1006: invokevirtual 311	org/json/JSONArray:length	()I
    //   1009: ifne +1658 -> 2667
    //   1012: getstatic 372	android/provider/ContactsContract$Data:CONTENT_URI	Landroid/net/Uri;
    //   1015: invokestatic 681	android/content/ContentProviderOperation:newDelete	(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;
    //   1018: astore 45
    //   1020: iconst_2
    //   1021: anewarray 203	java/lang/String
    //   1024: astore 46
    //   1026: aload 46
    //   1028: iconst_0
    //   1029: new 237	java/lang/StringBuilder
    //   1032: dup
    //   1033: invokespecial 238	java/lang/StringBuilder:<init>	()V
    //   1036: ldc_w 683
    //   1039: invokevirtual 244	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1042: iload 5
    //   1044: invokevirtual 686	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   1047: invokevirtual 251	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1050: aastore
    //   1051: aload 46
    //   1053: iconst_1
    //   1054: ldc 118
    //   1056: aastore
    //   1057: aload 6
    //   1059: aload 45
    //   1061: ldc_w 688
    //   1064: aload 46
    //   1066: invokevirtual 678	android/content/ContentProviderOperation$Builder:withSelection	(Ljava/lang/String;[Ljava/lang/String;)Landroid/content/ContentProviderOperation$Builder;
    //   1069: invokevirtual 362	android/content/ContentProviderOperation$Builder:build	()Landroid/content/ContentProviderOperation;
    //   1072: invokevirtual 281	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   1075: pop
    //   1076: iconst_1
    //   1077: istore 28
    //   1079: aload_0
    //   1080: getfield 128	org/apache/cordova/ContactAccessorSdk5:mApp	Lorg/apache/cordova/api/CordovaInterface;
    //   1083: invokeinterface 451 1 0
    //   1088: invokevirtual 457	android/app/Activity:getContentResolver	()Landroid/content/ContentResolver;
    //   1091: ldc_w 459
    //   1094: aload 6
    //   1096: invokevirtual 465	android/content/ContentResolver:applyBatch	(Ljava/lang/String;Ljava/util/ArrayList;)[Landroid/content/ContentProviderResult;
    //   1099: pop
    //   1100: iload 28
    //   1102: ifeq +1839 -> 2941
    //   1105: aload_1
    //   1106: areturn
    //   1107: astore 8
    //   1109: ldc 182
    //   1111: ldc_w 697
    //   1114: invokestatic 407	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   1117: pop
    //   1118: goto -848 -> 270
    //   1121: iconst_0
    //   1122: istore 109
    //   1124: aload 108
    //   1126: invokevirtual 311	org/json/JSONArray:length	()I
    //   1129: istore 110
    //   1131: iload 109
    //   1133: iload 110
    //   1135: if_icmpge -779 -> 356
    //   1138: aload 108
    //   1140: iload 109
    //   1142: invokevirtual 397	org/json/JSONArray:get	(I)Ljava/lang/Object;
    //   1145: checkcast 138	org/json/JSONObject
    //   1148: astore 111
    //   1150: aload_0
    //   1151: aload 111
    //   1153: ldc 26
    //   1155: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   1158: astore 112
    //   1160: aload 112
    //   1162: ifnonnull +97 -> 1259
    //   1165: new 699	android/content/ContentValues
    //   1168: dup
    //   1169: invokespecial 700	android/content/ContentValues:<init>	()V
    //   1172: astore 113
    //   1174: aload 113
    //   1176: ldc_w 374
    //   1179: iload 5
    //   1181: invokestatic 438	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   1184: invokevirtual 703	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   1187: aload 113
    //   1189: ldc_w 285
    //   1192: ldc_w 293
    //   1195: invokevirtual 706	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   1198: aload 113
    //   1200: ldc 42
    //   1202: aload_0
    //   1203: aload 111
    //   1205: ldc_w 480
    //   1208: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   1211: invokevirtual 706	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   1214: aload 113
    //   1216: ldc 52
    //   1218: aload_0
    //   1219: aload_0
    //   1220: aload 111
    //   1222: ldc 161
    //   1224: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   1227: invokespecial 650	org/apache/cordova/ContactAccessorSdk5:getPhoneType	(Ljava/lang/String;)I
    //   1230: invokestatic 438	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   1233: invokevirtual 703	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   1236: aload 6
    //   1238: getstatic 372	android/provider/ContactsContract$Data:CONTENT_URI	Landroid/net/Uri;
    //   1241: invokestatic 348	android/content/ContentProviderOperation:newInsert	(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;
    //   1244: aload 113
    //   1246: invokevirtual 710	android/content/ContentProviderOperation$Builder:withValues	(Landroid/content/ContentValues;)Landroid/content/ContentProviderOperation$Builder;
    //   1249: invokevirtual 362	android/content/ContentProviderOperation$Builder:build	()Landroid/content/ContentProviderOperation;
    //   1252: invokevirtual 281	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   1255: pop
    //   1256: goto +1687 -> 2943
    //   1259: aload 6
    //   1261: getstatic 372	android/provider/ContactsContract$Data:CONTENT_URI	Landroid/net/Uri;
    //   1264: invokestatic 669	android/content/ContentProviderOperation:newUpdate	(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;
    //   1267: ldc_w 712
    //   1270: iconst_2
    //   1271: anewarray 203	java/lang/String
    //   1274: dup
    //   1275: iconst_0
    //   1276: aload 112
    //   1278: aastore
    //   1279: dup
    //   1280: iconst_1
    //   1281: ldc_w 293
    //   1284: aastore
    //   1285: invokevirtual 678	android/content/ContentProviderOperation$Builder:withSelection	(Ljava/lang/String;[Ljava/lang/String;)Landroid/content/ContentProviderOperation$Builder;
    //   1288: ldc 42
    //   1290: aload_0
    //   1291: aload 111
    //   1293: ldc_w 480
    //   1296: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   1299: invokevirtual 356	android/content/ContentProviderOperation$Builder:withValue	(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
    //   1302: ldc 52
    //   1304: aload_0
    //   1305: aload_0
    //   1306: aload 111
    //   1308: ldc 161
    //   1310: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   1313: invokespecial 650	org/apache/cordova/ContactAccessorSdk5:getPhoneType	(Ljava/lang/String;)I
    //   1316: invokestatic 438	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   1319: invokevirtual 356	android/content/ContentProviderOperation$Builder:withValue	(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
    //   1322: invokevirtual 362	android/content/ContentProviderOperation$Builder:build	()Landroid/content/ContentProviderOperation;
    //   1325: invokevirtual 281	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   1328: pop
    //   1329: goto +1614 -> 2943
    //   1332: astore 10
    //   1334: ldc 182
    //   1336: ldc_w 409
    //   1339: invokestatic 407	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   1342: pop
    //   1343: goto -987 -> 356
    //   1346: iconst_0
    //   1347: istore 98
    //   1349: aload 97
    //   1351: invokevirtual 311	org/json/JSONArray:length	()I
    //   1354: istore 99
    //   1356: iload 98
    //   1358: iload 99
    //   1360: if_icmpge -918 -> 442
    //   1363: aload 97
    //   1365: iload 98
    //   1367: invokevirtual 397	org/json/JSONArray:get	(I)Ljava/lang/Object;
    //   1370: checkcast 138	org/json/JSONObject
    //   1373: astore 100
    //   1375: aload_0
    //   1376: aload 100
    //   1378: ldc 26
    //   1380: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   1383: astore 101
    //   1385: aload 101
    //   1387: ifnonnull +97 -> 1484
    //   1390: new 699	android/content/ContentValues
    //   1393: dup
    //   1394: invokespecial 700	android/content/ContentValues:<init>	()V
    //   1397: astore 102
    //   1399: aload 102
    //   1401: ldc_w 374
    //   1404: iload 5
    //   1406: invokestatic 438	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   1409: invokevirtual 703	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   1412: aload 102
    //   1414: ldc_w 285
    //   1417: ldc_w 295
    //   1420: invokevirtual 706	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   1423: aload 102
    //   1425: ldc 42
    //   1427: aload_0
    //   1428: aload 100
    //   1430: ldc_w 480
    //   1433: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   1436: invokevirtual 706	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   1439: aload 102
    //   1441: ldc 52
    //   1443: aload_0
    //   1444: aload_0
    //   1445: aload 100
    //   1447: ldc 161
    //   1449: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   1452: invokespecial 642	org/apache/cordova/ContactAccessorSdk5:getContactType	(Ljava/lang/String;)I
    //   1455: invokestatic 438	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   1458: invokevirtual 703	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   1461: aload 6
    //   1463: getstatic 372	android/provider/ContactsContract$Data:CONTENT_URI	Landroid/net/Uri;
    //   1466: invokestatic 348	android/content/ContentProviderOperation:newInsert	(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;
    //   1469: aload 102
    //   1471: invokevirtual 710	android/content/ContentProviderOperation$Builder:withValues	(Landroid/content/ContentValues;)Landroid/content/ContentProviderOperation$Builder;
    //   1474: invokevirtual 362	android/content/ContentProviderOperation$Builder:build	()Landroid/content/ContentProviderOperation;
    //   1477: invokevirtual 281	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   1480: pop
    //   1481: goto +1468 -> 2949
    //   1484: aload 6
    //   1486: getstatic 372	android/provider/ContactsContract$Data:CONTENT_URI	Landroid/net/Uri;
    //   1489: invokestatic 669	android/content/ContentProviderOperation:newUpdate	(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;
    //   1492: ldc_w 712
    //   1495: iconst_2
    //   1496: anewarray 203	java/lang/String
    //   1499: dup
    //   1500: iconst_0
    //   1501: aload 101
    //   1503: aastore
    //   1504: dup
    //   1505: iconst_1
    //   1506: ldc_w 295
    //   1509: aastore
    //   1510: invokevirtual 678	android/content/ContentProviderOperation$Builder:withSelection	(Ljava/lang/String;[Ljava/lang/String;)Landroid/content/ContentProviderOperation$Builder;
    //   1513: ldc 42
    //   1515: aload_0
    //   1516: aload 100
    //   1518: ldc_w 480
    //   1521: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   1524: invokevirtual 356	android/content/ContentProviderOperation$Builder:withValue	(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
    //   1527: ldc 52
    //   1529: aload_0
    //   1530: aload_0
    //   1531: aload 100
    //   1533: ldc 161
    //   1535: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   1538: invokespecial 642	org/apache/cordova/ContactAccessorSdk5:getContactType	(Ljava/lang/String;)I
    //   1541: invokestatic 438	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   1544: invokevirtual 356	android/content/ContentProviderOperation$Builder:withValue	(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
    //   1547: invokevirtual 362	android/content/ContentProviderOperation$Builder:build	()Landroid/content/ContentProviderOperation;
    //   1550: invokevirtual 281	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   1553: pop
    //   1554: goto +1395 -> 2949
    //   1557: astore 12
    //   1559: ldc 182
    //   1561: ldc_w 414
    //   1564: invokestatic 407	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   1567: pop
    //   1568: goto -1126 -> 442
    //   1571: iconst_0
    //   1572: istore 87
    //   1574: aload 86
    //   1576: invokevirtual 311	org/json/JSONArray:length	()I
    //   1579: istore 88
    //   1581: iload 87
    //   1583: iload 88
    //   1585: if_icmpge -1057 -> 528
    //   1588: aload 86
    //   1590: iload 87
    //   1592: invokevirtual 397	org/json/JSONArray:get	(I)Ljava/lang/Object;
    //   1595: checkcast 138	org/json/JSONObject
    //   1598: astore 89
    //   1600: aload_0
    //   1601: aload 89
    //   1603: ldc 26
    //   1605: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   1608: astore 90
    //   1610: aload 90
    //   1612: ifnonnull +171 -> 1783
    //   1615: new 699	android/content/ContentValues
    //   1618: dup
    //   1619: invokespecial 700	android/content/ContentValues:<init>	()V
    //   1622: astore 91
    //   1624: aload 91
    //   1626: ldc_w 374
    //   1629: iload 5
    //   1631: invokestatic 438	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   1634: invokevirtual 703	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   1637: aload 91
    //   1639: ldc_w 285
    //   1642: ldc_w 297
    //   1645: invokevirtual 706	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   1648: aload 91
    //   1650: ldc 52
    //   1652: aload_0
    //   1653: aload_0
    //   1654: aload 89
    //   1656: ldc 161
    //   1658: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   1661: invokespecial 640	org/apache/cordova/ContactAccessorSdk5:getAddressType	(Ljava/lang/String;)I
    //   1664: invokestatic 438	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   1667: invokevirtual 703	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   1670: aload 91
    //   1672: ldc 42
    //   1674: aload_0
    //   1675: aload 89
    //   1677: ldc 170
    //   1679: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   1682: invokevirtual 706	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   1685: aload 91
    //   1687: ldc 60
    //   1689: aload_0
    //   1690: aload 89
    //   1692: ldc 172
    //   1694: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   1697: invokevirtual 706	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   1700: aload 91
    //   1702: ldc 84
    //   1704: aload_0
    //   1705: aload 89
    //   1707: ldc 174
    //   1709: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   1712: invokevirtual 706	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   1715: aload 91
    //   1717: ldc 88
    //   1719: aload_0
    //   1720: aload 89
    //   1722: ldc 176
    //   1724: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   1727: invokevirtual 706	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   1730: aload 91
    //   1732: ldc 92
    //   1734: aload_0
    //   1735: aload 89
    //   1737: ldc 178
    //   1739: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   1742: invokevirtual 706	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   1745: aload 91
    //   1747: ldc 96
    //   1749: aload_0
    //   1750: aload 89
    //   1752: ldc 180
    //   1754: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   1757: invokevirtual 706	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   1760: aload 6
    //   1762: getstatic 372	android/provider/ContactsContract$Data:CONTENT_URI	Landroid/net/Uri;
    //   1765: invokestatic 348	android/content/ContentProviderOperation:newInsert	(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;
    //   1768: aload 91
    //   1770: invokevirtual 710	android/content/ContentProviderOperation$Builder:withValues	(Landroid/content/ContentValues;)Landroid/content/ContentProviderOperation$Builder;
    //   1773: invokevirtual 362	android/content/ContentProviderOperation$Builder:build	()Landroid/content/ContentProviderOperation;
    //   1776: invokevirtual 281	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   1779: pop
    //   1780: goto +1175 -> 2955
    //   1783: aload 6
    //   1785: getstatic 372	android/provider/ContactsContract$Data:CONTENT_URI	Landroid/net/Uri;
    //   1788: invokestatic 669	android/content/ContentProviderOperation:newUpdate	(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;
    //   1791: ldc_w 712
    //   1794: iconst_2
    //   1795: anewarray 203	java/lang/String
    //   1798: dup
    //   1799: iconst_0
    //   1800: aload 90
    //   1802: aastore
    //   1803: dup
    //   1804: iconst_1
    //   1805: ldc_w 297
    //   1808: aastore
    //   1809: invokevirtual 678	android/content/ContentProviderOperation$Builder:withSelection	(Ljava/lang/String;[Ljava/lang/String;)Landroid/content/ContentProviderOperation$Builder;
    //   1812: ldc 52
    //   1814: aload_0
    //   1815: aload_0
    //   1816: aload 89
    //   1818: ldc 161
    //   1820: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   1823: invokespecial 640	org/apache/cordova/ContactAccessorSdk5:getAddressType	(Ljava/lang/String;)I
    //   1826: invokestatic 438	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   1829: invokevirtual 356	android/content/ContentProviderOperation$Builder:withValue	(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
    //   1832: ldc 42
    //   1834: aload_0
    //   1835: aload 89
    //   1837: ldc 170
    //   1839: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   1842: invokevirtual 356	android/content/ContentProviderOperation$Builder:withValue	(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
    //   1845: ldc 60
    //   1847: aload_0
    //   1848: aload 89
    //   1850: ldc 172
    //   1852: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   1855: invokevirtual 356	android/content/ContentProviderOperation$Builder:withValue	(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
    //   1858: ldc 84
    //   1860: aload_0
    //   1861: aload 89
    //   1863: ldc 174
    //   1865: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   1868: invokevirtual 356	android/content/ContentProviderOperation$Builder:withValue	(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
    //   1871: ldc 88
    //   1873: aload_0
    //   1874: aload 89
    //   1876: ldc 176
    //   1878: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   1881: invokevirtual 356	android/content/ContentProviderOperation$Builder:withValue	(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
    //   1884: ldc 92
    //   1886: aload_0
    //   1887: aload 89
    //   1889: ldc 178
    //   1891: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   1894: invokevirtual 356	android/content/ContentProviderOperation$Builder:withValue	(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
    //   1897: ldc 96
    //   1899: aload_0
    //   1900: aload 89
    //   1902: ldc 180
    //   1904: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   1907: invokevirtual 356	android/content/ContentProviderOperation$Builder:withValue	(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
    //   1910: invokevirtual 362	android/content/ContentProviderOperation$Builder:build	()Landroid/content/ContentProviderOperation;
    //   1913: invokevirtual 281	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   1916: pop
    //   1917: goto +1038 -> 2955
    //   1920: astore 14
    //   1922: ldc 182
    //   1924: ldc_w 419
    //   1927: invokestatic 407	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   1930: pop
    //   1931: goto -1403 -> 528
    //   1934: iconst_0
    //   1935: istore 76
    //   1937: aload 75
    //   1939: invokevirtual 311	org/json/JSONArray:length	()I
    //   1942: istore 77
    //   1944: iload 76
    //   1946: iload 77
    //   1948: if_icmpge -1334 -> 614
    //   1951: aload 75
    //   1953: iload 76
    //   1955: invokevirtual 397	org/json/JSONArray:get	(I)Ljava/lang/Object;
    //   1958: checkcast 138	org/json/JSONObject
    //   1961: astore 78
    //   1963: aload_0
    //   1964: aload 78
    //   1966: ldc 26
    //   1968: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   1971: astore 79
    //   1973: aload 79
    //   1975: ifnonnull +128 -> 2103
    //   1978: new 699	android/content/ContentValues
    //   1981: dup
    //   1982: invokespecial 700	android/content/ContentValues:<init>	()V
    //   1985: astore 80
    //   1987: aload 80
    //   1989: ldc_w 374
    //   1992: iload 5
    //   1994: invokestatic 438	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   1997: invokevirtual 703	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   2000: aload 80
    //   2002: ldc_w 285
    //   2005: ldc_w 301
    //   2008: invokevirtual 706	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   2011: aload 80
    //   2013: ldc 52
    //   2015: aload_0
    //   2016: aload_0
    //   2017: aload 78
    //   2019: ldc 161
    //   2021: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   2024: invokespecial 644	org/apache/cordova/ContactAccessorSdk5:getOrgType	(Ljava/lang/String;)I
    //   2027: invokestatic 438	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   2030: invokevirtual 703	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   2033: aload 80
    //   2035: ldc 56
    //   2037: aload_0
    //   2038: aload 78
    //   2040: ldc_w 646
    //   2043: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   2046: invokevirtual 706	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   2049: aload 80
    //   2051: ldc 42
    //   2053: aload_0
    //   2054: aload 78
    //   2056: ldc 40
    //   2058: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   2061: invokevirtual 706	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   2064: aload 80
    //   2066: ldc 60
    //   2068: aload_0
    //   2069: aload 78
    //   2071: ldc_w 648
    //   2074: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   2077: invokevirtual 706	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   2080: aload 6
    //   2082: getstatic 372	android/provider/ContactsContract$Data:CONTENT_URI	Landroid/net/Uri;
    //   2085: invokestatic 348	android/content/ContentProviderOperation:newInsert	(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;
    //   2088: aload 80
    //   2090: invokevirtual 710	android/content/ContentProviderOperation$Builder:withValues	(Landroid/content/ContentValues;)Landroid/content/ContentProviderOperation$Builder;
    //   2093: invokevirtual 362	android/content/ContentProviderOperation$Builder:build	()Landroid/content/ContentProviderOperation;
    //   2096: invokevirtual 281	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   2099: pop
    //   2100: goto +861 -> 2961
    //   2103: aload 6
    //   2105: getstatic 372	android/provider/ContactsContract$Data:CONTENT_URI	Landroid/net/Uri;
    //   2108: invokestatic 669	android/content/ContentProviderOperation:newUpdate	(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;
    //   2111: ldc_w 712
    //   2114: iconst_2
    //   2115: anewarray 203	java/lang/String
    //   2118: dup
    //   2119: iconst_0
    //   2120: aload 79
    //   2122: aastore
    //   2123: dup
    //   2124: iconst_1
    //   2125: ldc_w 301
    //   2128: aastore
    //   2129: invokevirtual 678	android/content/ContentProviderOperation$Builder:withSelection	(Ljava/lang/String;[Ljava/lang/String;)Landroid/content/ContentProviderOperation$Builder;
    //   2132: ldc 52
    //   2134: aload_0
    //   2135: aload_0
    //   2136: aload 78
    //   2138: ldc 161
    //   2140: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   2143: invokespecial 644	org/apache/cordova/ContactAccessorSdk5:getOrgType	(Ljava/lang/String;)I
    //   2146: invokestatic 438	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   2149: invokevirtual 356	android/content/ContentProviderOperation$Builder:withValue	(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
    //   2152: ldc 56
    //   2154: aload_0
    //   2155: aload 78
    //   2157: ldc_w 646
    //   2160: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   2163: invokevirtual 356	android/content/ContentProviderOperation$Builder:withValue	(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
    //   2166: ldc 42
    //   2168: aload_0
    //   2169: aload 78
    //   2171: ldc 40
    //   2173: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   2176: invokevirtual 356	android/content/ContentProviderOperation$Builder:withValue	(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
    //   2179: ldc 60
    //   2181: aload_0
    //   2182: aload 78
    //   2184: ldc_w 648
    //   2187: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   2190: invokevirtual 356	android/content/ContentProviderOperation$Builder:withValue	(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
    //   2193: invokevirtual 362	android/content/ContentProviderOperation$Builder:build	()Landroid/content/ContentProviderOperation;
    //   2196: invokevirtual 281	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   2199: pop
    //   2200: goto +761 -> 2961
    //   2203: astore 16
    //   2205: ldc 182
    //   2207: ldc_w 424
    //   2210: invokestatic 407	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   2213: pop
    //   2214: goto -1600 -> 614
    //   2217: iconst_0
    //   2218: istore 65
    //   2220: aload 64
    //   2222: invokevirtual 311	org/json/JSONArray:length	()I
    //   2225: istore 66
    //   2227: iload 65
    //   2229: iload 66
    //   2231: if_icmpge -1531 -> 700
    //   2234: aload 64
    //   2236: iload 65
    //   2238: invokevirtual 397	org/json/JSONArray:get	(I)Ljava/lang/Object;
    //   2241: checkcast 138	org/json/JSONObject
    //   2244: astore 67
    //   2246: aload_0
    //   2247: aload 67
    //   2249: ldc 26
    //   2251: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   2254: astore 68
    //   2256: aload 68
    //   2258: ifnonnull +97 -> 2355
    //   2261: new 699	android/content/ContentValues
    //   2264: dup
    //   2265: invokespecial 700	android/content/ContentValues:<init>	()V
    //   2268: astore 69
    //   2270: aload 69
    //   2272: ldc_w 374
    //   2275: iload 5
    //   2277: invokestatic 438	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   2280: invokevirtual 703	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   2283: aload 69
    //   2285: ldc_w 285
    //   2288: ldc_w 299
    //   2291: invokevirtual 706	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   2294: aload 69
    //   2296: ldc 42
    //   2298: aload_0
    //   2299: aload 67
    //   2301: ldc_w 480
    //   2304: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   2307: invokevirtual 706	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   2310: aload 69
    //   2312: ldc 52
    //   2314: aload_0
    //   2315: aload_0
    //   2316: aload 67
    //   2318: ldc 161
    //   2320: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   2323: invokespecial 635	org/apache/cordova/ContactAccessorSdk5:getImType	(Ljava/lang/String;)I
    //   2326: invokestatic 438	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   2329: invokevirtual 703	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   2332: aload 6
    //   2334: getstatic 372	android/provider/ContactsContract$Data:CONTENT_URI	Landroid/net/Uri;
    //   2337: invokestatic 348	android/content/ContentProviderOperation:newInsert	(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;
    //   2340: aload 69
    //   2342: invokevirtual 710	android/content/ContentProviderOperation$Builder:withValues	(Landroid/content/ContentValues;)Landroid/content/ContentProviderOperation$Builder;
    //   2345: invokevirtual 362	android/content/ContentProviderOperation$Builder:build	()Landroid/content/ContentProviderOperation;
    //   2348: invokevirtual 281	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   2351: pop
    //   2352: goto +615 -> 2967
    //   2355: aload 6
    //   2357: getstatic 372	android/provider/ContactsContract$Data:CONTENT_URI	Landroid/net/Uri;
    //   2360: invokestatic 669	android/content/ContentProviderOperation:newUpdate	(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;
    //   2363: ldc_w 712
    //   2366: iconst_2
    //   2367: anewarray 203	java/lang/String
    //   2370: dup
    //   2371: iconst_0
    //   2372: aload 68
    //   2374: aastore
    //   2375: dup
    //   2376: iconst_1
    //   2377: ldc_w 299
    //   2380: aastore
    //   2381: invokevirtual 678	android/content/ContentProviderOperation$Builder:withSelection	(Ljava/lang/String;[Ljava/lang/String;)Landroid/content/ContentProviderOperation$Builder;
    //   2384: ldc 42
    //   2386: aload_0
    //   2387: aload 67
    //   2389: ldc_w 480
    //   2392: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   2395: invokevirtual 356	android/content/ContentProviderOperation$Builder:withValue	(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
    //   2398: ldc 52
    //   2400: aload_0
    //   2401: aload_0
    //   2402: aload 67
    //   2404: ldc 161
    //   2406: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   2409: invokespecial 642	org/apache/cordova/ContactAccessorSdk5:getContactType	(Ljava/lang/String;)I
    //   2412: invokestatic 438	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   2415: invokevirtual 356	android/content/ContentProviderOperation$Builder:withValue	(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
    //   2418: invokevirtual 362	android/content/ContentProviderOperation$Builder:build	()Landroid/content/ContentProviderOperation;
    //   2421: invokevirtual 281	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   2424: pop
    //   2425: goto +542 -> 2967
    //   2428: astore 18
    //   2430: ldc 182
    //   2432: ldc_w 414
    //   2435: invokestatic 407	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   2438: pop
    //   2439: goto -1739 -> 700
    //   2442: iconst_0
    //   2443: istore 52
    //   2445: aload 51
    //   2447: invokevirtual 311	org/json/JSONArray:length	()I
    //   2450: istore 53
    //   2452: iload 52
    //   2454: iload 53
    //   2456: if_icmpge -1554 -> 902
    //   2459: aload 51
    //   2461: iload 52
    //   2463: invokevirtual 397	org/json/JSONArray:get	(I)Ljava/lang/Object;
    //   2466: checkcast 138	org/json/JSONObject
    //   2469: astore 54
    //   2471: aload_0
    //   2472: aload 54
    //   2474: ldc 26
    //   2476: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   2479: astore 55
    //   2481: aload 55
    //   2483: ifnonnull +97 -> 2580
    //   2486: new 699	android/content/ContentValues
    //   2489: dup
    //   2490: invokespecial 700	android/content/ContentValues:<init>	()V
    //   2493: astore 56
    //   2495: aload 56
    //   2497: ldc_w 374
    //   2500: iload 5
    //   2502: invokestatic 438	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   2505: invokevirtual 703	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   2508: aload 56
    //   2510: ldc_w 285
    //   2513: ldc_w 305
    //   2516: invokevirtual 706	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   2519: aload 56
    //   2521: ldc 42
    //   2523: aload_0
    //   2524: aload 54
    //   2526: ldc_w 480
    //   2529: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   2532: invokevirtual 706	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   2535: aload 56
    //   2537: ldc 52
    //   2539: aload_0
    //   2540: aload_0
    //   2541: aload 54
    //   2543: ldc 161
    //   2545: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   2548: invokespecial 642	org/apache/cordova/ContactAccessorSdk5:getContactType	(Ljava/lang/String;)I
    //   2551: invokestatic 438	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   2554: invokevirtual 703	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   2557: aload 6
    //   2559: getstatic 372	android/provider/ContactsContract$Data:CONTENT_URI	Landroid/net/Uri;
    //   2562: invokestatic 348	android/content/ContentProviderOperation:newInsert	(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;
    //   2565: aload 56
    //   2567: invokevirtual 710	android/content/ContentProviderOperation$Builder:withValues	(Landroid/content/ContentValues;)Landroid/content/ContentProviderOperation$Builder;
    //   2570: invokevirtual 362	android/content/ContentProviderOperation$Builder:build	()Landroid/content/ContentProviderOperation;
    //   2573: invokevirtual 281	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   2576: pop
    //   2577: goto +396 -> 2973
    //   2580: aload 6
    //   2582: getstatic 372	android/provider/ContactsContract$Data:CONTENT_URI	Landroid/net/Uri;
    //   2585: invokestatic 669	android/content/ContentProviderOperation:newUpdate	(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;
    //   2588: ldc_w 712
    //   2591: iconst_2
    //   2592: anewarray 203	java/lang/String
    //   2595: dup
    //   2596: iconst_0
    //   2597: aload 55
    //   2599: aastore
    //   2600: dup
    //   2601: iconst_1
    //   2602: ldc_w 305
    //   2605: aastore
    //   2606: invokevirtual 678	android/content/ContentProviderOperation$Builder:withSelection	(Ljava/lang/String;[Ljava/lang/String;)Landroid/content/ContentProviderOperation$Builder;
    //   2609: ldc 42
    //   2611: aload_0
    //   2612: aload 54
    //   2614: ldc_w 480
    //   2617: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   2620: invokevirtual 356	android/content/ContentProviderOperation$Builder:withValue	(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
    //   2623: ldc 52
    //   2625: aload_0
    //   2626: aload_0
    //   2627: aload 54
    //   2629: ldc 161
    //   2631: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   2634: invokespecial 642	org/apache/cordova/ContactAccessorSdk5:getContactType	(Ljava/lang/String;)I
    //   2637: invokestatic 438	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   2640: invokevirtual 356	android/content/ContentProviderOperation$Builder:withValue	(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
    //   2643: invokevirtual 362	android/content/ContentProviderOperation$Builder:build	()Landroid/content/ContentProviderOperation;
    //   2646: invokevirtual 281	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   2649: pop
    //   2650: goto +323 -> 2973
    //   2653: astore 23
    //   2655: ldc 182
    //   2657: ldc_w 432
    //   2660: invokestatic 407	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   2663: pop
    //   2664: goto -1762 -> 902
    //   2667: iconst_0
    //   2668: istore 37
    //   2670: aload 36
    //   2672: invokevirtual 311	org/json/JSONArray:length	()I
    //   2675: istore 38
    //   2677: iload 37
    //   2679: iload 38
    //   2681: if_icmpge -1605 -> 1076
    //   2684: aload 36
    //   2686: iload 37
    //   2688: invokevirtual 397	org/json/JSONArray:get	(I)Ljava/lang/Object;
    //   2691: checkcast 138	org/json/JSONObject
    //   2694: astore 39
    //   2696: aload_0
    //   2697: aload 39
    //   2699: ldc 26
    //   2701: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   2704: astore 40
    //   2706: aload_0
    //   2707: aload_0
    //   2708: aload 39
    //   2710: ldc_w 480
    //   2713: invokevirtual 384	org/apache/cordova/ContactAccessorSdk5:getJsonString	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   2716: invokespecial 652	org/apache/cordova/ContactAccessorSdk5:getPhotoBytes	(Ljava/lang/String;)[B
    //   2719: astore 41
    //   2721: aload 40
    //   2723: ifnonnull +80 -> 2803
    //   2726: new 699	android/content/ContentValues
    //   2729: dup
    //   2730: invokespecial 700	android/content/ContentValues:<init>	()V
    //   2733: astore 42
    //   2735: aload 42
    //   2737: ldc_w 374
    //   2740: iload 5
    //   2742: invokestatic 438	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   2745: invokevirtual 703	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   2748: aload 42
    //   2750: ldc_w 285
    //   2753: ldc 118
    //   2755: invokevirtual 706	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   2758: aload 42
    //   2760: ldc_w 654
    //   2763: iconst_1
    //   2764: invokestatic 438	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   2767: invokevirtual 703	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   2770: aload 42
    //   2772: ldc_w 656
    //   2775: aload 41
    //   2777: invokevirtual 715	android/content/ContentValues:put	(Ljava/lang/String;[B)V
    //   2780: aload 6
    //   2782: getstatic 372	android/provider/ContactsContract$Data:CONTENT_URI	Landroid/net/Uri;
    //   2785: invokestatic 348	android/content/ContentProviderOperation:newInsert	(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;
    //   2788: aload 42
    //   2790: invokevirtual 710	android/content/ContentProviderOperation$Builder:withValues	(Landroid/content/ContentValues;)Landroid/content/ContentProviderOperation$Builder;
    //   2793: invokevirtual 362	android/content/ContentProviderOperation$Builder:build	()Landroid/content/ContentProviderOperation;
    //   2796: invokevirtual 281	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   2799: pop
    //   2800: goto +179 -> 2979
    //   2803: aload 6
    //   2805: getstatic 372	android/provider/ContactsContract$Data:CONTENT_URI	Landroid/net/Uri;
    //   2808: invokestatic 669	android/content/ContentProviderOperation:newUpdate	(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;
    //   2811: ldc_w 712
    //   2814: iconst_2
    //   2815: anewarray 203	java/lang/String
    //   2818: dup
    //   2819: iconst_0
    //   2820: aload 40
    //   2822: aastore
    //   2823: dup
    //   2824: iconst_1
    //   2825: ldc 118
    //   2827: aastore
    //   2828: invokevirtual 678	android/content/ContentProviderOperation$Builder:withSelection	(Ljava/lang/String;[Ljava/lang/String;)Landroid/content/ContentProviderOperation$Builder;
    //   2831: ldc_w 654
    //   2834: iconst_1
    //   2835: invokestatic 438	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   2838: invokevirtual 356	android/content/ContentProviderOperation$Builder:withValue	(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
    //   2841: ldc_w 656
    //   2844: aload 41
    //   2846: invokevirtual 356	android/content/ContentProviderOperation$Builder:withValue	(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
    //   2849: invokevirtual 362	android/content/ContentProviderOperation$Builder:build	()Landroid/content/ContentProviderOperation;
    //   2852: invokevirtual 281	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   2855: pop
    //   2856: goto +123 -> 2979
    //   2859: astore 26
    //   2861: ldc 182
    //   2863: ldc_w 445
    //   2866: invokestatic 407	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   2869: pop
    //   2870: goto -1794 -> 1076
    //   2873: astore 32
    //   2875: ldc 182
    //   2877: aload 32
    //   2879: invokevirtual 476	android/os/RemoteException:getMessage	()Ljava/lang/String;
    //   2882: aload 32
    //   2884: invokestatic 192	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   2887: pop
    //   2888: ldc 182
    //   2890: aload 32
    //   2892: invokestatic 719	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   2895: aload 32
    //   2897: invokestatic 192	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   2900: pop
    //   2901: iconst_0
    //   2902: istore 28
    //   2904: goto -1804 -> 1100
    //   2907: astore 29
    //   2909: ldc 182
    //   2911: aload 29
    //   2913: invokevirtual 477	android/content/OperationApplicationException:getMessage	()Ljava/lang/String;
    //   2916: aload 29
    //   2918: invokestatic 192	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   2921: pop
    //   2922: ldc 182
    //   2924: aload 29
    //   2926: invokestatic 719	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   2929: aload 29
    //   2931: invokestatic 192	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   2934: pop
    //   2935: iconst_0
    //   2936: istore 28
    //   2938: goto -1838 -> 1100
    //   2941: aconst_null
    //   2942: areturn
    //   2943: iinc 109 1
    //   2946: goto -1822 -> 1124
    //   2949: iinc 98 1
    //   2952: goto -1603 -> 1349
    //   2955: iinc 87 1
    //   2958: goto -1384 -> 1574
    //   2961: iinc 76 1
    //   2964: goto -1027 -> 1937
    //   2967: iinc 65 1
    //   2970: goto -750 -> 2220
    //   2973: iinc 52 1
    //   2976: goto -531 -> 2445
    //   2979: iinc 37 1
    //   2982: goto -312 -> 2670
    //
    // Exception table:
    //   from	to	target	type
    //   59	76	1107	org/json/JSONException
    //   86	114	1107	org/json/JSONException
    //   119	129	1107	org/json/JSONException
    //   129	140	1107	org/json/JSONException
    //   145	155	1107	org/json/JSONException
    //   155	166	1107	org/json/JSONException
    //   171	181	1107	org/json/JSONException
    //   181	192	1107	org/json/JSONException
    //   197	207	1107	org/json/JSONException
    //   207	218	1107	org/json/JSONException
    //   223	233	1107	org/json/JSONException
    //   233	244	1107	org/json/JSONException
    //   249	259	1107	org/json/JSONException
    //   259	270	1107	org/json/JSONException
    //   270	278	1332	org/json/JSONException
    //   283	356	1332	org/json/JSONException
    //   1124	1131	1332	org/json/JSONException
    //   1138	1160	1332	org/json/JSONException
    //   1165	1256	1332	org/json/JSONException
    //   1259	1329	1332	org/json/JSONException
    //   356	364	1557	org/json/JSONException
    //   369	442	1557	org/json/JSONException
    //   1349	1356	1557	org/json/JSONException
    //   1363	1385	1557	org/json/JSONException
    //   1390	1481	1557	org/json/JSONException
    //   1484	1554	1557	org/json/JSONException
    //   442	450	1920	org/json/JSONException
    //   455	528	1920	org/json/JSONException
    //   1574	1581	1920	org/json/JSONException
    //   1588	1610	1920	org/json/JSONException
    //   1615	1780	1920	org/json/JSONException
    //   1783	1917	1920	org/json/JSONException
    //   528	536	2203	org/json/JSONException
    //   541	614	2203	org/json/JSONException
    //   1937	1944	2203	org/json/JSONException
    //   1951	1973	2203	org/json/JSONException
    //   1978	2100	2203	org/json/JSONException
    //   2103	2200	2203	org/json/JSONException
    //   614	622	2428	org/json/JSONException
    //   627	700	2428	org/json/JSONException
    //   2220	2227	2428	org/json/JSONException
    //   2234	2256	2428	org/json/JSONException
    //   2261	2352	2428	org/json/JSONException
    //   2355	2425	2428	org/json/JSONException
    //   807	815	2653	org/json/JSONException
    //   820	902	2653	org/json/JSONException
    //   2445	2452	2653	org/json/JSONException
    //   2459	2481	2653	org/json/JSONException
    //   2486	2577	2653	org/json/JSONException
    //   2580	2650	2653	org/json/JSONException
    //   990	999	2859	org/json/JSONException
    //   1004	1076	2859	org/json/JSONException
    //   2670	2677	2859	org/json/JSONException
    //   2684	2721	2859	org/json/JSONException
    //   2726	2800	2859	org/json/JSONException
    //   2803	2856	2859	org/json/JSONException
    //   1079	1100	2873	android/os/RemoteException
    //   1079	1100	2907	android/content/OperationApplicationException
  }

  private JSONObject nameQuery(Cursor paramCursor)
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      String str1 = paramCursor.getString(paramCursor.getColumnIndex("data3"));
      String str2 = paramCursor.getString(paramCursor.getColumnIndex("data2"));
      String str3 = paramCursor.getString(paramCursor.getColumnIndex("data5"));
      String str4 = paramCursor.getString(paramCursor.getColumnIndex("data4"));
      String str5 = paramCursor.getString(paramCursor.getColumnIndex("data6"));
      StringBuffer localStringBuffer = new StringBuffer("");
      if (str4 != null)
        localStringBuffer.append(str4 + " ");
      if (str2 != null)
        localStringBuffer.append(str2 + " ");
      if (str3 != null)
        localStringBuffer.append(str3 + " ");
      if (str1 != null)
        localStringBuffer.append(str1);
      if (str5 != null)
        localStringBuffer.append(" " + str5);
      localJSONObject.put("familyName", str1);
      localJSONObject.put("givenName", str2);
      localJSONObject.put("middleName", str3);
      localJSONObject.put("honorificPrefix", str4);
      localJSONObject.put("honorificSuffix", str5);
      localJSONObject.put("formatted", localStringBuffer);
      return localJSONObject;
    }
    catch (JSONException localJSONException)
    {
      Log.e("ContactsAccessor", localJSONException.getMessage(), localJSONException);
    }
    return localJSONObject;
  }

  private JSONObject organizationQuery(Cursor paramCursor)
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("id", paramCursor.getString(paramCursor.getColumnIndex("_id")));
      localJSONObject.put("pref", false);
      localJSONObject.put("type", getOrgType(paramCursor.getInt(paramCursor.getColumnIndex("data2"))));
      localJSONObject.put("department", paramCursor.getString(paramCursor.getColumnIndex("data5")));
      localJSONObject.put("name", paramCursor.getString(paramCursor.getColumnIndex("data1")));
      localJSONObject.put("title", paramCursor.getString(paramCursor.getColumnIndex("data4")));
      return localJSONObject;
    }
    catch (JSONException localJSONException)
    {
      Log.e("ContactsAccessor", localJSONException.getMessage(), localJSONException);
    }
    return localJSONObject;
  }

  private JSONObject phoneQuery(Cursor paramCursor)
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("id", paramCursor.getString(paramCursor.getColumnIndex("_id")));
      localJSONObject.put("pref", false);
      localJSONObject.put("value", paramCursor.getString(paramCursor.getColumnIndex("data1")));
      localJSONObject.put("type", getPhoneType(paramCursor.getInt(paramCursor.getColumnIndex("data2"))));
      return localJSONObject;
    }
    catch (JSONException localJSONException)
    {
      Log.e("ContactsAccessor", localJSONException.getMessage(), localJSONException);
      return localJSONObject;
    }
    catch (Exception localException)
    {
      Log.e("ContactsAccessor", localException.getMessage(), localException);
    }
    return localJSONObject;
  }

  private JSONObject photoQuery(Cursor paramCursor, String paramString)
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("id", paramCursor.getString(paramCursor.getColumnIndex("_id")));
      localJSONObject.put("pref", false);
      localJSONObject.put("type", "url");
      localJSONObject.put("value", Uri.withAppendedPath(ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(paramString).longValue()), "photo").toString());
      return localJSONObject;
    }
    catch (JSONException localJSONException)
    {
      Log.e("ContactsAccessor", localJSONException.getMessage(), localJSONException);
    }
    return localJSONObject;
  }

  private JSONObject populateContact(JSONObject paramJSONObject, JSONArray paramJSONArray1, JSONArray paramJSONArray2, JSONArray paramJSONArray3, JSONArray paramJSONArray4, JSONArray paramJSONArray5, JSONArray paramJSONArray6, JSONArray paramJSONArray7)
  {
    try
    {
      if (paramJSONArray1.length() > 0)
        paramJSONObject.put("organizations", paramJSONArray1);
      if (paramJSONArray2.length() > 0)
        paramJSONObject.put("addresses", paramJSONArray2);
      if (paramJSONArray3.length() > 0)
        paramJSONObject.put("phoneNumbers", paramJSONArray3);
      if (paramJSONArray4.length() > 0)
        paramJSONObject.put("emails", paramJSONArray4);
      if (paramJSONArray5.length() > 0)
        paramJSONObject.put("ims", paramJSONArray5);
      if (paramJSONArray6.length() > 0)
        paramJSONObject.put("urls", paramJSONArray6);
      if (paramJSONArray7.length() > 0)
        paramJSONObject.put("photos", paramJSONArray7);
      return paramJSONObject;
    }
    catch (JSONException localJSONException)
    {
      Log.e("ContactsAccessor", localJSONException.getMessage(), localJSONException);
    }
    return paramJSONObject;
  }

  // ERROR //
  private JSONArray populateContactArray(int paramInt, HashMap<String, java.lang.Boolean> paramHashMap, Cursor paramCursor)
  {
    // Byte code:
    //   0: ldc_w 683
    //   3: astore 4
    //   5: ldc_w 683
    //   8: astore 5
    //   10: iconst_1
    //   11: istore 6
    //   13: new 307	org/json/JSONArray
    //   16: dup
    //   17: invokespecial 763	org/json/JSONArray:<init>	()V
    //   20: astore 7
    //   22: new 138	org/json/JSONObject
    //   25: dup
    //   26: invokespecial 139	org/json/JSONObject:<init>	()V
    //   29: astore 8
    //   31: new 307	org/json/JSONArray
    //   34: dup
    //   35: invokespecial 763	org/json/JSONArray:<init>	()V
    //   38: astore 9
    //   40: new 307	org/json/JSONArray
    //   43: dup
    //   44: invokespecial 763	org/json/JSONArray:<init>	()V
    //   47: astore 10
    //   49: new 307	org/json/JSONArray
    //   52: dup
    //   53: invokespecial 763	org/json/JSONArray:<init>	()V
    //   56: astore 11
    //   58: new 307	org/json/JSONArray
    //   61: dup
    //   62: invokespecial 763	org/json/JSONArray:<init>	()V
    //   65: astore 12
    //   67: new 307	org/json/JSONArray
    //   70: dup
    //   71: invokespecial 763	org/json/JSONArray:<init>	()V
    //   74: astore 13
    //   76: new 307	org/json/JSONArray
    //   79: dup
    //   80: invokespecial 763	org/json/JSONArray:<init>	()V
    //   83: astore 14
    //   85: new 307	org/json/JSONArray
    //   88: dup
    //   89: invokespecial 763	org/json/JSONArray:<init>	()V
    //   92: astore 15
    //   94: aload_3
    //   95: ldc 28
    //   97: invokeinterface 147 2 0
    //   102: istore 16
    //   104: aload_3
    //   105: ldc_w 374
    //   108: invokeinterface 147 2 0
    //   113: istore 17
    //   115: aload_3
    //   116: ldc_w 285
    //   119: invokeinterface 147 2 0
    //   124: istore 18
    //   126: aload_3
    //   127: ldc 42
    //   129: invokeinterface 147 2 0
    //   134: istore 19
    //   136: aload_3
    //   137: ldc 42
    //   139: invokeinterface 147 2 0
    //   144: istore 20
    //   146: aload_3
    //   147: ldc 42
    //   149: invokeinterface 147 2 0
    //   154: istore 21
    //   156: aload_3
    //   157: ldc 42
    //   159: invokeinterface 147 2 0
    //   164: istore 22
    //   166: aload_3
    //   167: ldc 52
    //   169: invokeinterface 147 2 0
    //   174: istore 23
    //   176: aload_3
    //   177: invokeinterface 766 1 0
    //   182: ifle +733 -> 915
    //   185: aload_3
    //   186: invokeinterface 769 1 0
    //   191: ifeq +689 -> 880
    //   194: aload 7
    //   196: invokevirtual 311	org/json/JSONArray:length	()I
    //   199: iload_1
    //   200: iconst_1
    //   201: isub
    //   202: if_icmpgt +678 -> 880
    //   205: aload_3
    //   206: iload 16
    //   208: invokeinterface 151 2 0
    //   213: astore 4
    //   215: aload_3
    //   216: iload 17
    //   218: invokeinterface 151 2 0
    //   223: astore 27
    //   225: aload_3
    //   226: invokeinterface 772 1 0
    //   231: ifne +7 -> 238
    //   234: aload 4
    //   236: astore 5
    //   238: aload 5
    //   240: aload 4
    //   242: invokevirtual 207	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   245: ifne +136 -> 381
    //   248: aload 7
    //   250: aload_0
    //   251: aload 8
    //   253: aload 9
    //   255: aload 10
    //   257: aload 11
    //   259: aload 12
    //   261: aload 13
    //   263: aload 14
    //   265: aload 15
    //   267: invokespecial 774	org/apache/cordova/ContactAccessorSdk5:populateContact	(Lorg/json/JSONObject;Lorg/json/JSONArray;Lorg/json/JSONArray;Lorg/json/JSONArray;Lorg/json/JSONArray;Lorg/json/JSONArray;Lorg/json/JSONArray;Lorg/json/JSONArray;)Lorg/json/JSONObject;
    //   270: invokevirtual 777	org/json/JSONArray:put	(Ljava/lang/Object;)Lorg/json/JSONArray;
    //   273: pop
    //   274: new 138	org/json/JSONObject
    //   277: dup
    //   278: invokespecial 139	org/json/JSONObject:<init>	()V
    //   281: astore 44
    //   283: new 307	org/json/JSONArray
    //   286: dup
    //   287: invokespecial 763	org/json/JSONArray:<init>	()V
    //   290: astore 45
    //   292: new 307	org/json/JSONArray
    //   295: dup
    //   296: invokespecial 763	org/json/JSONArray:<init>	()V
    //   299: astore 46
    //   301: new 307	org/json/JSONArray
    //   304: dup
    //   305: invokespecial 763	org/json/JSONArray:<init>	()V
    //   308: astore 47
    //   310: new 307	org/json/JSONArray
    //   313: dup
    //   314: invokespecial 763	org/json/JSONArray:<init>	()V
    //   317: astore 48
    //   319: new 307	org/json/JSONArray
    //   322: dup
    //   323: invokespecial 763	org/json/JSONArray:<init>	()V
    //   326: astore 49
    //   328: new 307	org/json/JSONArray
    //   331: dup
    //   332: invokespecial 763	org/json/JSONArray:<init>	()V
    //   335: astore 50
    //   337: new 307	org/json/JSONArray
    //   340: dup
    //   341: invokespecial 763	org/json/JSONArray:<init>	()V
    //   344: astore 51
    //   346: iconst_1
    //   347: istore 6
    //   349: aload 51
    //   351: astore 15
    //   353: aload 50
    //   355: astore 14
    //   357: aload 49
    //   359: astore 13
    //   361: aload 48
    //   363: astore 12
    //   365: aload 47
    //   367: astore 11
    //   369: aload 46
    //   371: astore 10
    //   373: aload 45
    //   375: astore 9
    //   377: aload 44
    //   379: astore 8
    //   381: iload 6
    //   383: ifeq +27 -> 410
    //   386: iconst_0
    //   387: istore 6
    //   389: aload 8
    //   391: ldc 26
    //   393: aload 4
    //   395: invokevirtual 154	org/json/JSONObject:put	(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
    //   398: pop
    //   399: aload 8
    //   401: ldc_w 662
    //   404: aload 27
    //   406: invokevirtual 154	org/json/JSONObject:put	(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
    //   409: pop
    //   410: aload_3
    //   411: iload 18
    //   413: invokeinterface 151 2 0
    //   418: astore 28
    //   420: aload 28
    //   422: ldc_w 289
    //   425: invokevirtual 207	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   428: ifeq +19 -> 447
    //   431: aload 8
    //   433: ldc 36
    //   435: aload_3
    //   436: iload 19
    //   438: invokeinterface 151 2 0
    //   443: invokevirtual 154	org/json/JSONObject:put	(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
    //   446: pop
    //   447: aload 28
    //   449: ldc_w 289
    //   452: invokevirtual 207	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   455: ifeq +29 -> 484
    //   458: aload_0
    //   459: ldc 40
    //   461: aload_2
    //   462: invokevirtual 781	org/apache/cordova/ContactAccessorSdk5:isRequired	(Ljava/lang/String;Ljava/util/HashMap;)Z
    //   465: ifeq +19 -> 484
    //   468: aload 8
    //   470: ldc 40
    //   472: aload_0
    //   473: aload_3
    //   474: invokespecial 783	org/apache/cordova/ContactAccessorSdk5:nameQuery	(Landroid/database/Cursor;)Lorg/json/JSONObject;
    //   477: invokevirtual 154	org/json/JSONObject:put	(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
    //   480: pop
    //   481: goto +590 -> 1071
    //   484: aload 28
    //   486: ldc_w 293
    //   489: invokevirtual 207	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   492: ifeq +45 -> 537
    //   495: aload_0
    //   496: ldc 68
    //   498: aload_2
    //   499: invokevirtual 781	org/apache/cordova/ContactAccessorSdk5:isRequired	(Ljava/lang/String;Ljava/util/HashMap;)Z
    //   502: ifeq +35 -> 537
    //   505: aload 11
    //   507: aload_0
    //   508: aload_3
    //   509: invokespecial 785	org/apache/cordova/ContactAccessorSdk5:phoneQuery	(Landroid/database/Cursor;)Lorg/json/JSONObject;
    //   512: invokevirtual 777	org/json/JSONArray:put	(Ljava/lang/Object;)Lorg/json/JSONArray;
    //   515: pop
    //   516: goto +555 -> 1071
    //   519: astore 25
    //   521: ldc 182
    //   523: aload 25
    //   525: invokevirtual 186	org/json/JSONException:getMessage	()Ljava/lang/String;
    //   528: aload 25
    //   530: invokestatic 192	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   533: pop
    //   534: goto +537 -> 1071
    //   537: aload 28
    //   539: ldc_w 295
    //   542: invokevirtual 207	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   545: ifeq +27 -> 572
    //   548: aload_0
    //   549: ldc 72
    //   551: aload_2
    //   552: invokevirtual 781	org/apache/cordova/ContactAccessorSdk5:isRequired	(Ljava/lang/String;Ljava/util/HashMap;)Z
    //   555: ifeq +17 -> 572
    //   558: aload 12
    //   560: aload_0
    //   561: aload_3
    //   562: invokespecial 787	org/apache/cordova/ContactAccessorSdk5:emailQuery	(Landroid/database/Cursor;)Lorg/json/JSONObject;
    //   565: invokevirtual 777	org/json/JSONArray:put	(Ljava/lang/Object;)Lorg/json/JSONArray;
    //   568: pop
    //   569: goto +502 -> 1071
    //   572: aload 28
    //   574: ldc_w 297
    //   577: invokevirtual 207	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   580: ifeq +27 -> 607
    //   583: aload_0
    //   584: ldc 76
    //   586: aload_2
    //   587: invokevirtual 781	org/apache/cordova/ContactAccessorSdk5:isRequired	(Ljava/lang/String;Ljava/util/HashMap;)Z
    //   590: ifeq +17 -> 607
    //   593: aload 10
    //   595: aload_0
    //   596: aload_3
    //   597: invokespecial 789	org/apache/cordova/ContactAccessorSdk5:addressQuery	(Landroid/database/Cursor;)Lorg/json/JSONObject;
    //   600: invokevirtual 777	org/json/JSONArray:put	(Ljava/lang/Object;)Lorg/json/JSONArray;
    //   603: pop
    //   604: goto +467 -> 1071
    //   607: aload 28
    //   609: ldc_w 301
    //   612: invokevirtual 207	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   615: ifeq +27 -> 642
    //   618: aload_0
    //   619: ldc 102
    //   621: aload_2
    //   622: invokevirtual 781	org/apache/cordova/ContactAccessorSdk5:isRequired	(Ljava/lang/String;Ljava/util/HashMap;)Z
    //   625: ifeq +17 -> 642
    //   628: aload 9
    //   630: aload_0
    //   631: aload_3
    //   632: invokespecial 791	org/apache/cordova/ContactAccessorSdk5:organizationQuery	(Landroid/database/Cursor;)Lorg/json/JSONObject;
    //   635: invokevirtual 777	org/json/JSONArray:put	(Ljava/lang/Object;)Lorg/json/JSONArray;
    //   638: pop
    //   639: goto +432 -> 1071
    //   642: aload 28
    //   644: ldc_w 299
    //   647: invokevirtual 207	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   650: ifeq +27 -> 677
    //   653: aload_0
    //   654: ldc 98
    //   656: aload_2
    //   657: invokevirtual 781	org/apache/cordova/ContactAccessorSdk5:isRequired	(Ljava/lang/String;Ljava/util/HashMap;)Z
    //   660: ifeq +17 -> 677
    //   663: aload 13
    //   665: aload_0
    //   666: aload_3
    //   667: invokespecial 793	org/apache/cordova/ContactAccessorSdk5:imQuery	(Landroid/database/Cursor;)Lorg/json/JSONObject;
    //   670: invokevirtual 777	org/json/JSONArray:put	(Ljava/lang/Object;)Lorg/json/JSONArray;
    //   673: pop
    //   674: goto +397 -> 1071
    //   677: aload 28
    //   679: ldc_w 303
    //   682: invokevirtual 207	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   685: ifeq +32 -> 717
    //   688: aload_0
    //   689: ldc 114
    //   691: aload_2
    //   692: invokevirtual 781	org/apache/cordova/ContactAccessorSdk5:isRequired	(Ljava/lang/String;Ljava/util/HashMap;)Z
    //   695: ifeq +22 -> 717
    //   698: aload 8
    //   700: ldc 114
    //   702: aload_3
    //   703: iload 20
    //   705: invokeinterface 151 2 0
    //   710: invokevirtual 154	org/json/JSONObject:put	(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
    //   713: pop
    //   714: goto +357 -> 1071
    //   717: aload 28
    //   719: ldc_w 291
    //   722: invokevirtual 207	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   725: ifeq +32 -> 757
    //   728: aload_0
    //   729: ldc 66
    //   731: aload_2
    //   732: invokevirtual 781	org/apache/cordova/ContactAccessorSdk5:isRequired	(Ljava/lang/String;Ljava/util/HashMap;)Z
    //   735: ifeq +22 -> 757
    //   738: aload 8
    //   740: ldc 66
    //   742: aload_3
    //   743: iload 21
    //   745: invokeinterface 151 2 0
    //   750: invokevirtual 154	org/json/JSONObject:put	(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
    //   753: pop
    //   754: goto +317 -> 1071
    //   757: aload 28
    //   759: ldc_w 305
    //   762: invokevirtual 207	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   765: ifeq +27 -> 792
    //   768: aload_0
    //   769: ldc 120
    //   771: aload_2
    //   772: invokevirtual 781	org/apache/cordova/ContactAccessorSdk5:isRequired	(Ljava/lang/String;Ljava/util/HashMap;)Z
    //   775: ifeq +17 -> 792
    //   778: aload 14
    //   780: aload_0
    //   781: aload_3
    //   782: invokespecial 796	org/apache/cordova/ContactAccessorSdk5:websiteQuery	(Landroid/database/Cursor;)Lorg/json/JSONObject;
    //   785: invokevirtual 777	org/json/JSONArray:put	(Ljava/lang/Object;)Lorg/json/JSONArray;
    //   788: pop
    //   789: goto +282 -> 1071
    //   792: aload 28
    //   794: ldc 112
    //   796: invokevirtual 207	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   799: ifeq +44 -> 843
    //   802: aload_0
    //   803: ldc 110
    //   805: aload_2
    //   806: invokevirtual 781	org/apache/cordova/ContactAccessorSdk5:isRequired	(Ljava/lang/String;Ljava/util/HashMap;)Z
    //   809: ifeq +262 -> 1071
    //   812: iconst_3
    //   813: aload_3
    //   814: iload 23
    //   816: invokeinterface 165 2 0
    //   821: if_icmpne +250 -> 1071
    //   824: aload 8
    //   826: ldc 110
    //   828: aload_3
    //   829: iload 22
    //   831: invokeinterface 151 2 0
    //   836: invokevirtual 154	org/json/JSONObject:put	(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
    //   839: pop
    //   840: goto +231 -> 1071
    //   843: aload 28
    //   845: ldc 118
    //   847: invokevirtual 207	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   850: ifeq +221 -> 1071
    //   853: aload_0
    //   854: ldc_w 440
    //   857: aload_2
    //   858: invokevirtual 781	org/apache/cordova/ContactAccessorSdk5:isRequired	(Ljava/lang/String;Ljava/util/HashMap;)Z
    //   861: ifeq +210 -> 1071
    //   864: aload 15
    //   866: aload_0
    //   867: aload_3
    //   868: aload 4
    //   870: invokespecial 798	org/apache/cordova/ContactAccessorSdk5:photoQuery	(Landroid/database/Cursor;Ljava/lang/String;)Lorg/json/JSONObject;
    //   873: invokevirtual 777	org/json/JSONArray:put	(Ljava/lang/Object;)Lorg/json/JSONArray;
    //   876: pop
    //   877: goto +194 -> 1071
    //   880: aload 7
    //   882: invokevirtual 311	org/json/JSONArray:length	()I
    //   885: iload_1
    //   886: if_icmpge +29 -> 915
    //   889: aload 7
    //   891: aload_0
    //   892: aload 8
    //   894: aload 9
    //   896: aload 10
    //   898: aload 11
    //   900: aload 12
    //   902: aload 13
    //   904: aload 14
    //   906: aload 15
    //   908: invokespecial 774	org/apache/cordova/ContactAccessorSdk5:populateContact	(Lorg/json/JSONObject;Lorg/json/JSONArray;Lorg/json/JSONArray;Lorg/json/JSONArray;Lorg/json/JSONArray;Lorg/json/JSONArray;Lorg/json/JSONArray;Lorg/json/JSONArray;)Lorg/json/JSONObject;
    //   911: invokevirtual 777	org/json/JSONArray:put	(Ljava/lang/Object;)Lorg/json/JSONArray;
    //   914: pop
    //   915: aload_3
    //   916: invokeinterface 799 1 0
    //   921: aload 7
    //   923: areturn
    //   924: astore 25
    //   926: aload 44
    //   928: astore 8
    //   930: goto -409 -> 521
    //   933: astore 25
    //   935: aload 45
    //   937: astore 9
    //   939: aload 44
    //   941: astore 8
    //   943: goto -422 -> 521
    //   946: astore 25
    //   948: aload 46
    //   950: astore 10
    //   952: aload 45
    //   954: astore 9
    //   956: aload 44
    //   958: astore 8
    //   960: goto -439 -> 521
    //   963: astore 25
    //   965: aload 47
    //   967: astore 11
    //   969: aload 46
    //   971: astore 10
    //   973: aload 45
    //   975: astore 9
    //   977: aload 44
    //   979: astore 8
    //   981: goto -460 -> 521
    //   984: astore 25
    //   986: aload 48
    //   988: astore 12
    //   990: aload 47
    //   992: astore 11
    //   994: aload 46
    //   996: astore 10
    //   998: aload 45
    //   1000: astore 9
    //   1002: aload 44
    //   1004: astore 8
    //   1006: goto -485 -> 521
    //   1009: astore 25
    //   1011: aload 49
    //   1013: astore 13
    //   1015: aload 48
    //   1017: astore 12
    //   1019: aload 47
    //   1021: astore 11
    //   1023: aload 46
    //   1025: astore 10
    //   1027: aload 45
    //   1029: astore 9
    //   1031: aload 44
    //   1033: astore 8
    //   1035: goto -514 -> 521
    //   1038: astore 25
    //   1040: aload 50
    //   1042: astore 14
    //   1044: aload 49
    //   1046: astore 13
    //   1048: aload 48
    //   1050: astore 12
    //   1052: aload 47
    //   1054: astore 11
    //   1056: aload 46
    //   1058: astore 10
    //   1060: aload 45
    //   1062: astore 9
    //   1064: aload 44
    //   1066: astore 8
    //   1068: goto -547 -> 521
    //   1071: aload 4
    //   1073: astore 5
    //   1075: goto -890 -> 185
    //
    // Exception table:
    //   from	to	target	type
    //   205	234	519	org/json/JSONException
    //   238	283	519	org/json/JSONException
    //   389	410	519	org/json/JSONException
    //   410	447	519	org/json/JSONException
    //   447	481	519	org/json/JSONException
    //   484	516	519	org/json/JSONException
    //   537	569	519	org/json/JSONException
    //   572	604	519	org/json/JSONException
    //   607	639	519	org/json/JSONException
    //   642	674	519	org/json/JSONException
    //   677	714	519	org/json/JSONException
    //   717	754	519	org/json/JSONException
    //   757	789	519	org/json/JSONException
    //   792	840	519	org/json/JSONException
    //   843	877	519	org/json/JSONException
    //   283	292	924	org/json/JSONException
    //   292	301	933	org/json/JSONException
    //   301	310	946	org/json/JSONException
    //   310	319	963	org/json/JSONException
    //   319	328	984	org/json/JSONException
    //   328	337	1009	org/json/JSONException
    //   337	346	1038	org/json/JSONException
  }

  private JSONObject websiteQuery(Cursor paramCursor)
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("id", paramCursor.getString(paramCursor.getColumnIndex("_id")));
      localJSONObject.put("pref", false);
      localJSONObject.put("value", paramCursor.getString(paramCursor.getColumnIndex("data1")));
      localJSONObject.put("type", getContactType(paramCursor.getInt(paramCursor.getColumnIndex("data2"))));
      return localJSONObject;
    }
    catch (JSONException localJSONException)
    {
      Log.e("ContactsAccessor", localJSONException.getMessage(), localJSONException);
    }
    return localJSONObject;
  }

  public JSONObject getContactById(String paramString)
    throws JSONException
  {
    Cursor localCursor = this.mApp.getActivity().getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, "contact_id = ? ", new String[] { paramString }, "contact_id ASC");
    JSONArray localJSONArray1 = new JSONArray();
    localJSONArray1.put("*");
    JSONArray localJSONArray2 = populateContactArray(1, buildPopulationSet(localJSONArray1), localCursor);
    int i = localJSONArray2.length();
    JSONObject localJSONObject = null;
    if (i == 1)
      localJSONObject = localJSONArray2.getJSONObject(0);
    return localJSONObject;
  }

  public boolean remove(String paramString)
  {
    Cursor localCursor = this.mApp.getActivity().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, "_id = ?", new String[] { paramString }, null);
    Uri localUri;
    if (localCursor.getCount() == 1)
    {
      localCursor.moveToFirst();
      String str = localCursor.getString(localCursor.getColumnIndex("lookup"));
      localUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, str);
    }
    for (int i = this.mApp.getActivity().getContentResolver().delete(localUri, null, null); i > 0; i = 0)
    {
      return true;
      Log.d("ContactsAccessor", "Could not find contact with ID");
    }
    return false;
  }

  public String save(JSONObject paramJSONObject)
  {
    Account[] arrayOfAccount = AccountManager.get(this.mApp.getActivity()).getAccounts();
    String str1;
    String str2;
    if (arrayOfAccount.length == 1)
    {
      str1 = arrayOfAccount[0].name;
      str2 = arrayOfAccount[0].type;
    }
    String str3;
    label270: label280: 
    while (true)
    {
      str3 = getJsonString(paramJSONObject, "id");
      if (str3 != null)
        break;
      return createNewContact(paramJSONObject, str2, str1);
      int i = arrayOfAccount.length;
      str1 = null;
      str2 = null;
      if (i <= 1)
        continue;
      int j = arrayOfAccount.length;
      int k = 0;
      label83: str1 = null;
      str2 = null;
      int i2;
      label155: int m;
      if (k < j)
      {
        Account localAccount3 = arrayOfAccount[k];
        if ((localAccount3.type.contains("eas")) && (localAccount3.name.matches(".+@.+\\.+.+")))
        {
          str1 = localAccount3.name;
          str2 = localAccount3.type;
        }
      }
      else
      {
        if (str1 == null)
        {
          int i1 = arrayOfAccount.length;
          i2 = 0;
          if (i2 < i1)
          {
            Account localAccount2 = arrayOfAccount[i2];
            if ((!localAccount2.type.contains("com.google")) || (!localAccount2.name.matches(".+@.+\\.+.+")))
              break label270;
            str1 = localAccount2.name;
            str2 = localAccount2.type;
          }
        }
        if (str1 != null)
          continue;
        m = arrayOfAccount.length;
      }
      for (int n = 0; ; n++)
      {
        if (n >= m)
          break label280;
        Account localAccount1 = arrayOfAccount[n];
        if (!localAccount1.name.matches(".+@.+\\.+.+"))
          continue;
        str1 = localAccount1.name;
        str2 = localAccount1.type;
        break;
        k++;
        break label83;
        i2++;
        break label155;
      }
    }
    return modifyContact(str3, paramJSONObject, str2, str1);
  }

  public JSONArray search(JSONArray paramJSONArray, JSONObject paramJSONObject)
  {
    int i = 2147483647;
    String str2;
    String str1;
    if (paramJSONObject != null)
    {
      str2 = paramJSONObject.optString("filter");
      if (str2.length() == 0)
        str1 = "%";
    }
    try
    {
      boolean bool = paramJSONObject.getBoolean("multiple");
      if (!bool)
        i = 1;
      label45: HashMap localHashMap;
      Cursor localCursor1;
      HashSet localHashSet1;
      while (true)
      {
        localHashMap = buildPopulationSet(paramJSONArray);
        ContactAccessor.WhereOptions localWhereOptions1 = buildWhereClause(paramJSONArray, str1);
        localCursor1 = this.mApp.getActivity().getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[] { "contact_id" }, localWhereOptions1.getWhere(), localWhereOptions1.getWhereArgs(), "contact_id ASC");
        localHashSet1 = new HashSet();
        int j = -1;
        while (localCursor1.moveToNext())
        {
          if (j < 0)
            j = localCursor1.getColumnIndex("contact_id");
          localHashSet1.add(localCursor1.getString(j));
        }
        str1 = "%" + str2 + "%";
        break;
        str1 = "%";
      }
      localCursor1.close();
      ContactAccessor.WhereOptions localWhereOptions2 = buildIdClause(localHashSet1, str1);
      HashSet localHashSet2 = new HashSet();
      localHashSet2.add("contact_id");
      localHashSet2.add("raw_contact_id");
      localHashSet2.add("mimetype");
      if (isRequired("displayName", localHashMap))
        localHashSet2.add("data1");
      if (isRequired("name", localHashMap))
      {
        localHashSet2.add("data3");
        localHashSet2.add("data2");
        localHashSet2.add("data5");
        localHashSet2.add("data4");
        localHashSet2.add("data6");
      }
      if (isRequired("phoneNumbers", localHashMap))
      {
        localHashSet2.add("_id");
        localHashSet2.add("data1");
        localHashSet2.add("data2");
      }
      if (isRequired("emails", localHashMap))
      {
        localHashSet2.add("_id");
        localHashSet2.add("data1");
        localHashSet2.add("data2");
      }
      if (isRequired("addresses", localHashMap))
      {
        localHashSet2.add("_id");
        localHashSet2.add("data2");
        localHashSet2.add("data1");
        localHashSet2.add("data4");
        localHashSet2.add("data7");
        localHashSet2.add("data8");
        localHashSet2.add("data9");
        localHashSet2.add("data10");
      }
      if (isRequired("organizations", localHashMap))
      {
        localHashSet2.add("_id");
        localHashSet2.add("data2");
        localHashSet2.add("data5");
        localHashSet2.add("data1");
        localHashSet2.add("data4");
      }
      if (isRequired("ims", localHashMap))
      {
        localHashSet2.add("_id");
        localHashSet2.add("data1");
        localHashSet2.add("data2");
      }
      if (isRequired("note", localHashMap))
        localHashSet2.add("data1");
      if (isRequired("nickname", localHashMap))
        localHashSet2.add("data1");
      if (isRequired("urls", localHashMap))
      {
        localHashSet2.add("_id");
        localHashSet2.add("data1");
        localHashSet2.add("data2");
      }
      if (isRequired("birthday", localHashMap))
      {
        localHashSet2.add("data1");
        localHashSet2.add("data2");
      }
      if (isRequired("photos", localHashMap))
        localHashSet2.add("_id");
      Cursor localCursor2 = this.mApp.getActivity().getContentResolver().query(ContactsContract.Data.CONTENT_URI, (String[])localHashSet2.toArray(new String[0]), localWhereOptions2.getWhere(), localWhereOptions2.getWhereArgs(), "contact_id ASC");
      return populateContactArray(i, localHashMap, localCursor2);
    }
    catch (JSONException localJSONException)
    {
      break label45;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.ContactAccessorSdk5
 * JD-Core Version:    0.6.0
 */