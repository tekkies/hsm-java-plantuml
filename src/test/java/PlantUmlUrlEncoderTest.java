import org.junit.Assert;
import org.junit.Test;

import uk.co.tekkies.hsm.plantuml.PlantUmlUrlEncoder;

public class PlantUmlUrlEncoderTest {

    @Test
    public void canEncodeDiagram() {
        String plantUml = "@startuml\nstate \"Enabled\" as Enabled  {\n    state \"Adapter Disabled\" as Adapter_Disabled  {\n    }\n    state \"ENABLING_ADAPTER\" as ENABLING_ADAPTER  {\n    }\n    state \"Adapter Enabled\" as Adapter_Enabled  {\n        state \"Found\" as Found  {\n        }\n        state \"Scanning\" as Scanning  {\n        }\n    }\n    state \"Init\" as Init  {\n    }\n}\nstate \"Disabled\" as Disabled  {\n}\nAdapter_Disabled --> ENABLING_ADAPTER : Connect_to_board\nAdapter_Disabled --> Adapter_Enabled : Adapter_Enabled\nENABLING_ADAPTER --> Adapter_Enabled : Adapter_Enabled\nENABLING_ADAPTER --> Adapter_Disabled : Adapter_Disabled\nScanning --> Found : Found\nAdapter_Enabled --> Adapter_Disabled : Adapter_Disabled\nInit --> Adapter_Enabled : Adapter_Enabled\nInit --> Adapter_Disabled : Adapter_Disabled\nEnabled --> Disabled : Disable\nDisabled --> Enabled : Enable\n \n@enduml";
        String url = new PlantUmlUrlEncoder().getUrl(plantUml);

        String expected = "https://www.plantuml.com/plantuml/png/UDgjKq4EWZ0GlEyhDjpv00S32XeIGunw9rKQGu9R0-La-Bj0APRM60zowNGpipEPXh2Hh9RjlO8UI8vUGknIyS93rk244P-0_JSnee8z9AynBfitTHhcUgWrdQbCicYzJxDT7iNHuPmSbOar_AZKdcOwRMcbD5HRqPBYZiXaT3RtT6L49Tr6khwuYaMgb4evyWVmpjtfBXSDcSrquFJb-oktYW0tWeXVPIv5VX6iBZuBxIe2UmBEwZ_8vWY1CuAvmK6WsW_K2RRhhol7ad-BxL2_xJLp6Bm9mlA5Pak5027aLFJ_pmjKVWR7";
        Assert.assertTrue(url.equals(expected));
    }


}
