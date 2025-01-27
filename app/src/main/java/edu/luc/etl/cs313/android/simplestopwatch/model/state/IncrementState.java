package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import edu.luc.etl.cs313.android.simplestopwatch.R;

// Original Lap-Running State
// It increments when the button is clicked
// If the counter is more than 3 seconds, transition to the decrementing state
class IncrementState implements StopwatchState {

    // Counter to keep track of ticks.
    private int counter = 0;

    public IncrementState(final StopwatchSMStateView sm) {
        this.sm = sm;
    }

    private final StopwatchSMStateView sm;

    @Override
    public void onStartStop() {
        // Reset the counter to 0.
        counter = 0;

        // If runtime is greater than or equal to 99 and not 0, transition to RunningState.
        if (sm.getRuntime() >= 99 && sm.getRuntime() != 0) {
            sm.toDecrementState();
        } else {
            // Otherwise, increment the action and transition to IncrementState.
            sm.actionInc();
            sm.toIncrementState();
        }
    }



    @Override
    public void onTick() {
        // Increment the counter.
        counter++;
        // If the counter reaches 3, trigger an alarm and transition to RunningState.
        if (counter == 3) {
            // Assuming AlarmingState has a static method alarm().
            sm.toDecrementState();
        } else {
            // Otherwise, transition to IncrementState again.
            sm.toIncrementState();
        }
    }

    @Override
    public void updateView() {
        sm.updateUIRuntime();
    }

    @Override
    public int getId() {
        return R.string.INCREMENTING;
    }
}
