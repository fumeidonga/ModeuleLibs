


我们启动一个Activity， 有多种类型，1. 通过Launcher， 2. 在应用内部 3. 外部跳转等等

还可以分成隐示启动、显示启动

我们从startActivity方法开始

android/app/Activity.java
<pre>

    1
    @Override
    public void startActivity(Intent intent) {
        this.startActivity(intent, null);
    }
    
    2
    public void startActivityForResult(@RequiresPermission Intent intent, int requestCode,
            @Nullable Bundle options) {
        if (mParent == null) {
            ...
        } else {
            if (options != null) {
                mParent.startActivityFromChild(this, intent, requestCode, options);
            } else {
                // Note we want to go through this method for compatibility with
                // existing applications that may have overridden it.
                mParent.startActivityFromChild(this, intent, requestCode);
            }
        }
    }
    
    3 
    public void startActivityFromChild(@NonNull Activity child, @RequiresPermission Intent intent,
            int requestCode, @Nullable Bundle options) {
        options = transferSpringboardActivityOptions(options);
        
        Instrumentation.ActivityResult ar =
            mInstrumentation.execStartActivity(
                this, mMainThread.getApplicationThread(), mToken, child,
                intent, requestCode, options);
        if (ar != null) {
            mMainThread.sendActivityResult(
                mToken, child.mEmbeddedID, requestCode,
                ar.getResultCode(), ar.getResultData());
        }
        cancelInputsAndStartExitTransition(options);
    }
</pre>

4

android/app/Instrumentation.java
<pre>

    public ActivityResult execStartActivity(
        Context who, IBinder contextThread, IBinder token, String target,
        Intent intent, int requestCode, Bundle options) {
        IApplicationThread whoThread = (IApplicationThread) contextThread;
        ...
        try {
            intent.migrateExtraStreamToClipData();
            intent.prepareToLeaveProcess(who);
            
            5
            
            int result = ActivityManager.getService()
            
            6
                .startActivity(whoThread, who.getBasePackageName(), intent,
                        intent.resolveTypeIfNeeded(who.getContentResolver()),
                        token, target, requestCode, 0, null, options);
            checkStartActivityResult(result, intent);
        } catch (RemoteException e) {
            throw new RuntimeException("Failure from system", e);
        }
        return null;
    }
</pre>


5

ActivityManager .java
<pre>

    这里我们可以看到，IActivityManager是一个AIDL文件，binder夸进程通讯
    返回ActivityManagerService的远程接口，即ActivityManagerProxy接口
    
    /**
     * @hide
     */
    public static IActivityManager getService() {
        return IActivityManagerSingleton.get();
    }
    
    private static final Singleton<IActivityManager> IActivityManagerSingleton =
            new Singleton<IActivityManager>() {
                @Override
                protected IActivityManager create() {
                    final IBinder b = ServiceManager.getService(Context.ACTIVITY_SERVICE);
                    final IActivityManager am = IActivityManager.Stub.asInterface(b);
                    return am;
                }
            };
</pre>

6 ActivityManagerProxy.java

<pre>

    通过Binder驱动程序调用到ActivityManagerService
    ...
	public int startActivity(IApplicationThread caller, Intent intent,
			String resolvedType, Uri[] grantedUriPermissions, int grantedMode,
			IBinder resultTo, String resultWho,
			int requestCode, boolean onlyIfNeeded,
			boolean debug) throws RemoteException {
		Parcel data = Parcel.obtain();
		Parcel reply = Parcel.obtain();
		...
		data.writeInt(onlyIfNeeded ? 1 : 0);
		data.writeInt(debug ? 1 : 0);
		mRemote.transact(START_ACTIVITY_TRANSACTION, data, reply, 0);
		reply.readException();
		int result = reply.readInt();
		reply.recycle();
		data.recycle();
		return result;
	}
</pre>
7

