package de.uniks.stp24.service;

import de.uniks.stp24.dto.JobDto;
import de.uniks.stp24.model.game.Fleet;
import de.uniks.stp24.model.game.Fraction;
import de.uniks.stp24.util.ComparableWrapper;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class FleetService {
    @Inject
    JobService jobService;
    @Inject
    public FleetService() {
    }
    public Observable<Response<JobDto>> moveFleet(Fleet fleet, Fraction destination) {
        List<String> path = findPath(fleet.getLocation(), destination);
        if(fleet.getMoving() != null) {
            return jobService.deleteJob(fleet.getMoving().get_id())
                    .flatMap(job -> jobService.createTravelJob(fleet.get_id(), path));
        }
        return jobService.createTravelJob(fleet.get_id(), path);
    }
    private List<String> findPath(Fraction start, Fraction destination) {
        HashSet<Fraction> visited = new HashSet<>();
        PriorityQueue<ComparableWrapper<List<Fraction>, Integer>> open = new PriorityQueue<>();
        open.add(new ComparableWrapper<>(List.of(start), 0));
        while (!open.isEmpty()) {
            ComparableWrapper<List<Fraction>, Integer> current = open.poll();
            Fraction currentFraction = current.getValue().getLast();
            int distance = current.getPriority();
            if (currentFraction.equals(destination)) {
                return current.getValue().stream().map(Fraction::get_id).toList();
            }
            visited.add(currentFraction);
            for (Fraction neighbor : currentFraction.getLinks()) {
                if (!visited.contains(neighbor)) {
                    List<Fraction> newPath = new LinkedList<>(current.getValue());
                    newPath.add(neighbor);
                    int xOff = neighbor.getX() - destination.getX();
                    int yOff = neighbor.getY() - destination.getY();
                    xOff = xOff*xOff;
                    yOff = yOff*yOff;
                    int pathLength = distance + (int) Math.ceil(Math.sqrt(xOff + yOff));
                    open.add(new ComparableWrapper<>(newPath, pathLength));
                }
            }
        }
        return List.of("");
    }
}
