package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import edu.luc.etl.cs313.android.simplestopwatch.common.StopwatchModelListener;
import edu.luc.etl.cs313.android.simplestopwatch.model.clock.ClockModel;
import edu.luc.etl.cs313.android.simplestopwatch.model.time.TimeModel;

/**
 * An implementation of the state machine for the stopwatch.
 *
 * @author laufer
 */
public class DefaultStopwatchStateMachine implements StopwatchStateMachine {

    public DefaultStopwatchStateMachine(final TimeModel timeModel, final ClockModel clockModel) {
        this.timeModel = timeModel;
        this.clockModel = clockModel;
    }

    private final TimeModel timeModel;

    private final ClockModel clockModel;

    /**
     * The internal state of this adapter component. Required for the State pattern.
     */
    private StopwatchState state;

    @Override
    public int getRuntime() {
        return timeModel.getRuntime();
    }

    protected void setState(final StopwatchState state) {
        this.state = state;
        listener.onStateUpdate(state.getId());
    }

    private StopwatchModelListener listener;

    @Override
    public void setModelListener(final StopwatchModelListener listener) {
        this.listener = listener;
    }

    // forward event uiUpdateListener methods to the current state
    // these must be synchronized because events can come from the
    // UI thread or the timer thread
    @Override public synchronized void onStartStop() { state.onStartStop(); }

    @Override public synchronized void onTick()      { state.onTick(); }




    @Override public void updateUIRuntime() { listener.onTimeUpdate(timeModel.getRuntime()); }


    // known states
    private final StopwatchState STOPPED     = new StoppedState(this);
    private final StopwatchState RUNNING     = new DecrementingState(this);
    private final StopwatchState INCREMENT = new IncrementState(this);
    private final StopwatchState ALARM = new AlarmingState(this);

    // transitions
    @Override public void toDecrementState()    { setState(RUNNING); }
    @Override public void toStoppedState()    { setState(STOPPED); }
    @Override public void toIncrementState() { setState(INCREMENT); }
    @Override public void toAlarmState() { setState(ALARM); }

    // actions
    @Override public void actionInit()       { toStoppedState(); actionReset(); }
    @Override public void actionReset()      { timeModel.resetRuntime(); actionUpdateView(); }
    @Override public void actionStart()      { clockModel.start(); }
    @Override public void actionStop()       { clockModel.stop(); }
    @Override public void actionDec()        { timeModel.decRuntime(); }
    @Override public void actionInc()        { timeModel.incRuntime(); actionUpdateView(); }
    @Override public void actionUpdateView() { state.updateView(); }


}