ActivityManagerService.java
<pre>

    @Override
    public final int startActivity(IApplicationThread caller, String callingPackage,
            Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode,
            int startFlags, ProfilerInfo profilerInfo, Bundle bOptions) {
        return startActivityAsUser(caller, callingPackage, intent, resolvedType, resultTo,
                resultWho, requestCode, startFlags, profilerInfo, bOptions,
                UserHandle.getCallingUserId());
    }
    
    public final int startActivityAsUser(IApplicationThread caller, String callingPackage,
            Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode,
            int startFlags, ProfilerInfo profilerInfo, Bundle bOptions, int userId,
            boolean validateIncomingUser) {
        enforceNotIsolatedCaller("startActivity");

        userId = mActivityStartController.checkTargetUser(userId, validateIncomingUser,
                Binder.getCallingPid(), Binder.getCallingUid(), "startActivityAsUser");
        
        8
        
        // TODO: Switch to user app stacks here.
        return mActivityStartController.obtainStarter(intent, "startActivityAsUser")
                .setCaller(caller)
                .setCallingPackage(callingPackage)
                .setResolvedType(resolvedType)
                .setResultTo(resultTo)
                .setResultWho(resultWho)
                .setRequestCode(requestCode)
                .setStartFlags(startFlags)
                .setProfilerInfo(profilerInfo)
                .setActivityOptions(bOptions)
                .setMayWait(userId)
                .execute();

    }
</pre>
9

ActivityStarter : execute
<pre>

    int execute() {
        try {
            // TODO(b/64750076): Look into passing request directly to these methods to allow
            // for transactional diffs and preprocessing.
            if (mRequest.mayWait) {
                return startActivityMayWait(mRequest.caller, mRequest.callingUid,
                        mRequest.callingPackage, mRequest.intent, mRequest.resolvedType,
                        mRequest.voiceSession, mRequest.voiceInteractor, mRequest.resultTo,
                        mRequest.resultWho, mRequest.requestCode, mRequest.startFlags,
                        mRequest.profilerInfo, mRequest.waitResult, mRequest.globalConfig,
                        mRequest.activityOptions, mRequest.ignoreTargetSecurity, mRequest.userId,
                        mRequest.inTask, mRequest.reason,
                        mRequest.allowPendingRemoteAnimationRegistryLookup);
            } else {
            ...
            }
        } finally {
            onExecutionComplete();
        }
    }
    
    10
    
    private int startActivityMayWait(...) {
        ...
        对参数intent的内容进行解析，得到相关信息，保存在aInfo变量中
        
        ResolveInfo rInfo = mSupervisor.resolveIntent(intent, resolvedType, userId,
                0 /* matchFlags */,
                        computeResolveFilterUid(
                                callingUid, realCallingUid, mRequest.filterCallingUid));
        ...
        
        // Collect information about the target of the Intent. 收集有关意图目标的信息
        ActivityInfo aInfo = mSupervisor.resolveActivity(intent, rInfo, startFlags, profilerInfo);
        
        ...
        startActivity(...);
        ...
    }
    
    11
    
    private int startActivity(...) {
        ...
        startActivityUnchecked(...);
        ...
    }
    
    12
    
    private int startActivityUnchecked(...) {
    
        ...
        13
        reusedActivity = setTargetStackAndMoveToFrontIfNeeded(reusedActivity);
        ...
        mTargetStack.startActivityLocked(mStartActivity, topFocused, newTask, mKeepCurTransition,
                mOptions);
        ...
        
    }
    
    13
    private int setTargetStackAndMoveToFrontIfNeeded(...) {
        ...
        // If the caller has requested that the target task be reset, then do so.
        if ((mLaunchFlags & FLAG_ACTIVITY_RESET_TASK_IF_NEEDED) != 0) {
            return mTargetStack.resetTaskIfNeededLocked(intentActivity, mStartActivity);
        }
        ...
    }
</pre>

13  ActivityStack : startActivityLocked
<pre>

    final ActivityRecord resetTaskIfNeededLocked(ActivityRecord taskTop,
            ActivityRecord newActivity) {
        ...
        addTask(...);
        ...
    }

    private int startActivityLocked(...) {
        ...
        startActivityUnchecked(...);
        ...
    }
</pre>








