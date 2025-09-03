package de.uniks.stp24.service;

import de.uniks.stp24.dto.CreateJobDto;
import de.uniks.stp24.dto.JobDto;
import de.uniks.stp24.model.game.Fraction;
import de.uniks.stp24.model.game.Jobs;
import de.uniks.stp24.rest.JobApiService;
import io.reactivex.rxjava3.core.Observable;
import org.fulib.fx.annotation.controller.Resource;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.List;
import java.util.ResourceBundle;

public class JobService {
    @Inject
    JobApiService jobApiService;
    @Inject
    GameService gameService;
    @Inject
    @Resource
    ResourceBundle resources;

    @Inject
    TechnologiesServices technologiesServices;

    @Inject
    public JobService() {
    }

    public Observable<Response<JobDto>> createUpgradingJob(String fraction) {
        return jobApiService.createJob(
                gameService.getGame().getId(),
                gameService.getOwnUser().getEmpire().get_id(),
                new CreateJobDto(
                        fraction,
                        0, // highest priority
                        "upgrade",
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                ));
    }

    public Observable<Response<JobDto>> createDistrictJob(String fraction, String district) {
        return jobApiService.createJob(
                gameService.getGame().getId(),
                gameService.getOwnUser().getEmpire().get_id(),
                new CreateJobDto(
                        fraction,
                        0, // highest priority
                        "district",
                        null,
                        district,
                        null,
                        null,
                        null,
                        null
                ));
    }

    public Observable<Response<JobDto>> deleteJob(String jobID) {
        return jobApiService.deleteJob(
                gameService.getGame().getId(),
                gameService.getOwnUser().getEmpire().get_id(),
                jobID);
    }

    public String findJobIdByDistrictName(String districtName, Fraction fraction) {
        for (Jobs job : fraction.getJobs()) {
            if ("district".equals(job.getType()) && districtName.equals(job.getDistrict())) {
                return job.get_id();
            }
        }
        return null;
    }

    public Observable<Response<JobDto>> createBuildingJob(String fraction, String building) {
        return jobApiService.createJob(
                gameService.getGame().getId(),
                gameService.getOwnUser().getEmpire().get_id(),
                new CreateJobDto(
                        fraction,
                        0, // highest priority
                        "building",
                        building,
                        null,
                        null,
                        null,
                        null,
                        null
                ));
    }

    public Observable<Response<JobDto>> createTechnologyJob(String fraction, String technology) {
        return jobApiService.createJob(
                gameService.getGame().getId(),
                gameService.getOwnUser().getEmpire().get_id(),
                new CreateJobDto(
                        fraction,
                        0, // highest priority
                        "technology",
                        null,
                        null,
                        technology,
                        null,
                        null,
                        null
                ));
    }

    public Observable<Response<JobDto>> createTravelJob(String fleet, List<String> path) {
        return jobApiService.createJob(
                gameService.getGame().getId(),
                gameService.getOwnUser().getEmpire().get_id(),
                new CreateJobDto(
                        null,
                        0, // highest priority
                        "travel",
                        null,
                        null,
                        null,
                        fleet,
                        null,
                        path
                ));
    }

    public Observable<Response<JobDto>> createShipJob(String fleet, String system, String type) {
        return jobApiService.createJob(
                gameService.getGame().getId(),
                gameService.getOwnUser().getEmpire().get_id(),
                new CreateJobDto(
                        system,
                        0, // highest priority
                        "ship",
                        null,
                        null,
                        null,
                        fleet,
                        type,
                        null
                ));
    }

}
