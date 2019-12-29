import de.artcom.hsm.*;
import org.junit.Test;
import uk.co.tekkies.hsm.plantuml.PlantUmlBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;


public class MyTestClass {

    @Test
    public void canGenerateHierarcicalUml() {
        //given:
        State loud = new State("loud");
        State quiet = new Sub("quiet", new State("quiet a"), new State("quiet b"));

        quiet.addHandler("volume_up", loud, TransitionKind.External);
        loud.addHandler("volume_down", quiet, TransitionKind.External);

        Sub on = new Sub("on", new StateMachine(quiet, loud));

        State off = new State("off");

        on.addHandler("switched_off", off, TransitionKind.External);
        off.addHandler("switched_on", on, TransitionKind.External);

        StateMachine sm = new StateMachine(off, on);
        sm.init();
        String uml = new PlantUmlBuilder(sm).generateUml();

        writeToFile(uml, "test-output"+ File.separator+"superstate.plantuml");
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
    }
}
