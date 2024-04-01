package dev.notenger.clients.telematics;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "telematics",
        url = "${clients.telematics.url}"
)
// todo: RequestMapping?
public interface TelematicsClient {

    @PostMapping(path = "api/v1/telematics/create-warehouse-agent")
    void createWarehouseAgent(@RequestBody CreateWarehouseAgentRequest request);

    @PostMapping("api/v1/telematics/init-warehouses")
    void initWarehouseAgents(@RequestBody List<CreateWarehouseAgentRequest> request);

    @PostMapping(path = "api/v1/telematics/register-vehicle-agent")
    void registerVehicleAgent(@RequestBody RegisterVehicleAgentRequest request);

    @PostMapping(path = "api/v1/telematics/create-store-agent")
    void createStoreAgent(@RequestBody CreateStoreAgentRequest request);

    @PutMapping("api/v1/telematics/{vehicleId}")
    void updateVehicleAgent(@PathVariable("vehicleId") int vehicleId, @RequestBody UpdateVehicleAgentRequest updateRequest);

    @DeleteMapping("api/v1/telematics/{vehicleId}")
    void deleteVehicleAgent(@PathVariable("vehicleId") int vehicleId);

}
