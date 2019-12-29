
import de.artcom.hsm.*;
import org.junit.Assert;
import org.junit.Test;
//import org.mockito.InOrder;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

//import static org.mockito.Mockito.inOrder;
//import static org.mockito.Mockito.mock;

public class MyTestClass {

    @Test
    public void test1() {
        new PlantUmlGenerator().generate();
        new String("sdfsdfsf");
    }
/*

        @Test
    public void canGenerateHierarcicalUml() {
        //given:
        Action onEnterLoud = mock(Action.class);
        Action onEnterQuiet = mock(Action.class);
        Action onEnterOn = mock(Action.class);
        Action onEnterOff = mock(Action.class);
        State loud = new State("loud")
                .onEnter(onEnterLoud);
        State quiet = new Sub("quiet", new State("quiet a"), new State("quiet b"))
                .onEnter(onEnterQuiet);

        quiet.addHandler("volume_up", loud, TransitionKind.External);
        loud.addHandler("volume_down", quiet, TransitionKind.External);

        Sub on = new Sub("on", new StateMachine(quiet, loud))
                .onEnter(onEnterOn);

        State off = new State("off")
                .onEnter(onEnterOff);

        on.addHandler("switched_off", off, TransitionKind.External);
        off.addHandler("switched_on", on, TransitionKind.External);

        StateMachine sm = new StateMachine(off, on);
        sm.init();
        String uml = new UmlBuilder(sm).generateUml();

        writeToFile(uml, "superstate.plantuml");

    }

    private void writeToFile(String uml, String fileName) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(fileName, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        writer.println(uml);
        writer.close();
    } */
}
