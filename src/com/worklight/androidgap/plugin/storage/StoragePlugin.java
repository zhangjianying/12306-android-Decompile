package com.worklight.androidgap.plugin.storage;

public class StoragePlugin extends DispatchingPlugin
{
  public StoragePlugin()
  {
    addDispatcher(new AllDirtyActionDispatcher());
    addDispatcher(new ChangePasswordActionDispatcher());
    addDispatcher(new CloseDatabaseActionDispatcher());
    addDispatcher(new DestroyDBFileAndKeychainActionDispatcher());
    addDispatcher(new DropTableActionDispatcher());
    addDispatcher(new CountActionDispatcher());
    addDispatcher(new FindActionDispatcher());
    addDispatcher(new FindByIdActionDispatcher());
    addDispatcher(new IsDirtyActionDispatcher());
    addDispatcher(new IsKeyGenRequiredActionDispatcher());
    addDispatcher(new LocalCountActionDispatcher());
    addDispatcher(new MarkCleanActionDispatcher());
    addDispatcher(new ProvisionActionDispatcher());
    addDispatcher(new RemoveActionDispatcher());
    addDispatcher(new ReplaceActionDispatcher());
    addDispatcher(new StoreActionDispatcher());
    addDispatcher(new StoreDPKActionDispatcher());
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.storage.StoragePlugin
 * JD-Core Version:    0.6.0
 */