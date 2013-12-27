package com.worklight.wlclient.api.challengehandler;

import com.worklight.wlclient.WLRequest;
import com.worklight.wlclient.api.WLRequestOptions;
import com.worklight.wlclient.api.WLResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class BaseChallengeHandler<T>
{
  protected WLRequest activeRequest = null;
  private String realm;
  private List<WLRequest> requestWaitingList = new ArrayList();

  protected BaseChallengeHandler(String paramString)
  {
    this.realm = paramString;
  }

  private void clearChallengeRequests()
  {
    monitorenter;
    try
    {
      this.activeRequest = null;
      clearWaitingList();
      return;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public void clearWaitingList()
  {
    monitorenter;
    try
    {
      this.requestWaitingList.clear();
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  public String getRealm()
  {
    return this.realm;
  }

  public abstract void handleChallenge(T paramT);

  public void releaseWaitingList()
  {
    monitorenter;
    try
    {
      Iterator localIterator = this.requestWaitingList.iterator();
      while (localIterator.hasNext())
        ((WLRequest)localIterator.next()).removeExpectedAnswer(this.realm);
    }
    finally
    {
      monitorexit;
    }
    clearWaitingList();
    monitorexit;
  }

  public void startHandleChallenge(WLRequest paramWLRequest, T paramT)
  {
    monitorenter;
    try
    {
      if (!paramWLRequest.getOptions().isFromChallenge())
      {
        if (this.activeRequest != null)
        {
          this.requestWaitingList.add(paramWLRequest);
          return;
        }
        this.activeRequest = paramWLRequest;
      }
      monitorexit;
      handleChallenge(paramT);
      return;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  protected void submitFailure(WLResponse paramWLResponse)
  {
    clearChallengeRequests();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.wlclient.api.challengehandler.BaseChallengeHandler
 * JD-Core Version:    0.6.0
 */