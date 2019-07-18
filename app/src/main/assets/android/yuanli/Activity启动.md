# 

先看图

app启动的时序图

![](https://github.com/fumeidonga/markdownPic/blob/master/yuanli/launcher_app.png?raw=true)

我们以桌面启动为例

参考[基于Android 9.0的Activity启动流程源码分析](https://www.jianshu.com/p/e1153fed5b23)

![](https://github.com/fumeidonga/markdownPic/blob/master/yuanli/launcher_app0.png?raw=true)

### 我们来简单的概述一下

1. 点击启动图标，经过一系列的startActivity，最后通过binder进程间通信调用到ActivityManagerService进程中，在调用该服务的startActivity方法。

2. ActivityManagerService调用ActivityStack.startActivityMayWait来做准备要启动的Activity的相关信息

3. ActivityStack通知ApplicationThread要进行Activity启动调度了，这里的ApplicationThread代表的是调用ActivityManagerService.startActivity接口的进程，
   对于通过点击应用程序图标的情景来说，这个进程就是Launcher了，而对于通过在Activity内部调用startActivity的情景来说，这个进程就是这个Activity所在的进程了

4. ApplicationThread不执行真正的启动操作，它通过调用ActivityManagerService.activityPaused接口进入到ActivityManagerService进程中，看看是否需要创建新的进程来启动Activity

5. 对于通过点击应用程序图标来启动Activity的情景来说，ActivityManagerService在这一步中，会调用startProcessLocked来创建一个新的进程，而对于通过在Activity内部调用
   startActivity来启动新的Activity来说，这一步是不需要执行的，因为新的Activity就在原来的Activity所在的进程中进行启动

6. ActivityManagerServic调用ApplicationThread.scheduleLaunchActivity接口，通知相应的进程执行启动Activity的操作

7. ApplicationThread把这个启动Activity的操作转发给ActivityThread，ActivityThread通过ClassLoader导入相应的Activity类，然后把它启动起来


# 1. Launcher2
packages   apps   Launcher2

<pre>
	public void onClick(View v) {
		Object tag = v.getTag();
		if (tag instanceof ShortcutInfo) {
			// Open shortcut
			final Intent intent = ((ShortcutInfo) tag).intent;
			int[] pos = new int[2];
			v.getLocationOnScreen(pos);
			intent.setSourceBounds(new Rect(pos[0], pos[1],
				pos[0] + v.getWidth(), pos[1] + v.getHeight()));
			startActivitySafely(intent, tag);
		} else if (tag instanceof FolderInfo) {
			......
		} else if (v == mHandleView) {
			......
		}
	}
 
	void startActivitySafely(Intent intent, Object tag) {
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			......
		} catch (SecurityException e) {
			......
		}
	}

</pre>
由于我们从桌面启动，一般都会有如下参数的

action = "android.intent.action.Main"，

category="android.intent.category.LAUNCHER", 

cmp="xxx.MainActivity"

# 2. framework
我们在来看一看startActivity方法开始

android/app/Activity.java
<pre>

    1
    @Override
    public void startActivity(Intent intent) {
        this.startActivity(intent, null);
    }
    
     @Override
    public void startActivity(Intent intent, @Nullable Bundle options) {
        if (options != null) {
            startActivityForResult(intent, -1, options);    //注释1
        } else {
            // Note we want to go through this call for compatibility with
            // applications that may have overridden the method.
            startActivityForResult(intent, -1);     //注释2
        }
    }
    
    2
    public void startActivityForResult(@RequiresPermission Intent intent, int requestCode,
            @Nullable Bundle options) {
            
        所代表的其实是当前Activity的父，这是Activity嵌套Activity的设计，现在当然很少用了，这里mParent为null
        
        //mMainThread是Activity类的成员变量，它的类型是ActivityThread
        //对于这个时候的mMainThread 代表的是Launcher应用程序运行的进程
        //mToken是一个Binder对象的远程接口
        
        if (mParent == null) {
            options = transferSpringboardActivityOptions(options);
            
            ActivityResult是Instrumentation的一个内部类，代表着Activity的启动结果数据
            
            Instrumentation.ActivityResult ar =
                
                3
            
                mInstrumentation.execStartActivity(
                    this, mMainThread.getApplicationThread(), mToken, this,
                    intent, requestCode, options);
            if (ar != null) {
                mMainThread.sendActivityResult(
                    mToken, mEmbeddedID, requestCode, ar.getResultCode(),
                    ar.getResultData());
            }
            ...
        } else {
            ...
        }
    }
</pre>

4

android/app/Instrumentation.java   用来监控应用程序和系统的交互
<pre>
    
    返回Activity的启动结果数据
    public ActivityResult execStartActivity(
        Context who, IBinder contextThread, IBinder token, String target,
        Intent intent, int requestCode, Bundle options) {
        IApplicationThread whoThread = (IApplicationThread) contextThread;
        ...
        try {
            intent.migrateExtraStreamToClipData();
            intent.prepareToLeaveProcess(who);
            
            5
            这里获取的是 ActivityManagerService 
            int result = ActivityManager.getService()
            
            6
                .startActivity(whoThread, who.getBasePackageName(), intent,
                        intent.resolveTypeIfNeeded(who.getContentResolver()),
                        token, target, requestCode, 0, null, options);
                        
            对启动结果进行检查  我们放到最后面看看        
            checkStartActivityResult(result, intent);
        } catch (RemoteException e) {
            throw new RuntimeException("Failure from system", e);
        }
        return null;
    }
    
    intent.java 
    返回这个intent的MIME类型, 在这里返回null
    public @Nullable String resolveTypeIfNeeded(@NonNull ContentResolver resolver) {
        if (mComponent != null) {
            return mType;
        }
        return resolveType(resolver);
    }
</pre>




5

ActivityManager .java
<pre>

    这里我们可以看到，IActivityManager是一个AIDL文件，binder夸进程通讯
    返回ActivityManagerService的远程接口, 在这之前我们需要先了解下service的启动过程
    
    SystemServer启动过程中会调用 ActivityManagerService.setSystemProcess()，在setSystemProcess()方法中可以找到以下内容：
    ServiceManager.addService(Context.ACTIVITY_SERVICE, this, true);
    没错会将ActivityManagerService存储到ServiceManager中进行管理。也就是注释1处通过Context.ACTIVITY_SERVICE查找到IBinder类型的AMS引用

    IActivityManager.java会在编译时生成，Stub是其内部类，调用其asInterface()方法会将IBinder转换成IActivityManager接口，如果
    是跨进程的会为其生成代理Stub.Proxy。ActivityManagerService继承IActivityManager.Stub

    注意：
    实际上在这里使用AIDL方式是在Android 8.0，Android 8.0之前使用的是代理，在Instrumentation.execStartActivity()方法
    中使用ActivityManagerNative.getDefault().startActivity()，ActivityManagerNative.getDefault()返回ActivityManagerProxy

    
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
                    获取到IBinder类型的ActivityManagerService的引用
                    final IBinder b = ServiceManager.getService(Context.ACTIVITY_SERVICE);
                    final IActivityManager am = IActivityManager.Stub.asInterface(b);
                    return am;
                }
            };
