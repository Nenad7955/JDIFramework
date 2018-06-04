package epam;


import com.epam.jdi.uitests.web.selenium.driver.ScreenshotMaker;
import com.epam.jdi.uitests.web.selenium.elements.composite.WebSite;
import com.epam.jdi.uitests.web.testng.testRunner.TestNGBase;
import com.epam.web.matcher.base.DoScreen;
import com.epam.web.matcher.testng.Assert;
import org.apache.commons.io.output.ByteArrayOutputStream;
import ru.yandex.qatools.allure.annotations.Attachment;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Stories;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.annotations.*;
import epam.entities.Data;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static com.epam.jdi.uitests.core.settings.JDISettings.driverFactory;
import static com.epam.jdi.uitests.core.settings.JDISettings.logger;
//import static com.epam.web.matcher.base.DoScreen;
import static epam.EpamWebsite.login;
import static epam.EpamWebsite.*;
import static org.apache.commons.io.IOUtils.toByteArray;

@Listeners//(epam.allure.allure.class)
@Features({"Testing"})
@Stories({"the test"})
public class EpamTest extends TestNGBase {

    @DataProvider
    public static Object[] dataProvider() throws IOException, ParseException {

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("c:\\test.json"));
        JSONObject jsonObject = (JSONObject) obj; //main object, containing all 5 data

        JSONObject[] data = new JSONObject[5];
        Data[] dat = new Data[5];
        for (int i = 0; i < data.length; i++) {

            data[i] = (JSONObject) jsonObject.get("data_" + Integer.toString(i + 1));
            dat[i] = new Data();

            JSONArray summary = (JSONArray) data[i].get("summary");
            for (int index = 0; index < dat[i].summary.length; index++)
                dat[i].summary[index] = (Long) summary.get(index);

            JSONArray elements = (JSONArray) data[i].get("elements");
            dat[i].elements = new String[elements.size()];
            for (int index = 0; index < dat[i].elements.length; index++)
                dat[i].elements[index] = (String) elements.get(index);

            dat[i].color = (String) data[i].get("color");
            dat[i].metals = (String) data[i].get("metals");

            JSONArray vegetables = (JSONArray) data[i].get("vegetables");

            dat[i].vegetables = new String[vegetables.size()];
            for (int index = 0; index < dat[i].vegetables.length; index++) {
                dat[i].vegetables[index] = (String) vegetables.get(index);
                if (dat[i].vegetables[index].equals("Salad"))
                    dat[i].vegetables[index] = "Vegetables";
            }
        }
        return dat;
    }


    @BeforeClass(alwaysRun = true)
    public static void setUp() {
        WebSite.init(EpamWebsite.class);
        logger.info("Run Tests");
        driverFactory.getDriver();
    }


    @Test
    public void loginTest() {
        homePage.open();
        login();
        homePage.checkOpened();


        metalsAndColors.open();

        //deselecting default set value
        metalsAndColors.vegetables.select("Vegetables");

        System.out.println("*****************************");
        System.out.println(metalsAndColors.radioButtons.getValues());
        System.out.println("*****************************");

        System.out.println("*****************************");
        System.out.println(metalsAndColors.elements.getValues());
        System.out.println("*****************************");


        System.out.println("*****************************");
        System.out.println(metalsAndColors.colors.getValues());
        System.out.println("*****************************");

        System.out.println("*****************************");
        System.out.println(metalsAndColors.vegetables.getValues());
        System.out.println("*****************************");

        System.out.println("*****************************");
        System.out.println(metalsAndColors.metals.getValues());
        System.out.println("*****************************");


    }

    @Attachment(type = "image/png")
    public byte[] scr() throws IOException {

        String screenLocation = ScreenshotMaker.takeScreen();
        //System.out.println(screenLocation);
        //BufferedImage bufferedImage = ImageIO.read(new File(screenLocation));

        //      WritableRaster raster = bufferedImage .getRaster();
        //    DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();


        BufferedImage bImage = ImageIO.read(new File(screenLocation));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "png", bos);
        byte[] data = bos.toByteArray();
        return data;
        //return ( data.getData() );
    }

    @Test(dataProvider = "dataProvider")
    public void testWithData(Data data) {
        metalsAndColors.fillMetalsAndColorsForm(data);
        metalsAndColors.validate(data);
        try {
            scr();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
