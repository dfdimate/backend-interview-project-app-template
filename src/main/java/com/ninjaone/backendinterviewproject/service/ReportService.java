package com.ninjaone.backendinterviewproject.service;

import com.ninjaone.backendinterviewproject.database.DeviceRepository;
import com.ninjaone.backendinterviewproject.database.ITServiceRepository;
import com.ninjaone.backendinterviewproject.exceptions.DeviceFeeNotConfiguredException;
import com.ninjaone.backendinterviewproject.model.Device;
import com.ninjaone.backendinterviewproject.model.ITService;
import com.ninjaone.backendinterviewproject.model.Report;
import com.ninjaone.backendinterviewproject.model.ReportItem;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    /**
        Small rudimentary cache, HashMap in memory of the application
     */
    private final HashMap<String, Report> cache = new HashMap<>();
    private final DeviceRepository repository;

    private static final String DEVICE_FEE_SERVICE_NAME = "DEVICE FEE";
    private final ITServiceRepository itServiceRepository;

    public ReportService(DeviceRepository repository, ITServiceRepository itServiceRepository) {
        this.repository = repository;
        this.itServiceRepository = itServiceRepository;
    }

    /**
     * Retrieves the total amount for the current database state, checks for existing Cache or revalidates
     * @return The amount of the cost of the services provided
     */
    public Report getTotalAmount() throws DeviceFeeNotConfiguredException {
        if(!cache.containsKey("total")) {
            resetCache();
        }

        return cache.get("total");
    }

    /**
     * Resets cache on demand, re-queries.
     */
    public void resetCache() throws DeviceFeeNotConfiguredException {
        cache.remove("total");
        cache.put("total",  getTotalFromServices());
    }

    public Report getTotalFromServices() throws DeviceFeeNotConfiguredException {
        List<ITService> items = itServiceRepository.findAll();
         long devices = repository.count();
        if(items.isEmpty()) {
            return new Report(0.0, Collections.emptyList());
        }

        ITService deviceFee = items.stream().filter( i -> i.getName().equals(DEVICE_FEE_SERVICE_NAME)).findFirst().orElseThrow(DeviceFeeNotConfiguredException::new);
        System.out.println(items);
        items.remove(deviceFee);
        List<ReportItem> itemsFactured = items.stream().map( s -> new ReportItem(s.getName(), s.getDevicesServiced().size()*s.getCost())).collect(Collectors.toList());
        itemsFactured.add(new ReportItem(DEVICE_FEE_SERVICE_NAME, devices * deviceFee.getCost() ));
        return new Report( itemsFactured.stream().mapToDouble(ReportItem::getServiceTotal).sum(), itemsFactured );
    }
}
