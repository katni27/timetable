package org.acme.timetabling.rest;

import org.acme.timetabling.domain.CourseSchedule;
import org.acme.timetabling.persistence.CourseScheduleRepository;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.solver.SolutionManager;
import org.optaplanner.core.api.solver.SolverManager;
import org.optaplanner.core.api.solver.SolverStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/timeTable")
public class CourseScheduleController {

    @Autowired
    private CourseScheduleRepository courseScheduleRepository;
    @Autowired
    private SolverManager<CourseSchedule, Long> solverManager;
    @Autowired
    private SolutionManager<CourseSchedule, HardSoftScore> solutionManager;

    // To try, GET http://localhost:8080/timeTable
    @GetMapping()
    public CourseSchedule getTimeTable() {
        // Get the solver status before loading the solution
        // to avoid the race condition that the solver terminates between them
        SolverStatus solverStatus = getSolverStatus();
        CourseSchedule solution = courseScheduleRepository.findById(CourseScheduleRepository.SINGLETON_TIME_TABLE_ID);
        solutionManager.update(solution); // Sets the score
        solution.setSolverStatus(solverStatus);
        return solution;
    }

    @PostMapping("/solve")
    public void solve() {
        solverManager.solveAndListen(CourseScheduleRepository.SINGLETON_TIME_TABLE_ID,
                courseScheduleRepository::findById,
                courseScheduleRepository::save);
    }

    public SolverStatus getSolverStatus() {
        return solverManager.getSolverStatus(CourseScheduleRepository.SINGLETON_TIME_TABLE_ID);
    }

    @PostMapping("/stopSolving")
    public void stopSolving() {
        solverManager.terminateEarly(CourseScheduleRepository.SINGLETON_TIME_TABLE_ID);
    }

}
