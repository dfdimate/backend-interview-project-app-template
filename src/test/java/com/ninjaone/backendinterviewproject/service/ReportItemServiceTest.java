package com.ninjaone.backendinterviewproject.service;

import com.ninjaone.backendinterviewproject.database.DeviceRepository;
import com.ninjaone.backendinterviewproject.database.ITServiceRepository;
import com.ninjaone.backendinterviewproject.exceptions.DeviceFeeNotConfiguredException;
import com.ninjaone.backendinterviewproject.model.Device;
import com.ninjaone.backendinterviewproject.model.ITService;
import com.ninjaone.backendinterviewproject.model.Report;
import com.ninjaone.backendinterviewproject.model.ReportItem;
import com.ninjaone.backendinterviewproject.model.enums.DeviceType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReportItemServiceTest {

    @Mock
    private ITServiceRepository serviceRepository;

    @Mock
    private DeviceRepository deviceRepository;
    @InjectMocks
    private  ReportService reportService;

    @Test
    public void givenAnEmptyCacheAndAServiceListWithJustDeviceFeeAndNoDeviceWhenTryingToGetTheReportThenDoTheCalculation() throws DeviceFeeNotConfiguredException {
        //GIVEN
        ArrayList<ITService> services = new ArrayList<>();
        services.add(new ITService(132L, "DEVICE FEE", "Description", 4.0, List.of(DeviceType.ANY), Collections.emptySet()));

        when(serviceRepository.findAll()).thenReturn(services);
        when(deviceRepository.count()).thenReturn(0L);
        //WHEN
        Report response = reportService.getTotalAmount();
        //THEN
        Mockito.verify(serviceRepository, Mockito.times(1)).findAll();
        Assertions.assertEquals(0.0, response.getTotalCost());
    }

    @Test
    public void givenAnEmptyCacheAndAServiceListWithJustDeviceFeeButOneDeviceWhenTryingToGetTheReportThenDoTheCalculation() throws DeviceFeeNotConfiguredException {
        //GIVEN
        ArrayList<ITService> services = new ArrayList<>();


        services.add(new ITService(132L, "DEVICE FEE", "Description", 4.0, List.of(DeviceType.ANY), Collections.emptySet()));

        when(serviceRepository.findAll()).thenReturn(services);
        when(deviceRepository.count()).thenReturn(1L);
        //WHEN
        Report response = reportService.getTotalAmount();
        //THEN
        Mockito.verify(serviceRepository, Mockito.times(1)).findAll();
        Assertions.assertEquals(4.0, response.getTotalCost());
    }

    @Test
    public void givenAnEmptyCacheAndAServiceListWithDifferentItemsAndDevicesServicedThenGetTheReportWithTheTotalCostAndPerItemCost() throws DeviceFeeNotConfiguredException {
        //GIVEN
        ArrayList<ITService> services = new ArrayList<>();
        HashSet<Device> devices = new HashSet<>();

        devices.add(new Device(1L, DeviceType.ANY, "Personal Computer", Collections.emptySet()));
        devices.add(new Device(2L, DeviceType.ANY, "Personal Computer 2", Collections.emptySet()));

        services.add(new ITService(132L, "DEVICE FEE", "Description", 4.0, List.of(DeviceType.ANY), Collections.emptySet()));
        services.add(new ITService(132L, "ANTIVIRUS", "Description", 10.0, List.of(DeviceType.ANY), devices));

        when(serviceRepository.findAll()).thenReturn(services);
        when(deviceRepository.count()).thenReturn(2L);
        //WHEN
        Report response = reportService.getTotalAmount();
        //THEN
        Mockito.verify(serviceRepository, Mockito.times(1)).findAll();
        Assertions.assertEquals(28.0, response.getTotalCost());
        Assertions.assertEquals(2, response.getReportItemList().size());

        Assertions.assertEquals("ANTIVIRUS", response.getReportItemList().get(0).getServiceName());
        Assertions.assertEquals(20.0, response.getReportItemList().get(0).getServiceTotal());
        Assertions.assertEquals("DEVICE FEE", response.getReportItemList().get(1).getServiceName());
        Assertions.assertEquals(8.0, response.getReportItemList().get(1).getServiceTotal());
    }

    @Test
    public void givenAFilledCacheWhenTryingToGetTheReportThenUseTheCache() throws DeviceFeeNotConfiguredException {
        //GIVEN
        HashMap<String, Report> table = new HashMap<String, Report>();
        Report testReport = new Report(0.0, Collections.emptyList());
        table.put("total", testReport);

        ReflectionTestUtils.setField(reportService, "cache", table );
        //WHEN
        Report response = reportService.getTotalAmount();
        //THEN
        Mockito.verify(serviceRepository, Mockito.times(0)).findAll();

        Assertions.assertEquals(testReport, response);
    }
}
