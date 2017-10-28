package hu.bearmaster.minecraftstarter.server.service;

import static hu.bearmaster.minecraftstarter.server.model.MapName.mapName;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import hu.bearmaster.minecraftstarter.server.model.MapName;

@RunWith(SpringRunner.class)
@Import(MapNameGeneratorService.class)
@TestPropertySource("/application.properties")
public class MapNameGeneratorServiceTest {

    @Autowired
    private MapNameGeneratorService sut;

    @Test
    public void testInjectedProperly() {
        assertThat(sut).isNotNull();
    }

    @Test
    public void testParseWithMatchingValue() {
        MapName name = mapName("spi-islands", LocalDate.of(2017, 5, 16), "zip");
        assertThat(sut.parse("spi-islands-2017-05-16.zip")).hasValue(name);
    }

    @Test
    public void testParseWithNotMatchingPrefix() {
        assertThat(sut.parse("spi-test-2017-05-16.zip")).isEmpty();
    }

    @Test
    public void testParseWithNotMatchingExtension() {
        assertThat(sut.parse("spi-islands-2017-05-16.7z")).isEmpty();
    }

    @Test
    public void testParseWithNotMatchingDateFormat() {
        assertThat(sut.parse("spi-islands-20170516.zip")).isEmpty();
    }

}