</pre>


 ActivityManagerProxy.java

<pre>
    
    8.0 之前的做法，直接使用了代理，8.0 之后就不是这样了，
    
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

public class ActivityManagerService extends IActivityManager.Stub

ActivityManagerService.java
<pre>
    @Override
    public final int startActivity(IApplicationThread caller, String callingPackage,
            Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode,
            int startFlags, ProfilerInfo profilerInfo, Bundle bOptions) {
        return startActivityAsUser(caller, callingPackage, intent, resolvedType, resultTo,
                resultWho, requestCode, startFlags, profilerInfo, bOptions,
                UserHandle.getCallingUserId());   //注释1
    }

    @Override
    public final int startActivityAsUser(IApplicationThread caller, String callingPackage,
            Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode,
            int startFlags, ProfilerInfo profilerInfo, Bundle bOptions, int userId) {
        return startActivityAsUser(caller, callingPackage, intent, resolvedType, resultTo,
                resultWho, requestCode, startFlags, profilerInfo, bOptions, userId,
                true /*validateIncomingUser*/);   //注释2
    }
    
    public final int startActivityAsUser(IApplicationThread caller, String callingPackage,
            Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode,
            int startFlags, ProfilerInfo profilerInfo, Bundle bOptions, int userId,
            boolean validateIncomingUser) {
        判断调用者进程是否被隔离 隔离了就直接抛异常
        enforceNotIsolatedCaller("startActivity");
        //检查调用者权限 检查启动Activity的userId是否是合法的
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
    
    
    ActivityStarter setMayWait(int userId) {
        mRequest.mayWait = true;
        mRequest.userId = userId;

        return this;
    }
</pre>
9

以委托的方式处理启动Activity，算是一个中间类，对ActivityStarter类进行处理，

可以不影响启动结果，只负责间接传递处理。说白了就是一个间接类，在ActivityStarter和ActivityManagerService加多一层而已

ActivityStarter : execute
<pre>

    int execute() {
        try {
            // TODO(b/64750076): Look into passing request directly to these methods to allow
            // for transactional diffs and preprocessing.
            
            上个方法链路调用用了setMayWait(userId)的方法可以知道mRequest.mayWait = true 
            
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
        //用于记录Activity的数据
        final ActivityRecord[] outRecord = new ActivityRecord[1];
        
        11
        
        //启动Activity
        int res = startActivity(caller, intent, ephemeralIntent, resolvedType, aInfo, rInfo,
                voiceSession, voiceInteractor, resultTo, resultWho, requestCode, callingPid,
                callingUid, callingPackage, realCallingPid, realCallingUid, startFlags, options,
                ignoreTargetSecurity, componentSpecified, outRecord, inTask, reason,
                allowPendingRemoteAnimationRegistryLookup);
        ...
    }
    
    private int startActivity(IApplicationThread caller, Intent intent, Intent ephemeralIntent,
            String resolvedType, ActivityInfo aInfo, ResolveInfo rInfo,
            IVoiceInteractionSession voiceSession, IVoiceInteractor voiceInteractor,
            IBinder resultTo, String resultWho, int requestCode, int callingPid, int callingUid,
            String callingPackage, int realCallingPid, int realCallingUid, int startFlags,
            SafeActivityOptions options, boolean ignoreTargetSecurity, boolean componentSpecified,
            ActivityRecord[] outActivity, TaskRecord inTask, String reason,
            boolean allowPendingRemoteAnimationRegistryLookup) {

        ...
        
        mLastStartActivityResult = startActivity(caller, intent, ephemeralIntent, resolvedType,
                aInfo, rInfo, voiceSession, voiceInteractor, resultTo, resultWho, requestCode,
                callingPid, callingUid, callingPackage, realCallingPid, realCallingUid, startFlags,
                options, ignoreTargetSecurity, componentSpecified, mLastStartActivityRecord,
                inTask, allowPendingRemoteAnimationRegistryLookup);

        ...
    }
    
    11
    
    启动Activity
    private int startActivity(...) {
        ...
        
        if (caller != null) {
            // 获取调用者进程的记录对象
            callerApp = mService.getRecordForAppLocked(caller);
            if (callerApp != null) {
                callingPid = callerApp.pid;
                callingUid = callerApp.info.uid;
            } else {
                Slog.w(TAG, "Unable to find app for caller " + caller
                        + " (pid=" + callingPid + ") when starting: "
                        + intent.toString());
                err = ActivityManager.START_PERMISSION_DENIED;
            }
        }
        
        ...
        

        ActivityRecord sourceRecord = null;
        ActivityRecord resultRecord = null;
        if (resultTo != null) {
            //获取调用者所在Activity记录
            sourceRecord = mSupervisor.isInAnyStackLocked(resultTo);
            if (DEBUG_RESULTS) Slog.v(TAG_RESULTS,
                    "Will send result to " + resultTo + " " + sourceRecord);
            if (sourceRecord != null) {
                if (requestCode >= 0 && !sourceRecord.finishing) {
                    resultRecord = sourceRecord;
                }
            }
        }
        
        final int launchFlags = intent.getFlags();
        
        if ((launchFlags & Intent.FLAG_ACTIVITY_FORWARD_RESULT) != 0 && sourceRecord != null) {
            //将Activity执行结果从源Activity转移到正在启动的新Activity，不需要返回结果则不会进入该分支
            ...
        }
        
        ...
        
        // 检查启动Activityd的权限
        boolean abort = !mSupervisor.checkStartAnyActivityPermission(intent, aInfo, resultWho,
                requestCode, callingPid, callingUid, callingPackage, ignoreTargetSecurity,
                inTask != null, callerApp, resultRecord, resultStack);
        abort |= !mService.mIntentFirewall.checkStartActivity(intent, callingUid,
                callingPid, resolvedType, aInfo.applicationInfo);
                
        //权限检查
        
        ...
        
        // 创建Activity记录对象
        ActivityRecord r = new ActivityRecord(mService, callerApp, callingPid, callingUid,
                callingPackage, intent, resolvedType, aInfo, mService.getGlobalConfiguration(),
                resultRecord, resultWho, requestCode, componentSpecified, voiceSession != null,
                mSupervisor, checkedOptions, sourceRecord);
        
        ...

        //处理 pendind Activity的启动 
        mController.doPendingActivityLaunches(false);

        return startActivity(r, sourceRecord, voiceSession, voiceInteractor, startFlags,
                true /* doResume */, checkedOptions, inTask, outActivity);
        ...
    }
    
    12
    
    private int startActivity(final ActivityRecord r, ActivityRecord sourceRecord,
                IVoiceInteractionSession voiceSession, IVoiceInteractor voiceInteractor,
                int startFlags, boolean doResume, ActivityOptions options, TaskRecord inTask,
                ActivityRecord[] outActivity) {
        int result = START_CANCELED;
        try {
            mService.mWindowManager.deferSurfaceLayout();
            
            13
            
            result = startActivityUnchecked(r, sourceRecord, voiceSession, voiceInteractor,
                    startFlags, doResume, options, inTask, outActivity);
        } finally {
            ...
            //获取Activity的任务栈
            
        }

        postStartActivityProcessing(r, result, mTargetStack);

        return result;
    }
    
    13
    
    private int startActivityUnchecked(...) {
    
        ...
        
        computeLaunchingTaskFlags();      //处理Activity的启动模式，确定Activity的启动模式
        
        ...
        
        if (reusedActivity != null) {
            ...
            //根据Activity启动模式来设置栈
            if (mStartActivity.getTask() == null && !clearTopAndResetStandardLaunchMode) {
                mStartActivity.setTask(reusedActivity.getTask());
            }
            ...
            
            
        }
        ...
        // 如果正在启动的活动与当前位于顶部的活动相同，那么我们需要检查它是否应该只启动一次。
        final ActivityStack topStack = mSupervisor.mFocusedStack;
        final ActivityRecord topFocused = topStack.topActivity();
        final ActivityRecord top = topStack.topRunningNonDelayedActivityLocked(mNotTop);
        final boolean dontStart = top != null && mStartActivity.resultTo == null
                && top.realActivity.equals(mStartActivity.realActivity)
                && top.userId == mStartActivity.userId
                && top.app != null && top.app.thread != null
                && ((mLaunchFlags & FLAG_ACTIVITY_SINGLE_TOP) != 0
                || mLaunchSingleTop || mLaunchSingleTask);
        if (dontStart) {
            // 确保我们已经正确地恢复了顶部的Activity。
            topStack.mLastPausedActivity = null;
            if (mDoResume) {
                mSupervisor.resumeFocusedStackTopActivityLocked();
            }
            ActivityOptions.abort(mOptions);
            if ((mStartFlags & START_FLAG_ONLY_IF_NEEDED) != 0) {
                // We don t need to start a new activity, and the client said not to do
                // anything if that is the case, so this is it!
                return START_RETURN_INTENT_TO_CALLER;
            }
    
            deliverNewIntent(top);
    
            // 不要使用mStartActivity.task来显示吐司。 我们不是开始一项新活动，而是重新放到顶部。 mStartActivity中的字段可能未完全初始化。
            mSupervisor.handleNonResizableTaskIfNeeded(top.getTask(), preferredLaunchStackId,
                    preferredLaunchDisplayId, topStack.mStackId);
    
            return START_DELIVERED_TO_TOP;
        }
    
        boolean newTask = false;
        final TaskRecord taskToAffiliate = (mLaunchTaskBehind && mSourceRecord != null)
                ? mSourceRecord.getTask() : null;
    
        // Should this be considered a new task?
        int result = START_SUCCESS;
        if (mStartActivity.resultTo == null && mInTask == null && !mAddingToTask
                && (mLaunchFlags & FLAG_ACTIVITY_NEW_TASK) != 0) {
            newTask = true;
            // 创建新的栈
            result = setTaskFromReuseOrCreateNewTask(
                    taskToAffiliate, preferredLaunchStackId, topStack);
        } else if (mSourceRecord != null) {
            result = setTaskFromSourceRecord();
        } else if (mInTask != null) {
            result = setTaskFromInTask();
        } else {
            setTaskToCurrentTopOrCreateNewTask();
        }
        if (result != START_SUCCESS) {
            return result;
        }
        ...
        //根据Activity的启动模式来判断是直接插入已存在的栈顶还是新开栈插入
        mTargetStack.startActivityLocked(mStartActivity, topFocused, newTask, mKeepCurTransition,
                mOptions);
        if (mDoResume) {
            final ActivityRecord topTaskActivity =
                    mStartActivity.getTask().topRunningActivityLocked();
            if (!mTargetStack.isFocusable()
                    || (topTaskActivity != null && topTaskActivity.mTaskOverlay
                    && mStartActivity != topTaskActivity)) {
                ...
            } else {
                if (mTargetStack.isFocusable() && !mSupervisor.isFocusedStack(mTargetStack)) {
                    mTargetStack.moveToFront("startActivityUnchecked");
                }
                mSupervisor.resumeFocusedStackTopActivityLocked(mTargetStack, mStartActivity,
                        mOptions);
            }
        } else {
            mTargetStack.addRecentActivityLocked(mStartActivity);
        }
        ...
        return START_SUCCESS;
        
    }
    
    14
    ActivityStackSupervisor.java 
    
    boolean resumeFocusedStackTopActivityLocked(
        ActivityStack targetStack, ActivityRecord target, ActivityOptions targetOptions) {

    if (!readyToResume()) {
        return false;
    }
    targetStack不为null，而且isFocusedStack(targetStack)判断也为true，所以判断条件为true
    if (targetStack != null && isFocusedStack(targetStack)) {
        //确保恢复堆栈中的顶部活动
        return targetStack.resumeTopActivityUncheckedLocked(target, targetOptions);
    }
    
    ...
}
</pre>

15  ActivityStack : startActivityLocked
<pre>
    boolean resumeTopActivityUncheckedLocked(ActivityRecord prev, ActivityOptions options) {
        
        ...
        
        boolean result = false;
        try {
            // Protect against recursion.
            mStackSupervisor.inResumeTopActivity = true;
            result = resumeTopActivityInnerLocked(prev, options);

            ...
        } finally {
            mStackSupervisor.inResumeTopActivity = false;
        }

        return result;
    }
    
    private boolean resumeTopActivityInnerLocked(ActivityRecord prev, ActivityOptions options) {
         ...
         mStackSupervisor.startSpecificActivityLocked(next, true, true);
         ...
         return true;
    }
</pre>

16  

ActivityStackSupervisor.java 

<pre>
    void startSpecificActivityLocked(ActivityRecord r,
            boolean andResume, boolean checkConfig) {
        // Is this activity s application already running?
        // 获取启动Activity所在进程
        ProcessRecord app = mService.getProcessRecordLocked(r.processName,
                r.info.applicationInfo.uid, true);

        getLaunchTimeTracker().setLaunchTime(r);

        // 判断进程是否已运行
        if (app != null && app.thread != null) {
            try {
                if ((r.info.flags&ActivityInfo.FLAG_MULTIPROCESS) == 0
                        || !"android".equals(r.info.packageName)) {
                    app.addPackage(r.info.packageName, r.info.applicationInfo.longVersionCode,
                            mService.mProcessStats);
                }
                
                17
                
                realStartActivityLocked(r, app, andResume, checkConfig);
                return;
            } catch (RemoteException e) {
                Slog.w(TAG, "Exception when starting activity "
                        + r.intent.getComponent().flattenToShortString(), e);
            }
            ...
        }

        mService.startProcessLocked(r.processName, r.info.applicationInfo, true, 0,
                "activity", r.intent.getComponent(), false, false, true);
    }
    
    17
    
    final boolean realStartActivityLocked(ActivityRecord r, ProcessRecord app,
          boolean andResume, boolean checkConfig) throws RemoteException {
        
        判断其他的activity是否已经pause
        if (!allPausedActivitiesComplete()) {
            // While there are activities pausing we skipping starting any new activities until
            // pauses are complete. NOTE: that we also do this for activities that are starting in
            // the paused state because they will first be resumed then paused on the client side.
            if (DEBUG_SWITCH || DEBUG_PAUSE || DEBUG_STATES) Slog.v(TAG_PAUSE,
                    "realStartActivityLocked: Skipping start of r=" + r
                    + " some activities pausing...");
            return false;
        }  
        if (!allPausedActivitiesComplete()) {
            // While there are activities pausing we skipping starting any new activities until
            // pauses are complete. NOTE: that we also do this for activities that are starting in
            // the paused state because they will first be resumed then paused on the client side.
            if (DEBUG_SWITCH || DEBUG_PAUSE || DEBUG_STATES) Slog.v(TAG_PAUSE,
                    "realStartActivityLocked: Skipping start of r=" + r
                    + " some activities pausing...");
            return false;
        }

         ......
              9.0相对于其他低版本的版本，改动最大的就是注释2的代码块了，我们先贴 8.0的代码：
              app.thread.scheduleLaunchActivity(new Intent(r.intent), r.appToken,
                   System.identityHashCode(r), r.info,
                   // TODO: Have this take the merged configuration instead of separate global and
                   // override configs.
                   mergedConfiguration.getGlobalConfiguration(),
                    mergedConfiguration.getOverrideConfiguration(), r.compat,
                    r.launchedFromPackage, task.voiceInteractor, app.repProcState, r.icicle,
                    r.persistentState, results, newIntents, !andResume,
                    mService.isNextTransitionForward(), profilerInfo);
                    
             //注释2
             下面是9.0的代码
             9.0是通过事务机制来启动Activity，而8.0则是直接通过主线程：app.thread.scheduleLaunchActivity 调度来执行的
             // Create activity launch transaction.   
             通过ClientTransaction来管理了Activity的启动过程
             该类包含了客户端(app)的一系列的消息，可以对这些消息做出反应回调，并且发送给客户端，让客户端也就是app去执
             行回调,完成具体的消息执行和整个生命周期的调度执行
             
             app.thread 也就是IApplicationThread类型
                final ClientTransaction clientTransaction = ClientTransaction.obtain(app.thread,
                        r.appToken);
                clientTransaction.addCallback(LaunchActivityItem.obtain(new Intent(r.intent),
                        System.identityHashCode(r), r.info,
                        // TODO: Have this take the merged configuration instead of separate global
                        // and override configs.
                        mergedConfiguration.getGlobalConfiguration(),
                        mergedConfiguration.getOverrideConfiguration(), r.compat,
                        r.launchedFromPackage, task.voiceInteractor, app.repProcState, r.icicle,
                        r.persistentState, results, newIntents, mService.isNextTransitionForward(),
                        profilerInfo));

                // Set desired final state.
                final ActivityLifecycleItem lifecycleItem;
                if (andResume) {
                    lifecycleItem = ResumeActivityItem.obtain(mService.isNextTransitionForward());
                } else {
                    lifecycleItem = PauseActivityItem.obtain();
                }
                clientTransaction.setLifecycleStateRequest(lifecycleItem);

                // Schedule transaction.
                mService.getLifecycleManager().scheduleTransaction(clientTransaction);
          ......
    }
</pre>

ClientLifecycleManager.java
<pre>
    void scheduleTransaction(ClientTransaction transaction) throws RemoteException {
        final IApplicationThread client = transaction.getClient();
        transaction.schedule();
        if (!(client instanceof Binder)) {
            // If client is not an instance of Binder - it s a remote call and at this point it is
            // safe to recycle the object. All objects used for local calls will be recycled after
            // the transaction is executed on client in ActivityThread.
            transaction.recycle();
        }
    }
    
    
    ClientTransaction.java
    
    client本来就是Binder, IApplicationThread也是个aidl,
    
    private IApplicationThread mClient;
    
    public void schedule() throws RemoteException {
        mClient.scheduleTransaction(this);
    }
    
</pre>

ActivityThread.java

ActivityThread extends ClientTransactionHandler

<pre>
    ApplicationThread，是ActivityThread的一个内部类
    
    @Override
    public void scheduleTransaction(ClientTransaction transaction) throws RemoteException {
        ActivityThread.this.scheduleTransaction(transaction);
    }
        
    void scheduleTransaction(ClientTransaction transaction) {
        transaction.preExecute(this); //在启动前执行
        sendMessage(ActivityThread.H.EXECUTE_TRANSACTION, transaction);
    }
    
    通过sendMessage给ActivityThread的H发送消息，而H继承与Handler，我们可以直接点进去看看在H中对EXECUTE_TRANSACTION是如何做处理的：
    
      class H extends Handler {

     ......
     public void handleMessage(Message msg) {
    
        ......
             case EXECUTE_TRANSACTION:
                        final ClientTransaction transaction = (ClientTransaction) msg.obj;
                        mTransactionExecutor.execute(transaction);   //注释1
                        if (isSystem()) {
                            // Client transactions inside system process are recycled on the client side
                            // instead of ClientLifecycleManager to avoid being cleared before this
                            // message is handled.
                            transaction.recycle();
                        }
                        // TODO(lifecycler): Recycle locally scheduled transactions.
                        break;
        ......
    }
        ......
    }   

    public void execute(ClientTransaction transaction) {
        final IBinder token = transaction.getActivityToken();
        log("Start resolving transaction for client: " + mTransactionHandler + ", token: " + token);

        executeCallbacks(transaction);    //注释1

        executeLifecycleState(transaction);
        mPendingActions.clear();
        log("End resolving transaction");
    }
    
    public void executeCallbacks(ClientTransaction transaction) {
        ...

        //从服务中获取客户端APP(自己正在使用的APP)
        final IBinder token = transaction.getActivityToken();
        ActivityClientRecord r = mTransactionHandler.getActivityClient(token);
        ...

        final int size = callbacks.size();
        for (int i = 0; i < size; ++i) {
            ...

            // 注释2 执行启动Activity 
            item.execute(mTransactionHandler, token, mPendingActions);
            item.postExecute(mTransactionHandler, token, mPendingActions);
            if (r == null) {
                // Launch activity request will create an activity record.
                r = mTransactionHandler.getActivityClient(token);
            }
            ...
        }
    }
    可以看到通过  item.execute(mTransactionHandler, token, mPendingActions) 来执行启动Activity，
    而item也就是ClientTransactionItem 到底是什么？从源码中的类声明可以知道ClientTransactionItem 是
    作为系统与客户端(APP)的回调枢纽，比如配置变化、多窗口模式改变、Activity的启动等等

</pre>
接着往下看

LaunchActivityItem extends ClientTransactionItem 

<pre>
  @Override
    public void execute(ClientTransactionHandler client, IBinder token,
            PendingTransactionActions pendingActions) {
        Trace.traceBegin(TRACE_TAG_ACTIVITY_MANAGER, "activityStart");
        ActivityClientRecord r = new ActivityClientRecord(token, mIntent, mIdent, mInfo,
                mOverrideConfig, mCompatInfo, mReferrer, mVoiceInteractor, mState, mPersistentState,
                mPendingResults, mPendingNewIntents, mIsForward,
                mProfilerInfo, client);
        client.handleLaunchActivity(r, pendingActions, null /* customIntent */);   //注释1
        Trace.traceEnd(TRACE_TAG_ACTIVITY_MANAGER);
    }
</pre>

ActivityThread的handleLaunchActivity方法

<pre>
    @Override
    public Activity handleLaunchActivity(ActivityClientRecord r,
            PendingTransactionActions pendingActions, Intent customIntent) {
         
        ...
        final Activity a = performLaunchActivity(r, customIntent);   //注释1
        ...
    }
    private Activity performLaunchActivity(ActivityClientRecord r, Intent customIntent) {

        ......

       //创建Activity的Context
        ContextImpl appContext = createBaseContextForActivity(r);
        Activity activity = null;
        try {
            //利用类加载器来创建Activity实例
            java.lang.ClassLoader cl = appContext.getClassLoader();
            activity = mInstrumentation.newActivity(
                    cl, component.getClassName(), r.intent);
            StrictMode.incrementExpectedActivityCount(activity.getClass());
            r.intent.setExtrasClassLoader(cl);
            r.intent.prepareToEnterProcess();
            if (r.state != null) {
                r.state.setClassLoader(cl);
            }
        } catch (Exception e) {
            if (!mInstrumentation.onException(activity, e)) {
                throw new RuntimeException(
                    "Unable to instantiate activity " + component
                    + ": " + e.toString(), e);
            }
        }

          ......
     //执行Activity的attach方法
    activity.attach(appContext, this, getInstrumentation(), r.token,
                        r.ident, app, r.intent, r.activityInfo, title, r.parent,
                        r.embeddedID, r.lastNonConfigurationInstances, config,
                        r.referrer, r.voiceInteractor, window, r.configCallback);

          ......

        if (r.isPersistable()) {
                    mInstrumentation.callActivityOnCreate(activity, r.state, r.persistentState);                       //注释1
                } else {
                    mInstrumentation.callActivityOnCreate(activity, r.state);   //注释2
                }


        return activity;

}

   public void callActivityOnCreate(Activity activity, Bundle icicle) {
        prePerformCreate(activity);
        activity.performCreate(icicle);    
        postPerformCreate(activity);
    }
    直接搜索Activity的performCreate方法：
 final void performCreate(Bundle icicle, PersistableBundle persistentState) {
        mCanEnterPictureInPicture = true;
        restoreHasCurrentPermissionRequest(icicle);
        if (persistentState != null) {
            onCreate(icicle, persistentState);
        } else {
            onCreate(icicle);
        }
        writeEventLog(LOG_AM_ON_CREATE_CALLED, "performCreate");
        mActivityTransitionState.readState(icicle);

        mVisibleFromClient = !mWindow.getWindowStyle().getBoolean(
                com.android.internal.R.styleable.Window_windowNoDisplay, false);
        mFragments.dispatchActivityCreated();
        mActivityTransitionState.setEnterActivityOptions(this, getActivityOptions());
    }
    
</pre>



android/app/Instrumentation.java   用来监控应用程序和系统的交互
<pre>
    看到这里是否有点熟悉这里抛出的异常，特别是没有在AndroidManifest注册的Activity的异常！！！
    这里会对启动Activity的结果返回做一个检查判断
    
    对启动结果进行检查
    
    public static void checkStartActivityResult(int res, Object intent) {
        if (!ActivityManager.isStartResultFatalError(res)) {
            return;
        }

        switch (res) {
            case ActivityManager.START_INTENT_NOT_RESOLVED:
            case ActivityManager.START_CLASS_NOT_FOUND:
                if (intent instanceof Intent && ((Intent)intent).getComponent() != null)
                    throw new ActivityNotFoundException(
                            "Unable to find explicit activity class "
                            + ((Intent)intent).getComponent().toShortString()
                            + "; have you declared this activity in your AndroidManifest.xml?");
                throw new ActivityNotFoundException(
                        "No Activity found to handle " + intent);
            case ActivityManager.START_PERMISSION_DENIED:
                throw new SecurityException("Not allowed to start activity "
                        + intent);
            case ActivityManager.START_FORWARD_AND_REQUEST_CONFLICT:
                throw new AndroidRuntimeException(
                        "FORWARD_RESULT_FLAG used while also requesting a result");
            case ActivityManager.START_NOT_ACTIVITY:
                throw new IllegalArgumentException(
                        "PendingIntent is not an activity");
            case ActivityManager.START_NOT_VOICE_COMPATIBLE:
                throw new SecurityException(
                        "Starting under voice control not allowed for: " + intent);
            case ActivityManager.START_VOICE_NOT_ACTIVE_SESSION:
                throw new IllegalStateException(
                        "Session calling startVoiceActivity does not match active session");
            case ActivityManager.START_VOICE_HIDDEN_SESSION:
                throw new IllegalStateException(
                        "Cannot start voice activity on a hidden session");
            case ActivityManager.START_ASSISTANT_NOT_ACTIVE_SESSION:
                throw new IllegalStateException(
                        "Session calling startAssistantActivity does not match active session");
            case ActivityManager.START_ASSISTANT_HIDDEN_SESSION:
                throw new IllegalStateException(
                        "Cannot start assistant activity on a hidden session");
            case ActivityManager.START_CANCELED:
                throw new AndroidRuntimeException("Activity could not be started for "
                        + intent);
            default:
                throw new AndroidRuntimeException("Unknown error code "
                        + res + " when starting " + intent);
        }
    }
</pre>
