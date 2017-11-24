package ucl.cs.camera;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

public class CameraTest {

    private static final byte[] IMAGE = new byte[4];
    @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  Sensor sensor = context.mock(Sensor.class);
  MemoryCard memoryCard = context.mock(MemoryCard.class);

  Camera camera = new Camera(sensor, memoryCard);

  @Test
  public void switchingTheCameraOnPowersUpTheSensor() {

    context.checking(new Expectations(){{
        exactly(1).of(sensor).powerUp();
    }});

    // write your test here
    camera.powerOn();
  }

    @Test
    public void switchingTheCameraOffPowersOffTheSensor() {

        context.checking(new Expectations(){{
            ignoring(sensor).powerUp();
            exactly(1).of(sensor).powerDown();
        }});

        // write your test here
        camera.powerOn();
        camera.powerOff();
    }

    @Test
    public void pressingTheShutterWithPowerOnCopiesTheDatFromSensorToMemoryCard() {

        context.checking(new Expectations(){{
            ignoring(sensor).powerUp();
            exactly(1).of(sensor).readData(); will(returnValue(IMAGE));
            exactly(1).of(memoryCard).write(IMAGE);
        }});

        // write your test here
        camera.powerOn();
        camera.pressShutter();
    }

    @Test
    public void pressingTheShutterWithPowerOffDoesNothing() {

        context.checking(new Expectations(){{
            never(sensor);
            never(memoryCard);
        }});

        // write your test here

        camera.pressShutter();
    }

    @Test
    public void doesNotPowerDownTheSensorUntilWritingIsComplete() {

        context.checking(new Expectations(){{
            ignoring(sensor).powerUp();
            exactly(1).of(sensor).readData(); will(returnValue(IMAGE));
            exactly(1).of(memoryCard).write(IMAGE);
        }});

        // write your test here
        camera.powerOn();
        camera.pressShutter();

        camera.powerOff();

        context.checking(new Expectations(){{
            exactly(1).of(sensor).powerDown();
        }});

        camera.writeComplete();
    }


}
