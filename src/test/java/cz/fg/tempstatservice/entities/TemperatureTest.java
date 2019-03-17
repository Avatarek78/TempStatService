package cz.fg.tempstatservice.entities;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class TemperatureTest {

    @Test
    public void checkLombokFunctionality() {
        Temperature temperature = new Temperature();
        Long id = 1L;
        Date date = new Date();
        Float temp = 25.15F;
        temperature.setId(id);
        temperature.setDateAndTime(date);
        temperature.setTemperature(temp);
        assertEquals("Unexpected Id value", id, temperature.getId());
        assertEquals("Unexpected DateAndTime value", date, temperature.getDateAndTime());
        assertEquals("Unexpected temperature value", temp, temperature.getTemperature());
        assertNotNull("Unexpected toString value", temperature.toString());
        assertFalse("Unexpected toString value", temperature.toString().isEmpty());
        Temperature temperature2 = new Temperature();
        temperature2.setId(id+1);
        temperature2.setDateAndTime(date);
        temperature2.setTemperature(temp);
        assertNotEquals("Unexpected result", temperature, temperature2);
    }
}