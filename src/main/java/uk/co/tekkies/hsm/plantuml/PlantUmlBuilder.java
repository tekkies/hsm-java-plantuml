package uk.co.tekkies.hsm.plantuml;

import com.google.common.collect.LinkedListMultimap;
import de.artcom.hsm.Handler;
import de.artcom.hsm.State;
import de.artcom.hsm.StateMachine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlantUmlBuilder {
    StringBuilder stringBuilder;
    StringBuilder transitions;
    private StateMachine stateMachine;

    public PlantUmlBuilder(StateMachine stateMachine) {

        this.stateMachine = stateMachine;
        stringBuilder = new StringBuilder();
        transitions = new StringBuilder();
    }

    public String generateUml() {
        appendLine("@startuml");
        appendLine("scale 600 width");

        List<State> descendantStates = stateMachine.getDescendantStates();
        for (State state : descendantStates) {
            if(isTopLevelState(state)) {
                declareState(state, "");
            }
        }

        stringBuilder.append(transitions);

        stringBuilder.append(" \n" +
                "@enduml");
        return stringBuilder.toString();
    }

    private boolean isTopLevelState(State state) {
        StateMachine owner = state.getOwner();
        State container = owner.getContainer();
        return container == null;
    }

    private void declareState(State state, String indent) {
        appendLine("%sstate \"%s\" as %s {", indent, state.getId(), safeName(state.getId()));
        ArrayList<State> descendantStates = (ArrayList<State>) state.getDescendantStates();
        if(descendantStates != null) {
            for (State substate : descendantStates) {
                if(isImmediateDecenent(state, substate))
                    declareState(substate, indent + "    ");
            }
        }
        declareTransitions(state);
        appendLine("%s}", indent);
    }

    private void declareTransitions(State state) {
        LinkedListMultimap<String, Handler> eventHandlers = state.getEventHandlers();
        List<Map.Entry<String, Handler>> entries = eventHandlers.entries();
        for (Map.Entry<String, Handler> entry:entries) {
            Handler transition = entries.get(0).getValue();
            State targetState = transition.getTargetState();
            addTransition(state, targetState, entries.get(0).getKey());
        }

    }

    private void addTransition(State state, State targetState, String handler) {
        transitions.append(String.format("%s --> %s : %s",
                safeName(state.getId()),
                safeName(targetState.getId()),
                safeName(handler)));
        transitions.append(System.lineSeparator());
    }

    private boolean isImmediateDecenent(State parent, State state) {
        return state.getOwner().getContainer() == parent;
    }

    private String safeName(String name) {
        return name.replace(" ", "_");
    }

    private void appendLine(String format, Object... args) {
        stringBuilder.append(String.format(format, args));
        stringBuilder.append(System.lineSeparator());
    }
}
