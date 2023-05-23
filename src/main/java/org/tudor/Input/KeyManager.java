package org.tudor.Input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Singleton class that handles the keyboard inputs for the game. Using the <i>Observer Pattern</i>, it
 * allows anyone to register to it to be able to receive keyboard input in an <b>asynchronous</b> way.
 */
public class KeyManager implements KeyListener  {
    /** Singleton instance for the manager. */
    private static KeyManager singleton = null;

    /** A list of observers that want to receive keyboard input. */
    private final List<InputObserver> observers = new CopyOnWriteArrayList<>();

    /** Maps key codes to InputTypes.*/
    private final HashMap<Integer, InputType> inputMap = new HashMap<>();
    /** Input buffer for Player1 for combo detection. */
    private final LinkedList<Input> inputBufferPlayer1 = new LinkedList<>();
    /** Input buffer for Player2 for combo detection. */
    private final LinkedList<Input> inputBufferPlayer2 = new LinkedList<>();
    /** List describing the moves for combo1. */
    private final LinkedList<InputType> combo1 = new LinkedList<>();
    /** List describing the moves for combo2. */
    private final LinkedList<InputType> combo2 = new LinkedList<>();

    /**
     * Private constructor for the singleton design pattern.
     */
    private KeyManager() {
        // Set up input map for player 1
        inputMap.put(KeyEvent.VK_W, InputType.UPL);
        inputMap.put(KeyEvent.VK_A, InputType.LEFTL);
        inputMap.put(KeyEvent.VK_S, InputType.DOWNL);
        inputMap.put(KeyEvent.VK_D, InputType.RIGHTL);
        inputMap.put(KeyEvent.VK_F, InputType.PUNCHL);
        inputMap.put(KeyEvent.VK_G, InputType.DEFENDL);
        inputMap.put(KeyEvent.VK_H, InputType.KICKL);

        // Set up input map for player 2
        inputMap.put(KeyEvent.VK_UP,    InputType.UPR);
        inputMap.put(KeyEvent.VK_LEFT,  InputType.LEFTR);
        inputMap.put(KeyEvent.VK_DOWN,  InputType.DOWNR);
        inputMap.put(KeyEvent.VK_RIGHT, InputType.RIGHTR);
        inputMap.put(KeyEvent.VK_J,     InputType.PUNCHR);
        inputMap.put(KeyEvent.VK_K,     InputType.DEFENDR);
        inputMap.put(KeyEvent.VK_L,     InputType.KICKR);

        // Set up input map for other keys
        inputMap.put(KeyEvent.VK_ENTER, InputType.ENTER);
        inputMap.put(KeyEvent.VK_ESCAPE, InputType.ESCAPE);

        // Set up combo 1
        combo1.add(InputType.PUNCHL);
        combo1.add(InputType.KICKL);
        combo1.add(InputType.DEFENDL);
        combo1.add(InputType.DEFENDL);
        combo1.add(InputType.RIGHTL);

        // Set up combo 2
        combo2.add(InputType.PUNCHR);
        combo2.add(InputType.KICKR);
        combo2.add(InputType.DEFENDR);
        combo2.add(InputType.DEFENDR);
        combo2.add(InputType.RIGHTR);
    }

    /**
     * Getter for the shared instance.
     * @return The shared instance.
     */
    public static KeyManager shared() {
        if ( singleton == null ) {
            singleton = new KeyManager();
        }

        return singleton;
    }

    /**
     * Checks for any combos in the input buffers.
     */
    public void update() {
        removeExpiredInputs(inputBufferPlayer1);
        removeExpiredInputs(inputBufferPlayer2);

        if ( detectCombo(combo1, inputBufferPlayer1) ) {
            for ( InputObserver o: observers ) {
                o.newInput(InputType.COMBO1L);
            }
            removeMarkedInputs(inputBufferPlayer1);
        } else {
            unMarkInputs(inputBufferPlayer1);
        }

        if ( detectCombo(combo2, inputBufferPlayer2) ) {
            for ( InputObserver o: observers ) {
                o.newInput(InputType.COMBO1R);
            }
            removeMarkedInputs(inputBufferPlayer2);
        } else {
            unMarkInputs(inputBufferPlayer1);
        }
    }

    /**
     * Unmarks all inputs after a combo detection check
     * @param inputs An input buffer.
     */
    private void unMarkInputs(LinkedList<Input> inputs) {
        for ( Input i: inputs) {
            i.marked = false;
        }
    }

    /**
     * Removes all marked inputs from the buffer after a combo was detected.
     * @param inputs An input buffer.
     */
    private void removeMarkedInputs(LinkedList<Input> inputs) {
        inputs.removeIf(input -> input.marked);
    }

    /**
     * Removes expired inputs from the buffer.
     * @param inputs An input buffer.
     */
    private void removeExpiredInputs(LinkedList<Input> inputs) {
        long time = System.currentTimeMillis();

        Iterator<Input> it = inputs.iterator();
        while ( it.hasNext() ) {
            Input i = it.next();
            long INPUT_EXPIRE_TIME_MS = 1000;
            if (time - i.activationTime > INPUT_EXPIRE_TIME_MS)
                it.remove();
        }
    }

    /**
     * Detects a combo.
     * @param combo The combo to detect in the buffer.
     * @param inputs The buffer to search for the combo.
     * @return Whether the combo was detected in the buffer.
     */
    private boolean detectCombo(LinkedList<InputType> combo, LinkedList<Input> inputs) {
        //TODO: Refactor the method to check for combos in larger inputBuffers as well.
        // This works for now as the method is called quickly enough to detect the combo
        // correctly at 60FPS.

        if ( inputs.size() != combo.size() ) {
            return false;
        }

        Iterator<InputType> comboIt = combo.iterator();
        Iterator<Input> inputIt = inputs.iterator();
        while ( comboIt.hasNext() ) {
            Input i = inputIt.next();
            if ( comboIt.next() != i.key )
                return false;
            i.marked = true;
        }

        return true;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        InputType type = inputMap.get(e.getKeyCode());

        if ( type != null ) {
            // This is a key we care about...
            Input i = new Input(type, System.currentTimeMillis());

            switch (type) {
                case LEFTL, RIGHTL, UPL, DOWNL, PUNCHL, KICKL, DEFENDL -> inputBufferPlayer1.offer(i);
                case LEFTR, RIGHTR, UPR, DOWNR, PUNCHR, KICKR, DEFENDR -> inputBufferPlayer2.offer(i);
            }

            for ( InputObserver o: observers ) {
                o.newInput(type);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        InputType type = inputMap.get(e.getKeyCode());

        if ( type != null ) {
            // This is a key we care about...
            for ( InputObserver o: observers ) {
                o.releasedInput(type);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * Registers an observer to receive inputs.
     * @param o The new observer.
     */
    public void register(InputObserver o) {
        observers.add(o);
    }

    /**
     * Unregisters an observer.
     * @param o The observer that doesn't want to receive inputs anymore.
     */
    public void unregister(InputObserver o) {
        observers.remove(o);
    }
}
