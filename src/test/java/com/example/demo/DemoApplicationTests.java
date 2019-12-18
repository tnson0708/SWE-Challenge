package com.example.demo;

import com.example.demo.controller.GPSController;
import com.example.demo.domain.*;
import com.example.demo.service.GPSService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(GPSController.class)
public class DemoApplicationTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GPSService gpsService;

    @Test
    public void testUploadFile() throws Exception {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<gpx xmlns=\"http://www.topografix.com/GPX/1/1\" version=\"1.1\" creator=\"OruxMaps v.7.1.6 Donate\">\n" +
                "\t<metadata>\n" +
                "\t\t<name>Test data</name>\n" +
                "\t\t<desc>Description</desc>\n" +
                "\t\t<author></author>\n" +
                "\t\t<link href=\"http://www.oruxmaps.com\">\n" +
                "\t\t\t<text>OruxMaps</text>\n" +
                "\t\t</link>\n" +
                "\t\t<time>2017-10-22T09:41:33Z</time>\n" +
                "\t</metadata>\n" +
                "\t<wpt lat=\"42.2205377\" lon=\"-1.4564538\">\n" +
                "\t\t<name>Sorteamos por arriba</name>\n" +
                "\t\t<sym>/static/wpt/Waypoint</sym>\n" +
                "\t</wpt>\n" +
                "\n" +
                "\t<trk>\n" +
                "\t\t<trkseg>\n" +
                "\t\t\t<trkpt lat=\"42.2208895\" lon=\"-1.4580696\">\n" +
                "\t\t\t\t<ele>315.86</ele>\n" +
                "\t\t\t\t<time>2017-10-22T09:41:38Z</time>\n" +
                "\t\t\t</trkpt>\n" +
                "\n" +
                "\t\t</trkseg>\n" +
                "\t</trk>\n" +
                "</gpx>";
        GPS gps = mock(GPS.class);
        String testFileName = "testFile.gpx";
        File rootFile = new File(".");
        String testFileLocation = rootFile.getAbsolutePath().substring(0, rootFile.getAbsolutePath().length() - 1) + testFileName;
        File testFile = new File(testFileLocation);
        testFile.delete();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", testFileName,
                "multipart/form-data", testData.getBytes());
        Files.write(Paths.get(testFileLocation), testData.getBytes());
        doNothing().when(gpsService).saveGPS(gps);
        this.mockMvc.perform(fileUpload("/api/upload").file(mockMultipartFile)).andExpect(status().isOk());
        Assert.assertTrue(testFile.exists());
        testFile.delete();
    }

    @Test
    public void testUploadFileWhenFileIsEmpty() throws Exception {
        String testData = "";
        GPS gps = mock(GPS.class);
        String testFileName = "testFile.gpx";
        File rootFile = new File(".");
        String testFileLocation = rootFile.getAbsolutePath().substring(0, rootFile.getAbsolutePath().length() - 1) + testFileName;
        File testFile = new File(testFileLocation);
        testFile.delete();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "",
                "multipart/form-data", testData.getBytes());
        Files.write(Paths.get(testFileLocation), testData.getBytes());

        doNothing().when(gpsService).saveGPS(gps);
        ResultActions perform = this.mockMvc.perform(fileUpload("/api/upload").file(mockMultipartFile));
        perform.andExpect(status().isOk());
        perform.andExpect(content().string("Please select a file!"));
        Assert.assertTrue(testFile.exists());
        testFile.delete();
    }

    @Test
    public void testUploadFileWhenBadRequest() throws Exception {
        String testData = "test data";
        GPS gps = mock(GPS.class);
        String testFileName = "testFile.gpx";
        File rootFile = new File(".");
        String testFileLocation = rootFile.getAbsolutePath().substring(0, rootFile.getAbsolutePath().length() - 1) + testFileName;
        File testFile = new File(testFileLocation);
        testFile.delete();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "",
                "multipart/form-data", testData.getBytes());
        Files.write(Paths.get(testFileLocation), testData.getBytes());
        doNothing().when(gpsService).saveGPS(gps);
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getBytes()).thenThrow(new IOException());
        ResultActions perform = this.mockMvc.perform(fileUpload("/api/upload").file(mockMultipartFile));
        perform.andExpect(status().isBadRequest());
        Assert.assertTrue(testFile.exists());
        testFile.delete();
    }

    @Test
    public void getGPSById() throws Exception {
        GPS gps = createGPS();
        given(gpsService.getGPSById(1)).willReturn(gps);
        ResultActions perform = this.mockMvc.perform(get("/api/getgps?id=1"));
        perform.andExpect(status().isOk());
        perform.andExpect(content().json("{'gpsId':1,'creator':'Son Tran','version':'1.1','metadata':{'id':2,'name':'Ho Chi Minh','description':'A good place','author':{'name':'Son Tran'},'links':null,'time':'2019-12-18T10:00:00.000001234+07:00'}," +
                "'wayPoints':[{'latitude':5678.0,'longitude':1234.0,'name':'Thao Dien','symbol':null,'elevation':123,'time':'2019-12-18T10:00:00.000001234+07:00'}]," +
                "'tracks':[{'trackSegments':[{'wayPoints':[{'latitude':2222.0,'longitude':1111.0,'name':'Xuan Thuy','symbol':null,'elevation':333,'time':'2019-12-18T10:00:00.000001234+07:00'}," +
                "{'latitude':6666.0,'longitude':5555.0,'name':'Quan 2','symbol':null,'elevation':444,'time':'2019-12-18T11:00:00.000001234+07:00'}]}]}]}"));
    }

    @Test
    public void getLatestGPSById() throws Exception {
        GPS gps = createGPS();
        given(gpsService.getGPSById(1)).willReturn(gps);
        ResultActions perform = this.mockMvc.perform(get("/api/getgps/latest?id=1"));
        perform.andExpect(status().isOk());
        perform.andExpect(content().json("[{'latitude':6666.0,'longitude':5555.0,'name':'Quan 2','symbol':null,'elevation':444,'time':'2019-12-18T11:00:00.000001234+07:00'}," +
                "{'latitude':2222.0,'longitude':1111.0,'name':'Xuan Thuy','symbol':null,'elevation':333,'time':'2019-12-18T10:00:00.000001234+07:00'}]"));
    }

    private GPS createGPS() {
        GPS gps = new GPS();
        gps.setGpsId(1);
        gps.setVersion("1.1");
        gps.setCreator("Son Tran");
        Metadata metadata = new Metadata();
        ZoneId zoneId = ZoneId.of("UTC+7");
        ZonedDateTime zonedDateTime = ZonedDateTime.of(2019, 12, 18, 10, 00, 00, 1234, zoneId);
        metadata.setTime(zonedDateTime);
        metadata.setName("Ho Chi Minh");
        metadata.setDescription("A good place");
        metadata.setId(2);
        Person author = new Person();
        author.setName("Son Tran");
        metadata.setAuthor(author);
        gps.setMetadata(metadata);
        List<WayPoint> wayPoints = new ArrayList<>();
        WayPoint wayPoint = new WayPoint();
        wayPoint.setName("Thao Dien");
        wayPoint.setTime(zonedDateTime);
        wayPoint.setElevation(123L);
        wayPoint.setLongitude(1234D);
        wayPoint.setLatitude(5678D);
        wayPoints.add(wayPoint);
        gps.setWayPoints(wayPoints);
        List<Track> tracks = new ArrayList<>();
        Track track = new Track();
        List<TrackSegment> trackSegments = new ArrayList<>();
        TrackSegment trackSegment = new TrackSegment();
        List<WayPoint> wayPointList = new ArrayList<>();

        WayPoint wayPoint1 = new WayPoint();
        wayPoint1.setName("Xuan Thuy");
        ZonedDateTime zonedDateTime1 = ZonedDateTime.of(2019, 12, 18, 10, 00, 00, 1234, zoneId);
        wayPoint1.setTime(zonedDateTime1);
        wayPoint1.setElevation(333L);
        wayPoint1.setLongitude(1111D);
        wayPoint1.setLatitude(2222D);

        WayPoint wayPoint2 = new WayPoint();
        wayPoint2.setName("Quan 2");
        ZonedDateTime zonedDateTime2 = ZonedDateTime.of(2019, 12, 18, 11, 00, 00, 1234, zoneId);
        wayPoint2.setTime(zonedDateTime2);
        wayPoint2.setElevation(444L);
        wayPoint2.setLongitude(5555D);
        wayPoint2.setLatitude(6666D);

        wayPointList.add(wayPoint1);
        wayPointList.add(wayPoint2);
        trackSegment.setWayPoints(wayPointList);
        trackSegments.add(trackSegment);
        track.setTrackSegments(trackSegments);
        tracks.add(track);
        gps.setTracks(tracks);
        return gps;
    }
}
