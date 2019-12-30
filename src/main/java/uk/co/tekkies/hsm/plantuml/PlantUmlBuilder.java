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
    ArrayList<String> transitions;
    private StateMachine stateMachine;
    private boolean highlightActiveState =false;

    public PlantUmlBuilder(StateMachine stateMachine) {

        this.stateMachine = stateMachine;
        stringBuilder = new StringBuilder();
        transitions = new ArrayList<String>();
    }

    public String build() {
        appendLine("@startuml");
        appendLine("scale 600 width");

        List<State> descendantStates = stateMachine.getDescendantStates();
        for (State state : descendantStates) {
            if(isTopLevelState(state)) {
                declareState(state, "");
            }
        }

        appendTransitions();

        stringBuilder.append(" \n" +
                "@enduml");
        return stringBuilder.toString();
    }

    private void appendTransitions() {
        for (String transtion:transitions) {
            stringBuilder.append(transtion);
        }
    }

    private boolean isTopLevelState(State state) {
        StateMachine owner = state.getOwner();
        State container = owner.getContainer();
        return container == null;
    }

    private void declareState(State state, String indent) {
        appendLine("%sstate \"%s\" as %s %s {",
                indent,
                state.getId(),
                safeName(state.getId()),
                getColor(state));
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

    private String getColor(State state) {
        String color="";
        if(highlightActiveState && isActiveState(state))
        {
            color = "#LightGreen";
        }
        return color;
    }

    private boolean isActiveState(State state) {
        List<State> allActiveStates = stateMachine.getAllActiveStates();
        return allActiveStates.indexOf(state) == (allActiveStates.size() - 1);
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
        String transition = String.format("%s --> %s : %s%s",
                safeName(state.getId()),
                safeName(targetState.getId()),
                safeName(handler),
                System.lineSeparator());
        if(!transitions.contains(transition))
        {
            transitions.add(transition);
        }
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

    public PlantUmlBuilder highlightActiveState() {
        this.highlightActiveState = true;
        return this;
    }
}
