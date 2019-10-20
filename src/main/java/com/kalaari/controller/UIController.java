package com.kalaari.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kalaari.constant.EnumConstants;
import com.kalaari.entity.common.DemandCenterState;
import com.kalaari.entity.common.SimulationOutput;
import com.kalaari.entity.controller.InitialDataResponse;
import com.kalaari.exception.KalaariException;
import com.kalaari.service.DemandCenterService;
import com.kalaari.service.SimulationDataGenerationService;
import com.kalaari.service.SimulationService;
import com.kalaari.util.ContextHolder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping(value = "api/v1/ui")
public class UIController {

    private String map1Html;
    private String map2Html;
    private String map3Html;

    @Autowired
    private SimulationService simulationService;

    @Autowired
    private SimulationDataGenerationService simulationDataGenerationService;

    @GetMapping(value = "/map1", produces = { "text/html" })
    @ResponseBody
    public String getMap1() throws KalaariException, IOException {

        String path = "src/main/resources/Map1.html";
        String ans = readFile(path);
        InitialDataResponse res = simulationDataGenerationService.getInitialData();
        Map<String, InitialDataResponse.InitialDataResponseEntity> nameToData = new HashMap<>();
        for (InitialDataResponse.InitialDataResponseEntity datum : res.getData()) {
            nameToData.put(datum.getName(), datum);
        }
        replaceData(ans, nameToData);
        map1Html = ans;

        Map<EnumConstants.SimulationType, SimulationOutput> data = simulationService.simulate();
        SimulationOutput so1 = data.get(EnumConstants.SimulationType.FCFS);
        SimulationOutput so2 = data.get(EnumConstants.SimulationType.SV);

        path = "src/main/resources/Map2.html";
        ans = readFile(path);
        nameToData = new HashMap<>();
        for (DemandCenterState dcs : so1.getDemandCenterStates()) {
            String name = ContextHolder.getBean(DemandCenterService.class).getDemandCenterById(dcs.getDcId()).getName();
            nameToData.put(name, new InitialDataResponse.InitialDataResponseEntity(dcs.getDcId(), name,
                    dcs.getNoOfVehicles(), dcs.getIdleTimeMins()));
        }
        replaceData(ans, nameToData);
        map2Html = ans;

        path = "src/main/resources/Map3.html";
        ans = readFile(path);
        nameToData = new HashMap<>();
        for (DemandCenterState dcs : so2.getDemandCenterStates()) {
            String name = ContextHolder.getBean(DemandCenterService.class).getDemandCenterById(dcs.getDcId()).getName();
            nameToData.put(name, new InitialDataResponse.InitialDataResponseEntity(dcs.getDcId(), name,
                    dcs.getNoOfVehicles(), dcs.getIdleTimeMins()));
        }
        replaceData(ans, nameToData);
        map3Html = ans;

        return map1Html;
    }

    private void replaceData(String ans, Map<String, InitialDataResponse.InitialDataResponseEntity> nameToData) {
        replaceData(nameToData.get("Whitefield").getIdleWaitMins(), nameToData.get("Whitefield").getCount(),
                nameToData.get("Marathahalli").getIdleWaitMins(), nameToData.get("Marathahalli").getCount(),
                nameToData.get("Sarjapur").getIdleWaitMins(), nameToData.get("Sarjapur").getCount(), ans);
    }

    private void replaceData(Long itm1, Long itm2, Long itm3, Long c1, Long c2, Long c3, String ans) {
        ans.replaceAll("MY_TEXT1", "<h3>Whitefield<br>" + c1 + "<br>" + itm1 + "</h3>");
        ans.replaceAll("MY_LAT1", 12.97772 + "");
        ans.replaceAll("MY_LNG1", 77.741395 + "");
        ans.replaceAll("MY_RADIUS1", 200 + "");
        ans.replaceAll("MY_TEXT2", "<h3>Marathahalli<br>" + c2 + "<br>" + itm2 + "</h3>");
        ans.replaceAll("MY_LAT2", 12.967636 + "");
        ans.replaceAll("MY_LNG2", 77.695034 + "");
        ans.replaceAll("MY_RADIUS2", 200 + "");
        ans.replaceAll("MY_TEXT3", "<h3>Sarjapur<br>" + c3 + "<br>" + itm3 + "</h3>");
        ans.replaceAll("MY_LAT3", 12.859945 + "");
        ans.replaceAll("MY_LNG3", 77.791261 + "");
        ans.replaceAll("MY_RADIUS3", 200 + "");
    }

    private String readFile(String path) throws IOException {
        File file = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String ans = "";
        String st;
        while ((st = br.readLine()) != null)
            ans += st;
        return ans;
    }

    @GetMapping(value = "/map2", produces = { "text/html" })
    @ResponseBody
    public String getMap2() throws KalaariException, IOException {
        return map2Html;
    }

    @GetMapping(value = "/map3", produces = { "text/html" })
    @ResponseBody
    public String getMap3() throws KalaariException, IOException {
        return map3Html;
    }
}