import de.artcom.hsm.State;
import de.artcom.hsm.StateMachine;
import de.artcom.hsm.Sub;
import de.artcom.hsm.TransitionKind;
import org.junit.Test;
import uk.co.tekkies.hsm.plantuml.PlantUmlBuilder;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;


public class PlantUmlBuilderTest {

    @Test
    public void canGenerateHierarcicalUml() {
        //given:
        State bb = new State("b.b");
        State bab = new State("b.a.b");
        State baa = new State("b.a.a");
        State ba = new Sub("b.a", baa, bab);

        ba.addHandler("b.a to b.b", bb, TransitionKind.External);
        bb.addHandler("b.b to b.a", ba, TransitionKind.External);

        Sub b = new Sub("b", new StateMachine(ba, bb));

        State a = new State("a");

        b.addHandler("b to a", a, TransitionKind.External);
        a.addHandler("a to b", b, TransitionKind.External);
        a.addHandler("a to b.a.b", bab, TransitionKind.External);
        baa.addHandler("b.a.a to a", a, TransitionKind.External);

        StateMachine sm = new StateMachine(a, b);
        sm.init();
        sm.handleEvent("a to b");
        String uml = new PlantUmlBuilder(sm)
                .highlightActiveState()
                .build();

        writeToFile(uml, "superstate.plantuml");
    }

    @Test
    public void simpleExample() {
        State broken = new State("Broken");
        State fixed = new State("Fixed");
        broken.addHandler("Fix", fixed, TransitionKind.External);
        StateMachine stateMachine = new StateMachine(broken, fixed);
        stateMachine.init();
        String plantUml = new PlantUmlBuilder(stateMachine)
                .highlightActiveState()
                .build();
    }
        private void writeToFile(String uml, String fileName) {
        fileName = Paths.get("test-output", fileName).toString();
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
    }
}
